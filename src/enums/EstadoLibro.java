package enums;

import java.util.Arrays;

/**
 * Enumeración que representa el estado de un libro.
 */
public enum EstadoLibro {
	NUEVO("Nuevo"),
	USADO_NUEVO("Usado Nuevo"),
	USADO_SEMINUEVO("Usado Seminuevo"),
	USADO_ESTROPEADO("Usado Estropeado"),
	RESTAURADO("Restaurado");
	
	private String valor;
	
	private EstadoLibro(String valor) {
		this.valor = valor;
	}
	
	/**
	 * Obtiene el valor asociado al estado del libro.
	 * @return Valor asociado al estado del libro
	 */
	public String getValor() {
		return this.valor;
	}
	
	/**
	 * Obtiene el estado del libro a partir de su valor.
	 * @param valor Valor del estado del libro
	 * @return Estado del libro correspondiente al valor proporcionado
	 */
	public static EstadoLibro getByValor(String valor) {
		return Arrays.stream(EstadoLibro.values())
		.filter(e -> e.valor.equals(valor))
		.findFirst()
		.orElse(null);
	}
}
