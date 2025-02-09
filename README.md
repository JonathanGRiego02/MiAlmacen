# Aplicación de Gestión de Almacenes

Esta es una aplicación de gestión de almacenes desarrollada en Kotlin para Android. La aplicación permite a los usuarios gestionar almacenes, agregar y eliminar objetos, y ver detalles de los almacenes.

## Características

- Autenticación de usuarios
- Agregar, editar y eliminar almacenes
- Agregar, editar y eliminar objetos dentro de los almacenes
- Ver detalles de los almacenes
- Funcionalidad de cierre de sesión
- Diálogo de información sobre la aplicación

## Tecnologías Utilizadas

- Kotlin
- Android SDK
- SQLite
- Android Jetpack (Navigation, ViewModel, LiveData)
- Componentes de Diseño Material

## Estructura del Proyecto

- `actividades`: Contiene las actividades principales de la aplicación.
- `adaptadores`: Contiene los adaptadores para los RecyclerViews.
- `controladores`: Contiene la clase del gestor de la base de datos.
- `modelos`: Contiene los modelos de datos.
- `ui`: Contiene los fragmentos de la interfaz de usuario.
- `res/layout`: Contiene los archivos de diseño XML.
- `res/values`: Contiene los valores de recursos como cadenas y dimensiones.

## Instalación

1. Clona el repositorio:
    ```sh
    git clone https://github.com/JonathanGRiego02/WarehouseManagementApp.git
    ```
2. Abre el proyecto en Android Studio.
3. Construye el proyecto para descargar las dependencias necesarias.
4. Ejecuta la aplicación en un emulador o dispositivo físico.

## Uso

1. **Iniciar Sesión**: Usa las credenciales proporcionadas para iniciar sesión.
2. **Ver Almacenes**: La pantalla principal muestra una lista de almacenes.
3. **Agregar Almacén**: Usa la opción del menú para agregar un nuevo almacén.
4. **Editar Almacén**: Mantén presionado un almacén para editarlo o eliminarlo.
5. **Agregar Objeto**: Selecciona un almacén y usa el botón "Agregar Objeto" para agregar un nuevo objeto.
6. **Editar Objeto**: Mantén presionado un objeto para eliminarlo.
7. **Acerca de**: Usa el botón "Acerca de" para ver la información de la aplicación.
8. **Cerrar Sesión**: Usa el botón "Cerrar Sesión" para salir de la aplicación.

## Esquema de la Base de Datos

- **Usuarios**: Almacena la información de los usuarios.
- **Almacén**: Almacena la información de los almacenes.
- **Stock**: Almacena los objetos dentro de los almacenes.

## Licencia

Este proyecto está licenciado bajo la Licencia MIT. Consulta el archivo `LICENSE` para más detalles.

## Contacto

Para cualquier consulta o comentario, por favor contacta a Jonathan Gutiérrez Riego en jonathangrclases@gmail.com.