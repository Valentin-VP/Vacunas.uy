package rest;

import java.util.ArrayList;
import java.util.Map;
import java.util.logging.Logger;

import javax.annotation.security.DeclareRoles;
import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.ejb.EJB;
import javax.ws.rs.Consumes;
import javax.ws.rs.CookieParam;
import javax.ws.rs.GET;
import javax.ws.rs.NotAuthorizedException;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.Cookie;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.jose4j.jwt.consumer.InvalidJwtException;

import datatypes.DtEnfermedad;
import datatypes.DtEtapa;
import datatypes.DtGetVacunados;
import datatypes.DtPlanVacunacion;
import datatypes.DtUsuarioExterno;
import datatypes.DtVacuna;
import datatypes.ErrorInfo;
import exceptions.EnfermedadInexistente;
import exceptions.EtapaInexistente;
import exceptions.PlanVacunacionInexistente;
import exceptions.UsuarioInexistente;
import exceptions.VacunaInexistente;
import interfaces.IConstanciaVacunaDAOLocal;
import interfaces.IControladorVacunaLocal;
import interfaces.IEnfermedadLocal;
import interfaces.IEtapaLocal;
import interfaces.IPlanVacunacionLocal;
import interfaces.IReservaDAOLocal;
import rest.filter.ResponseBuilder;
import rest.filter.TokenSecurity;

