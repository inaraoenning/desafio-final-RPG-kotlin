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
    val id: Long? = null,
    val nome: String,
    var forca: Int,
    var velocidade: Int,
    var vida: Int,
    var estaDefendendo: Boolean = false,
    var poderUsado: Boolean = false

) {
    open fun usarPoder(adversario: Personagem) {
        println("O herói $nome usou poder!")
    }

    fun tentarUsarPoder(adversario: Personagem){
        if (this.poderUsado){
             println("❌ $nome tentou usar seu poder, mas já o esgotou nesta batalha!")
             return
        }

        usarPoder(adversario)
        this.poderUsado = true // marca como usado

    }

    // calc o mais rápido e descontar a Força da Vida do adversário.
    open fun atacar(adversario: Personagem, danoEspecial: Int? = null) {
        val danoFinal = danoEspecial ?: this.forca // Se houver dano especial

        println("$nome atacou 🤺  causando $danoFinal a ${adversario.nome}")
        adversario.receberDano(danoFinal)
    }


    open fun defender(adversario: Personagem) {
        print(" 💨 $nome ativou postura defensiva! 💨 ")
        this.estaDefendendo = true
        return
    }

    // Dentro de Personagem.kt
    open fun receberDano(valor: Int) {
        var danoReal = valor

        // se estava defendendo toma metade do dano
        if(this.estaDefendendo){
            danoReal = valor / 2 
            print(" 🛡️ $nome defendeu o ataque, reduzindo o dano pela metade! ($danoReal) 🛡️ \n")
            this.estaDefendendo = false // Remove a defesa após sofrer o golpe
        }

        this.vida -= danoReal
        
        if(this.vida < 0) this.vida = 0 // Se a vida ficou negativa, zera ela

        println("❤️ $nome recebeu $danoReal de dano! (Restante: $vida)")
    }
}