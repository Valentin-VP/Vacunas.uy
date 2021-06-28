package rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.logging.Logger;

import javax.annotation.security.DeclareRoles;
import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.ejb.EJB;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.CookieParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.Context;
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
import exceptions.UsuarioInexistente;
import interfaces.IControladorVacunadorLocal;
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

	@EJB
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
	@GET
	@Path("/sectores")
	public Response listarSectoresExterno() {
		String url = "https://rcastro.pythonanywhere.com/api/usuarios/";
		Client conexion = ClientBuilder.newClient();
		List<DtUsuarioExterno> resultados = conexion.target(url)
				.request(MediaType.APPLICATION_JSON)
				.get(new GenericType<List<DtUsuarioExterno>> () {});
		List <String> sectores = new ArrayList<>();
		sectores.add("todos");
		for(DtUsuarioExterno dt: resultados) {
			String sector = dt.getTipoSector();
			if (!sectores.contains(sector)) {
				sectores.add(sector);
			}
		}
		return Response.ok(sectores).build();
	}

	@RolesAllowed({ "ciudadano" })
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
	
	@RolesAllowed({ "ciudadano" })
	@GET
	@Path("/ciudadano/datosModificar")
	public Response datosModificarCiudadano(@CookieParam("x-access-token") Cookie cookie) {	
		System.out.println("entro al datosmodificar");
		String token = cookie.getValue();
		String ci;
		try {
			ci = TokenSecurity.getIdClaim(TokenSecurity.validateJwtToken(token));
			DtCiudadano ciudadano = IUsuarioLocal.buscarCiudadano(Integer.parseInt(ci));
			JSONObject retorno = new JSONObject();
			retorno.put("direccion", ciudadano.getDireccion().getDireccion());
			retorno.put("barrio", ciudadano.getDireccion().getBarrio());
			retorno.put("departamento", ciudadano.getDireccion().getDepartamento());
			retorno.put("mail", ciudadano.getEmail());
			return Response.ok(retorno.toString()).build();
		} catch (InvalidJwtException | NumberFormatException | UsuarioInexistente | JSONException e) {
			return ResponseBuilder.createResponse(Response.Status.BAD_REQUEST, e.getMessage());
		}	
	}
	
	
	@RolesAllowed({ "ciudadano" })
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
	
	@RolesAllowed({ "administrador", "autoridad" })
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
		
	@RolesAllowed({ "vacunador" })
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
	
	@RolesAllowed({ "administrador", "autoridad" })
	@POST
	@Path("/vacunador/eliminar") 
	public Response eliminarVacunador(@QueryParam("ci") String ci) {
		try {
			IUsuarioLocal.eliminarVacunador(ci);
			return Response.ok().build();
		} catch (NumberFormatException | UsuarioInexistente e) {
			return ResponseBuilder.createResponse(Response.Status.BAD_REQUEST,e.getMessage());
		}
	}
	
	@RolesAllowed({ "administrador" })
	@POST
	@Path("/interno/eliminar") 
	public Response eliminarInterno(@QueryParam("ci") String ci) {
		try {
			IUsuarioLocal.eliminarInterno(ci);
			l.deleteUser(Integer.valueOf(ci));
			return Response.ok().build();
		} catch (NumberFormatException | UsuarioInexistente e) {
			return ResponseBuilder.createResponse(Response.Status.BAD_REQUEST,e.getMessage());
		}
	}
	
	@RolesAllowed({"administrador", "autoridad" })
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
	
	
	
	@RolesAllowed({"vacunador" })
	@POST
	@Path("/vacunador/modificar")
	public Response modificarVacunador(@CookieParam("x-access-token") Cookie cookie, String datos) {
		if (cookie != null) {
			try {
				JSONObject datosInterno = new JSONObject(datos);
				String token = cookie.getValue();
				String ci = TokenSecurity.getIdClaim(TokenSecurity.validateJwtToken(token));
				DtVacunador vacunador = IUsuarioLocal.buscarVacunador(Integer.parseInt(ci));
				//String nombre, String apellido, LocalDate fechaNac, int IdUsuario, String email, DtDireccion direccion, Sexo sexo
				vacunador.setEmail(datosInterno.getString("email"));
				JSONObject jsonDir = datosInterno.getJSONObject("direccion");
				DtDireccion dir = new DtDireccion(jsonDir.getString("direccion"), jsonDir.getString("barrio"), jsonDir.getString("departamento"));
				vacunador.setDireccion(dir);
				IUsuarioLocal.ModificarVacunador(vacunador);
				return ResponseBuilder.createResponse(Response.Status.CREATED, "Se modifico el usuario correctamente");
			} catch (JSONException | InvalidJwtException | NumberFormatException | UsuarioInexistente e) {
				return ResponseBuilder.createResponse(Response.Status.BAD_REQUEST, e.getMessage());
			}
		}
		return ResponseBuilder.createResponse(Response.Status.BAD_REQUEST, "No se ha seteado la Cookie");
	}
	
	
	
	
	
	@RolesAllowed({ "vacunador", "ciudadano", "administrador", "autoridad" })
	@GET
	@Path("/cerrarSesion")
	public Response deleteToken(@CookieParam("x-access-token") Cookie cookie, @Context HttpServletRequest request) {
		String token = cookie.getValue();
		try {
			String ci = TokenSecurity.getIdClaim(TokenSecurity.validateJwtToken(token));
			String tipoUser = TokenSecurity.getTipoUsuarioClaim(TokenSecurity.validateJwtToken(token));
			IUsuarioLocal.borrarToken(ci, TokenSecurity.getTipoUsuarioClaim(TokenSecurity.validateJwtToken(token)));
			String host = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort();
			URI url;
			System.out.println(tipoUser);
			if(tipoUser.equals("ciudadano") || tipoUser.equals("vacunador")) {// si es de vue
				url = new URI(host + "/grupo15-web/html/login.html");
			}else {//si es de interno
				url = new URI(host + "/grupo15-web/html/loginInterno.html");
				System.out.println("adentro de interno "+url);
			}
			System.out.println(url);
			return Response.temporaryRedirect(url).build();
		} catch (InvalidJwtException | NumberFormatException | UsuarioInexistente | URISyntaxException e) {
			return ResponseBuilder.createResponse(Response.Status.BAD_REQUEST, e.getMessage());
		}
	}
	
	@RolesAllowed({"ciudadano"})
	@GET
	@Path("/checkToken")
	public Response checktoken(@CookieParam("x-access-token") Cookie cookie) {
		String token = cookie.getValue();
		try {
			String ci = TokenSecurity.getIdClaim(TokenSecurity.validateJwtToken(token));
			return ResponseBuilder.createResponse(Response.Status.OK, "existe el token");
		} catch (InvalidJwtException | NumberFormatException e) {
			return ResponseBuilder.createResponse(Response.Status.BAD_REQUEST, "no existe token");
		}
	}
}
