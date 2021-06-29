package rest;

import java.io.Serializable;

import javax.annotation.security.DeclareRoles;
import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import datatypes.ErrorInfo;
import exceptions.PlanVacunacionInexistente;
import interfaces.IPlanVacunacionLocal;
import rest.filter.ResponseBuilder;

@DeclareRoles({"vacunador", "ciudadano", "administrador", "autoridad"})
@Path("/planagenda")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@SessionScoped
public class ConsultarAgendaPlanRWS implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@EJB
	IPlanVacunacionLocal cp;
	
	public ConsultarAgendaPlanRWS() {
		// TODO Auto-generated constructor stub
	}
	
	@RolesAllowed({"ciudadano"})
	@GET
	@Path("/vigente")
	public Response listarAgendasAbiertas() {
		try {
			return Response.ok(cp.listarAgendasAbiertas()).build();
		}catch(PlanVacunacionInexistente e) {
			return ResponseBuilder.createResponse(Response.Status.BAD_REQUEST);
		}
	}
	
	@RolesAllowed({"ciudadano"})
	@GET
	@Path("/proxima")
	public Response listarAgendasProximas() {
		try {
			return Response.ok(cp.listarAgendasProximas()).build();
		}catch(PlanVacunacionInexistente e) {
			return ResponseBuilder.createResponse(Response.Status.BAD_REQUEST);
		}
	}
}
