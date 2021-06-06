package rest;

import java.util.logging.Logger;

import javax.annotation.security.DeclareRoles;
import javax.annotation.security.PermitAll;
import javax.ejb.EJB;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import exceptions.ConstanciaInexistente;
import interfaces.IConstanciaVacunaDAOLocal;
import rest.filter.ResponseBuilder;

@DeclareRoles({"vacunador", "ciudadano", "administrador", "autoridad"})
@Path("/monitor")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class MonitorVue {
	private final Logger LOGGER = Logger.getLogger(getClass().getName());
	@EJB(lookup = "java:global/grupo15/grupo15-ejb/ControladorConstanciaVacuna!interfaces.IConstanciaVacunaDAOLocal")
	private IConstanciaVacunaDAOLocal IConstancia;
	public MonitorVue(){}
	
	@GET
	@Path("/vacunados")
	@PermitAll
	public Response getVacunados() { //retorna todos los vacunados por mes dia y año
		int dia, mes, anio;
		LOGGER.info("Entro a getVacunados");
			dia = IConstancia.listarConstanciasPeriodo(1); //para setear el dia
			mes = IConstancia.listarConstanciasPeriodo(30); //para setear el mes
			anio = IConstancia.listarConstanciasPeriodo(365); //para setear el anio
		
		JSONObject datos = new JSONObject();
        try {
            datos.put("dia", dia);
            datos.put("mes", mes);
            datos.put("anio", anio);
            return ResponseBuilder.createResponse(Response.Status.OK, datos);                       
        } catch (JSONException e) {
            return ResponseBuilder.createResponse(Response.Status.BAD_REQUEST,
                    e.getMessage());
        }
	}

}
