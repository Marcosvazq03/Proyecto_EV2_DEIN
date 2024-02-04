package controllers;

import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.ResourceBundle;

import daos.DaoHistoricoPrestamo;
import daos.DaoLibro;
import daos.DaoPrestamo;
import enums.EstadoLibro;
import excepciones.BibliotecaException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import models.Prestamo;
import utils.Utilidades;

public class DevolverPrestamoController implements Initializable {
	
	private BibliotecaController contexto;

	private Prestamo prestamo;
	
    @FXML
    private Button btnCancelar;

    @FXML
    private Button btnConfirmar;

    @FXML
    private ChoiceBox<String> cbEstado;

    @FXML
    private DatePicker dpFechaDevolucion;

    @FXML
    private Label lblFechaDevolucion;

    @FXML
    private Label lblLibro;

    @FXML
    void cancelar(ActionEvent event) {
    	Utilidades.cerrarVentanaDesdeEvento(event);
    }

    @FXML
    void confirmar(ActionEvent event) {
    	
		try {
			
			DaoPrestamo.borrarPrestamo(prestamo);
			prestamo.setFechaDevolucion(Utilidades.local2Date(dpFechaDevolucion.getValue()));
			DaoLibro.modificarLibro(prestamo.getLibro().setEstado(EstadoLibro.getByValor(cbEstado.getValue())));
			DaoHistoricoPrestamo.anadirHistoricoPrestamo(prestamo);
			
	    	if (contexto != null) {
	    		contexto.buscarLibro(event);
	    		contexto.buscarPrestamo(event);
	    		contexto.buscarHistoricoPrestamo(event);
	    	}
	    	cancelar(event);
		} catch (BibliotecaException |SQLException e) {
			Utilidades.lanzarError(e);
		}
    }

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		cbEstado
		.getItems()
		.addAll(Arrays.stream(EstadoLibro.values())
				.map(EstadoLibro::getValor)
				.toList());
		
		cbEstado.getSelectionModel().selectFirst();
	}
    
	public DevolverPrestamoController setContexto(BibliotecaController controlador) {
    	this.contexto = controlador;
    	return this;
    }
    
	public DevolverPrestamoController setPrestamo(Prestamo prestamo) {
		this.prestamo = prestamo;
		lblLibro.setText(prestamo.getLibro().getTitulo());
		if (prestamo.getLibro().getEstado() != null) {
			cbEstado.getSelectionModel().select(prestamo.getLibro().getEstado().getValor());			
		}
		dpFechaDevolucion.setValue(LocalDate.now());
		return this;
	}

}
