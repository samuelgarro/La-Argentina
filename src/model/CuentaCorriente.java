package model;

public class CuentaCorriente {
	private Cliente cliente;
	private double monto;
	
	/**
	 * @return the cliente
	 */
	public Cliente getCliente() {
		return cliente;
	}
	/**
	 * @param cliente the cliente to set
	 */
	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}
	/**
	 * @return the monto
	 */
	public double getMonto() {
		return monto;
	}
	/**
	 * @param monto the monto to set
	 */
	public void setMonto(double monto) {
		this.monto = monto;
	}
	/**
	 * establece un cliente apartir de su documento
	 * @param documento
	 */
	public void setCliente(long documento) {
		cliente = new Cliente(documento);		
	}
	
	
}
