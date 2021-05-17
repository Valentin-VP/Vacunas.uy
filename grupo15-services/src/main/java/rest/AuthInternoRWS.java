package rest;

import java.io.Serializable;
import java.util.Base64;
import java.util.List;
import java.util.StringTokenizer;

import javax.annotation.security.PermitAll;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.naming.NamingException;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.jose4j.lang.JoseException;

import interfaces.ILdapLocal;
import rest.filter.TokenSecurity;

@SessionScoped
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Path("/internalauth")
public class AuthInternoRWS implements Serializable{

	private static final long serialVersionUID = 1L;

	@EJB
	ILdapLocal l;
	
	public AuthInternoRWS() {}

	@POST
	@Path("/login")
	@PermitAll
	public Response autenticarUsuario(@Context HttpHeaders headers) {
		// http://wiki.eclipse.org/Tutorial:_Extending_the_JaxRS_Remote_Services_Provider
		//Obtener Headers de Auth
		List<String> authHeaders = headers.getRequestHeader("Authorization");
		if (authHeaders == null) {
			throw new IllegalArgumentException("Request does not have Authorization header");
		}
		//Obtener value del header Auth 
		String authHeaderValue = authHeaders.get(0);
		if (authHeaderValue == null) {
			throw new IllegalArgumentException("Request does not have authorization header value");
		}
		// Decodificar considerando value: Basic <encoded_username>:<encoded_password>
		StringTokenizer tokenizer = new StringTokenizer(new String(
				Base64.getDecoder().decode(authHeaderValue.replaceFirst("Basic ", "").getBytes())),
				":");
		String ci = tokenizer.nextToken();
		String password = tokenizer.nextToken();
		Boolean isok;
		String token = null;
		isok = l.authUser(ci, password);
		if (isok) {
			String tipo;
			try {
				tipo = l.searchType(ci);
				token = TokenSecurity.generateJwtToken(ci, tipo);
			} catch (NamingException e1) {
				e1.printStackTrace();
			} catch (JoseException e) {
				e.printStackTrace();
			}
		}
		return Response.status(Response.Status.OK).header(HttpHeaders.LOCATION, "/grupo15-services/logininterno")
				.header("token", token).build();
	}
}
