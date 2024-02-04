/**
 * 
 */
/**
 * 
 */
module Proyecto_EV2_DEIN {
	requires javafx.controls;
	requires javafx.fxml;
	requires java.sql;
	requires javafx.base;
	requires jasperreports;
	
	opens application to javafx.graphics, javafx.fxml, javafx.base;
	opens models to javafx.graphics, javafx.fxml, javafx.base;
	opens controllers to javafx.graphics, javafx.fxml, java.sql, javafx.base;
	opens informes to javafx.graphics, javafx.fxml, java.sql, javafx.base, jasperreports;
}