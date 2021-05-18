package soap;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import javax.ejb.EJB;
import javax.jws.WebMethod;
import javax.jws.WebService;

import exceptions.FechaIncorrecta;
import exceptions.SinPuestosLibres;
import exceptions.UsuarioInexistente;
import exceptions.VacunatorioNoCargadoException;
import interfaces.IControladorVacunadorLocal;

@WebService
public class AsignadoSoap {

	@EJB
	IControladorVacunadorLocal vc;
	
	@WebMethod
	public void asignarVacunadorAVacunatorio(int idVacunador, String idVacunatorio, String fecha) throws UsuarioInexistente, VacunatorioNoCargadoException, SinPuestosLibres, FechaIncorrecta {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		LocalDate f = LocalDate.parse(fecha, formatter);
		vc.asignarVacunadorAVacunatorio(idVacunador, idVacunatorio, f);
	}
	
	public String isVacunadorAsignadoEnFecha(int idVacunador, String fecha) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		LocalDate f = LocalDate.parse(fecha, formatter);
		return vc.isVacunadorAsignadoEnFecha(idVacunador, f);
	}
}
