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

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.jose4j.jwt.consumer.InvalidJwtException;

import datatypes.DtCertificadoVac;
import datatypes.DtConstancia;
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
		@EJB
		private IConstanciaVacunaDAOLocal IConstVac;
		
		@EJB
		private ICertificadoVacunacionDAOLocal ICertVac;
		
		@GET
		@Path("/certificado")
		@RolesAllowed("ciudadano")
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
		
		@GET
		@Path("/certificadovue")
		@RolesAllowed("ciudadano")
		public Response getCertificadovue(@CookieParam("x-access-token") Cookie cookie) {
			String token = cookie.getValue();
			String ci;
			DtCertificadoVac dtCV;
			try {
				ci = TokenSecurity.getIdClaim(TokenSecurity.validateJwtToken(token));
				System.out.println(ci);
				dtCV = ICertVac.obtenerCertificadoVacunacion(Integer.parseInt(ci));
				System.out.println(dtCV);
				JSONArray certificado = new JSONArray();
				for(DtConstancia c: dtCV.getConstancias()) {
					JSONObject datos = new JSONObject();
					datos.put("ci", ci);
					datos.put("vacuna", c.getVacuna());
					datos.put("enfermedad", c.getEnfermedad());
					datos.put("pInmunidad", c.getPeriodoInmunidad());
					datos.put("totalDosis", c.getDosisRecibidas());
					datos.put("ultimaDosis", c.getFechaUltimaDosis().toString());
					certificado.put(datos);
				}

				return Response.ok(certificado.toString()).build();
			} catch (NumberFormatException | CertificadoInexistente | UsuarioExistente | InvalidJwtException | JSONException error) {
				return ResponseBuilder.createResponse(Response.Status.BAD_REQUEST, error.getMessage());
			}
		}
		

}
