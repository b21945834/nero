FROM openjdk:17
EXPOSE 8080
ADD target/nero-app.jar nero-app.jar
ENTRYPOINT ["java", "-jar", "nero-0.0.1-SNAPSHOT.jar"]