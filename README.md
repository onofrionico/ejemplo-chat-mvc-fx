# Chat MVC — Migración a JavaFX

Ejemplo educativo de migración de una aplicación de chat con interfaz gráfica **Swing** a **JavaFX**, aplicando el patrón de diseño **MVC (Modelo - Vista - Controlador)**.

Basado en el ejemplo original: [federicoradeljak/ejemplo-chat-mvc](https://github.com/federicoradeljak/ejemplo-chat-mvc)

---

## Tecnologías

- Java 18
- JavaFX 18.0.2
- Maven

---

## Estructura del proyecto

```
src/
└── main/
    ├── java/ar/edu/unlu/chatmvc/
    │   ├── controlador/
    │   │   └── Controlador.java       # Puente entre modelo y vista
    │   ├── modelo/
    │   │   ├── Chat.java              # Lógica del chat (Observable)
    │   │   ├── Eventos.java           # Enum de eventos del modelo
    │   │   ├── IMensaje.java
    │   │   ├── IUsuario.java
    │   │   ├── Mensaje.java
    │   │   └── Usuario.java
    │   ├── vista/
    │   │   ├── IVista.java            # Interfaz de la vista
    │   │   └── grafica/
    │   │       ├── VentanaInicioSesion.java  # Stage de login
    │   │       ├── VentanaPrincipal.java     # Stage del chat
    │   │       └── VistaGrafica.java         # Coordinador de vistas
    │   └── pruebas/
    │       └── Prueba.java            # Punto de entrada (Application)
    └── resources/
        └── styles/
            └── chat.css               # Hoja de estilos JavaFX
```

---

## Conceptos de JavaFX introducidos (commit a commit)

| Commit | Concepto |
|--------|----------|
| `chore`: migrar a Maven | `pom.xml`, dependencia `javafx-controls`, `javafx-maven-plugin` |
| `feat(prueba)` | `Application`, `launch()`, `start(Stage)`, ciclo de vida |
| `feat(vista)` VentanaInicioSesion | `Stage`, `Scene`, `GridPane`, `Label`, `TextField`, `Button` |
| `feat(vista)` VentanaPrincipal | `BorderPane`, `HBox`, `VBox`, `ListView<String>`, `TextArea` |
| `feat(vista)` VistaGrafica | `EventHandler`, lambdas, `Platform.runLater()` |
| `feat(estilos)` | CSS en JavaFX, selectores `-fx-`, pseudo-clases `:hover`, `:pressed` |

---

## Cómo correrlo localmente en IntelliJ IDEA

### Requisitos previos

- [IntelliJ IDEA](https://www.jetbrains.com/idea/) (Community o Ultimate)
- [JDK 18](https://www.oracle.com/java/technologies/javase/jdk18-archive-downloads.html)
- Conexión a internet (Maven descarga JavaFX automáticamente)

---

### Paso 1 — Clonar el repositorio

Abrí IntelliJ IDEA y en la pantalla de inicio elegí **Get from VCS**.

```
https://github.com/onofrionico/ejemplo-chat-mvc-fx.git
```

Hacé click en **Clone** y esperá que se descargue el proyecto.

---

### Paso 2 — Cargar el proyecto Maven

IntelliJ detecta el `pom.xml` automáticamente y muestra una notificación:

> *"Maven build script found. Load it?"*

Hacé click en **Load Maven Project**.

Si no aparece la notificación:
`File` → `Project Structure` → `Modules` → `+` → `Import Module` → seleccioná el `pom.xml`

---

### Paso 3 — Verificar el SDK de Java

`File` → `Project Structure` → `Project`

- **SDK**: seleccioná **Java 18**
- **Language level**: **18**

Si Java 18 no aparece en la lista:
1. Hacé click en `Add SDK` → `Download JDK`
2. Elegí **Version 18** y cualquier vendor (Oracle, Temurin, etc.)
3. Hacé click en **Download**

---

### Paso 4 — Descargar las dependencias

En la barra lateral derecha abrí el panel **Maven** y hacé click en el ícono 🔄 **Reload All Maven Projects**.

Maven descarga automáticamente `javafx-controls-18.0.2` desde Maven Central. No es necesario instalar JavaFX por separado.

---

### Paso 5 — Ejecutar la aplicación

En el panel **Maven** (barra derecha), navegá hasta:

```
chat-mvc-fx
  └── Plugins
        └── javafx
              └── javafx:run   ← doble click acá
```

Se abrirán **dos ventanas de inicio de sesión** (dos clientes de prueba que comparten el mismo modelo).

> **¿Por qué dos ventanas?** La clase `Prueba` crea dos instancias de `VistaGrafica` sobre un único `Chat` compartido, para simular dos usuarios chateando desde la misma máquina.

---

### Solución de problemas frecuentes

| Error | Causa | Solución |
|-------|-------|----------|
| `SDK not defined` | No hay JDK configurado | `File → Project Structure → SDK → JDK 18` |
| `Cannot resolve symbol 'javafx'` | Maven no descargó las dependencias | Clic en 🔄 Reload Maven Projects |
| `Error: JavaFX runtime components are missing` | Se usó el botón ▶ en vez de Maven | Usar siempre `javafx:run` desde el panel Maven |
| La app se cierra inmediatamente | `main()` no llama a `launch()` | Verificar que `Prueba` extiende `Application` |
| `ClassNotFoundException` | Proyecto no compilado | Ejecutar `mvn compile` en la terminal integrada |

---

## Diferencias clave Swing vs JavaFX

| Concepto | Swing | JavaFX |
|----------|-------|--------|
| Ventana | `JFrame` | `Stage` |
| Contenedor raíz | `JPanel` | `Scene` |
| Layout en grilla | `MigLayout` / `GridLayout` | `GridPane` |
| Layout en fila | `FlowLayout` | `HBox` |
| Layout en columna | `BoxLayout` | `VBox` |
| Layout con zonas | `BorderLayout` | `BorderPane` |
| Texto editable | `JTextField` | `TextField` |
| Texto multilínea | `JTextPane` | `TextArea` |
| Lista | `JList` + `AbstractListModel` | `ListView<T>` + `ObservableList` |
| Botón | `JButton` | `Button` |
| Etiqueta | `JLabel` | `Label` |
| Evento de botón | `ActionListener` | `EventHandler<ActionEvent>` (lambda) |
| Evento de cierre | `WindowAdapter.windowClosing()` | `stage.setOnCloseRequest()` |
| Hilo de UI | `SwingUtilities.invokeLater()` | `Platform.runLater()` |
| Estilos | Propiedades Java (`.setBackground()`) | CSS con propiedades `-fx-` |
