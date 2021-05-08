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

@ApplicationScoped
@AutoApplySession
public class Auth0AuthenticationMechanism implements HttpAuthenticationMechanism {
    private final AuthenticationController authenticationController;
    private final IdentityStoreHandler identityStoreHandler;
    private final Logger LOGGER = Logger.getLogger(getClass().getName());
    @Inject
    Auth0AuthenticationMechanism(AuthenticationController authenticationController, IdentityStoreHandler identityStoreHandler) {
        this.authenticationController = authenticationController;
        this.identityStoreHandler = identityStoreHandler;
    }

    @Override
    public AuthenticationStatus validateRequest(HttpServletRequest httpServletRequest,
                                                HttpServletResponse httpServletResponse,
                                                HttpMessageContext httpMessageContext) throws AuthenticationException {

        // Exchange the code for the ID token, and notify container of result.
        if (isCallbackRequest(httpServletRequest)) {
            try {
                Tokens tokens = authenticationController.handle(httpServletRequest, httpServletResponse);
                Auth0JwtCredential auth0JwtCredential = new Auth0JwtCredential(tokens.getIdToken());
                CredentialValidationResult result = identityStoreHandler.validate(auth0JwtCredential);
                LOGGER.severe("Status del TokenID: " + result.getStatus());
                return httpMessageContext.notifyContainerAboutLogin(result);
            } catch (IdentityVerificationException e) {
                return httpMessageContext.responseUnauthorized();
            }
        }
        return httpMessageContext.doNothing();
    }

    private boolean isCallbackRequest(HttpServletRequest request) {
        return request.getRequestURI().equals("/callback") && request.getParameter("code") != null;
    }
}
