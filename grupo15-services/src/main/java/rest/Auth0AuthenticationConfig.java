package rest;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

@ApplicationScoped
public class Auth0AuthenticationConfig {

	private String authorizeEndpoint;
    private String clientId;
    private String clientSecret;
    private String openid;
	private String response_type;
	private String redirect_uri;
	private String tokenEndpoint;
	private String userInfoEndpoint;

    @PostConstruct
    public void init() {
        // Se obtienen los valores del archivo web.xml
        try {
            Context env = (Context)new InitialContext().lookup("java:comp/env");
            // Endpoint de autenticación
            this.authorizeEndpoint = (String) env.lookup("auth0.authorizeEndpoint");
            // clientId de testing
            this.clientId = (String) env.lookup("auth0.clientId");
            // clientSecret de testing
            this.clientSecret = (String) env.lookup("auth0.clientSecret");
            // scope, al menos requiere `openid`
            this.openid = (String) env.lookup("auth0.scope");
            this.response_type = (String) env.lookup("auth0.response_type");
            this.redirect_uri = (String) env.lookup("auth0.redirect_uri");
            this.tokenEndpoint = (String) env.lookup("auth0.tokenEndpoint");
            this.userInfoEndpoint = (String) env.lookup("auth0.userInfoEndpoint");
        } catch (NamingException e) {
            throw new IllegalArgumentException("Unable to lookup auth0 configuration properties from web.xml", e);
        }

        if (this.authorizeEndpoint == null || this.clientId == null || this.clientSecret == null || this.openid == null || this.response_type == null || this.redirect_uri == null || this.tokenEndpoint == null) {
            throw new IllegalArgumentException("domain, clientId, clientSecret, response_type, redirect_uri and scope must be set in web.xml");
        }
//        if (this.domain == null || this.clientId == null || this.clientSecret == null || this.scope == null) {
//            throw new IllegalArgumentException("domain, clientId, clientSecret, and scope must be set in web.xml");
//        }
    }

    public String getAuthorizeEndpoint() {
         return authorizeEndpoint;
    }

    public String getClientId() {
         return clientId;
    }

    public String getClientSecret() {
         return clientSecret;
    }

    public String getOpenid() {
        return openid;
    }

	public String getResponse_type() {
		return response_type;
	}

	public String getRedirect_uri() {
		return redirect_uri;
	}

	public String getTokenEndpoint() {
		return tokenEndpoint;
	}

	public String getUserInfoEndpoint() {
		return userInfoEndpoint;
	}

}
