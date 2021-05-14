package rest;

import java.io.IOException;
import java.util.logging.Logger;
import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.auth0.AuthenticationController;

/**
 * Servlet implementation class LoginServlet
 */
@WebServlet(urlPatterns = "/login")
public class LoginServlet extends HttpServlet {
	// Se agrega serializacion
	private static final long serialVersionUID = 1L;
	private final Auth0AuthenticationConfig config;
    private final Logger LOGGER = Logger.getLogger(getClass().getName());
    @Inject
    LoginServlet(Auth0AuthenticationConfig config) {
        this.config = config;
    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // URL donde la app recibira el authorization code (e.g., http://localhost:3000/callback)
    	String authorizationUri = config.getAuthorizeEndpoint();
    	String clientId = config.getClientId();
    	String redirectUri = config.getRedirect_uri();
    	String scope = config.getOpenid();
    	String tipoUsuario = "vacunador";
    	//String tipoUsuario = request.getParameter("tipoUsuario");
        LOGGER.severe("Test log message with callbackUrl: " + redirectUri);
        // Create the authorization URL to redirect the user to, to begin the authentication flow.
		/*
		 * String authURL = authenticationController.buildAuthorizeUrl(request,
		 * response, callbackUrl) .withParameter("openid", config.getOpenid())
		 * //.withParameter("response_type", config.getResponse_type())
		 * //.withScope(config.getScope()) .build();
		 */
        String authURL = authorizationUri + "?response_type=code"
        		  + "&client_id=" + clientId
        		  + "&redirect_uri=" + redirectUri
        		  + "&scope=" + scope
        		  + "&state=" + tipoUsuario;

        response.sendRedirect(authURL);
    }
}

