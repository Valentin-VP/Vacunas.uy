package interfaces;

import java.util.ArrayList;

import javax.ejb.Remote;

import datatypes.DtPuesto;
import entities.Vacunatorio;
import exceptions.PuestoCargadoException;
import exceptions.PuestoNoCargadoException;
import exceptions.PuestoNoCargadosException;
import exceptions.VacunatorioNoCargadoException;




@Remote
public interface IControladorPuestoRemote {

	public void agregarPuesto(String id, String vacunatorio) throws PuestoCargadoException, VacunatorioNoCargadoException;
	public String obtenerPuesto(String id, String vac) throws PuestoNoCargadoException, VacunatorioNoCargadoException;
	public ArrayList<String> listarPuestos(String idVac) throws PuestoNoCargadosException;
	public void modificarPuesto(DtPuesto dtPuesto) throws PuestoNoCargadoException;
	public void eliminarPuesto(String id) throws PuestoNoCargadoException;
}
