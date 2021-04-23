package controller;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import model.Producto;
import model.UnidadMedida;

public class InventarioController implements Resisable {

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
	private TableView<Producto> tabla;

	@FXML
	private TableColumn<Producto, String> colProducto;

	@FXML
	private TableColumn<Producto, String> colModelo;

	@FXML
	private TableColumn<Producto, String> colMarca;

	@FXML
	private TableColumn<Producto, Double> colCantidad;

	@FXML
	private TableColumn<Producto, UnidadMedida> colUnidad;

	@FXML
	private TableColumn<Producto, Double> colMayorista;

	@FXML
	private TableColumn<Producto, Double> colMinorista;

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

		// prepara la tabla
		colProducto.setCellValueFactory(new PropertyValueFactory<Producto, String>("nombre"));
		colModelo.setCellValueFactory(new PropertyValueFactory<Producto, String>("modelo"));
		colMarca.setCellValueFactory(new PropertyValueFactory<Producto, String>("marca"));
		colMayorista.setCellValueFactory(new PropertyValueFactory<Producto, Double>("precioMayorista"));
		colMinorista.setCellValueFactory(new PropertyValueFactory<Producto, Double>("precioMinorista"));
		colCantidad.setCellValueFactory(new PropertyValueFactory<Producto, Double>("cantidad"));
		colUnidad.setCellValueFactory(new PropertyValueFactory<Producto, UnidadMedida>("unidad"));
		
		colMayorista.setCellFactory(tc -> new TableCell<Producto, Double>() {
			@Override
			protected void updateItem(Double price, boolean empty) {
				super.updateItem(price, empty);
				if (empty) {
					setText(null);
				} else {
					setText("$ " + new DecimalFormat("###, ###. ##").format(price));
				}
			}
		});
		
		colMinorista.setCellFactory(tc -> new TableCell<Producto, Double>() {
			@Override
			protected void updateItem(Double price, boolean empty) {
				super.updateItem(price, empty);
				if (empty) {
					setText(null);
				} else {
					setText("$ " + new DecimalFormat("###, ###. ##").format(price));
				}
			}
		});

		colCantidad.setCellFactory(tc -> new TableCell<Producto, Double>() {
			@Override
			protected void updateItem(Double price, boolean empty) {
				super.updateItem(price, empty);
				if (empty) {
					setText(null);
				} else {
					setText(new DecimalFormat("###, ###. ##").format(price));
				}
			}
		});

		tabla.getItems().setAll(Producto.llenarTabla());
	}

	@Override
	public void redimencionar(double width, double height) {
		tabla.setPrefSize(width - 40, height - 120);

		colModelo.setPrefWidth(100);
		colMarca.setPrefWidth(100);
		colCantidad.setPrefWidth(100);// 300
		colUnidad.setPrefWidth(100);
		colMayorista.setPrefWidth(120);
		colMinorista.setPrefWidth(120); // 640
		colProducto.setPrefWidth(tabla.getPrefWidth() - 640);
	}

	/**
	 * establece el contenedor padre
	 * 
	 * @param parent
	 */
	public void setParent(Pane parent) {
		parentContenedor = parent;
	}

	/**
	 * cambia la pantalla del inventario por la de ABM de producto
	 * 
	 * @param event
	 */
	@FXML
	private void cambiarPantalla(ActionEvent event) {
		Producto product = (Producto) tabla.getSelectionModel().getSelectedItem();

		if (btnEditar.equals(event.getSource()) && product == null)
			return;

		FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/ProductoView.fxml"));
		try {
			parentContenedor.getChildren().setAll((Pane) loader.load());

			if (btnEditar.equals(event.getSource())) {
				ProductoController controller = loader.getController();
				controller.cargarDatos(product);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@FXML
	private void eliminarProducto(ActionEvent event) {
		Producto product = (Producto) tabla.getSelectionModel().getSelectedItem();

		if (product == null)
			return;

		if (product.delete()) {
			tabla.getItems().remove(product);
		} else {
			Alert alert = new Alert(Alert.AlertType.ERROR);
			ButtonType type = new ButtonType("Ok", ButtonData.OK_DONE);
			alert.setHeaderText(null);
			alert.getDialogPane().getButtonTypes().setAll(type);
			alert.setTitle("Error");
			alert.setContentText("No se ha podido eliminar el producto");
			alert.showAndWait();
		}
	}

}
