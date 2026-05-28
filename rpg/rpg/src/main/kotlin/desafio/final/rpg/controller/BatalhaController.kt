package desafio.final.rpg.controller

import desafio.final.rpg.DTOs.AcaoRedeDTO
import desafio.final.rpg.model.Batalha
import desafio.final.rpg.DTOs.ExecutarAcaoHostDTO
import desafio.final.rpg.DTOs.IniciarBatalhaDTO
import desafio.final.rpg.DTOs.ResultadoRoundDTO
import desafio.final.rpg.DTOs.StatusBatalhaDTO
import desafio.final.rpg.service.BatalhaService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/batalha")
@Tag(
    name = "Batalha",
    description = "API para gerenciamento de combates e batalhas entre personagens"
)
class BatalhaController(
    private val batalhaService: BatalhaService
) {

    @Operation(
        summary = "Lista todas as batalhas",
        description = "Retorna uma lista com o status de todas as batalhas registradas no sistema."
    )
    @GetMapping
    fun listarTodasBatalhas(): ResponseEntity<List<StatusBatalhaDTO>> =
        ResponseEntity.ok(batalhaService.listarTodasBatalha())

    @Operation(
        summary = "Consulta o estado de uma batalha",
        description = "Busca os detalhes e o status atual de uma batalha específica utilizando o seu ID."
    )
    @ApiResponses(
        ApiResponse(responseCode = "200", description = "Batalha encontrada com sucesso"),
        ApiResponse(responseCode = "404", description = "Nenhuma batalha encontrada com o ID informado", content = [Content()])
    )
    @GetMapping("/{id}")
    fun consultarBatalha(@PathVariable id: Long): ResponseEntity<StatusBatalhaDTO> {
        val resposta = batalhaService.consultarEstadoBatalha(id)
            ?: return ResponseEntity.notFound().build()

        return ResponseEntity.ok(resposta)
    }

    @Operation(
        summary = "Inicia uma nova batalha",
        description = "Cria um novo combate na arena entre dois personagens com base nos IDs fornecidos."
    )
    @PostMapping("/iniciar")
    fun iniciarBatalha(@RequestBody dto: IniciarBatalhaDTO): ResponseEntity<Batalha> =
        ResponseEntity.ok(batalhaService.iniciarNovaBatalha(dto.idPersonagem1, dto.idPersonagem2))

    @Operation(
        summary = "Envia a ação do cliente para o host",
        description = "Registra a ação (ataque, defesa, etc.) tomada pelo jogador rival/cliente no painel do host."
    )
    @PostMapping("/enviar-acao")
    fun enviarAcaoClienteParaHost(@RequestBody dto: AcaoRedeDTO): String =
        batalhaService.registrarAcaoRival(dto.idBatalha, dto.acao)

    @Operation(
        summary = "Executa a rodada no host",
        description = "Processa o round da batalha do lado do servidor host, calculando os impactos das ações de ambos os jogadores."
    )
    @PostMapping("/executar-acao-host")
    fun executarAcaoHost(@RequestBody dto: ExecutarAcaoHostDTO): ResponseEntity<Any> =
        ResponseEntity.ok(batalhaService.processarRoundHost(dto.idBatalha, dto.acao, dto.urlCliente))

    @Operation(
        summary = "Sincroniza o resultado do round",
        description = "Recebe e aplica o resultado final já calculado de um turno para garantir que ambos os jogadores vejam o mesmo status."
    )
    @PostMapping("/sincronizar-resultado")
    fun receberResultadoSincronizado(@RequestBody dto: ResultadoRoundDTO): String =
        batalhaService.aplicarResultadoSincronizado(dto)
}