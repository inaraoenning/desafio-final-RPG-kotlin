package desafio.final.rpg.repository

import desafio.final.rpg.model.Personagem
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface PersonagemRepository: JpaRepository<Personagem, Long> {
    fun findBynome(nome: String): MutableList<Personagem>
    fun deletePersonagemsByNome(nome: String): Personagem
}
