FROM openjdk:23-jdk
VOLUME /tmp
COPY target/gateway-1.0.jar Gateway.jar
ENTRYPOINT ["java", "-jar", "Gateway.jar"]