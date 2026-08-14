package ar.edu.unlu.chatmvc.vista.grafica.fxml;

import ar.edu.unlu.chatmvc.controlador.Controlador;
import ar.edu.unlu.chatmvc.modelo.IMensaje;
import ar.edu.unlu.chatmvc.modelo.IUsuario;
import ar.edu.unlu.chatmvc.vista.IVista;

import javafx.application.Platform;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Coordinador de vistas FXML — equivalente a VistaGrafica.java, pero
 * instanciando VentanaInicioSesionFXML/VentanaPrincipalFXML en vez de
 * las versiones a mano. Misma logica de wiring, mismo uso de
 * Platform.runLater(). Cero cambios en Controlador ni Modelo: esta es
 * la prueba en codigo real de que separar en capas (IVista) permite
 * intercambiar la vista sin tocar el resto de la app.
 */
public class VistaGraficaFXML implements IVista {

    private final VentanaInicioSesionFXML vInicioSesion;
    private final VentanaPrincipalFXML vPrincipal;
    private final Controlador controlador;

    public VistaGraficaFXML(Controlador controlador, Stage stageInicio) throws IOException {
        this.controlador = controlador;
        this.controlador.setVista(this);

        this.vInicioSesion = new VentanaInicioSesionFXML(stageInicio);
        this.vPrincipal = new VentanaPrincipalFXML(new Stage());

        this.vPrincipal.onClickEnviar(e -> {
            controlador.enviarMensaje(vPrincipal.getTextoMensaje());
            vPrincipal.setTextoMensaje("");
        });

        this.vPrincipal.onCerrarVentana(e -> controlador.cerrarApp());

        this.vInicioSesion.onClickIniciar(e -> {
            controlador.conectarUsuario(vInicioSesion.getNombreUsuario());
            mostrarVentanaChat();
        });
    }

    @Override
    public void mostrarListaUsuarios(IUsuario[] usuarios) {
        Platform.runLater(() -> vPrincipal.actualizarListaUsuarios(usuarios));
    }

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
