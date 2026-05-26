// ========================================
// TEMPLATE DOCUMENTO ACADÉMICO EN TYPST
// ========================================

#set text(lang: "es", region: "es")

#set page(
  paper: "a4",
  margin: (x: 3cm, y: 2.5cm),
)

#set text(
  font: "Times New Roman",
  size: 12pt,
)

#set par(
  justify: true,
  leading: 0.8em,
)

#set heading(
  numbering: "1.",
)

#show heading.where(level: 1): it => {
  set align(left)
  text(weight: "bold", size: 16pt)[#it.body]
  linebreak()
}

// ========================================
// PORTADA (SIN NUMERACIÓN)
// ========================================

#set page(numbering: none)

#align(center)[
  #v(2cm)

  #text(size: 26pt, weight: "bold")[ Arquitectura Software ]

  #v(2cm)

  #text(size: 22pt, weight: "bold")[ Laboratorio 02 ]

  #v(4cm)

  #text(size: 16pt)[
    Augusto Pedicino Florez \
    Erick Salazar Suarez \
    Felipe Garrido Flores
  ]

  #v(8cm)

  #text(size: 15pt)[
    Pontificia Universidad Javeriana \
    #datetime.today().display()
  ]
]

// ========================================
// INICIO DE NUMERACIÓN
// ========================================

#pagebreak()

#outline()

#pagebreak()

#set page(
  numbering: "1",
  number-align: center,
)
// ========================================
// MARCO CONCEPTUAL
// ========================================

= Marco conceptual

== Tecnologías

=== Java

Java es un lenguaje de programación orientado a objetos, de propósito general y fuertemente tipado, desarrollado originalmente por Sun Microsystems y
actualmente mantenido por Oracle. Una de sus características más destacadas es su portabilidad: el código Java se compila a _bytecode_, que es ejecutado
por la Máquina Virtual de Java (JVM), permitiendo que una misma aplicación corra en cualquier sistema operativo sin modificaciones.

En el contexto del laboratorio, Java actúa como el lenguaje base de toda la aplicación. Su sistema de tipos estricto, su soporte para interfaces y su
ecosistema de herramientas lo hacen especialmente adecuado para implementar los principios de la arquitectura hexagonal, donde la separación entre
contratos (interfaces) e implementaciones es fundamental.

=== Spring Boot

Spring Boot es un framework de desarrollo de aplicaciones Java construido sobre el ecosistema Spring. Su principal objetivo es simplificar la
configuración y el arranque de aplicaciones, eliminando la necesidad de configuración XML extensa mediante convenciones y autoconfiguración. Permite crear
aplicaciones listas para producción con un servidor embebido (Tomcat o Netty) y una estructura de proyecto estandarizada.

Para este laboratorio, Spring Boot provee el contenedor de inyección de dependencias (IoC), la integración con JPA para MariaDB, la integración con Spring
Data MongoDB, la creación y definición de una CLI (Command Line Interface) o interfaz por linea de comandos, todo esto utilizando anotaciones como
`@SpringBootApplication` y realizando la implementación de la interfaz `CommandLineRunner`. También tiene soporte para exponer servicios REST mediante
anotaciones como `@RestController`. La autoconfiguración de Spring Boot permite conectar los adaptadores de entrada y salida con el núcleo de dominio de
forma declarativa, sin acoplar las capas entre sí.

=== Maven

Maven es una herramienta de gestión y construcción de proyectos Java. Permite declarar las dependencias de un proyecto en un archivo `pom.xml` (Project
Object Model), gestionar su descarga desde repositorios centrales, y definir el ciclo de vida del build (compilación, pruebas, empaquetado e instalación).
Además, soporta proyectos multi-módulo, donde un POM padre agrega varios módulos hijos con dependencias entre ellos.

En el laboratorio, Maven organiza los ocho módulos del proyecto en un reactor multi-módulo. El POM raíz define las propiedades y dependencias compartidas,
mientras que cada módulo (dominio, aplicación, adaptadores de entrada y salida) tiene su propio `pom.xml` que hereda del padre. Esto garantiza que las
versiones de las dependencias sean consistentes en todo el proyecto y que el orden de compilación sea el correcto.

=== MariaDB

MariaDB es un sistema de gestión de bases de datos relacional (RDBMS) de código abierto, derivado de MySQL. Organiza la información en tablas con filas y
columnas, y permite definir relaciones entre entidades mediante claves primarias y foráneas. Utiliza SQL estándar para las operaciones de definición (DDL)
y manipulación de datos (DML).

En el laboratorio, MariaDB almacena las entidades del dominio (persona, profesión, teléfono y estudios) en tablas relacionales con integridad referencial.
La integración con Spring Boot se realiza mediante Spring Data JPA e Hibernate como proveedor ORM, que mapea las entidades Java a tablas de la base de
datos sin necesidad de escribir SQL manualmente para las operaciones básicas.

=== MongoDB

MongoDB es un sistema de gestión de bases de datos NoSQL orientado a documentos. En lugar de tablas y filas, almacena la información en documentos con
formato BSON (Binary JSON), agrupados en colecciones. Esta estructura flexible permite almacenar datos sin un esquema fijo, lo que facilita la evolución
del modelo de datos sin migraciones complejas.

