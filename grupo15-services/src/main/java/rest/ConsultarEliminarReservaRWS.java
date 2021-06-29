package rest;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.logging.Logger;

import javax.annotation.security.DeclareRoles;
import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.ejb.EJB;
import javax.ws.rs.Consumes;
import javax.ws.rs.CookieParam;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.NotAuthorizedException;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Cookie;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.jose4j.jwt.consumer.InvalidJwtException;

import datatypes.ErrorInfo;
import exceptions.EnfermedadInexistente;
import exceptions.EtapaInexistente;
import exceptions.ReservaInexistente;
import exceptions.UsuarioInexistente;
import interfaces.IReservaDAOLocal;
import rest.filter.ResponseBuilder;
import rest.filter.TokenSecurity;

@DeclareRoles({"vacunador", "ciudadano", "administrador", "autoridad"})
@Path("/reservas")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class ConsultarEliminarReservaRWS {
	@EJB
	IReservaDAOLocal rs;
	
	private final Logger LOGGER = Logger.getLogger(getClass().getName());
	
	public ConsultarEliminarReservaRWS() {

	}
	
	@RolesAllowed({"ciudadano"}) 
	//@PermitAll
	@GET
	@Path("/listar")
	public Response listarReservasCiudadano(@CookieParam("x-access-token") Cookie cookie) {
		try {
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
			return Response.ok(rs.listarReservasCiudadano(Integer.parseInt(ci))).build();
		} catch (NumberFormatException | ReservaInexistente | UsuarioInexistente e) {
			return ResponseBuilder.createResponse(Response.Status.BAD_REQUEST,
					e.getMessage());
		}
	}
	
	/*@RolesAllowed({"ciudadano"}) 
	@PermitAll
	@GET
	@Path("/obtener")
	public Response obtenerReservaCiudadano(@CookieParam("x-access-token") Cookie cookie, @QueryParam("p") int plan, @QueryParam("e") int etapa, @QueryParam("date") String fecha) {
		if (fecha==null) {
			return ResponseBuilder.createResponse(Response.Status.BAD_REQUEST,
					"No se han ingresado todos los parametros necesarios.");
		}
		try {
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
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
			return Response.ok(rs.obtenerReserva(Integer.parseInt(ci), plan, etapa, LocalDateTime.parse(fecha, formatter))).build();
		} catch (NumberFormatException | ReservaInexistente | UsuarioInexistente | EtapaInexistente e) {
			return ResponseBuilder.createResponse(Response.Status.BAD_REQUEST,
					e.getMessage());
		}
	}*/
	
	@RolesAllowed({"ciudadano"}) 
	//@PermitAll
	@DELETE
	@Path("/eliminar")
	public Response eliminarReservaCiudadano(@CookieParam("x-access-token") Cookie cookie, @QueryParam("e") String idEnfermedad, @QueryParam("date") String fecha) {
		if (idEnfermedad==null || fecha==null) {
			return ResponseBuilder.createResponse(Response.Status.BAD_REQUEST,
					"No se han ingresado todos los parametros necesarios.");
		}
		try {
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
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
			rs.eliminarReserva(Integer.parseInt(ci), LocalDateTime.parse(fecha, formatter), idEnfermedad);
			return Response.ok().build();
		} catch (NumberFormatException | ReservaInexistente | UsuarioInexistente | EnfermedadInexistente e) {
			return ResponseBuilder.createResponse(Response.Status.BAD_REQUEST,
					e.getMessage());
		}
	}
	
}
