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

## [Manager](./manager)
Central component with REST endpoints to add simulation instances for supported simulation types (i.e. types for which there are adaptors),
create sessions containing different simulation instances, executing commands for simulation instances contained in a session,
and fetch attributes from the simulation instances in the session.

## [User Interface](./simcomp-ui)
React project offering a more user-friendly interaction with the system/ the Manager.

## [Service Registry](./service-registry)
The service registry providing information about available adaptors and their supported actions.

## [Demo Adaptor](./demo-adaptor)
A demo adaptor showing integration with the service-registry.

To implement the uniform REST-interface for an adaptor, you have to follow below steps:

1. Add [commons](./commons/pom.xml) as a dependency and enable component-scanning for commons.
2. Provide the [config](./commons/src/main/java/at/jku/swe/simcomp/commons/registry/dto/ServiceRegistrationConfigDTO.java) as a config bean to the spring container.
3. Implement the [adaptor-endpoint-service](./commons/src/main/java/at/jku/swe/simcomp/commons/adaptor/endpoint/AdaptorEndpointService.java) as a spring bean.
4. Extend the [command-execution-visitor](./commons/src/main/java/at/jku/swe/simcomp/commons/adaptor/execution/command/CommandExecutionVisitor.java) and provide the implementation as a spring bean.

For step 4, the default-methods of the [interface](./commons/src/main/java/at/jku/swe/simcomp/commons/adaptor/execution/command/ExecutionCommandVisitor.java) throw an *UnsupportedOperationException*.
One should override the supported execution-commands that are configured in step 1.

For an example see the [demo-adaptor](./demo-adaptor):
1. [here](./demo-adaptor/pom.xml), [here](./demo-adaptor/src/main/java/at/jku/swe/simcomp/demoadaptor/DemoAdaptorApplication.java)
2. [here](./demo-adaptor/src/main/java/at/jku/swe/simcomp/demoadaptor/config/ServiceRegistrationConfig.java)
3. [here](./demo-adaptor/src/main/java/at/jku/swe/simcomp/demoadaptor/service/DemoAdaptorEndpointService.java)
4. and [here](./demo-adaptor/src/main/java/at/jku/swe/simcomp/demoadaptor/service/DemoCommandExecutionVisitor.java)

The implemented beans are injected into the [uniform controller](./commons/src/main/java/at/jku/swe/simcomp/commons/adaptor/endpoint/AdaptorEndpointController.java).
The endpoint also comes with uniform exception handling defined in the [exception-handler](./commons/src/main/java/at/jku/swe/simcomp/commons/adaptor/endpoint/AdaptorEndpointExceptionHandler.java).
All exceptions return a specific error-code and a message defined in the [HttpErrorDTO](./commons/src/main/java/at/jku/swe/simcomp/commons/HttpErrorDTO.java) and subclasses.
Implementations of an adaptor can add additional Exception-Handlers by creating their own ControllerAdvice.

When the execution of an operation fails, implementations of the adaptor are encouraged to throw a
[RoboOperationFailedException](./commons/src/main/java/at/jku/swe/simcomp/commons/adaptor/endpoint/exception/RoboOperationFailedException.java), or a more specific exception for that matter. Adaptors can initialize this exception with the [state](./commons/src/main/java/at/jku/swe/simcomp/commons/adaptor/dto/RoboStateDTO.java) of the roboter-arm in order to return the state and a message if desired.
This ensures consistent handling also for errors in arbitrary nested composite-commands with expressive error-messages.

## [Webots-Adaptor](./webots-adaptor) 
The adaptor for webots simulations.

## [Azure-Adaptor](./azure-adaptor) 
The adaptor for azure simulations.

## [Kinematics Service](./axis-converter)
Spring boot project with REST endpoints to perform direct and inverse kinematics for the Ned2.

## General Guidelines

### Logging
To ensure consistent logging across the microservices, we use the default framework of Spring, logback.
If a project includes lombok, a logger can be added with the @Slf4j annotation.
```
		<dependency>
			<groupId>org.projectlombok</groupId>
			<artifactId>lombok</artifactId>
			<optional>true</optional>
		</dependency>
```
For consistent log messages, a project can create [this](./service-registry/src/main/java/resources/logback.xml) in the resources folder of the Spring Boot project.
This creates a console appender for the INFO level and a file appender on the DEBUG level.

### Tests
Coverage reports are published on [github-pages](https://jku-swe-simcomp.github.io/simcomp-services/).
To add a report for a new Spring Boot project, include the jacoco-plugin (see [here](./manager/pom.xml) for an example), include the generated report in the [Github actions workflow](./.github/workflows/main.yml) and add a link to the [index.html](./.github/index.html).
Additionally, E2E tests are completed on every change to the docker-compose configuration (see section Deployment). 
Last completion time and details can also be found on github-pages.

### Java Docs
Java docs are also published on [github-pages](https://jku-swe-simcomp.github.io/simcomp-services/). 
To add a report for a new Spring Boot project, 
include the javadoc-plugin (see [here](./manager/pom.xml) for an example), include the generated report in the [Github actions workflow](./.github/workflows/main.yml) and add a link to the [index.html](./.github/index.html).


### Deployment
This repository is equipped with a Github Actions Continuous Integration pipeline,
which builds/tests all projects on every push to the main branch, pushes Docker images for all services to the Docker hub.
Check out the [local-deploy](https://github.com/jku-swe-simcomp/local-deploy) for a docker-compose based deployment of the whole system.
