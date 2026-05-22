package desafio.final.rpg.controller

import desafio.final.rpg.model.AcaoRedeDTO
import desafio.final.rpg.model.Batalha
import desafio.final.rpg.model.ExecutarAcaoHostDTO
import desafio.final.rpg.model.IniciarBatalhaDTO
import desafio.final.rpg.model.ResultadoRoundDTO
import desafio.final.rpg.model.StatusBatalhaDTO
import desafio.final.rpg.service.BatalhaService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/batalha")
class BatalhaController(
    private val batalhaService: BatalhaService
) {
    @PostMapping("/iniciar")
    fun iniciarBatalha(@RequestBody dto: IniciarBatalhaDTO): ResponseEntity<Batalha> {
        val batalha = batalhaService.iniciarNovaBatalha(dto.idPersonagem1, dto.idPersonagem2)
        return ResponseEntity.ok(batalha)
    }

    @PostMapping("/enviar-acao")
    fun enviarAcaoClienteParaHost(@RequestBody dto: AcaoRedeDTO): String {
        return batalhaService.registrarAcaoRival(dto.idBatalha, dto.acao)
    }

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

    @GetMapping("/{id}")
    fun consultarBatalha(@PathVariable id: Long): ResponseEntity<StatusBatalhaDTO> {
        val resposta = batalhaService.consultarEstadoBatalha(id)
            ?: return ResponseEntity.notFound().build()

        return ResponseEntity.ok(resposta)
    }

    @GetMapping
    fun listarTodasBatalhas(): ResponseEntity<List<StatusBatalhaDTO>> {
        return ResponseEntity.ok(batalhaService.listarTodasBatalha())
    }

}