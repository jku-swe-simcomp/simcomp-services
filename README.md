# simcomp-services
Mono-Repository for all provided services.

Exposed REST Apis are documented using Swagger at {SERVICE_HOST}:{SERVICE_PORT}/swagger-ui.html

## [Commons](./commons)
Package with utility classes, DTOs which are common to different services.
Services can integrate commons with the following maven dependency:
```
<!--	Commons-->
<dependency>
  <groupId>at.jku.swe.simcomp</groupId>
  <artifactId>commons</artifactId>
  <version>1.0</version>
</dependency>
```
**Note:** When working locally, you might have to build the project before this works, execute this command in the [commons-directory](./commons):
```
mvn clean install
```

## [Service Registry](./service-registry)
The service registry managing available adaptors and their endpoints.

## [Demo Adaptor](./demo-adaptor)
A demo adaptor showing integration with the service-registry.

To implement the uniform REST-interface for an adaptor, you have to follow below steps:

1. Add [commons](./commons/pom.xml) as a dependency and enable component-scanning for commons.
2. Provide the [config](./commons/src/main/java/at/jku/swe/simcomp/commons/registry/dto/ServiceRegistrationConfigDTO.java) as a config bean to the spring container.
3. Implement the [adaptor-endpoint-service](./commons/src/main/java/at/jku/swe/simcomp/commons/adaptor/endpoint/AdaptorEndpointService.java) as a spring bean.
4. Implement the [execution-command-visitor](./commons/src/main/java/at/jku/swe/simcomp/commons/adaptor/execution/command/ExecutionCommandVisitor.java) as a spring bean.

For step 4, the default-methods of the interface throw an *UnsupportedOperationException*.
One should override the supported execution-commands that are configured in step 1.

For an example see the [demo-adaptor](./demo-adaptor):
1. [here](./demo-adaptor/pom.xml), [here](./demo-adaptor/src/main/java/at/jku/swe/simcomp/demoadaptor/DemoAdaptorApplication.java)
2. [here](./demo-adaptor/src/main/java/at/jku/swe/simcomp/demoadaptor/config/ServiceRegistrationConfig.java)
3. [here](./demo-adaptor/src/main/java/at/jku/swe/simcomp/demoadaptor/service/DemoAdaptorEndpointService.java)
4. and [here](./demo-adaptor/src/main/java/at/jku/swe/simcomp/demoadaptor/service/DemoExecutionCommandVisitor.java)

The implemented beans are injected into the [uniform controller](./commons/src/main/java/at/jku/swe/simcomp/commons/adaptor/endpoint/AdaptorEndpointController.java).
The endpoint also comes with uniform exception handling defined in the [exception-handler](./commons/src/main/java/at/jku/swe/simcomp/commons/adaptor/endpoint/AdaptorEndpointExceptionHandler.java).
All exceptions return a specific error-code and a message defined in the [HttpErrorDTO](./commons/src/main/java/at/jku/swe/simcomp/commons/HttpErrorDTO.java).
Implementations of an adaptor can add additional Exception-Handlers by creating their own ControllerAdvice.

When the execution of an operation fails, implementations of the adaptor are encouraged to throw a
[RoboOperationFailedException](./commons/src/main/java/at/jku/swe/simcomp/commons/adaptor/endpoint/exception/RoboOperationFailedException.java), or a more specific exception for that matter. Adaptors can initialize this exception with the [state](./commons/src/main/java/at/jku/swe/simcomp/commons/adaptor/dto/RoboStateDTO.java) of the roboter-arm in order to return the state and a message if desired.
This ensures consistent handling also for errors in arbitrary nested composite-commands with expressive error-messages.

