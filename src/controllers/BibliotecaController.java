package controllers;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.Date;
import java.util.ResourceBundle;
import java.util.concurrent.atomic.AtomicLong;

import daos.DaoAlumno;
import daos.DaoHistoricoPrestamo;
import daos.DaoLibro;
import daos.DaoPrestamo;
import excepciones.BibliotecaException;
import informes.Creador;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.DatePicker;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Tab;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.FlowPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import models.Alumno;
import models.Libro;
import models.Prestamo;
import utils.StringUtils;
import utils.Utilidades;

public class BibliotecaController implements Initializable {

    @FXML
    private Button btnAnadirAlumno;

    @FXML
    private Button btnAnadirHistoricoPrestamo;

    @FXML
    private Button btnAnadirLibro;

    @FXML
    private Button btnAnadirPrestamo;
    
    @FXML
    private Button btnInforme1;

    @FXML
    private Button btnInforme2;

    @FXML
    private Button btnInforme3;
    
    @FXML
    private CheckBox cbBusquedaBajaLibros;
    

    @FXML
    private DatePicker dpHistoricoDesde;

    @FXML
    private DatePicker dpHistoricoHasta;

    @FXML
    private Tab tabAlumnos;

    @FXML
    private Tab tabHistoricoPrestamos;

    @FXML
    private Tab tabLibros;

    @FXML
    private Tab tabPrestamos;

    @FXML
    private TableColumn<Alumno, String> tcApellido1Alumno;

    @FXML
    private TableColumn<Alumno, String> tcApellido2Alumno;

    @FXML
    private TableColumn<Libro, String> tcAutorLibro;

    @FXML
    private TableColumn<Prestamo, String> tcAutorLibroHistoricoPrestamo;

    @FXML
    private TableColumn<Prestamo, String> tcAutorLibroPrestamo;

    @FXML
    private TableColumn<Libro, Boolean> tcBajaLibro;

    @FXML
    private TableColumn<Libro, Integer> tcCodigoLibro;

    @FXML
    private TableColumn<Prestamo, Integer> tcCodigoLibroHistoricoPrestamo;

    @FXML
    private TableColumn<Prestamo, Integer> tcCodigoLibroPrestamo;

    @FXML
    private TableColumn<Prestamo, String> tcDNIAlumnoHistoricoPrestamo;

    @FXML
    private TableColumn<Prestamo, String> tcDNIAlumnoPrestamo;

    @FXML
    private TableColumn<Alumno, String> tcDniAlumno;

    @FXML
    private TableColumn<Libro, String> tcEditorialLibro;

    @FXML
    private TableColumn<Prestamo, String> tcEditorialLibroHistoricoPrestamo;

    @FXML
    private TableColumn<Prestamo, String> tcEditorialLibroPrestamo;

    @FXML
    private TableColumn<Libro, String> tcEstadoLibro;

    @FXML
    private TableColumn<Prestamo, Date> tcFechaDevolucionHistoricoPrestamo;

    @FXML
    private TableColumn<Prestamo, Date> tcFechaHistoricoPrestamo;

    @FXML
    private TableColumn<Prestamo, Date> tcFechaPrestamo;

    @FXML
    private TableColumn<Alumno, String> tcNombreAlumno;

    @FXML
    private TableColumn<Prestamo, String> tcNombreAlumnoHistoricoPrestamo;

    @FXML
    private TableColumn<Prestamo, String> tcNombreAlumnoPrestamo;

    @FXML
    private TableColumn<Libro, String> tcTituloLibro;

    @FXML
    private TableColumn<Prestamo, String> tcTituloLibroHistoricoPrestamo;

    @FXML
    private TableColumn<Prestamo, String> tcTituloLibroPrestamo;

    @FXML
    private TextField tfBuscarAlumnos;

    @FXML
    private TextField tfBuscarHistoricoPrestamos;

    @FXML
    private TextField tfBuscarLibros;

    @FXML
    private TextField tfBuscarPrestamos;

    @FXML
    private TableView<Alumno> tvAlumnos;

    @FXML
    private TableView<Prestamo> tvHistoricoPrestamos;

    @FXML
    private TableView<Libro> tvLibros;

    @FXML
    private TableView<Prestamo> tvPrestamos;
    
    @FXML
    void anadirAlumno(ActionEvent event) {
    	abrirEditorAlumno(null);
    }
    
    @FXML
    void anadirLibro(ActionEvent event) {
    	abrirEditorLibro(null);
    }
    
    @FXML
    void anadirPrestamo(ActionEvent event) {
    	abrirEditorPrestamo(null, false);
    }
    
