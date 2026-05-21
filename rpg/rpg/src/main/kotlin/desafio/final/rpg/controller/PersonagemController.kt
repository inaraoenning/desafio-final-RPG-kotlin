package desafio.final.rpg.controller


import desafio.final.rpg.model.Personagem
import org.springframework.web.bind.annotation.*
import desafio.final.rpg.repository.PersonagemRepository

@RestController
@RequestMapping("/personagem")
@CrossOrigin(origins = ["*"])
class PersonagemController(private val repository: PersonagemRepository) {

    @GetMapping
    fun listarTodos(): List<Personagem> = repository.findAll()

    @PostMapping
    fun criar(@RequestBody personagem: Personagem): Personagem = repository.save(personagem)

    @DeleteMapping("/{id}")
    fun deletar(@PathVariable id: Long) = repository.deleteById(id)
}