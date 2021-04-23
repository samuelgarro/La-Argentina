package controller;

import java.net.URL;
import java.util.ResourceBundle;

import database.DataBase;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import model.Producto;
import model.Rubro;
import view.Mensajes;

public class PreciosController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private ComboBox<Rubro> comboRubro;

    @FXML
    private Spinner<Double> porcentajeRubro;

    @FXML
    private ComboBox<Producto> comboProducto;

    @FXML
    private Spinner<Double> porcentajeProducto;

    @FXML
    private Spinner<Double> porcentajeTodo;

    @FXML
	public void initialize() {
		comboProducto.getItems().setAll(Producto.llenarCombo());
		comboRubro.getItems().setAll(Rubro.llenatCombo());

		porcentajeTodo.setValueFactory(new SpinnerValueFactory.DoubleSpinnerValueFactory(-100, 100, 0, 1));
		porcentajeProducto.setValueFactory(new SpinnerValueFactory.DoubleSpinnerValueFactory(-100, 100, 0, 1));
		porcentajeRubro.setValueFactory(new SpinnerValueFactory.DoubleSpinnerValueFactory(-100, 100, 0, 1));
	}

	@FXML
    private void ajustarPrecioProducto(ActionEvent event) {
		double porcentaje = 1 + porcentajeProducto.getValue()/100;
    	String sql = "UPDATE producto SET producto_precio = producto_precio * ?, "
    			+ "producto_precio_mayorista = producto_precio_mayorista * ? "
    			+ "WHERE producto_codigo = ?";
    	
    	int index = comboProducto.getSelectionModel().getSelectedIndex();
		
		if(index < 1) {
			Mensajes.error("Primero elija un producto");
			return;
		}
 
    	Producto producto = comboProducto.getItems().get(index);
    	
    	DataBase db = new DataBase();
    	db.setQuery(sql);
    	
    	db.setParametro(1, porcentaje);
    	db.setParametro(2, porcentaje);
    	db.setParametro(3, producto.getCodigo());
    	
    	if(db.execute()) {
    		Mensajes.confirmacion("Se ha actualizado el precio");
    		comboProducto.getSelectionModel().selectFirst();
    		porcentajeProducto.getValueFactory().setValue(0d);
		} else {
			Mensajes.error("No se ha podido actualizar el precio");
		}
    }

    @FXML
    private void ajustarPrecioRubro(ActionEvent event) {
    	double porcentaje = 1 + porcentajeRubro.getValue()/100;
    	String sql = "UPDATE producto SET producto_precio = producto_precio * ?, "
    			+ "producto_precio_mayorista = producto_precio_mayorista * ? "
    			+ "WHERE producto_codigo IN (SELECT producto_codigo FROM producto_rubro WHERE rubro_codigo = ?)";
    	int index = comboRubro.getSelectionModel().getSelectedIndex();
		
		if(index < 1) {
			Mensajes.error("Primero elija un rubro");
			return;
		}
 
    	Rubro rubro = comboRubro.getItems().get(index);
    	DataBase db = new DataBase();
    	db.setQuery(sql);
    	
    	db.setParametro(1, porcentaje);
    	db.setParametro(2, porcentaje);
    	db.setParametro(3, rubro.getCodigo());
    	
    	if(db.execute()) {
    		Mensajes.confirmacion("Se ha actualizado el precio");
    		comboRubro.getSelectionModel().selectFirst();
    		porcentajeRubro.getValueFactory().setValue(0d);
		} else {
			Mensajes.error("No se ha podido actualizar el precio");
		}
    }

    @FXML
    private void ajustarPrecioTodo(ActionEvent event) {
    	double porcentaje = 1 + porcentajeTodo.getValue()/100;
    	String sql = "UPDATE producto SET producto_precio = producto_precio * ?, "
    			+ "producto_precio_mayorista = producto_precio_mayorista * ? ";
    	
    	DataBase db = new DataBase();
    	db.setQuery(sql);
    	
    	db.setParametro(1, porcentaje);
    	db.setParametro(2, porcentaje);
    	
    	if(db.execute()) {
    		Mensajes.confirmacion("Se ha actualizado el precio");
    		porcentajeTodo.getValueFactory().setValue(0d);
		} else {
			Mensajes.error("No se ha podido actualizar el precio");
		}
    }
}
