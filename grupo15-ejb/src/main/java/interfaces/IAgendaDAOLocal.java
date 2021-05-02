package interfaces;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.Local;

import datatypes.DtAgenda;
import datatypes.DtCupo;
import exceptions.AgendaInexistente;
import exceptions.AgendaRepetida;

@Local
public interface IAgendaDAOLocal {
	public void agregarAgenda(int id, LocalDate fecha, List<DtCupo> cupos) throws AgendaRepetida;
	public DtAgenda obtenerAgenda(int id) throws AgendaInexistente;
	public ArrayList<DtAgenda> listarAgendas()  throws AgendaInexistente;
}
