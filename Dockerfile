# Финальный образ на openjdk
FROM openjdk:17-jdk-slim
WORKDIR /skuska

COPY /target/skuska-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
