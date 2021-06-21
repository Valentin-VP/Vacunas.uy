package rest;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javax.annotation.security.DeclareRoles;
import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.ejb.EJB;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import datatypes.DtEtapa;
import datatypes.DtPlanVacunacion;
import exceptions.AccionInvalida;
import exceptions.EnfermedadInexistente;
import exceptions.PlanVacunacionInexistente;
import interfaces.IEnfermedadLocal;
import interfaces.IEtapaLocal;
import interfaces.IPlanVacunacionLocal;
import rest.filter.ResponseBuilder;

@DeclareRoles({"vacunador", "ciudadano", "administrador", "autoridad"})
@Path("/plan")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class GestionPlanRWS {
	@EJB
	IPlanVacunacionLocal cp;
	
	@EJB
	IEtapaLocal controladorEtapa;
	
	@EJB
	IEnfermedadLocal controladorEnfermedad;
	
	private final Logger LOGGER = Logger.getLogger(getClass().getName());

	public GestionPlanRWS() {

	}
	
	//@RolesAllowed({"autoridad"}) 
	@PermitAll
	@GET
	@Path("/listar")
	public Response listarPlanes() {
		try {
			return Response.ok(cp.listarPlanesVacunacion()).build();
		} catch (PlanVacunacionInexistente e) {
			return ResponseBuilder.createResponse(Response.Status.BAD_REQUEST,
					e.getMessage());
		}
	}
	
	//@RolesAllowed({"autoridad"}) 
	@PermitAll
	@GET
	@Path("/obtener")
	public Response obtenerPlan(@QueryParam("p") String plan) {
		if (plan==null) {
			return ResponseBuilder.createResponse(Response.Status.BAD_REQUEST,
					"No se han ingresado todos los parametros necesarios.");
		}
		try {
			return Response.ok(cp.obtenerPlanVacunacion(Integer.parseInt(plan))).build();
		} catch ( NumberFormatException | PlanVacunacionInexistente e) {
			return ResponseBuilder.createResponse(Response.Status.BAD_REQUEST,
					e.getMessage());
		}
	}
	
	//@RolesAllowed({"autoridad"}) 
	@PermitAll
	@POST
	@Path("/agregar")
	public Response agregarPlan(String datos) {
		try {
			JSONObject datosInterno = new JSONObject(datos);
			cp.agregarPlanVacunacion(datosInterno.getString("nombre"), datosInterno.getString("descripcion"), datosInterno.getString("enfermedad"));
			return ResponseBuilder.createResponse(Response.Status.CREATED, "Se ha agregado el plan con exito.");
		} catch ( NumberFormatException | JSONException | EnfermedadInexistente e) {
			return ResponseBuilder.createResponse(Response.Status.BAD_REQUEST,
					e.getMessage());
		}
	}
	
	
	//@RolesAllowed({"autoridad"}) 
	@PermitAll
	@DELETE
	@Path("/eliminar")
	public Response eliminarPlan(@QueryParam("p") String plan) {
		if (plan==null) {
			return ResponseBuilder.createResponse(Response.Status.BAD_REQUEST,
					"No se han ingresado todos los parametros necesarios.");
		}
		try {
			cp.eliminarPlanVacunacion(Integer.parseInt(plan));
			return Response.ok("Se ha eliminado el plan con exito.").build();
		} catch (NumberFormatException | PlanVacunacionInexistente | AccionInvalida e) {
			return ResponseBuilder.createResponse(Response.Status.BAD_REQUEST,
					e.getMessage());
		}
	}
	
	@RolesAllowed({ "administrador", "autoridad"})
	@GET
	@Path("/enf/{p}")
	public Response getEnfermedadPorPlan(@PathParam("p") String plan) {
		List<DtEtapa> etapas = new ArrayList<DtEtapa>();
		try {
			DtPlanVacunacion dtPlan= cp.obtenerPlanVacunacion(Integer.valueOf(plan));
			for(DtEtapa dtE:dtPlan.getEtapa()) {
				etapas.add(dtE);
			}
			return Response.ok(etapas).build();
		} catch (NumberFormatException | PlanVacunacionInexistente e) {
			return ResponseBuilder.createResponse(Response.Status.BAD_REQUEST, e.getMessage());
		}
		
	}
}
