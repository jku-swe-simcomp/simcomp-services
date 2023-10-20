FROM openjdk:17-slim

RUN mkdir /home/app
WORKDIR /home/app
COPY target/demo-adaptor-1.0.jar .

CMD ["java", "-jar", "demo-adaptor-1.0.jar"]
