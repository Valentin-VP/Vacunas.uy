package soap;

import java.util.ArrayList;

import javax.ejb.EJB;
import javax.jws.WebMethod;
import javax.jws.WebService;

import datatypes.TransportistaInexistente;
import exceptions.AccionInvalida;
import interfaces.IMensajeLocal;
import interfaces.ITransportistaDaoLocal;

@WebService(name = "transService", targetNamespace = "webservice/transService")
public class NodoTrans {
	
	@EJB
	IMensajeLocal cm;
	@EJB
	ITransportistaDaoLocal ct;
	
	@WebMethod(action = "escuchar", operationName = "escucharMensajeSocio")
	public void escucharMensajeSocio(String mensaje, String idTransportista, String token) throws AccionInvalida, TransportistaInexistente {
		if (!ct.isTokenCorrecto(Integer.valueOf(idTransportista), token))
			throw new AccionInvalida("Fallo al identificar nodo.");
		cm.agregarMensaje(mensaje);
	}
	
	@WebMethod(action = "escuchar", operationName = "escucharMensajesSocio")
	public void escucharMensajesSocio(ArrayList<String> mensaje, String idTransportista, String token) throws TransportistaInexistente, AccionInvalida {
		if (!ct.isTokenCorrecto(Integer.valueOf(idTransportista), token))
			throw new AccionInvalida("Fallo al identificar nodo.");
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
