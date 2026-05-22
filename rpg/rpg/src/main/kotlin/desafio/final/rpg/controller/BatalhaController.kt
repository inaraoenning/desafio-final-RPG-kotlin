package desafio.final.rpg.controller

import desafio.final.rpg.model.AcaoRedeDTO
import desafio.final.rpg.model.Batalha
import desafio.final.rpg.model.ExecutarAcaoHostDTO
import desafio.final.rpg.model.IniciarBatalhaDTO
import desafio.final.rpg.model.ResultadoRoundDTO
import desafio.final.rpg.repository.BatalhaRepository
import desafio.final.rpg.service.BatalhaService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

// @RequesMapping("/batala"): todos os endpoints desta classe começam com /batalha
@RestController
@RequestMapping("/batalha")
class BatalhaController(
    // injeção de dependência: o Sping fornece as instãncias automaticamente.
    private val batalhaService: BatalhaService, private val batalhaRepository: BatalhaRepository
) {

    // ENDPOINT 1 - POST /batalha/iniciar
    // Responsabilidade: criar uma nova batalha no banco entre dois personagens.
    // o campo JSON deve conter: {"idPersonagem": 1, "idPersonagem2": 2)
    @PostMapping("/iniciar")
    fun iniciarBatalha(@RequestBody dto: IniciarBatalhaDTO): ResponseEntity<Batalha> {
        val batalha = batalhaService.iniciarNovaBatalha(dto.idPersonagem1, dto.idPersonagem2)
        return ResponseEntity.ok(batalha)
    }

    // Cliente envia sua ação para o Host
    @PostMapping("/enviar-acao")
    fun enviarAcaoClienteParaHost(@RequestBody dto: AcaoRedeDTO): String {
        return batalhaService.registrarAcaoRival(dto.idBatalha, dto.acao)
    }

    // Host executa a sua ação e roda o round inteiro
    @PostMapping("/executar-acao-host")
    fun executarAcaoHost(@RequestBody dto: ExecutarAcaoHostDTO): ResponseEntity<Any> {
        val resultado = batalhaService.processarRoundHost(dto.idBatalha, dto.acao, dto.urlCliente)
        return ResponseEntity.ok(resultado)
    }

    // Cliente recebe o resultado pronto do Host
    @PostMapping("/sincronizar-resultado")
    fun receberResultadoSincronizado(@RequestBody dto: ResultadoRoundDTO): String {
        return batalhaService.aplicarResultadoSincronizado(dto)
    }


    // ENDPOINT 2 - GET /batalha/{id}
    // Responsabilidade: consutlar o estado atual de uma batalha pelo ID.
    // Retonra HP dos personagens, log de eventos, vencedor, se esta encerrada, etc.
    // Usado pelo Postman e pela interface HTML para "ver o placar"
    @GetMapping("/{id}")
    fun consultarBatalha(@PathVariable id: Long): ResponseEntity<Any> {
        // orElse(null): retorna null se não aparecer, ao invés de lançar exceção.
        val batalha = batalhaRepository.findById(id).orElse(null) ?: return ResponseEntity.notFound().build()

        val resposta = mapOf(
            "id" to batalha.id,
            "encerrada" to batalha.encerrada,
            "vencedor" to (batalha.nomeVencedor ?: "Em andamento"),
            "personagem1" to mapOf(
                "nome" to batalha.personagem1.nome,
                "vida" to batalha.personagem1.vida,
                "forca" to batalha.personagem1.forca
            ),
            "personagem2" to mapOf(
                "nome" to batalha.personagem2.nome,
                "vida" to batalha.personagem2.vida,
                "forca" to batalha.personagem2.forca
            ),
            "log" to batalha.logDescritivo
        )

        return ResponseEntity.ok(resposta)
    }

    // ENDPOINT 3 - GET /batalha
    // Responsabilidade: listar TODAS as batalhas já registradas no banco
    @GetMapping
    fun listarTodasBatalhas(): ResponseEntity<List<Batalha>> {
        val batalhas = batalhaRepository.findAll()
        return ResponseEntity.ok(batalhas)
    }


}