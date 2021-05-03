package interfaces;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.Local;

import datatypes.DtAgenda;
import datatypes.DtCupo;
import datatypes.DtReserva;
import exceptions.AgendaInexistente;
import exceptions.AgendaRepetida;
import exceptions.CupoInexistente;
import exceptions.ReservaInexistente;

@Local
public interface IAgendaDAOLocal {
	public void agregarAgenda(int id, LocalDate fecha, List<DtCupo> cupos, List<DtReserva> reservas) throws AgendaRepetida, CupoInexistente, ReservaInexistente;
	public DtAgenda obtenerAgenda(int id) throws AgendaInexistente;
	public ArrayList<DtAgenda> listarAgendas()  throws AgendaInexistente;
	public void modificarAgenda(int id, LocalDate fecha, List<DtCupo> cupos, List<DtReserva> reservas) throws AgendaInexistente, ReservaInexistente, CupoInexistente, AgendaRepetida;
	 public void eliminarCuposAsociados(int idAgenda) throws AgendaInexistente;
}
