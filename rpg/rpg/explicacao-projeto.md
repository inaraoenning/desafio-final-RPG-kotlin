# ⚔️ Estrutura e Explicação do Projeto RPG

Este documento tem como objetivo explicar a arquitetura do projeto de RPG em Kotlin com Spring Boot, detalhando a responsabilidade de cada pasta e o funcionamento de cada entidade do jogo.

---

## 📁 Estrutura de Pastas (Arquitetura MVC)

O projeto segue a arquitetura padrão do Spring Boot (Model, Controller, Repository e Service).

### `src/main/kotlin/desafio/final/rpg/controller/`
Responsável por receber as requisições HTTP (da internet ou do seu Frontend) e devolver as respostas. É a "porta de entrada" da API.
*   **PersonagemController:** Gerencia as rotas CRUD (Criar, Ler, Deletar) dos personagens (ex: `POST /personagem`).
*   **BatalhaRedeController:** Gerencia a mecânica Host-Client da batalha. Recebe a ação do cliente, manda o Host processar o turno e devolve o resultado sincronizado.

### `src/main/kotlin/desafio/final/rpg/model/`
É o "Coração" do jogo. Aqui ficam as Entidades (classes) que representam tabelas no banco de dados e as regras de negócio de como os personagens interagem.
*   *Veja a seção "Como as Entidades Funcionam" logo abaixo para mais detalhes.*

### `src/main/kotlin/desafio/final/rpg/repository/`
Responsável exclusivamente por conversar com o banco de dados (PostgreSQL).
*   **PersonagemRepository / BatalhaRepository:** Interfaces mágicas do Spring Data JPA que salvam, buscam e deletam os dados sem precisarmos escrever códigos SQL complexos na mão.

### `src/main/kotlin/desafio/final/rpg/service/`
Onde fica a lógica "pesada" e o cálculo do jogo (Regras de Negócio).
*   **BatalhaService:** Aqui é onde a mágica acontece. Ele joga os dados (D20), soma a velocidade (Iniciativa), processa o dano, e verifica se alguém morreu para encerrar a batalha.

### `frontend/`
*   **index.html:** A interface visual para os jogadores. Ela consome a API (Backend) enviando os IDs e Ações via JavaScript (Fetch API) e atualiza as barras de HP e o chat WebSocket em tempo real.

### Raiz do Projeto (Docker)
*   **Dockerfile:** O manual de instruções para o Docker construir uma máquina Linux mínima, compilar nosso código Java/Kotlin e expor a porta da nossa API.
*   **docker-compose.yml:** Sobe o servidor do PostgreSQL e a API juntos em contêineres locais para testes.

---

## 🦸 Como as Entidades (Modelos) se Comportam

O projeto utiliza o conceito de **Polimorfismo** (Herança do Kotlin) em conjunto com o JPA (`@Inheritance(strategy = InheritanceType.SINGLE_TABLE)`). Isso significa que todas as classes de lutadores herdam da classe base `Personagem` e são salvas na mesma tabela do banco, sendo diferenciadas pela coluna `tipo_personagem`.

### 1. `Personagem` (A Classe Mãe)
*   Tem os atributos básicos que todo mundo possui: `nome`, `forca`, `velocidade` e `vida`.
*   Tem as mecânicas padrões de combate:
    *   **Atacar:** Compara a velocidade. Se for mais rápido, ataca. Se for mais lento, o inimigo tem a chance de esquivar.
    *   **Defender:** Se a velocidade for alta o suficiente, zera o dano recebido.

### 2. `Guerreiro` 🛡️ (O Tanque)
*   **Atributo Exclusivo:** `defesa`
*   **Comportamento:** Ele não tem um dano explosivo, mas sua defesa atua como uma "segunda barra de vida" ou redução de dano direto. Ideal para lutas longas de atrito.

### 3. `Ladino` 🗡️ (Veloz e Fatal)
*   **Atributo Exclusivo:** `sagacidade`
*   **Comportamento:** O poder especial dele escala com sagacidade. Como o ataque principal depende de ser o mais rápido da rodada, ele é focado inteiramente em Velocidade para garantir que os golpes entrem (senão o inimigo sempre esquiva).

### 4. `Mago` 🔮 (Escala Infinita)
*   **Atributo Exclusivo:** `magia`
*   **Comportamento:** Começa o combate muito vulnerável (pouca vida e força). Porém, o seu "Poder" não dá dano imediato, mas sim *aumenta* permanentemente sua Vida e Força enquanto a batalha durar. Se o inimigo não o matar rápido, o Mago vira um monstro gigante.

### 5. `Sacerdote` 🙏 (O Vingador)
*   **Atributos Exclusivos:** `vida_maxima` e `penitencia`
*   **Comportamento:** É uma classe de contra-ataque. Sua força e velocidade bases são horríveis. Porém, quanto mais ele apanha e sua vida cai, mais poderosa se torna a sua "Penitência" (ele dá um dano absurdo no inimigo e se cura com base no que falta para a vida máxima).

---

## ⚙️ Entidades de Sistema

### `Batalha`
*   Mantém o estado atual da briga. Sabe quem é o Jogador 1 (Host) e o Jogador 2 (Cliente).
*   Guarda o atributo `acaoPendenteRival` (que segura a ação que o amigo escolheu pela rede até que o Host finalmente processe o turno para os dois).
*   Gera os `logs` de texto (ex: *"Arthur usou ataque em Frieren!"*) que aparecem no chat do frontend.

### `MensagemChat`
*   Entidade simples em memória (não vai pro banco) usada pelo protocolo WebSocket (`STOMP/SockJS`) para distribuir as mensagens de texto de forma instantânea na aba de chat da Arena Web.
