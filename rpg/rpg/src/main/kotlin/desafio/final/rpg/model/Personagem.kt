package desafio.final.rpg.model

import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonTypeInfo
import jakarta.persistence.*

@Entity
@Table(name = "personagem")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "tipo_personagem", discriminatorType = DiscriminatorType.STRING)
@JsonTypeInfo(
    use = JsonTypeInfo.Id.NAME,
    include = JsonTypeInfo.As.PROPERTY,
    property = "tipo_personagem"
)
@JsonSubTypes(
    JsonSubTypes.Type(value = Guerreiro::class, name = "GUERREIRO"),
    JsonSubTypes.Type(value = Ladino::class, name = "LADINO"),
    JsonSubTypes.Type(value = Mago::class, name = "MAGO"),
    JsonSubTypes.Type(value = Sacerdote::class, name = "SACERDOTE")
)
abstract class Personagem(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) // Identificador único
    val nome: String,
    var forca: Int,
    var velocidade: Int,
    var vida: Int
) {
    open fun usarPoder(adversario: Personagem) {
        println("O herói $nome usou poder!")
        adversario.receberDano(this.forca * 3)
    }

    // calc o mais rápido e descontar a Força da Vida do adversário.
    open fun atacar(adversario: Personagem, danoEspecial: Int? = null) {
        val danoFinal = danoEspecial ?: this.forca // Se houver dano especial

        if (this.velocidade >= adversario.velocidade) {

            println("$nome é foi mais rápido e atacou 🤺 primeiro! causando $danoFinal a ${adversario.vida}")
            adversario.receberDano(danoFinal)
            return // early return
        } else {
            println("${adversario.nome} esquivou do ataque!")
        }
        return

    }


    open fun defender(adversario: Personagem) {
        // Calcula se a Velocidade é maior que a Força do adversário
        if (this.velocidade < adversario.forca) {
            print(" 💨 Você agiu rápido e esquivou do ataque! 💨 ")
            return
        }

        val dano = adversario.forca
        this.vida -= dano
        println(" -❤ $nome não conseguiu defender e sofreu $dano de dano.")

    }

    // Dentro de Personagem.kt
    open fun receberDano(valor: Int) {
        this.vida -= valor
        if (this.vida < 0) this.vida = 0 // Garante que a vida não fique negativa
        println("$nome recebeu $valor de dano! ❤️ Restante: $vida")
    }


}