En el laboratorio, MongoDB almacena las mismas entidades del dominio que MariaDB, pero representadas como documentos en colecciones. La integración con
Spring Boot se realiza mediante Spring Data MongoDB, que permite definir repositorios con métodos de consulta declarativos sobre las colecciones. La
coexistencia de ambas bases de datos en la misma aplicación ilustra cómo la arquitectura hexagonal desacopla el dominio de la tecnología de persistencia
utilizada.

== Patrón Arquitectónico

=== Arquitectura Hexagonal

La arquitectura hexagonal, también conocida como _Ports and Adapters_ y propuesta por Alistair Cockburn, es un estilo arquitectónico cuyo objetivo
principal es aislar la lógica de negocio del dominio de cualquier tecnología externa, ya sea una base de datos, una interfaz de usuario, un servicio REST
o una línea de comandos. La idea central es que el dominio no debe conocer ni depender de los detalles de infraestructura: son los adaptadores externos
los que se adaptan al dominio, y no al revés.

El nombre "hexagonal" no tiene un significado matemático específico: el hexágono representa visualmente el núcleo de la aplicación con múltiples puntos de
conexión hacia el exterior. Cada lado del hexágono puede conectarse a un adaptador diferente, lo que hace explícito que la aplicación puede ser conducida
por múltiples tecnologías de entrada y salida de forma simultánea e intercambiable.

==== Estructura y responsabilidades

La arquitectura se organiza en las siguientes capas o módulos, cada uno con una responsabilidad bien definida:

- *Common:* Contiene elementos transversales compartidos por todos los módulos, como anotaciones personalizadas (`@UseCase`, `@Adapter`, `@Port`,
  `@Mapper`), excepciones comunes y enumeraciones de configuración. No depende de ningún otro módulo del proyecto.

- *Domain:* Es el núcleo de la aplicación. Contiene las entidades del negocio (como `Person`, `Phone`, `Study`, `Profession`). También define las
  interfaces de los puertos de entrada (`PersonInputPort`) y de salida (`PersonOutputPort`), que representan los contratos que el dominio expone hacia el
  exterior.

- *Application:* Contiene los casos de uso, que son las implementaciones de los puertos de entrada. Clases como `PersonUseCase` orquestan la lógica de
  negocio invocando los puertos de salida sin conocer qué tecnología concreta los implementa. Este módulo depende únicamente de `domain` y `common`.

- *Output Adapters (maria-output-adapter, mongo-output-adapter):* Implementan los puertos de salida definidos en el dominio. Cada adaptador traduce entre
  el modelo de dominio y el modelo de persistencia específico de su tecnología (entidades JPA para MariaDB, documentos para MongoDB), utilizando clases
  _mapper_ dedicadas.

- *Input Adapters (rest-input-adapter, cli-input-adapter):* Implementan los puertos de entrada desde el exterior. El adaptador REST expone endpoints HTTP
  mediante controladores Spring, mientras que el adaptador CLI presenta un menú interactivo en consola. Ambos traducen las solicitudes externas en
  llamadas a los casos de uso del módulo `application`.

==== Comunicación entre capas

El flujo de una solicitud sigue siempre la misma dirección: desde el adaptador de entrada hacia el dominio, y desde el dominio hacia el adaptador de
salida, nunca al revés.

Cuando un usuario realiza una petición (ya sea por REST o por el menú CLI), el adaptador de entrada correspondiente la recibe y la transforma al modelo
del dominio. Luego invoca el caso de uso apropiado del módulo `application`, eligiendo previamente qué adaptador de salida utilizar (MariaDB o MongoDB)
mediante el método `setPersistencia()`. El caso de uso ejecuta la lógica de negocio y delega la operación de persistencia al puerto de salida. El
adaptador de salida seleccionado traduce la entidad de dominio a su representación específica (entidad JPA o documento MongoDB) y ejecuta la operación en
la base de datos. La respuesta recorre el camino inverso hasta llegar al usuario.

Esta comunicación unidireccional y mediada por interfaces garantiza que ninguna capa conozca los detalles de implementación de las demás, cumpliendo el
principio de inversión de dependencias.

=== Patrón Adaptador

El patrón Adaptador (_Adapter_) es un patrón de diseño estructural que permite que dos interfaces incompatibles trabajen juntas. Su función es actuar como
intermediario entre un cliente que espera una interfaz específica y una clase que ofrece una interfaz diferente, traduciendo las llamadas de una a otra
sin modificar ninguna de las dos partes.

En este proyecto, el patrón Adaptador se aplica de forma explícita en los adaptadores de entrada y salida. Los puertos del dominio definen contratos
(interfaces) como `PersonOutputPort`, y las implementaciones concretas como `PersonOutputAdapterMaria` o `PersonOutputAdapterMongo` adaptan esos contratos
a las APIs específicas de JPA y Spring Data MongoDB respectivamente. De esta forma, el dominio puede invocar `save()` o `find()` sin saber si por debajo
se está ejecutando una consulta SQL en MariaDB o una operación sobre una colección en MongoDB.

