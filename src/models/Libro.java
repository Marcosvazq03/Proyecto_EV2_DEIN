package models;

import java.io.Serializable;
import java.util.Objects;

import enums.EstadoLibro;

/**
 * Clase que representa un libro.
 */
public class Libro implements Serializable {
	
	private static final long serialVersionUID = -5576585320895867104L;
	
	private int codigo;
	private String titulo, autor, editorial;
	private boolean baja;
	private EstadoLibro estado;
	
	/**
	 * Obtiene el código del libro.
	 * @return el código del libro
	 */
	public int getCodigo() {
		return codigo;
	}
	
	/**
	 * Establece el código del libro.
	 * @param codigo el código a establecer
	 * @return el objeto Libro actualizado
	 */
	public Libro setCodigo(int codigo) {
		this.codigo = codigo;
		return this;
	}
	
	/**
	 * Obtiene el título del libro.
	 * @return el título del libro
	 */
	public String getTitulo() {
		return titulo;
	}
	
	/**
	 * Establece el título del libro.
	 * @param titulo el título a establecer
	 * @return el objeto Libro actualizado
	 */
	public Libro setTitulo(String titulo) {
		this.titulo = titulo;
		return this;
	}
	
	/**
	 * Obtiene el autor del libro.
	 * @return el autor del libro
	 */
	public String getAutor() {
		return autor;
	}
	
	/**
	 * Establece el autor del libro.
	 * @param autor el autor a establecer
	 * @return el objeto Libro actualizado
	 */
	public Libro setAutor(String autor) {
		this.autor = autor;
		return this;
	}
	
	/**
	 * Obtiene la editorial del libro.
	 * @return la editorial del libro
	 */
	public String getEditorial() {
		return editorial;
	}
	
	/**
	 * Establece la editorial del libro.
	 * @param editorial la editorial a establecer
	 * @return el objeto Libro actualizado
	 */
	public Libro setEditorial(String editorial) {
		this.editorial = editorial;
		return this;
	}
	
	/**
	 * Obtiene el estado del libro.
	 * @return el estado del libro
	 */
	public EstadoLibro getEstado() {
		return estado;
	}
	
	/**
	 * Establece el estado del libro.
	 * @param estado el estado a establecer
	 * @return el objeto Libro actualizado
	 */
	public Libro setEstado(EstadoLibro estado) {
		this.estado = estado;
		return this;
	}
	
	/**
	 * Indica si el libro está dado de baja.
	 * @return true si el libro está dado de baja, false en caso contrario
	 */
	public boolean isBaja() {
		return baja;
	}
	
	/**
	 * Establece si el libro está dado de baja.
	 * @param baja true si el libro está dado de baja, false en caso contrario
	 * @return el objeto Libro actualizado
	 */
	public Libro setBaja(boolean baja) {
		this.baja = baja;
		return this;
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(codigo);
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Libro other = (Libro) obj;
		return codigo == other.codigo;
	}
	
	@Override
	public String toString() {
		return titulo;
	}
	
	
	
}

