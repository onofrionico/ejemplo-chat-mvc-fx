package ar.edu.unlu.chatmvc.scenebuilderdemo;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Punto de entrada independiente para el ejemplo del Bloque 3 (Scene Builder).
 *
 * No tiene relacion con Prueba.java (el chat MVC): esta clase existe
 * solo para mostrar login.fxml + LoginController funcionando, sin
 * mezclarlo con el resto de la app.
 *
 * Correr con: mvn javafx:run -Djavafx.mainClass=ar.edu.unlu.chatmvc.scenebuilderdemo.DemoSceneBuilder
 */
public class DemoSceneBuilder extends Application {

    @Override
    public void start(Stage primaryStage) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/login.fxml"));
        primaryStage.setTitle("Demo Scene Builder - Login");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
