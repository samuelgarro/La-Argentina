package controller;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.ToolBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.Pane;

public class PrincipalController {

	@FXML
	private ResourceBundle resources;

	@FXML
	private URL location;

	@FXML
	private ToolBar menu;

	@FXML
	private Button btnInventario;

	@FXML
	private Button btnVentas;

	@FXML
	private Button btnCompras;

	@FXML
	private Button btnCaja;

	@FXML
	private Button btnProveedores;

	@FXML
	private Button btnAjustes;

	@FXML
	private Button btnClientes;

	@FXML
	private Pane contenido;

	@FXML
	public void initialize() {
	}

	/**
	 * evento click sobre los botones del menu. cambia el contenido del panel
	 * contenido.
	 * 
	 * @param event
	 */
	@FXML
	public void cambiarContenido(ActionEvent event) {
		Button btn;
		// sale si se ingreso por algo que no sea un click en un boton del menu
		if (event.getSource() instanceof Button)
			btn = (Button) event.getSource();
		else
			return;

		btn.setDisable(true); // evita seguir apretando
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/" + btn.getText() + "View.fxml"));
		Pane panel;
		try {
			// agrega los elementos de la pantalla
			panel = loader.load();
			contenido.getChildren().setAll(panel);

			Resisable controller = loader.getController();
			controller.redimencionar(contenido.getPrefWidth(), contenido.getPrefHeight());

			if (btn.equals(btnInventario))
				((InventarioController) controller).setParent(contenido);
			else if (btn.equals(btnAjustes))
				((AjustesController) controller).loadRubrosView();
			else if (btn.equals(btnProveedores))
				((ProveedoresController) controller).setParent(contenido);
			else if (btn.equals(btnClientes))
				((ClientesController) controller).loadCtaCte();

		} catch (IOException e) {
			e.printStackTrace();
		}

		btn.setDisable(false); // habilita nuevamente el boton
	}

	/**
	 * redimenciona la barra de menu para ajustarla al tamño de la pantalla
	 * 
	 * @param width  ancho
	 * @param heigth
	 */
	public void redimencionar(double width, double heigth) {
		menu.setPrefWidth(width);
		contenido.setPrefHeight(heigth - 85);
		contenido.setPrefWidth(width);
	}

	/**
	 * carga la pantalla del inventario
	 */
	public void loadInventario() {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/InventarioView.fxml"));
		Pane panel;
		try {
			// agrega los elementos de la pantalla
			panel = loader.load();
			contenido.getChildren().setAll(panel);

			InventarioController controller = loader.getController();
			controller.redimencionar(contenido.getPrefWidth(), contenido.getPrefHeight());
			controller.setParent(contenido);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * establece los iconos de los botones
	 */
	public void setIcon() {
		// boton inventario
		File archivo = new File("icono" + File.separator + "inventario.png");
		Image imagen = new Image(archivo.toURI().toString(), 48, 48, true, true, true);
		btnInventario.setGraphic(new ImageView(imagen));
		btnInventario.setContentDisplay(ContentDisplay.TOP);

		// boton proveedor
		archivo = new File("icono" + File.separator + "proveedor.png");
		imagen = new Image(archivo.toURI().toString(), 48, 48, true, true, true);
		btnProveedores.setGraphic(new ImageView(imagen));
		btnProveedores.setContentDisplay(ContentDisplay.TOP);

		// boton ventas
		archivo = new File("icono" + File.separator + "ventas.png");
		imagen = new Image(archivo.toURI().toString(), 48, 48, true, true, true);
		btnVentas.setGraphic(new ImageView(imagen));
		btnVentas.setContentDisplay(ContentDisplay.TOP);

		// boton compras
		archivo = new File("icono" + File.separator + "compras.png");
		imagen = new Image(archivo.toURI().toString(), 48, 48, true, true, true);
		btnCompras.setGraphic(new ImageView(imagen));
		btnCompras.setContentDisplay(ContentDisplay.TOP);

		// boton caja
		archivo = new File("icono" + File.separator + "caja.png");
		imagen = new Image(archivo.toURI().toString(), 48, 48, true, true, true);
		btnCaja.setGraphic(new ImageView(imagen));
		btnCaja.setContentDisplay(ContentDisplay.TOP);

		// boton caja
		archivo = new File("icono" + File.separator + "ajustes.png");
		imagen = new Image(archivo.toURI().toString(), 48, 48, true, true, true);
		btnAjustes.setGraphic(new ImageView(imagen));
		btnAjustes.setContentDisplay(ContentDisplay.TOP);

		// boton cliente
		archivo = new File("icono" + File.separator + "cliente.png");
		imagen = new Image(archivo.toURI().toString(), 50, 50, true, true, true);
		btnClientes.setGraphic(new ImageView(imagen));
		btnClientes.setContentDisplay(ContentDisplay.TOP);
	}

	/**
	 * establece la imagen de fondo
	 */
	public void setBackground() {
		BackgroundImage fondo = new BackgroundImage(new Image(new File("background/fondo.jpg").toURI().toString()),
				BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT,
				new BackgroundSize(BackgroundSize.AUTO, BackgroundSize.AUTO, false, false, true, true));
		contenido.setBackground(new Background(fondo));
	}
}
