package soap;

import java.time.LocalDate;
import java.util.ArrayList;

import javax.ejb.EJB;
import javax.jws.WebMethod;
import javax.jws.WebService;

import datatypes.DtReservaCompleto;
import exceptions.AgendaInexistente;
import interfaces.IAgendaDAOLocal;

//@WebService
public class AgendaSoap {

	@EJB
	IAgendaDAOLocal ac;
	
	//@WebMethod
	public ArrayList<DtReservaCompleto> obtenerAgendaVacunatorio(String idVacunatorio, String token) throws AgendaInexistente{
		return ac.obtenerAgendaSoap(idVacunatorio, LocalDate.now());
	}
}
