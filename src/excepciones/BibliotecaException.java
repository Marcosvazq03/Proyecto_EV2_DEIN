package excepciones;


/**
 * Clase de excepción personalizada para la biblioteca.
 */
public class BibliotecaException extends Exception {

	/**
	 * Identificador de serialización único para esta clase.
	 */
	private static final long serialVersionUID = 9075095558068192043L;

	/**
	 * Constructor por defecto.
	 */
	public BibliotecaException() {
		super();
	}

	/**
	 * Constructor con mensaje, causa, y banderas para habilitar la supresión y la traza de pila editable.
	 *
	 * @param message El mensaje de detalle.
	 * @param cause La causa. (Una excepción nula es permitida, e indica que la causa no existe o es desconocida.)
	 * @param enableSuppression Si la supresión está habilitada o deshabilitada.
	 * @param writableStackTrace Si la traza de pila es editable o no editable.
	 */
	public BibliotecaException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	/**
	 * Constructor con mensaje y causa.
	 *
	 * @param message El mensaje de detalle.
	 * @param cause La causa. (Una excepción nula es permitida, e indica que la causa no existe o es desconocida.)
	 */
	public BibliotecaException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * Constructor con mensaje.
	 *
	 * @param message El mensaje de detalle.
	 */
	public BibliotecaException(String message) {
		super(message);
	}

	/**
	 * Constructor con causa.
	 *
	 * @param cause La causa. (Una excepción nula es permitida, e indica que la causa no existe o es desconocida.)
	 */
	public BibliotecaException(Throwable cause) {
		super(cause);
	}
	
}
