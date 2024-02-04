package models;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

public class Prestamo implements Serializable {

	/**
	 * Clase que representa un préstamo de un libro a un alumno.
	 */
	
	private static final long serialVersionUID = -5244557243733412845L;
	
	private int id;
	private Alumno alumno;
	private Libro libro;
	private Date fecha, fechaDevolucion;
	
	/**
	 * Obtiene el ID del préstamo.
	 * @return el ID del préstamo
	 */
	public int getId() {
		return id;
	}
	
	/**
	 * Establece el ID del préstamo.
	 * @param id el ID del préstamo a establecer
	 * @return el objeto Prestamo actualizado
	 */
	public Prestamo setId(int id) {
		this.id = id;
		return this;
	}
	
	/**
	 * Obtiene el alumno asociado al préstamo.
	 * @return el alumno asociado al préstamo
	 */
	public Alumno getAlumno() {
		return alumno;
	}
	
	/**
	 * Establece el alumno asociado al préstamo.
	 * @param alumno el alumno asociado al préstamo
	 * @return el objeto Prestamo actualizado
	 */
	public Prestamo setAlumno(Alumno alumno) {
		this.alumno = alumno;
		return this;
	}
	
	/**
	 * Obtiene el libro asociado al préstamo.
	 * @return el libro asociado al préstamo
	 */
	public Libro getLibro() {
		return libro;
	}
	
	/**
	 * Establece el libro asociado al préstamo.
	 * @param libro el libro asociado al préstamo
	 * @return el objeto Prestamo actualizado
	 */
	public Prestamo setLibro(Libro libro) {
		this.libro = libro;
		return this;
	}
	
	/**
	 * Obtiene la fecha de préstamo.
	 * @return la fecha de préstamo
	 */
	public Date getFecha() {
		return fecha;
	}
	
	/**
	 * Establece la fecha de préstamo.
	 * @param fecha la fecha de préstamo a establecer
	 * @return el objeto Prestamo actualizado
	 */
	public Prestamo setFecha(Date fecha) {
		this.fecha = fecha;
		return this;
	}
	
	/**
	 * Obtiene la fecha de devolución del préstamo.
	 * @return la fecha de devolución del préstamo
	 */
	public Date getFechaDevolucion() {
		return fechaDevolucion;
	}
	
	/**
	 * Establece la fecha de devolución del préstamo.
	 * @param fechaDevolucion la fecha de devolución del préstamo a establecer
	 * @return el objeto Prestamo actualizado
	 */
	public Prestamo setFechaDevolucion(Date fechaDevolucion) {
		this.fechaDevolucion = fechaDevolucion;
		return this;
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(id);
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Prestamo other = (Prestamo) obj;
		return id == other.id;
	}
	
}