@DeclareRoles({"vacunador", "ciudadano", "administrador", "autoridad"})
@Path("/monitor")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class MonitorVue {
	private final Logger LOGGER = Logger.getLogger(getClass().getName());
	@EJB(lookup = "java:global/grupo15/grupo15-ejb/ControladorConstanciaVacuna!interfaces.IConstanciaVacunaDAOLocal")
	private IConstanciaVacunaDAOLocal IConstancia;
	
	@EJB
	IReservaDAOLocal rs;
	
	@EJB
	IPlanVacunacionLocal pv;
	
	@EJB
	IControladorVacunaLocal vac;
	
	@EJB
	IEtapaLocal et;
	
	public MonitorVue(){}
	
	@POST
	@Path("/vacunados")
	@PermitAll
	public Response getVacunados(String datos) {
		String enfermedad = "";
		String plan = "";
		String vacuna = "";
		try {
			JSONObject jsonObject = new JSONObject(datos);
			enfermedad = jsonObject.getString("enfermedad");
			String[] aux = jsonObject.getString("plan").split("-");//el plan me llega como "id-nombre"
			plan = aux[0];
			vacuna = jsonObject.getString("vacuna");
		} catch (JSONException e1) {
			return ResponseBuilder.createResponse(Response.Status.BAD_REQUEST, e1.getMessage());
		}
		System.out.println(plan);
		
		int dia = -1;
		int mes = -1;
		int anio = -1;
		
		if(enfermedad.equals("Todosl")) {
			if(plan.equals("Todos")) {
				if(vacuna.equals("Todos")) {
					//no filtro
					//----------------------------------------------------------------------
					dia = IConstancia.listarConstanciasPeriodo(1);
					mes = IConstancia.listarConstanciasPeriodo(30);
					anio = IConstancia.listarConstanciasPeriodo(365);
					//----------------------------------------------------------------------
				}else {
					//filtro por vacuna
					//----------------------------------------------------------------------
					dia = IConstancia.filtroPorVacuna(1, vacuna);
					mes = IConstancia.filtroPorVacuna(30, vacuna);
					anio = IConstancia.filtroPorVacuna(365, vacuna);
					//----------------------------------------------------------------------
				}
			}else {
				if(vacuna.equals("Todos")) {
					//filtro por plan
					//----------------------------------------------------------------------
					dia = IConstancia.filtroPorPlan(1, plan);
					mes = IConstancia.filtroPorPlan(30, plan);
					anio = IConstancia.filtroPorPlan(365, plan);
					
					//----------------------------------------------------------------------
				}else {
					//filtro por plan y vacuna
					//----------------------------------------------------------------------
					ArrayList<String> vacunas = new ArrayList<String>();
					DtPlanVacunacion planVac;
					try {
						planVac = pv.obtenerPlanVacunacion(Integer.valueOf(plan));
						for(DtEtapa dtEtp: planVac.getEtapa()) {			
							vacunas.add(dtEtp.getVacuna());
						}
						if(!vacunas.isEmpty()) {
							dia = IConstancia.filtroPorVacuna(1, vacuna);
							mes = IConstancia.filtroPorVacuna(30, vacuna);
							anio = IConstancia.filtroPorVacuna(365, vacuna);
						}
					} catch (NumberFormatException | PlanVacunacionInexistente e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					//----------------------------------------------------------------------
				}
			}
		}else {
			if(plan.equals("Todos")) {
				if(vacuna.equals("Todos")) {
					//filtro por enfermedad
					//----------------------------------------------------------------------
					dia = IConstancia.filtroPorEnfermedad(1, enfermedad);
					mes = IConstancia.filtroPorEnfermedad(30, enfermedad);
					anio = IConstancia.filtroPorEnfermedad(365, enfermedad);
					//----------------------------------------------------------------------
				}else {
					//filtro por enfermedad y vacuna
					//----------------------------------------------------------------------
					
					//----------------------------------------------------------------------
				}
			}else {
				if(vacuna.equals("Todos")) {
					//filtro por enfermedad y plan
					//----------------------------------------------------------------------
					ArrayList<String> planes = new ArrayList<String>();
					try {
						for(DtPlanVacunacion dtP: pv.listarPlanesVacunacion()) {
							System.out.println(dtP.getEnfermedad());
							if(dtP.getEnfermedad().equals(enfermedad))
								planes.add(dtP.getId() + "-" + dtP.getNombre());
						}
						if(!planes.isEmpty()) {
							dia = IConstancia.filtroPorPlan(1, plan);
							mes = IConstancia.filtroPorPlan(30, plan);
							anio = IConstancia.filtroPorPlan(365, plan);
						}
						
					} catch (PlanVacunacionInexistente e) {
						 return ResponseBuilder.createResponse(Response.Status.BAD_REQUEST, e.getMessage());
					}
					//----------------------------------------------------------------------
				}else {
					//filtro por enfermedad plan y vacuna
					//----------------------------------------------------------------------
					
					//----------------------------------------------------------------------
				}
			}
		}
		//luego de settear los datos genero el JSONObject
		JSONObject respuesta = new JSONObject();
	    try {
	        respuesta.put("dia", dia);
	        respuesta.put("mes", mes);
	        respuesta.put("anio", anio);
	        return ResponseBuilder.createResponse(Response.Status.OK, datos.toString());                       
	    } catch (JSONException e) {
	        return ResponseBuilder.createResponse(Response.Status.BAD_REQUEST, e.getMessage());
	    }
	}
	
	
	
	//retorna los vacunados de cada vacuna
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
	
	
	
	//retorna los vacunados de cada enfermedad
	@GET
	@Path("/vacunadosporenf")
	@PermitAll
	public Response getVacunadosPorEnfermedad() {
		Map<String, String> vacunados= IConstancia.listarConstanciaPorEnfermedad();
		try {
			JSONArray datos = new JSONArray();
			for (Map.Entry<String, String> entry : vacunados.entrySet()) {
			    JSONObject dato = new JSONObject();
			    dato.put("idEnfermedad", entry.getKey());
			    dato.put("vacunados", entry.getValue());
			    datos.put(dato);
			}
			return Response.ok(datos.toString()).build();
		} catch (JSONException e) {
            return ResponseBuilder.createResponse(Response.Status.BAD_REQUEST,
                    e.getMessage());
        }
	}
	
	
	
	//retorna los los planes que tienen la enfermedad
	@PermitAll
	@GET
	@Path("/enf/{e}")
	public Response seleccionarEnfermedad(@PathParam("e") String enfermedad){
		try {
			if(enfermedad.equals("Todos")) {
				ArrayList<String> planes = new ArrayList<String>();
				for(DtPlanVacunacion dtP: pv.listarPlanesVacunacion()) {
					planes.add(dtP.getId() + "-" + dtP.getNombre());
				}
				return Response.ok(planes).build();
			}else {
				ArrayList<String> planes = new ArrayList<String>();
				for(DtPlanVacunacion dtP: pv.listarPlanesVacunacion()) {
					System.out.println(dtP.getEnfermedad());
					if(dtP.getEnfermedad().equals(enfermedad))
						planes.add(dtP.getId() + "-" + dtP.getNombre());
				}
				return Response.ok(planes).build();
			}
		} catch (PlanVacunacionInexistente  e) {
			return Response.serverError().entity(new ErrorInfo(200, e.getMessage())).status(200).build();
		}
	}
	
	 
	//retorna las vacunas de una enfermedad, de un plan o de un plan y una enfermedad
	@PermitAll
	@GET
	@Path("/pv/{e}/{p}")
	public Response seleccionarPlan(@PathParam("e") String enfermedad, @PathParam("p") String plan){
		try {
			if(plan.equals("Todos")) {//si retorna un todos
				if(enfermedad.equals("Todos")) {
					ArrayList<String> vacunas = new ArrayList<String>();
					for(DtVacuna dtVac: vac.listarVacunas()) {
						vacunas.add(dtVac.getNombre());
					}
					return Response.ok(vacunas).build();
				}else {
					ArrayList<String> vacunas = new ArrayList<String>();
					for(DtVacuna dtVac: vac.listarVacunas()) {
						if(dtVac.getDtEnf().getNombre().equals(enfermedad)) {
							vacunas.add(dtVac.getNombre());
						}
					}
					return Response.ok(vacunas).build();
				}
			}else {
				String[] id = plan.split("-");
				String idPlan = id[0];
				ArrayList<String> vacunas = new ArrayList<String>();
				DtPlanVacunacion planVac = pv.obtenerPlanVacunacion(Integer.valueOf(idPlan));
				for(DtEtapa dtEtp: planVac.getEtapa()) {			
					vacunas.add(dtEtp.getVacuna());
				}
				return Response.ok(vacunas).build();
			}
		} catch ( VacunaInexistente | NumberFormatException | PlanVacunacionInexistente e) {
			return ResponseBuilder.createResponse(Response.Status.BAD_REQUEST, e.getMessage());
		}
	}
	
}
