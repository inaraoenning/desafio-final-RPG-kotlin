package desafio.final.rpg.config

import org.springframework.context.annotation.Configuration
import org.springframework.messaging.simp.config.MessageBrokerRegistry
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker
import org.springframework.web.socket.config.annotation.StompEndpointRegistry
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer

// @EnableWebSocketMessageBroker: ativa o suporte completo a WebSocket com protocolo STOMP
@Configuration
@CrossOrigin(origins = ["*"])
@EnableWebSocketMessageBroker

class WebSocketConfig : WebSocketMessageBrokerConfigurer {

    // configureMenssageBroker: define como as mensagens são ROTEADAS no servidor.
    override fun configureMessageBroker(config: MessageBrokerRegistry) {

        // Cria o canal "/topic". O que for enviado para ca, todos que estão conectos recebem.
        config.enableSimpleBroker("/topic")

        config.setApplicationDestinationPrefixes("/app")
    }

    // registerStompEndpoinst: define a URL onde os clientes fazem a conexão WebSocket inicial
    override fun registerStompEndpoints(registry: StompEndpointRegistry) {

        registry.addEndpoint("/chat-websocket")
            .setAllowedOriginPatterns("*")
            // Adicona o SockJS, que garente o funcionamento mesmo se o navegador não suportar WebSocket.
            .withSockJS()

    }
}