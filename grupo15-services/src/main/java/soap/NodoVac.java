package soap;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import javax.ejb.EJB;
import javax.jws.WebMethod;
import javax.jws.WebService;

import datatypes.DtReservaCompleto;
import datatypes.DtStock;
import datatypes.DtUsuarioSoap;
import datatypes.EstadoReserva;
import exceptions.AccionInvalida;
import exceptions.AgendaInexistente;
import exceptions.EtapaInexistente;
import exceptions.FechaIncorrecta;
import exceptions.PuestoNoCargadosException;
import exceptions.ReservaInexistente;
import exceptions.SinPuestosLibres;
import exceptions.StockVacunaVacunatorioInexistente;
import exceptions.UsuarioInexistente;
import exceptions.VacunaInexistente;
import exceptions.VacunatorioNoCargadoException;
import interfaces.IAgendaDAOLocal;
import interfaces.IControladorPuestoLocal;
import interfaces.IControladorVacunadorLocal;
import interfaces.IControladorVacunatorioLocal;
import interfaces.IReservaDAOLocal;
import interfaces.IStockDaoLocal;
import interfaces.IUsuarioLocal;

@WebService(name = "vacService", targetNamespace = "webservice/vacService")
public class NodoVac {
	
	@EJB
	IControladorVacunadorLocal vc;
	
	@EJB
	IControladorVacunatorioLocal vact;
	
	@EJB
	IUsuarioLocal cu;
	
	@EJB
	IControladorPuestoLocal cp;
	
	@EJB
	IStockDaoLocal cs;
	
	@EJB
	IReservaDAOLocal cr;

	@EJB
	IAgendaDAOLocal ac;
	
	@WebMethod(action = "listar", operationName = "listarPuestos")
	public ArrayList<String> listarPuestos(String idVac, String token) throws PuestoNoCargadosException, VacunatorioNoCargadoException, AccionInvalida {
		if (!vact.isTokenCorrecto(idVac, token))
			throw new AccionInvalida("Fallo al identificar nodo.");
		return cp.listarPuestos(idVac);
	}
	
	@WebMethod(action = "listar", operationName = "listarVacunadoresSoap")
	public ArrayList<DtUsuarioSoap> listarVacunadoresSoap(String idVacunatorio, String token) throws PuestoNoCargadosException, AccionInvalida, VacunatorioNoCargadoException {
		if (!vact.isTokenCorrecto(idVacunatorio, token))
			throw new AccionInvalida("Fallo al identificar nodo.");
		return cu.listarVacunadoresSoap();
	}
	
	@WebMethod(action = "obtener", operationName = "obtenerStockVacuna")
	public DtStock obtenerStockVacuna(String idVacunatorio, String idVacuna, String token) throws VacunatorioNoCargadoException, VacunaInexistente, StockVacunaVacunatorioInexistente, AccionInvalida {
		if (!vact.isTokenCorrecto(idVacunatorio, token))
			throw new AccionInvalida("Fallo al identificar nodo.");
		return cs.obtenerStock(idVacunatorio, idVacuna);
	}
	
	@WebMethod(action = "actualizar", operationName = "actualizarStockVacuna")
	public void actualizarStockVacuna(String idVacunatorio, String idVacuna, Integer cantidad, Integer descartadas,Integer administradas, Integer disponibles, String token) throws VacunatorioNoCargadoException, VacunaInexistente, StockVacunaVacunatorioInexistente, AccionInvalida {
		if (!vact.isTokenCorrecto(idVacunatorio, token))
			throw new AccionInvalida("Fallo al identificar nodo.");
		cs.modificarStock(idVacunatorio, idVacuna, cantidad, descartadas, administradas, disponibles);
	}
	
	@WebMethod(action = "actualizar", operationName = "actualizarReserva")
	public void actualizarReserva(String idUser, String fecha, String estado, String idVacunatorio, String token) throws NumberFormatException, AccionInvalida, VacunatorioNoCargadoException {
		if (!vact.isTokenCorrecto(idVacunatorio, token))
			throw new AccionInvalida("Fallo al identificar nodo.");
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		EstadoReserva estadoReal;
		switch (estado){
		case "EN_PROCESO":
			estadoReal = EstadoReserva.EnProceso;
			break;
		case "COMPLETADA":
			estadoReal = EstadoReserva.Completada;
			break;
		case "CANCELADA":
			estadoReal = EstadoReserva.Cancelada;
			break;
		default:
			estadoReal = EstadoReserva.Cancelada;
			break;
		}
		cr.cambiarEstadoReserva(Integer.parseInt(idUser), LocalDateTime.parse(fecha, formatter), estadoReal);
	}
	
	@WebMethod(action = "asignar", operationName = "asignarVacunadorAVacunatorio")
	public void asignarVacunadorAVacunatorio(int idVacunador, String idVacunatorio, String fecha, String token) throws UsuarioInexistente, VacunatorioNoCargadoException, SinPuestosLibres, FechaIncorrecta, AccionInvalida {
		if (!vact.isTokenCorrecto(idVacunatorio, token))
			throw new AccionInvalida("Fallo al identificar nodo.");
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		LocalDate f = LocalDate.parse(fecha, formatter);
		vc.asignarVacunadorAVacunatorio(idVacunador, idVacunatorio, f);
	}
	
	@WebMethod(action = "consulta", operationName = "isVacunadorAsignadoEnFecha")
	public String isVacunadorAsignadoEnFecha(int idVacunador, String fecha, String idVacunatorio, String token) throws VacunatorioNoCargadoException, AccionInvalida {
		if (!vact.isTokenCorrecto(idVacunatorio, token))
			throw new AccionInvalida("Fallo al identificar nodo.");
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		LocalDate f = LocalDate.parse(fecha, formatter);
		return vc.isVacunadorAsignadoEnFecha(idVacunador, f);
	}
	
	@WebMethod(action = "obtener", operationName = "obtenerAgendaVacunatorio")
	public ArrayList<DtReservaCompleto> obtenerAgendaVacunatorio(String idVacunatorio, String token) throws AgendaInexistente, VacunatorioNoCargadoException, AccionInvalida{
		if (!vact.isTokenCorrecto(idVacunatorio, token))
			throw new AccionInvalida("Fallo al identificar nodo.");
		return ac.obtenerAgendaSoap(idVacunatorio, LocalDate.now());
	}
}
