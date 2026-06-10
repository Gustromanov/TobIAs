# ETAPA 1: Construção (Build) usando Java 21
FROM maven:3.9.6-eclipse-temurin-21 AS builder
WORKDIR /app

# Copia o pom.xml e baixa as dependências (otimiza o cache)
COPY pom.xml .
RUN mvn dependency:go-offline

# Copia o código e gera o arquivo .war
COPY src ./src
RUN mvn clean package -DskipTests

# ETAPA 2: Rodar o servidor Tomcat
# Como você usa Java 21, o ideal é o Tomcat 10 ou 11
FROM tomcat:10.1-jdk21-temurin
WORKDIR /usr/local/tomcat/webapps/

RUN apt-get update \
    && apt-get install -y --no-install-recommends maven \
    && rm -rf /var/lib/apt/lists/*

# Garante que nenhum deploy/cache antigo do Tomcat sobreviva na imagem final
RUN rm -rf /usr/local/tomcat/webapps/* \
    /usr/local/tomcat/work/* \
    /usr/local/tomcat/temp/*

# Copia e expande o .war gerado na etapa 1
COPY --from=builder /app/target/*.war /tmp/ROOT.war
RUN mkdir -p ./ROOT \
    && cd ./ROOT \
    && jar -xf /tmp/ROOT.war \
    && rm /tmp/ROOT.war

COPY docker/start-tomcat-dev.sh /usr/local/bin/start-tomcat-dev.sh
RUN chmod +x /usr/local/bin/start-tomcat-dev.sh

EXPOSE 8080

CMD ["start-tomcat-dev.sh"]
