FROM maven:3.6.3-jdk-11 AS builder
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

FROM openjdk:25-ea-25-jdk-slim
COPY --from=builder target/*.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]