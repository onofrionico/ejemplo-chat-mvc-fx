package ar.edu.unlu.chatmvc.pruebas;

import ar.edu.unlu.chatmvc.controlador.Controlador;
import ar.edu.unlu.chatmvc.modelo.Chat;
import ar.edu.unlu.chatmvc.vista.IVista;
import ar.edu.unlu.chatmvc.vista.grafica.fxml.VistaGraficaFXML;

import javafx.application.Application;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Punto de entrada paralelo a Prueba.java, pero usando VistaGraficaFXML
 * en vez de VistaGrafica. Mismo escenario: dos clientes sobre un mismo
 * Chat compartido. Prueba.java no se modifica ni se usa aca.
 *
 * Correr con:
 *   mvn javafx:run -Djavafx.mainClass=ar.edu.unlu.chatmvc.pruebas.PruebaFXML
 */
public class PruebaFXML extends Application {

    @Override
    public void start(Stage primaryStage) throws IOException {
        Chat modelo = new Chat();

        Controlador controlador1 = new Controlador(modelo);
        IVista vista1 = new VistaGraficaFXML(controlador1, primaryStage);
        vista1.iniciar();

        Stage secondaryStage = new Stage();
        Controlador controlador2 = new Controlador(modelo);
        IVista vista2 = new VistaGraficaFXML(controlador2, secondaryStage);
        vista2.iniciar();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
