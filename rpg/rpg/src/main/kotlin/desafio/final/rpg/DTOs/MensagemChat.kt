package desafio.final.rpg.DTOs

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