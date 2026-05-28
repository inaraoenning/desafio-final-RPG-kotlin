### Inara Oenning RA 60010842
### João Pedro RA 60012094

# Desafio Final RPG - Kotlin

Backend de um simulador de batalhas RPG, construído com Kotlin, Spring Boot e PostgreSQL. Este projeto gerencia o cadastro dos personagens (CRUD) e processa a lógica e a matemática do sistema de combate.

## Tecnologias Utilizadas
- **Linguagem:** Kotlin
- **Framework:** Spring Boot
- **Gerenciador de Dependências:** Maven
- **Banco de Dados:** PostgreSQL

---

## 🚀 Como Rodar o Projeto

### 1. Pré-requisitos
Antes de começar, certifique-se de ter instalado em sua máquina:
- **Java JDK 17** (ou versão correspondente configurada no projeto)
- **Maven** (opcional, pois o projeto deve conter o *Maven Wrapper*)
- **PostgreSQL** rodando localmente

### 2. Configurando o Banco de Dados
1. Abra o seu PostgreSQL (via pgAdmin ou terminal) e crie um banco de dados novo. Exemplo: `rpg_db`.
2. No código fonte do projeto, navegue até `src/main/resources/application.properties`.
3. Configure as credenciais do seu banco de dados local. Deve ficar parecido com isto:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/rpg_db
spring.datasource.username=SEU_USUARIO_AQUI
spring.datasource.password=SUA_SENHA_AQUI
spring.jpa.hibernate.ddl-auto=update
```

### 3. Executando a Aplicação
Abra o terminal na pasta raiz do projeto e execute o comando abaixo utilizando o Maven Wrapper:

**No Windows:**
```bash
mvnw.cmd spring-boot:run
```

**No Linux/Mac:**
```bash
./mvnw spring-boot:run
```

*(Se você tiver o Maven instalado globalmente na máquina, pode usar simplesmente `mvn spring-boot:run`)*

### 4. Acessando a API
Se tudo estiver configurado corretamente, o Spring Boot vai iniciar a aplicação e o servidor estará rodando. 
A API estará disponível por padrão na porta 8080:
* `http://localhost:8080/`

Agora você já pode testar os *endpoints* do CRUD de personagens e as rotas de combate usando o Postman, Insomnia ou outra ferramenta da sua preferência.
