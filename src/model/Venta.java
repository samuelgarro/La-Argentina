package model;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import database.DataBase;

public class Venta {
	private int numero;
	private Producto producto;
	private double precioUnitario;
	private double cantidad;
	private double precioTotal;
	private LocalDate fecha;
	private FormaPago formaPago;
	
	

	/**
	 * @param numero
	 * @param producto
	 * @param precioUnitario
	 * @param cantidad
	 * @param precioTotal
	 * @param fecha
	 */
	public Venta(int numero, Producto producto, double precioUnitario, double cantidad, double precioTotal,
			LocalDate fecha, FormaPago formaPago) {
		this.numero = numero;
		this.producto = producto;
		this.precioUnitario = precioUnitario;
		this.cantidad = cantidad;
		this.precioTotal = precioTotal;
		this.fecha = fecha;
		this.formaPago = formaPago;
	}
	
	

	/**
	 * 
	 */
	public Venta() {
		this.numero = -1;
		this.producto = null;
		this.precioUnitario = -1;
		this.cantidad = -1;
		this.precioTotal = -1;
		this.fecha = null;
		this.formaPago = null;
	}


	
	/**
	 * @return the formaPago
	 */
	public FormaPago getFormaPago() {
		return formaPago;
	}



	/**
	 * @param formaPago the formaPago to set
	 */
	public void setFormaPago(FormaPago formaPago) {
		this.formaPago = formaPago;
	}



	/**
	 * @return the precioTotal
	 */
	public double getPrecioTotal() {
		return precioTotal;
	}



	/**
	 * @param precioTotal the precioTotal to set
	 */
	public void setPrecioTotal(double precioTotal) {
		this.precioTotal = precioTotal;
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
	 * @return the precioUnitario
	 */
	public double getPrecioUnitario() {
		return precioUnitario;
	}

	/**
	 * @param precioUnitario the precioUnitario to set
	 */
	public void setPrecioUnitario(double precioUnitario) {
		this.precioUnitario = precioUnitario;
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
	
	public static boolean insert(Venta[] ventas, FormaPago formaPago, Cliente cliente) {
		String sql = "INSERT INTO venta (venta_id, venta_fecha, forma_pago_id, cliente_documento, venta_monto) VALUES (NULL,?,?,?,?)";
		DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		DataBase db = DataBase.getInstancia();
		int numero;
		double monto = 0;
		
		for(Venta venta : ventas) 
			monto += venta.precioTotal;
		
		db.setTransaction();
		db.setQuery(sql);
		
		db.setParametro(1, format.format(LocalDate.now()));
		db.setParametro(2, formaPago.ordinal()); // posicion del valor dentro del enum
		
		if(cliente == null || cliente.getDocumento() < 999999)
			db.setNull(3, Types.BIGINT);
		else 
			db.setParametro(3, cliente.getDocumento());
		
		db.setParametro(4, monto);
		
		ResultSet rs = db.executeGetKeys();
		
		try {
			if(rs.next()) {
				numero = rs.getInt(1);
				rs.close();
	
				sql = "INSERT INTO venta_producto (venta_id, producto_id, venta_producto_cantidad, venta_producto_monto) "
						+ "VALUES (?,?,?,?)";
				
				for (int i = 1; i < ventas.length; i++) 
					sql += ", (?,?,?,?)";
				db.setQuery(sql);
				
				for(int i = 0, indice; i < ventas.length; i++) {
					indice = i*4 + 1;
					db.setParametro(indice, numero);
					db.setParametro(1 + indice, ventas[i].producto.getCodigo());
					db.setParametro(2 + indice, ventas[i].cantidad);
					db.setParametro(3 + indice, ventas[i].precioTotal);					
				}
				
				if(db.execute()) {
					if(formaPago == FormaPago.CUENTA_CORRIENTE) {
						sql = "INSERT INTO cuenta_corriente VALUES (?,NULL,NULL);";
						db.setQuery(sql);
						
						db.setParametro(1, numero);
						
						if(db.execute())
							return db.commit();
						else
							return false;
						
					} else { // si no es una cuenta corriente
						return db.commit();
					}
				} else {
					return false;
				}				
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}
	
}
