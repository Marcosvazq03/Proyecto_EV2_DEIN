package controllers;

import java.sql.SQLException;

import daos.DaoAlumno;
import excepciones.BibliotecaException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import models.Alumno;
import utils.StringUtils;
import utils.Utilidades;

public class EditarAlumnoController {

	Alumno alumno = null;
	
	BibliotecaController contexto;
	
    @FXML
    private Button btnCancelar;

    @FXML
    private Button btnConfirmar;

    @FXML
    private Label lblDni;

    @FXML
    private TextField tfApellido1;

    @FXML
    private TextField tfApellido2;

    @FXML
    private TextField tfDni;

    @FXML
    private TextField tfNombre;

    @FXML
    void cancelar(ActionEvent event) {
    	Utilidades.cerrarVentanaDesdeEvento(event);
    }

    @FXML
    void confirmar(ActionEvent event) {
    	try {
    		String errores = validarAlumno();
        	if (StringUtils.isBlank(errores)) {
        		Alumno nuevoAlumno = construirAlumno(); 
        		if (alumno == null) {
        			
        			if (DaoAlumno.existe(nuevoAlumno)) {
        				Utilidades.lanzarError(new BibliotecaException("El alumno con ese DNI ya existe"));
        			} else {        				
        				DaoAlumno.anadirAlumno(nuevoAlumno);
        				cancelar(event);
        			}
        			
        		} else {
        			DaoAlumno.modificarAlumno(nuevoAlumno);
        			cancelar(event);
        		}
        			
        		if (contexto != null) {    		
        			contexto.buscarAlumno(event);
        		}
        		if (contexto != null) {
        			contexto.buscarAlumno(event);
        		}
        	} else {
        		Utilidades.lanzarError(new BibliotecaException(errores));
        	}
    	} catch (BibliotecaException | SQLException e) {
    		Utilidades.lanzarError(e);
    	}
    }
    
    private String validarAlumno() {
    	StringBuilder sbErrores = new StringBuilder();
    	sbErrores.append(Utilidades.checkCampoStrNotNullStr(tfDni) + "\n");
    	sbErrores.append(Utilidades.checkCampoStrNotNullStr(tfNombre) + "\n");
    	sbErrores.append(Utilidades.checkCampoStrNotNullStr(tfApellido1) + "\n");
    	sbErrores.append(Utilidades.checkCampoStrNotNullStr(tfApellido2) + "\n");
    	
    	return StringUtils.trimToEmpty(sbErrores.toString());
    }
    
    private Alumno construirAlumno() {
    	return new Alumno()
    			.setDni(StringUtils.trimToEmpty(tfDni.getText()))
    			.setNombre(StringUtils.trimToEmpty(tfNombre.getText()))
    			.setApellido1(StringUtils.trimToEmpty(tfApellido1.getText()))
    			.setApellido2(StringUtils.trimToEmpty(tfApellido2.getText()));
    }
    private void rellenarAlumno() {
    	if (alumno != null) {    		
    		tfDni.setText(alumno.getDni());
    		tfNombre.setText(alumno.getNombre());
    		tfApellido1.setText(alumno.getApellido1());
    		tfApellido2.setText(alumno.getApellido2());
    		
        	lblDni.setVisible(false);
        	tfDni.setVisible(false);
    	}
    }
    
    public EditarAlumnoController setContexto(BibliotecaController controlador) {
    	this.contexto = controlador;
    	return this;
    }
    
    public EditarAlumnoController setAlumno(Alumno alumno) {
    	this.alumno= alumno;
    	rellenarAlumno();
    	return this;
    }

}
