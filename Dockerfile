FROM openjdk:17
VOLUME /flightsBackend
ADD target/*.jar flights_backend.jar
ENTRYPOINT ["java", "-jar", "/flights_backend.jar"]
EXPOSE 8080