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
import javax.ws.rs.core.Response.Status;

import org.jose4j.jwt.consumer.InvalidJwtException;

import datatypes.ErrorInfo;
import exceptions.AccionInvalida;
import exceptions.LaboratorioInexistente;
import exceptions.LaboratorioRepetido;
import interfaces.ILaboratorioLocal;
import rest.filter.ResponseBuilder;
import rest.filter.TokenSecurity;

@DeclareRoles({"vacunador", "ciudadano", "administrador", "autoridad"})
@Path("/lab")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class GestionLaboratorioRWS {
	@EJB
	ILaboratorioLocal cl;
	private final Logger LOGGER = Logger.getLogger(getClass().getName());
	public GestionLaboratorioRWS() {
		// TODO Auto-generated constructor stub
	}
	
	//@RolesAllowed({"autoridad"}) 
		@PermitAll
		@GET
		@Path("/listar")
		public Response listarLaboratorio(@CookieParam("x-access-token") Cookie cookie) {
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
				return Response.ok(cl.listarLaboratorios()).build();
			} catch (LaboratorioInexistente e) {
				return ResponseBuilder.createResponse(Response.Status.BAD_REQUEST,
						e.getMessage());
			}
		}
		
		//@RolesAllowed({"autoridad"}) 
		@PermitAll
		@GET
		@Path("/obtener")
		public Response obtenerLaboratorio(@CookieParam("x-access-token") Cookie cookie, @QueryParam("lab") String lab) {
			if (lab==null) {
				return ResponseBuilder.createResponse(Response.Status.BAD_REQUEST,
						"No se ha recibido laboratorio");
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
				return Response.ok(cl.obtenerLaboratorio(lab)).build();
			} catch ( LaboratorioInexistente e) {
				return ResponseBuilder.createResponse(Response.Status.BAD_REQUEST,
						e.getMessage());
			}
		}
		
		//@RolesAllowed({"autoridad"}) 
		@PermitAll
		@POST
		@Path("/agregar")
		public Response agregarLaboratorio(@CookieParam("x-access-token") Cookie cookie, String lab) {
			if (lab==null) {
				return ResponseBuilder.createResponse(Response.Status.BAD_REQUEST,
						"No se ha recibido el laboratorio");
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
				cl.agregarLaboratorio(lab);
				return ResponseBuilder.createResponse(Response.Status.CREATED, "Se ha creado el laboratorio");
			} catch (LaboratorioRepetido e) {
				return ResponseBuilder.createResponse(Response.Status.BAD_REQUEST,
						e.getMessage());
			}
		}
		
		//@RolesAllowed({"autoridad"}) 
		@PermitAll
		@DELETE
		@Path("/eliminar")
		public Response eliminarLaboratorio(@CookieParam("x-access-token") Cookie cookie, @QueryParam("lab") String lab) {
			if (lab==null) {
				return ResponseBuilder.createResponse(Response.Status.BAD_REQUEST,
						"No se ha recibido laboratorio");
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
		            throw new NotAuthorizedException("No se encuentra CI en token de Cookie - Unauthorized!");
				LOGGER.info("Cedula obtenida en REST: " + ci);
				cl.eliminarLaboratorio(lab);
				return ResponseBuilder.createResponse(Response.Status.CREATED, "Se ha eliminado el laboratorio");
			} catch (AccionInvalida | LaboratorioInexistente e) {
				return ResponseBuilder.createResponse(Response.Status.BAD_REQUEST,
						e.getMessage());
			}
		}
}
