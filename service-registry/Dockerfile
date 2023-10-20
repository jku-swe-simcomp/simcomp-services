FROM openjdk:17-slim

RUN mkdir /home/app
WORKDIR /home/app
COPY target/service-registry-1.0.jar .

CMD ["java", "-jar", "service-registry-1.0.jar"]
