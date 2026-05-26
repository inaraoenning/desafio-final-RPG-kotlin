package desafio.final.rpg.model

import jakarta.persistence.*

@Entity
@DiscriminatorValue("SACERDOTE")
class Sacerdote(nome: String, forca: Int, velocidade: Int, var vidaMaxima: Int, var penitencia: Int) :
    Personagem(nome = nome, forca = forca, velocidade = velocidade, vida = vidaMaxima) {
    //Penitência para curar a si mesmo e causar dano baseado na vida perdida
    override fun usarPoder(adversario: Personagem) {
        println("$nome usou 'Penitência'! 🙏 ...")
        executarPenitencia(adversario)
    }

    fun executarPenitencia(adversario: Personagem) {
        val vidaPerdida = this.vidaMaxima - this.vida

        if (vidaPerdida <= 0) {
            println("$nome tentou usar Penitência, mas está com a vida cheia! O feitiço falhou.")
            return
        }

        // Lógica de Cura: Recupera metade da vida perdida + atributo penitência
        val cura = (vidaPerdida / 2) + this.penitencia
        this.vida += cura
        if (this.vida > this.vidaMaxima) this.vida = this.vidaMaxima
        println("$nome usou sua penitência e se curou em $cura pontos! ❤️ (Vida atual: $vida)")

        // Lógica de Dano: Causa dano baseado na vida perdida + penitência
        val danoDivino = vidaPerdida + this.penitencia
        println("A agonia e a fé de $nome são refletidas no adversário!")
        adversario.receberDano(danoDivino)
    }

    override fun atacar(adversario: Personagem, danoEspecial: Int?) {
        println("$nome ataca com força divina! ⚔️")
        super.atacar(adversario, danoEspecial = danoEspecial ?: forca)
    }

    override fun defender(adversario: Personagem) {
        println(" 🛡️ $nome invoca uma barreira de fé para o próximo ataque!")
        super.defender(adversario)
    }

    override fun receberDano(valor: Int) {
        var danoRestante = valor
        if (this.estaDefendendo) {
            println("🛡️ A barreira de fé amortece o impacto com ${this.penitencia} de poder!")
            danoRestante -= this.penitencia
            
            if (danoRestante <= 0) {
                println("A barreira anulou completamente o ataque!")
                this.estaDefendendo = false
                return
            }
        }
        super.receberDano(danoRestante)
    }
}