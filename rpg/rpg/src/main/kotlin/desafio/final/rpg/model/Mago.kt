package desafio.final.rpg.model

import jakarta.persistence.*

@Entity
@DiscriminatorValue("MAGO")
class Mago(nome: String, forca: Int, velocidade: Int, vida: Int, var magia: Int) :
    Personagem(nome = nome, forca = forca, velocidade = velocidade, vida = vida) {

    override fun usarPoder(adversario: Personagem) {
        this.vida += magia
        this.forca += magia
        println("$nome canalizou energia arcana, ganhando $magia de Vida e Força! 🔮")
    }

    override fun atacar(adversario: Personagem, danoEspecial: Int?) {
        println("$nome lança um feitiço poderoso concentrado! 💥")
        val danoMagico = this.forca + this.magia // Mago soma magia no ataque
        super.atacar(adversario, danoEspecial = danoMagico)
    }

    override fun defender(adversario: Personagem) {
        // Escudo de mana: gasta magia para absorver o dano
        if (this.magia >= adversario.forca) {
            println(" 🛡️ O ataque foi absorvido pelo escudo de mana de $nome! (Custou ${adversario.forca} de Magia)")
            this.magia -= adversario.forca
        } else {
            println(" 💥 O escudo de mana de $nome não foi forte o suficiente!")
            this.receberDano(adversario.forca)
        }
    }
}