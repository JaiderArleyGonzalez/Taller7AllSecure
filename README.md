# APLICACIÓN DISTRIBUIDA SEGURA EN TODOS SUS FRENTES
El proyecto consiste en un sistema de inicio de sesión y una aplicación para buscar recetas de comida.
## Funcionamiento

https://github.com/JaiderArleyGonzalez/Taller7AllSecure/assets/89174283/a01748e2-bb83-4b0a-a3d2-f97e820fbacf

## Prerrequisitos
1. Java Development Kit (JDK) 17: La aplicación está configurada para compilar y ejecutarse en Java 17. Asegúrate de tener JDK 17 instalado en tu sistema.
2. Maven: La gestión de dependencias y la compilación del proyecto se realizan utilizando Maven. Asegúrate de tener Maven instalado en tu sistema y configurado correctamente en tu variable de entorno PATH.
3. Git: Para trabajar con el control de versiones, necesitarás tener Git instalado en tu sistema.

## Instalación
Creamos dos instancias de EC2 en AWS, bajo los nombres "login" y "meal".
En cada una de las máquinas realizamos las siguientes instalaciones.
### Git
Para la instalación de Git (con el objetivo de clonar el repositorio para correr cada servicio respectivo) ejecutamos el siguiente comando:

```
    sudo yum install -y git
```

Git es un sistema de control de versiones distribuido que permite gestionar cambios en el código fuente de manera eficiente y colaborativa. Permite rastrear, revertir y fusionar cambios realizados por diferentes colaboradores en un proyecto de software, lo que facilita el trabajo en equipo y la administración del código a lo largo del tiempo.

### Java
Para la instalación de Java, usaremos la versión 17 que es la que se estableció en el POM

```
    sudo yum install -y java-17-amazon-corretto-devel
```

Java es un lenguaje de programación de propósito general y orientado a objetos, desarrollado por Sun Microsystems (adquirida por Oracle Corporation). Es conocido por su portabilidad, lo que significa que los programas escritos en Java pueden ejecutarse en diferentes plataformas sin necesidad de recompilación. Java es ampliamente utilizado en el desarrollo de aplicaciones empresariales, aplicaciones móviles (Android), aplicaciones web, sistemas embebidos, y más. Ofrece características como la gestión automática de memoria a través del recolector de basura, seguridad robusta, y una gran cantidad de bibliotecas estándar y frameworks que facilitan el desarrollo de software.
### Apache-Maven
```
    sudo wget https://repos.fedorapeople.org/repos/dchen/apache-maven/epel-apache-maven.repo -O /etc/yum.repos.d/epel-apache-maven.repo
```

```
    sudo sed -i s/\$releasever/6/g /etc/yum.repos.d/epel-apache-maven.repo
```

```
    sudo yum install -y apache-maven
```
Estas líneas de comandos son instrucciones para instalar Apache Maven en un sistema CentOS o Fedora utilizando el gestor de paquetes yum:

1. Descarga del archivo de repositorio de Maven: Utiliza el comando wget para descargar el archivo de repositorio de Apache Maven desde el repositorio de Fedora People y lo guarda en el directorio /etc/yum.repos.d/ con el nombre epel-apache-maven.repo.

2. Modificación del archivo de repositorio: Utiliza el comando sed para modificar el archivo de repositorio descargado, reemplazando la variable $releasever por 6. Esto se hace para especificar la versión de CentOS o Fedora que estás utilizando, ya que la variable $releasever se expandirá a la versión del sistema operativo.

3. Instalación de Apache Maven: Utiliza el comando yum para instalar Apache Maven junto con todas sus dependencias, utilizando la opción -y para confirmar automáticamente todas las preguntas de instalación con "sí".

Maven es una herramienta de gestión de proyectos y construcción de software ampliamente utilizada en el desarrollo de aplicaciones Java. Permite automatizar el proceso de compilación, gestión de dependencias, empaquetado y distribución de software. Maven utiliza un archivo de configuración llamado POM (Project Object Model) para describir cómo se construye el proyecto, incluyendo sus dependencias, plugins, y configuraciones. Facilita la gestión de proyectos complejos al proporcionar una estructura uniforme y coherente, además de integrarse fácilmente con repositorios de artefactos como Maven Central.
## Componentes
### Servicio de Inicio de Sesión (Login Service):
* Este servicio proporciona la funcionalidad de inicio de sesión para usuarios.
* Utiliza el protocolo HTTPS para garantizar la seguridad de las comunicaciones.
* Los usuarios pueden autenticarse proporcionando su nombre de usuario y contraseña.
* Las contraseñas se almacenan en forma de hash SHA-256 para mayor seguridad.
* Una vez autenticados, los usuarios reciben un token de sesión que les permite acceder a las funcionalidades protegidas del sistema.
* Se implementa un filtro de autenticación para proteger las rutas que requieren inicio de sesión.
### Servicio de Recetas (Meal Service):

* Este servicio permite a los usuarios buscar recetas de comida basadas en un ingrediente específico.
* Utiliza una API externa para obtener información sobre las recetas disponibles.
* Los usuarios pueden realizar consultas proporcionando el nombre del ingrediente deseado.
* La aplicación muestra las recetas encontradas de forma visual, incluyendo imágenes y nombres de platos.
* También se utiliza HTTPS para garantizar la seguridad de las conexiones.

### Interfaz de Usuario (UI):
* Se proporcionan interfaces de usuario (HTML, CSS, JavaScript) tanto para el servicio de inicio de sesión como para el servicio de recetas.
* La interfaz de inicio de sesión permite a los usuarios autenticarse ingresando su nombre de usuario y contraseña.
* La interfaz de búsqueda de recetas permite a los usuarios ingresar un ingrediente y ver las recetas correspondientes.
* Ambas interfaces están diseñadas para ser intuitivas y fáciles de usar.

