package rest;

import java.time.LocalDate;

import javax.annotation.security.DeclareRoles;
import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.ejb.EJB;
import javax.ws.rs.Consumes;
import javax.ws.rs.CookieParam;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Cookie;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import exceptions.VacunaInexistente;
import rest.filter.ResponseBuilder;

@DeclareRoles({"vacunador", "ciudadano", "administrador", "autoridad"})
@Path("/stock")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class GestionStock {

	@EJB
	interfaces.IStockDaoLocal cs;
	
	public GestionStock() {}

	@RolesAllowed({"autoridad"}) 
	@GET
	@Path("/actual")
	public Response getLotesActual(String datos) {
		try {
			JSONObject json = new JSONObject(datos);
			String enfermedad = json.getString("enfermedad");
			String vacuna = json.getString("vacuna");
			String tiempo = json.getString("tiempo");
			return Response.ok(cs).build();
		} catch (JSONException e) {
			return ResponseBuilder.createResponse(Response.Status.BAD_REQUEST,
					e.getMessage());
		}
	}
	
	@RolesAllowed({"autoridad"}) 
	@GET
	@Path("/historico")
	public Response getLotesHistorico(String datos) {
		try {
			JSONObject json = new JSONObject(datos);
			String vacunatorio = json.getString("vacunatorio");
			String enfermedad = json.getString("enfermedad");
			String vacuna = json.getString("vacuna");
			String tiempo = json.getString("tiempo");
			return Response.ok(cs.getHistoricoStock(enfermedad, vacuna, vacunatorio, LocalDate.now().minusMonths(Integer.valueOf(tiempo)), LocalDate.now())).build();
		} catch (JSONException e) {
			return ResponseBuilder.createResponse(Response.Status.BAD_REQUEST, e.getMessage());
		}
	}
}
