package rest;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Base64;
import java.util.logging.Logger;

import javax.inject.Inject;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
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

//cambiar por /callback !!!
@WebServlet(urlPatterns = { "/callback" })
public class CallbackServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
	private final Logger LOGGER = Logger.getLogger(getClass().getName());
	private final Auth0AuthenticationConfig config;
	private final AuthenticationController authenticationController;

	@Inject
	CallbackServlet(Auth0AuthenticationConfig config, AuthenticationController authenticationController) {
		this.config = config;
		this.authenticationController = authenticationController;
	}

	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		String referer = (String) request.getSession().getAttribute("Referer");
		String redirectTo = referer != null ? referer : "/";
		
		LOGGER.severe("Llega code: " + request.getParameter("code"));
		String code = request.getParameter("code");
		Client client = ClientBuilder.newClient();
		WebTarget target = client.target(config.getTokenEndpoint());
		
		//Creo Body Params para el POST al Token Endpoint
		Form form = new Form();
		form.param("grant_type", "authorization_code");
		form.param("code", code);
		form.param("redirect_uri", config.getRedirect_uri());

		//Creo Basic Auth Header
		String plainCredentials = config.getClientId() + ":" + config.getClientSecret();
		String base64Credentials = new String(Base64.getEncoder().encode(plainCredentials.getBytes()));
		String authorizationHeader = "Basic " + base64Credentials;
		LOGGER.severe("authorizationHeader : " + authorizationHeader);
		
		try {
			JsonObject tokenResponse = target.request(MediaType.APPLICATION_JSON_TYPE)
					.header(HttpHeaders.AUTHORIZATION, authorizationHeader)
					.post(Entity.entity(form, MediaType.APPLICATION_FORM_URLENCODED_TYPE), JsonObject.class);
			request.getSession().setAttribute("tokenResponse", tokenResponse);
			LOGGER.severe("Tokens obtenidos : " + tokenResponse);
			String accessToken = tokenResponse.getString("access_token");
			LOGGER.severe("Token de acceso : " + accessToken);
			requestUserId(accessToken);
			
		} catch (Exception ex) {
			System.out.println(ex.getMessage());
			request.setAttribute("error", ex.getMessage());
		}

		RequestDispatcher requestDispatcher = request.getRequestDispatcher("/");
		requestDispatcher.forward(request, response);

		//response.sendRedirect(redirectTo);
	}
	
	public String requestUserId(String accessToken) {
		// https://reqbin.com/req/java/5k564bhv/get-request-with-bearer-token-authorization-header
		String authorizationHeader = "Bearer " + accessToken;
		LOGGER.severe("authorizationHeader de tipo Bearer : " + authorizationHeader);
		try {
			URL url = new URL("https://auth-testing.iduruguay.gub.uy/oidc/v1/userinfo");
			HttpURLConnection http = (HttpURLConnection)url.openConnection();
			http.setRequestProperty("Accept", "application/json");
			http.setRequestProperty("Authorization", authorizationHeader);
			http.setDoInput(true);
			http.setRequestMethod("GET");
			http.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
			StringBuilder sb = new StringBuilder();
			int HttpResult = http.getResponseCode();
            if (HttpResult == HttpURLConnection.HTTP_OK) {
                BufferedReader br = new BufferedReader(
                        new InputStreamReader(http.getInputStream(), "utf-8"));
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
            LOGGER.severe("CI Usuario: " + object.getString("numero_documento"));
		} catch (Exception ex) {
			System.out.println(ex.getMessage());
		}
		return null;
	}
}
