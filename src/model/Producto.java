package model;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;

import database.DataBase;

public class Producto {
	private int codigo;
	private String nombre;
	private String marca;
	private String modelo;
	private double cantidad;
	private double stockSeguridad;
	private String talle;
	private double precioMinorista;
	private double precioMayorista;
	private String color;
	private UnidadMedida unidad;
	private Rubro[] rubros;

	/**
	 * constructor del objeto con todos los datos
	 * 
	 * @param nombre          nombre del producto
	 * @param marca           marca del producto
	 * @param modelo          modelo
	 * @param cantidad        cantidad
	 * @param talle           talle del producto
	 * @param precioMinorista precio al publico
	 * @param precioMayorista precio al mayorista
	 * @param color           color del producto
	 */
	public Producto(String nombre, String marca, String modelo, double cantidad, double stockSeguridad, String talle,
			double precioMinorista, double precioMayorista, String color, UnidadMedida unidad) {
		super();
		this.codigo = -1;
		this.nombre = nombre;
		this.marca = marca;
		this.modelo = modelo;
		this.cantidad = cantidad;
		this.stockSeguridad = stockSeguridad;
		this.talle = talle;
		this.precioMinorista = precioMinorista;
		this.precioMayorista = precioMayorista;
		this.color = color;
		this.unidad = unidad;
	}

