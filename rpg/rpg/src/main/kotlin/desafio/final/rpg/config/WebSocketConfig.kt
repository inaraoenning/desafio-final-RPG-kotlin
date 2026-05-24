package desafio.final.rpg.config

import org.springframework.context.annotation.Configuration
import org.springframework.messaging.simp.config.MessageBrokerRegistry
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker
import org.springframework.web.socket.config.annotation.StompEndpointRegistry
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer

@Configuration
@CrossOrigin(origins = ["*"])
@EnableWebSocketMessageBroker
class WebSocketConfig : WebSocketMessageBrokerConfigurer {

    override fun configureMessageBroker(config: MessageBrokerRegistry) {
        config.enableSimpleBroker("/topic")
        config.setApplicationDestinationPrefixes("/app")
    }

    // registerStompEndpoinst: define a URL onde os clientes fazem a conexão WebSocket inicial
    override fun registerStompEndpoints(registry: StompEndpointRegistry) {
        registry.addEndpoint("/chat-websocket")
            .setAllowedOriginPatterns("*")
            .withSockJS()
    }
}