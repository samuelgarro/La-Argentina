package controller;

import java.net.URL;
import java.util.ResourceBundle;

import evento.Validar;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TextField;
import model.Producto;
import model.Rubro;
import model.UnidadMedida;
import view.Mensajes;

public class ProductoController {

	@FXML
	private ResourceBundle resources;

	@FXML
	private URL location;

	@FXML
	private TextField producto;

	@FXML
	private TextField marca;

	@FXML
	private TextField modelo;

	@FXML
	private TextField talle;

	@FXML
	private Spinner<Double> precio;

	@FXML
	private Spinner<Double> precioMayorista;

	@FXML
	private Spinner<Double> cantidad;

	@FXML
	private ComboBox<UnidadMedida> unidad;

	@FXML
	private Button btnGuardar;

	@FXML
	private TextField color;

	@FXML
	private ListView<Rubro> rubrosSeleccionados;

	@FXML
	private ListView<Rubro> rubrosDisponibles;

	@FXML
	private Button btnAgregar;

	@FXML
	private Button btnQuitar;

	@FXML
	private Spinner<Double> stockSegruridad;

	private Producto product;

	@FXML
	public void initialize() {
		//establece los iconos
		FocusedSpinner event = new FocusedSpinner();
		precio.setValueFactory(new SpinnerValueFactory.DoubleSpinnerValueFactory(0, 9999999, 0, 1));
		precio.getEditor().setOnKeyTyped(Validar.numbers());
		event.setSpinner(precio);
		precio.focusedProperty().addListener(event);

		event = new FocusedSpinner();
		precioMayorista.setValueFactory(new SpinnerValueFactory.DoubleSpinnerValueFactory(0, 9999999, 0, 1));
		precioMayorista.getEditor().setOnKeyTyped(Validar.numbers());
		event.setSpinner(precioMayorista);
		precioMayorista.focusedProperty().addListener(event);

		event = new FocusedSpinner();
		cantidad.setValueFactory(new SpinnerValueFactory.DoubleSpinnerValueFactory(0, 9999999, 0, 1));
		cantidad.getEditor().setOnKeyTyped(Validar.numbers());
		event.setSpinner(cantidad);
		cantidad.focusedProperty().addListener(event);
		
		event = new FocusedSpinner();
		stockSegruridad.setValueFactory(new SpinnerValueFactory.DoubleSpinnerValueFactory(0, 9999999, 0, 1));
		stockSegruridad.getEditor().setOnKeyTyped(Validar.numbers());
		event.setSpinner(cantidad);
		stockSegruridad.focusedProperty().addListener(event);

		unidad.getItems().setAll(UnidadMedida.values());
		unidad.getSelectionModel().select(0);

		rubrosDisponibles.getItems().addAll(Rubro.llenatTabla());
		
		// introduce las validaciones
		producto.setOnKeyTyped(Validar.alphanumeric());
		marca.setOnKeyTyped(Validar.alphanumeric());
		modelo.setOnKeyTyped(Validar.alphanumeric());
		talle.setOnKeyTyped(Validar.alphanumeric());
		color.setOnKeyTyped(Validar.texts());
	}

	/**
	 * establece los datos del producto en el formulario
	 * 
	 * @param producto
	 */
	public void cargarDatos(Producto product) {
		this.product = product;

		producto.setText(product.getNombre());
		marca.setText(product.getMarca());
		modelo.setText(product.getModelo());
		talle.setText(product.getTalle());
		color.setText(product.getColor());
		precio.getValueFactory().setValue(product.getPrecioMinorista());
		precioMayorista.getValueFactory().setValue(product.getPrecioMayorista());
		cantidad.getValueFactory().setValue(product.getCantidad());
		unidad.getSelectionModel().select(product.getUnidad());
		stockSegruridad.getValueFactory().setValue(product.getStockSeguridad());
		Rubro[] rubros = product.getRubros();
		rubrosSeleccionados.getItems().addAll(rubros);
		
		for(Rubro rubro: rubros)
			rubrosDisponibles.getItems().remove(rubro);
		
		
	}

