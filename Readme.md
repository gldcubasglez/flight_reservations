# API para reservación de vuelos

Mediante la siguiente API se podrán realizar las siguientes operaciones:

* Generar datos de vuelos y asientos predeterminados.
* Realizar una reservación.
* Cancelar una reservación.
* Listar los asientos disponibles de un vuelo.
* Listar las reservaciones confirmadas.

Datos técnicos:

* Java 21
* Spring Boot 3
* PostgresSQL
* JPA + Hibernate
* RabbitMQ
* OpenApi / Swagger
* JUnit


## Swagger

```
http://localhost:8080/public/swagger-ui/index.html
```

## Datos predeterminados

Se implementó un endpoint para generar datos por defecto con información de vuelos y asientos. También se creó una reservación que ocupa todos los asientos de un vuelo para la ejecución de algunos tests.

## Pruebas

Se desarrollaron suites de pruebas unitarias y de integración.

## Datos Personales

Gleidy Cubas González

gldcubasglez@gmail.com

