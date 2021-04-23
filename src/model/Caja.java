package model;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import database.DataBase;

public class Caja {
	private int id;
	private double monto;
	private String descripcion;
	private LocalDate fecha;
	private TipoMovimiento tipo;
	private FormaPago formaPago;
	
	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}
	/**
	 * @param id the id to set
	 */
	public void setId(int id) {
		this.id = id;
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
	 * @return the descripcion
	 */
	public String getDescripcion() {
		return descripcion;
	}
	/**
	 * @param descripcion the descripcion to set
	 */
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
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
	 * @return the tipo
	 */
	public TipoMovimiento getTipo() {
		return tipo;
	}
	/**
	 * @param tipo the tipo to set
	 */
	public void setTipo(TipoMovimiento tipo) {
		this.tipo = tipo;
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
	 * crea un array de caja 
	 * @param desde
	 * @param hasta
	 * @param caja
	 * @param tipo
	 * @return
	 */
	public static Caja[] getCaja(LocalDate desde, LocalDate hasta, FormaPago formaPago, TipoMovimiento tipo) {
		int i = 3;
		ArrayList<Caja> retorno = new ArrayList<Caja>();
		String sql = "SELECT * FROM caja WHERE date(caja_fecha) BETWEEN date(?) AND date(?) ";
		DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		DataBase db = new DataBase();
		ResultSet rs;
		Caja caja;
		
		//prepara la consulta
		if(formaPago != null)
			sql += "AND forma_pago_id = ? ";
		
		if(tipo != null)
			sql += "AND caja_tipo_movimiento = ?";
		
		//agrega los parametros
		db.setQuery(sql);
		db.setParametro(1, format.format(desde));
		db.setParametro(2, format.format(hasta));
		
		if(formaPago != null) 
			db.setParametro(i++, formaPago.ordinal());		
		
		if(tipo != null)
			db.setParametro(i, tipo.ordinal());
		
		//obtiene los resultados
		rs = db.executeQuery();
		try {
			while(rs.next()) {
				caja = new Caja();
				caja.id = rs.getInt(1);
				caja.fecha = LocalDate.parse(rs.getString(2));
				caja.monto = rs.getDouble(3);
				caja.tipo = TipoMovimiento.values()[rs.getInt(4)];
				caja.formaPago = FormaPago.values()[rs.getInt(5)];
				
				if(caja.tipo == TipoMovimiento.COMPRA || caja.tipo == TipoMovimiento.VENTA) {
					caja.descripcion = caja.tipo + " DE PRODUCTOS EN " + caja.formaPago;
				} else {
					caja.descripcion = caja.tipo + " DE " + caja.formaPago;
				}
				retorno.add(caja);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		return retorno.toArray(new Caja[retorno.size()]);
	}
}
