package rest;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.logging.Logger;

import javax.annotation.security.DeclareRoles;
import javax.annotation.security.PermitAll;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.ws.rs.Consumes;
import javax.ws.rs.CookieParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Cookie;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.codehaus.jettison.json.JSONException;
import org.jose4j.jwt.consumer.InvalidJwtException;

import datatypes.DtTareaNotificacion;
import exceptions.UsuarioInexistente;
import interfaces.IFirestoreLocal;
import interfaces.IUsuarioLocal;
import rest.filter.ResponseBuilder;
import rest.filter.TokenSecurity;

@DeclareRoles({ "vacunador", "ciudadano", "administrador", "autoridad" })
@RequestScoped
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Path("/firestore")
public class FirestoreRWS {

	private final Logger LOGGER = Logger.getLogger(getClass().getName());

	@EJB
	private IFirestoreLocal FirestoreController;

	@EJB
	private IUsuarioLocal IUsuarioLocal;

	public FirestoreRWS() {
	}

	@POST
	@Path("/notificacion") // @RolesAllowed("ciudadano")
	@PermitAll
	public Response crearNotificacion(@CookieParam("x-access-token") Cookie cookie, DtTareaNotificacion task) {
		String token = cookie.getValue();
		String ci = null;
		try {
			ci = TokenSecurity.getIdClaim(TokenSecurity.validateJwtToken(token));
		} catch (InvalidJwtException e) {
			return ResponseBuilder.createResponse(Response.Status.BAD_REQUEST, "Error procesando JWT");
		}
		if (ci == null)
			return ResponseBuilder.createResponse(Response.Status.UNAUTHORIZED, "No se ha obtenido ci de Cookie/Token");
		LOGGER.info("Cedula obtenida en REST: " + ci);
		String mobileToken;
		try {
			mobileToken = IUsuarioLocal.buscarCiudadano(Integer.parseInt(ci)).getMobileToken();
			String vacunatorio = task.getVacunatorio();
			String puesto = task.getPuesto();
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
			DateTimeFormatter formatter2 = DateTimeFormatter.ofPattern("HH:mm:ss");
			LocalDate fecha = LocalDate.parse(task.getFecha(), formatter);
			LocalTime hora = LocalTime.parse(task.getHora(), formatter2);
			FirestoreController.nuevaNotificacion(mobileToken, vacunatorio, puesto, fecha, hora);
			return ResponseBuilder.createResponse(Response.Status.OK);
		} catch (NumberFormatException | UsuarioInexistente e) {
			return ResponseBuilder.createResponse(Response.Status.BAD_REQUEST, e.getMessage());
		}
	}

}
