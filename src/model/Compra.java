package model;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import database.DataBase;

public class Compra {
	private int numero;
	private Proveedor proveedor;
	private Producto producto;
	private double monto;
	private double cantidad;
	private LocalDate fecha;

	
	/**
	 * 
	 */
	public Compra() {
		this.numero = -1;
		this.proveedor = null;
		this.producto = null;
		this.monto = -1d;
		this.fecha = null;
	}
	/**
	 * @param proveedor
	 * @param producto
	 * @param monto
	 * @param fecha
	 */
	public Compra(int numero, Proveedor proveedor, Producto producto, double monto, LocalDate fecha) {
		this.numero = numero;
		this.proveedor = proveedor;
		this.producto = producto;
		this.monto = monto;
		this.fecha = fecha;
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
	 * @return the proveedor
	 */
	public Proveedor getProveedor() {
		return proveedor;
	}
	/**
	 * @param proveedor the proveedor to set
	 */
	public void setProveedor(Proveedor proveedor) {
		this.proveedor = proveedor;
	}
	/**
	 * @return the producto
	 */
	public Producto getProducto() {
		return producto;
	}
	/**
	 * @param producto the producto to set
	 */
	public void setProducto(Producto producto) {
		this.producto = producto;
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
	 * @return the fecha
	 */
	public LocalDate getFecha() {
		return fecha;
	}
	/**
	 * @param fecha the fecha to set
	 */
	public void setFecha(LocalDate fecha) {
		this.fecha = fecha;
	}
	/**
	 * @return the cantidad
	 */
	public double getCantidad() {
		return cantidad;
	}
	/**
	 * @param cantidad the cantidad to set
	 */
	public void setCantidad(double cantidad) {
		this.cantidad = cantidad;
	}
	public static boolean insert(Compra[] compras, Proveedor proveedor, FormaPago formaPago) {
		String sql = "INSERT INTO compra (compra_id, compra_fecha, proveedor_id, forma_pago_id, compra_monto) VALUES (NULL,?,?,?,?)";
		DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		DataBase db = DataBase.getInstancia();
		int numero;
		double monto = 0; 
		
		for(Compra compra: compras)
			monto += compra.monto;
		
		db.setTransaction();
		db.setQuery(sql);
		
		db.setParametro(1, format.format(LocalDate.now()));
		db.setParametro(2, proveedor.getId());
		db.setParametro(3, formaPago.ordinal()); // posicion dentro del enum, es igual al id
		db.setParametro(4, monto);
		
		ResultSet rs = db.executeGetKeys();
		
		try {
			if(rs.next()) {
				numero = rs.getInt(1);
				rs.close();
	
				sql = "INSERT INTO compra_producto (compra_id, producto_id, compra_producto_cantidad, compra_producto_monto) "
						+ "VALUES (?,?,?,?)";
				
				for (int i = 1; i < compras.length; i++) 
					sql += ", (?,?,?,?)";
				db.setQuery(sql);
				
				for(int i = 0, indice; i < compras.length; i++) {
					indice = i*4 + 1;
					db.setParametro(indice, numero);
					db.setParametro(1 + indice, compras[i].producto.getCodigo());
					db.setParametro(2 + indice, compras[i].cantidad);
					db.setParametro(3 + indice, compras[i].monto);					
				}
				
				if(db.execute()) 
					db.commit();
				else
					return false;
				
				return true;				
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	
}
