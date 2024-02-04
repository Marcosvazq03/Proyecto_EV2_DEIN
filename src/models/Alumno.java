package models;

import java.io.Serializable;
import java.util.Objects;

/**
 * Clase que representa a un alumno.
 */
public class Alumno implements Serializable {
		
	private static final long serialVersionUID = 1055759459211840526L;
	
	private String dni, nombre, apellido1, apellido2;

	/**
	 * Obtiene el DNI del alumno.
	 * @return el DNI del alumno
	 */
	public String getDni() {
		return dni;
	}

	/**
	 * Establece el DNI del alumno.
	 * @param dni el DNI a establecer
	 * @return el objeto Alumno actualizado
	 */
	public Alumno setDni(String dni) {
		this.dni = dni;
		return this;
	}

	/**
	 * Obtiene el nombre del alumno.
	 * @return el nombre del alumno
	 */
	public String getNombre() {
		return nombre;
	}

	/**
	 * Establece el nombre del alumno.
	 * @param nombre el nombre a establecer
	 * @return el objeto Alumno actualizado
	 */
	public Alumno setNombre(String nombre) {
		this.nombre = nombre;
		return this;
	}

	/**
	 * Obtiene el primer apellido del alumno.
	 * @return el primer apellido del alumno
	 */
	public String getApellido1() {
		return apellido1;
	}

	/**
	 * Establece el primer apellido del alumno.
	 * @param apellido1 el primer apellido a establecer
	 * @return el objeto Alumno actualizado
	 */
	public Alumno setApellido1(String apellido1) {
		this.apellido1 = apellido1;
		return this;
	}

	/**
	 * Obtiene el segundo apellido del alumno.
	 * @return el segundo apellido del alumno
	 */
	public String getApellido2() {
		return apellido2;
	}

	/**
	 * Establece el segundo apellido del alumno.
	 * @param apellido2 el segundo apellido a establecer
	 * @return el objeto Alumno actualizado
	 */
	public Alumno setApellido2(String apellido2) {
		this.apellido2 = apellido2;
		return this;
	}

	@Override
	public int hashCode() {
		return Objects.hash(dni);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Alumno other = (Alumno) obj;
		return Objects.equals(dni, other.dni);
	}

	@Override
	public String toString() {
		return String.format("%s %s, %s", apellido1, apellido2, nombre);
	}
	
	
	
}

