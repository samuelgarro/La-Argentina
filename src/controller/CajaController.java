package controller;

import java.text.DecimalFormat;
import java.time.LocalDate;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import model.Caja;
import model.FormaPago;
import model.TipoMovimiento;
import view.Mensajes;

public class CajaController implements Resisable {

    @FXML
    private ComboBox<FormaPago> tipoCaja;

    @FXML
    private ComboBox<TipoMovimiento> tipoMovimiento;

    @FXML
    private DatePicker desde;

    @FXML
    private DatePicker hasta;

    @FXML
    private TableView<Caja> tabla;

    @FXML
    private TableColumn<Caja, LocalDate> colFecha;

    @FXML
    private TableColumn<Caja, String> colDescripcion;

    @FXML
    private TableColumn<Caja, Double> colMonto;

    @FXML
    private VBox vBox;

    @FXML
    private Label lblIngresos;

    @FXML
    private Label lblGastos;

    @FXML
    private Label lblGanancia;

    @FXML
    private Label lblCantidad;

    @FXML
    public void initialize() {
    	//carga los datos de los combos
        tipoCaja.getItems().setAll(FormaPago.values());
        tipoMovimiento.getItems().setAll(TipoMovimiento.values());
        
        // establece la fecha de hoy en la caja
        desde.setValue(LocalDate.now());
        hasta.setValue(LocalDate.now());
        
        //prepara la tabla
        colFecha.setCellValueFactory(new PropertyValueFactory<Caja, LocalDate>("fecha"));
        colDescripcion.setCellValueFactory(new PropertyValueFactory<Caja, String>("descripcion"));
        colMonto.setCellValueFactory(new PropertyValueFactory<Caja, Double>("monto"));
        
        colMonto.setCellFactory(tc -> new TableCell<Caja, Double>() {
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
        
        //recupera los datos de la caja de hoy
        consultar(null);
    }

	@Override
	public void redimencionar(double width, double height) {
		tabla.setPrefHeight( height - tabla.getLayoutY() - 150);
		vBox.setLayoutY(tabla.getLayoutY() + tabla.getPrefHeight());
	}

	/**
	 * realiza la consulta de la aja para mostrar los datos
	 * @param event
	 */
	@FXML
	private void consultar(ActionEvent event) {
		int indexCaja = tipoCaja.getSelectionModel().getSelectedIndex();
		int indexTipo = tipoMovimiento.getSelectionModel().getSelectedIndex();
		LocalDate desdeFecha = desde.getValue();
		LocalDate hastaFecha = hasta.getValue();
		FormaPago formaPago = null;
		TipoMovimiento tipoMovimiento = null;
		
		
		//verifica si se selecciono o no una caja y un tipo de movimiento
		if(indexCaja >= 0) 
			formaPago = tipoCaja.getItems().get(indexCaja);
			
		
		if(indexTipo >= 0) 
			tipoMovimiento = this.tipoMovimiento.getItems().get(indexTipo);
		
		//verifica si se selecciono una fecha
		if(desdeFecha == null || hastaFecha == null) {
			Mensajes.error("Ambas fechas son obligatorias");
			return;
		} else if (desdeFecha.isAfter(hastaFecha)) {
			Mensajes.error("Invierta las fechas, la fecha de fin no puede ser anterior a la de comienzo");
		}
		
		tabla.getItems().setAll(Caja.getCaja(desdeFecha, hastaFecha, formaPago, tipoMovimiento));
		calcularTotal();
	}
	
	private void calcularTotal() {
		double ingreso = 0, 
				gastos = 0;
		DecimalFormat format = new DecimalFormat("###, ###. ##");
		
		//calcula el total de los movimientos
		for(Caja caja : tabla.getItems()) {
			if(caja.getTipo() == TipoMovimiento.COMPRA || caja.getTipo() == TipoMovimiento.EGRESO)
				gastos += caja.getMonto();
			else if(caja.getFormaPago() != FormaPago.CUENTA_CORRIENTE)
				ingreso += caja.getMonto();
		}
		
		//establece los valores
		lblIngresos.setText("$  " + format.format(ingreso));
		lblGastos.setText("$  " + format.format(gastos));
		lblGanancia.setText(((ingreso - gastos < 0) ? "-" : "") + "$  " + format.format(ingreso - gastos));
		lblCantidad.setText("" + tabla.getItems().size());
	}
}
