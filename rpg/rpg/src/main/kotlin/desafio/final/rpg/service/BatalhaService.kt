package desafio.final.rpg.service

import desafio.final.rpg.model.Batalha
import desafio.final.rpg.model.Ladino
import desafio.final.rpg.model.Personagem
import desafio.final.rpg.model.Sacerdote
import desafio.final.rpg.model.AcaoRedeDTO
import desafio.final.rpg.model.ResultadoRoundDTO
import desafio.final.rpg.model.StatusBatalhaDTO
import desafio.final.rpg.model.StatusPersonagemDTO
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import desafio.final.rpg.repository.BatalhaRepository
import desafio.final.rpg.repository.PersonagemRepository
import java.lang.RuntimeException
import org.springframework.web.client.RestClient
import org.springframework.beans.factory.annotation.Value

@Service
class BatalhaService(
    private val personagemRepository: PersonagemRepository,
    private val batalhaRepository: BatalhaRepository,
    restClientBuilder: RestClient.Builder
) {
    private val restClient: RestClient = restClientBuilder.build()

    // Inicia a batalha no banco
    @Transactional
    fun iniciarNovaBatalha(id1: Long, id2: Long): Batalha {
        val p1 = personagemRepository.findById(id1).get()
        val p2 = personagemRepository.findById(id2).get()

        val batalha = Batalha(personagem1 = p1, personagem2 = p2)
        batalha.adicionarLog("Início do combate: ${p1.nome} vs ${p2.nome}")
        return batalhaRepository.save(batalha)
    }

    // Iniciativa Aleatória
    @Transactional
    fun processarRound(batalhaId: Long, acaoP1: String, acaoP2: String): Batalha {
        val batalha = batalhaRepository.findById(batalhaId).orElseThrow { RuntimeException("Batalha não encontrada") }
        val p1 = batalha.personagem1
        val p2 = batalha.personagem2

        if (batalha.encerrada) return batalha

        // Rolar Iniciativa (Velocidade + Dado 1-20)
        val initP1 = p1.velocidade + (1..20).random()
        val initP2 = p2.velocidade + (1..20).random()

        batalha.adicionarLog("Iniciativa: ${p1.nome}($initP1) | ${p2.nome}($initP2)")

        // Define a ordem de quem age primeiro
        if (initP1 >= initP2) {
            executarAcao(p1, p2, acaoP1, batalha)
            if (p2.vida > 0) executarAcao(p2, p1, acaoP2, batalha)
        } else {
            executarAcao(p2, p1, acaoP2, batalha)
            if (p1.vida > 0) executarAcao(p1, p2, acaoP1, batalha)
        }

        // Verifica se alguém morreu
        verificarFimDeJogo(batalha)

        // Salva o estado atual dos personagens e da batalha no Postgres
        personagemRepository.saveAll(listOf(p1, p2))
        return batalhaRepository.save(batalha)
    }

    private fun executarAcao(ativo: Personagem, alvo: Personagem, acao: String, batalha: Batalha) {
        when (acao.uppercase()) {
            "ATACAR" -> {
                batalha.adicionarLog("${ativo.nome} atacou!")
                ativo.atacar(alvo)
            }
            "PODER" -> {
                batalha.adicionarLog("${ativo.nome} usou seu PODER ESPECIAL!")
                // Polimorfismo: o Kotlin chama o poder específico de cada classe
                ativo.usarPoder(alvo)

            }
            "DEFENDER" -> {
                batalha.adicionarLog("${ativo.nome} preparou defesa!")
                ativo.defender(alvo) // Usando sua lógica de esquiva/velocidade
            }
        }
    }

    private fun verificarFimDeJogo(batalha: Batalha) {
        if (batalha.personagem1.vida <= 0) {
            batalha.nomeVencedor = batalha.personagem2.nome
            batalha.encerrada = true
            batalha.adicionarLog("VITÓRIA DE ${batalha.personagem2.nome}!")
        } else if (batalha.personagem2.vida <= 0) {
            batalha.nomeVencedor = batalha.personagem1.nome
            batalha.encerrada = true
            batalha.adicionarLog("VITÓRIA DE ${batalha.personagem1.nome}!")
        }
    }

    @Value("\${rival.url}")
    private lateinit var defaultUrlRival: String



    // --- NOVA LÓGICA HOST-CLIENT ---

    @Transactional
    fun registrarAcaoRival(idBatalha: Long, acao: String): String {
        val batalha = batalhaRepository.findById(idBatalha).orElse(null)
            ?: return "Batalha não encontrada"

        if (batalha.encerrada) return "A batalha já está encerrada"

        batalha.acaoPendenteRival = acao
        batalhaRepository.save(batalha)
        return "Ação do rival registrada. Aguardando ação do host."
    }

    @Transactional
    fun processarRoundHost(idBatalha: Long, acaoHost: String, urlCliente: String? = null): Any {
        val batalha = batalhaRepository.findById(idBatalha).orElseThrow { RuntimeException("Batalha não encontrada") }

        val acaoRival = batalha.acaoPendenteRival
        if (acaoRival == null) {
            return "Aguardando jogador rival enviar a ação dele."
        }

        // Processa o round com as duas ações
        val batalhaAtualizada = processarRound(idBatalha, acaoHost, acaoRival)
        
        // Limpa a ação pendente para o próximo turno
        batalhaAtualizada.acaoPendenteRival = null
        batalhaRepository.save(batalhaAtualizada)

        // Envia o resultado para o cliente
        val urlFinal = urlCliente ?: defaultUrlRival
        val resultadoDTO = ResultadoRoundDTO(
            idBatalha = idBatalha,
            vidaP1 = batalhaAtualizada.personagem1.vida, // Vida do Host
            vidaP2 = batalhaAtualizada.personagem2.vida, // Vida do Cliente (rival para o host)
            logRound = batalhaAtualizada.logDescritivo,
            encerrada = batalhaAtualizada.encerrada,
            vencedor = batalhaAtualizada.nomeVencedor
        )

        try {
            restClient.post()
                .uri("$urlFinal/batalha/sincronizar-resultado")
                .body(resultadoDTO)
                .retrieve()
                .body(String::class.java)
            println("Resultado sincronizado com o cliente ($urlFinal) com sucesso!")
        } catch (e: Exception) {
            println("Erro ao sincronizar com cliente: ${e.message}")
        }

        return batalhaAtualizada
    }

    @Transactional
    fun aplicarResultadoSincronizado(resultado: ResultadoRoundDTO): String {
        val batalha = batalhaRepository.findById(resultado.idBatalha).orElse(null)
            ?: return "Batalha não encontrada"

        // Para o cliente, o P1 é ele mesmo (que para o Host era o P2), e o P2 é o Host (que para o Host era o P1).
        // Assim, a vidaP1 do DTO refere-se ao Host, e a vidaP2 refere-se ao Cliente.
        batalha.personagem2.vida = resultado.vidaP1 // P2 do cliente = Host
        batalha.personagem1.vida = resultado.vidaP2 // P1 do cliente = Cliente

        batalha.logDescritivo = resultado.logRound
        batalha.encerrada = resultado.encerrada
        batalha.nomeVencedor = resultado.vencedor

        personagemRepository.saveAll(listOf(batalha.personagem1, batalha.personagem2))
        batalhaRepository.save(batalha)

        return "Resultado do round sincronizado com sucesso!"
    }

    // Métodos de Consulta ajustados
    fun consultarEstadoBatalha(id: Long): StatusBatalhaDTO? {
        val batalha = batalhaRepository.findById(id).orElse(null) ?: return null
        return batalha.toStatusDTO()
    }

    fun listarTodasBatalha(): List<StatusBatalhaDTO>{
        val batalha = batalhaRepository.findAll()
        return batalha.map { it.toStatusDTO() } // Transforma a lista inteira de uma vez
    }

    // Função de extensão privada utilizada internamente para conversão
    private fun Batalha.toStatusDTO(): StatusBatalhaDTO {
        return StatusBatalhaDTO(
            id = this.id,
            encerrada = this.encerrada,
            vencedor = this.nomeVencedor ?: "Em andamento",
            personagem1 = StatusPersonagemDTO(
                nome = this.personagem1.nome,
                vida = this.personagem1.vida,
                forca = this.personagem1.forca
            ),
            personagem2 = StatusPersonagemDTO(
                nome = this.personagem2.nome,
                vida = this.personagem2.vida,
                forca = this.personagem2.forca
            ),
            log = this.logDescritivo
        )
    }
}