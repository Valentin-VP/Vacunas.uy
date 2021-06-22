package rest;

import java.util.logging.Logger;

import javax.annotation.security.DeclareRoles;
import javax.annotation.security.PermitAll;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.NewCookie;
import javax.ws.rs.core.Response;

import org.jose4j.lang.JoseException;

import datatypes.DtCiudadano;
import datatypes.DtUsuarioInterno;
import datatypes.DtVacunador;
import datatypes.Rol;
import exceptions.UsuarioExistente;
import exceptions.UsuarioInexistente;
import interfaces.IUsuarioLocal;
import rest.filter.ResponseBuilder;
import rest.filter.TokenSecurity;

@DeclareRoles({ "vacunador", "ciudadano", "administrador", "autoridad" })
@RequestScoped
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Path("/login")
public class LoginFalso {

	private final Logger LOGGER = Logger.getLogger(getClass().getName());

	@EJB
	private IUsuarioLocal IUsuarioLocal;

	public LoginFalso() {
	}

	@GET
	@Path("/fake")
	@PermitAll
	public Response getToken(@QueryParam("ci") String ci) {
		try {
			String token = TokenSecurity.generateJwtToken(ci, "ciudadano");
			IUsuarioLocal.agregarUsuarioCiudadano(Integer.parseInt(ci), null, null, null, null, null, null, null, false);
			DtCiudadano dt;
			dt = IUsuarioLocal.buscarCiudadano(Integer.parseInt(ci));
			dt.setToken(token);
			IUsuarioLocal.ModificarCiudadano(dt);
			NewCookie cookie = new NewCookie("x-access-token", token);
			return Response.ok().cookie(cookie).build();
		} catch (NumberFormatException | UsuarioExistente | JoseException | UsuarioInexistente e) {
			return ResponseBuilder.createResponse(Response.Status.BAD_REQUEST, "");
		}
	}
	
	@GET
	@Path("/fakeusuario")
	@PermitAll
	public Response getTokenUsuario(@QueryParam("ci") String ci, @QueryParam("tipo") String tipo) {
		try {
			String token = TokenSecurity.generateJwtToken(ci, tipo);
			switch(tipo){
				case "ciudadano":
					IUsuarioLocal.agregarUsuarioCiudadano(Integer.parseInt(ci), null, null, null, null, null, null, null, false);
					DtCiudadano ciu;
					ciu = IUsuarioLocal.buscarCiudadano(Integer.parseInt(ci));
					ciu.setToken(token);
					IUsuarioLocal.ModificarCiudadano(ciu);
					break;
				case "autoridad":
					IUsuarioLocal.agregarUsuarioInterno(Integer.parseInt(ci), ci, tipo, null, null, null, null, Rol.Autoridad);
					DtUsuarioInterno aut;
					aut = IUsuarioLocal.buscarUsuarioInterno(Integer.parseInt(ci));
					aut.setToken(token);
					IUsuarioLocal.ModificarUsuarioInterno(aut);
					break;
				case "administrador":
					IUsuarioLocal.agregarUsuarioInterno(Integer.parseInt(ci), ci, tipo, null, null, null, null, Rol.Administrador);
					DtUsuarioInterno adm;
					adm = IUsuarioLocal.buscarUsuarioInterno(Integer.parseInt(ci));
					adm.setToken(token);
					IUsuarioLocal.ModificarUsuarioInterno(adm);
					break;
				case "vacunador":
					IUsuarioLocal.agregarUsuarioVacunador(Integer.parseInt(ci), ci, tipo, null, token, null, null);
					DtVacunador vac;
					vac = IUsuarioLocal.buscarVacunador(Integer.parseInt(ci));
					vac.setToken(token);
					IUsuarioLocal.ModificarVacunador(vac);
					break;
			}
			NewCookie cookie = new NewCookie("x-access-token", token);
			return Response.ok().cookie(cookie).build();
		} catch (NumberFormatException | UsuarioExistente | JoseException | UsuarioInexistente e) {
			return ResponseBuilder.createResponse(Response.Status.BAD_REQUEST, "");
		}
	}
}
