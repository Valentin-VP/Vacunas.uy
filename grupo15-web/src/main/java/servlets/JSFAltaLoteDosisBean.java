package servlets;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

import datatypes.DtVacuna;
import datatypes.DtVacunatorio;

@Named("AltaLoteDosis")
@RequestScoped
public class JSFAltaLoteDosisBean implements Serializable {

	private static final long serialVersionUID = 1L;
	private final Logger LOGGER = Logger.getLogger(getClass().getName());

	private String token;
	private String identificador;
	private List<String> vacunas = new ArrayList<String>();
	private List<DtVacuna> dtVacunas = new ArrayList<DtVacuna>();
	private String vacuna;
	private List<String> vacunatorios = new ArrayList<String>();
	private List<DtVacunatorio> dtVacunatorios = new ArrayList<DtVacunatorio>();
	private String vacunatorio;
	private int cantTotal;
	private int cantEntregada;
	private int cantDescartada;
	private String temperatura;
	private int transportista;
	private List<String> estados = new ArrayList<String>();

	public JSFAltaLoteDosisBean() {}

}
