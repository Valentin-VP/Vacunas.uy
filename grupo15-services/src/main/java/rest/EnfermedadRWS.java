package rest;

import java.util.logging.Logger;

import javax.annotation.security.DeclareRoles;
import javax.annotation.security.PermitAll;
import javax.ejb.EJB;
import javax.ws.rs.Consumes;
import javax.ws.rs.CookieParam;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Cookie;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.jose4j.jwt.consumer.InvalidJwtException;

import datatypes.ErrorInfo;
import exceptions.AccionInvalida;
import exceptions.EnfermedadInexistente;
import exceptions.EnfermedadRepetida;
import interfaces.IEnfermedadLocal;
import rest.filter.ResponseBuilder;
import rest.filter.TokenSecurity;

@DeclareRoles({"vacunador", "ciudadano", "administrador", "autoridad"})
@Path("/enfermedad")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class EnfermedadRWS {
	@EJB
	IEnfermedadLocal ce;
	
	private final Logger LOGGER = Logger.getLogger(getClass().getName());

	public EnfermedadRWS() {
		// TODO Auto-generated constructor stub
	}
	
	//@RolesAllowed({"autoridad"}) 
	@PermitAll
	@GET
	@Path("/listar")
	public Response listarEnfermedades(@CookieParam("x-access-token") Cookie cookie) {
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
			return Response.ok(ce.listarEnfermedades()).build();
		} catch (EnfermedadInexistente e) {
			return ResponseBuilder.createResponse(Response.Status.BAD_REQUEST,
					e.getMessage());
		}
	}
	
	//@RolesAllowed({"autoridad"}) 
	@PermitAll
	@GET
	@Path("/obtener")
	public Response obtenerEnfermedad(@CookieParam("x-access-token") Cookie cookie, @QueryParam("enf") String enfermedad) {
		if (enfermedad==null) {
			return ResponseBuilder.createResponse(Response.Status.BAD_REQUEST,
					"No se ha recibido enfermedad");
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
			return Response.ok(ce.obtenerEnfermedad(enfermedad)).build();
		} catch (EnfermedadInexistente e) {
			return ResponseBuilder.createResponse(Response.Status.BAD_REQUEST,
					e.getMessage());
		}
	}
	
	//@RolesAllowed({"autoridad"}) 
	@PermitAll
	@POST
	@Path("/agregar")
	public Response agregarEnfermedad(@CookieParam("x-access-token") Cookie cookie, String enfermedad) {
		LOGGER.info("Accediento a REST agregarEnfermedad");
		if (enfermedad==null) {
			return ResponseBuilder.createResponse(Response.Status.BAD_REQUEST,
					"No se ha recibido enfermedad");
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
			ce.agregarEnfermedad(enfermedad);
			return ResponseBuilder.createResponse(Response.Status.CREATED, "Se ha creado la enfermedad");
		} catch (EnfermedadRepetida e) {
			return ResponseBuilder.createResponse(Response.Status.BAD_REQUEST,
					e.getMessage());
		}
	}
	
	//@RolesAllowed({"autoridad"}) 
	@PermitAll
	@DELETE
	@Path("/eliminar")
	public Response eliminarEnfermedad(@CookieParam("x-access-token") Cookie cookie, @QueryParam("enf") String enfermedad) {
		if (enfermedad==null) {
			return ResponseBuilder.createResponse(Response.Status.BAD_REQUEST,
					"No se ha recibido enfermedad");
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
			ce.eliminarEnfermedad(enfermedad);
			return ResponseBuilder.createResponse(Response.Status.OK);
		} catch (EnfermedadInexistente | AccionInvalida e) {
			return ResponseBuilder.createResponse(Response.Status.BAD_REQUEST,
					e.getMessage());
		}
	}
}
