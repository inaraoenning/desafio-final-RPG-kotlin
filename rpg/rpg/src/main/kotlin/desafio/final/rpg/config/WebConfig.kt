package desafio.final.rpg.config

import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.config.annotation.CorsRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Configuration
class WebConfig : WebMvcConfigurer {
    override fun addCorsMappings(registry: CorsRegistry) {
        registry.addMapping("/**") // Aplica para todas as rotas (batalha, personagem, etc)
            .allowedOriginPatterns("*") // Permite acesso de qualquer lugar (ex: seu arquivo HTML local)
            .allowedMethods("*")
            .allowedHeaders("*")
    }
}
