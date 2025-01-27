FROM gradle:8.11.1-jdk17 AS builder

RUN apt update && apt install -qq -y --no-install-recommends

WORKDIR /app

COPY . .

RUN gradle clean bootJar

FROM openjdk:17-jdk-slim

WORKDIR /app

COPY --from=builder /app/build/libs/Bingo-API-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java",  "-jar", "app.jar"]