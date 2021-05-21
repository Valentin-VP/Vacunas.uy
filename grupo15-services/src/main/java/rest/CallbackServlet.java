package rest;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Base64;
import java.util.logging.Logger;

import javax.ejb.EJB;
import javax.inject.Inject;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Form;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;

import com.auth0.AuthenticationController;

import datatypes.DtCiudadano;
import datatypes.DtVacunador;
import exceptions.UsuarioInexistente;
import interfaces.IUsuarioLocal;
import rest.filter.TokenSecurity;

//cambiar por /callback !!!
@WebServlet(urlPatterns = { "/callback" })
public class CallbackServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
	private final Logger LOGGER = Logger.getLogger(getClass().getName());
	private final Auth0AuthenticationConfig config;
	private final AuthenticationController authenticationController;

	@EJB(lookup = "java:global/grupo15/grupo15-ejb/ControladorUsuario!interfaces.IUsuarioLocal")
	private IUsuarioLocal IUsuarioLocal;

	@Inject
	CallbackServlet(Auth0AuthenticationConfig config, AuthenticationController authenticationController) {
		this.config = config;
		this.authenticationController = authenticationController;
	}

	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		// String referer = (String) request.getSession().getAttribute("Referer");
		// String redirectTo = referer != null ? referer : "/";

		String code = request.getParameter("code");
		String tipoUsuario = request.getParameter("state");
		String urlRedirect;

		LOGGER.severe("Llega code: " + code + " y tipoUsuario: " + tipoUsuario);

		Client client = ClientBuilder.newClient();
		WebTarget target = client.target(config.getTokenEndpoint());

		// Creo Body Params para el POST al Token Endpoint
		Form form = new Form();
		form.param("grant_type", "authorization_code");
		form.param("code", code);
		form.param("redirect_uri", config.getRedirect_uri());

		// Creo Basic Auth Header
		String plainCredentials = config.getClientId() + ":" + config.getClientSecret();
		String base64Credentials = new String(Base64.getEncoder().encode(plainCredentials.getBytes()));
		String authorizationHeader = "Basic " + base64Credentials;
		LOGGER.severe("authorizationHeader : " + authorizationHeader);
		String ci;
		String token = null;
		try {
			JsonObject tokenResponse = target.request(MediaType.APPLICATION_JSON_TYPE)
					.header(HttpHeaders.AUTHORIZATION, authorizationHeader)
					.post(Entity.entity(form, MediaType.APPLICATION_FORM_URLENCODED_TYPE), JsonObject.class);
			request.getSession().setAttribute("tokenResponse", tokenResponse);
			LOGGER.severe("Tokens obtenidos : " + tokenResponse);
			String accessToken = tokenResponse.getString("access_token");
			LOGGER.severe("Token de acceso : " + accessToken);
			ci = requestUserId(accessToken);
			if (ci != null) {
				if (tipoUsuario.equals("vacunador")) {
					try {
						// Busco si existe este usuario con el rol actual
						DtVacunador vacunador = IUsuarioLocal.buscarVacunador(Integer.parseInt(ci));
						// Genero token de sesion con ID y tipo de usuario
						token = TokenSecurity.generateJwtToken(ci, tipoUsuario);
						LOGGER.severe("JWT generado : " + token);
						// Persisto el token en la DB asociado al usuario
						vacunador.setToken(token);
						IUsuarioLocal.ModificarVacunador(vacunador);
						LOGGER.severe("Se agrega JWT al vacunador " + vacunador.getIdUsuario());
					} catch (UsuarioInexistente error){
						// No existe el usuario con rol Vacunador, se debe informar que debe solicitar el rol a un administrador --> Definir pagina destino
						LOGGER.severe("El usuario no existe o no tiene el rol vacunador");
						urlRedirect = "/grupo15-web/html/error.html";
						response.sendRedirect(urlRedirect);
					}
				} else {
					if (tipoUsuario.equals("ciudadano")) {
						try {
							// Busco si existe este usuario con el rol actual
							DtCiudadano ciudadano = IUsuarioLocal.buscarCiudadano(Integer.parseInt(ci));
							// Genero token de sesion con ID y tipo de usuario
							token = TokenSecurity.generateJwtToken(ci, tipoUsuario);
							LOGGER.severe("JWT generado : " + token);
							// Persisto el token en la DB asociado al usuario
							ciudadano.setToken(token);
							IUsuarioLocal.ModificarCiudadano(ciudadano);
							LOGGER.severe("Se agrega JWT al ciudadano " + ciudadano.getIdUsuario());
						}catch(UsuarioInexistente error) {
							// No existe el usuario, se debe pedir Registro, y luego redirigir desde ahi al login nuevamente
							LOGGER.severe("El usuario no existe. Debe darse de alta como ciudadano.");
							urlRedirect = "/grupo15-web/html/registro.html";
							response.sendRedirect(urlRedirect);
						}
						
					}
				}
			}
			if (token!= null) {
				// Se guarda token en Cookie asociada al path root ("/") para todo el dominio;
				// sino sólo quedaria en grupo15-services
				Cookie userCookie = new Cookie("x-access-token", token);
				userCookie.setPath("/");
				response.addCookie(userCookie);
				if (tipoUsuario.equals("ciudadano")) {
					urlRedirect = "/grupo15-web/html/MenuCiudadano.html";
				} else {
					urlRedirect = "/grupo15-web/html/menuVacunador.html";
				}
				LOGGER.severe("Redirecting to: " + urlRedirect);
				response.sendRedirect(urlRedirect);
			}
		} catch (Exception ex) {
			System.out.println(ex.getMessage());
			request.setAttribute("error", ex.getMessage());
		}

	}

	public String requestUserId(String accessToken) {
		// Ultimo paso con Gub Uy. Se envia el access token para solicitar la CI del
		// usuario actua al UserInfo Endpoint.
		// https://reqbin.com/req/java/5k564bhv/get-request-with-bearer-token-authorization-header
		String authorizationHeader = "Bearer " + accessToken;
		LOGGER.severe("authorizationHeader de tipo Bearer : " + authorizationHeader);
		try {
			URL url = new URL("https://auth-testing.iduruguay.gub.uy/oidc/v1/userinfo");
			HttpURLConnection http = (HttpURLConnection) url.openConnection();
			http.setRequestProperty("Accept", "application/json");
			http.setRequestProperty("Authorization", authorizationHeader);
			http.setDoInput(true);
			http.setRequestMethod("GET");
			http.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
			StringBuilder sb = new StringBuilder();
			int HttpResult = http.getResponseCode();
			if (HttpResult == HttpURLConnection.HTTP_OK) {
				BufferedReader br = new BufferedReader(new InputStreamReader(http.getInputStream(), "utf-8"));
				String line = null;
				while ((line = br.readLine()) != null) {
					sb.append(line + "\n");
				}
				br.close();
			} else {
				System.out.println(http.getResponseMessage());
			}
			http.disconnect();
			JsonReader jsonReader = Json.createReader(new StringReader(sb.toString()));
			JsonObject object = jsonReader.readObject();
			jsonReader.close();
			String ci = object.getString("numero_documento");
			if (ci != null) {
				LOGGER.severe("CI Usuario: " + ci);
				return ci;
			}
		} catch (Exception ex) {
			System.out.println(ex.getMessage());
		}
		return null;
	}
}
