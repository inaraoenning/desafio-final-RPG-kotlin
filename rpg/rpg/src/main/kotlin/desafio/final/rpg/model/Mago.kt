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
        println(" 🔮 $nome prepara seu escudo de mana para o próximo ataque!")
        super.defender(adversario)
    }

    override fun receberDano(valor: Int) {
        var danoRestante = valor
        
        // Escudo de mana: gasta magia para absorver o dano se estiver defendendo
        if (this.estaDefendendo && this.magia > 0) {
            if (this.magia >= valor) {
                println(" 🛡️ O ataque foi absorvido pelo escudo de mana de $nome! (Custou $valor de Magia)")
                this.magia -= valor
                this.estaDefendendo = false
                return
            } else {
                println(" 💥 O escudo de mana de $nome foi quebrado! (Absorveu ${this.magia})")
                danoRestante -= this.magia
                this.magia = 0
            }
        }
        super.receberDano(danoRestante)
    }
}