package desafio.final.rpg.model

import jakarta.persistence.*

@Entity
@DiscriminatorValue("GUERREIRO")
class Guerreiro(
    nome: String, 
    forca: Int, 
    velocidade: Int, 
    vida: Int, 
    var defesa: Int
    ) :
    Personagem(nome = nome, forca = forca, velocidade = velocidade, vida = vida) {

    override fun usarPoder(adversario: Personagem) {
       
            this.defesa += 10 // Aumenta a defesa temporariamente
            println("$nome ativou postura defensiva e ganhou +10 de Defesa! 🛡️ (Defesa atual: $defesa)")
       
    }

    override fun defender(adversario: Personagem) {
        println("$nome levanta o escudo pesado para aguentar o impacto! 🛡️")
        super.defender(adversario)
    }

    // Quando o ataque chamar "receberDano",
    // o Guerreiro usa lógica da armadura.
    override fun receberDano(valor: Int) {
        var danoRestante = valor

        if (defesa > 0) {
            // Se a defesa for maior que o dano, a defesa absorve tudo
            if (defesa >= valor) {
                defesa -= valor
                println("$nome absorveu $valor de dano na armadura! 🛡️ Defesa Restante: $defesa")
                return
            } else {
                // Se o dano for maior que a defesa, o que sobrar vai para a vida
                danoRestante = valor - defesa
                println("$nome teve a defesa quebrada! Absorveu $defesa.")
                defesa = 0
            }
        }

        // Passa o dano restante para a classe base tratar (defesa do turno e vida)
        super.receberDano(danoRestante)
    }
}