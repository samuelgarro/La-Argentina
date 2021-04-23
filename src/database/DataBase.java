package database;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.sql.Types;

public class DataBase {
	private String sql;
	private Object datos[][];
	private Connection conexion;
	private PreparedStatement pst;
	private CallableStatement call;
	private boolean batch;
	private static String url;
	private static DataBase instancia;

	/**
	 * crea una instancia de la clase
	 */
	public DataBase() {
		url = "jdbc:sqlite:database.db";
	}

	/**
	 * devuelve la coneccion a la base de datos principal
	 * 
	 * @return
	 */
	public Connection getConnection() {
		try {

			if (conexion == null || conexion.isClosed())
				crearConeccion();

			return conexion;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * revisa si se encuentra es una transaccion o no
	 * 
	 * @return tue si es una transaccion o false si no lo es
	 */
	public boolean getTransaction() {
		try {
			if (conexion == null || conexion.isClosed())
				return false;
			return !conexion.getAutoCommit();
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * agrega el valor en la posicion especificada.
	 * 
	 * @param index la primera posicion es 1, la segunda es 2 y asi sucesivamente.
	 * @param valor dato a guardar en la base de datos
	 */
	public void setParametro(int index, int valor) {
		datos[0][index - 1] = valor;
		datos[1][index - 1] = Types.INTEGER;
	}

	/**
	 * agrega el valor en la posicion especificada.
	 * 
	 * @param index la primera posicion es 1, la segunda es 2 y asi sucesivamente.
	 * @param valor dato a guardar en la base de datos
	 */
	public void setParametro(int index, long valor) {
		datos[0][index - 1] = valor;
		datos[1][index - 1] = Types.BIGINT;
	}

	/**
	 * agrega en la posicion indicada un valor null
	 * 
	 * @param index    la primera posicion es 1, la segunda es 2 y asi
	 *                 sucesivamente.
	 * @param tipoDato tipo de dato que se espera en la base de datos, valor sacado
	 *                 de Types ejemplo Types.VARCHAR, Types.DECIMAL
	 */
	public void setNull(int index, int tipoDato) {
		datos[0][index - 1] = tipoDato;
		datos[1][index - 1] = Types.NULL;
	}

	/**
	 * agrega el valor en la posicion especificada.
	 * 
	 * @param index la primera posicion es 1, la segunda es 2 y asi sucesivamente.
	 * @param valor dato a guardar en la base de datos
	 */
	public void setParametro(int index, String valor) {
		datos[0][index - 1] = valor;
		datos[1][index - 1] = Types.VARCHAR;
	}

	/**
	 * agrega el valor en la posicion especificada.
	 * 
	 * @param index la primera posicion es 1, la segunda es 2 y asi sucesivamente.
	 * @param valor dato a guardar en la base de datos
	 */
	public void setParametro(int index, float valor) {
		datos[0][index - 1] = valor;
		datos[1][index - 1] = Types.DECIMAL;
	}
	
	/**
	 * agrega el valor en la posicion especificada.
	 * 
	 * @param index la primera posicion es 1, la segunda es 2 y asi sucesivamente.
	 * @param valor dato a guardar en la base de datos
	 */
	public void setParametro(int index, double valor) {
		datos[0][index - 1] = valor;
		datos[1][index - 1] = Types.DECIMAL;
	}	

	/**
	 * agrega el valor en la posicion especificada.
	 * 
	 * @param index la primera posicion es 1, la segunda es 2 y asi sucesivamente.
	 * @param valor dato a guardar en la base de datos
	 */
	public void setParametro(int index, Date valor) {
		datos[0][index - 1] = valor;
		datos[1][index - 1] = Types.DATE;
	}

	/**
	 * agrega el valor en la posicion especificada.
	 * 
	 * @param index la primera posicion es 1, la segunda es 2 y asi sucesivamente.
	 * @param valor dato a guardar en la base de datos
	 */
	public void setParametro(int index, Timestamp valor) {
		datos[0][index - 1] = valor;
		datos[1][index - 1] = Types.TIMESTAMP;
	}

	/**
	 * crea la consulta
	 * 
	 * @param sql consulta a la base de datos
	 * @throws NullPointerException
	 */
	public void setQuery(String sql) {
		this.sql = sql;
		int contador = 0;
		for (int i = 0; i < sql.length(); i++) {
			if (sql.charAt(i) == '?')
				contador++;
		}
		datos = new Object[2][contador];

		try {
			if (conexion == null || conexion.isClosed())
				crearConeccion();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * agrega al batch las consultas
	 */
	public void addBatch() {
		try {
			if(!batch) {
				pst = conexion.prepareStatement(sql);
				batch = true;
			}
			setDatos(pst);
			pst.addBatch();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * configura el procedimiento almacenado
	 * 
	 * @param procedimiento nombre del precedimiento almacenado
	 */
	public void setProcedure(String procedimiento) {
		int contador = 0;
		for (int i = 0; i < procedimiento.length(); i++) {
			if (procedimiento.charAt(i) == '?')
				contador++;
		}
		datos = new Object[2][contador];
		try {

			if (conexion == null || conexion.isClosed())
				crearConeccion();

			call = conexion.prepareCall(procedimiento);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * configura la coneccion para realizar varias consultas a la vez al momento de
	 * tener todas la consultas hechas realizar un commit para finalizar la
	 * transaccion
	 */
	public void setTransaction() {
		try {
			if (conexion == null || conexion.isClosed())
				crearConeccion();

			conexion.setAutoCommit(false);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * realiza la consulta a la base de datos
	 * 
	 * @return devuelve el resultado
	 */
	public ResultSet executeQuery() {
		try {
			pst = conexion.prepareStatement(sql);
			if (datos.length > 0) {
				setDatos(pst);
			}
			return pst.executeQuery();
		} catch (SQLException e) {
			try {
				if (!conexion.getAutoCommit())
					conexion.rollback();
				System.out.println(e);
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		}
		return null;
	}

	/**
	 * realiza la manipulacion de datos Update, Delete y Insert
	 * 
	 * @return devuelve false si es que hubo un problema
	 */
	public boolean execute() {
		int resultado;
		try {
			pst = conexion.prepareStatement(sql);
			setDatos(pst);
			resultado = pst.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
			resultado = -1;
		}
		return (resultado == -1) ? false : true;
	}
	
	/**
	 * ejecuta los batch que se hayan agregado
	 * 
	 * @return devuelve false si es que hubo un problema
	 */
	public boolean executeBatch() {
		int resultado[];
		try {
			resultado = pst.executeBatch();
		} catch (SQLException e) {
			e.printStackTrace();
			resultado = new int[]{-1};
		}
		for(int i = 0; i < resultado.length; i++) {
			if(resultado[i] == -1)
				return false;
		}
		return true;
	}

	/**
	 * ejecuta la consulta select del procedimiento almacenado
	 * 
	 * @return devuelve un resulset con los datos de la consulta
	 */
	public ResultSet callQuery() {
		try {
			setDatos(call);
			ResultSet rs = call.executeQuery();
			datos = null;
			return rs;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * ejecuta la consulta update, delete o insert del procedimiento almacenado
	 * 
	 * @return devuelve true si la ejecución fue correcta o false si hubo un error
	 */
	public boolean callExecute() {
		int resultado = -1;
		try {
			setDatos(call);
			resultado = call.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return (resultado == -1) ? false : true;
	}

	/**
	 * finaliza la transaccion y si no vuelve
	 * 
	 * @return devuelve true en caso de que se ejecuto todo
	 */
	public boolean commit() {
		try {
			// guarda los datos
			conexion.commit();
			return true;
		} catch (SQLException e) {
			try {
				conexion.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * Cierra las conecciones a la base de datos
	 */
	public void close() {
		try {
			if (conexion == null)
				return;// sale del metodo

			if (conexion.getAutoCommit()) {
				if (!conexion.isClosed())
					conexion.close();
				if (pst != null && !pst.isClosed())
					pst.close();
				if (call != null && !call.isClosed())
					call.close();
				datos = null;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * devuelve las claves generadas por un autoincremento
	 * @return resultados con claves generadas, o null si falla
	 */
	public ResultSet executeGetKeys() {
		try {
			pst = conexion.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			setDatos(pst);
			int resultado = pst.executeUpdate();
			return (resultado != -1) ? pst.getGeneratedKeys() : null;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * crea la coneccion a la base de datos
	 */
	private void crearConeccion() {
		try {
			conexion = DriverManager.getConnection(url);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * agrega los parametros al prepareddStatement
	 * 
	 * @param pst preparedstatement al cual agregar los datos
	 */
	private void setDatos(PreparedStatement pst) {
		try {
			for (int i = 0; i < datos[0].length; i++) {
				switch ((int) datos[1][i]) {
				case Types.INTEGER:
					pst.setInt(i + 1, (int) datos[0][i]);
					break;
				case Types.BIGINT:
					pst.setLong(i + 1, (long) datos[0][i]);
					break;
				case Types.DECIMAL:
					pst.setDouble(i + 1, (double) datos[0][i]);
					break;
				case Types.VARCHAR:
					pst.setString(i + 1, (String) datos[0][i]);
					break;
				case Types.DATE:
					pst.setDate(i + 1, (Date) datos[0][i]);
					break;
				case Types.TIMESTAMP:
					pst.setTimestamp(i + 1, (Timestamp) datos[0][i]);
					break;
				case Types.NULL:
					pst.setNull(i + 1, (int) datos[1][i]);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * obtiene una misma instancia de la clase para trabajar con transacciones
	 * 
	 * @return devuelve la instancia unica de la clase
	 */
	public static DataBase getInstancia() {
		if (instancia == null)
			instancia = new DataBase();

		return instancia;
	}

	/**
	 * registra el inicio de sesion
	 */
	public static void registrarIngreso() {

	}

	/**
	 * registra el cierre de sesion
	 */
	public static void registrarSalida() {

	}

}
