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

data class IniciarBatalhaDTO(
    val idPersonagem1: Long,
    val idPersonagem2: Long
)

data class ExecutarAcaoHostDTO(
    val idBatalha: Long,
    val acao: String,
    val urlCliente: String? = null
)

data class StatusPersonagemDTO(
    val nome: String,
    val vida: Int,
    val forca: Int
)

data class StatusBatalhaDTO(
    val id: Long?,
    val encerrada: Boolean,
    val vencedor: String,
    val personagem1: StatusPersonagemDTO,
    val personagem2: StatusPersonagemDTO,
    val log: String
)