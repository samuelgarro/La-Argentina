package controller;

import java.net.URL;
import java.util.ResourceBundle;

import evento.Validar;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import model.Proveedor;
import view.Mensajes;

public class ProveedoresAMController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private TextField txtRazonSocial;

    @FXML
    private TextField txtTelefono;

    @FXML
    private TextField txtCorreo;

    @FXML
    private TextField txtDireccion;

    @FXML
    private Button btnGuardar;
    
    private Proveedor proveedor;

    @FXML
    public void initialize() {
    	txtRazonSocial.setOnKeyTyped(Validar.alphanumeric());
    	txtCorreo.setOnKeyTyped(Validar.email());
    	txtTelefono.setOnKeyTyped(Validar.numbers());
    	txtDireccion.setOnKeyTyped(Validar.alphanumeric());
    }
    
    public void llenarFormulario(Proveedor proveedor) {
    	txtCorreo.setText(proveedor.getCorreo());
		txtDireccion.setText(proveedor.getDireccion());
		txtTelefono.setText((proveedor.getTelefono() == -1L ? null : proveedor.getTelefono() + "")  );
		txtRazonSocial.setText(proveedor.getRazonSocial());		
		
		this.proveedor = proveedor;
    }

	@FXML
	private void registrar(ActionEvent event) {
		btnGuardar.setDisable(true);
		boolean nuevo = proveedor == null;
	
		String razonSocial = txtRazonSocial.getText();
		String direccion = txtDireccion.getText();
		String correo = txtCorreo.getText();
		long telefono = -1L;
		
		if(txtTelefono.getText() != null && txtTelefono.getText().length() > 0) {
			telefono = Validar.numberPhone(txtTelefono.getText());
			if(telefono == -1L) {
				Mensajes.error("El número de telefono introducido no es valido");
				btnGuardar.setDisable(false);
				return;
			}
		}
		
		if(correo != null && correo.length() > 0) {
			correo = Validar.emailAddress(correo); 
			if(correo == null) {
				Mensajes.error("La dirección de correo introducido no es valido");
				btnGuardar.setDisable(false);
				return;
			}			
		}		
	
		// si el producto es nuevo lo crea
		if (nuevo)
			proveedor = new Proveedor();
	
		// verifica si los datos fueron correctamente introducidos
		if (razonSocial == null || razonSocial.length() == 0) {
			Mensajes.error("Ingrese la razón social primero");
			btnGuardar.setDisable(false);
			return;
		}
	
		// introduce los valores
		proveedor.setRazonSocial(razonSocial);
		proveedor.setCorreo(correo);
		proveedor.setDireccion(direccion);
		proveedor.setTelefono(telefono);
	
		if (nuevo) {
			if (proveedor.insert()) {
				Mensajes.confirmacion("Se ha guardado el producto");
				proveedor = null;
				vaciarFormulario();
			} else {
				Mensajes.error("No se ha podido guardar el producto");
				proveedor = null;
			}
		} else {
			if (proveedor.update()) {
				Mensajes.confirmacion("Se ha actualizado el producto");
				proveedor = null;
				vaciarFormulario();
			} else {
				Mensajes.error("No se ha podido actualizar el producto");
			}
		}
	
		btnGuardar.setDisable(false);
	}

	/**
	 * vacia el formulario
	 */
	private void vaciarFormulario() {
		txtCorreo.setText(null);
		txtDireccion.setText(null);
		txtTelefono.setText(null);
		txtRazonSocial.setText(null);		
	}
}
