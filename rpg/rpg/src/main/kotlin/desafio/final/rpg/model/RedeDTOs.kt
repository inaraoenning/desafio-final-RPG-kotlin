package desafio.final.rpg.model

data class AcaoRedeDTO(
    val idBatalha: Long,
    val acao: String
)

data class ResultadoRoundDTO(
    val idBatalha: Long,
    val vidaP1: Int,
    val vidaP2: Int,
    val logRound: String,
    val encerrada: Boolean,
    val vencedor: String?
)
