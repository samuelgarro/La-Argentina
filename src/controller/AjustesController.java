package controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;

public class AjustesController implements Resisable {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Pane menu;

    @FXML
    private Button btnRubro;

    @FXML
    private Button btnPrecio;

    @FXML
    private Pane contenido;

    @FXML
    public void initialize() {    	

    }

	@Override
	public void redimencionar(double width, double height) {
		menu.setPrefHeight(height);
		
		contenido.setPrefSize(width - menu.getPrefWidth(), height);
	}
	
	/**
	 * carga la pantalla de rubros
	 */
	public void loadRubrosView() {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/viewAjustes/RubrosView.fxml"));
		try {
			
			contenido.getChildren().setAll( (Pane) loader.load());
			RubrosController controller = loader.getController();
			
			controller.redimencionar(contenido.getPrefWidth(), contenido.getPrefHeight());
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	/**
	 * carga la pantalla de rubros
	 */
	public void loadPreciosView() {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/viewAjustes/PreciosView.fxml"));
		try {
			
			contenido.getChildren().setAll( (Pane) loader.load());
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
}
