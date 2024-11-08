FROM openjdk:17-jdk-alpine
WORKDIR /opt/app

RUN apk add maven

COPY pom.xml    .
COPY ./src/     ./src/

RUN mvn compile && mvn package -Dmaven.test.skip
RUN mv ./target/*.jar ./flights_backend.jar && \
    rm -r ./src && \
    rm -r ./target;

ENTRYPOINT ["java", "-jar", "flights_backend.jar"]
EXPOSE 8080