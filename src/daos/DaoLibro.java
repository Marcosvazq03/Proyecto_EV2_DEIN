package daos;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

import conexion.ConexionBD;
import enums.EstadoLibro;
import excepciones.BibliotecaException;
import models.Libro;

/**
 * Clase para gestionar los libros en la base de datos
 */
public class DaoLibro {
	
	/**
	 * Mapea un ResultSet a un objeto Libro
	 * @param rs ResultSet a mapear
	 * @return Libro mapeado
	 * @throws SQLException si hay un error en la base de datos
	 */
	public static Libro mapLibro(ResultSet rs) throws SQLException {
		return new Libro()
				.setCodigo(rs.getInt("codigo"))
				.setTitulo(rs.getString("titulo"))
				.setAutor(rs.getString("autor"))
				.setEditorial(rs.getString("editorial"))
				.setEstado(EstadoLibro.getByValor(rs.getString("estado")))
				.setBaja(rs.getBoolean("baja"))
				;
	}
	
	/**
	 * Obtiene todos los libros de la base de datos
	 * @return Lista de libros
	 * @throws BibliotecaException si hay un error en la base de datos
	 */
	public static List<Libro> getLibros() throws BibliotecaException {
		return getLibros("");
	}
	
	/**
	 * Obtiene los libros de la base de datos que coinciden con la búsqueda
	 * @param busqueda Texto a buscar
	 * @return Lista de libros que coinciden con la búsqueda
	 * @throws BibliotecaException si hay un error en la base de datos
	 */
	public static List<Libro> getLibros(String busqueda) throws BibliotecaException {
		String like = "%" + busqueda + "%";
		List<Libro> libros= new LinkedList<>();
		String sql = "SELECT * FROM Libro "
				+ "WHERE titulo LIKE ? "
				+ "OR autor LIKE ? "
				+ "OR editorial LIKE ? "
				+ "OR CAST(codigo as CHAR) LIKE ?";
		try(Connection con = ConexionBD.getConexion()) {
			PreparedStatement st = con.prepareStatement(sql);
			st.setString(1, like);
			st.setString(2, like);
			st.setString(3, like);
			st.setString(4, like);
			ResultSet rs = st.executeQuery();
			while (rs.next()) {
				libros.add(mapLibro(rs));
			}
		} catch (SQLException e) {
			throw new BibliotecaException(e);
		}
		return libros;
	}
	
	/**
	 * Obtiene los libros de la base de datos que no están prestados y que coinciden con la búsqueda
	 * @param busqueda Texto a buscar
	 * @return Lista de libros que no están prestados y que coinciden con la búsqueda
	 * @throws BibliotecaException si hay un error en la base de datos
	 */
	public static List<Libro> getLibrosNoPrestados(String busqueda) throws BibliotecaException {
		String like = "%" + busqueda + "%";
		List<Libro> libros= new LinkedList<>();
		String sql = "SELECT Libro.* FROM Libro "
				+ "LEFT JOIN Prestamo ON Prestamo.codigo_libro = Libro.codigo "
				+ "WHERE Prestamo.id_prestamo IS NULL "
				+ "AND (titulo LIKE ? "
				+ "OR autor LIKE ? "
				+ "OR editorial LIKE ? "
				+ "OR CAST(codigo as CHAR) LIKE ?) "
				+ "GROUP BY Libro.codigo";
		try(Connection con = ConexionBD.getConexion()) {
			PreparedStatement st = con.prepareStatement(sql);
			st.setString(1, like);
			st.setString(2, like);
			st.setString(3, like);
			st.setString(4, like);
			ResultSet rs = st.executeQuery();
			while (rs.next()) {
				libros.add(mapLibro(rs));
			}
		} catch (SQLException e) {
			throw new BibliotecaException(e);
		}
		return libros;
	}
	
	/**
	 * Añade un libro a la base de datos
	 * @param libro Libro a añadir
	 * @throws BibliotecaException si hay un error en la base de datos o los datos del libro están incompletos
	 * @throws SQLException si hay un error en la base de datos
	 */
	public static void anadirLibro(Libro libro) throws BibliotecaException, SQLException {
		if (libro != null) {
			
			String sql = "INSERT INTO Libro ("
					+ "titulo, autor, editorial, estado, baja) "
					+ "VALUES (?, ?, ?, ?, ?)";
			
			Connection con = null;
			try {
				con = ConexionBD.getConexion();
				con.setAutoCommit(false);
				
				try (PreparedStatement ps = con.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
					ps.setString(1, libro.getTitulo());
					ps.setString(2, libro.getAutor());
					ps.setString(3, libro.getEditorial());
					ps.setString(4, libro.getEstado() != null ? libro.getEstado().getValor() : null);
					ps.setBoolean(5, libro.isBaja());
					
					ps.executeUpdate();
					
					ResultSet keys = ps.getGeneratedKeys();
					if (keys.first()) {
						libro.setCodigo(keys.getInt(1));
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
	 * Modifica un libro en la base de datos
	 * @param libro Libro a modificar
	 * @throws BibliotecaException si hay un error en la base de datos o los datos del libro están incompletos
	 * @throws SQLException si hay un error en la base de datos
	 */
	public static void modificarLibro (Libro libro) throws BibliotecaException, SQLException {
		if (libro != null && libro.getCodigo() > 0) {
			
			String sql = "UPDATE Libro SET "
					+ "titulo = ?, "
					+ "autor = ?, "
					+ "editorial = ?, "
					+ "estado = ?, "
					+ "baja = ? "
					+ "WHERE codigo = ?";
			
			Connection con = null;
			try {
				con = ConexionBD.getConexion();
				con.setAutoCommit(false);
				
				try (PreparedStatement ps = con.prepareStatement(sql)) {
					ps.setString(1, libro.getTitulo());
					ps.setString(2, libro.getAutor());
					ps.setString(3, libro.getEditorial());
					ps.setString(4, libro.getEstado() != null ? libro.getEstado().getValor() : null);
					ps.setBoolean(5, libro.isBaja());
					ps.setInt(6, libro.getCodigo());
					
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
	 * Borra un libro de la base de datos
	 * @param libro Libro a borrar
	 * @throws BibliotecaException si hay un error en la base de datos
	 * @throws SQLException si hay un error en la base de datos
	 */
	public static void borrarLibro(Libro libro) throws SQLException, BibliotecaException {
		if (libro != null && libro.getCodigo() > 0) {			
			String sql = "DELETE FROM Libro WHERE codigo = ?";
			Connection con = null;
			try {
				
				//BORRAR PRIMERO LAS REFERENCIAS AL NO HABER ON DELETE CASCADE
				DaoPrestamo.borrarPorLibro(libro);
				DaoHistoricoPrestamo.borrarPorLibro(libro);
				
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
	
}
