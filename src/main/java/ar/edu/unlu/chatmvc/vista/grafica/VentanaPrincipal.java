package ar.edu.unlu.chatmvc.vista.grafica;

import ar.edu.unlu.chatmvc.modelo.IMensaje;
import ar.edu.unlu.chatmvc.modelo.IUsuario;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * Ventana principal del chat reescrita en JavaFX.
 *
 * =============================================================
 * CONCEPTO CLAVE: BorderPane
 * =============================================================
 *
 * BorderPane divide el area de la Scene en 5 zonas:
 *
 *          ┌──────────────────────────┐
 *          │          TOP             │
 *          ├─────────────┬────────────┤
 *          │             │            │
 *          │   CENTER    │   RIGHT    │
 *          │  (crece)    │            │
 *          ├─────────────┴────────────┤
 *          │          BOTTOM          │
 *          └──────────────────────────┘
 *
 * La zona CENTER crece automaticamente para ocupar todo el espacio
 * disponible. Es el equivalente al MigLayout("[grow][50px][::80px]".
 *
 * =============================================================
 * CONCEPTO CLAVE: HBox y VBox
 * =============================================================
 *
 * HBox  = apila sus hijos horizontalmente (fila)
 * VBox  = apila sus hijos verticalmente   (columna)
 *
 * HBox.setHgrow(nodo, Priority.ALWAYS)  ->  el nodo se estira
 * VBox.setVgrow(nodo, Priority.ALWAYS)  ->  el nodo se estira
 *
 * =============================================================
 * CONCEPTO CLAVE: ListView<String>
 * =============================================================
 *
 * Reemplaza JList con generics tipados.
 * No necesita AbstractListModel; se alimenta con un ObservableList:
 *   listView.setItems(FXCollections.observableArrayList(nombres));
 *
 * =============================================================
 * CONCEPTO CLAVE: TextArea
 * =============================================================
 *
 * Reemplaza JTextPane para el log de mensajes.
 * textArea.setEditable(false) -> solo lectura
 * textArea.setScrollTop(Double.MAX_VALUE) -> scroll al final
 *
 * Estructura del scene graph:
 *
 *   Stage
 *     └── Scene (500 x 350)
 *           └── BorderPane
 *                 ├── center: HBox
 *                 │     ├── TextArea   (chat, crece)
 *                 │     └── VBox
 *                 │           ├── Label   "Usuarios conectados:"
 *                 │           └── ListView<String>
 *                 └── bottom: HBox
 *                       ├── TextField  (mensaje, crece)
 *                       └── Button     "Enviar"
 */
public class VentanaPrincipal {

    private Stage stage;
    private TextArea textChat;
    private TextField textMensaje;
    private Button btnEnviar;
    private ListView<String> listUsuarios;

    // Corrección del bug original: "YYYY" es año basado en semana (incorrecto),
    // "yyyy" es el año civil; "MM" despues de "HH" eran meses (no minutos).
    private static final DateTimeFormatter FORMATO =
            DateTimeFormatter.ofPattern("dd/MM/yyyy  HH:mm ");

    public VentanaPrincipal(Stage stage) {
        this.stage = stage;

        // ---------------------------------------------------------
        // Area de chat: TextArea de solo lectura (reemplaza JTextPane)
        // ---------------------------------------------------------
        textChat = new TextArea();
        textChat.setEditable(false);    // el usuario no puede escribir aqui
        textChat.setWrapText(true);     // quiebre de linea automatico
        textChat.setId("chat-area");    // ID para el selector CSS #chat-area
        HBox.setHgrow(textChat, Priority.ALWAYS);  // ocupa el ancho disponible

        // ---------------------------------------------------------
        // Lista de usuarios: ListView<String> (reemplaza JList)
        // ---------------------------------------------------------
        listUsuarios = new ListView<>();
        listUsuarios.setPrefWidth(130);

        Label lblUsuarios = new Label("Usuarios conectados:");

        // VBox: apila verticalmente el label y la lista
        VBox panelUsuarios = new VBox(4, lblUsuarios, listUsuarios);
        VBox.setVgrow(listUsuarios, Priority.ALWAYS);  // la lista crece verticalmente
        panelUsuarios.setPadding(new Insets(0, 0, 0, 6));

        // ---------------------------------------------------------
        // HBox central: chat (izquierda) + panel de usuarios (derecha)
        // ---------------------------------------------------------
        HBox centroBox = new HBox(textChat, panelUsuarios);
        centroBox.setPadding(new Insets(6));

        // ---------------------------------------------------------
        // Barra inferior: campo de texto + boton Enviar
        // ---------------------------------------------------------
        textMensaje = new TextField();
        textMensaje.setPromptText("Escribi tu mensaje...");
        HBox.setHgrow(textMensaje, Priority.ALWAYS);  // ocupa el ancho disponible

        btnEnviar = new Button("Enviar");

        HBox barraInferior = new HBox(6, textMensaje, btnEnviar);
        barraInferior.setPadding(new Insets(0, 6, 6, 6));

        // ---------------------------------------------------------
        // BorderPane: layout raiz con zonas top/center/bottom/left/right
        // ---------------------------------------------------------
        BorderPane root = new BorderPane();
        root.setCenter(centroBox);
        root.setBottom(barraInferior);

        // ---------------------------------------------------------
        // Scene y Stage
        // ---------------------------------------------------------
        Scene scene = new Scene(root, 500, 350);

        // Cargar la hoja de estilos CSS desde el classpath
        scene.getStylesheets().add(
            getClass().getResource("/styles/chat.css").toExternalForm()
        );

        stage.setTitle("Chat");
        stage.setScene(scene);

        // Foco automatico en el campo de texto al mostrar la ventana
        stage.setOnShown(e -> textMensaje.requestFocus());
    }

    /**
     * Registra el EventHandler del boton Enviar.
     * El mismo handler se usa para el campo de texto (tecla ENTER).
     */
    public void onClickEnviar(EventHandler<ActionEvent> handler) {
        this.btnEnviar.setOnAction(handler);
        this.textMensaje.setOnAction(handler);  // ENTER en el TextField envia
    }

    /**
     * Registra el handler que se ejecuta cuando el usuario cierra la ventana.
     * Equivale a JFrame.addWindowListener -> windowClosing().
     *
     * En JavaFX: stage.setOnCloseRequest(handler)
     * El WindowEvent se puede consumir con event.consume() para cancelar el cierre.
     */
    public void onCerrarVentana(EventHandler<WindowEvent> handler) {
        this.stage.setOnCloseRequest(handler);
    }

    /**
     * Actualiza el area de chat con todos los mensajes del modelo.
     * Reconstruye el texto completo (igual que la version Swing).
     */
    public void actualizarChat(IMensaje[] mensajes) {
        StringBuilder sb = new StringBuilder();
        for (IMensaje m : mensajes) {
            sb.append(m.getFecha().format(FORMATO));
            if (m.isMensajeDelSistema()) {
                sb.append("AVISO: ");
            } else {
                sb.append(m.getUsuario().getNombre()).append(": ");
            }
            sb.append(m.getTexto()).append("\n");
        }
        textChat.setText(sb.toString());
        textChat.setScrollTop(Double.MAX_VALUE);  // scroll automatico al final
    }

    /**
     * Actualiza la lista de usuarios conectados.
     *
     * En Swing se usaba AbstractListModel con getElementAt() y getSize().
     * En JavaFX alcanza con pasar un ObservableList<String>:
     *   listView.setItems(FXCollections.observableArrayList(coleccion));
     */
    public void actualizarListaUsuarios(IUsuario[] usuarios) {
        listUsuarios.setItems(
            FXCollections.observableArrayList(
                Arrays.stream(usuarios)
                      .map(IUsuario::getNombre)
                      .collect(Collectors.toList())
            )
        );
    }

    public String getTextoMensaje() {
        return this.textMensaje.getText();
    }

    public void setTextoMensaje(String texto) {
        this.textMensaje.setText(texto);
    }

    public void setVisible(boolean visible) {
        if (visible) {
            stage.show();
        } else {
            stage.hide();
        }
    }
}
