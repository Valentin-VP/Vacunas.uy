package rest;

import java.util.logging.Logger;

import javax.annotation.security.DeclareRoles;
import javax.annotation.security.PermitAll;
import javax.ejb.EJB;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.xml.ws.WebServiceException;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import entities.Vacuna;
import entities.Vacunatorio;
import exceptions.LoteRepetido;
import exceptions.VacunaInexistente;
import exceptions.VacunatorioNoCargadoException;
import interfaces.ILoteDosisDaoLocal;
import rest.filter.ResponseBuilder;
import soap.client.AccionInvalida_Exception;
import soap.client.NodoCentralService;

@DeclareRoles({"vacunador", "ciudadano", "administrador", "autoridad"})
@Path("/lotedosis")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class GestionLoteDosisRWS {
	private final Logger LOGGER = Logger.getLogger(getClass().getName());

	@EJB
	ILoteDosisDaoLocal cld;
	
	public GestionLoteDosisRWS() {
		
	}
	
	@PermitAll
	@POST
	@Path("/agregar")
	public Response crearLoteDosis(String datos) {
		try {
			JSONObject lote = new JSONObject(datos);
			//Integer Integer idLote, String idVacunatorio, String idVacuna, Integer cantidadTotal, float temperatura
			cld.agregarLoteDosis(Integer.getInteger(lote.getString("idLote")), lote.getString("idVacunatorio"), lote.getString("idVacuna"), Integer.getInteger(lote.getString("cantidadTotal"))); 
			try {
				NodoCentralService socio = new NodoCentralService();
				socio.getNodoCentralPort().recibirNuevoLote(lote.getString("idLote"), lote.getString("idVacunatorio"), lote.getString("idVacuna"), "0");
			}catch(WebServiceException w) {
				
			} catch (AccionInvalida_Exception e) {
				
			}
			
		} catch (JSONException | NumberFormatException | LoteRepetido | VacunatorioNoCargadoException | VacunaInexistente e) {
			return ResponseBuilder.createResponse(Response.Status.BAD_REQUEST,
					e.getMessage());
		}
		
		
		
        //@WebParam(name = "idLote") String idLote, @WebParam(name = "idVacunatorio") String idVacunatorio, @WebParam(name = "idVacuna") String idVacuna,
		//@WebParam(name = "cantidadTotal") String cantidadTotal
        
		return null;
	}
}
