package model;

public enum FormaPago {
	EFECTIVO, TARJETA_CREDITO, TARJETA_DEBITO, CHEQUE, TRANSFERENCIA, CUENTA_CORRIENTE;
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		String texto = super.toString();
		return texto.replaceAll("_", " ");
	}
}