## Arquitectura

![](/media/arquitectura.png)

### Cliente:
* El cliente está compuesto por interfaces de usuario desarrolladas con HTML, CSS y JavaScript.
* Para el servicio de inicio de sesión, el cliente presenta un formulario donde los usuarios pueden ingresar su nombre de usuario y contraseña.
* Para el servicio de búsqueda de recetas, el cliente proporciona un campo de entrada donde los usuarios pueden ingresar el nombre del ingrediente deseado.
* La interfaz de usuario se comunica con el servidor utilizando solicitudes HTTP, específicamente GET requests, para enviar datos al servidor y recibir respuestas.

### Servidor de Inicio de Sesión:
* Este servidor maneja las solicitudes de autenticación de usuarios.
* Verifica las credenciales proporcionadas por el cliente, realiza la autenticación y genera un token de sesión válido para el usuario autenticado.
* Utiliza HTTPS para asegurar las comunicaciones y proteger la privacidad y seguridad de los datos transmitidos entre el cliente y el servidor.

### Servidor MealService:
* Este servidor corre en el puerto 38000 y se encarga de proporcionar el servicio de búsqueda de recetas.
* Se conecta a una API externa que ofrece información detallada sobre recetas de comida basadas en el ingrediente proporcionado por el usuario.
* Utiliza HTTPS para garantizar la seguridad de las comunicaciones y la integridad de los datos recibidos de la API externa.

### Seguridad:
* Se implementa seguridad tanto en el servidor de inicio de sesión como en el servidor MealService.
* Las contraseñas se almacenan en forma de hash SHA-256 en el servidor de inicio de sesión para protegerlas de posibles ataques de seguridad.
* El uso de HTTPS en ambos servidores garantiza que todas las comunicaciones entre el cliente y los servidores estén cifradas, reduciendo así el riesgo de interceptación de datos y ataques de tipo "man-in-the-middle".
## Testing
### LoginServiceTest
Este conjunto de pruebas evalúa el comportamiento de la clase LoginService, que es responsable de manejar el inicio de sesión y la validación de sesiones de usuario en una aplicación.

Método setUp():
* Propósito: Prepara el entorno de prueba antes de ejecutar cada caso de prueba.
* Acciones realizadas:
  * Crea una instancia de LoginService.
  * Inicializa los usuarios utilizando el método initializeUsers(). 

Pruebas de inicio de sesión (testLoginWithValidCredentials, testLoginWithInvalidUsername, testLoginWithInvalidPassword)
* Propósito: Verificar la funcionalidad del método login() para iniciar sesión con credenciales válidas e inválidas.
* Acciones realizadas:
  * Se intenta iniciar sesión con diferentes combinaciones de nombres de usuario y contraseñas.
* Afirmaciones:
  * testLoginWithValidCredentials: Verifica que se pueda iniciar sesión con credenciales válidas.
  * testLoginWithInvalidUsername, testLoginWithInvalidPassword: Verifica que no se pueda iniciar sesión con un nombre de usuario o contraseña inválidos.

Pruebas de validez de sesión (testSesionValidaWithValidToken, testSesionValidaWithInvalidToken)
* Propósito: Evaluar la funcionalidad del método sesionValida() para validar tokens de sesión.
* Acciones realizadas:
  * Se intenta validar sesiones con tokens válidos e inválidos.
* Afirmaciones:
  * testSesionValidaWithValidToken: Verifica que un token de sesión válido sea considerado como válido.
  * testSesionValidaWithInvalidToken: Verifica que un token de sesión inválido sea considerado como inválido.

Estos tests aseguran que la funcionalidad proporcionada por la clase LoginService funcione según lo esperado, ayudando a mantener la integridad y confiabilidad del sistema de inicio de sesión en la aplicación.

Para probarlos se ejecuta el siguiente comando:

```
    mvn test
```

Se obtendrá lo siguiente:

![](/media/test.png)

## Escalabilidad de la Arquitectura de Seguridad

Para incorporar nuevos servicios a la arquitectura de seguridad, se pueden seguir varias estrategias.

### 1. Separación de Responsabilidades

Identificar las responsabilidades de seguridad específicas de cada servicio y asegúrese de que cada uno tenga su propio conjunto de medidas de seguridad. Esto implica implementar autenticación y autorización independientes para cada servicio, así como gestionar tokens de sesión y políticas de acceso de manera individual.

### 2. Centralización de la Autenticación

Implementar un servicio centralizado de autenticación, como un servidor de autorización OAuth 2.0 o un proveedor de identidad basado en tokens, para manejar la autenticación de los usuarios en todos los servicios. Esto simplificaría la implementación de la autenticación y proporcionaría una capa de seguridad coherente en toda la aplicación.

### 3. Implementación de Políticas de Seguridad Basadas en Roles

Definir roles y permisos específicos para cada servicio y utilizar políticas de seguridad basadas en roles para controlar el acceso a los recursos protegidos. Esto permitirá una gestión más granular de los accesos y una adaptación flexible a los requisitos de seguridad de cada servicio.

### 4. Monitoreo y Auditoría Centralizados

Implementar un sistema centralizado de monitoreo y auditoría de seguridad para supervisar y registrar actividades sospechosas en todos los servicios. Esto ayudará a identificar y responder rápidamente a posibles brechas de seguridad o violaciones.


## Consideraciones
* La funcionalidad de la aplicación depende de que las instancias de AWS se encuentren corriendo, se usan las DNS de IPv4 públicas asignadas.

## Agradecimientos
Se agradece especialmente a [themealdb](https://www.themealdb.com) por brindar la API que consume el mealService, una base de datos abierta y de fuentes múltiples de recetas de todo el mundo.
