package rest;

import java.io.IOException;
import java.util.logging.Logger;

import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jose4j.jwt.consumer.InvalidJwtException;

import datatypes.DtUsuarioInterno;
import exceptions.UsuarioInexistente;
import interfaces.IUsuarioLocal;
import rest.filter.AuthenticationFilter;
import rest.filter.TokenSecurity;

@WebServlet("/logininterno")
public class LoginInterno extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private final Logger LOGGER = Logger.getLogger(getClass().getName());
       
	@EJB(lookup = "java:global/grupo15/grupo15-ejb/ControladorUsuario!interfaces.IUsuarioLocal")
	private IUsuarioLocal IUsuarioLocal;
	
        public LoginInterno() {
        super();
    }

	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		LOGGER.info("Accediendo a LoginInterno WebServlet");
		String token = null;
		Cookie[] cookies = request.getCookies();
		if (cookies != null) {
		 for (Cookie cookie : cookies) {
		   if (cookie.getName().equals("x-acces-token")) {
			   token = cookie.getValue();
			   LOGGER.info("Se obtiene Cookie en LoginInterno WebServlet: " + token);
		    }
		  }
		}
		else {
			// Si llego sin Cookie, no esta autorizado (o accedio ilicito al recurso, o no existe en LDAP) --> No puede seguir, arrojar a pagina de error o index?
			throw new ServletException("No se detecto Cookie al llegar a servlet LoginInterno");
		}

		String ci = null ;
		String tipoUsuario = null;
		try {
			ci = TokenSecurity.getIdClaim(TokenSecurity.validateJwtToken(token));
			tipoUsuario = TokenSecurity.getTipoUsuarioClaim(TokenSecurity.validateJwtToken(token));
		} catch (InvalidJwtException e1) {
			LOGGER.severe("Error decodificando token");
		}
		if(token != null && (tipoUsuario.equals("administrador") || tipoUsuario.equals("autoridad"))) {
			DtUsuarioInterno interno = null;
			try {
				interno = IUsuarioLocal.buscarUsuarioInterno(Integer.parseInt(ci));
				interno.setToken(token);
				IUsuarioLocal.ModificarUsuarioInterno(interno);
			} catch (NumberFormatException e) {
				LOGGER.severe("Error parseando ci del token");
			} catch (UsuarioInexistente e) {
				// Denegar acceso, debe registrarse primero. Eliminar Cookie e informar
				Cookie cookie = new Cookie("x-acces-token", "");
		        cookie.setMaxAge(0);
		        response.addCookie(cookie);
		        LOGGER.severe("Usuario no existe en BD. Retirando Cookie...");
			}
			LOGGER.severe("Se agrega JWT en la Base de Datos al usuario interno " + interno.getIdUsuario());
		}
		else {
			// Caso en que el usuario tiene Cookie con token pero no inicio sesion como interno --> No puede seguir, arrojar a pagina de error o index?
			LOGGER.severe("No se ha recibido el token, o el tipo de usuario no es el correcto");
		}
		String urlRedirect = "/grupo15-web/html/index.html";
		LOGGER.severe("Redirecting to: " + urlRedirect);
		response.sendRedirect(urlRedirect);
	}


	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
