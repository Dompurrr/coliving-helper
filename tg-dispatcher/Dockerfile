FROM openjdk:11 as build

WORKDIR /app

COPY .mvn/ .mvn
COPY mvnw pom.xml ./
COPY src ./src

RUN ["./mvnw", "install"]

ENTRYPOINT ["java", "-jar", "./target/tg-dispatcher.jar"]