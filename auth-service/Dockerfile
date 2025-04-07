FROM gradle:8.10.0-jdk21 AS builder

WORKDIR /app

COPY . .

RUN chmod +x ./gradlew

RUN ./gradlew clean build -x test

FROM amazoncorretto:21

WORKDIR /app

COPY --from=builder /app/build/libs/auth-service-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 4005

ENTRYPOINT ["java", "-jar", "app.jar"]