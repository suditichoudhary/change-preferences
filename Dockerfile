FROM openjdk:8-jdk-alpine
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} update-m.jar
ENTRYPOINT ["java","-jar","/update-m.jar"]
