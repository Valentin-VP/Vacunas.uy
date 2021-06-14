package rest;

import java.util.Map;
import java.util.logging.Logger;

import javax.annotation.security.DeclareRoles;
import javax.annotation.security.PermitAll;
import javax.ejb.EJB;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.codehaus.jettison.json.JSONArray;
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
	@Path("/vacunadosdma")
	@PermitAll
	public Response getVacunados() { //retorna todos los vacunados por mes dia y año
		LOGGER.info("Entro a getVacunados");
			int dia = IConstancia.listarConstanciasPeriodo(1); //para setear el dia
			int mes = IConstancia.listarConstanciasPeriodo(30); //para setear el mes
			int anio = IConstancia.listarConstanciasPeriodo(365); //para setear el anio
		
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
	
	@GET
	@Path("/vacunadosdmae")
	@PermitAll        //retorna los vacunados por dia mes anio de una enfermedad
	public Response getVacunadosEnf(@QueryParam("enf") String enfermedad) {
		int dia = IConstancia.listarConstanciasPeriodoEnfermedad(1, enfermedad); //para setear el dia
		int mes = IConstancia.listarConstanciasPeriodoEnfermedad(30, enfermedad); //para setear el mes
		int anio = IConstancia.listarConstanciasPeriodoEnfermedad(365, enfermedad); //para setear el anio
		
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

	@GET
	@Path("/vacunadosporvacs")
	@PermitAll
	public Response getVacunadosPorVacunas() {
		Map<String, String> vacunas= IConstancia.listarConstanciaPorVacuna();
		try {
			JSONArray datos = new JSONArray();
			for (Map.Entry<String, String> entry : vacunas.entrySet()) {
			    System.out.println(entry.getKey()+": " + entry.getValue());
			    JSONObject dato = new JSONObject();
			    dato.put("idVacuna", entry.getKey());
			    dato.put("vacunados", entry.getValue());
			    datos.put(dato);
			}
			return Response.ok(datos.toString()).build();
		} catch (JSONException e) {
            return ResponseBuilder.createResponse(Response.Status.BAD_REQUEST,
                    e.getMessage());
        }
	}	
}
