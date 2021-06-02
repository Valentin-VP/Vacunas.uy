package rest;

import java.util.Base64;
import java.util.List;
import java.util.logging.Logger;

import javax.annotation.security.DeclareRoles;
import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.ejb.EJB;
import javax.ws.rs.Consumes;
import javax.ws.rs.CookieParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.Cookie;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.codehaus.jettison.json.JSONObject;
import org.jose4j.jwt.consumer.InvalidJwtException;

import datatypes.DtUsuarioExterno;
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

	@RolesAllowed({ "administrador", "autoridad" })
	@POST
	@Path("/cambiarpass")
	public Response cambiarPassword(@CookieParam("x-access-token") Cookie cookie, String hashedpass) {
		try {
			JSONObject jsonObject = new JSONObject(hashedpass);
			LOGGER.info("Pass hasheada recibida en REST: " + jsonObject.getString("pass"));
			String nuevaPass = new String(Base64.getDecoder().decode(jsonObject.getString("pass")));
			// String nuevaPass = new
			// String(Base64.getDecoder().decode(hashedpass.getBytes()));
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
		} catch (

		Exception e) {
			return ResponseBuilder.createResponse(Response.Status.BAD_REQUEST, e.getMessage());
		}
		return ResponseBuilder.createResponse(Response.Status.OK, "Password cambiada con exito");

	}
	
	//@RolesAllowed({ "administrador", "autoridad" })
	@PermitAll
	@GET
	@Path("/external")
	public Response obtenerDatosExternos() {
		String url = "https://rcastro.pythonanywhere.com/api/usuarios/";
		Client conexion = ClientBuilder.newClient();
		List<DtUsuarioExterno> resultados = conexion.target(url)
				.request(MediaType.APPLICATION_JSON)
				.get(new GenericType<List<DtUsuarioExterno>> () {});
		return Response.ok(resultados).build();
	}
	
	@PermitAll
	@GET
	@Path("/external/{ci}")
	public Response obtenerDatosExternos(@PathParam("ci") String ci) {
		LOGGER.info("Cedula obtenida en REST: " + ci);
		String url = "https://rcastro.pythonanywhere.com/api/usuarios/" + ci + "/";
		LOGGER.info("Ejecutando call REST: " + url);
		Client conexion = ClientBuilder.newClient();
		DtUsuarioExterno resultados = conexion.target(url)
				.request(MediaType.APPLICATION_JSON)
				.get(DtUsuarioExterno.class);
		LOGGER.info("Obtenido usuario: " + resultados.getCi());
		return Response.ok(resultados).build();
	}

}
