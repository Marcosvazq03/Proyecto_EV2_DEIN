package daos;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

import conexion.ConexionBD;
import excepciones.BibliotecaException;
import models.Alumno;
import utils.StringUtils;

/**
 * Clase DAOAlumno para interactuar con la base de datos.
 */
public class DaoAlumno {
	
	/**
	 * Mapea un ResultSet a un objeto Alumno.
	 * @param rs ResultSet a mapear.
	 * @return Alumno mapeado.
	 * @throws SQLException si ocurre un error durante el mapeo.
	 */
	public static Alumno mapAlumno(ResultSet rs) throws SQLException {
		return new Alumno()
				.setDni(rs.getString("dni"))
				.setNombre(rs.getString("nombre"))
				.setApellido1(rs.getString("apellido1"))
				.setApellido2(rs.getString("apellido2"));
	}
	
	/**
	 * Obtiene una lista de todos los alumnos.
	 * @return Lista de alumnos.
	 * @throws BibliotecaException si ocurre un error durante la consulta.
	 */
	public static List<Alumno> getAlumnos() throws BibliotecaException {
		return getAlumnos("");
	}
	
	/**
	 * Obtiene una lista de alumnos que coinciden con la búsqueda.
	 * @param busqueda String de búsqueda.
	 * @return Lista de alumnos que coinciden con la búsqueda.
	 * @throws BibliotecaException si ocurre un error durante la consulta.
	 */
	public static List<Alumno> getAlumnos(String busqueda) throws BibliotecaException {
		String like = "%" + busqueda + "%";
		List<Alumno> alumnos = new LinkedList<>();
		String sql = "SELECT * FROM Alumno "
				+ "WHERE dni like ? "
				+ "OR nombre like ? "
				+ "OR apellido1 like ? "
				+ "OR apellido2 like ?";
		try(Connection con = ConexionBD.getConexion()) {
			PreparedStatement st = con.prepareStatement(sql);
			st.setString(1, like);
			st.setString(2, like);
			st.setString(3, like);
			st.setString(4, like);
			ResultSet rs = st.executeQuery();
			while (rs.next()) {
				alumnos.add(mapAlumno(rs));
			}
		} catch (SQLException e) {
			throw new BibliotecaException(e);
		}
		return alumnos;
	}
	
	/**
	 * Añade un nuevo alumno a la base de datos.
	 * @param alumno Alumno a añadir.
	 * @throws BibliotecaException si ocurre un error durante la inserción.
	 * @throws SQLException si ocurre un error durante la inserción.
	 */
	public static void anadirAlumno(Alumno alumno) throws BibliotecaException, SQLException {
		if (alumno != null) {
			
			String sql = "INSERT INTO Alumno ("
					+ "dni, nombre, apellido1, apellido2) "
					+ "VALUES (?, ?, ?, ?)";
			
			Connection con = null;
			try {
				con = ConexionBD.getConexion();
				con.setAutoCommit(false);
				
				try (PreparedStatement ps = con.prepareStatement(sql)) {
					ps.setString(1, alumno.getDni());
					ps.setString(2, alumno.getNombre());
					ps.setString(3, alumno.getApellido1());
					ps.setString(4, alumno.getApellido2());
					
					ps.executeUpdate();
					
				}
				con.commit();
			} catch (SQLException e) {
				e.printStackTrace();
				con.rollback();
				throw new BibliotecaException(e);
			} finally {
				con.close();
			}			
		} else {			
			throw new BibliotecaException("Los datos introducidos están incompletos");
		}
	}
	
	/**
	 * Modifica un alumno existente en la base de datos.
	 * @param alumno Alumno a modificar.
	 * @throws BibliotecaException si ocurre un error durante la modificación.
	 * @throws SQLException si ocurre un error durante la modificación.
	 */
	public static void modificarAlumno (Alumno alumno) throws BibliotecaException, SQLException {
		if (alumno != null && !StringUtils.isBlank(alumno.getDni())) {
			
			String sql = "UPDATE Alumno SET "
					+ "dni = ?, "
					+ "nombre = ?, "
					+ "apellido1 = ?, "
					+ "apellido2 = ? "
					+ "WHERE dni = ?";
			
			Connection con = null;
			try {
				con = ConexionBD.getConexion();
				con.setAutoCommit(false);
				
				try (PreparedStatement ps = con.prepareStatement(sql)) {
					ps.setString(1, alumno.getDni());
					ps.setString(2, alumno.getNombre());
					ps.setString(3, alumno.getApellido1());
					ps.setString(4, alumno.getApellido2());
					ps.setString(5, alumno.getDni());
					
					ps.executeUpdate();
				}
				con.commit();
			} catch (SQLException e) {
				e.printStackTrace();
				con.rollback();
				throw new BibliotecaException(e);
			} finally {
				con.close();
			}			
		} else {			
			throw new BibliotecaException("Los datos introducidos están incompletos");
		}
	}
	
	/**
	 * Comprueba si un alumno existe en la base de datos.
	 * @param alumno Alumno a comprobar.
	 * @return true si el alumno existe, false en caso contrario.
	 * @throws BibliotecaException si ocurre un error durante la consulta.
	 */
	public static boolean existe(Alumno alumno) throws BibliotecaException {
		if (alumno != null && alumno.getDni() != null) {
			String sql = "SELECT * FROM Alumno WHERE dni = ?";
			try(Connection con = ConexionBD.getConexion()) {
				PreparedStatement st = con.prepareStatement(sql);
				st.setString(1, alumno.getDni());
				ResultSet rs = st.executeQuery();
				if (rs.next()) {
					return rs.first();
				}
			} catch (SQLException e) {
				throw new BibliotecaException(e);
			}
		}
			
		return false;
	}
	
	/**
	 * Borra un alumno de la base de datos.
	 * @param alumno Alumno a borrar.
	 * @throws SQLException si ocurre un error durante la eliminación.
	 * @throws BibliotecaException si ocurre un error durante la eliminación.
	 */
	public static void borrarAlumno(Alumno alumno) throws SQLException, BibliotecaException {
		if (alumno != null && !StringUtils.isBlank(alumno.getDni())) {			
			String sql = "DELETE FROM Alumno WHERE dni = ?";
			Connection con = null;
			try {
				
				//BORRAR PRIMERO LAS REFERENCIAS AL NO HABER ON DELETE CASCADE
				DaoPrestamo.borrarPorAlumno(alumno);
				DaoHistoricoPrestamo.borrarPorAlumno(alumno);
				
				con = ConexionBD.getConexion();
				con.setAutoCommit(false);
				PreparedStatement ps = con.prepareStatement(sql);
				ps.setString(1, alumno.getDni());
				ps.executeUpdate();
				con.commit();
			} catch (SQLException e) {
				con.rollback();
				throw new BibliotecaException(e);
			} finally {
				con.close();
			}
		}
		
	}
	
	
}
