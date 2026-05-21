package desafio.final.rpg.controller

import desafio.final.rpg.service.BatalhaService
import desafio.final.rpg.model.AcaoRedeDTO
import desafio.final.rpg.model.ResultadoRoundDTO
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/rede")
class BatalhaRedeController(private val batalhaService: BatalhaService) {

    // --- NOVOS ENDPOINTS HOST-CLIENT ---

    // 1. Cliente envia sua ação para o Host
    @PostMapping("/enviar-acao")
    fun enviarAcaoClienteParaHost(@RequestBody dto: AcaoRedeDTO): String {
        return batalhaService.registrarAcaoRival(dto.idBatalha, dto.acao)
    }

    // 2. Host executa a sua ação e roda o round inteiro
    @PostMapping("/executar-acao-host")
    fun executarAcaoHost(@RequestBody payload: Map<String, Any>): Any {
        val idBatalha = (payload["idBatalha"] as? Number)?.toLong() ?: return "Erro: idBatalha inválido"
        val acaoHost = payload["acao"] as? String ?: return "Erro: acao inválida"
        val urlCliente = payload["urlCliente"] as? String

        return batalhaService.processarRoundHost(idBatalha, acaoHost, urlCliente)
    }

    // 3. Cliente recebe o resultado pronto do Host
    @PostMapping("/sincronizar-resultado")
    fun receberResultadoSincronizado(@RequestBody dto: ResultadoRoundDTO): String {
        return batalhaService.aplicarResultadoSincronizado(dto)
    }


}