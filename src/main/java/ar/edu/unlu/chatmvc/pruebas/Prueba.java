package ar.edu.unlu.chatmvc.pruebas;

import ar.edu.unlu.chatmvc.controlador.Controlador;
import ar.edu.unlu.chatmvc.modelo.Chat;
import ar.edu.unlu.chatmvc.vista.IVista;
import ar.edu.unlu.chatmvc.vista.grafica.VistaGrafica;

import javafx.application.Application;
import javafx.stage.Stage;

/**
 * Punto de entrada de la aplicacion JavaFX.
 *
 * =============================================================
 * CONCEPTO CLAVE: javafx.application.Application
 * =============================================================
 *
 * En Swing bastaba con tener un metodo main() que creara un JFrame.
 * En JavaFX existe un ciclo de vida (lifecycle) mas estructurado:
 *
 *   1. main()   --> llama a launch(), que inicializa el toolkit grafico
 *   2. init()   --> [opcional] inicializacion sin UI (hilo de fondo)
 *   3. start()  --> se construye la interfaz y se muestran las ventanas
 *   4. stop()   --> [opcional] limpieza al cerrar la aplicacion
 *
 * Solo start() es obligatorio de implementar.
 *
 * =============================================================
 * CONCEPTO CLAVE: Stage primario
 * =============================================================
 *
 * JavaFX crea automaticamente el primer Stage (ventana del SO) y lo
 * pasa como parametro a start(). No lo instanciamos nosotros.
 * Para ventanas adicionales, usamos new Stage().
 *
 * En este ejemplo de prueba creamos DOS "clientes" que comparten
 * un mismo modelo Chat, como en la version Swing original.
 *
 * =============================================================
 * CONCEPTO CLAVE: MVC no cambia
 * =============================================================
 *
 * Chat, Controlador e IVista no se modifican en absoluto.
 * Solo cambia la forma en que se inicia la aplicacion.
 * Eso es la ventaja de una buena separacion en capas MVC.
 */
public class Prueba extends Application {

    @Override
    public void start(Stage primaryStage) {
        // Unico modelo compartido por los dos clientes de prueba
        Chat modelo = new Chat();

        // Cliente 1: usa el Stage primario que JavaFX nos provee
        Controlador controlador1 = new Controlador(modelo);
        IVista vista1 = new VistaGrafica(controlador1, primaryStage);
        vista1.iniciar();

        // Cliente 2: creamos un Stage adicional con new Stage()
        Stage secondaryStage = new Stage();
        Controlador controlador2 = new Controlador(modelo);
        IVista vista2 = new VistaGrafica(controlador2, secondaryStage);
        vista2.iniciar();
    }

    public static void main(String[] args) {
        // launch() es la unica forma correcta de iniciar una app JavaFX.
        // Internamente crea la instancia de Prueba y llama a start().
        // Nunca se debe instanciar Application con "new Prueba()".
        launch(args);
    }
}
