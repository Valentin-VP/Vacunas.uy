package rest;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;

import com.auth0.AuthenticationController;

@ApplicationScoped
public class Auth0AuthenticationProvider {

	//https://javadoc.io/doc/com.auth0/mvc-auth-commons/1.0.0/com/auth0/AuthenticationController.html
    @Produces
    public AuthenticationController authenticationController(Auth0AuthenticationConfig config) {
        return AuthenticationController.newBuilder(config.getAuthorizeEndpoint(), config.getClientId(), config.getClientSecret())
                .build();
    }
}
