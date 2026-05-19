package desafio.final.rpg.controller

import desafio.final.rpg.model.Batalha
import desafio.final.rpg.repository.BatalhaRepository
import desafio.final.rpg.service.BatalhaService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

// @RequesMapping("/batala"): todos os endpoints desta classe começam com /batalha
@RestController
@RequestMapping("/batalha")
class BatalhaController (
    // injeção de dependência: o Sping fornece as instãncias automaticamente.
    private val batalhaService: BatalhaService,
    private val batalhaRepository: BatalhaRepository
){

    // ENDPOINT 1 - POST /batalha/iniciar
    // Responsabilidade: criar uma nova batalha no banco entre dois personagens.
    // o campo JSON deve conter: {"idPersonagem": 1, "idPersonagem2": 2)
    @PostMapping("/iniciar")
    fun iniciarBatalha(@RequestBody payload: Map<String, Long>): ResponseEntity<Batalha>{
        // Extrai os IDs do corpo da requisição
        // O operador ?: lança exceção com mensagem se o campo não existir no JSON
        val id1 = payload["idPersonagem1"] ?: throw IllegalStateException("IdPersonagem1 é obrigatorio")
        val id2 = payload["idPersonagem2"] ?: throw IllegalStateException("IdPersonagem2 é obrigatorio")

        // Delega a criação ao Service (que contém as regras de negocio)
        // O Controller so orquesta - nunca tem lógica de jogo direto nele.
        val batalha = batalhaService.iniciarNovaBatalha(id1, id2)

        // ResponseEntity.ok() retorna HTTP 200 com objeto Batalha serializado em JSON
        return ResponseEntity.ok(batalha)
    }

    // INDPOINT 2 - GET /batalha/{id}
    // Responsabilidade: consutlar o estado atual de uma batalha pelo ID.
    // Retonra HP dos personagens, log de eventos, vencedor, se esta encerrada, etc.
    // Usado pelo Postman e pela interface HTML para "ver o placar"
    @GetMapping("/{id}")
    fun consultarBatalha(@PathVariable id: Long): ResponseEntity<Any>{
        // orElse(null): retorna null se não aparecer, ao invés de lançar exceção.
        val batalha = batalhaRepository.findById(id).orElse(null)
            ?: return ResponseEntity.notFound().build()

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
    fun listarTodasBatalhas(): ResponseEntity<List<Batalha>>{
        val batalhas = batalhaRepository.findAll()
        return ResponseEntity.ok(batalhas)
    }

    // ENDPoINT 4 - POST /batalha/{id}/round
    // Responsabilide: executar um turno de combate na batalha especificada.
    @PostMapping("/{id}/round")
    fun executarRound(@PathVariable id: Long, @RequestBody payload: Map<String, String>): ResponseEntity<Batalha> {
        val acao1 = payload["acaoPersonagem1"] ?: throw IllegalStateException("acaoPersonagem1 é obrigatorio")
        val acao2 = payload["acaoPersonagem2"] ?: throw IllegalStateException("acaoPersonagem2 é obrigatorio")
        val batalha = batalhaService.processarRound(id, acao1, acao2)
        return ResponseEntity.ok(batalha)
    }
}