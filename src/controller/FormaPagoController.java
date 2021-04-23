package controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.stage.Stage;
import model.FormaPago;

public class FormaPagoController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private ComboBox<FormaPago> comboFormaPago;

    @FXML
    private Button btnConfirmar;

    @FXML
    private Button btnCancelar;
    
    private FormaPago formaPago;

    @FXML
	public void initialize() {
	    comboFormaPago.getItems().setAll(FormaPago.values());
	    comboFormaPago.getItems().remove(FormaPago.CUENTA_CORRIENTE);
	    comboFormaPago.getSelectionModel().selectFirst();
	}
    
    /**
     * devuelve la forma de pago seleccionada
     * @return
     */
    public FormaPago getSelection() {
    	return formaPago;
    }

    /**
     * establece el valor a devolver si se cancela
     * @param event
     */
	@FXML
    private void cancelar(ActionEvent event) {
		formaPago = null;
		
		Stage stage = (Stage) btnCancelar.getParent().getScene().getWindow();
		stage.close();
    }

	/**
	 * establece
	 * @param event
	 */
    @FXML
    private void confirmar(ActionEvent event) {
    	int index = comboFormaPago.getSelectionModel().getSelectedIndex();
    	
    	if(index < 0) 
    		return ;
    	
    	formaPago = comboFormaPago.getItems().get(index);
    	
    	Stage stage = (Stage) btnConfirmar.getParent().getScene().getWindow();
		stage.close();
    }
  
}
