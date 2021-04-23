package evento;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javafx.event.EventHandler;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;

public abstract class Validar {
	/**
	 * evento para controlar que solo se ingresen caracteres permitidos
	 */
	private static EventHandler<KeyEvent> controlTextos = new EventHandler<KeyEvent>() {
		public void handle(KeyEvent e) {
			// obtiene el codigo ascii de los catacteres
			int[] caracteresEspeciales = { 'á', 'é', 'í', 'ó', 'ú', 'Á', 'É', 'Í', 'Ó', 'Ú', 'ñ', 'Ñ', 'ü', 'Ü' };
			int caracter = e.getCharacter().charAt(0);
			if (caracter < 97 || caracter > 122) {// si no es minuscula
				if (caracter < 65 || caracter > 90) { // si no es mayuscula
					if (caracter != 32 && caracter != '\b') {// si no es espacio o borrado a la izquierda
						for (int i = 0; i < caracteresEspeciales.length; i++) {
							if (caracteresEspeciales[i] == caracter) {// si no es una letra con acento
								if (Character.isLowerCase(caracter))// si es minuscula convierte a mayuscula
									;// e.setKeyChar((char) Character.toUpperCase(caracter));
								return;// termina la ejecución
							}
						}
						e.consume();
					}
				}
			} else { // si es minuscula
				// e.setKeyChar((char) (caracter-32));
			}
		}
	};
	/**
	 * evento para controlar que solo se ingresen caracteres permitidos
	 */
	private static EventHandler<KeyEvent> controlNumeros = new EventHandler<KeyEvent>() {
		public void handle(KeyEvent e) {
			char caracter = e.getCharacter().charAt(0);

			// Verificar si la tecla pulsada no es un digito \b es un borrado a la izquierda
			if ( (caracter < '0' || caracter > '9') && (caracter != '\b')) {
				if (caracter == '.' || caracter == ',') {
					TextField campo = null;
					if (e.getSource() instanceof TextField) {
						campo = (TextField) e.getSource();
						if (campo.getText().indexOf(',') == -1 && campo.getText().indexOf('.') == -1)
							/* e.setKeyChar(',') */;
						else
							e.consume();

					} else {
						e.consume();
					}
				} else {
					e.consume(); // ignorar el evento de teclado
				}
			}
		}
	};
	/**
	 * evento para controlar que solo se ingresen caracteres permitidos
	 */
	private static EventHandler<KeyEvent> controlAlfanumerico = new EventHandler<KeyEvent>() {
		public void handle(KeyEvent e) {
			int[] caracteresEspeciales = { 'á', 'é', 'í', 'ó', 'ú', 'Á', 'É', 'Í', 'Ó', 'Ú', 'ñ', 'Ñ', 'ü', 'Ü', ',',
					'.', '/' }; // obtiene el codigo ascii de los catacteres
			int caracter = e.getCharacter().charAt(0);
			if (caracter < 97 || caracter > 122) {// si no es minuscula a-z
				if (caracter < 65 || caracter > 90) { // si no es mayuscula A-Z
					if (caracter != 32 && caracter != '\b') { // si no es espacio o borrado a la izquierda
						if (caracter < 48 || caracter > 57) {// si no es un numero 0- 9
							for (int i = 0; i < caracteresEspeciales.length; i++) {
								if (caracteresEspeciales[i] == caracter) {// si no es una letra con acento
									if (Character.isLowerCase(caracter))// si es minuscula convierte a mayuscula
										;// e.setKeyChar((char) Character.toUpperCase(caracter));
									return;// termina la ejecución
								}
							}
							e.consume();
						}
					}
				}
			} else {// si es minuscula
				// e.setKeyChar((char) (caracter-32)); // convertir a mayuscula
			}
		}
	};
	/**
	 * evento para controlar que solo se ingresen caracteres permitidos
	 */
	private static EventHandler<KeyEvent> controlEmail = new EventHandler<KeyEvent>() {
		public void handle(KeyEvent e) {
			int[] caracteresEspeciales = { '+', '-', '.', '@', '_' }; // obtiene el codigo ascii de los catacteres
			int caracter = e.getCharacter().charAt(0);
			if (caracter < 97 || caracter > 122) {// si no es minuscula a-z
				if (caracter < 65 || caracter > 90) { // si no es mayuscula A-Z
					if (caracter != '\b') { // si no es espacio o borrado a la izquierda
						if (caracter < 48 || caracter > 57) { // si no es un numero 0- 9
							for (int i = 0; i < caracteresEspeciales.length; i++) {
								if (caracteresEspeciales[i] == caracter)// si es algun caracter permitido
									return;// termina la ejecución
							}
							e.consume();// si es un caracter no permitido lo borra
						}
					}
				}
			} else { // si es minuscula
				// e.setKeyChar((char) (caracter-32)); // convierte a mayuscula
			}
		}
	};

