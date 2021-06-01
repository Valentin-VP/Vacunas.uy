package rest;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.logging.Logger;

import javax.annotation.security.DeclareRoles;
import javax.annotation.security.PermitAll;
import javax.ejb.EJB;
import javax.ws.rs.Consumes;
import javax.ws.rs.CookieParam;
import javax.ws.rs.GET;
import javax.ws.rs.NotAuthorizedException;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Cookie;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.codehaus.jettison.json.JSONException;
import org.jose4j.jwt.consumer.InvalidJwtException;

import datatypes.ErrorInfo;
import exceptions.VacunaInexistente;
import exceptions.VacunatorioNoCargadoException;
import exceptions.VacunatoriosNoCargadosException;
import interfaces.IControladorVacunatorioLocal;
import rest.filter.JsonSerializable;
import rest.filter.ResponseBuilder;
import rest.filter.TokenSecurity;

@DeclareRoles({ "vacunador", "ciudadano", "administrador", "autoridad" })
@Path("/vacunatorios")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class GestionVacunatoriosRWS {
	
	private final Logger LOGGER = Logger.getLogger(getClass().getName());
	
	@EJB(lookup = "java:global/grupo15/grupo15-ejb/ControladorVacunatorio!interfaces.IControladorVacunatorioLocal")
	private IControladorVacunatorioLocal iControladorVacunatorio;

	public GestionVacunatoriosRWS() {}
	
	@PermitAll
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
	
	@PermitAll
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
			return ResponseBuilder.createResponse(Response.Status.OK, iControladorVacunatorio.obtenerVacunatorio(vacunatorio).toJson());
		} catch (JSONException e) {
			return ResponseBuilder.createResponse(Response.Status.BAD_REQUEST,
					e.getMessage());
		} catch (VacunatorioNoCargadoException e) {
			return ResponseBuilder.createResponse(Response.Status.BAD_REQUEST,
					e.getMessage());
		}
	}
	
	

}