Lo mismo ocurre en los adaptadores de entrada: `PersonaInputAdapterRest` y `PersonaInputAdapterCli` adaptan las interfaces externas (HTTP y consola
respectivamente) al contrato que exige el puerto de entrada `PersonInputPort`, traduciendo los DTOs REST o los datos del teclado en entidades de dominio.

=== Patrón Repositorio

El patrón Repositorio (_Repository_) es un patrón de diseño que abstrae el acceso a los datos detrás de una interfaz orientada a colecciones. Su objetivo
es desacoplar la lógica de negocio de los mecanismos de persistencia, permitiendo que los casos de uso trabajen con objetos de dominio sin conocer cómo ni
dónde se almacenan.

En este proyecto, el patrón Repositorio se aplica en ambos output adapters. Para MariaDB, interfaces como `PersonaRepositoryMaria` extienden
`JpaRepository` de Spring Data, que genera automáticamente las implementaciones de los métodos CRUD en tiempo de ejecución. Para MongoDB,
`PersonaRepositoryMongo` extiende `MongoRepository`, cumpliendo el mismo rol sobre colecciones de documentos. Los output adapters delegan en estos
repositorios todas las operaciones de persistencia, manteniendo la lógica de traducción (mappers) separada de la lógica de acceso a datos.


// ========================================
// DISEÑO
// ========================================

= Diseño

== Arquitectura

La aplicación sigue una arquitectura hexagonal (_Ports and Adapters_), organizada como un proyecto multi-módulo de Maven. El núcleo de la aplicación es el
dominio, que contiene las entidades de negocio y los contratos (puertos) que definen cómo se comunica con el exterior. Alrededor del dominio se ubican los
adaptadores de entrada, que reciben solicitudes desde el mundo exterior, y los adaptadores de salida, que se comunican con los sistemas de persistencia.
Ninguna capa interna conoce los detalles de implementación de las capas externas: toda comunicación se realiza a través de interfaces.

La aplicación puede ser conducida por dos adaptadores de entrada de forma independiente: una API REST (disponible en el puerto 3000) y una interfaz de
línea de comandos (CLI) con menú interactivo. Ambos adaptadores comparten el mismo núcleo de dominio y los mismos casos de uso, y pueden enrutar sus
operaciones hacia cualquiera de los dos motores de persistencia disponibles (MariaDB o MongoDB) en tiempo de ejecución, sin modificar el código del
dominio.

#figure(
  table(
    columns: (auto, auto, auto),
    inset: 8pt,
    align: horizon,
    stroke: 0.5pt + gray,
    table.header([*Módulo*], [*Tecnología*], [*Responsabilidad*]),

    [`domain`], [Java puro], [Entidades de negocio y contratos (puertos de entrada y salida)],
    [`application`], [Spring (IoC)], [Casos de uso: orquestación de la lógica de negocio],
    [`common`], [Java puro], [Anotaciones, excepciones y utilidades compartidas],
    [`rest-input-adapter`], [Spring Web / REST], [Endpoints HTTP, serialización JSON],
    [`cli-input-adapter`], [Spring Boot CLI], [Menú interactivo por consola],
    [`maria-output-adapter`], [Spring Data JPA / MariaDB], [Persistencia relacional],
    [`mongo-output-adapter`], [Spring Data MongoDB], [Persistencia documental],
  ),
  caption: [Módulos de la arquitectura hexagonal y tecnologías asociadas],
)

=== Dualidad REST y CLI

Una decisión central del diseño fue implementar dos adaptadores de entrada independientes que exponen la misma lógica de negocio mediante interfaces
distintas:

- *Adaptador REST (`rest-input-adapter`):* Contiene controladores Spring anotados con `@RestController` que reciben peticiones HTTP y retornan respuestas
  en formato JSON. Es el punto de entrada para clientes externos, aplicaciones web o herramientas de prueba como Swagger UI.

- *Adaptador CLI (`cli-input-adapter`):* Implementa `CommandLineRunner` de Spring Boot y presenta un menú jerárquico interactivo en la consola. Permite
  operar sobre las mismas entidades (Persona, Profesión, Teléfono, Estudio) seleccionando además el motor de persistencia (MariaDB o MongoDB) en cada
  sesión.

Ambos adaptadores inyectan los mismos casos de uso del módulo `application` a través de los puertos de entrada definidos en `domain`, garantizando que la
lógica de negocio subyacente sea idéntica independientemente del canal de acceso utilizado.

=== Dualidad de persistencia

Otra decisión relevante fue soportar dos motores de persistencia de forma simultánea dentro de la misma ejecución de la aplicación. El adaptador de salida
para MariaDB (`maria-output-adapter`) utiliza Spring Data JPA con Hibernate como ORM, mapeando las entidades del dominio a tablas relacionales. El
adaptador de salida para MongoDB (`mongo-output-adapter`) utiliza Spring Data MongoDB, representando las mismas entidades como documentos en colecciones.

La selección del motor de persistencia activo no requiere reiniciar la aplicación ni cambiar configuración: el adaptador de entrada invoca
`setPersistencia()` sobre el caso de uso antes de cada operación, inyectando el puerto de salida correspondiente. Esto es posible gracias a que ambos
adaptadores implementan la misma interfaz `PersonOutputPort` (y sus equivalentes para las demás entidades), definida en el dominio.

== Diagramas

