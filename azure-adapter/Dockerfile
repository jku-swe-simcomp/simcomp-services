FROM openjdk:17-slim

RUN mkdir /home/app
WORKDIR /home/app
COPY target/azure-adapter-1.0.0.jar .

CMD ["java", "-jar", "azure-adapter-1.0.0.jar"]