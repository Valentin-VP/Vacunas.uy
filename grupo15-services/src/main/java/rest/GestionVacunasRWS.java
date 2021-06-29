package rest;

import java.util.logging.Logger;

import javax.annotation.security.DeclareRoles;
import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.ejb.EJB;
import javax.ws.rs.Consumes;
import javax.ws.rs.CookieParam;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.NotAuthorizedException;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Cookie;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.jose4j.jwt.consumer.InvalidJwtException;

import datatypes.DtDatosVacuna;
import datatypes.ErrorInfo;
import exceptions.AccionInvalida;
import exceptions.EnfermedadInexistente;
import exceptions.LaboratorioInexistente;
import exceptions.VacunaInexistente;
import exceptions.VacunaRepetida;
import interfaces.IControladorVacunaLocal;
import rest.filter.ResponseBuilder;
import rest.filter.TokenSecurity;

@DeclareRoles({"vacunador", "ciudadano", "administrador", "autoridad"})
@Path("/vacunas")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class GestionVacunasRWS {
	@EJB
	IControladorVacunaLocal cv;
	private final Logger LOGGER = Logger.getLogger(getClass().getName());
	public GestionVacunasRWS() {
		// TODO Auto-generated constructor stub
	}
	
	@RolesAllowed({ "administrador", "autoridad" })
	@GET
	@Path("/listar")
	public Response listarVacunas(@CookieParam("x-access-token") Cookie cookie) {
		try {
			return Response.ok(cv.listarVacunas()).build();
		} catch (VacunaInexistente e) {
			return ResponseBuilder.createResponse(Response.Status.BAD_REQUEST,
					e.getMessage());
		}
	}
	
	@PermitAll
	@GET
	@Path("/obtener")
	public Response obtenerVacuna(@CookieParam("x-access-token") Cookie cookie, @QueryParam("vac") String vacuna) {
		if (vacuna==null) {
			return ResponseBuilder.createResponse(Response.Status.BAD_REQUEST,
					"No se ha recibido vacuna");
		}
		try {
			String token = cookie.getValue();
			String ci = null;
			try {
				ci = TokenSecurity.getIdClaim(TokenSecurity.validateJwtToken(token));
			} catch (InvalidJwtException e) {
				return ResponseBuilder.createResponse(Response.Status.BAD_REQUEST,
						e.getMessage());
			}
	        if( ci == null)
	        	return ResponseBuilder.createResponse(Response.Status.UNAUTHORIZED,
	        			"No se encuentra CI en token de Cookie - Unauthorized!");
			LOGGER.info("Cedula obtenida en REST: " + ci);
			return Response.ok(cv.obtenerVacuna(vacuna)).build();
		} catch (VacunaInexistente e) {
			return ResponseBuilder.createResponse(Response.Status.BAD_REQUEST,
					e.getMessage());
		}
	}
	
	@RolesAllowed({"autoridad"}) 
	@POST
	@Path("/agregar")
	public Response agregarVacuna(@CookieParam("x-access-token") Cookie cookie, DtDatosVacuna dtv) {
		if (dtv==null || dtv.getNombre()==null || dtv.getCantDosis()==null ||dtv.getTiempoEntreDosis()==null ||dtv.getExpira()==null ||dtv.getLaboratorio()==null ||dtv.getEnfermedad()==null ) {
			return ResponseBuilder.createResponse(Response.Status.BAD_REQUEST,
					"Se ha recibido un valor nulo");
		}
		try {
			String token = cookie.getValue();
			String ci = null;
			try {
				ci = TokenSecurity.getIdClaim(TokenSecurity.validateJwtToken(token));
			} catch (InvalidJwtException e) {
				return ResponseBuilder.createResponse(Response.Status.BAD_REQUEST,
						e.getMessage());
			}
	        if( ci == null)
	        	return ResponseBuilder.createResponse(Response.Status.UNAUTHORIZED,
	        			"No se encuentra CI en token de Cookie - Unauthorized!");
			LOGGER.info("Cedula obtenida en REST: " + ci);
			cv.agregarVacuna(dtv.getNombre(), Integer.parseInt(dtv.getCantDosis()), Integer.parseInt(dtv.getTiempoEntreDosis()), Integer.parseInt(dtv.getExpira()), dtv.getLaboratorio(), dtv.getEnfermedad());
			return ResponseBuilder.createResponse(Response.Status.CREATED, "Se ha creado la vacuna");
		} catch (NumberFormatException | VacunaRepetida | LaboratorioInexistente | EnfermedadInexistente e) {
			return ResponseBuilder.createResponse(Response.Status.BAD_REQUEST,
					e.getMessage());
		}
	}
	
	@RolesAllowed({"autoridad"}) 
	@DELETE
	@Path("/eliminar")
	public Response eliminarVacuna(@QueryParam("vac") String vacuna) {
		try {
			cv.eliminarVacuna(vacuna);
			return ResponseBuilder.createResponse(Response.Status.OK, "Se ha eliminado la vacuna");
		} catch (AccionInvalida | VacunaInexistente e) {
			System.out.println(e.getMessage());
			return ResponseBuilder.createResponse(Response.Status.BAD_REQUEST,
					e.getMessage());
		}
	}
	
	@RolesAllowed({ "autoridad", "administrador" }) 
	@POST
	@Path("/modificar")
	public Response modificarVacuna(String vacuna) {
		String nombre = "";
		int cantDosis = -1;
		int expira = -1;
		int tiempoEntreDosis = -1;
		String lab = "";
		String enf = "";
		try {
			JSONObject jsonObject = new JSONObject(vacuna);
			nombre = jsonObject.getString("nombre");
			cantDosis = Integer.valueOf(jsonObject.getString("cantDosis"));
			expira = Integer.valueOf(jsonObject.getString("expira"));
			tiempoEntreDosis = Integer.valueOf(jsonObject.getString("tiempoEntreDosis"));
			lab = jsonObject.getString("laboratorio");
			enf = jsonObject.getString("enfermedad");
			cv.modificarVacuna(nombre, cantDosis, expira, tiempoEntreDosis, lab, enf);
			return ResponseBuilder.createResponse(Response.Status.OK, "Se ha modificado la vacuna.");
		} catch (JSONException | VacunaInexistente | LaboratorioInexistente | EnfermedadInexistente | AccionInvalida e) {
			return ResponseBuilder.createResponse(Response.Status.BAD_REQUEST, e.getMessage());
		}
	}
}
