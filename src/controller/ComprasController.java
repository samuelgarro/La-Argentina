package controller;

import java.net.URL;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.util.ResourceBundle;

import evento.Validar;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import model.Compra;
import model.FormaPago;
import model.Producto;
import model.Proveedor;
import search.SearchComboBox;
import view.Mensajes;

public class ComprasController implements Resisable {

	@FXML
	private ResourceBundle resources;

	@FXML
	private URL location;

	@FXML
	private TextField monto;

	@FXML
	private SearchComboBox<Proveedor> proveedor;

	@FXML
	private SearchComboBox<Producto> producto;

	@FXML
	private Spinner<Double> cantidad;

	@FXML
	private Button btnAgregar;

	@FXML
	private Button btnQuitar;

	@FXML
	private TableView<Compra> tabla;

	@FXML
	private TableColumn<Compra, Producto> colProducto;

	@FXML
	private TableColumn<Compra, Double> colCantidad;

	@FXML
	private TableColumn<Compra, Double> colPrecio;

	@FXML
	private Button btnRegistrar;

	@FXML
	private Label lblTotal;

	@FXML
	private HBox boxTotal;
	
	@FXML
	private ComboBox<FormaPago> comboFormaPago;

	@FXML
	public void initialize() {
		comboFormaPago.getItems().setAll(FormaPago.values());
		
		monto.setOnKeyTyped(Validar.numbers());
		monto.focusedProperty().addListener((o, old, newValue) -> {
			String texto = monto.getText();
			if (texto == null)
				return;

			if (old) {
				texto = texto.replaceAll("\\$", "").replace(" ", "");
				int indice = texto.indexOf(",");
				if (indice != -1)
					texto = texto.replace(",", ".");
				else
					indice = texto.lastIndexOf(".");

				if (texto.indexOf(".") != -1 && texto.indexOf(".") != indice && indice != -1)
					texto = texto.substring(0, indice).replace(".", "") + texto.substring(indice);

				DecimalFormat format = new DecimalFormat("###, ###. ##");
				if (!texto.isEmpty()) {
					double valor = Double.parseDouble(texto);
					monto.setText("$ " + format.format(valor).trim());
				}
			} else {
				if (!texto.isEmpty() && texto.indexOf(".") != -1) {
					texto = texto.replace(".", "");
					monto.setText(texto);
				}
			}
		});

		FocusedSpinner event = new FocusedSpinner();
		cantidad.setValueFactory(new SpinnerValueFactory.DoubleSpinnerValueFactory(0, 9999999, 0, 1));
		cantidad.getEditor().setOnKeyTyped(Validar.numbers());
		event.setSpinner(cantidad);
		cantidad.focusedProperty().addListener(event);

		proveedor.setItems(Proveedor.llenarCombo());
		producto.setItems(Producto.llenarCombo());

		// prepara las columnas
		colPrecio.setCellValueFactory(new PropertyValueFactory<Compra, Double>("monto"));
		colProducto.setCellValueFactory(new PropertyValueFactory<Compra, Producto>("producto"));
		colCantidad.setCellValueFactory(new PropertyValueFactory<Compra, Double>("cantidad"));

		colPrecio.setCellFactory(tc -> new TableCell<Compra, Double>() {
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

		colCantidad.setCellFactory(tc -> new TableCell<Compra, Double>() {
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
	}

	@Override
	public void redimencionar(double width, double height) {
		colCantidad.setPrefWidth(200d);
		colPrecio.setPrefWidth(200d);
		colProducto.setPrefWidth(350d);

		tabla.setPrefHeight(height - tabla.getLayoutY() - 100);
		boxTotal.setLayoutY(tabla.getLayoutY() + tabla.getPrefHeight());
		btnRegistrar.setLayoutY(tabla.getLayoutY() + tabla.getPrefHeight() + 40);
	}

	/**
	 * agrega elementos a la tabla
	 * 
	 * @param event
	 */
	@FXML
	private void agregarProducto(ActionEvent event) {
		int indice;
		double cantidad;
		String monto;
		Producto producto;

		// extrae los datos de la pantalla
		indice = this.producto.getSelectionModel().getSelectedIndex();
		if (indice < 1) {
			Mensajes.error("Seleccione primero el producto");
			return;
		}
		producto = this.producto.getItems().get(indice);

		cantidad = this.cantidad.getValue();
		monto = this.monto.getText();
		monto = monto.replace("$", "").trim().replace(".", "").replace(",", ".");

		if (cantidad == 0) {
			Mensajes.error("Falta introducir la cantidad");
			return;
		}

		if (monto.isEmpty()) {
			Mensajes.error("Falta introducir el monto");
			return;
		}

		// genera el modelo de compra
		Compra compra = new Compra();
		compra.setProducto(producto);
		compra.setFecha(LocalDate.now());
		compra.setMonto(Double.parseDouble(monto));
		compra.setCantidad(cantidad);

		tabla.getItems().add(compra);

		calcularTotal();

		// limpia la pantalla
		this.producto.getSelectionModel().clearSelection();
		this.cantidad.getValueFactory().setValue(0d);
		this.monto.setText(null);
	}

	/**
	 * quita los elementos de la tabla
	 * 
	 * @param event
	 */
	@FXML
	private void quitarProducto(ActionEvent event) {
		Compra compra = tabla.getSelectionModel().getSelectedItem();

		if (compra == null)
			return;

		tabla.getItems().remove(compra);

		calcularTotal();
	}

	/**
	 * registra la compra en la base de datos
	 * @param event
	 */
	@FXML
	private void registrar(ActionEvent event) {
		int indice = proveedor.getSelectionModel().getSelectedIndex();
		Proveedor proveedor;
		FormaPago formaPago;

		// verifica si el proveedor fue elegido
		if (indice < 1) {
			Mensajes.error("Primero debe seleccionar el proveedor");
			return;
		} else if (tabla.getItems().size() == 0) {
			Mensajes.error("Primero debe agregar productos");
			return;
		} else if(comboFormaPago.getSelectionModel().getSelectedIndex() < 0) {
			Mensajes.error("Primero debe selccionar una forma de pago");
			return;
		}
		
		proveedor = this.proveedor.getItems().get(indice);
		indice = comboFormaPago.getSelectionModel().getSelectedIndex();
		formaPago = comboFormaPago.getItems().get(indice);
		
		// convierte la tabla a un array de compra
		Compra[] compras = new Compra[tabla.getItems().size()];
		for (int i = 0; i < tabla.getItems().size(); i++)
			compras[i] = tabla.getItems().get(i);

		// registra la compra
		if (Compra.insert(compras, proveedor, formaPago)) {
			Mensajes.confirmacion("Se ha guardado correctamente");

			// vacia el formulario
			this.proveedor.getSelectionModel().clearSelection();
			this.producto.getSelectionModel().clearSelection();
			this.cantidad.getValueFactory().setValue(0d);
			this.monto.setText(null);
			this.tabla.getItems().clear();
			this.comboFormaPago.getSelectionModel().clearSelection();
		} else {
			Mensajes.error("No se pudo guardar la compra");
		}

	}

	/**
	 * calcula el total a pagar
	 */
	private void calcularTotal() {
		double total = 0;
		DecimalFormat format = new DecimalFormat("###, ###. ##");

		for (Compra compra : tabla.getItems()) {
			total += compra.getMonto();
		}

		lblTotal.setText("$ " + format.format(total).trim());
	}

}
