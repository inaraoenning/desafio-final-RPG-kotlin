package desafio.final.rpg.controller

import desafio.final.rpg.model.MensagemChat
import desafio.final.rpg.model.TipoMensagem
import org.springframework.messaging.handler.annotation.MessageMapping
import org.springframework.messaging.handler.annotation.Payload
import org.springframework.messaging.handler.annotation.SendTo
import org.springframework.messaging.simp.SimpMessageHeaderAccessor
import org.springframework.stereotype.Controller


// @Controller (sem@Rest): necessário para controllers WebSockets/STOMP
// Não usamos @RestController porque não estamos respondendo a requisições HTTP
// normais - estamos processando mensagens WebSockt via protocolo STOMP.

@Controller
class ChatController {

    // @MeassageMapping("/chat.enviar"): este método é chamado quando um cliente
    // envia uma mensagem para o destino "/app/chat.enviar".
    // O Prefixo "/app" vem da config WebSocketConfig (setApplicationDestinationPrefixes).
    // Ou seja: cliente evnia para "/app/chat.enviar" -> Spring chama este método

    // @SendTo("/topic/mensagens")? após processar, o retorno é automaticamente
    // publicado no canal "/topic/mensagens". Todos os clientes que estiverem
    // "ouvindo" (subscribed) nesse canal recebem a mensagem em tempo real.
    // É o equivalente de um grupo de WhatsApp - um manda e todos recebem.

    // @Payload: instrui o Spring a desserializar o JSON recebido em um objeto MensagemChat.

    @MessageMapping("/chat.enviar")
    @SendTo("/topic/mensagens")
    fun enviarMensagem(@Payload mensagem: MensagemChat): MensagemChat {
        // Apenas repassa a mensagem para todos os assinantes do canal.
        return mensagem
    }

    // Guardamos o nome do jogador na sessão para identificá-lo caso ele saia.
    @MessageMapping("/chat.entrar")
    @SendTo("/topic/mensagens")
    fun adicionarUsuario (
        @Payload mensagem: MensagemChat,
        headerAccessor: SimpMessageHeaderAccessor
    ): MensagemChat {
        // Salva o nome do remetente nos atributos da sessão WebSocket.
        headerAccessor.sessionAttributes?.put("username", mensagem.remetente)

        // Retorna uma mensagem de "entrou na sala" para todos os clientes.
        return MensagemChat(
            remetente = mensagem.remetente,
            conteudo = "${mensagem.remetente}arena! ⚔️",
            tipo = TipoMensagem.ENTRAR
        )
    }

}