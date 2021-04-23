package controller;

import java.net.URL;
import java.util.ResourceBundle;

import evento.Validar;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import model.Cliente;
import model.Direccion;
import view.Mensajes;

public class AltaClienteController {

	@FXML
	private ResourceBundle resources;

	@FXML
	private URL location;

	@FXML
	private TextField documento;

	@FXML
	private TextField nombre;

	@FXML
	private TextField apellido;

	@FXML
	private TextField telefono;

	@FXML
	private TextField correo;

	@FXML
	private TextField ciudad;

	@FXML
	private TextField barrio;

	@FXML
	private TextField calle;

	@FXML
	private TextField numero;

	@FXML
	private TextField manzana;

	@FXML
	private TextField monoblock;

	@FXML
	private TextField departamento;

	@FXML
	private Button btnGuardar;
	
	private Cliente clienteOld;

	@FXML
	public void initialize() {
		// limita el ingreso de caracteres
		documento.setOnKeyTyped(Validar.numbers());
		nombre.setOnKeyTyped(Validar.texts());
		apellido.setOnKeyTyped(Validar.texts());
		telefono.setOnKeyTyped(Validar.numbers());
		correo.setOnKeyTyped(Validar.email());
		ciudad.setOnKeyTyped(Validar.alphanumeric());
		barrio.setOnKeyTyped(Validar.alphanumeric());
		calle.setOnKeyTyped(Validar.alphanumeric());
		numero.setOnKeyTyped(Validar.numbers());
		manzana.setOnKeyTyped(Validar.alphanumeric());
		monoblock.setOnKeyTyped(Validar.alphanumeric());
		departamento.setOnKeyTyped(Validar.alphanumeric());

	}

	/**
	 * establece la pantalla para modificar al cliente
	 * 
	 * @param cliente
	 */
	public void setUpdate(Cliente cliente) {
		clienteOld = cliente;
		// carga los datos en pantalla
		this.documento.setText("" + cliente.getDocumento());
		this.nombre.setText(cliente.getNombre());
		this.apellido.setText(cliente.getApellido());
		this.telefono.setText((cliente.getTelefono() == 0) ? null : (cliente.getTelefono() + ""));
		this.correo.setText(cliente.getCorreo());
		this.ciudad.setText(cliente.getDireccion().getCiudad());
		this.barrio.setText(cliente.getDireccion().getBarrio());
		this.calle.setText(cliente.getDireccion().getBarrio());
		this.numero.setText("" + cliente.getDireccion().getNumero());
		this.manzana.setText(cliente.getDireccion().getManzana());
		this.monoblock.setText(cliente.getDireccion().getMonoblock());
		this.departamento.setText(cliente.getDireccion().getDepartamento());
		// cambia el evento
		btnGuardar.setOnAction(this::update);
	}

	/**
	 * prepara los datos y manda a actualizar
	 * 
	 * @param event
	 */
	private void update(ActionEvent event) {
		// extrae los datos
		long documento = Validar.parseLong(this.documento.getText());
		String nombre = this.nombre.getText();
		String apellido = this.apellido.getText();
		long telefono = Validar.numberPhone(this.telefono.getText());
		String correo = Validar.emailAddress(this.correo.getText());
		String ciudad = this.ciudad.getText();
		String barrio = this.barrio.getText();
		String calle = this.calle.getText();
		int numero = Validar.parseInt(this.numero.getText());
		String manzana = this.manzana.getText();
		String monoblock = this.monoblock.getText();
		String departamento = this.departamento.getText();
		Direccion direccion;
		Cliente cliente;
	
		// verifica los datos obligatorios
		if (documento == -1L) {
			Mensajes.error("Introdusca primero un número de documento");
			return;
		} else if (nombre == null || nombre.length() == 0) {
			Mensajes.error("Primero introduzca un nombre");
			return;
		}
		// crea los modelos
		apellido = (apellido == null || apellido.length() == 0) ? null : apellido;
		ciudad = (ciudad == null || ciudad.length() == 0) ? null : ciudad;
		barrio = (barrio == null || barrio.length() == 0) ? null : barrio;
		calle = (calle == null || calle.length() == 0) ? null : calle;
		manzana = (manzana == null || manzana.length() == 0) ? null : manzana;
		monoblock = (monoblock == null || monoblock.length() == 0) ? null : monoblock;
		departamento = (departamento == null || departamento.length() == 0) ? null : departamento;
	
		direccion = new Direccion(ciudad, barrio, calle, numero, manzana, monoblock, departamento);
		cliente = new Cliente(documento, nombre, apellido, telefono, correo, direccion);
	
		// intenta dar de alta
		if (cliente.update(clienteOld.getDocumento())) { // si sale todo bien
			Mensajes.confirmacion("El cliente se guardo con exito");
			limpiarFormulario();
			//establece que para los proximos solo se creen
			btnGuardar.setOnAction(this::guardar);
		} else { // si falla
			Mensajes.error("No se ha podido registrar el cliente");
		}
	}

	/**
	 * da de alta a un nuevo cliente
	 * 
	 * @param event
	 */
	@FXML
	private void guardar(ActionEvent event) {
		// extrae los datos
		long documento = Validar.parseLong(this.documento.getText());
		String nombre = this.nombre.getText();
		String apellido = this.apellido.getText();
		long telefono = Validar.numberPhone(this.telefono.getText());
		String correo = Validar.emailAddress(this.correo.getText());
		String ciudad = this.ciudad.getText();
		String barrio = this.barrio.getText();
		String calle = this.calle.getText();
		int numero = Validar.parseInt(this.numero.getText());
		String manzana = this.manzana.getText();
		String monoblock = this.monoblock.getText();
		String departamento = this.departamento.getText();
		Direccion direccion;
		Cliente cliente;

		// verifica los datos obligatorios
		if (documento == -1L) {
			Mensajes.error("Introdusca primero un número de documento");
			return;
		} else if (nombre == null || nombre.length() == 0) {
			Mensajes.error("Primero introduzca un nombre");
			return;
		}
		// crea los modelos
		apellido = (apellido == null || apellido.length() == 0) ? null : apellido;
		ciudad = (ciudad == null || ciudad.length() == 0) ? null : ciudad;
		barrio = (barrio == null || barrio.length() == 0) ? null : barrio;
		calle = (calle == null || calle.length() == 0) ? null : calle;
		manzana = (manzana == null || manzana.length() == 0) ? null : manzana;
		monoblock = (monoblock == null || monoblock.length() == 0) ? null : monoblock;
		departamento = (departamento == null || departamento.length() == 0) ? null : departamento;

		direccion = new Direccion(ciudad, barrio, calle, numero, manzana, monoblock, departamento);
		cliente = new Cliente(documento, nombre, apellido, telefono, correo, direccion);

		// intenta dar de alta
		if (cliente.insert()) { // si sale todo bien
			Mensajes.confirmacion("El cliente se guardo con exito");
			limpiarFormulario();
		} else { // si falla
			Mensajes.error("No se ha podido registrar el cliente");
		}
	}

	/**
	 * limpia el formulario
	 */
	private void limpiarFormulario() {
		this.documento.setText(null);
		this.nombre.setText(null);
		this.apellido.setText(null);
		this.telefono.setText(null);
		this.correo.setText(null);
		this.ciudad.setText(null);
		this.barrio.setText(null);
		this.calle.setText(null);
		this.numero.setText(null);
		this.manzana.setText(null);
		this.monoblock.setText(null);
		this.departamento.setText(null);
	}
}
