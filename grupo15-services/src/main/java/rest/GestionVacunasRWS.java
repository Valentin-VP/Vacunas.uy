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
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Cookie;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.Response.Status;

import org.jose4j.jwt.consumer.InvalidJwtException;

import datatypes.DtDatosVacuna;
import datatypes.ErrorInfo;
import exceptions.AccionInvalida;
import exceptions.EnfermedadInexistente;
import exceptions.EnfermedadRepetida;
import exceptions.LaboratorioInexistente;
import exceptions.VacunaInexistente;
import exceptions.VacunaRepetida;
import interfaces.IControladorVacunaLocal;
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
	
	//@RolesAllowed({"autoridad"}) 
	@PermitAll
	@GET
	@Path("/listar")
	public Response listarVacunas(@CookieParam("x-access-token") Cookie cookie) {
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
			return Response.ok(cv.listarVacunas()).build();
		} catch (VacunaInexistente e) {
			return Response.serverError().entity(new ErrorInfo(200, e.getMessage())).status(200).build();
		}
	}
	
	//@RolesAllowed({"autoridad"}) 
	@PermitAll
	@GET
	@Path("/obtener")
	public Response obtenerVacuna(@CookieParam("x-access-token") Cookie cookie, @QueryParam("vac") String vacuna) {
		if (vacuna==null) {
			ResponseBuilder rb = Response.status(Status.BAD_REQUEST);
			return rb.build();
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
			return Response.ok(cv.obtenerVacuna(vacuna)).build();
		} catch (VacunaInexistente e) {
			return Response.serverError().entity(new ErrorInfo(200, e.getMessage())).status(200).build();
		}
	}
	
	//@RolesAllowed({"autoridad"}) 
	@PermitAll
	@POST
	@Path("/agregar")
	public Response agregarVacuna(@CookieParam("x-access-token") Cookie cookie, DtDatosVacuna dtv) {
		if (dtv==null || dtv.getNombre()==null || dtv.getCantDosis()==null ||dtv.getTiempoEntreDosis()==null ||dtv.getExpira()==null ||dtv.getLaboratorio()==null ||dtv.getEnfermedad()==null ) {
			ResponseBuilder rb = Response.status(Status.BAD_REQUEST);
			return rb.build();
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
			cv.agregarVacuna(dtv.getNombre(), Integer.parseInt(dtv.getCantDosis()), Integer.parseInt(dtv.getTiempoEntreDosis()), Integer.parseInt(dtv.getExpira()), dtv.getLaboratorio(), dtv.getEnfermedad());;
			return Response.ok().build();
		} catch (NumberFormatException | VacunaRepetida | LaboratorioInexistente | EnfermedadInexistente e) {
			return Response.serverError().entity(new ErrorInfo(200, e.getMessage())).status(200).build();
		}
	}
	
	//@RolesAllowed({"autoridad"}) 
	@PermitAll
	@DELETE
	@Path("/eliminar")
	public Response eliminarEnfermedad(@CookieParam("x-access-token") Cookie cookie, @QueryParam("vac") String vacuna) {
		if (vacuna==null) {
			ResponseBuilder rb = Response.status(Status.BAD_REQUEST);
			return rb.build();
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
			cv.eliminarVacuna(vacuna);
			return Response.ok().build();
		} catch (AccionInvalida | VacunaInexistente e) {
			return Response.serverError().entity(new ErrorInfo(200, e.getMessage())).status(200).build();
		}
	}
}
