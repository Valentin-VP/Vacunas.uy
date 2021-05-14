package rest;

import java.util.logging.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.security.enterprise.AuthenticationException;
import javax.security.enterprise.AuthenticationStatus;
import javax.security.enterprise.authentication.mechanism.http.AutoApplySession;
import javax.security.enterprise.authentication.mechanism.http.HttpAuthenticationMechanism;
import javax.security.enterprise.authentication.mechanism.http.HttpMessageContext;
import javax.security.enterprise.identitystore.CredentialValidationResult;
import javax.security.enterprise.identitystore.IdentityStoreHandler;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.auth0.AuthenticationController;
import com.auth0.IdentityVerificationException;
import com.auth0.Tokens;

//La etiqueta AutoApplySession permite crear una sesion para el usuario autenticado
@ApplicationScoped
@AutoApplySession
public class Auth0AuthenticationMechanism implements HttpAuthenticationMechanism {
	private final AuthenticationController authenticationController;
	private final IdentityStoreHandler identityStoreHandler;
	private final Logger LOGGER = Logger.getLogger(getClass().getName());

	public Auth0AuthenticationMechanism() {
		super();
		this.authenticationController = null;
		this.identityStoreHandler = null;
		// TODO Auto-generated constructor stub
	}

	@Inject
	Auth0AuthenticationMechanism(AuthenticationController authenticationController,
			IdentityStoreHandler identityStoreHandler) {
		this.authenticationController = authenticationController;
		this.identityStoreHandler = identityStoreHandler;
	}

	// Sobreescribe el metodo que intercepta cada request a la app, y en caso que
	// sea un request a /callback procede con lo siguiente
	@Override
	public AuthenticationStatus validateRequest(HttpServletRequest httpServletRequest,
			HttpServletResponse httpServletResponse, HttpMessageContext httpMessageContext)
			throws AuthenticationException {

		// Cuando sea /callback, obtiene el `code` del request y hace el intercambio con
		// el OP para obtener el Access Token y Refresh token
		// el JWT decodificado lo pasa al identityStoreHandler para validarlo. Retorna
		// un AuthenticationStatus.SUCCESS al contenedor si el token es valido.
		if (isCallbackRequest(httpServletRequest)) {
			try {
				// Obtencion de tokens a partir del `code` con el OP
				Tokens tokens = authenticationController.handle(httpServletRequest, httpServletResponse);
				Auth0JwtCredential auth0JwtCredential = new Auth0JwtCredential(tokens.getIdToken());
				CredentialValidationResult result = identityStoreHandler.validate(auth0JwtCredential);
				return httpMessageContext.notifyContainerAboutLogin(result);
			} catch (IdentityVerificationException e) {
				// Habria que solicitar el refresh en este caso
				return httpMessageContext.responseUnauthorized();
			}
		}
		return httpMessageContext.doNothing();
	}

	// Analiza si es la URI de callback, paso correspondiente a un usuario ya
	// autenticado en el OP y con el param `code` en la URI
	// CAMBIAR /callback !!!!!!
	private boolean isCallbackRequest(HttpServletRequest request) {
		LOGGER.severe("Chequeando callbackUrl: " + request.getRequestURI());
		return request.getRequestURI().equals("/callback") && request.getParameter("code") != null;
	}
}
