FROM openjdk:17-jdk-alpine
ARG JAR_FILE=target/*.jar
COPY ./target/chat-bot-0.0.1-SNAPSHOT.jar app.jar
WORKDIR /app
ENV telegram.botName=
ENV telegram.botToken=
ENV google.sheet.name=
COPY src/main/resources/Divisions.xml Divisions.xml
ENTRYPOINT ["java", "-jar", "/app.jar"]