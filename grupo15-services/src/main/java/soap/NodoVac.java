package soap;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import javax.ejb.EJB;
import javax.jws.WebMethod;
import javax.jws.WebService;

import datatypes.DtStock;
import datatypes.DtUsuarioSoap;
import datatypes.EstadoReserva;
import exceptions.AccionInvalida;
import exceptions.EtapaInexistente;
import exceptions.PuestoNoCargadosException;
import exceptions.ReservaInexistente;
import exceptions.StockVacunaVacunatorioInexistente;
import exceptions.UsuarioInexistente;
import exceptions.VacunaInexistente;
import exceptions.VacunatorioNoCargadoException;
import interfaces.IControladorPuestoLocal;
import interfaces.IReservaDAOLocal;
import interfaces.IStockDaoLocal;
import interfaces.IUsuarioLocal;

@WebService
public class NodoVac {
	
	@EJB
	IUsuarioLocal cu;
	
	@EJB
	IControladorPuestoLocal cp;
	
	@EJB
	IStockDaoLocal cs;
	
	@EJB
	IReservaDAOLocal cr;

	
	@WebMethod
	public ArrayList<String> listarPuestos(String idVac) throws PuestoNoCargadosException {
		return cp.listarPuestos(idVac);
	}
	
	@WebMethod
	public ArrayList<DtUsuarioSoap> listarVacunadoresSoap() throws PuestoNoCargadosException {
		return cu.listarVacunadoresSoap();
	}
	
	@WebMethod
	public DtStock obtenerStockVacuna(String idVacunatorio, String idVacuna) throws VacunatorioNoCargadoException, VacunaInexistente, StockVacunaVacunatorioInexistente {
		return cs.obtenerStock(idVacunatorio, idVacuna);
	}
	
	@WebMethod
	public void actualizarStockVacuna(String idVacunatorio, String idVacuna, Integer cantidad, Integer descartadas,Integer administradas, Integer disponibles) throws VacunatorioNoCargadoException, VacunaInexistente, StockVacunaVacunatorioInexistente {
		cs.modificarStock(idVacunatorio, idVacuna, cantidad, descartadas, administradas, disponibles);
	}
	
	@WebMethod
	public void actualizarReserva(String idUser, String fecha, String estado, String idVacunatorio) throws NumberFormatException, AccionInvalida {
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
}
