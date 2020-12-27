# Documentación técnica
### Barra de menú
Para añadir la barra de menú a todas las páginas he creado un _Fragment_ nuevo en el fichero
 `fragments.html`, el cual incluyo en dichas páginas con:
`<div th:replace="fragments :: barra-menu"/>`.

La barra de menú es una _navbar responsive_ de Bootstrap. 

Para poder acceder al nombre del usuario en la vista sin pasarle parámetros directamente 
(para que aparezca en el desplegable), he añadido el nombre de usuario como atributo a la 
sesión HTTP en el método `logearUsuario` de la clase `ManagerUserSession`, el cual se invoca al 
hacer login en el sitio web.

En caso de que el usuario no esté logeado, en lugar del nombre, aparece la opción de ir al
formulario de login. Esto lo he hecho con los condicionales de _Thymeleaf_ `th:if`.

### Lista de usuarios
Para ver la lista de usuarios he creado una plantilla y la clase `UsuarioController`, que tendrá 
los controladores para responder a aquellas peticiones que tengan que ver con los usuarios. 
He añadido también un  nuevo método de servicio (`allUsuarios`) que se llama desde el controlador
y que devuelve una lista con todos los usuarios. Esta lista de usuarios se añade al Model.
También he implemetnado un test que comprueba el funcionamiento del método de servicio.

### Página descripción de usuario
Para ver los detalles de usuario he creado una nueva plantilla y un método de controlador que 
responde a la petición GET. En este método se añade el usuario solicitado al Model para poder
acceder a sus datos en la plantilla.

### Usuario administrador
En primer lugar he añadido el atributo booleano `admin` a la clase `Usuario`, que indicará si el
usuario es administrador. Esta opción se puede seleccionar con una _checkbox_ en el formulario
de registro.

Para permitir que sólo se registre un usuario como administrador, he implementado en la clase
`UsuarioService` el método `adminExists` (junto con su test correspondiente). Este método se
invocará al hacer el GET del formulario de registro.

### Protección del listado y descripción de usuarios
Para evitar que un usuario no administrador vea el listado de usuarios he creado en la clase
`ManagerUserSesion` el método `comprobarUsuarioAdmin`, que comprueba si el usuario logeado
es administrador, en caso de que no lo sea, se lanza excepción indicando que el usuario no
está autorizado.

Del mismo modo, para evitar que un usuario no pueda ver la descripción de un usuario que no sea él
mismo, he añadido el método `comprobarUsuarioLogeadoOAdmin` en la clase `ManagerUserSesion`, el
cual comprueba que el usuario logeado sea el mismo que el usuario del cual se quieren ver los
detalles o que el usuario logeado sea administrador. Si no ocurre una de las dos cosas, se
devolverá el error 401 (no autorizado).

Al usuario aministrador le aparecerá un enlace a la lista de usuarios en la barra de menú, además
de acceder directamente a esta página al hacer login. 

### Bloqueo de usuarios
He añadido a la clase `Usuario` el atributo booleano bloqueado el cual es `false` cuando se 
registra un nuevo usuario. Para cambiar este atributo he añadido el método de servicio
`bloqueoUsuario` en la clase `UsuarioService` (además de su test).

El bloqueo y desbloqueo sólo lo puede llevar a cabo un administrador desde  el botón correspondiente
de la lista de usuarios, el cual hace una petición GET a la URL `/usuarios/:id/bloquear`. Desde
el método controlador `modificarUsuario`, se responde a esta petición comprobando, en primer lugar,
que el usuario que la ha realizado es administrador y, después, llamando al método de servicio que
cambia el estado bloqueado/desbloqueado.