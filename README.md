# simcomp-services
Mono-Repository for all provided services.

Exposed REST Apis are documented using Swagger at {SERVICE_HOST}:{SERVICE_PORT}/swagger-ui.html

## [Commons](./commons)
Package with utility classes, DTOs which are common to different services.

## [Service Registry](./service-registry)
The service registry managing available adaptors and their endpoints.

## [Demo Adaptor](./demo-adaptor)
A demo adaptor showing integration with the service-registry.
