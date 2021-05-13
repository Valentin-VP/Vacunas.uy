package rest;

import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.Temporal;
import java.util.ArrayList;
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
import datatypes.DtVacunatorio;
import datatypes.ErrorInfo;
import exceptions.UsuarioInexistente;
import exceptions.VacunadorSinAsignar;
import exceptions.VacunatorioNoCargadoException;
import exceptions.VacunatoriosNoCargadosException;
import interfaces.IControladorVacunadorLocal;
import interfaces.IControladorVacunatorioLocal;

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
	
	@EJB
	IControladorVacunatorioLocal vacs;
	
	private static final long serialVersionUID = 1L;

	public ConsultarPuestoVacunadorRWS() {
		// TODO Auto-generated constructor stub
	}
	
	
	@GET
	@Path("/vac")
	public Response listarVacunatorios(){
		try {
			return Response.ok(vacs.listarVacunatorio()).status(200).build();
		} catch (VacunatoriosNoCargadosException e) {
			return Response.serverError().entity(new ErrorInfo(200, e.getMessage())).status(200).build();
		}

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
	//2021-05-12 
	@GET
	@Path("/asignado") //agregar fecha
	public Response consultarPuestoVacunador(@QueryParam("user") int idVacunador, @QueryParam("vact") String idVacunatorio, @QueryParam("date") String fecha){
		if (idVacunatorio==null || fecha==null) {
			ResponseBuilder rb = Response.status(Status.BAD_REQUEST);
			return rb.build();
		}
		try {
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
			LocalDate f = LocalDate.parse(fecha, formatter);
			//LocalDate f = LocalDate.from(fecha.toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
			//Date nuevaFecha = Date.from(f.atStartOfDay(ZoneId.systemDefault()).toInstant());
			return Response.ok(vs.consultarPuestoAsignadoVacunador(idVacunador, idVacunatorio, f)).build();
		} catch (VacunatorioNoCargadoException | UsuarioInexistente | VacunadorSinAsignar e) {
			return Response.serverError().entity(new ErrorInfo(200, e.getMessage())).status(200).build();
			//return Response.serverError().entity(e.getMessage()).status(400).build();
			//ResponseBuilder rb = Response.status(Status.BAD_REQUEST);
			//return rb.entity(e.getMessage()).build();
		}
	}
}