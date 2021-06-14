package rest;

import java.util.logging.Logger;

import javax.annotation.security.DeclareRoles;
import javax.annotation.security.PermitAll;
import javax.ejb.EJB;
import javax.ws.rs.Consumes;
import javax.ws.rs.CookieParam;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.NotAuthorizedException;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Cookie;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.jose4j.jwt.consumer.InvalidJwtException;

import datatypes.DtEtapa;
import exceptions.AccionInvalida;
import exceptions.EnfermedadInexistente;
import exceptions.EtapaRepetida;
import exceptions.PlanVacunacionInexistente;
import exceptions.VacunaInexistente;
import interfaces.IEnfermedadLocal;
import interfaces.IEtapaLocal;
import interfaces.IPlanVacunacionLocal;
import rest.filter.ResponseBuilder;
import rest.filter.TokenSecurity;

@DeclareRoles({"vacunador", "ciudadano", "administrador", "autoridad"})
@Path("/plan")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class GestionPlanRWS {
	@EJB
	IPlanVacunacionLocal cp;
	
	@EJB
	IEtapaLocal controladorEtapa;
	
	@EJB
	IEnfermedadLocal controladorEnfermedad;
	
	private final Logger LOGGER = Logger.getLogger(getClass().getName());

	public GestionPlanRWS() {

	}
	
	//@RolesAllowed({"autoridad"}) 
	@PermitAll
	@GET
	@Path("/listar")
	public Response listarPlanes(@CookieParam("x-access-token") Cookie cookie) {
		try {
			String token = cookie.getValue();
			String ci = null;
			try {
				ci = TokenSecurity.getIdClaim(TokenSecurity.validateJwtToken(token));
			} catch (InvalidJwtException e) {
				e.printStackTrace();
			}
	        if( ci == null)
	            throw new NotAuthorizedException("No se encuentra CI en token de Cookie - Unauthorized!");
			LOGGER.info("Cedula obtenida en REST: " + ci);
			return Response.ok(cp.listarPlanesVacunacion()).build();
		} catch (PlanVacunacionInexistente e) {
			return ResponseBuilder.createResponse(Response.Status.BAD_REQUEST,
					e.getMessage());
		}
	}
	
	//@RolesAllowed({"autoridad"}) 
	@PermitAll
	@GET
	@Path("/obtener")
	public Response obtenerPlan(@CookieParam("x-access-token") Cookie cookie, @QueryParam("p") String plan) {
		if (plan==null) {
			return ResponseBuilder.createResponse(Response.Status.BAD_REQUEST,
					"No se han ingresado todos los parametros necesarios.");
		}
		try {
			String token = cookie.getValue();
			String ci = null;
			try {
				ci = TokenSecurity.getIdClaim(TokenSecurity.validateJwtToken(token));
			} catch (InvalidJwtException e) {
				e.printStackTrace();
			}
	        if( ci == null)
	            throw new NotAuthorizedException("No se encuentra CI en token de Cookie - Unauthorized!");
			LOGGER.info("Cedula obtenida en REST: " + ci);
			return Response.ok(cp.obtenerPlanVacunacion(Integer.parseInt(plan))).build();
		} catch ( NumberFormatException | PlanVacunacionInexistente e) {
			return ResponseBuilder.createResponse(Response.Status.BAD_REQUEST,
					e.getMessage());
		}
	}
	
	//@RolesAllowed({"autoridad"}) 
	@PermitAll
	@POST
	@Path("/agregar")
	public Response agregarPlan(String datos) {
		try {
			JSONObject datosInterno = new JSONObject(datos);
			cp.agregarPlanVacunacion(datosInterno.getString("nombre"), datosInterno.getString("descripcion"), datosInterno.getString("enfermedad"));
			DtEtapa etapa = (DtEtapa) datosInterno.get("etapa");
			controladorEtapa.agregarEtapa(etapa.getId(), etapa.getFechaInicio(), etapa.getFechaFin(), etapa.getCondicion(), etapa.getPlanVac(), etapa.getVacuna());
			return ResponseBuilder.createResponse(Response.Status.CREATED, "Se ha agregado el plan con exito.");
		} catch ( NumberFormatException | JSONException | EtapaRepetida | PlanVacunacionInexistente | VacunaInexistente | AccionInvalida | EnfermedadInexistente e) {
			return ResponseBuilder.createResponse(Response.Status.BAD_REQUEST,
					e.getMessage());
		}
	}
	
	
	//@RolesAllowed({"autoridad"}) 
	@PermitAll
	@DELETE
	@Path("/eliminar")
	public Response eliminarPlan(@CookieParam("x-access-token") Cookie cookie, @QueryParam("p") String plan) {
		if (plan==null) {
			return ResponseBuilder.createResponse(Response.Status.BAD_REQUEST,
					"No se han ingresado todos los parametros necesarios.");
		}
		try {
			String token = cookie.getValue();
			String ci = null;
			try {
				ci = TokenSecurity.getIdClaim(TokenSecurity.validateJwtToken(token));
			} catch (InvalidJwtException e) {
				e.printStackTrace();
			}
	        if( ci == null)
	            throw new NotAuthorizedException("No se encuentra CI en token de Cookie - Unauthorized!");
			LOGGER.info("Cedula obtenida en REST: " + ci);
			cp.eliminarPlanVacunacion(Integer.parseInt(plan));
			return Response.ok("Se ha eliminado el plan con exito.").build();
		} catch (NumberFormatException | PlanVacunacionInexistente | AccionInvalida e) {
			return ResponseBuilder.createResponse(Response.Status.BAD_REQUEST,
					e.getMessage());
		}
	}
}
