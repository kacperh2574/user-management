plugins {
    id("org.springframework.boot") version "3.4.4"
    id("io.spring.dependency-management") version "1.1.4"
    id("com.google.protobuf") version "0.9.4"
    java
}

group = "com.user"
version = "0.0.1-SNAPSHOT"
description = "user-service"

java {
    sourceCompatibility = JavaVersion.VERSION_21
    targetCompatibility = JavaVersion.VERSION_21
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.8.5")
    implementation("io.grpc:grpc-netty-shaded:1.69.0")
    implementation("io.grpc:grpc-protobuf:1.69.0")
    implementation("io.grpc:grpc-stub:1.69.0")
    implementation("net.devh:grpc-spring-boot-starter:3.1.0.RELEASE")
    implementation("com.google.protobuf:protobuf-java:4.29.1")
    implementation("org.springframework.kafka:spring-kafka:3.3.0")

    runtimeOnly("org.postgresql:postgresql")
    runtimeOnly("org.springframework.boot:spring-boot-devtools")

    compileOnly("org.projectlombok:lombok:1.18.36")
    compileOnly("javax.annotation:javax.annotation-api:1.3.2")
    annotationProcessor("org.projectlombok:lombok:1.18.36")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
    implementation("com.h2database:h2")
}

protobuf {
    protoc {
        artifact = "com.google.protobuf:protoc:3.25.5"
    }
    plugins {
        create("grpc") {
            artifact = "io.grpc:protoc-gen-grpc-java:1.68.1"
        }
    }
    generateProtoTasks {
        all().forEach {
            it.plugins {
                create("grpc")
            }
        }
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}