=== HLD

#image("HLD.jpeg")

=== Diagrama Entidad Relación

#align(center)[ #image("erd.png", height: 85%) ]

=== C4 model

==== Diagrama de contexto

#image("c4-contexto.jpeg")

==== Diagrama de contenedores

#image("c4-contenedores.jpeg")

==== Diagrama de componentes

#image("c4-componentes.jpeg")

==== Diagrama de codigo

// #image("c4_codigo.jpeg")

==== Diagrama de despliegue

// #align(center)[ #image("c4_despliegue.jpeg", height: 85%) ]

==== Diagrama de secuencia/dinámico

// #align(center)[ #image("c4_dinamico.jpeg", height: 85%) ]

==== Diagrama de System landscape

// #image("c4_system_landscape.jpeg")

== Decisiones de diseño

=== Input-adapter REST y CLI

Una decisión central del diseño fue implementar dos adaptadores de entrada independientes que exponen la misma lógica de negocio a través de interfaces
distintas. Esto es posible gracias a que ambos adaptadores dependen únicamente de los puertos de entrada definidos en el dominio, sin conocer los detalles
de persistencia ni de los otros adaptadores.

El *adaptador REST* (`rest-input-adapter`) expone los casos de uso como endpoints HTTP mediante controladores Spring anotados con `@RestController`. Cada
controlador recibe peticiones HTTP, las transforma al modelo de dominio y delega la operación al caso de uso correspondiente, retornando la respuesta
serializada en JSON. Este adaptador incluye además documentación automática de la API mediante Swagger UI (`springdoc-openapi`).

El *adaptador CLI* (`cli-input-adapter`) implementa la interfaz `CommandLineRunner` de Spring Boot, lo que hace que el menú interactivo se ejecute
automáticamente al arrancar la aplicación. El menú permite al usuario seleccionar primero el motor de persistencia (MariaDB o MongoDB) y luego la
operación a realizar sobre cada entidad. Esta selección en tiempo de ejecución se realiza mediante el método `setPersistencia()` del caso de uso, que
intercambia el puerto de salida activo sin reiniciar la aplicación.

Ambos adaptadores comparten los mismos casos de uso del módulo `application` y pueden enrutar sus operaciones hacia cualquiera de los dos motores de
persistencia disponibles, lo que demuestra el principio fundamental de la arquitectura hexagonal: el dominio es independiente de la tecnología de entrada
y de salida.


// ========================================
// PROCEDIMIENTO
// ========================================

= Procedimiento

== Estructura Hexagonal

Las entidades de dominio (*domain-entities*) representan los objetos centrales del negocio: `Person`, `Phone`, `Profession` y `Study`. Son clases Java
puras, sin anotaciones de frameworks, que encapsulan tanto los datos como las reglas de negocio básicas mediante métodos de validación en sus métodos de
modificación. Al no depender de ninguna tecnología externa, pueden ser reutilizadas por cualquier adaptador.

#figure(
  image("domain-entities.png"),
  caption: [Definición de *domain-entites* o entidades de dominio.]
)

Los *in-ports* (puertos de entrada) son interfaces que definen el contrato que deben cumplir los adaptadores de entrada (CLI, REST, GraphQL, etc.) para
interactuar con los casos de uso. Cada puerto de entrada declara las operaciones disponibles sobre una entidad, como `create`, `edit`, `drop`, `findAll` y
`findOne`, sin exponer ningún detalle de implementación.

#figure(
  image("in-port.png"),
  caption: [Configuración de puertos de entrada.]
)

Los *out-ports* (puertos de salida) son interfaces que definen el contrato que deben cumplir los adaptadores de salida (MariaDB, MongoDB, etc.) para
persistir o recuperar datos. El dominio los utiliza para delegar las operaciones de persistencia sin conocer qué tecnología concreta los implementa, lo
que permite intercambiar la base de datos sin modificar la lógica de negocio.

#figure(
  image("out-port.png"),
  caption: [Configuración de puertos de salida.]
)

Los *usecases* (casos de uso) implementan los puertos de entrada y contienen la lógica de orquestación del negocio. Reciben un puerto de salida por
constructor (inyectado por Spring) y lo utilizan para delegar las operaciones de persistencia. El método `setPersistencia()` permite cambiar el motor de
base de datos en tiempo de ejecución, lo que es invocado por los adaptadores de entrada antes de cada operación.

#figure(
  image("usecase.png"),
  caption: [Configuración de *usecases* o casos de uso.]
)

El archivo `pom.xml` global, ubicado en la raíz del proyecto, actúa como POM padre del reactor multi-módulo Maven. Define las propiedades compartidas
(versiones de Java, Spring Boot, dependencias comunes como Lombok) y lista todos los módulos del proyecto. Incluye el plugin `flatten-maven-plugin`, que
resuelve la propiedad `${revision}` en todos los POMs hijos al momento del build, permitiendo que cada módulo pueda ser ejecutado de forma independiente
con `mvn spring-boot:run -pl` sin errores de resolución de dependencias.

#figure(
  image("pom-xml.png"),
  caption: [Configuración de archivo global `pom.xml` (archivo en la raíz del proyecto).]
)

== Configuración de la base de datos y creación de entidades y relaciones

