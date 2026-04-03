package ar.edu.unlu.chatmvc.vista.grafica;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.stage.Stage;

/**
 * Ventana de inicio de sesion reescrita en JavaFX.
 *
 * =============================================================
 * CONCEPTO CLAVE: Stage, Scene y el Scene Graph
 * =============================================================
 *
 * En Swing, JFrame era la ventana y JPanel el contenedor de controles.
 * En JavaFX la jerarquia es:
 *
 *   Stage   = la ventana del sistema operativo (barra de titulo,
 *             botones minimizar/maximizar/cerrar).
 *             Equivalente aproximado: JFrame.
 *
 *   Scene   = el area de contenido dentro del Stage.
 *             Contiene el "scene graph": un arbol de Nodes.
 *             Equivalente aproximado: JPanel raiz.
 *
 *   Node    = cualquier elemento visual (Label, Button, TextField,
 *             layouts como GridPane, etc.).
 *             Equivalente aproximado: JComponent.
 *
 * Relacion: un Stage tiene una Scene; una Scene tiene un Node raiz.
 *   stage.setScene(scene);
 *   scene = new Scene(nodoRaiz, ancho, alto);
 *
 * =============================================================
 * CONCEPTO CLAVE: GridPane como layout
 * =============================================================
 *
 * Reemplaza a MigLayout("", "[][grow]", "[][]").
 * GridPane organiza los nodos en filas y columnas.
 *
 * Para agregar un nodo:  gridPane.add(nodo, columna, fila);
 * Para configurar columnas: ColumnConstraints con setHgrow(Priority.ALWAYS)
 *
 * =============================================================
 * CONCEPTO CLAVE: Controles basicos
 * =============================================================
 *
 *   Label       reemplaza JLabel
 *   TextField   reemplaza JTextField  (mismo nombre, diferente paquete)
 *   Button      reemplaza JButton
 *
 * Estructura del scene graph de esta ventana:
 *
 *   Stage
 *     └── Scene  (280 x 130)
 *           └── GridPane  (layout principal)
 *                 ├── Label      "Usuario:"   [col 0, fila 0]
 *                 ├── TextField  textUsuario  [col 1, fila 0]
 *                 └── Button     "Iniciar"    [col 1, fila 1]
 */
public class VentanaInicioSesion {

    private Stage stage;
    private TextField textUsuario;
    private Button btnIniciar;

    /**
     * @param stage el Stage (ventana) que esta vista va a usar.
     *              El Stage es inyectado desde afuera (VistaGrafica)
     *              para que el ciclo de vida lo controle el llamador.
     */
    public VentanaInicioSesion(Stage stage) {
        this.stage = stage;

        // ---------------------------------------------------------
        // GridPane: layout en grilla de 2 columnas
        // ---------------------------------------------------------
        GridPane grid = new GridPane();
        grid.setPadding(new Insets(12));  // margen interno (equivale a EmptyBorder)
        grid.setHgap(8);                  // espacio horizontal entre celdas
        grid.setVgap(8);                  // espacio vertical entre celdas

        // Columna 0: ancho minimo, alineada a la derecha (para la etiqueta)
        ColumnConstraints col0 = new ColumnConstraints();
        col0.setHalignment(HPos.RIGHT);

        // Columna 1: crece para ocupar el espacio disponible (equivale a "grow")
        ColumnConstraints col1 = new ColumnConstraints();
        col1.setHgrow(Priority.ALWAYS);

        grid.getColumnConstraints().addAll(col0, col1);

        // ---------------------------------------------------------
        // Controles: Label, TextField, Button
        // ---------------------------------------------------------
        Label lblUsuario = new Label("Usuario:");

        textUsuario = new TextField();
        textUsuario.setPromptText("Ingresa tu nombre...");  // texto placeholder

        btnIniciar = new Button("Iniciar");
        GridPane.setHalignment(btnIniciar, HPos.RIGHT);  // alinear boton a la derecha

        // Agregar nodos a la grilla: add(nodo, columna, fila)
        grid.add(lblUsuario,  0, 0);
        grid.add(textUsuario, 1, 0);
        grid.add(btnIniciar,  1, 1);

        // ---------------------------------------------------------
        // Scene: contenedor visual que va dentro del Stage
        // new Scene(nodoRaiz, ancho, alto)
        // ---------------------------------------------------------
        Scene scene = new Scene(grid, 280, 130);

        // Tecla ENTER activa el boton (equivale a setDefaultButton en Swing)
        scene.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.ENTER) {
                btnIniciar.fire();
            }
        });

        // ---------------------------------------------------------
        // Configurar el Stage (la ventana del SO)
        // ---------------------------------------------------------
        stage.setTitle("Chat - Inicio de Sesion");
        stage.setResizable(false);
        stage.setScene(scene);
    }

    /**
     * Registra el manejador de evento para el boton Iniciar.
     *
     * En JavaFX los eventos se manejan con EventHandler<T> en lugar
     * de ActionListener. En la practica se usan expresiones lambda:
     *   boton.setOnAction(e -> { ... });
     *
     * @param handler accion a ejecutar al hacer click en "Iniciar"
     */
    public void onClickIniciar(EventHandler<ActionEvent> handler) {
        this.btnIniciar.setOnAction(handler);
    }

    public String getNombreUsuario() {
        return this.textUsuario.getText();
    }

    public void setVisible(boolean visible) {
        if (visible) {
            stage.show();
        } else {
            stage.hide();
        }
    }
}
