package model;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import database.DataBase;

public class Rubro {
	private int codigo;
	private String nombre;

	/**
	 * @param codigo
	 * @param nombre
	 */
	public Rubro(int codigo, String nombre) {
		this.codigo = codigo;
		this.nombre = nombre;
	}

	/**
	 * 
	 */
	public Rubro() {
	}

	/**
	 * @return the codigo
	 */
	public int getCodigo() {
		return codigo;
	}

	/**
	 * @param codigo the codigo to set
	 */
	public void setCodigo(int codigo) {
		this.codigo = codigo;
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
	 * inserta un nuevo rubro
	 * 
	 * @return
	 */
	public boolean insert() {
		String sql = "INSERT INTO rubro (rubro_codigo, rubro_nombre, rubro_estado) VALUES (NULL,?,1)";
		DataBase db = DataBase.getInstancia();
		db.setQuery(sql);
		
		db.setParametro(1, nombre);
		ResultSet rs = db.executeGetKeys();
		try {
			if(rs.next()) {
				codigo = rs.getInt(1);
				rs.close();
				return true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * actualiza los datos del rubro
	 * 
	 * @return
	 */
	public boolean update() {
		String sql = "UPDATE rubro SET rubro_nombre = ? WHERE rubro_codigo = ?";
		DataBase db = DataBase.getInstancia();
		db.setQuery(sql);
		
		db.setParametro(1, nombre);
		db.setParametro(2, codigo);
		return db.execute();
	}

	/**
	 * elimina el rubro
	 * 
	 * @return
	 */
	public boolean delete() {
		String sql = "UPDATE rubro SET rubro_estado = 0 WHERE rubro_codigo = ?";
		DataBase db = DataBase.getInstancia();
		db.setQuery(sql);
		
		db.setParametro(1, codigo);
		return db.execute();
	}
	
	@Override
	public String toString() {
		return (nombre == null) ? "" : nombre; 
	}
	
	@Override
	public boolean equals(Object obj) {
		Rubro r;
		if(obj instanceof Rubro)
			r = (Rubro) obj;
		else 
			return false;
		
		return r.codigo == codigo;
	}

	/**
	 * devuelve todos los rubros disponibles
	 * @return
	 */
	public static Rubro[] llenatTabla() {
		String sql = "SELECT * FROM rubro WHERE rubro_estado = 1";
		DataBase db = new DataBase();
		ArrayList<Rubro> lista = new ArrayList<Rubro>();
		db.setQuery(sql);
		
		ResultSet rs = db.executeQuery();
		
		try {
			Rubro rubro;
			while(rs.next()) {
				rubro = new Rubro(rs.getInt(1), // codigo
						rs.getString(2) // nombre
				);
				lista.add(rubro);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return lista.toArray(new Rubro[lista.size()]);
	}

	/**
	 * devuelve todos los rubros disponibles
	 * @return
	 */
	public static Rubro[] llenatCombo() {
		String sql = "SELECT * FROM rubro WHERE rubro_estado = 1";
		DataBase db = new DataBase();
		ArrayList<Rubro> lista = new ArrayList<Rubro>();
		db.setQuery(sql);
		
		ResultSet rs = db.executeQuery();
		
		try {
			lista.add(new Rubro());
			Rubro rubro;
			while(rs.next()) {
				rubro = new Rubro(rs.getInt(1), // codigo
						rs.getString(2) // nombre
				);
				lista.add(rubro);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return lista.toArray(new Rubro[lista.size()]);
	}
}
