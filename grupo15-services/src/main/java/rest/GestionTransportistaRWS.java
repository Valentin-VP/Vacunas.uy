package rest;

import java.util.logging.Logger;

import javax.annotation.security.DeclareRoles;
import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.ejb.EJB;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import exceptions.AccionInvalida;
import exceptions.TransportistaInexistente;
import exceptions.TransportistaRepetido;
import interfaces.ITransportistaDaoLocal;
import rest.filter.ResponseBuilder;
@DeclareRoles({ "vacunador", "ciudadano", "administrador", "autoridad" })
@Path("/transportistas")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class GestionTransportistaRWS {
private final Logger LOGGER = Logger.getLogger(getClass().getName());
	
	@EJB
	ITransportistaDaoLocal ct;
	
	public GestionTransportistaRWS() {}
	
	@RolesAllowed({"autoridad"}) 
	@PermitAll
    @GET
    @Path("/listar")
    public Response listarTransportistas() {
    	try {
			return Response.ok().entity(ct.listarTransportistas()).build();
		} catch (TransportistaInexistente e) {
			return ResponseBuilder.createResponse(Response.Status.BAD_REQUEST,
                    e.getMessage());
		}
	}
	
	@RolesAllowed({"autoridad"}) 
	@PermitAll
	@POST
	@Path("/agregar")
	public Response agregarTransportista(String datos) {
		try {
			JSONObject datosInterno = new JSONObject(datos);
			ct.agregarTransportista(Integer.valueOf(datosInterno.getString("id")), datosInterno.getString("url"));
			ct.generarTokenTransportista(Integer.valueOf(datosInterno.getString("id")));
			return ResponseBuilder.createResponse(Response.Status.CREATED, "Se ha agregado el nodo Transportista con exito.");
		} catch ( NumberFormatException | JSONException | TransportistaRepetido | TransportistaInexistente | AccionInvalida  e) {
			return ResponseBuilder.createResponse(Response.Status.BAD_REQUEST,
					e.getMessage());
		}
	}
	
	//@RolesAllowed({"autoridad"}) 
//	@PermitAll
//	@POST
//	@Path("/modificar")
//	public Response modificarTransportista(String datos) {
//		try {
//			JSONObject datosInterno = new JSONObject(datos);
//			ct.setURLtoTransportista(Integer.valueOf(datosInterno.getString("idTransportista")), datosInterno.getString("url"));
//			return ResponseBuilder.createResponse(Response.Status.OK, "Se ha modificado el transportista con exito.");
//		} catch ( NumberFormatException | JSONException | TransportistaInexistente  e) {
//			return ResponseBuilder.createResponse(Response.Status.BAD_REQUEST,
//					e.getMessage());
//		}
//	}
}
