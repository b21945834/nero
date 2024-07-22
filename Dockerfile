FROM openjdk:17
ADD target/nero-0.0.1-SNAPSHOT.jar
ENTRYPOINT ["java", "-jar", "nero-0.0.1-SNAPSHOT.jar"]