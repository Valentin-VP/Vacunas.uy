package rest;

import java.time.DateTimeException;
import java.time.LocalDate;

import javax.annotation.security.DenyAll;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.Response.Status;

import datatypes.ErrorInfo;
import exceptions.AgendaInexistente;
import interfaces.IAgendaDAOLocal;

@RequestScoped
@Path("/agendavac")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class ConsultarAgendaVacunacionRWS {

	@EJB
	IAgendaDAOLocal as;
	
	public ConsultarAgendaVacunacionRWS() {
		// TODO Auto-generated constructor stub
	}
	/*
	@GET
	@Path("/consultar") //quitar fecha (es .now())
	public DtAgenda consultarAgendaVacunacion(@QueryParam("date") Date fecha, @QueryParam("vact") String idVacunatorio){
		try {
			return as.obtenerAgenda(idVacunatorio, LocalDate.from(LocalDate.now()));
		} catch (DateTimeException | AgendaInexistente e) {
			return null;
		}
	}
*/
	 //.... ordenar fechas
	@GET
	@Path("/consultar/{vact}") //quitar fecha (es .now())
	@DenyAll
	public Response consultarAgendaVacunacion(@PathParam("vact") String idVacunatorio){
		if (idVacunatorio==null) {
			ResponseBuilder rb = Response.status(Status.BAD_REQUEST);
			return rb.build();
		}
		try {
			return Response.ok(as.obtenerAgenda(idVacunatorio, LocalDate.now())).build();
		} catch (DateTimeException | AgendaInexistente e) {
			//throw new WebApplicationException(e.getMessage());
			return Response.serverError().entity(new ErrorInfo(200, e.getMessage())).status(200).build();
			//ResponseBuilder rb = Response.status(Status.BAD_REQUEST);
			//return rb.entity(e.getMessage()).build();
		}
	}
}