	private static EventHandler<KeyEvent> controlUrl = new EventHandler<KeyEvent>() {
		public void handle(KeyEvent e) {
			// segun norma rfc 3986 seccion 2.2 caracteres reservados, sacando ; y '
			int[] caracteresEspeciales = { '.', ':', '/', '?', '#', '[', ']', '@', '!', '$', '&', '(', ')', '*', '+',
					',', '=' }; // obtiene el codigo ascii de los catacteres
			int caracter = e.getCharacter().charAt(0);
			if (caracter < 97 || caracter > 122) {// si no es minuscula a-z
				if (caracter < 65 || caracter > 90) { // si no es mayuscula A-Z
					if (caracter != '\b') { // si no es espacio o borrado a la izquierda
						if (caracter < 48 || caracter > 57) { // si no es un numero 0- 9
							for (int i = 0; i < caracteresEspeciales.length; i++) {
								if (caracteresEspeciales[i] == caracter)// si es algun caracter permitido
									return;// termina la ejecución
							}
							e.consume();// si es un caracter no permitido lo borra
						}
					}
				}
			} else { // si es minuscula
				// e.setKeyChar((char) (caracter-32)); // convierte a mayuscula
			}
		}
	};

	/**
	 * evento para controlar el numero de telefono
	 */
	public static final FocusListener controlTelefono = new FocusListener() {

		@Override
		public void focusLost(FocusEvent e) {
			if (e.getSource() instanceof TextField) {
				TextField campo = (TextField) e.getSource();
				String numero = campo.getText();
				long telefono = numberPhone(numero);
				if (telefono == -1L) {
					campo.setStyle("fx-text-fill: red;");
				} else {
					campo.setStyle("fx-text-fill: black;");
				}
			}
		}

		@Override
		public void focusGained(FocusEvent e) {
		}
	};

	/**
	 * valida la direccion de correo electronico al perder el foco
	 */
	public static final FocusListener controlAddressEmail = new FocusListener() {

		@Override
		public void focusLost(FocusEvent e) {
			if (e.getSource() instanceof TextField) {
				TextField campo = (TextField) e.getSource();
				String email = campo.getText();
				email = emailAddress(email);
				if (email == null) {
					campo.setStyle("fx-text-fill: red;");
					if (campo.getText().length() > 0) {
						System.out.println("La dirección de e-mail no es valida");
					}
				} else {
					campo.setStyle("fx-text-fill: black;");
				}
			}
		}

		@Override
		public void focusGained(FocusEvent e) {
		}
	};
	/**
	 * controla el numero de cuit al perder el foco
	 */
	public static final FocusListener controlCuit = new FocusListener() {

		@Override
		public void focusLost(FocusEvent e) {
			if (e.getSource() instanceof TextField) {
				TextField campo = (TextField) e.getSource();
				String numero = campo.getText();
				long cuit = numberCuit(numero);
				if (cuit == -1L) {
					campo.setStyle("fx-text-fill: red;");
					if (campo.getText().length() > 0) {
						System.out.println("El número de cuit no existe");
					}
				} else {
					campo.setStyle("fx-text-fill: black;");
				}
			}
		}

		@Override
		public void focusGained(FocusEvent e) {
		}
	};

	/**
	 * genera una clave o codigo aletorio con la probabilidad de repetirse en el dia
	 * de 1/100.000
	 * 
	 * @return clave generada
	 */
	public static int generarCodigo() {
		// obtiene un numero aleatorio de 0 a 99.999
		int random = (int) (100000 * Math.random());
		// obtiene el numero de dia del año
		short dia = (short) (new GregorianCalendar()).get(Calendar.DAY_OF_YEAR);
		// genera el codigo
		return (int) (dia * Math.pow(10, 5) + random);
	}

	/**
	 * controla que no se ingrese caracteres distintos a los esperados solo acepta
	 * letras
	 * 
	 * @return el evento que controla lo ingresado
	 */
	public static EventHandler<KeyEvent> texts() {
		return controlTextos;
	}

	/**
	 * controla que no se ingresen caracteres distintos a los esperados solo permite
	 * numeros y borrar
	 * 
	 * @return el evento que controla lo ingresado
	 */

	public static EventHandler<KeyEvent> numbers() {
		return controlNumeros;
	}

	/**
	 * controla que no se ingrese caracteres no esperados, permite numeros y letras
	 * 
	 * @return el evento que controla lo ingresado
	 */
	public static EventHandler<KeyEvent> alphanumeric() {
		return controlAlfanumerico;
	}

	/**
	 * controla que no se ingrese caracteres no esperados, permite numeros y letras
	 * 
	 * @return el evento que controla lo ingresado
	 */
	public static EventHandler<KeyEvent> email() {
		return controlEmail;
	}

	/**
	 * controla que no se ingrese caracteres no esperados, permite numero letras y
	 * caracteres especiales permitidos en una url
	 * 
	 * @return el evento que controla lo ingresado
	 */
	public static EventHandler<KeyEvent> url() {
		return controlUrl;
	}

