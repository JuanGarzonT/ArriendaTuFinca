FROM openjdk:21

COPY . /app

WORKDIR /app

RUN ./mvnw clean install -DskipTests

CMD ["java", "-jar", "target/proyecto-0.0.1-SNAPSHOT.jar"]

EXPOSE 9090