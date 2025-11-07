FROM maven:3.9.9-eclipse-temurin-21 AS builder
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

FROM openjdk:25-ea-25-jdk-slim
COPY --from=builder target/*.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]