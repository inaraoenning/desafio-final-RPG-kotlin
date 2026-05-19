package desafio.final.rpg.model

// MensagemChat é um DTO (Data Transfer Object) - uma classe simples que
// serve apenas para carregar dados entre cliente e servidor via WebSocket.
// Não tem @Entity porque não é salva no banco de dados.
// Cada mensagem do chat viaja como JSON neste formato:
// { "remetente": "Inara", "conteudo": "Olá", "tipo": "CHAT"}

class MensagemChat (
    val remetente: String = "",

    val conteudo: String = "",

    // Tipo da mensagem - usamos um enum para organizar:
    // CHAT = mensagem de texto normal entre jogadores
    // ENTRAR = aviso de que um novo jogador entrou na sala
    // SAIR = aviso de que um jogador saiu
    val tipo: TipoMensagem = TipoMensagem.CHAT
)

enum class TipoMensagem {
    CHAT,
    ENTRAR,
    SAIR
}