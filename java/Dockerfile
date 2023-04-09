FROM openjdk:17-jdk-alpine

ADD build/libs/*.jar /app/app.jar

WORKDIR /app

EXPOSE 8080

CMD ["java", "-jar", "app.jar"]