package desafio.final.rpg.controller

import desafio.final.rpg.model.MensagemChat
import desafio.final.rpg.model.TipoMensagem
import org.springframework.messaging.handler.annotation.MessageMapping
import org.springframework.messaging.handler.annotation.Payload
import org.springframework.messaging.handler.annotation.SendTo
import org.springframework.messaging.simp.SimpMessageHeaderAccessor
import org.springframework.stereotype.Controller

@Controller
class ChatController {

    // Distribui a mensagem de texto de um jogador para todos os outros no chat
    @MessageMapping("/chat.enviar")
    @SendTo("/topic/mensagens")
    fun enviarMensagem(@Payload mensagem: MensagemChat): MensagemChat = mensagem

    //
    @MessageMapping("/chat.entrar")
    @SendTo("/topic/mensagens")
    fun adicionarUsuario (
        @Payload mensagem: MensagemChat,
        headerAccessor: SimpMessageHeaderAccessor
    ): MensagemChat {

        headerAccessor.sessionAttributes?.put("username", mensagem.remetente)

        return MensagemChat(
            remetente = mensagem.remetente,
            conteudo = "${mensagem.remetente} entrou na arena! ⚔️",
            tipo = TipoMensagem.ENTRAR
        )
    }
}