package desafio.final.rpg.config

import org.springframework.context.annotation.Configuration
import org.springframework.messaging.simp.config.MessageBrokerRegistry
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker
import org.springframework.web.socket.config.annotation.StompEndpointRegistry
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer

// @EnableWebSocketMessageBroker: ativa o suporte completo a WebSocket com protocolo STOMP
// STOMP = Simple text Oriented Messaging Protocol
@Configuration
@EnableWebSocketMessageBroker

class WebScoketConfig : WebSocketMessageBrokerConfigurer {

    // configureMenssageBroker: define ocmo as mensagens são ROTEADAS no servidor
    override fun configureMessageBroker(config: MessageBrokerRegistry) {

        // enaleSimpleBroker: cria um "distribuidor" de mensagens simples em memória.
        // qualquer mensagem enviada epara "/topic/..." será transmitida para TODOS os clientes
        // que estiverem "assinados" (subscribed) naquele destino
        config.enableSimpleBroker("/topic")

        // setApplicationDestinationPrefixes: define o prefixo das mensagens que chegam
        // do cliente e precisam ser PROCESSADAS pro um método @MessageMapping no setvidor.
        // Ou seja: mensagem chegando em "/app/chat.enviar" -> vai para o método mapeado.
        config.setApplicationDestinationPrefixes("/app")
    }

    // registerStompEndpoinst: define a URL onde os clientes fazem a conexão WebSocket inicial;
    // É o "ponto de entrada" da conexão - como um endereço de prota de um prédio.
    override fun registerStompEndpoints(registry: StompEndpointRegistry) {

        // addEndpoint: registra o caminho "/chat-websocket" como ponto de conexão;
        // setAllowedOriginPatterns("*"): permite conexões de qualquer origem (necessário
        // para testar locais com HTML aberto direto no navegador sem servidor).
        // withSockJS: adiciona suporte ao SockJS, uma biblioteca JavaScript que usa
        // WebSocket quando disponível, e cai para HTTP long-polling como fallback
        // garantido que funcione em todos os navegadores.

        registry.addEndpoint("/chat-websocket")
            .setAllowedOriginPatterns("*")
            .withSockJS()

    }
}