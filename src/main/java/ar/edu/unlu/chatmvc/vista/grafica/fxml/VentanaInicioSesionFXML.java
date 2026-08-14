package ar.edu.unlu.chatmvc.vista.grafica.fxml;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Version FXML de VentanaInicioSesion (Bloque 1 y 2 del tutorial).
 *
 * Expone exactamente la misma API publica que VentanaInicioSesion
 * (constructor(Stage), onClickIniciar, getNombreUsuario, setVisible),
 * por eso VistaGraficaFXML puede usarla como reemplazo sin cambios
 * en Controlador ni Modelo.
 *
 * A diferencia del ejemplo standalone del Bloque 3 (LoginController),
 * aca esta misma clase actua como controller del FXML: se carga con
 * FXMLLoader y se asigna a si misma con loader.setController(this),
 * en vez de declarar fx:controller en el XML.
 */
public class VentanaInicioSesionFXML {

    private Stage stage;

    @FXML
    private TextField textUsuario;

    @FXML
    private Button btnIniciar;

    public VentanaInicioSesionFXML(Stage stage) throws IOException {
        this.stage = stage;

        FXMLLoader loader = new FXMLLoader(
            getClass().getResource("/fxml/ventana-inicio-sesion.fxml")
        );
        loader.setController(this);
        Parent root = loader.load();

        Scene scene = new Scene(root, 280, 130);
        scene.getStylesheets().add(
            getClass().getResource("/styles/chat.css").toExternalForm()
        );

        stage.setTitle("Chat - Inicio de Sesion (FXML)");
        stage.setResizable(false);
        stage.setScene(scene);
    }

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
