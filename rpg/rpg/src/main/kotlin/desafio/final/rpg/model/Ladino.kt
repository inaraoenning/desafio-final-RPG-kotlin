package desafio.final.rpg.model

import jakarta.persistence.*

@Entity
@DiscriminatorValue("LADINO")
class Ladino(nome: String, forca: Int, velocidade: Int, vida: Int, var sagacidade: Int) :
    Personagem(nome = nome, forca = forca, velocidade = velocidade, vida = vida) {

    override fun usarPoder(adversario: Personagem) {
        println("O Ladino $nome entrou em modo furtivo e atacou de surpresa! 🗡️")
        super.atacar(adversario, danoEspecial = forca + sagacidade) // Usa forca + sagacidade no dano especial
    }

    override fun atacar(adversario: Personagem, danoEspecial: Int?) {
        println("$nome usa sua alta velocidade para dar um golpe rápido! 🗡️")
        super.atacar(adversario, danoEspecial = danoEspecial ?: forca)
    }

    override fun defender(adversario: Personagem) {
        println(" 💨 $nome se prepara para usar sua agilidade extrema! 💨 ")
        super.defender(adversario)
    }

    override fun receberDano(valor: Int) {
        // Ladino soma sagacidade com velocidade para tentar esquivar se estiver defendendo
        if (this.estaDefendendo) {
            if ((this.velocidade + this.sagacidade) >= valor) {
                println(" 💨 $nome usou sua agilidade e esquivou completamente do ataque! 💨 ")
                this.estaDefendendo = false
                return
            }
            println(" 💨 $nome tentou esquivar, mas não foi rápido o suficiente! 💨 ")
        }
        
        super.receberDano(valor)
    }
}