Para la configuración de la base de datos, se utilizaron los DDL (Data Definition Language) y para el inserción de datos se usaron los DML (Data
Manipulation Language)

=== MariaDB

El *output-adapter* de MariaDB (`ProfessionOutputAdapterMaria`, `PersonOutputAdapterMaria`, etc.) implementa el puerto de salida correspondiente y actúa
como puente entre el dominio y la base de datos relacional. Utiliza un repositorio Spring Data JPA para ejecutar las operaciones de persistencia y un
mapper para traducir entre las entidades del dominio y las entidades JPA.

#figure(
  image("maria-db-output-adapter.png"),
  caption: [Configuración de output-adapter *MariaDB*.]
)

Las *entidades JPA* (`PersonaEntity`, `ProfesionEntity`, etc.) representan el modelo de datos relacional de MariaDB. Están anotadas con `@Entity`,
`@Table` y `@Column` de JPA, y mapean directamente a las tablas definidas en el DDL. A diferencia de las entidades de dominio, estas clases sí dependen
del framework de persistencia y no contienen lógica de negocio.

#figure(
  image("maria-db-entity.png"),
  caption: [Configuración de entidad en output-adapter *MariaDB*.]
)

El *mapper* de MariaDB (`PersonaMapperMaria`, `ProfesionMapperMaria`, etc.) es responsable de traducir entre la entidad JPA y la entidad de dominio en
ambas direcciones: `fromDomainToAdapter` convierte una entidad de dominio en una entidad JPA para persistir, y `fromAdapterToDomain` convierte una entidad
JPA recuperada de la BD en una entidad de dominio para retornar al caso de uso.

#figure(
  image("maria-db-mapper.png"),
  caption: [Configuración de mapper en output-adapter *MariaDB*.]
)

El *repository* de MariaDB extiende `JpaRepository` de Spring Data, lo que proporciona automáticamente los métodos CRUD básicos (`save`, `findById`,
`findAll`, `deleteById`) sin necesidad de escribir implementaciones. Spring genera el bean en tiempo de ejecución y lo inyecta en el output-adapter
mediante `@Autowired`.

#figure(
  image("maria-db-repository.png"),
  caption: [Configuración de repository en output-adapter *MariaDB*.]
)

=== MongoDB

El *output-adapter* de MongoDB (`ProfessionOutputAdapterMongo`, `PersonOutputAdapterMongo`, etc.) implementa el mismo puerto de salida que el adaptador de
MariaDB, pero delegando las operaciones a un repositorio Spring Data MongoDB. Gracias a que ambos implementan la misma interfaz, el caso de uso puede
trabajar con cualquiera de los dos sin modificación alguna.

#figure(
  image("mongo-db-output-adapter.png"),
  caption: [Configuración de output-adapter *MongoDB*]
)

Los *documentos* (`PersonaDocument`, `ProfesionDocument`, etc.) son el equivalente MongoDB de las entidades JPA. Están anotados con `@Document` de Spring
Data MongoDB e incluyen el campo `_class` para que Spring pueda deserializar correctamente los documentos almacenados en las colecciones. Su estructura
flexible permite almacenar campos opcionales sin necesidad de migraciones de esquema.

#figure(
  image("mongo-db-document.png"),
  caption: [Configuración del documento en output-adapter *MongoDB*.]
)

El *mapper* de MongoDB (`PersonaMapperMongo`, `ProfesionMapperMongo`, etc.) cumple la misma función que el mapper de MariaDB: traduce entre el documento
MongoDB y la entidad de dominio en ambas direcciones, aislando al dominio de los detalles de representación de MongoDB.

#figure(
  image("mongo-db-mapper.png"),
  caption: [Configuración de mapper en output-adapter *MongoDB*.]
)

El *repository* de MongoDB extiende `MongoRepository` de Spring Data, que provee los mismos métodos CRUD que `JpaRepository` pero sobre colecciones
MongoDB. Spring Data detecta automáticamente qué repositorios son JPA y cuáles son MongoDB según las anotaciones de sus documentos o entidades asociadas.

#figure(
  image("mongo-db-repository.png"),
  caption: [Configuración de repository en output-adapter *MongoDB*.]
)

== Implementación API REST y CLI

=== CLI

El *modelo CLI* (`PersonaModelCli`, etc.) es una clase de transferencia de datos específica del adaptador de línea de comandos. Representa la información
de una entidad en el formato que el CLI necesita mostrar o recibir del usuario, desacoplando la representación en consola del modelo de dominio.

#figure(
  image("cli-model.png"),
  caption: [Configuración de modelo para input-adapter *CLI*.]
)

La clase principal del adaptador CLI está anotada con `@SpringBootApplication` e implementa `CommandLineRunner`. Al arrancar la aplicación, Spring Boot
invoca automáticamente el método `run()`, que lanza el menú principal interactivo. Esta clase actúa como punto de entrada de la aplicación CLI y delega el
control al `MenuPrincipal`.

#figure(
  image("cli-spring-application.png"),
  caption: [Configuración de clase entrada o principal para input-adapter *CLI*.]
)

