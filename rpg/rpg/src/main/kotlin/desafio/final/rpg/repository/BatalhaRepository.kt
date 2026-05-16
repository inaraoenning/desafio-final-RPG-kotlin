package desafio.final.rpg.repository

import desafio.final.rpg.model.Batalha
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface BatalhaRepository: JpaRepository<Batalha, Long> {
}