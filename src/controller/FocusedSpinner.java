package controller;

import java.text.DecimalFormat;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.Spinner;

public class FocusedSpinner implements ChangeListener<Boolean> {
	private Spinner<Double> spinner;

	@Override
	public void changed(ObservableValue<? extends Boolean> o, Boolean oldValue, Boolean newValue) {
		double valor;
		String numero;
		DecimalFormat format;
		// si new value es true entonces salgo, porque ganó el foco
		if (newValue)
			return;

		// saca el valor del editor
		numero = spinner.getEditor().getText().trim();
		numero = numero.replaceAll(",", ".");
		valor = Double.parseDouble(numero);

		// establece el valor a mostrar
		format = new DecimalFormat("###, ###. ##");
		spinner.getEditor().setText(format.format(valor)); // valor que se muestra
		spinner.getValueFactory().setValue(valor);
	}

	/**
	 * establece el spinner que se controla
	 * 
	 * @param spinner
	 */
	public void setSpinner(Spinner<Double> spinner) {
		this.spinner = spinner;
	}
}