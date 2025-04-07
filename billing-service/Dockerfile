FROM gradle:8.10.0-jdk21 AS builder

WORKDIR /app

COPY . .

RUN chmod +x ./gradlew

RUN ./gradlew clean build -x test

FROM amazoncorretto:21

WORKDIR /app

COPY --from=builder /app/build/libs/billing-service-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 4001
EXPOSE 9001

ENTRYPOINT ["java", "-jar", "app.jar"]