    @FXML
    void anadirHistoricoPrestamo(ActionEvent event) {
    	abrirEditorPrestamo(null, true);
    }

    @FXML
    void buscarAlumno(ActionEvent event) {
    	try {
    		String busqueda = StringUtils.trimToEmpty(tfBuscarAlumnos.getText());
    		tvAlumnos.getItems().clear();
    		tvAlumnos.getItems().addAll(DaoAlumno.getAlumnos(busqueda));
		} catch (BibliotecaException e) {
			e.printStackTrace();
		}
    }

    @FXML
    void buscarLibro(ActionEvent event) {
    	try {
    		String busqueda = StringUtils.trimToEmpty(tfBuscarLibros.getText());
    		tvLibros.getItems().clear();
    		tvLibros.getItems().addAll(
    			DaoLibro.getLibros(busqueda).stream().filter(l -> l.isBaja() == cbBusquedaBajaLibros.isSelected()).toList()
    		);
		} catch (BibliotecaException e) {
			e.printStackTrace();
		}
    }
    
    @FXML
    void buscarPrestamo(ActionEvent event) {
    	try {
    		String busqueda = StringUtils.trimToEmpty(tfBuscarPrestamos.getText());
    		tvPrestamos.getItems().clear();
    		tvPrestamos.getItems().addAll(DaoPrestamo.getPrestamos(busqueda));
		} catch (BibliotecaException e) {
			e.printStackTrace();
		}
    }
    
    @FXML
    void buscarHistoricoPrestamo(ActionEvent event) {
    	AtomicLong tiempoDesde = new AtomicLong();
    	AtomicLong tiempoHasta = new AtomicLong();
    	Date fechaDesde = null;
    	Date fechaHasta = null;
    	
    	try {
    		fechaDesde = Utilidades.local2Date(dpHistoricoDesde.getValue());
    		if (fechaDesde != null) {
    			tiempoDesde.set(fechaDesde.getTime());
    		}
		} catch (Exception e) {/*QUEDA NULL*/}

    	try {    			
    		fechaHasta = Utilidades.local2Date(dpHistoricoHasta.getValue());
    		if (fechaHasta != null) {
    			tiempoHasta.set(fechaHasta.getTime());
    		}
    	} catch (Exception e) {/*QUEDA NULL*/}
    	
    	try {
    		
    		
    		String busqueda = StringUtils.trimToEmpty(tfBuscarHistoricoPrestamos.getText());
    		tvHistoricoPrestamos.getItems().clear();
    		tvHistoricoPrestamos.getItems().addAll(DaoHistoricoPrestamo
    				.getHistoricoPrestamos(busqueda)
    				.stream()
    				.filter(p -> comprobarPorFecha(p.getFecha(),
    						tiempoDesde.get() > 0 ? new Date(tiempoDesde.get()) : null,
    						tiempoHasta.get() > 0 ? new Date(tiempoHasta.get()) : null))
    				.toList()
    		);
		} catch (BibliotecaException e) {
			e.printStackTrace();
		}
    }
    
    @FXML
    void generarInforme1(ActionEvent event) {
    	Creador.crearInforme("/informes/informe2.jasper", null);
    }

    @FXML
    void generarInforme2(ActionEvent event) {
    	Creador.crearInforme("/informes/informe3.jasper", null);
    }

