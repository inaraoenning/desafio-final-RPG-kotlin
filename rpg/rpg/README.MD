## Model (ou entity)
- O formato do nosso dado no banco.

## Repository
- A interface que conversa com o banco de dados.

## Service
- Onde fica a regra de negócio (cálculos, validações).

## Controller
- Onde expomos as rotas da nossa API.

## Balanceamento RPG 
Guerreiro 🛡️: Ele tem uma barra de vida "extra" (a defesa). Ele aguenta pancada, mas não tem um dano explosivo no poder.

Ladino 🗡️: O poder dele usa sagacidade para atacar. Como o ataque comum dele depende de ser mais rápido que o alvo (velocidade), ele precisa ser o mais rápido do jogo para não errar ataques!

Mago 🔮: O poder dele é incrível (aumenta vida e forca permanentemente na batalha). Ele começa fraco (pouca vida/força), mas se o combate durar muito, ele vira um monstro imbatível.

Sacerdote 🙏: O poder dele (Penitência) é uma reação perfeita: quanto mais machucado ele estiver, mais ele se cura e mais dano ele explode no inimigo. Ele precisa de muita vida máxima para sobreviver até o momento de dar o "combo".

## Entidades no DB 

- 1. O GUERREIRO (Tanque de Guerra)
Muita vida, muita defesa, mas velocidade e força moderadas.
```INSERT INTO personagem (tipo_personagem, nome, forca, velocidade, vida, defesa) 
VALUES ('GUERREIRO', 'Arthur o Destemido', 15, 10, 120, 50);
```
- 2. O LADINO (Veloz e Cirúrgico)
Velocidade altíssima para garantir que acerta o ataque, força base menor, mas sagacidade alta no poder.
```INSERT INTO personagem (tipo_personagem, nome, forca, velocidade, vida, sagacidade) 
VALUES ('LADINO', '20MATA70CORRER', 12, 22, 90, 25);
```
- 3. O MAGO (Crescimento no Combate)
Começa com atributos baixos e pouca vida, mas se usar o poder vira um titã. Velocidade média.
```INSERT INTO personagem (tipo_personagem, nome, forca, velocidade, vida, magia) 
VALUES ('MAGO', 'Frieren 1.0', 10, 12, 80, 15);
```
- 4. O SACERDOTE (O Vingador ferido)
Força e velocidade baixas. Muita vida máxima para aguentar apanhar e acumular o dano da Penitência.
```INSERT INTO personagem (tipo_personagem, nome, forca, velocidade, vida, vida_maxima, penitencia) 
VALUES ('SACERDOTE', 'Padre Marcelo Rossi', 10, 8, 140, 140, 20);
```
