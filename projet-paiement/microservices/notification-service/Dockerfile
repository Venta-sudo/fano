FROM eclipse-temurin:17-jdk-focal AS builder

WORKDIR /app

COPY pom.xml .
COPY mvnw .
COPY .mvn .mvn

COPY src src

RUN ./mvnw clean package -Dmaven.test.skip=true

FROM eclipse-temurin:17-jre-focal

WORKDIR /app

COPY --from=builder /app/target/*.jar app.jar

EXPOSE 8004

ENTRYPOINT ["java", "-jar", "app.jar"]
