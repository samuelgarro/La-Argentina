package model;

public class Direccion {
	private String ciudad;
	private String barrio;
	private String calle;
	private int numero;
	private String manzana;
	private String monoblock;
	private String departamento;
	
	/**
	 * @param ciudad
	 * @param barrio
	 * @param calle
	 * @param numero
	 * @param manzana
	 * @param monoblock
	 * @param departamento
	 */
	public Direccion(String ciudad, String barrio, String calle, int numero, String manzana, String monoblock,
			String departamento) {
		this.ciudad = ciudad;
		this.barrio = barrio;
		this.calle = calle;
		this.numero = numero;
		this.manzana = manzana;
		this.monoblock = monoblock;
		this.departamento = departamento;
	}
	
	/**
	 * contruye el modelo sin datos
	 */
	public Direccion() {
		this.ciudad = null;
		this.barrio = null;
		this.calle = null;
		this.numero = -1;
		this.manzana = null;
		this.monoblock = null;
		this.departamento = null;
	}

	/**
	 * @return the ciudad
	 */
	public String getCiudad() {
		return ciudad;
	}
	/**
	 * @param ciudad the ciudad to set
	 */
	public void setCiudad(String ciudad) {
		this.ciudad = ciudad;
	}
	/**
	 * @return the barrio
	 */
	public String getBarrio() {
		return barrio;
	}
	/**
	 * @param barrio the barrio to set
	 */
	public void setBarrio(String barrio) {
		this.barrio = barrio;
	}
	/**
	 * @return the calle
	 */
	public String getCalle() {
		return calle;
	}
	/**
	 * @param calle the calle to set
	 */
	public void setCalle(String calle) {
		this.calle = calle;
	}
	/**
	 * @return the numero
	 */
	public int getNumero() {
		return numero;
	}
	/**
	 * @param numero the numero to set
	 */
	public void setNumero(int numero) {
		this.numero = numero;
	}
	/**
	 * @return the manzana
	 */
	public String getManzana() {
		return manzana;
	}
	/**
	 * @param manzana the manzana to set
	 */
	public void setManzana(String manzana) {
		this.manzana = manzana;
	}
	/**
	 * @return the monoblock
	 */
	public String getMonoblock() {
		return monoblock;
	}
	/**
	 * @param monoblock the monoblock to set
	 */
	public void setMonoblock(String monoblock) {
		this.monoblock = monoblock;
	}
	/**
	 * @return the departamento
	 */
	public String getDepartamento() {
		return departamento;
	}
	/**
	 * @param departamento the departamento to set
	 */
	public void setDepartamento(String departamento) {
		this.departamento = departamento;
	}
	
	@Override
	public String toString() {		
		String direccion = (calle == null) ? "" : (calle + (numero > 0  ? (" N° " + numero) : " S/N "));
		direccion += (ciudad == null) ? "" : (", " + ciudad + " ");
		return direccion;
	}
	
}
