package informes;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

import javafx.scene.control.Alert;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.view.JasperViewer;

public class Creador {
	/**
     * Crea un informe a partir de un archivo Jasper y unos parámetros dados.
     * @param ruta la ruta del archivo Jasper
     * @param parameters los parámetros a utilizar en el informe
     */
	public static void crearInforme(String ruta, Map<String, Object> parameters) {
	    try {
	    	InputStream jasper = Creador.class.getResourceAsStream(ruta);
	        
	    	String cadenaConexion = "jdbc:mysql://localhost/libros?serverTimezone=" + TimeZone.getDefault().getID();
	        Connection conexion = DriverManager.getConnection(cadenaConexion, "admin", "password");	        
	        
			try {
				JasperReport report = (JasperReport) JRLoader.loadObject(jasper);
				JasperPrint print = JasperFillManager.fillReport(report, parameters != null ? parameters : new HashMap<String, Object>(), conexion);
		        JasperViewer viewer = new JasperViewer(print, false);
		        viewer.setVisible(true);
			} catch (Exception e) {
	            Alert alert = new Alert(Alert.AlertType.ERROR);
	            alert.setHeaderText(null);
	            alert.setTitle("ERROR");
	            alert.setContentText("Ha ocurrido un error");
	            alert.showAndWait();
	            e.printStackTrace();
	        }
	        
	    } catch (Exception e) {
	    	e.printStackTrace();
	    }
	}
}