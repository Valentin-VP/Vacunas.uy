package rest;

import java.time.LocalDate;
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
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Cookie;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.jose4j.jwt.consumer.InvalidJwtException;

import datatypes.DtDatosEtapa;
import datatypes.DtEtapa;
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
	
		@RolesAllowed({"autoridad"}) 
		@GET
		@Path("/listar")
		public Response listarEtapas() {
			try {
				return Response.ok(ce.listarEtapas()).build();
			} catch (EtapaInexistente e) {
				return ResponseBuilder.createResponse(Response.Status.BAD_REQUEST,
						e.getMessage());
			}
		}
		
		@RolesAllowed({"autoridad"}) 
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
		
		@RolesAllowed({"autoridad"}) 
		@POST
		@Path("/agregar")
		public Response agregarEtapa(String datos) {
			try {
				JSONObject etapa = new JSONObject(datos);
				DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
				ce.agregarEtapa(LocalDate.parse(etapa.getString("fechaInicio"), formatter), LocalDate.parse(etapa.getString("fechaFin"), formatter), etapa.getString("condicion"), Integer.parseInt(etapa.getString("plan")), etapa.getString("vacuna"));
				return ResponseBuilder.createResponse(Response.Status.OK, "Se ha agregado la etapa con exito");
			} catch ( NumberFormatException | EtapaRepetida | PlanVacunacionInexistente | VacunaInexistente | AccionInvalida | JSONException e) {
				//
				return ResponseBuilder.createResponse(Response.Status.BAD_REQUEST,
						e.getMessage());
			}
		}
		
		@RolesAllowed({"autoridad"}) 
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
		
		@RolesAllowed({"autoridad"}) 
		@POST
		@Path("/modificar")
		public Response modificarEtapa(String datos) {
			JSONObject jsonObject;
			System.out.println("Entro a etapa");
			try {
				jsonObject = new JSONObject(datos);
				String id = jsonObject.getString("id");
				String plan = jsonObject.getString("plan");
				String[] fi = jsonObject.getString("fechaInicio").split("-");
				String[] ff = jsonObject.getString("fechaFin").split("-");
				LocalDate fechaInicio = LocalDate.of(Integer.valueOf(fi[0]), Integer.valueOf(fi[1]), Integer.valueOf(fi[2]));
				LocalDate fechaFin = LocalDate.of(Integer.valueOf(ff[0]), Integer.valueOf(ff[1]), Integer.valueOf(ff[2]));
				String condicion = jsonObject.getString("condicion");
				System.out.println("Creo los datos");
				ce.modificarEtapa(Integer.valueOf(id), Integer.valueOf(plan), fechaInicio, fechaFin, condicion);
				System.out.println("Borro la etapa");
				return Response.ok().build();
			} catch (JSONException | NumberFormatException | EtapaInexistente e) {
				return ResponseBuilder.createResponse(Response.Status.BAD_REQUEST, e.getMessage());
			}
		}
}		
