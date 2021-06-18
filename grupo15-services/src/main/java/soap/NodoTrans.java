package soap;

import java.util.ArrayList;

import javax.ejb.EJB;
import javax.jws.WebMethod;
import javax.jws.WebService;

import interfaces.IMensajeLocal;

@WebService(name = "transService", targetNamespace = "http://localhost:8080/grupo15-services/webservice/transService")
public class NodoTrans {
	
	@EJB
	IMensajeLocal cm;
	
	@WebMethod(action = "escuchar", operationName = "escucharMensajeSocio")
	public void escucharMensajeSocio(String mensaje) {
		cm.agregarMensaje(mensaje);
	}
	
	@WebMethod(action = "escuchar", operationName = "escucharMensajesSocio")
	public void escucharMensajesSocio(ArrayList<String> mensaje) {
		for (String s: mensaje)
			cm.agregarMensaje(s);
	}
	
	/*
	@WebMethod(action = "escuchar", operationName = "escucharEstadoLoteDosis")
	public void escucharEstadoLoteDosis(String mensaje) {
		cm.agregarMensaje(mensaje);
	}

	@WebMethod(action = "escuchar", operationName = "escucharTodosEstadoLoteDosis")
	public void escucharTodosEstadoLoteDosis(ArrayList<String> mensaje) {
		for (String s: mensaje)
			cm.agregarMensaje(s);
	}
	
	@WebMethod(action = "escuchar", operationName = "escucharPerderStockLoteDosis")
	public void escucharPerderStockLoteDosis(String mensaje) {
		cm.agregarMensaje(mensaje);
	}
	
	@WebMethod(action = "escuchar", operationName = "escucharTemperaturaCriticaLoteDosis")
	public void escucharTemperaturaCriticaLoteDosis(String mensaje) {
		cm.agregarMensaje(mensaje);
	}*/
}
