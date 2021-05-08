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
    private final AuthenticationController authenticationController;
    private final Logger LOGGER = Logger.getLogger(getClass().getName());
    @Inject
    LoginServlet(Auth0AuthenticationConfig config, AuthenticationController authenticationController) {
        this.config = config;
        this.authenticationController = authenticationController;
    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // URL where the application will receive the authorization code (e.g., http://localhost:3000/callback)
//        String callbackUrl = String.format(
//                "%s://%s:%s/grupo15-services/callback",
//                request.getScheme(),
//                request.getServerName(),
//                request.getServerPort()
//        );
    	String callbackUrl = "https://openidconnect.net/callback";
        LOGGER.severe("Test log message with callbackUrl: " + callbackUrl);
        // Create the authorization URL to redirect the user to, to begin the authentication flow.
        String authURL = authenticationController.buildAuthorizeUrl(request, response, callbackUrl)
        		.withParameter("openid", config.getOpenid())
        		//.withParameter("response_type", config.getResponse_type())
                //.withScope(config.getScope())
                .build();

        response.sendRedirect(authURL);
    }
}