El *input-adapter CLI* (`PersonaInputAdapterCli`, etc.) es el puente entre el menú interactivo y los casos de uso del dominio. Inyecta tanto el
output-adapter de MariaDB como el de MongoDB, y expone el método `setPersonOutputPortInjection()` que permite al menú seleccionar el motor de persistencia
en tiempo de ejecución. Sus métodos (`historial`, `crearPersona`, `updatePersona`, `deletePersona`) leen la entrada del usuario, construyen la entidad de
dominio y delegan la operación al caso de uso.

#figure(
  image("cli-input-adapter.png"),
  caption: [Configuración de input-adapter para *CLI*.]
)

El *mapper CLI* (`PersonaMapperCli`, etc.) traduce entre la entidad de dominio y el modelo CLI, adaptando los datos para su presentación en consola o para
construir la entidad de dominio a partir de la entrada del usuario.

#figure(
  image("cli-mapper.png"),
  caption: [Configuración de mapper para input-adapter *CLI*.]
)

El archivo `application.properties` del adaptador CLI configura la conexión a ambas bases de datos simultáneamente: los parámetros `spring.datasource.*`
para MariaDB mediante JDBC, y los parámetros `spring.data.mongodb.*` para MongoDB. Ambas conexiones están activas desde el arranque, lo que permite al
usuario alternar entre ellas sin reiniciar la aplicación.

#figure(
  image("cli-application-properties.png"),
  caption: [Configuración de archivo application.properties para input-adapter *CLI*.]
)

El *Dockerfile* del adaptador CLI utiliza una construcción multi-etapa: en la primera etapa (`builder`) compila el proyecto completo con Maven usando `-pl
cli-input-adapter -am` para incluir solo los módulos necesarios; en la segunda etapa copia únicamente el JAR generado sobre una imagen JRE ligera,
reduciendo el tamaño final de la imagen.

#figure(
  image("cli-dockerfile.png"),
  caption: [Configuración de archivo Dockerfile para input-adapter *CLI*.]
)

El *menú CLI* está organizado jerárquicamente: el `MenuPrincipal` presenta los módulos disponibles (Persona, Profesión, Teléfono, Estudio), cada módulo
tiene su propio menú que primero solicita el motor de persistencia y luego las operaciones CRUD disponibles. Esta estructura permite navegar y operar
sobre cualquier entidad en cualquier base de datos desde una única sesión de consola.

#figure(
  image("cli-menu.png"),
  caption: [Ejemplo de menú para modulo de input-adapter *CLI*.]
)

=== API REST

El módulo `rest-input-adapter` es el adaptador de entrada HTTP de la aplicación. Siguiendo los principios de arquitectura hexagonal, esta capa traduce las
peticiones HTTP en llamadas a los puertos de entrada del dominio, sin contener ninguna lógica de negocio.

*Estructura del módulo*

El módulo sigue el mismo patrón que el `cli-input-adapter` ya existente en el proyecto. Para cada entidad se crean cuatro tipos de artefactos:

#figure(
  table(
    columns: (auto, auto, auto),
    inset: 8pt,
    align: horizon,
    stroke: 0.5pt + gray,
    [*Paquete*], [*Clase*], [*Responsabilidad*],
    [`controller`], [`ProfesionControllerV1`], [Recibe la petición HTTP y delega al adapter],
    [`adapter`], [`ProfesionInputAdapterRest`], [Selecciona la base de datos e invoca el puerto de entrada],
    [`mapper`], [`ProfesionMapperRest`], [Convierte entre la entidad de dominio y los DTOs REST],
    [`model/request`], [`ProfesionRequest`], [DTO de entrada: representa el JSON que envía el cliente],
    [`model/response`], [`ProfesionResponse`], [DTO de salida: representa el JSON que retorna la API],
  ),
  caption: [Estructura de artefactos del módulo REST por entidad],
)

Los DTOs (`request` y `response`) desacoplan la representación HTTP del modelo de dominio. El campo `database` viaja en cada request para que el adapter
sepa qué motor de persistencia utilizar (MariaDB o MongoDB), siguiendo el mismo patrón de selección en tiempo de ejecución del `cli-input-adapter`.

#figure(
  image("rest-dtos-request-responde.png"),
  caption: [DTOs de entrada (*request*) y salida (*response*) para el adaptador REST.]
)

El mapper traduce entre la entidad de dominio y los DTOs REST en ambas direcciones. El método `fromAdapterToDomain` construye la entidad de dominio usando
su constructor, respetando que las entidades no son anémicas y contienen validaciones propias en sus métodos de modificación.

#figure(
  image("rest-mapper.png"),
  caption: [Mapper del adaptador REST: conversión entre dominio y DTOs HTTP.]
)

El adapter es la pieza central del módulo: recibe el campo `database` del request, selecciona el output port correspondiente (MariaDB o MongoDB),
instancia el caso de uso con ese port, y delega la operación. Sigue exactamente el mismo patrón de `setPersonOutputPortInjection` del `cli-input-adapter`.

#figure(
  image("rest-input-adapter.png"),
  caption: [Input adapter REST: selección de persistencia e invocación del caso de uso.]
)

