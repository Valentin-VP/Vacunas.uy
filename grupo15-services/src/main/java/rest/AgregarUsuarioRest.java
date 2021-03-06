package rest;

import java.io.Serializable;
import java.util.Base64;
import java.util.List;
import java.util.StringTokenizer;

import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.naming.NamingException;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.jose4j.lang.JoseException;

import datatypes.DtLdap;
import interfaces.ILdapLocal;
import rest.filter.TokenSecurity;

@SessionScoped
@Path("/agregarUsuario")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class AgregarUsuarioRest implements Serializable {

	private static final long serialVersionUID = 1L;

	@EJB
	ILdapLocal l;

	public AgregarUsuarioRest() {
		// TODO Auto-generated constructor stub
	}

	@POST
	@Path("/usuario")
	public void agregarUsuario(DtLdap dt) {

		l.addUser(dt.getApellido(), dt.getCi(), dt.getNombre(), dt.getTipoUser(), dt.getPassword());

	}

	

}
