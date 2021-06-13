package rest;

import java.net.URI;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.logging.Logger;

import javax.annotation.security.DeclareRoles;
import javax.annotation.security.RolesAllowed;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.ws.rs.Consumes;
import javax.ws.rs.CookieParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Cookie;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;

import org.jose4j.json.internal.json_simple.JSONObject;
import org.jose4j.jwt.consumer.InvalidJwtException;

import datatypes.DtCiudadano;
import datatypes.DtReserva;
import datatypes.DtTareaNotificacion;
import exceptions.UsuarioInexistente;
import interfaces.IUsuarioLocal;
import rest.filter.ResponseBuilder;
import rest.filter.TokenSecurity;

@DeclareRoles({ "vacunador", "ciudadano", "administrador", "autoridad" })
@RequestScoped
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Path("/app")
public class RegistrarAppMobile {

	private final Logger LOGGER = Logger.getLogger(getClass().getName());
	
	@EJB(lookup = "java:global/grupo15/grupo15-ejb/ControladorUsuario!interfaces.IUsuarioLocal")
	private IUsuarioLocal IUsuarioLocal;
	
	public RegistrarAppMobile() {}

	@POST
	@Path("/registrar")
	@RolesAllowed({"ciudadano"})
	public Response registrarMobileToken(@CookieParam("x-access-token") Cookie cookie, @Context HttpHeaders headers, JSONObject mobileTokenJson) {
		if (cookie != null) {
			String token = cookie.getValue();
			String ci = null;
			try {
				ci = TokenSecurity.getIdClaim(TokenSecurity.validateJwtToken(token));
			} catch (InvalidJwtException e) {
				return ResponseBuilder.createResponse(Response.Status.BAD_REQUEST, "Error procesando JWT");
			}
			LOGGER.info("Cedula obtenida en REST: " + ci);
			String mobileToken = (String) mobileTokenJson.get("mobileToken");
			LOGGER.info("MobileToken obtenido en REST: " + mobileToken);
			if (ci == null)
				return ResponseBuilder.createResponse(Response.Status.UNAUTHORIZED, "No se ha obtenido ci de Cookie/Token");
			try {
				DtCiudadano ciudadano = IUsuarioLocal.buscarCiudadano(Integer.parseInt(ci));
				String oldToken = ciudadano.getMobileToken();
				LOGGER.info("oldToken: " + oldToken);
				if (oldToken == null) {
					// Nuevo usuario mobile, se deben generar notificaciones de todas sus reservas
					ciudadano.setMobileToken(mobileToken);
					IUsuarioLocal.ModificarCiudadano(ciudadano);
					DateTimeFormatter fechaFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
					LocalDate fechaActual = LocalDate.now();
					for(DtReserva reserva: ciudadano.getReservas()) {
						String fragmentos[] = reserva.getFecha().split(" ");
						LocalDate fechaReserva = LocalDate.parse(fragmentos[0], fechaFormatter);
						if (fechaReserva.compareTo(fechaActual) > 0) {
							//generar push por esta reserva
							LOGGER.info("Realizando pedido de push para Reserva...");
							DateTimeFormatter horaFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");
							LocalTime horaReserva = LocalTime.parse(fragmentos[1], horaFormatter);
							String fecha = fechaReserva.format(fechaFormatter);
							String hora = horaReserva.format(horaFormatter);
							DtTareaNotificacion task = new DtTareaNotificacion(reserva.getPuesto(), mobileToken, reserva.getVacunatorio(), fecha, hora);
							
							String origin = headers.getHeaderString("Origin");
							if (origin == null || origin == "") 
								origin = "http://localhost:8080";
							String url = origin + "/grupo15-services/rest/firestore/notificacion";
							URI uri = UriBuilder.fromPath(url).build();
							LOGGER.severe("Uri para REST Firestore: " + uri.toString());
							
							
							Client conexion = ClientBuilder.newClient();
							Response loginResponse = conexion.target(uri)
							.request(MediaType.APPLICATION_JSON)
							.cookie(cookie)
							.buildPost(Entity.entity(task, MediaType.APPLICATION_JSON))
							.invoke();
							
							if(loginResponse.getStatus() == 200) {
								LOGGER.info("Pedido de push realizado a REST de Firestore retorna 200 OK");
							}else {
								LOGGER.severe("Ha ocurrido un error realizando el pedido REST a Firestore (interno) - " + loginResponse.getEntity());
							}
						}
					} // cierra for
					return ResponseBuilder.createResponse(Response.Status.OK, "Se han generado push notifications de las reservas existentes y se ha actualizado el mobileToken del Ciudadano");
				} else if (!oldToken.equals(mobileToken)) {
					// Diferentes tokens, pero ya tiene app. Solo actualizar token
					ciudadano.setMobileToken(mobileToken);
					IUsuarioLocal.ModificarCiudadano(ciudadano);
					return ResponseBuilder.createResponse(Response.Status.OK, "Se ha actualizado el mobileToken del Ciudadano");
				}
			} catch (NumberFormatException | UsuarioInexistente e) {
				return ResponseBuilder.createResponse(Response.Status.BAD_REQUEST, e.getMessage());
			}
		} //else:
		return ResponseBuilder.createResponse(Response.Status.BAD_REQUEST,"No se ha recibido Cookie");
	}
	
	
}
