package rest;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.util.List;
import java.util.logging.Logger;

import javax.annotation.security.DeclareRoles;
import javax.annotation.security.PermitAll;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.ws.rs.Consumes;
import javax.ws.rs.CookieParam;
import javax.ws.rs.GET;
import javax.ws.rs.NotAuthorizedException;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Cookie;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.Response.Status;

import org.jose4j.jwt.consumer.InvalidJwtException;

import datatypes.ErrorInfo;
import exceptions.AgendaInexistente;
import interfaces.IAgendaDAOLocal;
import rest.filter.AuthenticationFilter;
import rest.filter.TokenSecurity;

@DeclareRoles({"vacunador", "ciudadano", "administrador", "autoridad"})
@RequestScoped
@Path("/agendavac")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class ConsultarAgendaVacunacionRWS {
	
	private final Logger LOGGER = Logger.getLogger(getClass().getName());

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
	@PermitAll
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
