package rest;

import java.io.Serializable;

import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.Response.Status;

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
	
	@GET
	@Path("/asignado")
	public Response consultarPuestoVacunador(@QueryParam("user") int idVacunador, @QueryParam("vact") String idVacunatorio){
		if (idVacunatorio==null) {
			ResponseBuilder rb = Response.status(Status.BAD_REQUEST);
			return rb.build();
		}
		try {
			return Response.ok(vs.consultarPuestoAsignadoVacunador(idVacunador, idVacunatorio)).build();
		} catch (VacunatorioNoCargadoException | UsuarioInexistente | VacunadorSinAsignar e) {
			ResponseBuilder rb = Response.status(Status.BAD_REQUEST);
			return rb.entity(e.getMessage()).build();
		}
	}

}
