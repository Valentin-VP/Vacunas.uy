package interfaces;

import java.time.LocalDate;
import java.util.ArrayList;

import javax.ejb.Remote;

import datatypes.DtAgenda;
import datatypes.DtReserva;
import datatypes.DtReservaCompleto;
import exceptions.AgendaInexistente;
import exceptions.AgendaRepetida;
import exceptions.CupoInexistente;
import exceptions.VacunatorioNoCargadoException;

@Remote
public interface IAgendaDAORemote {
	public void agregarAgenda(String vacunatorio, LocalDate fecha) throws AgendaRepetida, CupoInexistente, VacunatorioNoCargadoException;
	public ArrayList<DtReserva> obtenerAgenda(String vacunatorio, LocalDate fecha) throws AgendaInexistente ;
	public ArrayList<DtAgenda> listarAgendas(String vacunatorio)  throws AgendaInexistente, VacunatorioNoCargadoException;
	public ArrayList<DtReservaCompleto> obtenerAgendaSoap(String vacunatorio, LocalDate fecha) throws AgendaInexistente;
}