El controller es deliberadamente delgado: únicamente recibe la petición HTTP, la delega al adapter, y retorna la respuesta serializada en JSON. No
contiene lógica de negocio ni accede directamente a repositorios. Está anotado con `@RestController` y define las rutas mediante `@RequestMapping`,
`@GetMapping`, `@PostMapping`, `@PutMapping` y `@DeleteMapping`.

#figure(
  image("rest-controller.png"),
  caption: [Controller REST: punto de entrada HTTP, delega toda la lógica al adapter.]
)

== Tag y Release

\<#text(fill: blue)[llenar tag y release]\>

== Despliegue y construccion del proyecto

=== Docker

Se realizaron dos Dockerfiles en los cuales se especifica como se construyen las imágenes para tanto el adaptador de entrada API REST y el de entrada CLI.
En el Dockerfile de la API REST se expone el *puerto 3000* tal cual como lo dice el README del repositorio del enunciado.

=== Compose

El archivo `compose.yml` define tres servicios: *MariaDB*, *MongoDB* y la *API REST*. Las bases de datos se inicializan automáticamente con los scripts
DDL y DML ubicados en la carpeta `scripts/`. El servicio REST espera a que ambas bases de datos estén saludables antes de arrancar, gracias a la directiva
`depends_on` con `condition: service_healthy`. El adaptador CLI *no se incluye en el compose* porque es una aplicación interactiva que requiere una
terminal con entrada estándar activa, por lo que se ejecuta de forma independiente.

=== Links del despliegue

- *Repositorio:* #text(fill: blue)[#link("https://github.com/ErickSalazar07/personapp-hexa-spring-boot")]
- *API REST (Swagger UI):* #text(fill: blue)[#link("http://localhost:3000/swagger-ui/index.html")]

=== Pasos para correr el proyecto

- El *Dockerfile.rest* y el *Dockerfile.cli* especifican cómo se construyen las imágenes de cada adaptador de entrada utilizando una construcción
  multi-etapa con Maven.
- El archivo *compose.yml* especifica cómo levantar todos los servicios necesarios (bases de datos y API REST) para que el proyecto se ejecute de manera
  correcta.

==== API REST y bases de datos

+ *Clonar el repositorio:* \
  #box(
    stroke: 1pt + gray,
    inset: 10pt,
    radius: 2pt,
    fill: rgb("#f5f5f5"),
    [ ```sh git clone https://github.com/ErickSalazar07/personapp-hexa-spring-boot.git ``` ]
  )

+ *Construcción y ejecución de las bases de datos y la API REST:* \
  #box(
    stroke: 1pt + gray,
    inset: 10pt,
    radius: 2pt,
    fill: rgb("#f5f5f5"),
    [ ```sh docker compose up -d ``` ]
  )

+ *Verificar que todos los servicios estén corriendo:* \
  #box(
    stroke: 1pt + gray,
    inset: 10pt,
    radius: 2pt,
    fill: rgb("#f5f5f5"),
    [ ```sh docker compose ps ``` ]
  )

  Una vez levantados, la API REST estará disponible en #text(fill: blue)[`http://localhost:3000`] y la documentación Swagger en #text(fill:
  blue)[`http://localhost:3000/swagger-ui/index.html`].

==== CLI

El adaptador CLI requiere una terminal interactiva para funcionar, por lo que se construye y ejecuta de forma independiente al compose.

+ *Construir la imagen del CLI:* \
  #box(
    stroke: 1pt + gray,
    inset: 10pt,
    radius: 2pt,
    fill: rgb("#f5f5f5"),
    [ ```sh docker build -f Dockerfile.cli -t personapp-cli . ``` ]
  )

+ *Ejecutar el CLI conectado a la red del compose:* \
  #box(
    stroke: 1pt + gray,
    inset: 10pt,
    radius: 2pt,
    fill: rgb("#f5f5f5"),
    [```sh
      docker run -it \
      --network personapp-hexa-spring-boot_default \
      -e MARIADB_HOST=mariadb \
      -e MARIADB_PORT=3306 \
      -e MARIADB_USER=persona_db \
      -e MARIADB_PASSWORD=persona_db \
      -e MONGO_HOST=mongodb \
      -e MONGO_USER=persona_db \
      -e MONGO_PASSWORD=persona_db \
      personapp-cli
    ```]
  )

  La red `personapp-hexa-spring-boot_default` es creada automáticamente por Docker Compose al hacer `docker compose up`. Esta red permite que el
  contenedor CLI encuentre los servicios de MariaDB y MongoDB por nombre de host (`mariadb` y `mongodb`), que son los mismos nombres definidos en el
  `compose.yml`. La bandera `-it` habilita la terminal interactiva necesaria para el menú.

  Para confirmar el nombre exacto de la red en tu máquina: \
  #box(
    stroke: 1pt + gray,
    inset: 10pt,
    radius: 2pt,
    fill: rgb("#f5f5f5"),
    [ ```sh docker network ls ``` ]
  )

== Producto final

=== API REST

#figure(
  image("swagger-01.png"),
  caption: [Swagger de API REST.]
)

#figure(
  image("swagger-02.png"),
  caption: [Swagger de API REST.]
)

#figure(
  image("swagger-03.png"),
  caption: [Swagger de API REST.]
)

#figure(
  image("get-persona-maria.png"),
  caption: [Get de personas especificando la base de datos *MariaDB*.]
)

