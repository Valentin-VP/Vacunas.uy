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
import exceptions.VacunatorioNoCargadoException;

@Local
public interface IAgendaDAOLocal {
	public void agregarAgenda(int id, String vacunatorio, LocalDate fecha, ArrayList<DtCupo> cupos, ArrayList<DtReserva> reservas) throws AgendaRepetida, CupoInexistente, VacunatorioNoCargadoException;
	public void modificarAgenda(int id, String vacunatorio, LocalDate fecha, ArrayList<DtCupo> cupos, ArrayList<DtReserva> reservas) throws AgendaInexistente, CupoInexistente, AgendaRepetida, VacunatorioNoCargadoException;
	public DtAgenda obtenerAgenda(int id) throws AgendaInexistente ;
	public ArrayList<DtAgenda> listarAgendas()  throws AgendaInexistente;
	public void eliminarCuposAsociados(int idAgenda) throws AgendaInexistente ;
}
