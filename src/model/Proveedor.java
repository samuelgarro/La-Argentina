package model;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;

import database.DataBase;

public class Proveedor {
	private int id;
	private String razonSocial;
	private long telefono;
	private String correo;
	private String direccion;
	
	/**
	 * @param razonSocial
	 * @param telefono
	 * @param correo
	 * @param direccion
	 */
	public Proveedor(String razonSocial, long telefono, String correo, String direccion) {
		this.razonSocial = razonSocial;
		this.telefono = telefono;
		this.correo = correo;
		this.direccion = direccion;
	}
	
	
	/**
	 * @param id
	 * @param razonSocial
	 * @param telefono
	 * @param correo
	 * @param direccion
	 */
	public Proveedor(int id, String razonSocial, long telefono, String correo, String direccion) {
		this.id = id;
		this.razonSocial = razonSocial;
		this.telefono = telefono;
		this.correo = correo;
		this.direccion = direccion;
	}


	/**
	 * contructor sin parametros
	 */
	public Proveedor() {
		this.id = -1;
		this.razonSocial = null;
		this.telefono = -1L;
		this.correo = null;
		this.direccion = null;
	}


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
	 * @return the razonSocial
	 */
	public String getRazonSocial() {
		return razonSocial;
	}
	/**
	 * @param razonSocial the razonSocial to set
	 */
	public void setRazonSocial(String razonSocial) {
		this.razonSocial = razonSocial;
	}
	/**
	 * @return the telefono
	 */
	public long getTelefono() {
		return telefono;
	}
	/**
	 * @param telefono the telefono to set
	 */
	public void setTelefono(long telefono) {
		this.telefono = telefono;
	}
	/**
	 * @return the correo
	 */
	public String getCorreo() {
		return correo;
	}
	/**
	 * @param correo the correo to set
	 */
	public void setCorreo(String correo) {
		this.correo = correo;
	}
	/**
	 * @return the direccion
	 */
	public String getDireccion() {
		return direccion;
	}
	/**
	 * @param direccion the direccion to set
	 */
	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}
	
	/**
	 * inserta un nuevo proveedor en la base de datos
	 * @return
	 */
	public boolean insert() {
		String sql = "INSERT INTO proveedor (proveedor_razon_social, proveedor_telefono, proveedor_correo, "
				+ "proveedor_direccion, proveedor_estado) VALUES (?,?,?,?,1)";
		DataBase db = DataBase.getInstancia();
		db.setQuery(sql);
		
		db.setParametro(1, razonSocial);
		
		if(telefono == -1L)
			db.setNull(2, Types.INTEGER);
		else
			db.setParametro(2, telefono);
		
		if(correo == null)
			db.setNull(3, Types.VARCHAR);
		else
			db.setParametro(3, correo);
		
		if(direccion == null)
			db.setNull(4, Types.VARCHAR);
		else
			db.setParametro(4, direccion);
		
		return db.execute();
	}
	
	/**
	 * actualiza los datos del proveedor
	 * @return
	 */
	public boolean update() {
		String sql = "UPDATE proveedor SET proveedor_razon_social = ?, proveedor_telefono = ?, proveedor_correo = ?, "
				+ "proveedor_direccion = ? WHERE proveedor_id = ?";
		DataBase db = DataBase.getInstancia();
		db.setQuery(sql);
		
		db.setParametro(1, razonSocial);
		
		if(telefono == -1L)
			db.setNull(2, Types.INTEGER);
		else
			db.setParametro(2, telefono);
		
		if(correo == null)
			db.setNull(3, Types.VARCHAR);
		else
			db.setParametro(3, correo);
		
		if(direccion == null)
			db.setNull(4, Types.VARCHAR);
		else
			db.setParametro(4, direccion);
		
		db.setParametro(5, id);
		
		return db.execute();
	}
	
	/**
	 * cambia el estado del proveedor
	 * @return
	 */
	public boolean delete() {
		String sql = "UPDATE proveedor SET proveedor_estado = 0 WHERE proveedor_id = ?";
		DataBase db = DataBase.getInstancia();
		db.setQuery(sql);
		
		db.setParametro(1, id);
		
		return db.execute();
	}
	
	/**
	 * convierte el objeto en un string
	 */
	public String toString() {
		return razonSocial == null ? "" : razonSocial;
	}
	/**
	 * devuelve todos los proveedores habilitados para llenar la tabla de proveedores
	 * @return
	 */
	public static Proveedor[] llenarTabla() {
		String sql = "SELECT * FROM proveedor WHERE proveedor_estado = 1";
		DataBase db = new DataBase();
		ArrayList<Proveedor> lista = new ArrayList<Proveedor>();
		db.setQuery(sql);
		
		ResultSet rs = db.executeQuery();
		
		try {
			Proveedor proveedor;
			while(rs.next()) {
				proveedor = new Proveedor(rs.getInt(1), // id
						rs.getString(2), // razon social
						rs.getLong(3), // telefono
						rs.getString(4), // correo
						rs.getString(5) // direccion
				);
				lista.add(proveedor);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return lista.toArray(new Proveedor[lista.size()]);
	}
	
	/**
	 * devuelve todos los proveedores habilitados para llenar un combo de proveedores
	 * @return
	 */
	public static Proveedor[] llenarCombo() {
		String sql = "SELECT * FROM proveedor WHERE proveedor_estado = 1";
		DataBase db = new DataBase();
		ArrayList<Proveedor> lista = new ArrayList<Proveedor>();
		db.setQuery(sql);
		
		ResultSet rs = db.executeQuery();
		
		try {
			Proveedor proveedor;
			lista.add(new Proveedor());
			while(rs.next()) {
				proveedor = new Proveedor(rs.getInt(1), // id
						rs.getString(2), // razon social
						rs.getLong(3), // telefono
						rs.getString(4), // correo
						rs.getString(5) // direccion
				);
				lista.add(proveedor);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return lista.toArray(new Proveedor[lista.size()]);
	}
}
