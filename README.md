# User Management

## user-service

Environment variables:
```
SPRING_DATASOURCE_URL=jdbc:postgresql://user-service-db:5432/db;
SPRING_DATASOURCE_USERNAME=admin_user;
SPRING_DATASOURCE_PASSWORD=password;
SPRING_JPA_HIBERNATE_DDL_AUTO=update;
SPRING_SQL_INIT_MODE=always
SPRING_KAFKA_BOOTSTRAP_SERVERS=kafka:9092;
BILLING_SERVICE_ADDRESS=billing-service
BILLING_SERVICE_GRPC_PORT=9001
```

## analytics-service

Environment variables:
```
SPRING_KAFKA_BOOTSTRAP_SERVERS=kafka:9092
```
