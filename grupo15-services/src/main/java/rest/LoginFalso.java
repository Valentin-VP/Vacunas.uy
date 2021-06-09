package rest;

import java.util.logging.Logger;

import javax.annotation.security.DeclareRoles;
import javax.annotation.security.PermitAll;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.jose4j.lang.JoseException;

import exceptions.UsuarioExistente;
import interfaces.IUsuarioLocal;
import rest.filter.ResponseBuilder;
import rest.filter.TokenSecurity;

@DeclareRoles({ "vacunador", "ciudadano", "administrador", "autoridad" })
@RequestScoped
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Path("/login")
public class LoginFalso {
	
	private final Logger LOGGER = Logger.getLogger(getClass().getName());
	
	@EJB(lookup = "java:global/grupo15/grupo15-ejb/ControladorUsuario!interfaces.IUsuarioLocal")
	private IUsuarioLocal IUsuarioLocal;

	public LoginFalso() {}

	@GET
	@Path("/fake")
	@PermitAll
	public Response getToken(@QueryParam("ci") String ci) {
		try {
			IUsuarioLocal.agregarUsuarioCiudadano(Integer.parseInt(ci), null, null, null, null, null, null, null, false);
			String token = TokenSecurity.generateJwtToken(ci, "ciudadano");
			return ResponseBuilder.createResponse(Response.Status.OK, token);
		} catch (NumberFormatException | UsuarioExistente | JoseException e) {
			return ResponseBuilder.createResponse(Response.Status.BAD_REQUEST, "");
		}
	}
}
