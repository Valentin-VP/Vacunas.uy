package rest.filter;

import org.glassfish.jersey.server.ResourceConfig;

public class RestApplicationConfig extends ResourceConfig {
    
    public RestApplicationConfig() {
        packages( "rest.rest.filter" );
        register( AuthenticationFilter.class );
    }
}
