package controllers;

import java.net.URL;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.ResourceBundle;

import daos.DaoLibro;
import enums.EstadoLibro;
import excepciones.BibliotecaException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import models.Libro;
import utils.StringUtils;
import utils.Utilidades;

public class EditarLibroController implements Initializable{
	
	private BibliotecaController contexto;
	
	private Libro libro;

    @FXML
    private Button btnCancelar;

    @FXML
    private Button btnConfirmar;

    @FXML
    private CheckBox cbBaja;

    @FXML
    private TextField tfAutor;

    @FXML
    private TextField tfEditorial;


    @FXML
    private ChoiceBox<String> cbEstado;

    @FXML
    private TextField tfTitulo;

    @FXML
    void cancelar(ActionEvent event) {
    	Utilidades.cerrarVentanaDesdeEvento(event);
    }

    @FXML
    void confirmar(ActionEvent event) {
    	try {
    		String errores = validarLibro();
        	if (StringUtils.isBlank(errores)) {
        		if (libro == null) {
        			DaoLibro.anadirLibro(construirLibro());
        			
        		} else {
        			DaoLibro.modificarLibro(construirLibro());
        		}
        		cancelar(event);
        			
        		if (contexto != null) {    		
        			contexto.buscarLibro(event);
        		}
        		if (contexto != null) {
        			contexto.buscarLibro(event);
        		}
        	} else {
        		Utilidades.lanzarError(new BibliotecaException(errores));
        	}
    	} catch (BibliotecaException | SQLException e) {
    		Utilidades.lanzarError(e);
    	}
    }
    
    private String validarLibro() {
    	StringBuilder sbErrores = new StringBuilder();
    	sbErrores.append(Utilidades.checkCampoStrNotNullStr(tfTitulo) + "\n");
    	sbErrores.append(Utilidades.checkCampoStrNotNullStr(tfAutor) + "\n");
    	sbErrores.append(Utilidades.checkCampoStrNotNullStr(tfEditorial) + "\n");
    	
    	return StringUtils.trimToEmpty(sbErrores.toString());
    }
    
    private Libro construirLibro() {
    	Libro nuevo = new Libro()
    			.setTitulo(StringUtils.trimToEmpty(tfTitulo.getText()))
    			.setAutor(StringUtils.trimToEmpty(tfAutor.getText()))
    			.setEditorial(StringUtils.trimToEmpty(tfEditorial.getText()))
    			.setEstado(EstadoLibro.getByValor(cbEstado.getValue()))
    			.setBaja(cbBaja.isSelected());
    	
    	if (libro != null && libro.getCodigo() > 0) {
    		nuevo.setCodigo(libro.getCodigo());
    	}
    	return nuevo;
    }
    private void rellenarLibro() {
    	if (libro != null) {
    		tfTitulo.setText(libro.getTitulo());
    		tfAutor.setText(libro.getAutor());
    		tfEditorial.setText(libro.getEditorial());
    		cbEstado.setValue(libro.getEstado() != null ? libro.getEstado().getValor() : "");
    		cbBaja.setSelected(libro.isBaja());
    	}
    }
    
    public EditarLibroController setContexto(BibliotecaController controlador) {
    	this.contexto = controlador;
    	return this;
    }
    
    public EditarLibroController setLibro(Libro libro) {
    	this.libro = libro;
    	rellenarLibro();
    	return this;
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

}
