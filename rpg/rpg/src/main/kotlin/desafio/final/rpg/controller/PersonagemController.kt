package desafio.final.rpg.controller


import desafio.final.rpg.model.Personagem
import org.springframework.web.bind.annotation.*
import desafio.final.rpg.repository.PersonagemRepository
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag

@RestController
@RequestMapping("/personagem")
@Tag(
    name = "Personagem",
    description = "API para gerencimaento de personagens")
class PersonagemController(private val repository: PersonagemRepository) {

    @Operation(
        summary = "Lista todos os personagens",
        description = "Retorna uma lista completa de todos os personagens cadastrados no banco de dados"
    )
    @GetMapping
    fun listarTodos(): List<Personagem> = repository.findAll()

    @Operation(
        summary = "Cria um personagem",
        description = "Recebe os dados de um novo personagem no corpo da requisição e o cadastra no banco de dados"
    )
    @PostMapping
    fun criar(@RequestBody personagem: Personagem): Personagem = repository.save(personagem)

    @Operation(
        summary = "Deleta um personagem pelo ID",
        description = "Busca um personagem específico pelo ID na URL e o remove permanentemente do banco de dados"
    )
    @DeleteMapping("/{id}")
    fun deletar(@PathVariable id: Long) = repository.deleteById(id)
}