	/**
	 * verifica numero de telefono
	 * 
	 * @param number numero a verificar
	 * @return devuelve el número de telefono si es correcto o el número corregido
	 *         si se agregaron caracteres como el 0, 9 o el 15, o devuelve un -1L si
	 *         esta mal
	 */
	public static long numberPhone(String number) {
		if (number != null && number.length() > 0) {
			if (number.length() == 10) {
				try {
					return Long.parseLong(number);
				} catch (NumberFormatException e) {
					return -1L;
				}
			} else if (number.length() > 10) {

				// elimina el 54
				if (number.substring(0, 2).equals("54"))
					number = number.substring(2);
				// elimina el 0 o el 9
				if (number.charAt(0) == '0' || number.charAt(0) == '9')
					number = number.substring(1);

				if (number.length() == 10) {
					try {
						return Long.parseLong(number);
					} catch (NumberFormatException e) {
						return -1L;
					}
				} // else implicito
					// hay 12 digitos falta eliminar el 15
				if (number.substring(0, 2).equals("11")) {
					number = "11" + number.substring(4);
				} else {
					if (number.charAt(3) == '1' && number.charAt(4) == '5') {
						// tiene 3 caractes la caracteristica
						number = number.substring(0, 3) + number.substring(5);
					} else { // tiene 4 caracteres la catacteristicas
						number = number.substring(0, 4) + number.substring(6);
					}
					if (number.length() == 10) {
						try {
							return Long.parseLong(number);
						} catch (NumberFormatException e) {
							return -1L;
						}
					} else {
						return -1L;
					}
				}
			}
		}
		return -1L;
	}

	/**
	 * verifica si es correcta una direccion de correo electronico
	 * 
	 * @param email direccion de correo electronico a verificar
	 * @return devuelve la direccion de correo electronico si esta bien o null si no
	 *         cumple con un correo valido
	 */
	public static String emailAddress(String email) {

		if (email != null && email.length() > 0) {
			Pattern pattern = Pattern.compile(
					"^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$");

			Matcher mather = pattern.matcher(email);
			if (mather.find()) {
				return email;
			}
		}
		return null;
	}

	/**
	 * verifica el número de cuit o cuil de una empresa o persona
	 * 
	 * @param cuit numero a verificar
	 * @return el numero si es correcto o -1 si no es valido
	 */
	public static long numberCuit(String cuit) {
		if (cuit.length() != 11) {
			return -1L;
		}

		char[] cuitArray = cuit.toCharArray();
		byte[] serie = { 5, 4, 3, 2, 7, 6, 5, 4, 3, 2 };
		int aux = 0;

		for (int i = 0; i < 10; i++) {
			aux += Character.getNumericValue(cuitArray[i]) * serie[i];
		}
		aux = 11 - (aux % 11);

		if (aux == 11) {
			aux = 0;
		}

		if (Objects.equals(Character.getNumericValue(cuitArray[10]), aux)) {
			return Long.parseLong(cuit);
		}
		return -1L;
	}
	
	
	/**
	 * Permite validar si es mayor de edad una persona
	 */
	public static boolean isAdult(DatePicker calendario) {
		// Controla que la fecha sea null
		if (calendario.getValue() == null)
			return false;

		// Obtengo dia,mes y año
		int dia = calendario.getValue().getDayOfMonth();
		int mes = calendario.getValue().getMonthValue();
		int anio = calendario.getValue().getYear();

		int fecha = Validar.calcularEdad(dia, mes, anio);

		if (fecha <= 18) 
			return false;
		
		return true;
	}
	
	/**
	 * valida y convierte el número en un long
	 * @param numero
	 * @return
	 */
	public static long parseLong(String numero) {
		long num;
		try {
			num = Long.parseLong(numero);
			return num;
		} catch (NumberFormatException e) {
			return -1L;
		}
	}
	
	/**
	 * valida y convierte el número en un int
	 * @param numero
	 * @return
	 */
	public static int parseInt(String numero) {
		int num;
		try {
			num = Integer.parseInt(numero);
			return num;
		} catch (NumberFormatException e) {
			return -1;
		}
	}

	/**
	 * Permite calcular la edad de una persona
	 * 
	 * @param dia  dia seleccionado del calendario
	 * @param mes  mes seleccionado del calendario
	 * @param anio anio seleccionado del calendario
	 * @return cantidad de años de una persona
	 */
	private static int calcularEdad(int dia, int mes, int anio) {
		// Fecha actual
		Calendar actual = new GregorianCalendar();
	
		// Cojo los datos necesarios
		int diaActual = actual.get(Calendar.DAY_OF_MONTH);
		int mesActual = actual.get(Calendar.MONTH) + 1;
		int anioActual = actual.get(Calendar.YEAR);
		//System.out.println(anioActual);
	
		// Diferencia de años
		int diferencia = anioActual - anio;
	
		/* 
		 Si el mes actual es menor que el que me pasan le resto 1
		 Si el mes es igual y el dia que me pasan es mayor al actual le resto 1
		 Si el mes es igual y el dia que me pasan es menor al actual no le resto 1
		 */
		if (mesActual <= mes) {
			// si
			if (mesActual == mes) {
				if (dia > diaActual) {
					diferencia--;
				}
			} else {
				diferencia--;
			}
		}
	
		return diferencia;
	
	}

}