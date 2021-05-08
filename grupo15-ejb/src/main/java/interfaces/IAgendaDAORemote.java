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
import exceptions.CupoInexistente;
import exceptions.ReservaInexistente;
import exceptions.VacunatorioNoCargadoException;

@Remote
public interface IAgendaDAORemote {
	public void agregarAgenda(String vacunatorio, LocalDate fecha) throws AgendaRepetida, CupoInexistente, VacunatorioNoCargadoException;
	public DtAgenda obtenerAgenda(String vacunatorio, LocalDate fecha) throws AgendaInexistente ;
	public ArrayList<DtAgenda> listarAgendas(String vacunatorio)  throws AgendaInexistente, VacunatorioNoCargadoException;
}
