package ar.edu.unlu.chatmvc.vista.grafica.fxml;

import ar.edu.unlu.chatmvc.modelo.IMensaje;
import ar.edu.unlu.chatmvc.modelo.IUsuario;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * Version FXML de VentanaPrincipal (Bloque 1 y 2 del tutorial).
 *
 * Expone exactamente la misma API publica que VentanaPrincipal
 * (constructor(Stage), onClickEnviar, onCerrarVentana, actualizarChat,
 * actualizarListaUsuarios, getTextoMensaje, setTextoMensaje, setVisible).
 */
public class VentanaPrincipalFXML {

    private Stage stage;

    @FXML
    private TextArea textChat;

    @FXML
    private TextField textMensaje;

    @FXML
    private Button btnEnviar;

    @FXML
    private ListView<String> listUsuarios;

    private static final DateTimeFormatter FORMATO =
            DateTimeFormatter.ofPattern("dd/MM/yyyy  HH:mm ");

    public VentanaPrincipalFXML(Stage stage) throws IOException {
        this.stage = stage;

        FXMLLoader loader = new FXMLLoader(
            getClass().getResource("/fxml/ventana-principal.fxml")
        );
        loader.setController(this);
        Parent root = loader.load();

        Scene scene = new Scene(root, 500, 350);
        scene.getStylesheets().add(
            getClass().getResource("/styles/chat.css").toExternalForm()
        );

        stage.setTitle("Chat (FXML)");
        stage.setScene(scene);
        stage.setOnShown(e -> textMensaje.requestFocus());
    }

    public void onClickEnviar(EventHandler<ActionEvent> handler) {
        this.btnEnviar.setOnAction(handler);
        this.textMensaje.setOnAction(handler);
    }

    public void onCerrarVentana(EventHandler<WindowEvent> handler) {
        this.stage.setOnCloseRequest(handler);
    }

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
        textChat.setScrollTop(Double.MAX_VALUE);
    }

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
