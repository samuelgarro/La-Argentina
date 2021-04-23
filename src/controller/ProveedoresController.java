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
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import model.Proveedor;

public class ProveedoresController implements Resisable {

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
	private TableView<Proveedor> tabla;

	@FXML
	private TableColumn<Proveedor, String> colRazonSocial;

	@FXML
	private TableColumn<Proveedor, Long> colTelefono;

	@FXML
	private TableColumn<Proveedor, String> colCorreo;

	@FXML
	private TableColumn<Proveedor, String> colDireccion;

	private Pane parentContenedor;

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
		
		colRazonSocial.setCellValueFactory(new PropertyValueFactory<Proveedor, String>("razonSocial"));
		colTelefono.setCellValueFactory(new PropertyValueFactory<Proveedor, Long>("telefono"));
		colCorreo.setCellValueFactory(new PropertyValueFactory<Proveedor, String>("correo"));
		colDireccion.setCellValueFactory(new PropertyValueFactory<Proveedor, String>("direccion"));
		
		tabla.getItems().setAll(Proveedor.llenarTabla());
	}

	/**
	 * establece el contenedor padre
	 * 
	 * @param parent
	 */
	public void setParent(Pane parent) {
		parentContenedor = parent;
	}

	@Override
	public void redimencionar(double width, double height) {
		tabla.setPrefSize(900, height - 120);

		colRazonSocial.setPrefWidth(250);
		colTelefono.setPrefWidth(150);
		colCorreo.setPrefWidth(250);// 300
		colDireccion.setPrefWidth(250);
		
		double x = (width - 900)/2;
		tabla.setLayoutX(x);
		
		btnAgregar.setLayoutX(x);
		btnEliminar.setLayoutX(x + 80);
		btnEditar.setLayoutX(x + 160);
	}

	@FXML
	private void cambiarPantalla(ActionEvent event) {
		Proveedor proveedor = (Proveedor) tabla.getSelectionModel().getSelectedItem();
	
		if (btnEditar.equals(event.getSource()) && proveedor == null)
			return;
	
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/ProveedorFormView.fxml"));
		try {
			parentContenedor.getChildren().setAll((Pane) loader.load());
	
			if (btnEditar.equals(event.getSource())) {
				ProveedoresAMController controller = loader.getController();
				controller.llenarFormulario(proveedor);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@FXML
	private void eliminarProveedor(ActionEvent event) {
		Proveedor proveedor = tabla.getSelectionModel().getSelectedItem();
		
		if(proveedor == null)
			return ;
		
		if(proveedor.delete()) {
			tabla.getItems().remove(proveedor);
			tabla.refresh();
		}
	}
}
