FROM maven:3.9.8-eclipse-temurin-17 AS build
WORKDIR /app
COPY pom.xml .
RUN mvn clean verify --fail-never
COPY src ./src
RUN mvn clean package -DskipTests

FROM eclipse-temurin:17-jre
WORKDIR /app
COPY --from=build /app/target/*.jar beach-product-rental-0.0.1-SNAPSHOT.jar
EXPOSE 8080
ENV TZ=America/Sao_Paulo
ENV JAVA_OPTS=""
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar /app/beach-product-rental-0.0.1-SNAPSHOT.jar"]
