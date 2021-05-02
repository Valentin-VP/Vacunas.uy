package interfaces;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.Remote;

import datatypes.DtAgenda;
import datatypes.DtCupo;
import datatypes.DtReserva;
import exceptions.AgendaInexistente;
import exceptions.AgendaRepetida;

@Remote
public interface IAgendaDAORemote {
	public void agregarAgenda(int id, LocalDate fecha, List<DtCupo> cupos, List<DtReserva> reservas) throws AgendaRepetida;
	public DtAgenda obtenerAgenda(int id) throws AgendaInexistente;
	public ArrayList<DtAgenda> listarAgendas()  throws AgendaInexistente;
}
