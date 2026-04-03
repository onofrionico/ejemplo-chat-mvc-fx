package ar.edu.unlu.chatmvc.vista.grafica;

import ar.edu.unlu.chatmvc.controlador.Controlador;
import ar.edu.unlu.chatmvc.modelo.IMensaje;
import ar.edu.unlu.chatmvc.modelo.IUsuario;
import ar.edu.unlu.chatmvc.vista.IVista;

import javafx.application.Platform;
import javafx.stage.Stage;

/**
 * Coordinador de vistas en JavaFX.
 *
 * Esta clase conecta el Controlador con las dos ventanas graficas.
 * Su rol en el patron MVC no cambia respecto a la version Swing.
 *
 * =============================================================
 * CONCEPTO CLAVE: EventHandler vs ActionListener
 * =============================================================
 *
 * En Swing los eventos se manejaban con clases anonimas:
 *
 *   boton.addActionListener(new ActionListener() {
 *       public void actionPerformed(ActionEvent e) {
 *           // logica
 *       }
 *   });
 *
 * En JavaFX se usa EventHandler<T>, que en la practica se escribe
 * como una expresion lambda (Java 8+):
 *
 *   boton.setOnAction(e -> {
 *       // logica
 *   });
 *
 * Notar que en Swing se usaba addActionListener() (acumula listeners).
 * En JavaFX se usa setOnAction() (reemplaza el handler anterior).
 *
 * =============================================================
 * CONCEPTO CLAVE: Platform.runLater()
 * =============================================================
 *
 * JavaFX, al igual que Swing (con SwingUtilities.invokeLater),
 * exige que TODA modificacion de la UI ocurra en el hilo dedicado:
 * el JavaFX Application Thread (JAT).
 *
 * El metodo update() del Controlador puede ser llamado desde
 * cualquier hilo (especialmente en versiones distribuidas con RMI
 * o sockets). Si ese hilo no es el JAT, JavaFX lanza:
 *   java.lang.IllegalStateException: Not on FX application thread
 *
 * La solucion es envolver el codigo de UI en Platform.runLater():
 *
 *   Platform.runLater(() -> {
 *       // este bloque se ejecutara en el JAT
 *       nodo.setText("...");
 *   });
 *
 * REGLA PRACTICA:
 *   Cualquier llamada a metodos de Node (setText, setItems, show, hide)
 *   que provenga de un Observer/callback debe estar dentro de runLater().
 *
 * En esta app local el Observer se dispara en el mismo JAT, por lo que
 * no falla sin runLater(). Pero agregarlo es la practica correcta
 * y evita bugs dificiles de reproducir en versiones distribuidas.
 *
 * =============================================================
 * CONCEPTO CLAVE: Inyeccion de Stage
 * =============================================================
 *
 * VistaGrafica recibe el Stage primario desde Prueba.start().
 * VentanaInicioSesion usa ese Stage. VentanaPrincipal crea su propio
 * Stage interno con new Stage(), ya que necesita una ventana separada.
 *
 * Esto refleja el patron de diseno: las vistas no crean ventanas del SO,
 * las reciben o las crean internamente segun su responsabilidad.
 */
public class VistaGrafica implements IVista {

    private final VentanaInicioSesion vInicioSesion;
    private final VentanaPrincipal vPrincipal;
    private final Controlador controlador;

    public VistaGrafica(Controlador controlador, Stage stageInicio) {
        this.controlador = controlador;
        this.controlador.setVista(this);

        // VentanaInicioSesion usa el Stage que nos pasa Prueba
        this.vInicioSesion = new VentanaInicioSesion(stageInicio);

        // VentanaPrincipal crea su propio Stage (ventana separada)
        this.vPrincipal = new VentanaPrincipal(new Stage());

        // ---------------------------------------------------------
        // Eventos con lambda (reemplazan clases anonimas de Swing)
        // ---------------------------------------------------------

        // Boton "Enviar": enviar mensaje y limpiar el campo
        this.vPrincipal.onClickEnviar(e -> {
            controlador.enviarMensaje(vPrincipal.getTextoMensaje());
            vPrincipal.setTextoMensaje("");
        });

        // Cerrar la ventana principal: desconectar del modelo
        // Equivale al WindowAdapter.windowClosing() de Swing
        this.vPrincipal.onCerrarVentana(e -> controlador.cerrarApp());

        // Boton "Iniciar": conectar usuario y cambiar a la ventana principal
        this.vInicioSesion.onClickIniciar(e -> {
            controlador.conectarUsuario(vInicioSesion.getNombreUsuario());
            mostrarVentanaChat();
        });
    }

    /**
     * Llamado por el Controlador cuando cambia la lista de usuarios.
     * Platform.runLater garantiza que la actualizacion de UI
     * ocurra en el JavaFX Application Thread.
     */
    @Override
    public void mostrarListaUsuarios(IUsuario[] usuarios) {
        Platform.runLater(() -> vPrincipal.actualizarListaUsuarios(usuarios));
    }

    /**
     * Llamado por el Controlador cuando llega un nuevo mensaje.
     * Platform.runLater garantiza la seguridad en multithreading.
     */
    @Override
    public void mostrarChat(IMensaje[] mensajes) {
        Platform.runLater(() -> vPrincipal.actualizarChat(mensajes));
    }

    @Override
    public void iniciar() {
        mostrarInicioSesion();
    }

    private void mostrarInicioSesion() {
        vInicioSesion.setVisible(true);
        vPrincipal.setVisible(false);
    }

    private void mostrarVentanaChat() {
        vInicioSesion.setVisible(false);
        vPrincipal.setVisible(true);
    }
}
