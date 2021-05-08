package interfaces;

import java.time.LocalDate;
import java.util.ArrayList;

import javax.ejb.Local;

import datatypes.DtAgenda;
import exceptions.AgendaInexistente;
import exceptions.AgendaRepetida;
import exceptions.CupoInexistente;
import exceptions.VacunatorioNoCargadoException;

@Local
public interface IAgendaDAOLocal {
	public void agregarAgenda(String vacunatorio, LocalDate fecha) throws AgendaRepetida, CupoInexistente, VacunatorioNoCargadoException;
	public DtAgenda obtenerAgenda(String vacunatorio, LocalDate fecha) throws AgendaInexistente ;
	public ArrayList<DtAgenda> listarAgendas(String vacunatorio)  throws AgendaInexistente, VacunatorioNoCargadoException;
}
