package rest;

import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.Temporal;
import java.util.Date;

import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.Response.Status;

import datatypes.DtAsignado;
import datatypes.ErrorInfo;
import exceptions.UsuarioInexistente;
import exceptions.VacunadorSinAsignar;
import exceptions.VacunatorioNoCargadoException;
import interfaces.IControladorVacunadorLocal;

@SessionScoped
@Path("/puestovac")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class ConsultarPuestoVacunadorRWS implements Serializable {

	/**
	 * 
	 */
	@EJB
	IControladorVacunadorLocal vs;
	
	private static final long serialVersionUID = 1L;

	public ConsultarPuestoVacunadorRWS() {
		// TODO Auto-generated constructor stub
	}
	/*
	@GET
	@Path("/asignado") //agregar fecha
	public DtAsignado consultarPuestoVacunador(@QueryParam("user") int idVacunador, @QueryParam("vact") String idVacunatorio, @QueryParam("date") Date fecha){
		try {
			LocalDate f = LocalDate.from(fecha.toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
			Date nuevaFecha = Date.from(Instant.from(f));
			return vs.consultarPuestoAsignadoVacunador(idVacunador, idVacunatorio, nuevaFecha);
		} catch (VacunatorioNoCargadoException | UsuarioInexistente | VacunadorSinAsignar e) {
			return null;
		}
	}
*/
	
	@GET
	@Path("/asignado") //agregar fecha
	public Response consultarPuestoVacunador(@QueryParam("user") int idVacunador, @QueryParam("vact") String idVacunatorio, @QueryParam("date") Date fecha){
		if (idVacunatorio==null || fecha==null) {
			ResponseBuilder rb = Response.status(Status.BAD_REQUEST);
			return rb.build();
		}
		try {
			LocalDate f = LocalDate.from(fecha.toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
			Date nuevaFecha = Date.from(f.atStartOfDay(ZoneId.systemDefault()).toInstant());
			return Response.ok(vs.consultarPuestoAsignadoVacunador(idVacunador, idVacunatorio, nuevaFecha)).build();
		} catch (VacunatorioNoCargadoException | UsuarioInexistente | VacunadorSinAsignar e) {
			return Response.serverError().entity(new ErrorInfo(400, e.getMessage())).status(400).build();
			//return Response.serverError().entity(e.getMessage()).status(400).build();
			//ResponseBuilder rb = Response.status(Status.BAD_REQUEST);
			//return rb.entity(e.getMessage()).build();
		}
	}
}
