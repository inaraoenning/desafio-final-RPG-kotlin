# Explicação da Batalha e Criação de Menu Interativo

## Como funciona a batalha entre 2 PCs?
Atualmente, o projeto usa **APIs REST** para a comunicação entre as máquinas:
1. Seu computador roda a aplicação na porta `8080`.
2. O computador do seu rival roda a aplicação na porta `8080` (na rede local/internet).
3. Quando você ataca, o `BatalhaRedeController` (no endpoint `/atacar`) envia uma requisição **HTTP POST** para a URL do seu rival (ex: `http://192.168.1.5:8080/receber-ataque`).
4. O computador do rival recebe o dano através do endpoint `/receber-ataque`, que desconta a vida do personagem defensor e devolve a resposta do contra-ataque.

## Como eu faria um menu para escolher o personagem?
Como você está usando o **Spring Boot**, a forma mais incrível e moderna de fazer isso é criar uma interface Web (HTML/CSS/JS) diretamente dentro da sua aplicação. Assim você terá botões, animações e efeitos visuais em vez de apenas texto no console!

### Proposed Changes

#### [NEW] `c:\Users\Feyre\Desktop\desafio-final-RPG-kotlin\rpg\rpg\src\main\resources\static\index.html`
- Criaremos uma página inicial moderna com tema *Dark RPG*, contendo botões estilizados.
- A página irá buscar via API (JavaScript `fetch`) os personagens do seu banco de dados (`/personagens`).
- Teremos caixas de seleção interativas para escolher o "Seu Personagem" e o "Personagem Inimigo".
- Um botão **"Iniciar Batalha!"** que fará a requisição para o seu servidor iniciar o combate e exibirá um log visual da luta.

#### [NEW] `c:\Users\Feyre\Desktop\desafio-final-RPG-kotlin\rpg\rpg\src\main\resources\static\style.css` e `script.js`
- Adicionaremos estilos *Glassmorphism*, cores vibrantes (neon/fogo para ataques, cura para sacerdotes) e animações responsivas.

> [!TIP]
> Também corrigi um problema oculto de codificação no seu arquivo `application.properties` que estava fazendo o projeto falhar ao rodar, e confirmei que seus pacotes agora estão corretos dentro de `desafio.final.rpg`!

## User Review Required
- menu Web moderno com HTML/JS/CSS 
- menu mais simples no terminal de texto, me avise.
