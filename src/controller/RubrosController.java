package controller;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import model.Rubro;
import view.Mensajes;

public class RubrosController implements Resisable {

	@FXML
	private ResourceBundle resources;

	@FXML
	private URL location;

	@FXML
	private Button btnAgregar;

	@FXML
	private Button btnEliminar;

	@FXML
	private Button btnEditar;

	@FXML
	private TextField rubro;

	@FXML
	private Button btnGuardar;

	@FXML
	private TableView<Rubro> tabla;

	@FXML
	private TableColumn<Rubro, Integer> colCodigo;

	@FXML
	private TableColumn<Rubro, String> colRubro;

	@FXML
	public void initialize() {
		// boton inventario
		File archivo = new File("icono" + File.separator + "agregar.png");
		Image imagen = new Image(archivo.toURI().toString(), 40, 40, true, true, true);
		btnAgregar.setGraphic(new ImageView(imagen));
		btnAgregar.setContentDisplay(ContentDisplay.TOP);

		// boton proveedor
		archivo = new File("icono" + File.separator + "editar.png");
		imagen = new Image(archivo.toURI().toString(), 40, 40, true, true, true);
		btnEditar.setGraphic(new ImageView(imagen));
		btnEditar.setContentDisplay(ContentDisplay.TOP);

		// boton ventas
		archivo = new File("icono" + File.separator + "eliminar.png");
		imagen = new Image(archivo.toURI().toString(), 40, 40, true, true, true);
		btnEliminar.setGraphic(new ImageView(imagen));
		btnEliminar.setContentDisplay(ContentDisplay.TOP);

		colCodigo.setCellValueFactory(new PropertyValueFactory<Rubro, Integer>("codigo"));
		colRubro.setCellValueFactory(new PropertyValueFactory<Rubro, String>("nombre"));

		tabla.getItems().addAll(Rubro.llenatTabla());
		tabla.setOnMouseClicked(e -> cambiarDatos());
	}

	@Override
	public void redimencionar(double width, double height) {

	}

	private void cambiarDatos() {
		btnGuardar.setDisable(true);
		rubro.setDisable(true);
		Rubro rubro = tabla.getSelectionModel().getSelectedItem();

		if (rubro == null)
			return;
		
		this.rubro.setText(rubro.getNombre());
	}

	/**
	 * prepara el boton de guardar para crear un nuevo rubro
	 * 
	 * @param event
	 */
	@FXML
	private void agregarRubro(ActionEvent event) {
		btnGuardar.setDisable(false);
		btnGuardar.setOnAction(this::insertRubro);
		rubro.setDisable(false);
		rubro.setText(null);
	}

	/**
	 * eliina el rubro
	 * 
	 * @param event
	 */
	@FXML
	private void eliminarRubro(ActionEvent event) {
		Rubro rubro = tabla.getSelectionModel().getSelectedItem();

		if (rubro == null)
			return;

		if (rubro.delete())
			tabla.getItems().remove(rubro);
	}

	/**
	 * prepara el boton de guardar para actualizar el rubro
	 * 
	 * @param event
	 */
	@FXML
	private void modificarRubro(ActionEvent event) {
		btnGuardar.setDisable(false);
		btnGuardar.setOnAction(this::updateRubro);
		rubro.setDisable(false);
	}

	@FXML
	private void insertRubro(ActionEvent event) {
		btnGuardar.setDisable(true);
		rubro.setDisable(true);
		String nombre = rubro.getText();
		if (nombre == null || nombre.length() == 0) {
			Mensajes.error("Primero ingrese el nombre del rubro", "Faltan datos");
			btnGuardar.setDisable(false);
			this.rubro.setDisable(false);
		}

		Rubro rubro = new Rubro();
		rubro.setNombre(nombre);

		if (rubro.insert()) {
			Mensajes.confirmacion("Se ha guardado el rubro correctamente");
			tabla.getItems().add(rubro);

		} else {
			Mensajes.error("No se ha podido guardar el rubro");
			btnGuardar.setDisable(false);
			this.rubro.setDisable(false);
		}

	}

	@FXML
	private void updateRubro(ActionEvent event) {
		btnGuardar.setDisable(true);
		this.rubro.setDisable(true);
		String nombre = rubro.getText();
		if (nombre == null || nombre.length() == 0) {
			Mensajes.error("Primero ingrese el nombre del rubro", "Faltan datos");
			btnGuardar.setDisable(false);
			this.rubro.setDisable(false);
		}

		Rubro rubro = tabla.getSelectionModel().getSelectedItem();

		if (rubro == null)
			return;

		rubro.setNombre(nombre);

		if (rubro.update()) {
			Mensajes.confirmacion("Se ha actualizadp el rubro correctamente");
			tabla.refresh();
		} else {
			Mensajes.error("No se ha podido actualizar el rubro");
			btnGuardar.setDisable(false);
			this.rubro.setDisable(false);
		}

	}
}
