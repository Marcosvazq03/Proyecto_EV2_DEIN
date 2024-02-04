package controllers;

import java.net.URL;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

import daos.DaoAlumno;
import daos.DaoHistoricoPrestamo;
import daos.DaoLibro;
import daos.DaoPrestamo;
import excepciones.BibliotecaException;
import informes.Creador;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import models.Alumno;
import models.Libro;
import models.Prestamo;
import utils.Utilidades;

public class EditarPrestamoController implements Initializable {

	private BibliotecaController contexto;
	private Prestamo prestamo;
	private boolean historico;

    @FXML
    private Button btnCancelar;

    @FXML
    private Button btnConfirmar;

    @FXML
    private ChoiceBox<Alumno> cbAlumno;

    @FXML
    private ChoiceBox<Libro> cbLibro;

    @FXML
    private DatePicker dpFecha;

    @FXML
    private DatePicker dpFechaDevolucion;

    @FXML
    private Label lblFechaDevolucion;


    @FXML
    void cancelar(ActionEvent event) {
    	Utilidades.cerrarVentanaDesdeEvento(event);
    }

    @FXML
    void confirmar(ActionEvent event) {
    	try {
    		Prestamo nuevoPrestamo = construirPrestamo();
    		if (historico) {
        		if (prestamo != null) {
        			DaoHistoricoPrestamo.modificarHistoricoPrestamo(nuevoPrestamo);
        		} else {
        			DaoHistoricoPrestamo.anadirHistoricoPrestamo(nuevoPrestamo);
        		}
        		
        		if (contexto != null) {
        			contexto.buscarHistoricoPrestamo(event);
        		}
        	} else {
        		if (prestamo != null) {
        			DaoPrestamo.modificarPrestamo(nuevoPrestamo);
        		} else {
        			DaoPrestamo.anadirPrestamo(nuevoPrestamo);
        			Map<String, Object> parameters = new HashMap<String, Object>();
        			parameters.put("ID_PRESTAMO", nuevoPrestamo.getId());
        			Creador.crearInforme("/informes/informe1.jasper", parameters);
        		}
        		
        		if (contexto != null) {
        			contexto.buscarPrestamo(event);
        		}
        	}
    		cancelar(event);
    	} catch (BibliotecaException | SQLException e) {
    		Utilidades.lanzarError(e);
    	}
    	
    }

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		try {
			cbAlumno
			.getItems()
			.addAll(DaoAlumno.getAlumnos());

			cbAlumno.getSelectionModel().selectFirst();

			cbLibro
			.getItems()
			.addAll(DaoLibro.getLibros());
			
			cbLibro.getSelectionModel().selectFirst();
			
		} catch (BibliotecaException e) {
			Utilidades.lanzarError(e);
		}	
	}
    
    private Prestamo construirPrestamo() {
    	Prestamo nuevo = new Prestamo()
    			.setAlumno(cbAlumno.getValue())
    			.setLibro(cbLibro.getValue())
    			.setFecha(Utilidades.local2Date(dpFecha.getValue()))
    			.setFechaDevolucion(Utilidades.local2Date(dpFechaDevolucion.getValue()))
    			;
    	
    	if (prestamo != null && prestamo.getId() > 0) {
    		nuevo.setId(prestamo.getId());
    	}
    	return nuevo;
    }
    
    private void rellenarPrestamo() {
    	if (prestamo != null) {
    		cbAlumno.setValue(prestamo.getAlumno());
    		cbLibro.setValue(prestamo.getLibro());
    		if (prestamo.getFecha() != null) {    			
    			dpFecha.setValue(Utilidades.date2Local(prestamo.getFecha()));
    		}
    		if (prestamo.getFechaDevolucion() != null) {    			
    			dpFechaDevolucion.setValue(Utilidades.date2Local(prestamo.getFechaDevolucion()));
    		}
    	}
    }
	
    public EditarPrestamoController setContexto(BibliotecaController controlador) {
    	this.contexto = controlador;
    	return this;
    }
    
    public EditarPrestamoController setPrestamo(Prestamo prestamo) {
    	this.prestamo= prestamo;
    	rellenarPrestamo();
    	return this;
    }
	
    public EditarPrestamoController setHistorico(boolean historico) {
    	this.historico = historico;
    	if (!historico) {
    		lblFechaDevolucion.setVisible(false);
        	dpFechaDevolucion.setVisible(false);
			try {
				cbLibro.getItems().clear(); //Si no es el histórico, resetear y meter solo los no prestados

				if (prestamo != null && prestamo.getLibro() != null) { //Primero el seleccionado
					cbLibro
					.getItems()
					.add(prestamo.getLibro());
				}

				cbLibro
				.getItems()
				.addAll(DaoLibro.getLibrosNoPrestados(""));
				

				cbLibro.getSelectionModel().selectFirst();
			} catch (BibliotecaException e) {
				Utilidades.lanzarError(e);
			}
			
    	}
    	return this;
    }
    
}