	/**
	 * genera un producto vacio sin datos
	 */
	public Producto() {
		super();
		this.codigo = -1;
		this.nombre = null;
		this.marca = null;
		this.modelo = null;
		this.cantidad = -1d;
		this.stockSeguridad = -1d;
		this.talle = null;
		this.precioMinorista = -1d;
		this.precioMayorista = -1d;
		this.color = null;
		this.unidad = null;
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
	 * @return the marca
	 */
	public String getMarca() {
		return marca;
	}

	/**
	 * @param marca the marca to set
	 */
	public void setMarca(String marca) {
		this.marca = marca;
	}

	/**
	 * @return the modelo
	 */
	public String getModelo() {
		return modelo;
	}

	/**
	 * @param modelo the modelo to set
	 */
	public void setModelo(String modelo) {
		this.modelo = modelo;
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
	 * aumenta la cantidad del stock del producto
	 * @param cantidad
	 */
	public void addCantidad(double cantidad) {
		this.cantidad += cantidad;		
	}

	/**
	 * @return the talle
	 */
	public String getTalle() {
		return talle;
	}

	/**
	 * @param talle the talle to set
	 */
	public void setTalle(String talle) {
		this.talle = talle;
	}

	/**
	 * @return the precioMinorista
	 */
	public double getPrecioMinorista() {
		return precioMinorista;
	}

	/**
	 * @param precioMinorista the precioMinorista to set
	 */
	public void setPrecioMinorista(double precioMinorista) {
		this.precioMinorista = precioMinorista;
	}

	/**
	 * @return the precioMayorista
	 */
	public double getPrecioMayorista() {
		return precioMayorista;
	}

	/**
	 * @param precioMayorista the precioMayorista to set
	 */
	public void setPrecioMayorista(double precioMayorista) {
		this.precioMayorista = precioMayorista;
	}

	/**
	 * @return the color
	 */
	public String getColor() {
		return color;
	}

	/**
	 * @param color the color to set
	 */
	public void setColor(String color) {
		this.color = color;
	}

	/**
	 * @return the unidad
	 */
	public UnidadMedida getUnidad() {
		return unidad;
	}

	/**
	 * @param unidad the unidad to set
	 */
	public void setUnidad(UnidadMedida unidad) {
		this.unidad = unidad;
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
	 * @return the stockSeguridad
	 */
	public double getStockSeguridad() {
		return stockSeguridad;
	}

	/**
	 * @param stockSeguridad the stockSeguridad to set
	 */
	public void setStockSeguridad(double stockSeguridad) {
		this.stockSeguridad = stockSeguridad;
	}

	/**
	 * @return the rubros
	 */
	public Rubro[] getRubros() {
		if(rubros == null) {
			String sql = "SELECT r.* from rubro r "
					+ "INNER JOIN producto_rubro pr "
					+ "ON pr.rubro_codigo = r.rubro_codigo "
					+ "WHERE pr.producto_codigo = ? AND "
					+ "pr.producto_rubro_estado = 1 AND r.rubro_estado = 1";
			ArrayList<Rubro> lista = new ArrayList<Rubro>();
			DataBase db = new DataBase();
			db.setQuery(sql);
			
			db.setParametro(1, codigo);
			
			ResultSet rs = db.executeQuery();
			
			try {
				Rubro rubro;
				while(rs.next()) {
					rubro = new Rubro(rs.getInt(1), 
							rs.getString(2)
					);
					
					lista.add(rubro);
				}
				rubros = lista.toArray(new Rubro[lista.size()]);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return rubros;
	}

	/**
	 * @param rubros the rubros to set
	 */
	public void setRubros(Rubro[] rubros) {
		this.rubros = rubros;
	}

	@Override
	public String toString() {
		return (nombre == null ? "" : (nombre + (modelo == null ? "" : (" - " + modelo + (marca == null ? "" : " " + marca)))));
	}

	/**
	 * guarda un nuevo producto
	 * 
	 * @return
	 */
	public boolean insert() {
		String sql = "INSERT INTO producto (producto_nombre, producto_marca, producto_modelo, "
				+ "producto_cantidad, producto_stock_seguridad, producto_talle, producto_precio, producto_precio_mayorista, "
				+ "producto_color, unidad_medida_id, producto_estado) VALUES (?,?,?,?,?,?,?,?,?,?,1)";
		DataBase db = DataBase.getInstancia();
		db.setTransaction();
		db.setQuery(sql);
		db.setParametro(1, nombre);

		if (marca == null)
			db.setNull(2, Types.VARCHAR);
		else
			db.setParametro(2, marca);

		if (modelo == null)
			db.setNull(3, Types.VARCHAR);
		else
			db.setParametro(3, modelo);

		db.setParametro(4, cantidad);

		if (stockSeguridad == -1d)
			db.setNull(5, Types.DECIMAL);
		else
			db.setParametro(5, stockSeguridad);

		if (talle == null)
			db.setNull(6, Types.VARCHAR);
		else
			db.setParametro(6, talle);

		db.setParametro(7, precioMinorista);
		db.setParametro(8, precioMayorista);

		if (color == null)
			db.setNull(9, Types.VARCHAR);
		else
			db.setParametro(9, color);

		db.setParametro(10, unidad.ordinal());

		// obtiene el codigo asignado por la base de datos
		ResultSet rs = db.executeGetKeys();

		try {
			if (rs.next()) {
				codigo = rs.getInt(1);
				rs.close();
				// inserta las relaciones
				boolean resultado = true;;
				if(rubros != null) {
					sql = "INSERT INTO producto_rubro (producto_codigo, rubro_codigo, producto_rubro_estado) VALUES (?,?,1)";
					
					for (int i = 1; i < rubros.length; i++) 
						sql += ", (?,?,1)";
					
					db.setQuery(sql + ";");
					
					for (int i = 0, index; i < rubros.length; i++) {
						index = i*2 + 1;
						db.setParametro(index, codigo);
						db.setParametro(index + 1, rubros[i].getCodigo());
					}
	
					resultado = db.execute();
				}
				
				if (resultado)
					resultado = db.commit();
				
				return resultado;				
			}
			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * actualiza los datos del producto
	 * 
	 * @return
	 */
	public boolean update() {
		String sql = "UPDATE producto SET producto_nombre = ?, producto_marca = ?, producto_modelo = ?, "
				+ "producto_cantidad = ?, producto_stock_seguridad = ?, producto_talle = ?, producto_precio = ?, producto_precio_mayorista = ?, "
				+ "producto_color = ?, unidad_medida_id = ? WHERE producto_codigo = ?";
		DataBase db = DataBase.getInstancia();
		db.setTransaction();
		db.setQuery(sql);
		db.setParametro(1, nombre);

		if (marca == null)
			db.setNull(2, Types.VARCHAR);
		else
			db.setParametro(2, marca);

		if (modelo == null)
			db.setNull(3, Types.VARCHAR);
		else
			db.setParametro(3, modelo);

		db.setParametro(4, cantidad);

		if (stockSeguridad == -1d)
			db.setNull(5, Types.DECIMAL);
		else
			db.setParametro(5, stockSeguridad);

		if (talle == null)
			db.setNull(6, Types.VARCHAR);
		else
			db.setParametro(6, talle);

		db.setParametro(7, precioMinorista);
		db.setParametro(8, precioMayorista);

		if (color == null)
			db.setNull(9, Types.VARCHAR);
		else
			db.setParametro(9, color);

		db.setParametro(10, unidad.ordinal());
		db.setParametro(11, codigo);

		boolean resultado = db.execute();

		if (resultado) {
			// actualiza las relaciones
			sql = "UPDATE producto_rubro SET producto_rubro_estado = 0 where producto_codigo = ?";
			db.setQuery(sql);
			db.setParametro(1, codigo);
			resultado = db.execute();

			if (resultado) {
				sql = "INSERT OR REPLACE INTO producto_rubro (producto_codigo, rubro_codigo, producto_rubro_estado) VALUES (?,?,1);";

				for (Rubro rubro : rubros) {
					db.setQuery(sql);
					
					db.setParametro(1, codigo);
					db.setParametro(2, rubro.getCodigo());
					
					if((resultado = db.execute()))
						break;
				}


				if (resultado)
					resultado = db.commit();
			}
		}
		return resultado;
	}

	/**
	 * cambia el estado del producto
	 * 
	 * @return
	 */
	public boolean delete() {
		String sql = "UPDATE producto SET producto_estado = 0 WHERE producto_codigo = ?";
		DataBase db = DataBase.getInstancia();
		db.setQuery(sql);

		db.setParametro(1, codigo);

		boolean resultado = db.execute();
		return resultado;
	}

	/**
	 * devuelve todos los materiales que esten habilitados
	 * 
	 * @return
	 */
	public static Producto[] llenarTabla() {
		String sql = "SELECT * FROM producto WHERE producto_estado = 1";
		ArrayList<Producto> retorno = new ArrayList<Producto>();
		DataBase db = new DataBase();
		db.setQuery(sql);
		ResultSet rs = db.executeQuery();
		try {
			Producto producto;
			while (rs.next()) {
				producto = new Producto(rs.getString(2), // nombre
						rs.getString(3), // marca
						rs.getString(4), // modelo
						rs.getDouble(5), // cantidad
						rs.getDouble(6), // estock de seguridad
						rs.getString(7), // talle
						rs.getDouble(8), // precio al publico (minorista)
						rs.getDouble(9), // precio al por mayor
						rs.getString(10), // color
						UnidadMedida.values()[rs.getInt(11)]);

				producto.codigo = rs.getInt(1);
				retorno.add(producto);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return retorno.toArray(new Producto[retorno.size()]);
	}
	
	/**
	 * devuelve todos los materiales que esten habilitados
	 * 
	 * @return
	 */
	public static Producto[] llenarCombo() {
		String sql = "SELECT * FROM producto WHERE producto_estado = 1";
		ArrayList<Producto> retorno = new ArrayList<Producto>();
		DataBase db = new DataBase();
		db.setQuery(sql);
		ResultSet rs = db.executeQuery();
		try {
			Producto producto;
			retorno.add(new Producto());
			while (rs.next()) {
				producto = new Producto(rs.getString(2), // nombre
						rs.getString(3), // marca
						rs.getString(4), // modelo
						rs.getDouble(5), // cantidad
						rs.getDouble(6), // estock de seguridad
						rs.getString(7), // talle
						rs.getDouble(8), // precio al publico (minorista)
						rs.getDouble(9), // precio al por mayor
						rs.getString(10), // color
						UnidadMedida.values()[rs.getInt(11)]);

				producto.codigo = rs.getInt(1);
				retorno.add(producto);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return retorno.toArray(new Producto[retorno.size()]);
	}

}
