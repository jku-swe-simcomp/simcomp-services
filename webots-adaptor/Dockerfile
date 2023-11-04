FROM openjdk:17-slim

RUN mkdir /home/app
WORKDIR /home/app
COPY target/webots-adaptor-1.0.jar .

CMD ["java", "-jar", "webots-adaptor-1.0.jar"]