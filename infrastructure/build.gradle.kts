plugins {
    application
    java
}

group = "com.user"
version = "1.0-SNAPSHOT"

java {
    sourceCompatibility = JavaVersion.VERSION_21
    targetCompatibility = JavaVersion.VERSION_21
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("software.amazon.awscdk:aws-cdk-lib:2.187.0")
    implementation("com.amazonaws:aws-java-sdk:1.12.782")
}

application {
    mainClass.set("com.user.stack.LocalStack")
}