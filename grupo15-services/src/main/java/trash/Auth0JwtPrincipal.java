/*
 * package trash;
 * 
 * import javax.security.enterprise.CallerPrincipal; import
 * java.util.logging.Logger; import com.auth0.jwt.interfaces.DecodedJWT;
 * 
 * // http://eir.me/javaee/security-api001.php // Principal type that can
 * represent the identity of the application caller. public class
 * Auth0JwtPrincipal extends CallerPrincipal { private final DecodedJWT idToken;
 * private final Logger LOGGER = Logger.getLogger(getClass().getName());
 * Auth0JwtPrincipal(DecodedJWT idToken) {
 * super(idToken.getClaim("numero_documento").asString()); this.idToken =
 * idToken; LOGGER.severe("Se guarda el TokenID: " + idToken); }
 * 
 * public DecodedJWT getIdToken() { return this.idToken; } }
 */