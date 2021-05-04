package interfaces;

import java.util.ArrayList;

import javax.ejb.Local;

import datatypes.DtCupo;
import exceptions.AgendaInexistente;
import exceptions.CupoInexistente;
import exceptions.CupoRepetido;

@Local
public interface ICupoDAOLocal {
	public void agregarCupo(int idCupo, int idAgenda, boolean ocupado) throws CupoRepetido, AgendaInexistente;
	 public void modificarCupo(int idCupo, int idAgenda, boolean ocupado) throws AgendaInexistente, CupoInexistente;
	 public void eliminarCupo(int idCupo) throws CupoInexistente, AgendaInexistente ;
	 public DtCupo obtenerCupo(int idCupo) throws CupoInexistente ;
	 public ArrayList<DtCupo> listarCupos() throws CupoInexistente;
}
