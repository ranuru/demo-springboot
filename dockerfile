#build
FROM gradle:8.4-jdk21 AS builder
WORKDIR /app
COPY . .
RUN chmod +x ./gradlew
RUN ./gradlew clean bootJar --no-daemon

#run
FROM eclipse-temurin:21-jre
WORKDIR /app

RUN groupadd -r appuser && useradd -r -g appuser appuser

COPY --from=builder app/build/libs/*.jar app.jar

RUN chown -R appuser:appuser /app
USER appuser

ENTRYPOINT ["java","-jar","app.jar"]
