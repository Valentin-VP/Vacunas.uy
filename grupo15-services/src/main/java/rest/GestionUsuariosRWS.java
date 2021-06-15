package rest;

import java.time.LocalDate;
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

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.jose4j.jwt.consumer.InvalidJwtException;

import datatypes.DtCiudadano;
import datatypes.DtDireccion;
import datatypes.DtUsuarioExterno;
import datatypes.DtUsuarioInterno;
import datatypes.DtVacunador;
import datatypes.Sexo;
import exceptions.UsuarioInexistente;
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

	@PermitAll
	@POST
	@Path("/ciudadano/modificar")
	public Response modificarCiudadano(@CookieParam("x-access-token") Cookie cookie, String datos) {
		JSONObject jsonObject;
		try {
			jsonObject = new JSONObject(datos);
			String token = cookie.getValue();
			String ci = TokenSecurity.getIdClaim(TokenSecurity.validateJwtToken(token));
			DtCiudadano ciudadano = IUsuarioLocal.buscarCiudadano(Integer.parseInt(ci));
			DtDireccion oldDir = ciudadano.getDireccion();
			DtDireccion dir = new DtDireccion();
			//seteo email
			if(jsonObject.getString("email") != null && !jsonObject.getString("email").isEmpty())
				ciudadano.setEmail(jsonObject.getString("email"));
			//seteo direccion
			if(jsonObject.getString("direccion") != null && !jsonObject.getString("direccion").isEmpty()) {
				dir.setDireccion(jsonObject.getString("direccion"));
			}else {
				dir.setDireccion(oldDir.getDireccion());
			}
			//seteo barrio
			if(jsonObject.getString("barrio") != null && !jsonObject.getString("barrio").isEmpty()) {
				dir.setBarrio(jsonObject.getString("barrio"));
			}else {
				dir.setBarrio(oldDir.getBarrio());
			}
			//seteo departamento
			if(jsonObject.getString("departamento") != null && !jsonObject.getString("departamento").isEmpty()) {
				dir.setDepartamento(jsonObject.getString("departamento"));
			}else {
				dir.setDepartamento(oldDir.getDepartamento());
			}
			ciudadano.setDireccion(dir);
			IUsuarioLocal.ModificarCiudadano(ciudadano);
			
			return ResponseBuilder.createResponse(Response.Status.OK, "Se modifico el usuario correctamente");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			return ResponseBuilder.createResponse(Response.Status.BAD_REQUEST, e.getMessage());
		}catch (InvalidJwtException e) {
			// TODO Auto-generated catch block
			return ResponseBuilder.createResponse(Response.Status.BAD_REQUEST, e.getMessage());
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			return ResponseBuilder.createResponse(Response.Status.BAD_REQUEST, e.getMessage());
		} catch (UsuarioInexistente e) {
			// TODO Auto-generated catch block
			return ResponseBuilder.createResponse(Response.Status.BAD_REQUEST, e.getMessage());
		}
		
	}
	
	@PermitAll
	@GET
	@Path("/ciudadano/buscar") //obtiene el usuario del cookie obtenido
	public Response obtenerCiudadano(@CookieParam("x-access-token") Cookie cookie) {
		if (cookie != null) {
			LOGGER.info(cookie.getValue());
			String token = cookie.getValue();
			String ci;
			try {
				ci = TokenSecurity.getIdClaim(TokenSecurity.validateJwtToken(token));
				DtCiudadano ciudadano = IUsuarioLocal.buscarCiudadano(Integer.parseInt(ci));
				return Response.ok(ciudadano).build();
			} catch (InvalidJwtException | NumberFormatException | UsuarioInexistente e) {
				return ResponseBuilder.createResponse(Response.Status.BAD_REQUEST, e.getMessage());
			}
		}
		return ResponseBuilder.createResponse(Response.Status.BAD_REQUEST, "No se obtuvo ciudadano");
	}
	
	@PermitAll
	@GET
	@Path("/interno/buscar") //obtiene el usuario del cookie obtenido
	public Response obtenerInterno(@CookieParam("x-access-token") Cookie cookie) {
		if (cookie != null) {
			String token = cookie.getValue();
			String ci = null;
			try {
				ci = TokenSecurity.getIdClaim(TokenSecurity.validateJwtToken(token));
				LOGGER.info("Cedula obtenida en REST: " + ci);
				DtUsuarioInterno interno = IUsuarioLocal.buscarUsuarioInterno(Integer.parseInt(ci));
				
				JSONObject jsonObject = new JSONObject();
				jsonObject.put("email", interno.getEmail());
				jsonObject.put("barrio", interno.getDireccion().getBarrio());
				jsonObject.put("direccion", interno.getDireccion().getDireccion());
				jsonObject.put("departamento", interno.getDireccion().getDepartamento());
				return Response.ok(jsonObject.toString()).build();
			} catch (InvalidJwtException | NumberFormatException | UsuarioInexistente | JSONException e) {
				return ResponseBuilder.createResponse(Response.Status.BAD_REQUEST, e.getMessage());
			}
		}
		return ResponseBuilder.createResponse(Response.Status.BAD_REQUEST, "No se obtuvo interno");
	}
		
	@PermitAll
	@GET
	@Path("/vacunador/buscar") //obtiene el usuario del cookie obtenido
	public Response obtenerVacunador(@CookieParam("x-access-token") Cookie cookie) {
		if (cookie != null) {
			String token = cookie.getValue();
			String ci = null;
			try {
				ci = TokenSecurity.getIdClaim(TokenSecurity.validateJwtToken(token));
				LOGGER.info("Cedula obtenida en REST: " + ci);
				DtVacunador interno = IUsuarioLocal.buscarVacunador(Integer.parseInt(ci));
				return Response.ok(interno).build();
			} catch (InvalidJwtException | NumberFormatException | UsuarioInexistente e) {
				return ResponseBuilder.createResponse(Response.Status.BAD_REQUEST, e.getMessage());
			}
		}
		return ResponseBuilder.createResponse(Response.Status.BAD_REQUEST, "No se obtuvo interno");
	}
	
	@PermitAll
	@POST
	@Path("/interno/modificar")
	public Response modificarInterno(@CookieParam("x-access-token") Cookie cookie, String datos) {
		if (cookie != null) {
			try {
				JSONObject datosInterno = new JSONObject(datos);
				String token = cookie.getValue();
				String ci = TokenSecurity.getIdClaim(TokenSecurity.validateJwtToken(token));
				DtUsuarioInterno interno = IUsuarioLocal.buscarUsuarioInterno(Integer.parseInt(ci));
				//String nombre, String apellido, LocalDate fechaNac, int IdUsuario, String email, DtDireccion direccion, Sexo sexo
				interno.setEmail(datosInterno.getString("email"));
				JSONObject jsonDir = datosInterno.getJSONObject("direccion");
				DtDireccion dir = new DtDireccion(jsonDir.getString("direccion"), jsonDir.getString("barrio"), jsonDir.getString("departamento"));
				interno.setDireccion(dir);
				IUsuarioLocal.ModificarUsuarioInterno(interno);
				return ResponseBuilder.createResponse(Response.Status.CREATED, "Se modifico el usuario correctamente");
			} catch (JSONException | InvalidJwtException | NumberFormatException | UsuarioInexistente e) {
				return ResponseBuilder.createResponse(Response.Status.BAD_REQUEST, e.getMessage());
			}
		}
		return ResponseBuilder.createResponse(Response.Status.BAD_REQUEST, "No se ha seteado la Cookie");
	}
}
