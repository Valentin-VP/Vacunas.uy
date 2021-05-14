package rest.filter;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

import javax.annotation.Priority;
import javax.annotation.security.DenyAll;
import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.ejb.EJB;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ResourceInfo;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import javax.ws.rs.Priorities;
import javax.ws.rs.ext.Provider;

import org.jose4j.jwt.consumer.InvalidJwtException;

import datatypes.DtUsuario;
import exceptions.UsuarioInexistente;
import interfaces.IUsuarioLocal;

/**
 * The AuthenticationFilter verifies the access permissions for a user based on
 * the provided jwt token and role annotations
 **/
@Provider
@Priority(Priorities.AUTHENTICATION)
public class AuthenticationFilter implements javax.ws.rs.container.ContainerRequestFilter {

	private final Logger LOGGER = Logger.getLogger(getClass().getName());

	@EJB(lookup = "java:global/grupo15/grupo15-ejb/ControladorUsuario!interfaces.IUsuarioLocal")
	private IUsuarioLocal IUsuarioLocal;

	@Context
	private ResourceInfo resourceInfo;

	public static final String HEADER_PROPERTY_ID = "id";
	public static final String HEADER_PROPERTY_TIPO = "tipo";
	public static final String AUTHORIZATION_PROPERTY = "x-access-token";

	// Do not use static responses, rebuild reponses every time
	/*
	 * private static final String ACCESS_REFRESH =
	 * "Token expired. Please authenticate again!"; private static final String
	 * ACCESS_INVALID_TOKEN = "Token invalid. Please authenticate again!"; private
	 * static final String ACCESS_DENIED = "Not allowed to access this resource!";
	 * private static final String ACCESS_FORBIDDEN = "Access forbidden!";
	 */

	@Override
	public void filter(ContainerRequestContext requestContext) {
		Method method = resourceInfo.getResourceMethod();
		// everybody can access (e.g. user/create or user/authenticate)
		LOGGER.info("Filtrando Request...");
		if (!method.isAnnotationPresent(PermitAll.class)) {
			// nobody can access
			if (method.isAnnotationPresent(DenyAll.class)) {
				requestContext
						.abortWith(ResponseBuilder.createResponse(Response.Status.FORBIDDEN, "Access forbidden!"));
				return;
			}

			// get request headers to extract jwt token
			final MultivaluedMap<String, String> headers = requestContext.getHeaders();
			final List<String> authProperty = headers.get("x-access-token");
			//final List<String> tipoUsuarioProp = headers.get("tipo");

			// block access if no authorization information is provided
			if (authProperty == null || authProperty.isEmpty()) {
				LOGGER.warning("No token provided!");
				requestContext.abortWith(ResponseBuilder.createResponse(Response.Status.UNAUTHORIZED,
						"Not allowed to access this resource!"));
				return;
			}

			/*
			 * if (tipoUsuarioProp == null || tipoUsuarioProp.isEmpty()) {
			 * LOGGER.warning("No se informo tipo de Usuario!");
			 * requestContext.abortWith(ResponseBuilder.createResponse(Response.Status.
			 * UNAUTHORIZED, "Not allowed to access this resource!")); return; }
			 */

			Integer id = null;
			String tipoUsuario = null;
			String jwt = authProperty.get(0);
			//String tipoUsuario = tipoUsuarioProp.get(0);

			// try to decode the jwt - deny access if no valid token provided
			try {
				id = Integer.parseInt(TokenSecurity.getIdClaim(TokenSecurity.validateJwtToken(jwt)));
				tipoUsuario = TokenSecurity.getTipoUsuarioClaim(TokenSecurity.validateJwtToken(jwt));
				LOGGER.severe("Claims recuperados: " + id + " y " + tipoUsuario);
			} catch (InvalidJwtException e) {
				LOGGER.warning("Invalid token provided!");
				requestContext.abortWith(ResponseBuilder.createResponse(Response.Status.UNAUTHORIZED,
						"Token invalid. Please authenticate again!"));
				return;
			}

			// check if token matches an user token (set in user/authenticate)
			// UserDAO userDao = UserDAOFactory.getUserDAO();
			DtUsuario user = null;
			// Recupero el ciudadano o vacunador segun corresponda, para chequear si tiene un token de sesion activo. Si ingreso como ciudadano, no va a tener como vacunador.
			try {
				if(tipoUsuario.equals("vacunador")) {
					user = IUsuarioLocal.buscarVacunador(id);
				}else {
					if (tipoUsuario.equals("ciudadano")) {
						user = IUsuarioLocal.buscarCiudadano(id);
					}
				}
				//user = IUsuarioLocal.buscarUsuario(id);
			} catch (UsuarioInexistente e) {
				LOGGER.warning("Token missmatch!");
				requestContext.abortWith(ResponseBuilder.createResponse(Response.Status.UNAUTHORIZED,
						"Not allowed to access this resource!"));
				return;
			}

			// UserSecurity userSecurity = userDao.getUserAuthentication( user.getId() );

			// token does not match with token stored in database - enforce re
			// authentication
			if (!user.getToken().equals(jwt)) {
				LOGGER.warning("Token expired!");
				requestContext.abortWith(ResponseBuilder.createResponse(Response.Status.UNAUTHORIZED,
						"Token expired. Please authenticate again!"));
				return;
			}

			// verify user access from provided roles ("admin", "user", "guest")
			if (method.isAnnotationPresent(RolesAllowed.class)) {
				// get annotated roles
				RolesAllowed rolesAnnotation = method.getAnnotation(RolesAllowed.class);
				Set<String> rolesSet = new HashSet<String>(Arrays.asList(rolesAnnotation.value()));

				// user valid?
				if (!isUserAllowed(tipoUsuario, rolesSet)) {
					LOGGER.warning("User does not have the rights to acces this resource!");
					requestContext.abortWith(ResponseBuilder.createResponse(Response.Status.UNAUTHORIZED,
							"Not allowed to access this resource!"));
					return;
				}
			}

			// set header param id y tipoUsuario-> Para identificacion del usuario en los servicios rest
			// Para no decodificar de nuevo el jwt en los servicios rest
			List<String> idList = new ArrayList<String>();
			List<String> tipoUsuarioList = new ArrayList<String>();
			idList.add(id.toString());
			tipoUsuarioList.add(tipoUsuario);
			LOGGER.warning("Acceso Permitido - Seteando id y tipoUsuario en Headers");
			headers.put("id", idList);
			headers.put("tipoUsuario", tipoUsuarioList);
		}
	}

	private boolean isUserAllowed(final String userRole, final Set<String> rolesSet) {
		boolean isAllowed = false;

		if (rolesSet.contains(userRole)) {
			isAllowed = true;
		}

		return isAllowed;
	}
}