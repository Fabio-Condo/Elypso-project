FROM openjdk:11-jre

WORKDIR /app

COPY target/*.jar /app/spc.jar

EXPOSE 8080

CMD ["java", "-jar", "spc.jar"]