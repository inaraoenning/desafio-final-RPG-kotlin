package desafio.final.rpg.service

import desafio.final.rpg.model.Personagem
import org.springframework.stereotype.Service
import desafio.final.rpg.repository.PersonagemRepository

@Service
class PersonagemService(
    val personagemRepository: PersonagemRepository
) {
    // Salvar
    fun salvar(personagem: Personagem) = personagemRepository.save(personagem) // Jpa: se já tiver primary key atualiza os dados senão cria um novo

    // Buscar
    fun buscarTodos():List<Personagem> = personagemRepository.findAll()

    // Excluir
    fun excluirPersonagem(id: Long) = personagemRepository.deleteById(id)

    // Ressuscitar
    fun ressuscitarMortos(): List<Personagem> {
        val personagensMortos = personagemRepository.findByVidaLessThanEqual(0)

        personagensMortos.forEach { personagem ->
            personagem.vida = 100
        }

        return personagemRepository.saveAll(personagensMortos)
    }
}