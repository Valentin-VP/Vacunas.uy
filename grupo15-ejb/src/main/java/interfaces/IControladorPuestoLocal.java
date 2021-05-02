
package interfaces;

import java.util.ArrayList;


import javax.ejb.Local;

import datatypes.DtPuesto;
import entities.Vacunatorio;
import exceptions.PuestoCargadoException;
import exceptions.PuestoNoCargadoException;
import exceptions.PuestoNoCargadosException;

@Local
public interface IControladorPuestoLocal {

	public void agregarPuesto(String id, Vacunatorio vacunatorio) throws PuestoCargadoException;
	public DtPuesto obtenerPuesto(String id) throws PuestoNoCargadoException;
	public ArrayList<DtPuesto> listarPuestos()throws PuestoNoCargadosException;
	public void modificarPuesto(DtPuesto dtPuesto) throws PuestoNoCargadoException;
	public void eliminarPuesto(String id) throws PuestoNoCargadoException;
}
