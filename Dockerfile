FROM openjdk:17
EXPOSE 8080
ADD target/nero-app.jar nero-app.jar
ENTRYPOINT ["java", "-jar", "nero-app.jar"]