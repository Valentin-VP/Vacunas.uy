package interfaces;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Date;

import javax.ejb.Remote;


import datatypes.DtHora;
import datatypes.DtReglasCupos;


import exceptions.ReglasCuposCargadoException;
import exceptions.ReglasCuposNoCargadoException;
import exceptions.ReglasCuposNoCargadosException;



@Remote
public interface IControladorReglasCuposRemote {


	public void agregarReglasCupos(String id, Integer duracionTurno,  LocalTime horaApertura,LocalTime horaCierre) throws ReglasCuposCargadoException;
	public DtReglasCupos obtenerReglasCupos(String id) throws ReglasCuposNoCargadoException;
	public ArrayList<DtReglasCupos> listarReglasCupos()throws ReglasCuposNoCargadosException;
	public void modificarReglasCupos(DtReglasCupos dtRegCup) throws ReglasCuposNoCargadoException;
	public void eliminarReglasCupos(String id) throws ReglasCuposNoCargadoException;
	
}
