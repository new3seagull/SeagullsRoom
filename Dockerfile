FROM openjdk:17-jdk
ARG JAR_FILE=SeagullsRoom/build/libs/*.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java", "-Dspring.profiles.active=docker", "-jar", "app.jar"]