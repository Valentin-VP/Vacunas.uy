
package interfaces;

import java.util.ArrayList;
import java.util.Date;

import javax.ejb.Local;

import datatypes.DtHora;
import datatypes.DtReglasCupos;
import exceptions.ReglasCuposCargadoException;
import exceptions.ReglasCuposNoCargadoException;
import exceptions.ReglasCuposNoCargadosException;

@Local
public interface IControladorReglasCuposLocal {

	public void agregarReglasCupos(String id, Date fecha, Integer duracionTurno,  DtHora horaApertura,DtHora horaCierre) throws ReglasCuposCargadoException;
	public DtReglasCupos obtenerReglasCupos(String id) throws ReglasCuposNoCargadoException;
	public ArrayList<DtReglasCupos> listarReglasCupos()throws ReglasCuposNoCargadosException;
	public void modificarReglasCupos(DtReglasCupos dtRegCup) throws ReglasCuposNoCargadoException;
	public void eliminarReglasCupos(String id) throws ReglasCuposNoCargadoException;
	
}
