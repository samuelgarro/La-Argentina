package controller;

import java.net.URL;
import java.text.DecimalFormat;
import java.util.ResourceBundle;

import evento.Validar;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import model.Cliente;
import model.FormaPago;
import model.Producto;
import model.Venta;
import search.SearchComboBox;
import view.Mensajes;

public class VentasController implements Resisable {

	@FXML
	private ResourceBundle resources;

	@FXML
	private URL location;

	@FXML
	private ComboBox<FormaPago> formaPago;

	@FXML
	private SearchComboBox<Producto> producto;

	@FXML
	private Spinner<Double> cantidad;

	@FXML
	private TableView<Venta> tabla;

	@FXML
	private TableColumn<Venta, Producto> colProducto;

	@FXML
	private TableColumn<Venta, Double> colCantidad;

	@FXML
	private TableColumn<Venta, Double> colPrecio;

	@FXML
	private TableColumn<Venta, Double> colSubTotal;

	@FXML
	private Button btnRegistrar;

	@FXML
	private HBox boxTotal;

	@FXML
	private Label total;
	
	@FXML
	private Label lblCliente;
	
	@FXML
	private SearchComboBox<Cliente> comboCliente;

	@FXML
	private CheckBox checkMayorista;

	@FXML
	public void initialize() {
		formaPago.getItems().setAll(FormaPago.values());
		formaPago.getSelectionModel().clearSelection();
		
		comboCliente.setItems(Cliente.llenarCombo());

		producto.setItems(Producto.llenarCombo());
		cantidad.setOnKeyTyped(Validar.numbers());
		cantidad.setEditable(true);
		FocusedSpinner event = new FocusedSpinner();
		event.setSpinner(cantidad);
		cantidad.focusedProperty().addListener(event);
		
		// prepara los datos de las columnas
		colPrecio.setCellValueFactory(new PropertyValueFactory<Venta, Double>("precioUnitario"));
		colProducto.setCellValueFactory(new PropertyValueFactory<Venta, Producto>("producto"));
		colCantidad.setCellValueFactory(new PropertyValueFactory<Venta, Double>("cantidad"));
		colSubTotal.setCellValueFactory(new PropertyValueFactory<Venta, Double>("precioTotal"));
		
		
		// visualización de datos
		colPrecio.setCellFactory(tc -> new TableCell<Venta, Double>() {
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
		
		colSubTotal.setCellFactory(tc -> new TableCell<Venta, Double>() {
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
		
		colCantidad.setCellFactory(tc -> new TableCell<Venta, Double>() {
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

	/**
	 * redimenciona los componentes de la pantalla
	 */
	@Override
	public void redimencionar(double width, double height) {
		tabla.setPrefSize(850 , height - tabla.getLayoutY() - 120);

		boxTotal.setLayoutY(tabla.getLayoutY() + tabla.getPrefHeight());
		boxTotal.setLayoutX(tabla.getPrefWidth() + tabla.getLayoutX() - boxTotal.getPrefWidth());

		btnRegistrar.setLayoutY(boxTotal.getLayoutY() + 30);
		btnRegistrar.setLayoutX(850 / 2 - btnRegistrar.getPrefWidth());
		
		// redimencion de columnas
		colProducto.setPrefWidth(400);
		colPrecio.setPrefWidth(150);
		colCantidad.setPrefWidth(150);
		colSubTotal.setPrefWidth(150);
	}
	

	/**
	 * agrega productos a la lista de compra
	 * @param event
	 */
	@FXML
	private void agregarProducto(ActionEvent event) {
		int index = producto.getSelectionModel().getSelectedIndex();
		double cantidad = this.cantidad.getValue();
		double montoTotal;
		Producto producto;
		Venta venta;

		if (index == -1) {
			Mensajes.error("Primero elija el producto");
			return;
		} else if (cantidad == 0) {
			Mensajes.error("Debe introducir la cantidad a vender");
			return;
		}

		producto = this.producto.getItems().get(index);
		cantidad = this.cantidad.getValue();

		if (checkMayorista.isSelected())
			montoTotal = cantidad * producto.getPrecioMayorista();
		else
			montoTotal = cantidad * producto.getPrecioMinorista();

		venta = new Venta();
		venta.setCantidad(cantidad);
		venta.setPrecioTotal(montoTotal);
		venta.setPrecioUnitario(montoTotal / cantidad);
		venta.setProducto(producto);

		tabla.getItems().add(venta);
		calcularTotal();
		
		this.producto.getSelectionModel().clearSelection();
		this.cantidad.getValueFactory().setValue(0d);
	}

	/**
	 * cambia los precios entre mayorista y minorista
	 * @param event
	 */
	@FXML
	private void cambiarPrecio(ActionEvent event) {
		boolean isMayorista = checkMayorista.isSelected();
		double cantidad;
		Producto producto;
		
		for(Venta venta: tabla.getItems()) {
			cantidad = venta.getCantidad();
			producto = venta.getProducto();
			
			if(isMayorista) {
				venta.setPrecioTotal(cantidad * producto.getPrecioMayorista());
				venta.setPrecioUnitario(producto.getPrecioMayorista());
			} else {
				venta.setPrecioTotal(cantidad * producto.getPrecioMinorista());
				venta.setPrecioUnitario(producto.getPrecioMinorista());
			}			
		}
		
		tabla.refresh();
		calcularTotal();
	}

	/**
	 * elimina un item de la tabla
	 * 
	 * @param event
	 */
	@FXML
	private void quitarProducto(ActionEvent event) {
		int index = tabla.getSelectionModel().getSelectedIndex();

		if (index == -1) {
			Mensajes.error("Primero elija el item a eliminar");
			return;
		}

		tabla.getItems().remove(index);
		calcularTotal();
	}

	/**
	 * registra la venta
	 * 
	 * @param event
	 */
	@FXML
	private void registrar(ActionEvent event) {
		int index = formaPago.getSelectionModel().getSelectedIndex();
		Cliente cliente = null;
		FormaPago formaPago;
		
		//verifica si hay productos que se venden y si se elijio la forma de pago
		if(tabla.getItems().size() == 0) {
			Mensajes.error("Primero debe agregar productos");
			return;
		} else if(index < 0) {
			Mensajes.error("Primero elija la forma de pago");
			return;
		}
		
		//se verifica la forma de pago
		formaPago = this.formaPago.getItems().get(index);
		index = this.comboCliente.getSelectionModel().getSelectedIndex();
		
		if(formaPago == FormaPago.CUENTA_CORRIENTE) {
			if(index < 0) {
				Mensajes.error("En cuenta corriente se debe de seleccionar un cliente");
				return;
			} else {
				cliente = this.comboCliente.getItems().get(index);
			}
		} else if(index >= 0) {
			cliente = this.comboCliente.getItems().get(index);
		}
		
		//convierte la tabla a un array de venta
    	Venta[] ventas = new Venta[tabla.getItems().size()];
    	for(int i = 0; i < tabla.getItems().size(); i++)
    		ventas[i] = tabla.getItems().get(i);
		
    	//inserta los datos
    	if(Venta.insert(ventas, formaPago, cliente)) {
    		Mensajes.confirmacion("Se guardo con exito la venta");
    		
    		//limpia la pantalla
    		this.producto.getSelectionModel().clearSelection();
    		this.cantidad.getValueFactory().setValue(0d);
    		this.formaPago.getSelectionModel().clearSelection();
    		this.comboCliente.getSelectionModel().clearSelection();
    		
    		tabla.getItems().clear();
    	} else { // si ocurre un error
    		Mensajes.error("No se ha podido registrar la venta");
    	}
	}

	@FXML
	private void establecerMaximo(ActionEvent event) {
		int index = this.producto.getSelectionModel().getSelectedIndex();
		Producto producto;
		
		if(index == -1)
			return;
		
		producto = this.producto.getItems().get(index);
		
		cantidad.setValueFactory(new SpinnerValueFactory.DoubleSpinnerValueFactory(0, producto.getCantidad(), 0, 1));
	}

	/**
	 * establece el total que se debe de abonar
	 */
	private void calcularTotal() {
		double total = 0;
		DecimalFormat format = new DecimalFormat("###, ###. ##");

		for (Venta venta : tabla.getItems())
			total += venta.getPrecioTotal();

		this.total.setText("$ " + format.format(total));
	}
}
