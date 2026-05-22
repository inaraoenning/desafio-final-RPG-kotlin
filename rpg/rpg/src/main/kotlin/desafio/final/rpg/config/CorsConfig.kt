package desafio.final.rpg.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.client.RestClient
import org.springframework.web.servlet.config.annotation.CorsRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Configuration
class CorsConfig: WebMvcConfigurer {

    // Cria o cliente HTTP para o serviço de sincronização de batalha
    @Bean
    fun restClient(): RestClient {
        return RestClient.create()
    }

    override fun addCorsMappings(registry: CorsRegistry) {
        registry.addMapping("/**")
            .allowedOrigins("https://inaraoenning.github.io", "http://localhost:4200", "http://localhost:8080", "http://127.0.0.1:5500")
            .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // OPTIONS é vital para o CORS preflight
            .allowedHeaders("*")
            .allowCredentials(false)
    }
}