    @FXML
    void generarInforme3(ActionEvent event) {
    	Creador.crearInforme("/informes/informe4.jasper", null);
    }

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		try {
			inicializarAlumnos();
			inicializarLibros();
			inicializarPrestamos();
			inicializarHistoricoPrestamos();
		} catch (BibliotecaException e) {
			e.printStackTrace();
		}
	}
	
	private void inicializarAlumnos() throws BibliotecaException {
		tcDniAlumno.setCellValueFactory(new PropertyValueFactory<Alumno, String>("dni"));
		tcNombreAlumno.setCellValueFactory(new PropertyValueFactory<Alumno, String>("nombre"));
		tcApellido1Alumno.setCellValueFactory(new PropertyValueFactory<Alumno, String>("apellido1"));
		tcApellido2Alumno.setCellValueFactory(new PropertyValueFactory<Alumno, String>("apellido2"));
		buscarAlumno(null);
		
		tfBuscarAlumnos.setOnKeyTyped(event -> {
			buscarAlumno(null);
		});
		
		MenuItem miBorrar = new MenuItem("Borrar");
		miBorrar.setOnAction(evento -> {
			Utilidades.confirmarSiNo("¿Desea borrar este alumno y todos sus préstamos?", () -> {
				try {
					DaoAlumno.borrarAlumno(tvAlumnos.getSelectionModel().getSelectedItem());
					buscarAlumno(evento);
				} catch (BibliotecaException |SQLException e) {
					Utilidades.lanzarError(e);
				}
			});
		});
		
		MenuItem miModificar = new MenuItem("Modificar");
		miModificar.setOnAction(evento -> {
			abrirEditorAlumno(tvAlumnos.getSelectionModel().getSelectedItem());
		});
		
		ContextMenu cm = new ContextMenu(miBorrar, miModificar);
		tvAlumnos.setContextMenu(cm);
	}
	
	private void inicializarLibros() throws BibliotecaException {
		tcCodigoLibro.setCellValueFactory(new PropertyValueFactory<Libro, Integer>("codigo"));
		tcTituloLibro.setCellValueFactory(new PropertyValueFactory<Libro, String>("titulo"));
		tcAutorLibro.setCellValueFactory(new PropertyValueFactory<Libro, String>("autor"));
		tcEditorialLibro.setCellValueFactory(new PropertyValueFactory<Libro, String>("editorial"));
		tcEstadoLibro.setCellValueFactory(new PropertyValueFactory<Libro, String>("estado"));
		tcBajaLibro.setCellFactory(tc -> new CheckBoxTableCell<Libro, Boolean>());
		buscarLibro(null);
		
		tfBuscarLibros.setOnKeyTyped(evento -> {
			buscarLibro(null);
		});

		cbBusquedaBajaLibros.setOnAction(evento -> {
			buscarLibro(null);
		});
		
		MenuItem miBorrar = new MenuItem("Borrar");
		miBorrar.setOnAction(evento -> {
			Utilidades.confirmarSiNo("¿Desea borrar este libro y todos sus préstamos?", () -> {
				try {
					DaoLibro.borrarLibro(tvLibros.getSelectionModel().getSelectedItem());
					buscarLibro(evento);
				} catch (BibliotecaException |SQLException e) {
					Utilidades.lanzarError(e);
				}
			});
		});
		
		MenuItem miModificar = new MenuItem("Modificar");
		miModificar.setOnAction(evento -> {
			abrirEditorLibro(tvLibros.getSelectionModel().getSelectedItem());
		});
		
		ContextMenu cm = new ContextMenu(miBorrar, miModificar);
		tvLibros.setContextMenu(cm);
	}
	
	private void inicializarPrestamos() throws BibliotecaException {
		
		tcCodigoLibroPrestamo.setCellValueFactory(param -> {
            Prestamo prestamo = param.getValue();
            if (prestamo != null && prestamo.getLibro() != null) {
                return new SimpleIntegerProperty(prestamo.getLibro().getCodigo()).asObject();
            }
            return new SimpleIntegerProperty().asObject();
        });
		
		tcTituloLibroPrestamo.setCellValueFactory(param -> {
            Prestamo prestamo = param.getValue();
            if (prestamo != null && prestamo.getLibro() != null) {
                return new SimpleStringProperty(StringUtils.trimToEmpty(prestamo.getLibro().getTitulo()));
            }
            return new SimpleStringProperty();
        });
		
		tcAutorLibroPrestamo.setCellValueFactory(param -> {
			Prestamo prestamo = param.getValue();
			if (prestamo != null && prestamo.getLibro() != null) {
				return new SimpleStringProperty(StringUtils.trimToEmpty(prestamo.getLibro().getAutor()));
			}
			return new SimpleStringProperty();
		});
		
		tcEditorialLibroPrestamo.setCellValueFactory(param -> {
			Prestamo prestamo = param.getValue();
			if (prestamo != null && prestamo.getLibro() != null) {
				return new SimpleStringProperty(StringUtils.trimToEmpty(prestamo.getLibro().getEditorial()));
			}
			return new SimpleStringProperty();
		});
		
		tcDNIAlumnoPrestamo.setCellValueFactory(param -> {
			Prestamo prestamo = param.getValue();
			if (prestamo != null && prestamo.getAlumno() != null) {
				return new SimpleStringProperty(StringUtils.trimToEmpty(prestamo.getAlumno().getDni()));
			}
			return new SimpleStringProperty();
		});
		
		
		tcNombreAlumnoPrestamo.setCellValueFactory(param -> {
			Prestamo prestamo = param.getValue();
			if (prestamo != null && prestamo.getAlumno() != null) {
				String prenom = StringUtils.trimToEmpty(prestamo.getAlumno().getNombre());
				String nom1 = StringUtils.trimToEmpty(prestamo.getAlumno().getApellido1());
				String nom2 = StringUtils.trimToEmpty(prestamo.getAlumno().getApellido2());
				return new SimpleStringProperty(String.format("%s %s, %s", nom1, nom2, prenom));
			}
			return new SimpleStringProperty();
		});
		
		tcFechaPrestamo.setCellValueFactory(new PropertyValueFactory<Prestamo, Date>("fecha"));
		
		buscarPrestamo(null);
		tfBuscarPrestamos.setOnKeyTyped(evento -> {
			buscarPrestamo(null);
		});

		MenuItem miDevolver = new MenuItem("Devolver");
		miDevolver.setOnAction(evento -> {
			abrirDevolverPrestamo(tvPrestamos.getSelectionModel().getSelectedItem());
		});
		
		MenuItem miBorrar = new MenuItem("Borrar");
		miBorrar.setOnAction(evento -> {
			Utilidades.confirmarSiNo("¿Desea borrar este préstamo?", () -> {
				try {
					DaoPrestamo.borrarPrestamo(tvPrestamos.getSelectionModel().getSelectedItem());
					buscarPrestamo(evento);
				} catch (BibliotecaException |SQLException e) {
					Utilidades.lanzarError(e);
				}
			});
		});
		
		MenuItem miModificar = new MenuItem("Modificar");
		miModificar.setOnAction(evento -> {
			abrirEditorPrestamo(tvPrestamos.getSelectionModel().getSelectedItem(), false);
		});
		
		ContextMenu cm = new ContextMenu(miDevolver, miBorrar, miModificar);
		tvPrestamos.setContextMenu(cm);
	}
	
	private void inicializarHistoricoPrestamos() throws BibliotecaException {
		
		tcCodigoLibroHistoricoPrestamo.setCellValueFactory(param -> {
            Prestamo prestamo = param.getValue();
            if (prestamo != null && prestamo.getLibro() != null) {
                return new SimpleIntegerProperty(prestamo.getLibro().getCodigo()).asObject();
            }
            return new SimpleIntegerProperty().asObject();
        });
		
		tcTituloLibroHistoricoPrestamo.setCellValueFactory(param -> {
            Prestamo prestamo = param.getValue();
            if (prestamo != null && prestamo.getLibro() != null) {
                return new SimpleStringProperty(StringUtils.trimToEmpty(prestamo.getLibro().getTitulo()));
            }
            return new SimpleStringProperty();
        });
		
		tcAutorLibroHistoricoPrestamo.setCellValueFactory(param -> {
			Prestamo prestamo = param.getValue();
			if (prestamo != null && prestamo.getLibro() != null) {
				return new SimpleStringProperty(StringUtils.trimToEmpty(prestamo.getLibro().getAutor()));
			}
			return new SimpleStringProperty();
		});
		
		tcEditorialLibroHistoricoPrestamo.setCellValueFactory(param -> {
			Prestamo prestamo = param.getValue();
			if (prestamo != null && prestamo.getLibro() != null) {
				return new SimpleStringProperty(StringUtils.trimToEmpty(prestamo.getLibro().getEditorial()));
			}
			return new SimpleStringProperty();
		});
		
		tcDNIAlumnoHistoricoPrestamo.setCellValueFactory(param -> {
			Prestamo prestamo = param.getValue();
			if (prestamo != null && prestamo.getAlumno() != null) {
				return new SimpleStringProperty(StringUtils.trimToEmpty(prestamo.getAlumno().getDni()));
			}
			return new SimpleStringProperty();
		});
		
		
		tcNombreAlumnoHistoricoPrestamo.setCellValueFactory(param -> {
			Prestamo prestamo = param.getValue();
			if (prestamo != null && prestamo.getAlumno() != null) {
				String prenom = StringUtils.trimToEmpty(prestamo.getAlumno().getNombre());
				String nom1 = StringUtils.trimToEmpty(prestamo.getAlumno().getApellido1());
				String nom2 = StringUtils.trimToEmpty(prestamo.getAlumno().getApellido2());
				return new SimpleStringProperty(String.format("%s %s, %s", nom1, nom2, prenom));
			}
			return new SimpleStringProperty();
		});
		
		tcFechaHistoricoPrestamo.setCellValueFactory(new PropertyValueFactory<Prestamo, Date>("fecha"));
		tcFechaDevolucionHistoricoPrestamo.setCellValueFactory(new PropertyValueFactory<Prestamo, Date>("fechaDevolucion"));
		
		buscarHistoricoPrestamo(null);
		tfBuscarHistoricoPrestamos.setOnKeyTyped(evento -> {
			buscarHistoricoPrestamo(null);
		});
		
		MenuItem miBorrar = new MenuItem("Borrar");
		miBorrar.setOnAction(evento -> {
			Utilidades.confirmarSiNo("¿Desea borrar este préstamo histórico?", () -> {
				try {
					DaoHistoricoPrestamo.borrarHistoricoPrestamo(tvHistoricoPrestamos.getSelectionModel().getSelectedItem());
					buscarHistoricoPrestamo(evento);
				} catch (BibliotecaException |SQLException e) {
					Utilidades.lanzarError(e);
				}
			});
		});
		
		MenuItem miModificar = new MenuItem("Modificar");
		miModificar.setOnAction(evento -> {
			abrirEditorPrestamo(tvHistoricoPrestamos.getSelectionModel().getSelectedItem(), true);
		});
		
		ContextMenu cm = new ContextMenu(miBorrar, miModificar);
		tvHistoricoPrestamos.setContextMenu(cm);
		
	}
	
    private void abrirEditorAlumno(Alumno alumno) {
        FlowPane root;
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/EditarAlumno.fxml"));
            root = loader.load();
            EditarAlumnoController controlador = loader.getController();
            
            controlador
            .setContexto(this)
            .setAlumno(alumno);
            
            Stage stage = new Stage();
            if (alumno != null) {                
                stage.setTitle("EDITAR ALUMNO");
            } else {
            	stage.setTitle("AÑADIR ALUMNO");            	
            }
            stage.initModality(Modality.WINDOW_MODAL);
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    private void abrirEditorLibro(Libro libro) {
    	FlowPane root;
    	try {
    		FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/EditarLibro.fxml"));
    		root = loader.load();
    		EditarLibroController controlador = loader.getController();
    		
    		controlador
    		.setContexto(this)
    		.setLibro(libro);
    		
    		Stage stage = new Stage();
    		if (libro != null) {                
    			stage.setTitle("EDITAR LIBRO");
    		} else {
    			stage.setTitle("AÑADIR LIBRO");
    		}
    		stage.initModality(Modality.WINDOW_MODAL);
    		Scene scene = new Scene(root);
    		stage.setScene(scene);
    		stage.showAndWait();
    	} catch (IOException e) {
    		e.printStackTrace();
    	}
    }
    
    private void abrirEditorPrestamo(Prestamo prestamo, boolean historico) {
    	FlowPane root;
    	try {
    		FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/EditarPrestamo.fxml"));
    		root = loader.load();
    		EditarPrestamoController controlador = loader.getController();
    		
    		controlador
    		.setContexto(this)
    		.setPrestamo(prestamo)
    		.setHistorico(historico);
    		
    		Stage stage = new Stage();
    		if (prestamo != null) {                
    			stage.setTitle("EDITAR " + (historico ? "HISTÓRICO DE PRÉSTAMOS" : "PRÉSTAMO"));
    		} else {
    			stage.setTitle("AÑADIR " + (historico ? "HISTÓRICO DE PRÉSTAMOS" : "PRÉSTAMO"));
    		}
    		stage.initModality(Modality.WINDOW_MODAL);
    		Scene scene = new Scene(root);
    		stage.setScene(scene);
    		stage.showAndWait();
    	} catch (IOException e) {
    		e.printStackTrace();
    	}
    }
    
    private void abrirDevolverPrestamo(Prestamo prestamo) {
    	if (prestamo != null) {    		
    		FlowPane root;
    		try {
    			FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/DevolverPrestamo.fxml"));
    			root = loader.load();
    			DevolverPrestamoController controlador = loader.getController();
    			
    			controlador
    			.setContexto(this)
    			.setPrestamo(prestamo);
    			
    			Stage stage = new Stage();
				stage.setTitle("DEVOLVER PRÉSTAMO");
    			stage.initModality(Modality.WINDOW_MODAL);
    			Scene scene = new Scene(root);
    			stage.setScene(scene);
    			stage.showAndWait();
    		} catch (IOException e) {
    			e.printStackTrace();
    		}
    	}
    }
    
    private static boolean comprobarPorFecha(Date fechaPrestamo, Date fechaDesde, Date fechaHasta) {
    	if (fechaPrestamo != null) {
    		
    		if (fechaDesde != null && fechaPrestamo.before(fechaDesde)) {
    			return false;
    		} else if (fechaHasta != null && fechaPrestamo.after(fechaHasta)) {
    			return false;
    		}
    	}
    	return true;
    }

}
