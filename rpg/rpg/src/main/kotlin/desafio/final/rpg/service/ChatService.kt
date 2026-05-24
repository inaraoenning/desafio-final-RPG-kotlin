package desafio.final.rpg.service

import desafio.final.rpg.model.MensagemChat
import desafio.final.rpg.model.TipoMensagem
import org.springframework.context.event.EventListener
import org.springframework.messaging.simp.SimpMessageSendingOperations
import org.springframework.messaging.simp.stomp.StompHeaderAccessor
import org.springframework.stereotype.Service
import org.springframework.web.socket.messaging.SessionDisconnectEvent

@Service
class ChatService(
    private val messagingTemplate: SimpMessageSendingOperations
) {
    @EventListener
    fun desconexao(event: SessionDisconnectEvent) {
        val headerAccessor = StompHeaderAccessor.wrap(event.message)
        val username = headerAccessor.sessionAttributes?.get("username") as String?

        if (username != null) {
            val mensagemSaida = MensagemChat(
                remetente = username,
                conteudo = "$username fugiu da arena! 🏃💨",
                tipo = TipoMensagem.SAIR
            )
            messagingTemplate.convertAndSend("/topic/mensagens", mensagemSaida)
        }
    }
}