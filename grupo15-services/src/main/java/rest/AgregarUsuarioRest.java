package rest;

import java.io.Serializable;
import java.util.Base64;
import java.util.List;
import java.util.StringTokenizer;
import java.util.logging.Logger;

import javax.annotation.security.PermitAll;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.naming.NamingException;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.jose4j.lang.JoseException;

import datatypes.DtLdap;
import interfaces.ILdapLocal;
import rest.filter.TokenSecurity;

@SessionScoped
@Path("/agregarUsuario")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class AgregarUsuarioRest implements Serializable {

	private static final long serialVersionUID = 1L;

	@EJB
	ILdapLocal l;

	public AgregarUsuarioRest() {
		// TODO Auto-generated constructor stub
	}

	private final Logger LOGGER = Logger.getLogger(getClass().getName());
	
	@PermitAll
	@POST
	@Path("/add")
	public Response agregarUsuario(@Context HttpHeaders headers, DtLdap dt) {
		LOGGER.info("Accediendo a AgregarUsuarioRest");

		List<String> authHeaders = headers.getRequestHeader("Authorization");
		if (authHeaders == null) {
			throw new IllegalArgumentException("Request does not have Authorization header");
		}

		// Obtener value del header Auth
		String authHeaderValue = authHeaders.get(0);
		System.out.println(authHeaderValue);
		if (authHeaderValue == null) {
			throw new IllegalArgumentException("Request does not have authorization header value");
		}

		StringTokenizer tokenizer = new StringTokenizer(
				new String(Base64.getDecoder().decode(authHeaderValue.replaceFirst("Basic ", "").getBytes())), ":");
		String ci = tokenizer.nextToken();
		String password = tokenizer.nextToken();
		System.out.println(ci);
		System.out.println(password);
		l.addUser(dt.getApellido(), Integer.parseInt(ci), dt.getNombre(), dt.getTipoUser(), password);
		
		return Response.ok().build();
	}

}
