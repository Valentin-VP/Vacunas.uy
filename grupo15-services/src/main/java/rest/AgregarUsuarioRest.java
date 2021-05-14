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


import datatypes.DtUsuarioInterno;
import datatypes.ErrorInfo;
import exceptions.UsuarioExistente;


import interfaces.IUsuarioLocal;

@SessionScoped
@Path("/agregarUsuario")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class AgregarUsuarioRest implements Serializable {
	@EJB
	IUsuarioLocal usu;
	

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	
	@POST
	@Path("/usuario")
	public Response agregarUsuario(DtUsuarioInterno dt){
	
			try {
				usu.agregarUsuarioInterno(dt.getIdUsuario(), dt.getNombre(), dt.getApellido(), dt.getFechaNac(), dt.getEmail(), 
						dt.getDireccion(), dt.getSexo(), dt.getPassword(), dt.getRol());
				return Response.ok().build();
			} catch (UsuarioExistente e) {
				return Response.serverError().entity(new ErrorInfo(400, e.getMessage())).status(400).build();
			}
			
		
			
		}

	}

