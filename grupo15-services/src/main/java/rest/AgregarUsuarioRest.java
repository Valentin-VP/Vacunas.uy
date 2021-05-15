package rest;

import java.io.Serializable;


import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.ws.rs.Consumes;

import javax.ws.rs.POST;
import javax.ws.rs.Path;

import javax.ws.rs.Produces;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import datatypes.DtLdap;
import ldap.ILdap;
import ldap.Ldap;
import interfaces.IUsuarioLocal;

@SessionScoped
@Path("/agregarUsuario")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class AgregarUsuarioRest implements Serializable {
	@EJB
	ILdap l;
	

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	
	@POST
	@Path("/usuario")
	public Response agregarUsuario(DtLdap dt){
	
			l.addUser(dt.getIdUsuario(), dt.getApellido(), dt.getNombre(),dt.getRol());
			return Response.ok().build();
			
		
			
		}

	}

