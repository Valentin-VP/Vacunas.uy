package rest;

import java.util.Base64;
import java.util.logging.Logger;

import javax.annotation.security.DeclareRoles;
import javax.annotation.security.RolesAllowed;
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

import interfaces.ILdapLocal;
import interfaces.IUsuarioLocal;
import rest.filter.ResponseBuilder;
import rest.filter.TokenSecurity;

@DeclareRoles({ "vacunador", "ciudadano", "administrador", "autoridad" })
@Path("/usuario")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class GestionUsuariosRWS {

	private final Logger LOGGER = Logger.getLogger(getClass().getName());

	@EJB(lookup = "java:global/grupo15/grupo15-ejb/ControladorUsuario!interfaces.IUsuarioLocal")
	private IUsuarioLocal IUsuarioLocal;

	@EJB
	ILdapLocal l;

	public GestionUsuariosRWS() {
	}

	@RolesAllowed("administrador")
	@GET
	@Path("/cambiarpass")
	public Response cambiarPassword(@CookieParam("x-access-token") Cookie cookie, String hashedpass) {
		try {
			LOGGER.info("Pass hasheada recibida en REST: " + hashedpass);
			String nuevaPass = new String(Base64.getDecoder().decode(hashedpass.getBytes()));
			LOGGER.info("Pass deshasheada  en REST: " + nuevaPass);
			String token = cookie.getValue();
			String ci = null;
			try {
				ci = TokenSecurity.getIdClaim(TokenSecurity.validateJwtToken(token));
			} catch (InvalidJwtException e) {
				return ResponseBuilder.createResponse(Response.Status.BAD_REQUEST, "Error procesando JWT");
			}
			if (ci == null)
				return ResponseBuilder.createResponse(Response.Status.UNAUTHORIZED,
						"No se ha obtenido ci de Cookie/Token");
			LOGGER.info("Cedula obtenida en REST: " + ci);
			l.updateUserPass(ci, nuevaPass);
		} catch (Exception e) {
			return ResponseBuilder.createResponse(Response.Status.BAD_REQUEST, e.getMessage());
		}
		return ResponseBuilder.createResponse(Response.Status.OK, "Password cambiada con exito");

	}

}
