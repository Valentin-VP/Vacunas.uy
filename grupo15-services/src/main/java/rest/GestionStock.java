package rest;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

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
import javax.ws.rs.core.Cookie;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import datatypes.DtVacuna;
import exceptions.VacunaInexistente;
import interfaces.IControladorVacunaLocal;
import rest.filter.ResponseBuilder;

@DeclareRoles({"vacunador", "ciudadano", "administrador", "autoridad"})
@Path("/stock")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class GestionStock {

	@EJB
	interfaces.IStockDaoLocal cs;
	
	@EJB
	IControladorVacunaLocal vac;
	
	public GestionStock() {}

	@RolesAllowed({"autoridad"}) 
	@POST
	@Path("/actual")
	public Response getLotesActual(String datos) {
		try {
			JSONObject json = new JSONObject(datos);
			String vacunatorio = json.getString("vacunatorio");
			String enfermedad = json.getString("enfermedad");
			String vacuna = json.getString("vacuna");
			if(vacuna.equals("todos"))
				vacuna = "";
			if(vacunatorio.equals("todos"))
				vacunatorio = "";
			System.out.println(vacunatorio);
			System.out.println(enfermedad);
			System.out.println(vacuna);
			return Response.ok(cs.getStockActual(enfermedad, vacuna, vacunatorio)).build();
		} catch (JSONException e) {
			return ResponseBuilder.createResponse(Response.Status.BAD_REQUEST,
					e.getMessage());
		}
	}
	
	@RolesAllowed({"autoridad"}) 
	@POST
	@Path("/historico")
	public Response getLotesHistorico(String datos) {
		System.out.println("Entro al getHistorico");
		try {
			JSONObject json = new JSONObject(datos);
			String vacunatorio = json.getString("vacunatorio");
			String enfermedad = json.getString("enfermedad");
			String vacuna = json.getString("vacuna");
			String tiempo = json.getString("periodo");
			if(vacuna.equals("Todos"))
				vacuna = "";
			if(vacunatorio.equals("Todos"))
				vacunatorio = "";
			System.out.println(vacunatorio);
			System.out.println(enfermedad);
			System.out.println(vacuna);
			System.out.println(tiempo);
			return Response.ok(cs.getHistoricoStock(enfermedad, vacuna, vacunatorio, LocalDate.now().minusMonths(Integer.valueOf(tiempo)), LocalDate.now())).build();
		} catch (JSONException e) {
			return ResponseBuilder.createResponse(Response.Status.BAD_REQUEST, e.getMessage());
		}
	}
	
	@RolesAllowed({"autoridad"}) 
	@GET
	@Path("/enf/{e}")
	public Response seleccionarEnfermedad(@PathParam("e") String enfermedad){
		try {
			List<DtVacuna> vacunas = new ArrayList<DtVacuna>();
			for(DtVacuna dtVac: vac.listarVacunas()) {
				if(dtVac.getDtEnf().getNombre().equals(enfermedad)) {
					vacunas.add(dtVac);
				}
			}
			return Response.ok(vacunas).build();
		} catch (VacunaInexistente e) {
				return ResponseBuilder.createResponse(Response.Status.BAD_REQUEST, e.getMessage());
		}
	}
}
