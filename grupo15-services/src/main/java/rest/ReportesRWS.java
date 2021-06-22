package rest;

import java.util.Map;
import java.util.logging.Logger;

import javax.annotation.security.DeclareRoles;
import javax.annotation.security.RolesAllowed;
import javax.ejb.EJB;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import interfaces.IConstanciaVacunaDAOLocal;

@DeclareRoles({ "vacunador", "ciudadano", "administrador", "autoridad" })
@Path("/reportes")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class ReportesRWS {

	private final Logger LOGGER = Logger.getLogger(getClass().getName());
	
	@EJB
	IConstanciaVacunaDAOLocal cv;
	
	public ReportesRWS() {}

	@RolesAllowed({ "administrador", "autoridad" })
	@GET
	@Path("/reporte1")
	public Response getVacunadosPorMes() {
		Map<String,String> vacunados = cv.constanciasPorMes();
		System.out.println(vacunados);
		return Response.ok(vacunados).build();
	}
	
	@RolesAllowed({ "administrador", "autoridad" })
	@GET
	@Path("/reporte2")
	public Response getVacunadosPorEdad() {
		Map<String,String> vacunados = cv.constanciasPorEdad();
		System.out.println(vacunados);
		return Response.ok(vacunados).build();
	}
	
	@RolesAllowed({ "administrador", "autoridad" })
	@GET
	@Path("/reporte3")
	public Response getVacunadosPorSexo() {
		Map<String,String> vacunados = cv.constanciasPorSexo();
		System.out.println(vacunados);
		return Response.ok(vacunados).build();
	}
}
