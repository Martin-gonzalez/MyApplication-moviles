# Documentación Técnica Detallada de la Aplicación

## 1. Arquitectura General: MVVM + Repository

Esta aplicación sigue un patrón de arquitectura moderno y robusto, basado en **MVVM (Model-View-ViewModel)** y el **Patrón Repositorio**. Este diseño separa las responsabilidades, haciendo que la aplicación sea más fácil de mantener, escalar y testear.

- **Model (Modelo)**: Representa los datos y la lógica de negocio. En nuestro caso, son las clases `Juego` y `usuario`.
- **View (Vista)**: Es la capa de UI. En esta app, son las funciones Composable (`LoginScreen`, `CatalogoScreen`, etc.) que se encargan de mostrar los datos y capturar las interacciones del usuario.
- **ViewModel (Vista-Modelo)**: Actúa como un intermediario entre la Vista y el Modelo (a través del Repositorio). Expone los datos que la UI necesita y contiene la lógica de la interfaz (qué hacer cuando se pulsa un botón, etc.).
- **Repository (Repositorio)**: Es la **única fuente de verdad** para los datos de la aplicación. Abstrae el origen de los datos (en este caso, un archivo JSON) para que los ViewModels no necesiten saber si los datos vienen de una red, una base de datos local o un archivo.

---

## 2. Desglose de Archivos y Componentes Clave

A continuación se detalla el propósito de cada archivo y paquete importante en el proyecto.

### 📁 `repository/`

Este paquete es el corazón del manejo de datos.

- **`AppData.kt`**:
  - **Qué hace**: Es una clase de datos (`data class`) que representa la estructura completa de nuestra base de datos en JSON. Contiene una lista de `usuarios` y una lista de `juegos`.
  - **Por qué existe**: Define la "forma" de nuestro archivo `app_data.json`.

- **`AppDataRepository.kt`**:
  - **Qué hace**: Es la clase más importante para la persistencia. Se encarga de:
    1.  **Leer y Escribir**: Gestiona la lectura y escritura del archivo `app_data.json` en el almacenamiento interno del teléfono.
    2.  **Serialización**: Usa la librería `kotlinx.serialization` para convertir los objetos de Kotlin (`AppData`) a texto JSON y viceversa.
    3.  **Creación de Datos por Defecto**: Si el archivo no existe (la primera vez que se abre la app), crea una lista inicial con un usuario "admin", un usuario de prueba y una lista de juegos.
    4.  **Fuente de Verdad en Tiempo Real**: Expone los datos a través de un `StateFlow<AppData>`, que notifica a sus observadores (los ViewModels) cada vez que los datos cambian.
  - **Por qué existe**: Para centralizar y abstraer toda la lógica de acceso a datos.

### 📁 `model/`

Contiene las "plantillas" de los datos que maneja la aplicación.

- **`Juego.kt`** y **`usuario.kt`**:
  - **Qué hacen**: Son `data class` anotadas con `@Serializable`. Esto le dice a la librería de serialización cómo convertir una instancia de `Juego` o `usuario` a formato JSON.
  - **Por qué existen**: Definen la estructura y los campos de los objetos fundamentales de la app.

### 📁 `factory/`

Este paquete gestiona la creación de los ViewModels (Inyección de Dependencias).

- **`ViewModelFactory.kt`**:
  - **Qué hace**: Es una fábrica que sabe cómo crear instancias de `LoginViewModel`, `RegisterViewModel` y `CatalogoViewModel`. Su trabajo más importante es **inyectar** la instancia única del `AppDataRepository` en el constructor de cada ViewModel.
  - **Por qué existe**: Para asegurar que todos los ViewModels compartan **una única instancia** del repositorio, evitando así inconsistencias de datos.

### 📁 `viewmodel/`

Contiene la lógica de la interfaz y el estado de cada pantalla.

- **`LoginViewModel.kt`**, **`RegisterViewModel.kt`**, **`CatalogoViewModel.kt`**:
  - **Qué hacen**: Cada ViewModel se comunica con el `AppDataRepository` para realizar acciones y obtener datos.
    - `LoginViewModel`: Verifica las credenciales del usuario contra la lista de usuarios del repositorio.
    - `RegisterViewModel`: Valida los datos de un nuevo usuario y, si son correctos, le pide al repositorio que lo guarde.
    - `CatalogoViewModel`: Expone la lista de juegos del repositorio y le pide que guarde nuevos juegos.
  - **Exponen el Estado**: Cada uno utiliza un `StateFlow<UiState>` para mantener el estado de su pantalla (ej. `isLoading`, `error`, `success`), que la UI observa para redibujarse.

### 📁 `view/`

Contiene todas las pantallas (funciones Composable).

- **`...Screen.kt`** (ej. `LoginScreen.kt`, `CatalogoScreen.kt`, etc.):
  - **Qué hacen**: Son responsables únicamente de la parte visual. Dibujan los botones, textos, y listas. No contienen lógica de negocio.
  - **Son "tontas"**: Reciben un `ViewModel`, observan su estado (`collectAsState()`) y muestran la información correspondiente. Cuando el usuario interactúa (ej. pulsa un botón), la pantalla no decide qué hacer, simplemente notifica al `ViewModel` (ej. `viewModel.onLoginClick()`).

### 📄 `MyApplication.kt`

- **Qué hace**: Es el punto de entrada de más alto nivel de la aplicación. Su única función es crear la instancia única del `AppDataRepository` que se compartirá en toda la app.
- **Por qué existe**: Es la pieza clave de nuestro sistema de inyección de dependencias.

### 📄 `MainActivity.kt`

- **Qué hace**: Es el punto de entrada principal de la UI. Sus responsabilidades son:
  1.  Configurar la arquitectura: Obtiene el repositorio de `MyApplication`, crea la `ViewModelFactory` y la usa para instanciar los ViewModels.
  2.  Gestionar la Navegación: Configura el `NavHost` de Jetpack Navigation, definiendo todas las rutas y qué pantalla (`composable`) corresponde a cada una.
  3.  Orquestar el flujo entre pantallas: Contiene la lógica para navegar entre pantallas (ej. de `Login` a `Catalogo` o `BackOffice`).
