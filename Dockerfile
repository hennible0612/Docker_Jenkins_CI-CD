# 기본 이미지로 OpenJDK 17을 사용
FROM openjdk:17-jdk-slim
WORKDIR /app
ARG JAR_FILE=/build/libs/logTest-0.0.1-SNAPSHOT.jar
COPY ${JAR_FILE} /app/logTest.jar
ENTRYPOINT ["java", "-jar", "/app/logTest.jar"]
