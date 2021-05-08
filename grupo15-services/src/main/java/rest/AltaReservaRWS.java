package rest;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Date;

import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.Response.Status;

import datatypes.DtReserva;
import exceptions.CupoInexistente;
import exceptions.EnfermedadInexistente;
import exceptions.EtapaInexistente;
import exceptions.PlanVacunacionInexistente;
import exceptions.UsuarioInexistente;
import exceptions.VacunatorioNoCargadoException;
import exceptions.VacunatoriosNoCargadosException;
import interfaces.IControladorVacunatorioLocal;
import interfaces.IEnfermedadLocal;
import interfaces.IReservaDAOLocal;

@SessionScoped
@Path("/reservas")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class AltaReservaRWS implements Serializable {
	@EJB
	IReservaDAOLocal rs;
	
	@EJB
	IControladorVacunatorioLocal vs;
	
	@EJB
	IEnfermedadLocal es;
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public AltaReservaRWS() {
		// TODO Auto-generated constructor stub
	}
	
	@GET
	@Path("/enf")
	public Response listarEnfermedades(){
		try {
			return Response.ok(rs.listarEnfermedades()).build();
		} catch (EnfermedadInexistente e) {
			ResponseBuilder rb = Response.status(Status.BAD_REQUEST);
			return rb.entity(e.getMessage()).build();
		}
	}
	
	@GET
	@Path("/vac")
	public Response listarVacunatorios(){
		try {
			return Response.ok(vs.listarVacunatorio()).build();
		} catch (VacunatoriosNoCargadosException e) {
			ResponseBuilder rb = Response.status(Status.BAD_REQUEST);
			return rb.entity(e.getMessage()).build();
		}

	}
	
	@GET
	@Path("/enf/{e}")
	public Response seleccionarEnfermedad(@PathParam("e") String enfermedad){
		try {
			return Response.ok(rs.seleccionarEnfermedad(enfermedad)).build();
		} catch (PlanVacunacionInexistente e) {
			ResponseBuilder rb = Response.status(Status.BAD_REQUEST);
			return rb.entity(e.getMessage()).build();
		}
	}
	
	@GET
	@Path("/pv/{p}")
	public Response seleccionarPlan(@PathParam("p") int plan){
		try {
			return Response.ok(rs.seleccionarPlanVacunacion(plan)).build();
		} catch (PlanVacunacionInexistente | EtapaInexistente e) {
			ResponseBuilder rb = Response.status(Status.BAD_REQUEST);
			return rb.entity(e.getMessage()).build();
		}
	}
	
	@GET
	@Path("/fecha/{vac}/{date}")
	public Response seleccionarFecha(@PathParam("vac") String idVacunatorio, @PathParam("date") Date fecha){
		try {
			return Response.ok(rs.seleccionarFecha(LocalDate.from(fecha.toInstant()), idVacunatorio)).build();
		} catch (VacunatorioNoCargadoException e) {
			ResponseBuilder rb = Response.status(Status.BAD_REQUEST);
			return rb.entity(e.getMessage()).build();
		}
	}
	
	@POST
	@Path("/confirmar")
	public Response confirmarReserva(@FormParam("idUser") int idCiudadano, @FormParam("idEnf") String idEnfermedad, @FormParam("idPlan") int idPlan, @FormParam("idVac") String idVacunatorio,
			@FormParam("fecha")Date fecha, @FormParam("hora")Date hora){
		try {
			rs.confirmarReserva(idCiudadano, idEnfermedad, idPlan, idVacunatorio, LocalDate.from(fecha.toInstant()), LocalTime.from(hora.toInstant()));
			return Response.ok().build();
		} catch (UsuarioInexistente | PlanVacunacionInexistente | VacunatorioNoCargadoException | EnfermedadInexistente
				| CupoInexistente e) {
			ResponseBuilder rb = Response.status(Status.BAD_REQUEST);
			return rb.entity(e.getMessage()).build();
		}
	}
	
	
	
}
