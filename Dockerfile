FROM openjdk:17
WORKDIR /app
COPY target/fix-now-api.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
