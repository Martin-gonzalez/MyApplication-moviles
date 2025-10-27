# Documentación de la Aplicación de E-Commerce de Videojuegos

## 1. Resumen de la Arquitectura

Esta aplicación utiliza una arquitectura moderna y escalable basada en los principios recomendados por Google para el desarrollo de Android. La estructura principal sigue el patrón **UI - ViewModel - Repository**.

- **UI (Capa de Interfaz de Usuario)**: Construida 100% con **Jetpack Compose**. Las pantallas (`Screens`) son funciones Composable que observan datos y envían eventos.
- **ViewModel**: Clases que sobreviven a los cambios de configuración y exponen el estado de la UI a través de `StateFlow`. Contienen la lógica de la interfaz y se comunican con el Repositorio.
- **Repository (Repositorio)**: Actúa como la **única fuente de verdad** para los datos de la aplicación. Es el único componente que sabe cómo obtener y guardar los datos.

## 2. Flujo de Datos y Persistencia

La característica clave de esta aplicación es su sistema de persistencia de datos, que no depende de una base de datos compleja como Room o SQLite, sino de un **único archivo JSON**.

### `AppDataRepository.kt` - El Corazón de los Datos

- **Fuente de Verdad Única**: Esta clase es la responsable de manejar todos los datos de la aplicación (usuarios y juegos).
- **Persistencia en JSON**: Al iniciarse, el repositorio intenta leer un archivo llamado `app_data.json` del almacenamiento interno del dispositivo.
  - Si el archivo existe, lo decodifica usando `kotlinx.serialization` y carga los datos.
  - Si el archivo no existe (la primera vez que se abre la app) o está corrupto, crea una lista de datos por defecto (incluyendo un usuario administrador).
- **Guardado Automático**: Cada vez que se realiza una acción que modifica los datos (registrar un usuario, agregar un juego), el repositorio actualiza su estado y **reescribe el archivo `app_data.json` completo**. Esto asegura que los datos siempre estén sincronizados y persistidos.

### `StateFlow` - Datos en Tiempo Real

El `AppDataRepository` expone los datos a través de un `StateFlow<AppData>`. Esto significa que los ViewModels pueden suscribirse a este flujo y recibir actualizaciones automáticamente cada vez que el archivo JSON es modificado. La UI, a su vez, observa los `StateFlow` de los ViewModels, completando un ciclo de datos reactivo y eficiente.

## 3. Inyección de Dependencias

Para evitar crear múltiples instancias del repositorio (lo que crearía inconsistencias), la aplicación utiliza un sistema simple y efectivo de **inyección de dependencias**.

1.  **`MyApplication.kt`**: Es una clase personalizada que se ejecuta al iniciar la aplicación. Su única responsabilidad es crear una **instancia única** del `AppDataRepository`.
2.  **`AndroidManifest.xml`**: Se ha modificado para que el sistema operativo utilice nuestra clase `MyApplication`, asegurando que el repositorio se cree una sola vez.
3.  **`ViewModelFactory.kt`**: Esta fábrica es la encargada de crear todos los ViewModels. Al crearla, le pasamos la instancia única del repositorio. Luego, la fábrica se asegura de que cada ViewModel reciba ese mismo repositorio en su constructor.
4.  **`MainActivity.kt`**: En el punto de entrada de la UI, obtenemos el repositorio desde `MyApplication`, creamos la `ViewModelFactory` y la usamos para instanciar todos los ViewModels. Esto conecta todo el sistema.

## 4. Navegación

La navegación se gestiona con **Jetpack Navigation para Compose**. En `MainActivity.kt`, un `NavHost` define todas las pantallas (`composable`) y las rutas para moverse entre ellas (`navController.navigate(...)`).

- La lógica de navegación principal (por ejemplo, decidir si ir a la pantalla de "Catálogo" o "Back Office" después del login) se maneja en `MainActivity`, observando el estado del `LoginViewModel`.

## 5. Componentes Principales

- **`Model`**: Contiene las clases de datos `Juego` y `usuario`. Ambas están anotadas con `@Serializable` para permitir su conversión a JSON.
- **`view`**: Contiene todas las pantallas de la aplicación, construidas con Jetpack Compose.
- **`viewmodel`**: Contiene los ViewModels (`LoginViewModel`, `RegisterViewModel`, `CatalogoViewModel`), que albergan la lógica de la UI y se comunican con el repositorio.
- **`repository`**: Contiene el `AppDataRepository`, el componente central para la gestión de datos.
- **`factory`**: Contiene la `ViewModelFactory` para la inyección de dependencias.

Este diseño, aunque simple, es extremadamente robusto y escalable. Si en el futuro se quisiera cambiar la fuente de datos (por ejemplo, a una base de datos en la nube como Firebase), solo se necesitaría modificar el `AppDataRepository`, sin tener que tocar los ViewModels o la UI.
