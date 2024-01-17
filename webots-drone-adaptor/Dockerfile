FROM openjdk:17-slim

RUN mkdir /home/app
WORKDIR /home/app
COPY target/webots-drone-adaptor-1.0.jar .

CMD ["java", "-jar", "webots-drone-adaptor-1.0.jar"]