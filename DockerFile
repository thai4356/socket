FROM maven:3.9.9-eclipse-temurin-21 AS build
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests

FROM openjdk:21-slim
WORKDIR /app
COPY --from=build /app/target/demo123-0.0.1-SNAPSHOT.jar demo123.jar
ENTRYPOINT ["java", "-jar", "demo123.jar", "--server.port=${PORT}"]
