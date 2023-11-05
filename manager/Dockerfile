FROM openjdk:17-slim

RUN mkdir /home/app
RUN mkdir /home/app/logs
WORKDIR /home/app
COPY target/manager-1.0.jar .

CMD ["java", "-jar", "manager-1.0.jar"]
