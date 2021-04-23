package controller;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.ResourceBundle;

import database.DataBase;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.Cliente;
import model.CuentaCorriente;
import model.FormaPago;
import model.MovimientoCliente;
import view.Mensajes;

public class CtaCteController {

	@FXML
	private ResourceBundle resources;

	@FXML
	private URL location;

	@FXML
	private TableView<CuentaCorriente> tablaCtaCte;

	@FXML
	private TableColumn<CuentaCorriente, Cliente> colCliente;

	@FXML
	private TableColumn<CuentaCorriente, Double> colMontoCtaCte;

	@FXML
	private TableView<MovimientoCliente> tablaHistorial;

	@FXML
	private TableColumn<MovimientoCliente, LocalDate> colFecha;

	@FXML
	private TableColumn<MovimientoCliente, Double> colMontoMovimiento;

	@FXML
	private TableColumn<MovimientoCliente, String> colOperación;

	@FXML
	private Label lblCliente;

	@FXML
	private Label lblDireccion;

	@FXML
	private Button btnEditar;

	@FXML
	private Button btnAsentarPago;

	private Pane parent;
	
	@FXML
	public void initialize() {
		// prepara las tablas
		colCliente.setCellValueFactory(new PropertyValueFactory<CuentaCorriente, Cliente>("cliente"));
		colMontoCtaCte.setCellValueFactory(new PropertyValueFactory<CuentaCorriente, Double>("monto"));

		colFecha.setCellValueFactory(new PropertyValueFactory<MovimientoCliente, LocalDate>("fecha"));
		colMontoMovimiento.setCellValueFactory(new PropertyValueFactory<MovimientoCliente, Double>("monto"));
		colOperación.setCellValueFactory(new PropertyValueFactory<MovimientoCliente, String>("operacion"));

		colMontoCtaCte.setCellFactory(tc -> new TableCell<CuentaCorriente, Double>() {
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

		colMontoMovimiento.setCellFactory(tc -> new TableCell<MovimientoCliente, Double>() {
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

		// carga los datos a la tabla
		String sql = "SELECT sum(venta_monto), cliente_documento FROM venta where forma_pago_id = ? "
				+ "AND venta_id NOT IN (SELECT venta_id FROM cuenta_corriente WHERE NOT cta_cte_fecha_pago ISNULL) "
				+ "GROUP BY cliente_documento;";

		DataBase db = new DataBase();
		db.setQuery(sql);
		db.setParametro(1, FormaPago.CUENTA_CORRIENTE.ordinal());

		ResultSet rs = db.executeQuery();

		try {
			ArrayList<CuentaCorriente> datos = new ArrayList<CuentaCorriente>();
			CuentaCorriente cuenta;
			while (rs.next()) {
				cuenta = new CuentaCorriente();
				cuenta.setMonto(rs.getDouble(1));
				cuenta.setCliente(rs.getLong(2));

				datos.add(cuenta);
			}
			tablaCtaCte.getItems().setAll(datos);
		} catch (SQLException e) {
			e.printStackTrace();
		}

		tablaCtaCte.setOnMouseClicked(e -> cargarHistorial());
		//btnAsentarPago.setDisable(true);
		setIcons();
	}
	
	/**
	 * establece el contenedor padre
	 * @param parent
	 */
	public void setParent(Pane parent) {
		this.parent = parent;
	}

	/**
	 * asienta un pago
	 * @param event
	 */
	@FXML
	private void asentarPago(ActionEvent event) {
		int index = tablaHistorial.getSelectionModel().getSelectedIndex();
		String sql = "UPDATE cuenta_corriente SET cta_cte_fecha_pago = ?, forma_pago_id = ? WHERE venta_id = ?";
		DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		DataBase db;
		CuentaCorriente cuenta;
		MovimientoCliente movimiento;
		FormaPago formaPago = null;
		
		if(index < 0) // sale si no se selecciono un movimiento 
			return ;
		
		movimiento = tablaHistorial.getItems().get(index);
		index = tablaCtaCte.getSelectionModel().getSelectedIndex();
		
		if(index < 0) // sale si no se selecciono una cuenta 
			return ;
		
		cuenta = tablaCtaCte.getItems().get(index);
		
		//verifica que no se haya pagado antes
		for(MovimientoCliente mov: tablaHistorial.getItems()) {
			if(mov.getIdVenta() == movimiento.getIdVenta()) {
				if(mov.getOperacion().equals("Pago"))
					return;
			}
		}
		
		// consulta la forma de pago
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/viewClientes/FormaDePagoView.fxml"));
			Scene scene = new Scene((Pane)loader.load());
			FormaPagoController controller = loader.getController();
			
			Stage stage = new Stage();
			stage.setTitle("Forma de Pago");
			stage.setScene(scene);
			stage.setResizable(false);
			stage.setMaximized(false);
			stage.initModality(Modality.APPLICATION_MODAL);
			stage.showAndWait();

			formaPago = controller.getSelection();
			if(formaPago == null)
				return;
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		// inserta el pago
		db = DataBase.getInstancia();
		db.setTransaction();
		db.setQuery(sql);
		
		db.setParametro(1, format.format(LocalDate.now()));
		db.setParametro(2, formaPago.ordinal());
		db.setParametro(3, movimiento.getIdVenta());
		
		if(db.execute()) {
			// si sale bien inserto en caja
			sql = "INSERT INTO caja (caja_id, caja_fecha, caja_monto, caja_tipo_movimiento, forma_pago_id, caja_clave_foranea) VALUES (NULL,?,?,3,?,?);";
			db.setQuery(sql);
			db.setParametro(1, format.format(LocalDate.now()));
			db.setParametro(2, movimiento.getMonto());
			db.setParametro(3, formaPago.ordinal());
			db.setParametro(4, movimiento.getIdVenta());
			
			if(db.execute()) {
				//hago el commit
				if(db.commit()) {
					Mensajes.confirmacion("Se ha registrado correctamente");
					cuenta.setMonto(cuenta.getMonto() - movimiento.getMonto());
					tablaCtaCte.refresh();
					cargarHistorial();
				} else {
					Mensajes.error("No se ha podido registrar");
				}
			} else {
				Mensajes.error("No se ha podido registrar");
			}
		} else {
			Mensajes.error("No se ha podido registrar");
		}
	}

	/**
	 * carga el historial de movimiento y muestra los datos del cliente
	 */
	@FXML
	private void cargarHistorial() {
		// muestra por pantalla los datos del cliente
		int index = tablaCtaCte.getSelectionModel().getSelectedIndex();
		Cliente cliente;

		if (index < 0)
			return;

		cliente = tablaCtaCte.getItems().get(index).getCliente();

		lblCliente.setText(cliente.toString());
		lblDireccion.setText(cliente.getDireccion().toString());

		// carga el historial de pagos y compras
		// carga los datos a la tabla
		String sql = "SELECT venta_monto, venta_fecha, 'Compra', venta_id " 
				+ "FROM venta " 
				+ "WHERE cliente_documento = ? AND forma_pago_id = ?"
				+ "UNION " 
				+ "SELECT c.caja_monto, cta.cta_cte_fecha_pago, 'Pago', v.venta_id " 
				+ "FROM cuenta_corriente cta "
				+ "INNER JOIN venta v " 
				+ "ON v.venta_id = cta.venta_id " 
				+ "INNER JOIN caja c "
				+ "ON c.caja_clave_foranea = cta.venta_id " 
				+ "WHERE NOT cta_cte_fecha_pago ISNULL AND v.cliente_documento = ?";

		DataBase db = new DataBase();
		db.setQuery(sql);
		db.setParametro(1, cliente.getDocumento());
		db.setParametro(2, FormaPago.CUENTA_CORRIENTE.ordinal());
		db.setParametro(3, cliente.getDocumento());

		ResultSet rs = db.executeQuery();

		try {
			ArrayList<MovimientoCliente> datos = new ArrayList<MovimientoCliente>();
			MovimientoCliente movimiento;
			while (rs.next()) {
				movimiento = new MovimientoCliente();
				movimiento.setMonto(rs.getDouble(1));
				movimiento.setFecha(LocalDate.parse(rs.getString(2)));
				movimiento.setOperacion(rs.getString(3));
				movimiento.setIdVenta(rs.getInt(4));

				datos.add(movimiento);
			}
			tablaHistorial.getItems().setAll(datos);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * carga la pantalla de modificacion de cliente
	 * @param event
	 */
	@FXML
	private void editarCliente(ActionEvent event) {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/viewClientes/AltaView.fxml"));
		Cliente cliente;
		int i = tablaCtaCte.getSelectionModel().getSelectedIndex();
		if(i < 0)
			return;
		
		cliente = tablaCtaCte.getSelectionModel().getSelectedItem().getCliente();
		try {
			parent.getChildren().setAll((Pane) loader.load());
			AltaClienteController controller = loader.getController();
			
			controller.setUpdate(cliente);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * establece los iconos
	 */
	private void setIcons() {
		// boton editar
		File archivo = new File("icono" + File.separator + "editar.png");
		Image imagen = new Image(archivo.toURI().toString(), 48, 48, true, true, true);
		btnEditar.setGraphic(new ImageView(imagen));
		btnEditar.setContentDisplay(ContentDisplay.TOP);
		
		// boton asentar pago
		archivo = new File("icono" + File.separator + "pago.png");
		imagen = new Image(archivo.toURI().toString(), 48, 48, true, true, true);
		btnAsentarPago.setGraphic(new ImageView(imagen));
		btnAsentarPago.setContentDisplay(ContentDisplay.TOP);
	}
}
