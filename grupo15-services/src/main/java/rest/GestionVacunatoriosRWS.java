package rest;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.logging.Logger;

import javax.annotation.security.DeclareRoles;
import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.ejb.EJB;
import javax.ws.rs.Consumes;
import javax.ws.rs.CookieParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Cookie;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.jose4j.jwt.consumer.InvalidJwtException;

import datatypes.DtDireccion;
import datatypes.DtVacunatorio;
import exceptions.AccionInvalida;
import exceptions.PuestoCargadoException;
import exceptions.PuestoNoCargadosException;
import exceptions.ReglasCuposCargadoException;
import exceptions.VacunatorioCargadoException;
import exceptions.VacunatorioNoCargadoException;
import exceptions.VacunatoriosNoCargadosException;
import interfaces.IControladorPuestoLocal;
import interfaces.IControladorVacunatorioLocal;
import rest.filter.ResponseBuilder;
import rest.filter.TokenSecurity;

@DeclareRoles({ "vacunador", "ciudadano", "administrador", "autoridad" })
@Path("/vacunatorios")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class GestionVacunatoriosRWS {
	
	private final Logger LOGGER = Logger.getLogger(getClass().getName());
	
	@EJB
	private IControladorVacunatorioLocal iControladorVacunatorio;
	@EJB
	private IControladorPuestoLocal cp;

	public GestionVacunatoriosRWS() {}
	
	@RolesAllowed({ "administrador", "autoridad" })
    @GET
    @Path("/listar")
    public Response listarVacunatorios() {
        try {
            LOGGER.info("accediendo a listar vacunatorio");
            return Response.ok(iControladorVacunatorio.listarVacunatorio()).build();
        } catch (VacunatoriosNoCargadosException e) {
            return ResponseBuilder.createResponse(Response.Status.BAD_REQUEST,
                    e.getMessage());
        }
    }
	
	@RolesAllowed({ "administrador", "autoridad" })
	@GET
	@Path("/obtener")
	public Response obtenerVacunatorio(@CookieParam("x-access-token") Cookie cookie, @QueryParam("vacunatorio") String vacunatorio) {
		if (vacunatorio==null) {
			return ResponseBuilder.createResponse(Response.Status.BAD_REQUEST,
					"Se requiere el QueryParam 'vacunatorio'");
		}
		try {
			String token = cookie.getValue();
			String ci = null;
			try {
				ci = TokenSecurity.getIdClaim(TokenSecurity.validateJwtToken(token));
			} catch (InvalidJwtException e) {
				return ResponseBuilder.createResponse(Response.Status.BAD_REQUEST,
						"Error procesando JWT");
			}
	        if( ci == null)
	        	return ResponseBuilder.createResponse(Response.Status.UNAUTHORIZED,
						"No se ha obtenido ci de Cookie/Token");
			LOGGER.info("Cedula obtenida en REST: " + ci);
			return ResponseBuilder.createResponse(Response.Status.OK, toJson(iControladorVacunatorio.obtenerVacunatorio(vacunatorio)));
		} catch (JSONException e) {
			return ResponseBuilder.createResponse(Response.Status.BAD_REQUEST,
					e.getMessage());
		} catch (VacunatorioNoCargadoException e) {
			return ResponseBuilder.createResponse(Response.Status.BAD_REQUEST,
					e.getMessage());
		}
	}
	
	@RolesAllowed({ "administrador", "autoridad" })
	@POST
	@Path("/agregar")
	public Response agregarVacunatorio(String datos) {
		try {
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
			JSONObject datosInterno = new JSONObject(datos);
			DtDireccion dtDir = new DtDireccion(datosInterno.getString("direccion"), datosInterno.getString("barrio"), datosInterno.getString("departamento"));
			iControladorVacunatorio.agregarVacunatorio(datosInterno.getString("id"), datosInterno.getString("nombre"), dtDir,
					Integer.valueOf(datosInterno.getString("telefono")), Float.parseFloat(datosInterno.getString("latitud")), Float.parseFloat(datosInterno.getString("longitud")), datosInterno.getString("url"));
			iControladorVacunatorio.agregarReglasCupos(datosInterno.getString("id"), datosInterno.getString("idReglas"),
					Integer.parseInt(datosInterno.getString("duracionTurno")),
					LocalTime.parse(datosInterno.getString("horaApertura"), formatter), LocalTime.parse(datosInterno.getString("horaCierre"), formatter));
			iControladorVacunatorio.generarTokenVacunatorio(datosInterno.getString("id"));
			return ResponseBuilder.createResponse(Response.Status.CREATED, "Se ha agregado el nodo Vacunatorio con exito.");
		} catch ( NumberFormatException | JSONException | VacunatorioNoCargadoException | ReglasCuposCargadoException | VacunatorioCargadoException | AccionInvalida  e) {
			return ResponseBuilder.createResponse(Response.Status.BAD_REQUEST,
					e.getMessage());
		}
	}
	
	@RolesAllowed({ "administrador", "autoridad" })
	@POST
	@Path("/modificar")
	public Response modificarVacunatorio(String datos) {
		try {
			JSONObject datosInterno = new JSONObject(datos);
			DtDireccion dtDir = new DtDireccion(datosInterno.getString("direccion"), datosInterno.getString("barrio"), datosInterno.getString("departamento"));
			iControladorVacunatorio.modificarVacunatorio(datosInterno.getString("idVacunatorio"), datosInterno.getString("nombre"), dtDir,
					Integer.valueOf(datosInterno.getString("telefono")), Float.parseFloat(datosInterno.getString("latitud")), Float.parseFloat(datosInterno.getString("longitud")), datosInterno.getString("url"));
			return ResponseBuilder.createResponse(Response.Status.OK, "Se ha agregado el nodo Vacunatorio con exito.");
		} catch ( NumberFormatException | JSONException | VacunatorioNoCargadoException  e) {
			return ResponseBuilder.createResponse(Response.Status.BAD_REQUEST,
					e.getMessage());
		}
	}
	
	@RolesAllowed({ "administrador", "autoridad" })
	@POST
	@Path("/agregarPuesto")
	public Response agregarPuesto(String datos) {
		try {
			JSONObject datosInterno = new JSONObject(datos);
			cp.agregarPuesto(datosInterno.getString("idPuesto"), datosInterno.getString("idVacunatorio"));
			return ResponseBuilder.createResponse(Response.Status.CREATED, "Se ha agregado el puesto al vacunatorio.");
		} catch ( NumberFormatException | JSONException | PuestoCargadoException | VacunatorioNoCargadoException  e) {
			return ResponseBuilder.createResponse(Response.Status.BAD_REQUEST,
					e.getMessage());
		}
	}

	@RolesAllowed({ "administrador", "autoridad" })
	@GET
	@Path("/listarPuestos")
	public Response listarPuestos(@QueryParam("idVacunatorio") String idVacunatorio) {
		try {
			ArrayList<String> retorno = cp.listarPuestos(idVacunatorio);
			return Response.ok().entity(retorno).build();
		} catch ( NumberFormatException | PuestoNoCargadosException  e) {
			return ResponseBuilder.createResponse(Response.Status.BAD_REQUEST,
					e.getMessage());
		}
	}
	
	//@RolesAllowed({"autoridad"}) 
//	@PermitAll
//	@POST
//	@Path("/agregar")
//	public Response agregarVacunatorio(String datos){
//		try {
//			JSONObject datosInterno = new JSONObject(datos);
//			// pendiente
//		} catch (JSONException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	}
	private JSONObject toJson(DtVacunatorio dt) throws JSONException {
		JSONObject jsonObject = new JSONObject();
		jsonObject.put( "id", dt.getId() );
		jsonObject.put( "nombre", dt.getNombre() );
		jsonObject.put( "dtDir", toJsonDtDir(dt.getDtDir()));
		jsonObject.put( "telefono", dt.getTelefono() );
		jsonObject.put( "latitud", dt.getLatitud() );
		jsonObject.put( "longitud", dt.getLongitud() );
		jsonObject.put( "url", dt.getUrl() );
		jsonObject.put( "token", dt.getToken() );
		return jsonObject;
	}
	
	public JSONObject toJsonDtDir(DtDireccion dt) throws JSONException {
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("direccion", dt.getDireccion());
		jsonObject.put("barrio", dt.getBarrio());
		jsonObject.put("departamento", dt.getDepartamento());
		return jsonObject;
	}
}
