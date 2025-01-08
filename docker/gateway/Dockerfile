FROM openjdk:17-jdk-alpine

WORKDIR /app

COPY gateway-0.0.1-SNAPSHOT.jar /app/gateway.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "/app/gateway.jar"]
