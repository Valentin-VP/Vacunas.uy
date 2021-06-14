package rest;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.logging.Logger;

import javax.annotation.security.DeclareRoles;
import javax.annotation.security.PermitAll;
import javax.ejb.EJB;
import javax.ws.rs.Consumes;
import javax.ws.rs.CookieParam;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.NotAuthorizedException;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Cookie;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.jose4j.jwt.consumer.InvalidJwtException;

import datatypes.DtDatosEtapa;
import exceptions.AccionInvalida;
import exceptions.EtapaInexistente;
import exceptions.EtapaRepetida;
import exceptions.PlanVacunacionInexistente;
import exceptions.VacunaInexistente;
import interfaces.IEtapaLocal;
import rest.filter.ResponseBuilder;
import rest.filter.TokenSecurity;

@DeclareRoles({"vacunador", "ciudadano", "administrador", "autoridad"})
@Path("/etapa")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class GestionEtapaRWS {
	@EJB
	IEtapaLocal ce;
	private final Logger LOGGER = Logger.getLogger(getClass().getName());
	public GestionEtapaRWS() {

	}
	
	//@RolesAllowed({"autoridad"}) 
		@PermitAll
		@GET
		@Path("/listar")
		public Response listarEtapas(@CookieParam("x-access-token") Cookie cookie) {
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
				return Response.ok(ce.listarEtapas()).build();
			} catch (EtapaInexistente e) {
				return ResponseBuilder.createResponse(Response.Status.BAD_REQUEST,
						e.getMessage());
			}
		}
		
		//@RolesAllowed({"autoridad"}) 
		@PermitAll
		@GET
		@Path("/obtener")
		public Response obtenerEtapa(@CookieParam("x-access-token") Cookie cookie, @QueryParam("p") String plan, @QueryParam("e") String etapa) {
			if (plan==null || etapa==null) {
				return ResponseBuilder.createResponse(Response.Status.BAD_REQUEST,
						"No se han ingresado todos los parametros necesarios.");
			}
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
				return Response.ok(ce.obtenerEtapa(Integer.parseInt(etapa), Integer.parseInt(plan))).build();
			} catch ( NumberFormatException |  EtapaInexistente e) {
				return ResponseBuilder.createResponse(Response.Status.BAD_REQUEST,
						e.getMessage());
			}
		}
		
		//@RolesAllowed({"autoridad"}) 
		@PermitAll
		@POST
		@Path("/agregar")
		public Response agregarEtapa(@CookieParam("x-access-token") Cookie cookie, DtDatosEtapa dte) {
			if (dte==null || dte.getIdEtapa() == null || dte.getfIni() == null || dte.getfFin() == null || dte.getCond() == null  || dte.getIdPlan() == null  || dte.getIdVacuna() == null) {
				return ResponseBuilder.createResponse(Response.Status.BAD_REQUEST,
						"No se han ingresado todos los parametros necesarios.");
			}
			try {
				DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
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
				ce.agregarEtapa(Integer.parseInt(dte.getIdEtapa()), LocalDate.parse(dte.getfIni(), formatter), LocalDate.parse(dte.getfFin(), formatter), dte.getCond(), Integer.parseInt(dte.getIdPlan()), dte.getIdVacuna());
				return Response.ok("Se ha agregado la etapa con exito.").build();
			} catch ( NumberFormatException | EtapaRepetida | PlanVacunacionInexistente | VacunaInexistente | AccionInvalida e) {
				return ResponseBuilder.createResponse(Response.Status.BAD_REQUEST,
						e.getMessage());
			}
		}
		
		//@RolesAllowed({"autoridad"}) 
		@PermitAll
		@DELETE
		@Path("/eliminar")
		public Response eliminarEtapa(@CookieParam("x-access-token") Cookie cookie, @QueryParam("e") String etapa, @QueryParam("p") String plan) {
			if (etapa==null||plan==null) {
				return ResponseBuilder.createResponse(Response.Status.BAD_REQUEST,
						"No se han ingresado todos los parametros necesarios.");
			}
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
				ce.eliminarEtapa(Integer.parseInt(etapa), Integer.parseInt(plan));
				return Response.ok("Se ha eliminado la etapa con exito.").build();
			} catch (NumberFormatException | AccionInvalida | EtapaInexistente e) {
				return ResponseBuilder.createResponse(Response.Status.BAD_REQUEST,
						e.getMessage());
			}
		}
}
