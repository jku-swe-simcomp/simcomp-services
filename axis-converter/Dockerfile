FROM openjdk:17-slim

RUN mkdir /home/app
WORKDIR /home/app
COPY target/axis-converter-1.0.jar .

CMD ["java", "-jar", "axis-converter-1.0.jar"]
