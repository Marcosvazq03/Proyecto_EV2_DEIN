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
import models.Libro;
import models.Prestamo;
import utils.StringUtils;
import utils.Utilidades;

/**
 * Clase que representa el acceso a datos del historial de préstamos.
 */
public class DaoHistoricoPrestamo {
	
	/**
	 * Obtiene el historial de préstamos.
	 * @return Lista de préstamos
	 * @throws BibliotecaException si ocurre un error al acceder a la base de datos
	 */
	public static List<Prestamo> getHistoricoPrestamos() throws BibliotecaException {
		return getHistoricoPrestamos("");
	}

	/**
	 * Obtiene el historial de préstamos que coinciden con la búsqueda especificada.
	 * @param busqueda Término de búsqueda
	 * @return Lista de préstamos que coinciden con la búsqueda
	 * @throws BibliotecaException si ocurre un error al acceder a la base de datos
	 */
	public static List<Prestamo> getHistoricoPrestamos(String busqueda) throws BibliotecaException {
		String like = "%" + busqueda + "%";
		List<Prestamo> prestamos = new LinkedList<>();
		String sql = "SELECT "
				+ "id_prestamo, "
				+ "dni_alumno, "
				+ "Alumno.nombre as nombre_alumno, "
				+ "Alumno.apellido1 as apellido1_alumno, "
				+ "Alumno.apellido2 as apellido2_alumno, "
				+ "codigo_libro, "
				+ "Libro.titulo as titulo_libro, "
				+ "Libro.autor as autor_libro, "
				+ "Libro.editorial as editorial_libro, "
				+ "Libro.estado as estado_libro, "
				+ "Libro.baja as baja_libro, "
				+ "fecha_prestamo, "
				+ "fecha_devolucion "
				+ "FROM Historico_prestamo "
				+ "INNER JOIN Alumno on Alumno.dni = Historico_prestamo.dni_alumno "
				+ "INNER JOIN Libro on Libro.codigo = Historico_prestamo.codigo_libro "
				+ "WHERE dni_alumno LIKE ? "
				+ "OR Alumno.nombre LIKE ? "
				+ "OR Alumno.apellido1 LIKE ? "
				+ "OR Alumno.apellido2 LIKE ? "
				+ "OR Libro.titulo LIKE ? "
				+ "OR Libro.editorial LIKE ? "
				+ "OR Libro.estado LIKE ? "
				+ "OR CAST(codigo_libro as CHAR) LIKE ? "
				+ "ORDER BY fecha_prestamo";
		try(Connection con = ConexionBD.getConexion()) {
			PreparedStatement st = con.prepareStatement(sql);
			st.setString(1, like);
			st.setString(2, like);
			st.setString(3, like);
			st.setString(4, like);
			st.setString(5, like);
			st.setString(6, like);
			st.setString(7, like);
			st.setString(8, like);
			ResultSet rs = st.executeQuery();
			while (rs.next()) {
				prestamos.add(DaoPrestamo.mapPrestamo(rs, true));
			}
		} catch (SQLException e) {
			throw new BibliotecaException(e);
		}
		return prestamos;
	}
	
