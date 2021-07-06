package rest;

import java.net.URI;
import java.util.Base64;
import java.util.List;
import java.util.StringTokenizer;
import java.util.logging.Logger;

import javax.annotation.security.DeclareRoles;
import javax.annotation.security.PermitAll;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.naming.NamingException;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Cookie;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.NewCookie;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.jose4j.lang.JoseException;

import interfaces.ILdapLocal;
import rest.filter.ResponseBuilder;
import rest.filter.TokenSecurity;

@DeclareRoles({"vacunador", "ciudadano", "administrador", "autoridad"})
@RequestScoped
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Path("/internalauth")
public class AuthInternoRWS{

	@EJB
	ILdapLocal l;
	
	private final Logger LOGGER = Logger.getLogger(getClass().getName());
	
	public AuthInternoRWS() {}

	@PermitAll
	@POST
	@Path("/login")
	public Response autenticarUsuario(@Context HttpHeaders headers) {
		LOGGER.info("Accediendo a AuthInternoRWS");
		// http://wiki.eclipse.org/Tutorial:_Extending_the_JaxRS_Remote_Services_Provider
		//Obtener Headers de Auth
		// Este Header es diferente a los obtenidos en otros REST, no modificar. Es para leer el Basic user:pass
		List<String> authHeaders = headers.getRequestHeader("Authorization");
		if (authHeaders == null) {
			LOGGER.severe("Request does not have Authorization header");
			return ResponseBuilder.createResponse(Response.Status.BAD_REQUEST, "Request does not have Authorization header");
		}
		//Obtener value del header Auth 
		String authHeaderValue = authHeaders.get(0);
		System.out.println(authHeaderValue);
		if (authHeaderValue == null) {
			LOGGER.severe("Request does not have authorization header value");
			return ResponseBuilder.createResponse(Response.Status.BAD_REQUEST, "Request does not have authorization header value");
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
				LOGGER.info("Tipo de Usuario obternido de LDAP en AuthInternoRWS: " + tipo);
				token = TokenSecurity.generateJwtToken(ci, tipo);
			} catch (NamingException | JoseException error) {
				return ResponseBuilder.createResponse(Response.Status.BAD_REQUEST, error.getMessage());
			}
		}
		else {
			// Usuario no existe en LDAP, se retorna 400 con mensaje
			LOGGER.severe("Usuario no existe en LDAP");
			return ResponseBuilder.createResponse(Response.Status.BAD_REQUEST, "Usuario no existe en LDAP");
		}
		Cookie userCookie = new Cookie("x-access-token", token, "/", "");
		NewCookie rwsCookie = new NewCookie(userCookie);

//		String url = "http://" + headers.getHeaderString("Host") + "/grupo15-services/logininterno";
		String url = "localhost/grupo15-services/logininterno";
		URI uri = UriBuilder.fromPath(url).build();
		LOGGER.severe("Uri: " + uri.toString());
		
		Client conexion = ClientBuilder.newClient();
		String loginResponse = conexion.target(uri)
		.request(MediaType.APPLICATION_JSON)
		.cookie(rwsCookie)
		.header("Origin", headers.getHeaderString("Origin"))
		.get(String.class);
		LOGGER.severe("LoginResponse: " + loginResponse);
		try {
			JSONObject respuesta = new JSONObject(loginResponse);
			String urlStatus = respuesta.getString("url");
			if (urlStatus.equals("error")) {
				return ResponseBuilder.createResponse(Response.Status.BAD_REQUEST);
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return Response.ok().cookie(rwsCookie).entity(loginResponse).build();
	}
}
