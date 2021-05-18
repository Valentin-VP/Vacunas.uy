package rest.filter;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

import javax.annotation.Priority;
import javax.annotation.security.DenyAll;
import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.ejb.EJB;
import javax.ws.rs.core.Cookie;
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
 * AuthenticationFilter verifica los permisos de aceso de un usuario basado en
 * el JWT de acceso, que incluye su rol, y verificando los roles de los REST
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

	@Override
	public void filter(ContainerRequestContext requestContext) {
		Method method = resourceInfo.getResourceMethod();
		// Si el metodo es PermitAll, todos pueden acceder aun sin token
		LOGGER.info("Filtrando Request...");
		if (!method.isAnnotationPresent(PermitAll.class)) {
			// Nadie tiene acceso a ese recurso
			if (method.isAnnotationPresent(DenyAll.class)) {
				requestContext
						.abortWith(ResponseBuilder.createResponse(Response.Status.FORBIDDEN, "Access forbidden!"));
				return;
			}

			// Se extrae jwt de la Cookie de sesion
			final MultivaluedMap<String, String> headers = requestContext.getHeaders();
			String cookieName = "x-access-token";
			Map<String, Cookie> cookies = requestContext.getCookies();
			if (cookies == null || cookies.isEmpty()) {
				LOGGER.warning("No token provided!");
				requestContext.abortWith(ResponseBuilder.createResponse(Response.Status.UNAUTHORIZED,
						"Not allowed to access this resource!"));
				return;
			}
			Cookie cookie = cookies.get(cookieName);
			// final List<String> authProperty = headers.get("x-access-token");

			Integer id = null;
			String tipoUsuario = null;
			String jwt = cookie.getValue();
			// String jwt = authProperty.get(0);
			// String tipoUsuario = tipoUsuarioProp.get(0);

			// Se decodifica el jwt - Acceso es denegado si no es un token valido
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

			DtUsuario user = null;
			// Recupero el ciudadano o vacunador segun corresponda, para chequear si tiene
			// un token de sesion activo. Si ingreso como ciudadano, no va a tener como
			// vacunador.
			try {
				if (tipoUsuario.equals("vacunador")) {
					user = IUsuarioLocal.buscarVacunador(id);
				} else {
					if (tipoUsuario.equals("ciudadano")) {
						user = IUsuarioLocal.buscarCiudadano(id);
					} else {
						if (tipoUsuario.equals("administrador") || tipoUsuario.equals("autoridad")) {
							user = IUsuarioLocal.buscarUsuarioInterno(id);
						}
					}
				}
			} catch (UsuarioInexistente e) {
				LOGGER.warning("Token missmatch!");
				requestContext.abortWith(ResponseBuilder.createResponse(Response.Status.UNAUTHORIZED,
						"Not allowed to access this resource!"));
				return;
			}

			// Si el token no coincide, se fuerza reautenticar
			if (!user.getToken().equals(jwt)) {
				LOGGER.warning("Token expired!");
				requestContext.abortWith(ResponseBuilder.createResponse(Response.Status.UNAUTHORIZED,
						"Token expired. Please authenticate again!"));
				return;
			}

			// verificar acceso de los roles "vacunador", "ciudadano", "administrador" o
			// "autoridad"
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

			// set header param id y tipoUsuario -> Para identificacion del usuario en los
			// servicios rest
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