# Contexto del proyecto
Este repositorio contiene "appweb", una aplicación web desarrollada con Spring Boot cuyo dominio es la venta de equipos y accesorios de dispositivos Apple. La aplicación provee interfaz web (Thymeleaf) para roles de administrador, empleado y cliente, administración de catálogos, inventario, pedidos y módulos de pago. Está preparada para ejecutarse localmente con Maven y en contenedores Docker (imagen Java + PostgreSQL).

# Informe técnico completo - Proyecto appweb

## Resumen ejecutivo
- Nombre del proyecto: appweb
- Grupo / package base: `com.istore.appweb`
- Propósito: Plataforma web e-commerce de ventas y administración de productos y pedidos (marca Apple).

---

## 1. Visión general del repositorio (archivos raíz relevantes)
- pom.xml — configuración Maven y dependencias.
- Dockerfile — imagen runtime para ejecutar el JAR con Java 21 (limita memoria).
- docker-compose.yml — orquesta servicios: PostgreSQL (db) y la app (app).
- .gitignore — reglas de exclusión, incluye ignore de application-dev.properties.
- README.md — este archivo (reemplazado por el informe).
- src/main — código fuente Java y recursos (detallado en la sección 5).

---

## 2. Tecnologías y versiones clave
- Java 21 (propiedad `<java.version>21` en pom.xml).
- Spring Boot 3.5.6 (parent en pom.xml).
- PostgreSQL 17 (imagen `postgres:17-alpine` en docker-compose.yml).
- JRE base para Docker: `eclipse-temurin:21-jre-alpine`.
- Plantillas: Thymeleaf + thymeleaf-extras-springsecurity6.
- Construcción: Maven (mvn), proyecto con Maven Wrapper (`mvnw`, `mvnw.cmd` esperado).
- Sistema operativo objetivo para desarrollo: Windows (ruta del proyecto en Windows).

---

## 3. Dependencias principales (extraídas de pom.xml)
- org.springframework.boot:spring-boot-starter-data-jpa — JPA / Hibernate
- org.springframework.boot:spring-boot-starter-security — seguridad (Spring Security)
- org.springframework.boot:spring-boot-starter-thymeleaf — vistas server-side
- thymeleaf-extras-springsecurity6 — integración Thymeleaf & Spring Security 6
- org.springframework.boot:spring-boot-starter-validation — validación (Jakarta)
- org.springframework.boot:spring-boot-starter-web — MVC / REST
- org.postgresql:postgresql — driver JDBC (scope runtime)
- org.projectlombok:lombok — reducción de boilerplate (optional)
- org.springframework.boot:spring-boot-devtools — herramientas de desarrollo (optional, runtime)
- spring-boot-configuration-processor — procesador de propiedades
- Dependencias de pruebas: spring-boot-starter-test, spring-security-test

---

## 4. Compilación y ejecución (local y con Docker)
- Compilar con Maven:
  - Windows: mvnw.cmd clean package
  - Linux/Mac: ./mvnw clean package
  - Alternativa: mvn clean package
