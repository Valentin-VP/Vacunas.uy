package rest;

import java.io.Serializable;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;

import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.Response.Status;

import datatypes.DtDatosReserva;
import datatypes.DtEnfermedad;
import datatypes.DtEtapa;
import datatypes.DtHora;
import datatypes.DtPlanVacunacion;
import datatypes.DtVacunatorio;
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
	public ArrayList<DtEnfermedad> listarEnfermedades(){
		try {
			return rs.listarEnfermedades();
		} catch (EnfermedadInexistente e) {
			return null;
		}
	}
	
	@GET
	@Path("/vac")
	public ArrayList<DtVacunatorio> listarVacunatorios(){
		try {
			return vs.listarVacunatorio();
		} catch (VacunatoriosNoCargadosException e) {
			return null;
		}

	}
	
	@GET
	@Path("/enf/{e}")
	public ArrayList<DtPlanVacunacion> seleccionarEnfermedad(@PathParam("e") String enfermedad){
		try {
			return rs.seleccionarEnfermedad(enfermedad);
		} catch (EnfermedadInexistente | PlanVacunacionInexistente  e) {
			return null;
		}
	}
	
	@GET
	@Path("/pv/{p}")
	public ArrayList<DtEtapa> seleccionarPlan(@PathParam("p") int plan){
		try {
			return rs.seleccionarPlanVacunacion(plan);
		} catch (PlanVacunacionInexistente | EtapaInexistente e) {
			return null;
		}
	}
	
	@GET
	@Path("/fecha")///{vac}/{date}")
	public ArrayList<LocalTime> seleccionarFecha(@QueryParam("vac") String idVacunatorio, @QueryParam("date") Date fecha){
		try {
			return rs.seleccionarFecha(LocalDate.from(fecha.toInstant().atZone(ZoneId.systemDefault()).toLocalDate()), idVacunatorio);
		} catch (DateTimeException | VacunatorioNoCargadoException e) {
			return null;
		}
	}
	
	@POST
	@Path("/confirmar")
	public void confirmarReserva(DtDatosReserva dtr){
		try {
			//return Response.ok(LocalTime.from(hora.toInstant().atZone(ZoneId.systemDefault()).toLocalTime())).build();
			rs.confirmarReserva(dtr.getIdCiudadano(), dtr.getIdEnfermedad(), dtr.getIdPlan(), dtr.getIdVacunatorio(),
					LocalDate.from(dtr.getFecha().toInstant().atZone(ZoneId.systemDefault()).toLocalDate()),
					LocalTime.from(dtr.getHora().toInstant().atZone(ZoneId.systemDefault()).toLocalTime()));
		} catch (DateTimeException | UsuarioInexistente | PlanVacunacionInexistente | VacunatorioNoCargadoException | EnfermedadInexistente
				| CupoInexistente e) {
			
		}
	}
		/*(@FormParam("idUser") int idCiudadano, @FormParam("idEnf") String idEnfermedad, @FormParam("idPlan") int idPlan, @FormParam("idVac") String idVacunatorio,
			@FormParam("fecha")Date fecha, @FormParam("hora")Date hora){
		if (idEnfermedad == null || idVacunatorio==null || fecha==null || hora == null) {
			ResponseBuilder rb = Response.status(Status.BAD_REQUEST);
			return rb.build();
		}
		try {
			//return Response.ok(LocalTime.from(hora.toInstant().atZone(ZoneId.systemDefault()).toLocalTime())).build();
			rs.confirmarReserva(idCiudadano, idEnfermedad, idPlan, idVacunatorio, LocalDate.from(fecha.toInstant().atZone(ZoneId.systemDefault()).toLocalDate()), LocalTime.from(hora.toInstant().atZone(ZoneId.systemDefault()).toLocalTime()));
			return Response.ok().build();
		} catch (DateTimeException e | UsuarioInexistente | PlanVacunacionInexistente | VacunatorioNoCargadoException | EnfermedadInexistente
				| CupoInexistente e) {
			ResponseBuilder rb = Response.status(Status.BAD_REQUEST);
			return rb.entity(e.getMessage()).build();
		}
	}
	*/
	
	/*
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
		} catch (EnfermedadInexistente | PlanVacunacionInexistente  e) {
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
	@Path("/fecha")///{vac}/{date}")
	public Response seleccionarFecha(@QueryParam("vac") String idVacunatorio, @QueryParam("date") Date fecha){
		if (idVacunatorio==null || fecha==null) {
			ResponseBuilder rb = Response.status(Status.BAD_REQUEST);
			return rb.build();
		}
		try {
			return Response.ok(rs.seleccionarFecha(LocalDate.from(fecha.toInstant().atZone(ZoneId.systemDefault()).toLocalDate()), idVacunatorio)).build();
		} catch (DateTimeException | VacunatorioNoCargadoException e) {
			ResponseBuilder rb = Response.status(Status.BAD_REQUEST);
			return rb.entity(e.getMessage()).build();
		}
	}
	
	@POST
	@Path("/confirmar")
	public Response confirmarReserva(DtDatosReserva dtr){
		if (dtr.getIdEnfermedad()==null || dtr.getIdVacunatorio()==null || dtr.getFecha()==null || dtr.getHora()==null) {
			ResponseBuilder rb = Response.status(Status.BAD_REQUEST);
			return rb.build();
		}
		try {
			//return Response.ok(LocalTime.from(hora.toInstant().atZone(ZoneId.systemDefault()).toLocalTime())).build();
			rs.confirmarReserva(dtr.getIdCiudadano(), dtr.getIdEnfermedad(), dtr.getIdPlan(), dtr.getIdVacunatorio(),
					LocalDate.from(dtr.getFecha().toInstant().atZone(ZoneId.systemDefault()).toLocalDate()),
					LocalTime.from(dtr.getHora().toInstant().atZone(ZoneId.systemDefault()).toLocalTime()));
			return Response.ok().build();
		} catch (DateTimeException | UsuarioInexistente | PlanVacunacionInexistente | VacunatorioNoCargadoException | EnfermedadInexistente
				| CupoInexistente e) {
			ResponseBuilder rb = Response.status(Status.BAD_REQUEST);
			return rb.entity(e.getMessage()).build();
		}
	}
	*/
	
}
