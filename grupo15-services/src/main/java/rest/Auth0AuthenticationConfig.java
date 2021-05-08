package rest;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

@ApplicationScoped
public class Auth0AuthenticationConfig {

	private String domain;
    private String clientId;
    private String clientSecret;
    private String openid;
	private String response_type;
	private String redirect_uri;

    @PostConstruct
    public void init() {
        // Get authentication config values from env-entries in web.xml
        try {
            Context env = (Context)new InitialContext().lookup("java:comp/env");

            this.domain = (String) env.lookup("auth0.domain");
            this.clientId = (String) env.lookup("auth0.clientId");
            this.clientSecret = (String) env.lookup("auth0.clientSecret");
            this.openid = (String) env.lookup("auth0.scope");
            this.response_type = (String) env.lookup("auth0.response_type");
            this.redirect_uri = (String) env.lookup("auth0.redirect_uri");
        } catch (NamingException ne) {
            throw new IllegalArgumentException("Unable to lookup auth0 configuration properties from web.xml", ne);
        }

        if (this.domain == null || this.clientId == null || this.clientSecret == null || this.openid == null || this.response_type == null || this.redirect_uri == null) {
            throw new IllegalArgumentException("domain, clientId, clientSecret, response_type, redirect_uri and scope must be set in web.xml");
        }
//        if (this.domain == null || this.clientId == null || this.clientSecret == null || this.scope == null) {
//            throw new IllegalArgumentException("domain, clientId, clientSecret, and scope must be set in web.xml");
//        }
    }

    public String getDomain() {
         return domain;
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

}