	/**
	 * Añade un préstamo al historial.
	 * @param prestamo El préstamo a añadir
	 * @throws BibliotecaException si ocurre un error al acceder a la base de datos
	 * @throws SQLException si ocurre un error al ejecutar la consulta SQL
	 */
	public static void anadirHistoricoPrestamo(Prestamo prestamo) throws BibliotecaException, SQLException {
		if (prestamo != null) {
			
			String sql = "INSERT INTO Historico_prestamo ("
					+ "dni_alumno, codigo_libro, fecha_prestamo, fecha_devolucion) "
					+ "VALUES (?, ?, ?, ?)";
			
			Connection con = null;
			try {
				con = ConexionBD.getConexion();
				con.setAutoCommit(false);
				
				try (PreparedStatement ps = con.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
					ps.setString(1, prestamo.getAlumno().getDni());
					ps.setInt(2, prestamo.getLibro().getCodigo());
					ps.setDate(3, Utilidades.sqlDate(prestamo.getFecha()));
					ps.setDate(4, Utilidades.sqlDate(prestamo.getFechaDevolucion()));
					
					ps.executeUpdate();
					
					ResultSet keys = ps.getGeneratedKeys();
					if (keys.first()) {
						prestamo.setId(keys.getInt(1));
					}
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
	 * Modifica un préstamo en el historial.
	 * @param prestamo El préstamo a modificar
	 * @throws BibliotecaException si ocurre un error al acceder a la base de datos
	 * @throws SQLException si ocurre un error al ejecutar la consulta SQL
	 */
	public static void modificarHistoricoPrestamo (Prestamo prestamo) throws BibliotecaException, SQLException {
		if (prestamo != null && prestamo.getId() > 0) {
			
			String sql = "UPDATE Historico_prestamo SET "
					+ "dni_alumno = ?, "
					+ "codigo_libro = ?, "
					+ "fecha_prestamo = ?, "
					+ "fecha_devolucion = ? "
					+ "WHERE id_prestamo = ?";
			
			Connection con = null;
			try {
				con = ConexionBD.getConexion();
				con.setAutoCommit(false);
				
				try (PreparedStatement ps = con.prepareStatement(sql)) {
					ps.setString(1, prestamo.getAlumno().getDni());
					ps.setInt(2, prestamo.getLibro().getCodigo());
					ps.setDate(3, Utilidades.sqlDate(prestamo.getFecha()));
					ps.setDate(4, Utilidades.sqlDate(prestamo.getFechaDevolucion()));
					ps.setInt(5, prestamo.getId());
					
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
	 * Borra un préstamo del historial.
	 * @param prestamo El préstamo a borrar
	 * @throws SQLException si ocurre un error al ejecutar la consulta SQL
	 * @throws BibliotecaException si ocurre un error al acceder a la base de datos
	 */
	public static void borrarHistoricoPrestamo (Prestamo prestamo) throws SQLException, BibliotecaException {
		if (prestamo != null && prestamo.getId() > 0) {
			String sql = "DELETE FROM Historico_prestamo WHERE id_prestamo = ?";
			Connection con = null;
			try {
				con = ConexionBD.getConexion();
				con.setAutoCommit(false);
				PreparedStatement ps = con.prepareStatement(sql);
				ps.setInt(1, prestamo.getId());
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
	
	/**
	 * Borra todos los préstamos asociados a un libro del historial.
	 * @param libro El libro del cual borrar los préstamos
	 * @throws SQLException si ocurre un error al ejecutar la consulta SQL
	 * @throws BibliotecaException si ocurre un error al acceder a la base de datos
	 */
	public static void borrarPorLibro(Libro libro) throws SQLException, BibliotecaException {
		if (libro != null && libro.getCodigo() > 0) {
			String sql = "DELETE FROM Historico_prestamo WHERE codigo_libro = ?";
			Connection con = null;
			try {
				con = ConexionBD.getConexion();
				con.setAutoCommit(false);
				PreparedStatement ps = con.prepareStatement(sql);
				ps.setInt(1, libro.getCodigo());
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
	
	/**
	 * Borra todos los préstamos asociados a un alumno del historial.
	 * @param alumno El alumno del cual borrar los préstamos
	 * @throws SQLException si ocurre un error al ejecutar la consulta SQL
	 * @throws BibliotecaException si ocurre un error al acceder a la base de datos
	 */
	public static void borrarPorAlumno(Alumno alumno) throws SQLException, BibliotecaException {
		if (alumno != null && !StringUtils.isBlank(alumno.getDni())) {
			String sql = "DELETE FROM Historico_prestamo WHERE dni_alumno = ?";
			Connection con = null;
			try {
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
