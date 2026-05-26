package desafio.final.rpg.model

class MensagemChat (
    val remetente: String = "",
    val conteudo: String = "",
    val tipo: TipoMensagem = TipoMensagem.CHAT
)

enum class TipoMensagem {
    CHAT,
    ENTRAR,
    SAIR
}