	/**
	 * vacia el formulario
	 */
	private void vaciarFormulario() {
		producto.setText(null);
		marca.setText(null);
		modelo.setText(null);
		talle.setText(null);
		color.setText(null);
		precio.getValueFactory().setValue(0d);
		precioMayorista.getValueFactory().setValue(0d);
		cantidad.getValueFactory().setValue(0d);
		stockSegruridad.getValueFactory().setValue(0d);
		unidad.getSelectionModel().select(0);
		ObservableList<Rubro> rubros = rubrosSeleccionados.getItems();
		rubrosDisponibles.getItems().addAll(rubros);
		rubrosSeleccionados.getItems().clear();
		stockSegruridad.getValueFactory().setValue(0d);
	}

	/**
	 * guarda el producto si es un producto nuevo lo registra si es un producto
	 * existente actualiza lo datos
	 * 
	 * @param event
	 */
	@FXML
	private void guardarProducto(ActionEvent event) {
		btnGuardar.setDisable(true);
		boolean nuevo = product == null;

		// saca los datos del formulario
		String nombre = this.producto.getText();
		String marca = this.marca.getText();
		String modelo = this.modelo.getText();
		double cantidad = this.cantidad.getValue();
		double stockSeguridad = this.stockSegruridad.getValue();
		String talle = this.talle.getText();
		double precioMinorista = this.precio.getValue();
		double precioMayorista = this.precioMayorista.getValue();
		String color = this.color.getText();
		UnidadMedida unidad = this.unidad.getSelectionModel().getSelectedItem();
		Rubro[] rubros = new Rubro[rubrosSeleccionados.getItems().size()];
		
		for(int i = 0; i < rubros.length; i++) {
			rubros[i] = rubrosSeleccionados.getItems().get(i);
		}

		// si el producto es nuevo lo crea
		if (nuevo)
			product = new Producto();

		// verifica si los datos fueron correctamente introducidos
		if (nombre == null || nombre.length() == 0) {
			Mensajes.error("Falta el nombre del producto");
			btnGuardar.setDisable(false);
			return;
		} else if (precioMinorista == 0) { // si no se introdujo el precio al publico
			Mensajes.error("El precio al publico no puede ser 0");
			btnGuardar.setDisable(false);
			return;
		} else if (precioMayorista == 0) { // si no se introdujo el precio al por mayor
			Mensajes.error("El precio mayorista no puede ser 0");
			btnGuardar.setDisable(false);
			return;
		}

		// introduce los valores
		product.setNombre(nombre);
		product.setMarca(marca);
		product.setModelo(modelo);
		product.setTalle(talle);
		product.setPrecioMinorista(precioMinorista);
		product.setPrecioMayorista(precioMayorista);
		product.setCantidad(cantidad);
		product.setStockSeguridad(stockSeguridad);
		product.setColor(color);
		product.setUnidad(unidad);
		product.setRubros(rubros);

		if (nuevo) {
			if (product.insert()) {
				Mensajes.confirmacion("Se ha guardado el producto");
				product = null;
				vaciarFormulario();
			} else {
				Mensajes.error("No se ha podido guardar el producto");
				product = null;
			}
		} else {
			if (product.update()) {
				Mensajes.confirmacion("Se ha actualizado el producto");
				product = null;
				vaciarFormulario();
			} else {
				Mensajes.error("No se ha podido actualizar el producto");
			}
		}

		btnGuardar.setDisable(false);
	}
	
	@FXML
	private void agregarRubro(ActionEvent event) {
		Rubro seleccionado = rubrosDisponibles.getSelectionModel().getSelectedItem();
		rubrosSeleccionados.getItems().add(seleccionado);
		rubrosDisponibles.getItems().remove(seleccionado);
	}

	@FXML
	private void quitarRubro(ActionEvent event) {
		Rubro seleccionado = rubrosSeleccionados.getSelectionModel().getSelectedItem();
		rubrosDisponibles.getItems().add(seleccionado);
		rubrosSeleccionados.getItems().remove(seleccionado);
	}
}
