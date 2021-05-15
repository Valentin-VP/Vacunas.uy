package rest;

import java.io.Serializable;

import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;

import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import datatypes.DtLdap;

import interfaces.ILdapLocal;
import rest.filter.TokenSecurity;



@SessionScoped
@Path("/agregarUsuario")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class AgregarUsuarioRest implements Serializable {
	
	/**
	 * 
	 */
	
	@EJB
	ILdapLocal l;

	public AgregarUsuarioRest() {
		// TODO Auto-generated constructor stub
	}

	@POST
	@Path("/usuario")
	public void agregarUsuario(DtLdap dt) {
		
		l.addUser(dt.getApellido(), dt.getCi(), dt.getNombre(),dt.getTipoUser(), dt.getPassword());


	}
	
	//@GET
//	@Path("/authUsuario")
	//public Response autenticarUsuario(String ci, String password, @Context HttpHeaders headers )  {
		//Boolean isok;
	//	isok = l.authUser(ci, password);
	//	if (isok) {
			//String tipo = l.searchType(ci);
			// token = TokenSecurity.generateJwtToken(ci, tipo);
		//	return Response.status(Response.Status.SEE_OTHER)
		         //   .header(HttpHeaders.LOCATION, "/grupo15-services/logininterno")
		          //  .header("token", "bar")
		        //    .build();
		//}
	}
	

	


