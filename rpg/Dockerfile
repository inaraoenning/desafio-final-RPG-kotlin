# Estágio 1: Build da aplicação com Maven
FROM eclipse-temurin:17-jdk-jammy AS build
WORKDIR /app

# Copia os arquivos do Maven para fazer o download das dependências primeiro (cache)
COPY rpg/.mvn/ .mvn
COPY rpg/mvnw rpg/pom.xml ./
RUN chmod +x mvnw

# Baixa as dependências offline para otimizar builds futuros
RUN ./mvnw dependency:go-offline

# Copia o código fonte e faz o build
COPY rpg/src ./src
RUN ./mvnw package -DskipTests

# Estágio 2: Imagem final apenas com o JRE para ser mais leve
FROM eclipse-temurin:17-jre-jammy
WORKDIR /app

# Copia o JAR gerado no estágio anterior
COPY --from=build /app/target/*.jar app.jar

# Expõe a porta 8080
EXPOSE 8080

# Comando para iniciar a aplicação
ENTRYPOINT ["java", "-jar", "app.jar"]
