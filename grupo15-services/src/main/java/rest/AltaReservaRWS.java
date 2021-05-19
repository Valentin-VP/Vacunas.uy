package rest;

import java.io.Serializable;
import java.time.DateTimeException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.security.DeclareRoles;
import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
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
import datatypes.ErrorInfo;
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
import rest.filter.AuthenticationFilter;

@DeclareRoles({"vacunador", "ciudadano", "interno"})
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
	/*
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
	@Path("/pv")
	public ArrayList<DtEtapa> seleccionarPlan(@QueryParam("p") int plan, @QueryParam("u") int user){
		try {
			return rs.seleccionarPlanVacunacion(plan, user);
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
	*/
	/*
		(@FormParam("idUser") int idCiudadano, @FormParam("idEnf") String idEnfermedad, @FormParam("idPlan") int idPlan, @FormParam("idVac") String idVacunatorio,
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
			return Response.serverError().entity(new ErrorInfo(400, e.getMessage())).status(400).build();
		}
	}
	*/
	
	@RolesAllowed({"ciudadano"}) 
	//@PermitAll
	@GET
	@Path("/enf")
	public Response listarEnfermedades(){
		try {
			return Response.ok(rs.listarEnfermedades()).build();
		} catch (EnfermedadInexistente e) {
			return Response.serverError().entity(new ErrorInfo(200, e.getMessage())).status(200).build();
		}
	}
	
	@RolesAllowed({"ciudadano"}) 
	//@PermitAll
	@GET
	@Path("/vac")
	public Response listarVacunatorios(){
		try {
			return Response.ok(vs.listarVacunatorio()).build();
		} catch (VacunatoriosNoCargadosException e) {
			return Response.serverError().entity(new ErrorInfo(200, e.getMessage())).status(200).build();
		}

	}
	
	@RolesAllowed({"ciudadano"})
	//@PermitAll
	@GET
	@Path("/enf/{e}")
	public Response seleccionarEnfermedad(@PathParam("e") String enfermedad){
		try {
			return Response.ok(rs.seleccionarEnfermedad(enfermedad)).build();
		} catch (EnfermedadInexistente | PlanVacunacionInexistente  e) {
			return Response.serverError().entity(new ErrorInfo(200, e.getMessage())).status(200).build();
		}
	}
	
	@RolesAllowed({"ciudadano"}) 
	//@PermitAll
	@GET
	@Path("/pv/")
	public Response seleccionarPlan(@Context HttpHeaders headers, @QueryParam("p") int plan){
		try {
			List<String> id = headers.getRequestHeader( AuthenticationFilter.HEADER_PROPERTY_ID ); 
			return Response.ok(rs.seleccionarPlanVacunacion(plan, Integer.parseInt(id.get(0)))).build();
		} catch (PlanVacunacionInexistente | EtapaInexistente | UsuarioInexistente e) {
			return Response.serverError().entity(new ErrorInfo(200, e.getMessage())).status(200).build();
		}
	}
	
	@RolesAllowed({"ciudadano"}) 
	//@PermitAll
	@GET
	@Path("/fecha")///{vac}/{date}")
	public Response seleccionarFecha(@Context HttpHeaders headers, @QueryParam("vac") String idVacunatorio, @QueryParam("date") String fecha, @QueryParam("p") int plan){
		if (idVacunatorio==null || fecha==null) {
			ResponseBuilder rb = Response.status(Status.BAD_REQUEST);
			return rb.build();
		}
		try {
			List<String> id = headers.getRequestHeader( AuthenticationFilter.HEADER_PROPERTY_ID ); 
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
			LocalDate f = LocalDate.parse(fecha, formatter);
			//LocalDate f = LocalDate.from(fecha.toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
			//Date nuevaFecha = Date.from(f.atStartOfDay(ZoneId.systemDefault()).toInstant());
			return Response.ok(rs.seleccionarFecha(f, idVacunatorio, plan, Integer.parseInt(id.get(0)))).build();
		} catch (DateTimeException | VacunatorioNoCargadoException | PlanVacunacionInexistente | UsuarioInexistente | EtapaInexistente e) {
			return Response.serverError().entity(new ErrorInfo(200, e.getMessage())).status(200).build();
		}
	}
	
	//@RolesAllowed({"ciudadano"}) 
	@PermitAll
	@POST
	@Path("/confirmar")
	public Response confirmarReserva(@Context HttpHeaders headers, DtDatosReserva dtr){
		if (dtr.getIdEnfermedad()==null || dtr.getIdVacunatorio()==null || dtr.getFecha()==null || dtr.getHora()==null) {
			ResponseBuilder rb = Response.status(Status.BAD_REQUEST);
			return rb.build();
		}
		try {
			List<String> id = headers.getRequestHeader( AuthenticationFilter.HEADER_PROPERTY_ID ); 
			System.out.println(id);
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
			DateTimeFormatter formatter2 = DateTimeFormatter.ofPattern("HH:mm:ss");
			LocalDate f = LocalDate.parse(dtr.getFecha(), formatter);
			LocalTime h = LocalTime.parse(dtr.getHora(), formatter2);
			
			//return Response.ok(LocalTime.from(hora.toInstant().atZone(ZoneId.systemDefault()).toLocalTime())).build();
			rs.confirmarReserva(Integer.parseInt(id.get(0)), dtr.getIdEnfermedad(), dtr.getIdPlan(), dtr.getIdVacunatorio(),
					f,//LocalDate.from(Instant.from(f).atZone(ZoneId.systemDefault()).toLocalDate()),
					h);//LocalTime.from(Instant.from(h).atZone(ZoneId.systemDefault()).toLocalTime()));
			return Response.ok().build();
		} catch (DateTimeException | UsuarioInexistente | PlanVacunacionInexistente | VacunatorioNoCargadoException | EnfermedadInexistente
				| CupoInexistente | EtapaInexistente e) {
			return Response.serverError().entity(new ErrorInfo(200, e.getMessage())).status(200).build();
		}
	}
	
	
}
