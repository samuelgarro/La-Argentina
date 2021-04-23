package model;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;

import database.DataBase;

public class Cliente {
	private long documento;
	private String nombre;
	private String apellido;
	private long telefono;
	private String correo;
	private Direccion direccion;
	/**
	 * @param documento
	 * @param nombre
	 * @param apellido
	 * @param telefono
	 * @param correo
	 */
	public Cliente(long documento, String nombre, String apellido, long telefono, String correo, Direccion direccion) {
		this.documento = documento;
		this.nombre = nombre;
		this.apellido = apellido;
		this.telefono = telefono;
		this.correo = correo;
		this.direccion = direccion;
	}
	/**
	 * 
	 */
	public Cliente() {
		this.documento = -1L;
		this.nombre = null;
		this.apellido = null;
		this.telefono = -1L;
		this.correo = null;
		this.direccion = null;
	}
	/**
	 * crea un nuevo cliente apartir del número de documento
	 * @param numeroDoc
	 */
	public Cliente(long numeroDoc) {
		String sql = "SELECT * FROM cliente WHERE cliente_documento = ?";
		DataBase db = new DataBase();
		ResultSet rs;
		
		db.setQuery(sql);
		db.setParametro(1, numeroDoc);
		rs = db.executeQuery();
		
		
		try {
			if(rs.next()) {
				direccion = new Direccion();
				
				documento = rs.getLong(1);
				nombre = rs.getString(2);
				apellido = rs.getString(3);
				telefono = rs.getLong(4);
				correo = rs.getString(5);
				
				direccion.setCiudad(rs.getString(6));
				direccion.setBarrio(rs.getString(7));
				direccion.setCalle(rs.getString(8));
				direccion.setNumero(rs.getInt(9));
				direccion.setManzana(rs.getString(10));
				direccion.setMonoblock(rs.getString(11));
				direccion.setDepartamento(rs.getString(12));
				
			} else {
				direccion = null;
				
				documento = -1L;
				nombre = null;
				apellido = null;
				telefono = -1L;
				correo = null;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	/**
	 * @return the direccion
	 */
	public Direccion getDireccion() {
		return direccion;
	}
	/**
	 * @param direccion the direccion to set
	 */
	public void setDireccion(Direccion direccion) {
		this.direccion = direccion;
	}
	/**
	 * @return the documento
	 */
	public long getDocumento() {
		return documento;
	}
	/**
	 * @param documento the documento to set
	 */
	public void setDocumento(long documento) {
		this.documento = documento;
	}
	/**
	 * @return the nombre
	 */
	public String getNombre() {
		return nombre;
	}
	/**
	 * @param nombre the nombre to set
	 */
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	/**
	 * @return the apellido
	 */
	public String getApellido() {
		return apellido;
	}
	/**
	 * @param apellido the apellido to set
	 */
	public void setApellido(String apellido) {
		this.apellido = apellido;
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
	 * inserta un nuevo cliente
	 * @return
	 */
	public boolean insert() {
		String sql = "INSERT INTO cliente(cliente_documento,cliente_nombre,"
				+ "cliente_apellido,cliente_telefono,cliente_correo,cliente_ciudad,"
				+ "cliente_barrio,cliente_calle,cliente_numero,cliente_mazana,"
				+ "cliente_monoblock,cliente_departamento) VALUES (?,?,?,?,?,?,?,?,?,?,?,?);";
		
		DataBase db = DataBase.getInstancia();
		db.setQuery(sql);
		
		db.setParametro(1, documento);
		db.setParametro(2, nombre);
		
		if(apellido == null)
			db.setNull(3, Types.VARCHAR);
		else
			db.setParametro(3, apellido);
		
		if(telefono == -1L)
			db.setNull(4, Types.BIGINT);
		else
			db.setParametro(4, telefono);
		
		if(correo == null)
			db.setNull(5, Types.VARCHAR);
		else
			db.setParametro(5, correo);
		
		if(direccion == null || direccion.getCiudad() == null)
			db.setNull(6, Types.VARCHAR);
		else
			db.setParametro(6, direccion.getCiudad());
		
		if(direccion == null || direccion.getBarrio() == null)
			db.setNull(7, Types.VARCHAR);
		else
			db.setParametro(7, direccion.getBarrio());
		
		if(direccion == null || direccion.getCalle() == null)
			db.setNull(8, Types.VARCHAR);
		else
			db.setParametro(8, direccion.getCalle());
		
		if(direccion == null || direccion.getNumero() == -1)
			db.setNull(9, Types.INTEGER);
		else
			db.setParametro(9, direccion.getNumero());
		
		if(direccion == null || direccion.getManzana() == null)
			db.setNull(10, Types.VARCHAR);
		else
			db.setParametro(10, direccion.getManzana());
		
		if(direccion == null || direccion.getMonoblock() == null)
			db.setNull(11, Types.VARCHAR);
		else
			db.setParametro(11, direccion.getMonoblock());
		
		if(direccion == null || direccion.getDepartamento() == null)
			db.setNull(12, Types.VARCHAR);
		else
			db.setParametro(12, direccion.getDepartamento());
		
		return db.execute();
	}
	
	public boolean update(long numeroDoc) {
		String sql = "UPDATE cliente SET cliente_documento = ?, cliente_nombre = ?,"
				+ "cliente_apellido = ?, cliente_telefono = ?, cliente_correo = ?, cliente_ciudad = ?,"
				+ "cliente_barrio = ?, cliente_calle = ?, cliente_numero = ?, cliente_mazana = ?,"
				+ "cliente_monoblock = ?, cliente_departamento = ? WHERE cliente_documento = ?";
		
		DataBase db = DataBase.getInstancia();
		db.setQuery(sql);
		
		db.setParametro(1, documento);
		db.setParametro(2, nombre);
		
		if(apellido == null)
			db.setNull(3, Types.VARCHAR);
		else
			db.setParametro(3, apellido);
		
		if(telefono == -1L)
			db.setNull(4, Types.BIGINT);
		else
			db.setParametro(4, telefono);
		
		if(correo == null)
			db.setNull(5, Types.VARCHAR);
		else
			db.setParametro(5, correo);
		
		if(direccion == null || direccion.getCiudad() == null)
			db.setNull(6, Types.VARCHAR);
		else
			db.setParametro(6, direccion.getCiudad());
		
		if(direccion == null || direccion.getBarrio() == null)
			db.setNull(7, Types.VARCHAR);
		else
			db.setParametro(7, direccion.getBarrio());
		
		if(direccion == null || direccion.getCalle() == null)
			db.setNull(8, Types.VARCHAR);
		else
			db.setParametro(8, direccion.getCalle());
		
		if(direccion == null || direccion.getNumero() == -1)
			db.setNull(9, Types.INTEGER);
		else
			db.setParametro(9, direccion.getNumero());
		
		if(direccion == null || direccion.getManzana() == null)
			db.setNull(10, Types.VARCHAR);
		else
			db.setParametro(10, direccion.getManzana());
		
		if(direccion == null || direccion.getMonoblock() == null)
			db.setNull(11, Types.VARCHAR);
		else
			db.setParametro(11, direccion.getMonoblock());
		
		if(direccion == null || direccion.getDepartamento() == null)
			db.setNull(12, Types.VARCHAR);
		else
			db.setParametro(12, direccion.getDepartamento());
		
		db.setParametro(13, numeroDoc);
		
		return db.execute();
	}
	
	@Override
	public String toString() {
		return nombre == null ? "" : (nombre + (apellido == null ? "" : (" " + apellido) )) 
				+ (documento > 0 ? (" - " + documento) : "" );
	}
	
	/**
	 * devuelve todos los clientes existentes
	 * @return
	 */
	public static Cliente[] llenarCombo() {
		String sql = "SELECT * FROM cliente";
		DataBase db = new DataBase();
		ArrayList<Cliente> retorno = new ArrayList<Cliente>();
		ResultSet rs;
		
		db.setQuery(sql);
		rs = db.executeQuery();
		
		try {
			retorno.add(new Cliente());
			Cliente cliente;
			Direccion direccion;
			while(rs.next()) {
				direccion = new Direccion();
				cliente = new Cliente();
				
				cliente.documento = rs.getLong(1);
				cliente.nombre = rs.getString(2);
				cliente.apellido = rs.getString(3);
				cliente.telefono = rs.getLong(4);
				cliente.correo = rs.getString(5);
				
				direccion.setCiudad(rs.getString(6));
				direccion.setBarrio(rs.getString(7));
				direccion.setCalle(rs.getString(8));
				direccion.setNumero(rs.getInt(9));
				direccion.setManzana(rs.getString(10));
				direccion.setMonoblock(rs.getString(11));
				direccion.setDepartamento(rs.getString(12));
				
				cliente.direccion = direccion;
				
				retorno.add(cliente);
				
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return retorno.toArray(new Cliente[retorno.size()]);
	}
	
	
}
