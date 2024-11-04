# Turismonroy

**Turismonroy** es una aplicación móvil desarrollada para guías turísticos que permite capturar fotos y grabar videos de momentos especiales en entornos naturales. Además, la aplicación ofrece un acceso rápido a la galería de fotos del dispositivo.

## Características

- **Captura de Fotos**: La aplicación permite tomar fotos con la cámara del dispositivo y las guarda en una carpeta específica.
- **Grabación de Videos**: Los usuarios pueden grabar videos cortos y guardarlos en la misma carpeta de medios de la aplicación.
- **Zoom en la Cámara**: Se incluyen controles de zoom para facilitar el enfoque en objetos lejanos.
- **Cambio de Cámara**: Posibilidad de alternar entre la cámara frontal y trasera.
- **Acceso a la Galería**: Permite abrir la galería del dispositivo para revisar las fotos y videos almacenados.

## Estructura del Código

### 1. `MainActivity`

`MainActivity` es la pantalla principal de la aplicación y actúa como el punto de entrada. Incluye botones para:
- Capturar fotos (abre `CameraActivity`).
- Grabar videos (abre `VideoCaptureActivity`).
- Acceder a la galería del dispositivo (abre la galería predeterminada del sistema).

#### Funciones principales
- **`openDeviceGallery()`**: Utiliza un `Intent` para abrir la galería del sistema y permite al usuario revisar sus fotos y videos.

### 2. `CameraActivity`

`CameraActivity` se encarga de la captura de fotos. Incluye controles de zoom y cambio de cámara, además de un adaptador para mostrar en la pantalla una galería horizontal con las fotos tomadas recientemente.

#### Funciones principales
- **`startCamera()`**: Inicia la cámara y configura la vista previa.
- **`takePhoto()`**: Captura una foto y la guarda en el directorio de salida.
- **`configureZoom()`**: Configura el `SeekBar` para permitir el zoom en la cámara.
- **`toggleCamera()`**: Cambia entre la cámara frontal y trasera.
- **`updateGallery()`**: Actualiza el adaptador de la galería para mostrar las fotos más recientes.

### 3. `VideoCaptureActivity`

`VideoCaptureActivity` permite grabar videos. La interfaz incluye un botón de grabación y una opción para cambiar de cámara (frontal/trasera).

#### Funciones principales
- **`startCamera()`**: Inicia la cámara en modo de grabación de video.
- **`startRecording()`** y **`stopRecording()`**: Inician y detienen la grabación de video, respectivamente.
- **`toggleCamera()`**: Cambia entre la cámara frontal y trasera.

### 4. `MediaAdapter`

`MediaAdapter` es un adaptador personalizado para mostrar las fotos en un `RecyclerView`. Utiliza la biblioteca **Glide** para cargar imágenes de forma eficiente.

#### Funciones principales
- **`onBindViewHolder()`**: Carga las imágenes en los elementos del `RecyclerView`.
- **`updateMediaFiles()`**: Actualiza la lista de archivos en el adaptador y refresca la vista.

## Bibliotecas Utilizadas

- **CameraX**: Utilizada para acceder a las funciones de la cámara de Android. Proporciona una API moderna y sencilla para el manejo de cámara.
- **Glide**: Biblioteca de carga de imágenes que se usa en `MediaAdapter` para mostrar las miniaturas de las fotos en la galería.
- **RecyclerView**: Proporciona una vista en lista flexible para mostrar y actualizar las fotos capturadas dentro de la aplicación.

## Decisiones de Diseño

1. **Uso de la Galería del Dispositivo**: En lugar de implementar una galería personalizada dentro de la aplicación, se decidió abrir la galería del sistema con un `Intent`. Esto simplifica la aplicación y proporciona una experiencia de usuario más fluida al permitir que los usuarios utilicen la galería a la que están acostumbrados.

2. **Controles de Zoom y Cambio de Cámara**: Se implementaron controles de zoom y cambio de cámara en `CameraActivity` y `VideoCaptureActivity` para brindar flexibilidad en el uso de la cámara, especialmente en entornos naturales donde el usuario puede querer acercarse a objetos lejanos.

3. **Interfaz Intuitiva**: Se optó por un diseño minimalista con un fondo de gradiente y botones claramente etiquetados. El objetivo era que los guías turísticos pudieran operar la aplicación fácilmente con una sola mano y sin distracciones innecesarias.

## Instalación y Ejecución

1. Clona el repositorio en tu máquina local.
2. Abre el proyecto en Android Studio.
3. Conecta un dispositivo Android o utiliza un emulador.
4. Compila y ejecuta la aplicación en el dispositivo o emulador.


