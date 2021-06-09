package rest;

import java.util.logging.Logger;

import javax.annotation.security.DeclareRoles;
import javax.annotation.security.PermitAll;
import javax.ejb.EJB;
import javax.ws.rs.Consumes;
import javax.ws.rs.CookieParam;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Cookie;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.jose4j.jwt.consumer.InvalidJwtException;

import exceptions.UsuarioInexistente;
import interfaces.IUsuarioLocal;
import rest.filter.ResponseBuilder;
import rest.filter.TokenSecurity;

@DeclareRoles({"vacunador", "ciudadano", "administrador", "autoridad"})
@Path("/chat")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class AccesoChatRWS {

	private final Logger LOGGER = Logger.getLogger(getClass().getName());
	@EJB(lookup = "java:global/grupo15/grupo15-ejb/ControladorUsuario!interfaces.IUsuarioLocal")
	private IUsuarioLocal IUsuarioLocal;
	
	public AccesoChatRWS() {}
	
	@GET
	@Path("/vacunador")
	@PermitAll
	//@RolesAllowed({"vacunador"})
	public Response getCedulaJWT(@CookieParam("x-access-token") Cookie cookie) {
		String token = cookie.getValue();
		String ci = null;
		String tipoUsuario = null;
		String nombre = null;
		try {
			ci = TokenSecurity.getIdClaim(TokenSecurity.validateJwtToken(token));
			tipoUsuario = TokenSecurity.getTipoUsuarioClaim(TokenSecurity.validateJwtToken(token)).toLowerCase();
		} catch (InvalidJwtException e) {
			return ResponseBuilder.createResponse(Response.Status.BAD_REQUEST,
					e.getMessage());
		}
        if( ci == null || tipoUsuario != "vacunador")
        	return ResponseBuilder.createResponse(Response.Status.BAD_REQUEST,
        			"No se encuentra CI en token de Cookie o el usuario no es vacunador - Unauthorized!");
		LOGGER.info("Cedula obtenida en REST: " + ci);
		try {
			nombre = IUsuarioLocal.buscarVacunador(Integer.parseInt(ci)).getNombre();
		} catch (NumberFormatException e) {
			return ResponseBuilder.createResponse(Response.Status.BAD_REQUEST,
					e.getMessage());
		} catch (UsuarioInexistente e) {
			return ResponseBuilder.createResponse(Response.Status.BAD_REQUEST,
					e.getMessage());
		}
		return ResponseBuilder.createResponse(Response.Status.OK, nombre);
	}

}
