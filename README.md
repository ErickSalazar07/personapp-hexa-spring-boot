# personapp-hexa-spring-boot
Plantilla Laboratorio Arquitectura Limpia

Instalar MariaDB en puerto 3307
Instalar MongoDB en puerto 27017

Ejecutar los scripts en las dbs

el adaptador rest corre en el puerto 3000
el swagger en http://localhost:3000/swagger-ui.html

Son dos adaptadores de entrada, 2 SpringApplication diferentes

Deben configurar el lombok en sus IDEs

Pueden hacer Fork a este repo, no editar este repositorio

---

# Laboratorio 2

Implementación de  Servicio WEB en Hexagonal con Repository y Service

Stack

- JDK 11
- Spring Boot
- MongoDB y MariaDB
- REST y CLI
- Swagger 3

Realizar endpoints para el CRUD del siguiente modelo de datos

```mermaid
---
config:
    layout: elk
---
erDiagram

    PROFESION ||--|{ ESTUDIOS : "pertenece"
    ESTUDIOS }|--||PERSONA : "tiene"
    PERSONA ||--|{ TELEFONO : "tiene"

PROFESION {
    id_profesion INT(6) PK
    nom VARCHAR(90)
    des TEXT
}

ESTUDIOS {
    id_estudios INT(6) PK
    id_prof INT(6) FK
    cc_per INT(15) FK
    fecha DATE
    univer VARCHAR(50)
}

%% genero: Enum('M','F')
PERSONA {
    cc INT(15) PK
    nombre VARCHAR(45)
    apellido VARCHAR(45)
    genero VARCHAR(1)
    edad INT(3)
}


TELEFONO {
    num VARCHAR(15) PK
    oper VARCHAR(15)
    duenio VARCHAR(15)
    cc_per INT(15) FK
}

```

se entrega:

- URL TAG del repositorio git (Github/Gitlab/Bitbucket/etc)
    - README con la configuración, pasos para configurar ambiente, compilación y despliegue.
    - Script DDL y DML
    - Código fuente.
- Documento:
    - Portada
    - Marco conceptual
    - Diseño
    - Procedimiento
    - Conclusiones y lecciones aprendidas
    - Referencias
