package rest;

import java.util.List;
import java.util.logging.Logger;

import javax.annotation.security.DeclareRoles;
import javax.annotation.security.RolesAllowed;
import javax.ejb.EJB;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.NotAuthorizedException;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;

import org.glassfish.jersey.server.ResourceConfig;

import exceptions.UsuarioInexistente;
import interfaces.IUsuarioLocal;
import rest.filter.AuthenticationFilter;

@DeclareRoles({"vacunador", "ciudadano", "interno"})
@Path("/mobile")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class LoginMobileDemo {

	//private static final long serialVersionUID = 1L;
	private final Logger LOGGER = Logger.getLogger(getClass().getName());
	@EJB(lookup = "java:global/grupo15/grupo15-ejb/ControladorUsuario!interfaces.IUsuarioLocal")
	private IUsuarioLocal IUsuarioLocal;
	public LoginMobileDemo() {}
	
	@GET
	@Path("/id")
	@RolesAllowed({"ciudadano"})
	public String getCedulaJWT(@Context HttpHeaders headers) {
		String id = getId( headers );
		LOGGER.info("Recuperando CI de JWT en Header: " + id);
		try {
			return IUsuarioLocal.buscarCiudadano(Integer.parseInt(id)).getNombre();
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UsuarioInexistente e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	@GET
	@Path("/forbidden")
	@RolesAllowed({"vacunador"}) 
	public void metodoVacunador() {
		LOGGER.info("Metodo no permitido para ciudadano. Este print nunca deberiamos verlo dede la app mobile!");
	}
	
    private String getId( HttpHeaders headers) {
        // Obtener CI del AuthenticationFilter
        List<String> id = headers.getRequestHeader( AuthenticationFilter.HEADER_PROPERTY_ID );   
        if( id == null || id.size() != 1 )
            throw new NotAuthorizedException("Unauthorized!");
        return id.get(0);
    }
}
