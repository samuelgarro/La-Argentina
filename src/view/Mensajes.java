package view;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ButtonBar.ButtonData;

public class Mensajes {
	
	/**
	 * muestra un mensaje de confirmación con el titulo y mensaje especificado
	 * @param mensaje
	 * @param titulo
	 */
	public static void confirmacion(String mensaje, String titulo) {
		Alert alert = new Alert(AlertType.CONFIRMATION);
		ButtonType type = new ButtonType("Ok", ButtonData.OK_DONE);
		alert.setHeaderText(null);
		alert.getDialogPane().getButtonTypes().setAll(type);
		alert.setTitle(titulo);
		alert.setContentText(mensaje);
		alert.showAndWait();
	}
	
	/**
	 * muestra un mensaje de confirmación con el titulo "exito" y mensaje especificado
	 * @param mensaje
	 */
	public static void confirmacion(String mensaje) {
		Alert alert = new Alert(AlertType.CONFIRMATION);
		ButtonType type = new ButtonType("Ok", ButtonData.OK_DONE);
		alert.setHeaderText(null);
		alert.getDialogPane().getButtonTypes().setAll(type);
		alert.setTitle("Exito");
		alert.setContentText(mensaje);
		alert.showAndWait();
	}
	
	/**
	 * muestra un mensaje de confirmación con el titulo "Error" y mensaje especificado
	 * @param mensaje
	 */
	public static void error(String mensaje) {
		Alert alert = new Alert(AlertType.ERROR);
		ButtonType type = new ButtonType("Ok", ButtonData.OK_DONE);
		alert.setHeaderText(null);
		alert.getDialogPane().getButtonTypes().setAll(type);
		alert.setTitle("Error");
		alert.setContentText(mensaje);
		alert.showAndWait();
	}
	
	/**
	 * muestra un mensaje de confirmación con el titulo y mensaje especificado
	 * @param mensaje
	 */
	public static void error(String mensaje, String titulo) {
		Alert alert = new Alert(AlertType.ERROR);
		ButtonType type = new ButtonType("Ok", ButtonData.OK_DONE);
		alert.setHeaderText(null);
		alert.getDialogPane().getButtonTypes().setAll(type);
		alert.setTitle(titulo);
		alert.setContentText(mensaje);
		alert.showAndWait();
	}
	
	
}
