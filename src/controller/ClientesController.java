package controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;

public class ClientesController implements Resisable{

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Pane menu;

    @FXML
    private Button btnClientes;

    @FXML
    private Button btnCtaCte;

    @FXML
    private Button btnAlta;

    @FXML
    private Pane contenido;

    @FXML
	public void initialize() {
	    
	}

    /**
     *  redimenciona el contenido
     */
	@Override
	public void redimencionar(double width, double height) {
		contenido.setPrefSize(width - menu.getPrefWidth(), height);
		menu.setPrefHeight(height);
	}
	
	/**
	 * carga la tabla de clientes
	 */
	public void loadCtaCte() {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/viewClientes/CtaCteView.fxml"));
		try {
			contenido.getChildren().setAll((Pane) loader.load());
			CtaCteController controller = loader.getController();
			controller.setParent(contenido);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	

	/**
	 * cambia el contenido de la pantalla
	 * @param event
	 */
	@FXML
    private void cambiarPantalla(ActionEvent event) {
		String vista;
		Button btn;
		
		if(event.getSource() instanceof Button)
			btn = (Button) event.getSource();
		else
			return;
		
		
		if(btn.equals(btnCtaCte)) {
			vista = "CtaCte";
		} else { // entonces se preciono el boton de altas
			vista = "Alta";
		}

		FXMLLoader loader = new FXMLLoader(getClass().getResource("/viewClientes/" + vista + "View.fxml"));
		try {
			contenido.getChildren().setAll((Pane) loader.load());
			
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
}
