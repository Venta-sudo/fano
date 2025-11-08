FROM eclipse-temurin:17-jdk-focal AS builder

WORKDIR /app

# bind-mount ~/.m2 via docker-compose
ENV MAVEN_OPTS="-Dmaven.repo.local=/root/.m2"

COPY pom.xml .
COPY mvnw .
COPY .mvn .mvn

RUN mkdir -p src/main/java
RUN ./mvnw -q dependency:go-offline

COPY src src

RUN ./mvnw clean package -Dmaven.test.skip=true

FROM eclipse-temurin:17-jre-focal

WORKDIR /app

COPY --from=builder /app/target/*.jar app.jar

EXPOSE 8004

ENTRYPOINT ["java", "-jar", "app.jar"]