#figure(
  image("get-persona-mongo.png"),
  caption: [Get de personas especificando la base de datos *MongoDB*.]
)


=== CLI (Command Line Interface)

#figure(
  image("get-profesion-cli-maria.png"),
  caption: [Opción listar profesiones especificando base de datos *MariaDB*.]
)

#figure(
  image("get-profesion-cli-mongo.png"),
  caption: [Opción listar profesiones especificando base de datos *MongoDB*.]
)

#figure(
  image("create-cli-profesion-maria.png"),
  caption: [Opción crear profesión especificando base de datos *MariaDB*.]
)

#figure(
  image("create-cli-profesion-mongo.png"),
  caption: [Opción crear profesión especificando base de datos *MongoDB*.]
)


// ========================================
// CONCLUSIONES
// ========================================
= Conclusiones y lecciones aprendidas

== Conclusiones

La arquitectura hexagonal demostró ser una solución efectiva para aislar la lógica de negocio de los detalles de infraestructura. A lo largo del
laboratorio se comprobó que el dominio permanece completamente independiente de las tecnologías utilizadas: las entidades `Person`, `Phone`, `Profession`
y `Study` no contienen ninguna anotación de framework, y los casos de uso en el módulo `application` pueden trabajar indistintamente con MariaDB o MongoDB
sin modificar una sola línea de código del núcleo.

La decisión de implementar dos adaptadores de entrada (REST y CLI) que comparten exactamente los mismos casos de uso validó en la práctica el principio
fundamental de la arquitectura: el dominio no depende de quién lo invoca. Ambos adaptadores pueden seleccionar el motor de persistencia en tiempo de
ejecución mediante el método `setPersistencia()`, lo que demuestra la flexibilidad que otorga el diseño basado en puertos e interfaces.

El uso de un proyecto multi-módulo Maven con el `flatten-maven-plugin` permitió organizar cada capa de la arquitectura en un módulo independiente con sus
propias dependencias, reforzando a nivel de compilación las restricciones de dependencia que impone la arquitectura hexagonal: el dominio no puede
importar clases de los adaptadores porque simplemente no los tiene como dependencia en su `pom.xml`.

== Lecciones aprendidas

La compatibilidad entre versiones de herramientas es crítica antes de iniciar el desarrollo. La incompatibilidad entre Lombok `1.18.26` y Java 21 generó
errores de compilación no evidentes que requirieron diagnóstico detallado. Verificar la matriz de compatibilidad de las dependencias principales al inicio
del proyecto evita bloqueos innecesarios durante el desarrollo.

La gestión de propiedades con variables de entorno en Spring Boot (`${VARIABLE:default}`) es una práctica esencial para que la misma aplicación funcione
tanto en desarrollo local como dentro de contenedores Docker. La diferencia entre el puerto externo del host y el puerto interno del contenedor
(`3307:3306`) es un punto de confusión frecuente que tiene un impacto directo en la configuración de conexión a bases de datos.

El patrón de inicialización de bases de datos con scripts montados en `docker-entrypoint-initdb.d` solo se ejecuta cuando el volumen está vacío. Este
comportamiento de Docker implica que cualquier cambio en los scripts de inicialización requiere eliminar el volumen con `docker compose down -v` antes de
volver a levantar los contenedores, de lo contrario los cambios no tienen efecto.

Diseñar entidades de dominio no anémicas desde el inicio del proyecto es más costoso en tiempo, pero produce un modelo más expresivo y seguro. Incorporar
validaciones directamente en los métodos de modificación de las entidades (`updateAge`, `updateFirstName`, etc.) permite detectar datos inválidos en el
momento exacto en que se intenta modificar el estado, independientemente del adaptador que realice la operación.

// ========================================
// REFERENCIAS
// ========================================
= Referencias

- Cockburn, A. (2005). _Hexagonal Architecture_. Recuperado de #link("https://alistair.cockburn.us/hexagonal-architecture/")
- Spring Boot Reference Documentation (v2.7.11). (2023). _Spring Framework_. Recuperado de
  #link("https://docs.spring.io/spring-boot/docs/2.7.11/reference/html/")
- Spring Data JPA Reference Documentation. (2023). _Spring Data JPA_. Recuperado de
  #link("https://docs.spring.io/spring-data/jpa/docs/current/reference/html/")
- Spring Data MongoDB Reference Documentation. (2023). _Spring Data MongoDB_. Recuperado de
  #link("https://docs.spring.io/spring-data/mongodb/docs/current/reference/html/")
- Martin, R. C. (2017). _Clean Architecture: A Craftsman's Guide to Software Structure and Design_. Prentice Hall.
- Gamma, E., Helm, R., Johnson, R., & Vlissides, J. (1994). _Design Patterns: Elements of Reusable Object-Oriented Software_. Addison-Wesley.
- Docker Documentation. (2024). _Compose file reference_. Recuperado de #link("https://docs.docker.com/compose/compose-file/")
- MariaDB Documentation. (2024). _MariaDB Server Documentation_. Recuperado de #link("https://mariadb.com/kb/en/documentation/")
- MongoDB Documentation. (2024). _MongoDB Manual_. Recuperado de #link("https://www.mongodb.com/docs/manual/")
