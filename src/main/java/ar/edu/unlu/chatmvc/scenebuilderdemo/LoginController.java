package ar.edu.unlu.chatmvc.scenebuilderdemo;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

/**
 * Controller de referencia para login.fxml (Bloque 3 del tutorial).
 *
 * JavaFX inyecta automaticamente los campos anotados con @FXML que
 * coincidan con un fx:id del FXML, y conecta los metodos @FXML
 * referenciados por onAction. No hace falta buscar los nodos a mano
 * ni registrar los listeners con codigo, como se hacia en el Bloque 1.
 */
public class LoginController {

    @FXML
    private TextField textUsuario;

    @FXML
    private Button btnIniciar;

    @FXML
    private void onIniciar(ActionEvent event) {
        String nombre = textUsuario.getText();
        System.out.println("Iniciar sesion con usuario: " + nombre);
    }
}
