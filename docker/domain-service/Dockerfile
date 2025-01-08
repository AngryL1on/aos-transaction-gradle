FROM openjdk:17-jdk-alpine
WORKDIR /app

COPY domain-service-0.0.1-SNAPSHOT.jar /app/domain-service.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "/app/domain-service.jar"]
