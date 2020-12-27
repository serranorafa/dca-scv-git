# Aplicación inicial ToDoList

Aplicación ToDoList de la asignatura [MADS](https://cvnet.cpd.ua.es/Guia-Docente/GuiaDocente/Index?wcodest=C203&wcodasi=34037&wlengua=es&scaca=2019-20) usando Spring Boot y plantillas Thymeleaf.

## Requisitos

Necesitas tener instalado en tu sistema:

- Java 8

## Ejecución

Puedes ejecutar la aplicación usando el _goal_ `run` del _plugin_ Maven 
de Spring Boot:

```
$ ./mvnw spring-boot:run 
```   

También puedes generar un `jar` y ejecutarlo:

```
$ ./mvnw package
$ java -jar target/mads-todolist-serranorafa-1.2.0.jar 
```

Una vez lanzada la aplicación puedes abrir un navegador y probar la página de inicio accediendo a:

- [http://localhost:8080/login](http://localhost:8080/login)

## Otros

Tablero de Trello con el *backlog* de historias de usuario:

- [https://trello.com/b/cNJQVRNZ/todolist-mads](https://trello.com/b/cNJQVRNZ/todolist-mads)
