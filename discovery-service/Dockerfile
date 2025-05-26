FROM openjdk:23-jdk
VOLUME /tmp
COPY target/discovery-service-1.0.jar DiscoveryService.jar
ENTRYPOINT ["java", "-jar", "DiscoveryService.jar"]
