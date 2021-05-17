package soap;

import java.time.LocalDate;
import java.util.ArrayList;

import javax.ejb.EJB;
import javax.jws.WebMethod;
import javax.jws.WebService;

import datatypes.DtReserva;
import exceptions.AgendaInexistente;
import interfaces.IAgendaDAOLocal;

@WebService
public class AgendaSoap {

	@EJB
	IAgendaDAOLocal ac;
	
	@WebMethod
	public ArrayList<DtReserva> getAgendaVacunatorio(String idVacunatorio) throws AgendaInexistente{
		return ac.obtenerAgenda(idVacunatorio, LocalDate.now());
	}
}
