package soap;

import java.util.ArrayList;

import javax.ejb.EJB;
import javax.jws.WebMethod;
import javax.jws.WebService;

import datatypes.DtVacunador;
import exceptions.PuestoNoCargadosException;
import interfaces.IControladorPuestoLocal;
import interfaces.IUsuarioLocal;

@WebService
public class NodoVac {
	
	@EJB
	IUsuarioLocal cu;
	
	@EJB
	IControladorPuestoLocal cp;
	
	@WebMethod
	public ArrayList<String> listarPuestos(String idVac) throws PuestoNoCargadosException {
		return cp.listarPuestos(idVac);
	}
	
	@WebMethod
	public ArrayList<DtVacunador> listarVacunadores() throws PuestoNoCargadosException {
		return cu.listarVacunadores();
	}
	
}
