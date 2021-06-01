package rest;

import java.util.logging.Logger;

import javax.annotation.security.DeclareRoles;
import javax.annotation.security.RolesAllowed;
import javax.ejb.EJB;
import javax.ws.rs.Consumes;
import javax.ws.rs.CookieParam;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Cookie;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.jose4j.jwt.consumer.InvalidJwtException;

import datatypes.DtCertificadoVac;
import exceptions.CertificadoInexistente;
import exceptions.UsuarioExistente;
import interfaces.ICertificadoVacunacionDAOLocal;
import interfaces.IConstanciaVacunaDAOLocal;
import interfaces.IUsuarioLocal;
import rest.filter.ResponseBuilder;
import rest.filter.TokenSecurity;

@DeclareRoles({"vacunador", "ciudadano", "administrador", "autoridad"})
@Path("/vacunaciones")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class ConsultarCertificado {

	//private static final long serialVersionUID = 1L;
		private final Logger LOGGER = Logger.getLogger(getClass().getName());
		@EJB(lookup = "java:global/grupo15/grupo15-ejb/ControladorConstanciaVacuna!interfaces.IConstanciaVacunaDAOLocal")
		private IConstanciaVacunaDAOLocal IConstVac;
		
		@EJB(lookup = "java:global/grupo15/grupo15-ejb/ControladorCertificadoVacunacion!interfaces.ICertificadoVacunacionDAOLocal")
		private ICertificadoVacunacionDAOLocal ICertVac;
		
		@GET
		@Path("/certificado")
		@RolesAllowed({"ciudadano"})
		public Response getCertificado(@CookieParam("x-access-token") Cookie cookie) {
			String token = cookie.getValue();
			String ci = null;
			try {
				ci = TokenSecurity.getIdClaim(TokenSecurity.validateJwtToken(token));
			} catch (InvalidJwtException e) {
				e.printStackTrace();
			}
			DtCertificadoVac dtCV;
			try {
				dtCV = ICertVac.obtenerCertificadoVacunacion(Integer.parseInt(ci));
			} catch (NumberFormatException | CertificadoInexistente | UsuarioExistente error) {
				return ResponseBuilder.createResponse(Response.Status.BAD_REQUEST, error.getMessage());
			}
			return Response.ok(dtCV).build();
		}
		
		

}
