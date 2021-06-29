package rest;

import java.util.logging.Logger;

import javax.annotation.security.DeclareRoles;
import javax.annotation.security.RolesAllowed;
import javax.ejb.EJB;
import javax.ws.rs.Consumes;
import javax.ws.rs.CookieParam;
import javax.ws.rs.GET;
import javax.ws.rs.NotAuthorizedException;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Cookie;
import javax.ws.rs.core.MediaType;

import org.jose4j.jwt.consumer.InvalidJwtException;

import exceptions.UsuarioInexistente;
import interfaces.IUsuarioLocal;
import rest.filter.TokenSecurity;

@DeclareRoles({"vacunador", "ciudadano", "administrador", "autoridad"})
@Path("/mobile")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class LoginMobileDemo {

	//private static final long serialVersionUID = 1L;
	private final Logger LOGGER = Logger.getLogger(getClass().getName());
	@EJB
	private IUsuarioLocal IUsuarioLocal;
	public LoginMobileDemo() {}
	
	@GET
	@Path("/id")
	@RolesAllowed({"ciudadano"})
	public String getCedulaJWT(@CookieParam("x-access-token") Cookie cookie) {
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
		LOGGER.info("Recuperando CI de JWT en Header: " + ci);
		try {
			return IUsuarioLocal.buscarCiudadano(Integer.parseInt(ci)).getNombre();
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UsuarioInexistente e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

}
