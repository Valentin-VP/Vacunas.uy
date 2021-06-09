package soap;

import java.util.ArrayList;

import javax.ejb.EJB;
import javax.jws.WebMethod;
import javax.jws.WebService;

import datatypes.DtStock;
import datatypes.DtUsuarioSoap;
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
	public void actualizarReserva(ArrayList<String> reservas) throws ReservaInexistente, UsuarioInexistente, EtapaInexistente {
		//cr.modificarReservaSoap(reservas);
	}
}
