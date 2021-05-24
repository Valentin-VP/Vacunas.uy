package rest;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Base64;
import java.util.List;
import java.util.StringTokenizer;
import java.util.logging.Logger;

import javax.annotation.security.DeclareRoles;
import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import datatypes.DtCiudadanoRegistro;
import datatypes.DtDireccion;
import datatypes.DtLdap;
import datatypes.DtVacunadorRegistro;
import datatypes.Rol;
import datatypes.Sexo;
import exceptions.UsuarioExistente;
import interfaces.ILdapLocal;
import interfaces.IUsuarioLocal;
import rest.filter.ResponseBuilder;

@DeclareRoles({ "vacunador", "ciudadano", "administrador", "autoridad" })
@RequestScoped
@Path("/registro")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class RegistrarUsuarios {

	private final Logger LOGGER = Logger.getLogger(getClass().getName());

	@EJB
	ILdapLocal l;
	@EJB(lookup = "java:global/grupo15/grupo15-ejb/ControladorUsuario!interfaces.IUsuarioLocal")
	private IUsuarioLocal IUsuarioLocal;

	public RegistrarUsuarios() {
	}

	// Solo los administradores pueden dar de alta usuarios internos; debe tener
	// iniciada la sesión un admin para acceder
	@RolesAllowed("administrador")
	@POST
	@Path("/interno")
	public Response agregarUsuarioInterno(@Context HttpHeaders headers, DtLdap dt) {
		LOGGER.info("Metodo agregarUsuarioInterno: Acceso concedido");

		List<String> authHeaders = headers.getRequestHeader("Authorization");
		if (authHeaders == null) {
			throw new IllegalArgumentException("Request does not have Authorization header");
		}

		// Obtener value del header Auth
		String authHeaderValue = authHeaders.get(0);
		System.out.println(authHeaderValue);
		if (authHeaderValue == null) {
			throw new IllegalArgumentException("Request does not have authorization header value");
		}

		StringTokenizer tokenizer = new StringTokenizer(
				new String(Base64.getDecoder().decode(authHeaderValue.replaceFirst("Basic ", "").getBytes())), ":");
		String ci = tokenizer.nextToken();
		String password = tokenizer.nextToken();

		try {
			l.addUser(dt.getApellido(), Integer.parseInt(ci), dt.getNombre(), dt.getTipoUser(), password);
		} catch (NumberFormatException error) {
			LOGGER.severe("No se recibe un ID valido en agregar usuario de LDAP `addUser()`");
			return ResponseBuilder.createResponse(Response.Status.BAD_REQUEST);
			// --> No hay excepciones particulares definidas aún en el controlador.
		} catch (Exception e) {
			LOGGER.severe("Ha ocurrido un error con agregar usuario de LDAP `addUser()`");
			return ResponseBuilder.createResponse(Response.Status.BAD_REQUEST);
		}

		// Crear usuario en Postgres
		try {
			// Parseo de los 3 atributos de direccion para tener un DtDireccion: dirrecion,
			// barrio y departamento
			// --> Cuando se reciban los 3 atributos:
			// DtDireccion direccion = new DtDireccion(dt.getDireccion(), dt.getBarrio(),
			// dt.getDepartamento());
			// Mientras:
			DtDireccion direccion = new DtDireccion(dt.getDireccion(), null, null);
			// Parseo de FechaNac para tener LocalDate
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
			LocalDate fechaNac = LocalDate.parse(dt.getFecha(), formatter);
			IUsuarioLocal.agregarUsuarioInterno(dt.getCi(), dt.getNombre(), dt.getApellido(), fechaNac, dt.getEmail(),
					direccion, Sexo.valueOf(dt.getSexo()), Rol.valueOf(dt.getTipoUser()));
		} catch (UsuarioExistente error) {
			LOGGER.severe("Ya existe un usuario con ese ID");

		} catch (Exception error) {

		}
		return ResponseBuilder.createResponse(Response.Status.CREATED);

	}

	// Cualquier usuario puede darse de alta como Ciudadano
	@PermitAll
	@POST
	@Path("/ciudadano")
	public Response agregarUsuarioCiudadano(DtCiudadanoRegistro dtCiudadanoRegistro) {
		// --> Agregar al if: dtCiudadanoRegistro.getBarrio() == null ||
		// dtCiudadanoRegistro.getDepartamento() == null
		if (dtCiudadanoRegistro.getId() == null || dtCiudadanoRegistro.getNombre() == null
				|| dtCiudadanoRegistro.getApellido() == null || dtCiudadanoRegistro.getFechaNac() == null
				|| dtCiudadanoRegistro.getSexo() == null || dtCiudadanoRegistro.getEmail() == null
				|| dtCiudadanoRegistro.getDireccion() == null) {
			LOGGER.severe("Se ha recibido un atributo null en el body");
			return ResponseBuilder.createResponse(Response.Status.BAD_REQUEST);
		}

		// Parseo de FechaNac para tener LocalDate
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		LocalDate fechaNac = LocalDate.parse(dtCiudadanoRegistro.getFechaNac(), formatter);

		// Parseo de los 3 atributos de direccion para tener un DtDireccion: dirrecion,
		// barrio y departamento
		// --> Cuando se reciban los 3 atributos:
		// DtDireccion direccion = new DtDireccion(dtCiudadanoRegistro.getDireccion(),
		// dtCiudadanoRegistro.getBarrio(), dtCiudadanoRegistro.getDepartamento());
		// Mientras:
		DtDireccion direccion = new DtDireccion(dtCiudadanoRegistro.getDireccion(), null, null);

		// El tipoSector se deberá consultar a una fuente externa de datos, a partir de
		// la CI, para poder completar el alta del ciudadano
		String tipoSector = "default";

		try {
			// int IdUsuario, String nombre, String apellido, LocalDate fechaNac, String
			// email,
			// DtDireccion direccion, Sexo sexo, String TipoSector, Boolean autenticado
			IUsuarioLocal.agregarUsuarioCiudadano(Integer.parseInt(dtCiudadanoRegistro.getId()),
					dtCiudadanoRegistro.getNombre(), dtCiudadanoRegistro.getApellido(), fechaNac,
					dtCiudadanoRegistro.getEmail(), direccion, Sexo.valueOf(dtCiudadanoRegistro.getSexo()), tipoSector,
					false);
		} catch (UsuarioExistente error) {
			LOGGER.severe(error.getMessage());
			return ResponseBuilder.createResponse(Response.Status.BAD_REQUEST);
		}

		return ResponseBuilder.createResponse(Response.Status.CREATED);

	}

	// Solo los admin pueden dar de alta un vacunador
	@RolesAllowed("administrador")
	@POST
	@Path("/vacunador")
	public Response agregarUsuarioVacunador(DtVacunadorRegistro dtVacunadorRegistro) {
		// --> Agregar al if: dtCiudadanoRegistro.getBarrio() == null ||
		// dtCiudadanoRegistro.getDepartamento() == null
		if (dtVacunadorRegistro.getId() == null || dtVacunadorRegistro.getNombre() == null
				|| dtVacunadorRegistro.getApellido() == null || dtVacunadorRegistro.getFechaNac() == null
				|| dtVacunadorRegistro.getSexo() == null || dtVacunadorRegistro.getEmail() == null
				|| dtVacunadorRegistro.getDireccion() == null) {
			LOGGER.severe("Se ha recibido un atributo null en el body");
			return ResponseBuilder.createResponse(Response.Status.BAD_REQUEST);
		}

		// Parseo de FechaNac para tener LocalDate
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		LocalDate fechaNac = LocalDate.parse(dtVacunadorRegistro.getFechaNac(), formatter);

		// Parseo de los 3 atributos de direccion para tener un DtDireccion: dirrecion,
		// barrio y departamento
		// --> Cuando se reciban los 3 atributos:
		// DtDireccion direccion = new DtDireccion(dtCiudadanoRegistro.getDireccion(),
		// dtCiudadanoRegistro.getBarrio(), dtCiudadanoRegistro.getDepartamento());
		// Mientras:
		DtDireccion direccion = new DtDireccion(dtVacunadorRegistro.getDireccion(), null, null);

		// El tipoSector se deberá consultar a una fuente externa de datos, a partir de
		// la CI, para poder completar el alta del ciudadano
		String tipoSector = "default";

		try {
			// int IdUsuario, String nombre, String apellido, LocalDate fechaNac, String
			// email,
			// DtDireccion direccion, Sexo sexo, String TipoSector, Boolean autenticado
			IUsuarioLocal.agregarUsuarioCiudadano(Integer.parseInt(dtVacunadorRegistro.getId()),
					dtVacunadorRegistro.getNombre(), dtVacunadorRegistro.getApellido(), fechaNac,
					dtVacunadorRegistro.getEmail(), direccion, Sexo.valueOf(dtVacunadorRegistro.getSexo()), tipoSector,
					false);
		} catch (UsuarioExistente error) {
			LOGGER.severe(error.getMessage());
			return ResponseBuilder.createResponse(Response.Status.BAD_REQUEST);
		}

		return ResponseBuilder.createResponse(Response.Status.CREATED);

	}

}
