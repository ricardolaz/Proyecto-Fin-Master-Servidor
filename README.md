## Proyecto fin de Máster (servidor)
Se trata de mi proyecto fin de máster de **Desarrolo web con Java y XML - Oficial de Oracle.mjo** de la Escuela CICE entregado en junio de 2021. 
Es una aplicación que respeta el patrón MVC. El proyecto está dividido en dos partes: la aplicación del cliente y la del servidor. Se trata de una aplicación donde los usuarios pueden registrarse, publicar opiniones de lugares, ver opiniones de otros usuarios o seguir a otros usuarios.

La aplicación permite a los usuarios:
* Registrarse
* Logarse
* Publicar opinión
* Listar todos los usuarios
* Listar a todos los usuarios seguidos
* Listar todas las opiniones
* Listar todos los lugares
* Visualizar tu propia cuenta (con tus opiniones)
* Seguir o dejar de seguir a un usuario
* Cerrar sesión

Este repositorio es la aplicación servidor del proyecto. El servidor contiene estructuras de datos para almacenar los datos necesarios para servir las operaciones de los clientes. 
La persistencia de datos se ha realizado a través de MySQL. Para crear la base de datos, el usuario y las tablas primero hay que ejecutar la clase CrearBBDD.java.
Una vez que esté creada la base de datos se ejecuta el servidor desde la clase ComunicadorServidor.java. Se trata de un servidor concurrente que puede atender a varios clientes a la vez. Aunque no se ejecute una aplicación cliente, al servidor se le incluye un pequeño menú con varias opciones para solicitar información y sacarla por consola.