- Ejecutar JAR:
  - java -jar target/*.jar
- Docker:
  - docker compose up --build
  - docker-compose.yml crea:
    - db: postgres:17-alpine, volumen persistente `postgres_data`, puerto host 5432
    - app: build desde Dockerfile, contenedor `istore-app`
  - Dockerfile:
    - Base: eclipse-temurin:21-jre-alpine
    - Añade usuario no-root `spring`
    - Copia `target/*.jar` a `/app.jar`
    - ENTRYPOINT: java -Xms256m -Xmx400m -jar /app.jar (límite de memoria)

---

## 5. Estructura detallada de src/main (organización completa)

Raíz: src/main
- java/
  - com/
    - istore/
      - appweb/
        - AppwebApplication.java
        - configs/
          - Utilidades.java
          - security/              (carpeta: configuración de seguridad)
            - ... (clases de configuración de seguridad, filtros, etc.)
        - controllers/            (carpeta con controladores web)
          - ... (controladores para rutas públicas y admin/empleado/cliente)
        - DTO/                    (carpeta para objetos de transferencia, subcarpetas)
          - categorias/
          - ... (otros DTOs)
        - entities/               (entidades JPA)
          - ... (clases @Entity — productos, usuarios, pedidos, roles, etc.)
        - repositories/           (interfaces Spring Data JPA)
          - ... (Repositorios por entidad)
        - services/               (servicios de negocio)
          - ... (implementaciones y contratos)
- resources/
  - application.properties
  - application-dev.properties   (fichero ignorado por .gitignore; usado para dev local)
  - messages.properties
  - static/
    - css/
      - error.css
      - styles.css
    - img/
      - accesorios/
      - applewatches/
      - audifonos/
      - catalogo/
      - iphones/
    - js/
      - administrador/
        - detalles-pedido.js
        - index-admin.js
        - modalEditarRegistro.js
        - modalEliminarRegistro.js
        - usuarios.js
      - empleado/
        - ... (scripts específicos de empleado)
  - templates/
    - index.html
    - login.html
    - nosotros.html
    - register.html
    - tiendas.html
    - administrador/
      - catalogo.html
      - devoluciones.html
      - errores.html
      - index-administrador.html
      - inventario.html
      - pedidos.html
      - tablasBD.html
      - usuarios.html
      - catalogo/
        - accesorios.html
        - applewatches.html
        - audifonos.html
        - iphones.html
      - inventario/
        - accesorios.html
        - applewatches.html
        - audifonos.html
        - iphones.html
      - usuarios/
        - administradores.html
        - clientes.html
        - empleados.html
    - catalogo/
      - accesorios.html
      - audifonos.html
      - iphones.html
      - relojes.html
    - clientes/
      - account.html
      - pago.html
      - pedidos.html
    - empleado/
      - catalogo.html
      - devoluciones.html
      - index-empleado.html
      - inventario.html
      - pedidos.html
      - catalogo/
        - accesorios.html
        - applewatches.html
        - audifonos.html
        - iphones.html
      - inventario/
        - accesorios.html
        - applewatches.html
        - audifonos.html
        - iphones.html
    - error/
      - 400.html
      - 403.html
      - 404.html
      - 405.html
      - 500.html
      - error.html
    - fragments/
      - footers.html
      - heads.html
      - modals.html
      - navbars.html
      - adminBD/
        - modalAgregarRegistro.html
        - modalEditarRegistro.html
        - modalEliminarRegistro.html
    - tablasBD/
      - categorias.html
      - colores.html
      - detalles-pedido.html
      - estadosCompras.html
      - estadosPagos.html
      - metodosPagos.html
      - pedidos.html
      - productos.html
      - roles.html
      - tiposComprobantes.html
      - usuarios.html

Observaciones:
- La carpeta `templates` está organizada por roles (administrador, empleado, clientes) y por funcionalidades (catálogo, inventario, tablasBD, fragments).
- Los recursos estáticos (css, js, img) están estructurados para separar lógica de administración y de usuario final.

---

## 6. Archivos de configuración sensibles y .gitignore
- `.gitignore` contiene:
  - target/
  - configuraciones de IDE (.idea, .vscode)
  - application-dev.properties (para no versionar credenciales locales)

---

## 7. Docker y orquestación (detalles)
- Dockerfile (ubicación: raíz del proyecto)
  - Imagen base: eclipse-temurin:21-jre-alpine
  - Usuario no-root: spring
  - Copia JAR: ARG JAR_FILE=target/*.jar -> COPY ${JAR_FILE} app.jar
  - ENTRYPOINT: ["java","-Xms256m","-Xmx400m","-jar","/app.jar"]
  - Comentario: límites de memoria pensados para entornos con poca RAM (ej. e2-micro).
- docker-compose.yml
  - Servicio db:
    - image: postgres:17-alpine
    - container_name: istore-db
    - environment: POSTGRES_USER/postgres, POSTGRES_PASSWORD/admin, POSTGRES_DB/db-istore, TZ, LANG
    - volumes: postgres_data:/var/lib/postgresql/data
    - ports: "5432:5432"
    - restart: always
  - Servicio app:
    - build: .
    - container_name: istore-app
    - ports: "80:3000" (revisar; posiblemente erróneo)
    - environment:
      - SPRING_DATASOURCE_URL: jdbc:postgresql://db:5432/db-istore?currentSchema=public&useUnicode=true&characterEncoding=UTF-8&serverTimezone=America/Lima
      - SPRING_DATASOURCE_USERNAME: postgres
      - SPRING_DATASOURCE_PASSWORD: admin
      - SPRING_JPA_HIBERNATE_DDL_AUTO: update
      - TZ: America/Lima
    - depends_on: - db
    - restart: always
  - volumes:
    - postgres_data

## 8. Puntos operativos y notas técnicas
- Inicio de la app: clase `com.istore.appweb.AppwebApplication`.
- Configuración / utilidades: `com.istore.appweb.configs.Utilidades`.
- Rutas y lógica: revisar controladores en `controllers`.
- Modelado de datos: entidades JPA en `entities` y repositorios en `repositories`.
- Vistas y navegación: `templates` (Thymeleaf) y fragments para cabeceras, pies y modales.
- Scripts front JS: en `static/js/administrador` y `static/js/empleado`.
- I18n: `messages.properties` para mensajes.

---

## 9. Referencias rápidas (archivos clave)
- pom.xml
- Dockerfile
- docker-compose.yml
- .gitignore
- src/main/java/com/istore/appweb/AppwebApplication.java
- src/main/java/com/istore/appweb/configs/Utilidades.java
- src/main/resources/application.properties
- src/main/resources/application-dev.properties (local)
- src/main/resources/messages.properties
- src/main/resources/templates/ (lista completa en la sección 5)
- src/main/resources/static/ (css, js, img)

---


# Estructura del Proyecto AppWeb

## 📁 Descripción General
Proyecto de aplicación web de tienda online (iStore) desarrollado con Spring Boot, incluyendo funcionalidades de catálogo, carrito de compras, gestión de usuarios y administración.

---

## 📂 Árbol de Directorios

```
appweb/
│
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/istore/appweb/
│   │   │       ├── AppwebApplication.java
│   │   │       ├── configs/
│   │   │       │   ├── Utilidades.java
│   │   │       │   └── security/
│   │   │       ├── controllers/
│   │   │       │   ├── CarritosController.java
│   │   │       │   ├── CatalogoController.java
│   │   │       │   ├── GlobalController.java
│   │   │       │   ├── HomeController.java
│   │   │       │   ├── Login.java
│   │   │       │   ├── administrador/
│   │   │       │   └── clientes/
│   │   │       ├── DTO/
│   │   │       │   ├── categorias/
│   │   │       │   ├── colores/
│   │   │       │   ├── estadosCompras/
│   │   │       │   ├── estadosPagos/
│   │   │       │   └── ...
│   │   │       ├── entities/
│   │   │       │   └── ...
│   │   │       └── repositories/
│   │   │           └── services/
│   │   │
│   │   └── resources/
│   │       ├── application-dev.properties
│   │       ├── application.properties
│   │       ├── messages.properties
│   │       ├── static/
│   │       │   ├── css/
│   │       │   │   ├── admin-layout.css
│   │       │   │   ├── error.css
│   │       │   │   └── styles.css
│   │       │   ├── img/
│   │       │   │   ├── accesorios/
│   │       │   │   ├── applewatches/
│   │       │   │   ├── audifonos/
│   │       │   │   ├── catalogo/
│   │       │   │   └── iphones/
│   │       │   └── js/
│   │       │       ├── administrador/
│   │       │       │   ├── detalles-pedido.js
│   │       │       │   ├── index-admin.js
│   │       │       │   ├── modalEditarRegistro.js
│   │       │       │   ├── modalEliminarRegistro.js
│   │       │       │   └── usuarios.js
│   │       │       └── empleado/
│   │       │
│   │       └── templates/
│   │           ├── index.html
│   │           ├── login.html
│   │           ├── nosotros.html
│   │           ├── register.html
│   │           ├── tiendas.html
│   │           ├── administrador/
│   │           │   ├── catalogo.html
│   │           │   ├── devoluciones.html
│   │           │   ├── errores.html
│   │           │   ├── index-administrador.html
│   │           │   ├── inventario.html
│   │           │   ├── pedidos.html
│   │           │   ├── tablasBD.html
│   │           │   ├── usuarios.html
│   │           │   └── tablasBD/
│   │           │       ├── categorias.html
│   │           │       ├── colores.html
│   │           │       ├── detalles-pedido.html
│   │           │       ├── estadosCompras.html
│   │           │       ├── estadosPagos.html
│   │           │       ├── metodosPagos.html
│   │           │       ├── pedidos.html
│   │           │       ├── productos.html
│   │           │       ├── roles.html
│   │           │       ├── tiposComprobantes.html
│   │           │       └── usuarios.html
│   │           ├── catalogo/
│   │           │   ├── accesorios.html
│   │           │   ├── audifonos.html
│   │           │   ├── iphones.html
│   │           │   └── relojes.html
│   │           ├── clientes/
│   │           │   ├── account.html
│   │           │   ├── pago.html
│   │           │   └── pedidos.html
│   │           ├── empleado/
│   │           │   ├── catalogo.html
│   │           │   ├── devoluciones.html
│   │           │   ├── index-empleado.html
│   │           │   ├── inventario.html
│   │           │   └── pedidos.html
│   │           ├── error/
│   │           │   ├── 400.html
│   │           │   ├── 403.html
│   │           │   ├── 404.html
│   │           │   ├── 405.html
│   │           │   ├── 500.html
│   │           │   └── error.html
│   │           └── fragments/
│   │               ├── footers.html
│   │               ├── heads.html
│   │               ├── modals.html
│   │               ├── navbars.html
│   │               └── adminBD/
│   │                   ├── modalAgregarRegistro.html
│   │                   ├── modalEditarRegistro.html
│   │                   └── modalEliminarRegistro.html
│   │
│   └── test/
│       └── java/
│           └── com/istore/appweb/
│               └── AppwebApplicationTests.java
│
└── estructura.md (este archivo)
```

---

## 📋 Descripción de Componentes

### 🎯 Controllers
- **CarritosController.java**: Gestión del carrito de compras
- **CatalogoController.java**: Visualización y filtrado de productos
- **GlobalController.java**: Controlador global para propiedades compartidas
- **HomeController.java**: Página de inicio
- **Login.java**: Autenticación de usuarios
- **administrador/**: Controladores para funciones administrativas
- **clientes/**: Controladores específicos para clientes

### 🗄️ DTO (Data Transfer Objects)
Carpetas para transferencia de datos:
- categorias/
- colores/
- estadosCompras/
- estadosPagos/
- Y más...

### 🔧 Configuraciones
- **Utilidades.java**: Funciones utilitarias
- **security/**: Configuraciones de seguridad

### 🎨 Recursos Estáticos

#### CSS
- **admin-layout.css**: Estilos para panel administrativo
- **error.css**: Estilos para páginas de error
- **styles.css**: Estilos generales

#### Imágenes
- accesorios/
- applewatches/
- audifonos/
- catalogo/
- iphones/

#### JavaScript
- **administrador/**: Scripts para panel admin
  - detalles-pedido.js
  - index-admin.js
  - modalEditarRegistro.js
  - modalEliminarRegistro.js
  - usuarios.js
- **empleado/**: Scripts para panel de empleados

### 📄 Templates HTML

#### Públicas
- index.html
- login.html
- nosotros.html
- register.html
- tiendas.html

#### Administrador
- Panel de control (index-administrador.html)
- Catálogo, Inventario, Pedidos, Devoluciones
- Tablas de base de datos

#### Catálogo
- accesorios.html
- audifonos.html
- iphones.html
- relojes.html

#### Clientes
- account.html
- pago.html
- pedidos.html

#### Empleados
- Panel de empleado (index-empleado.html)
- Catálogo, Inventario, Pedidos, Devoluciones

#### Errores
- 400.html, 403.html, 404.html, 405.html, 500.html
- error.html (genérica)

#### Fragmentos Reutilizables
- footers.html
- heads.html
- modals.html
- navbars.html
- adminBD/: Modales para gestión de BD

### ⚙️ Configuración
- **application.properties**: Configuración general
- **application-dev.properties**: Configuración para desarrollo
- **messages.properties**: Mensajes internacionalizados

### 🧪 Tests
- **AppwebApplicationTests.java**: Pruebas unitarias de la aplicación

---

## 🏗️ Arquitectura

La aplicación sigue un patrón **MVC (Model-View-Controller)** con:
- **Controllers**: Manejo de solicitudes HTTP
- **DTOs**: Transferencia de datos entre capas
- **Entities**: Modelos de datos
- **Repositories**: Acceso a datos
- **Services**: Lógica de negocio
- **Views (Templates)**: Plantillas Thymeleaf HTML

---

## 👥 Roles del Sistema

1. **Administrador**: Gestión completa del sistema
2. **Empleado**: Gestión de inventario y pedidos
3. **Cliente**: Navegación de catálogo y compras

---

## 📅 Última Actualización
8 de diciembre de 2025
