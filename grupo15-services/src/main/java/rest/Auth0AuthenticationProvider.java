package rest;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;

import com.auth0.AuthenticationController;

@ApplicationScoped
public class Auth0AuthenticationProvider {

    @Produces
    public AuthenticationController authenticationController(Auth0AuthenticationConfig config) {
        return AuthenticationController.newBuilder(config.getDomain(), config.getClientId(), config.getClientSecret())
                .build();
    }
}
