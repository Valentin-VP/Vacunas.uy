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

import org.jose4j.json.internal.json_simple.JSONObject;
import org.jose4j.jwt.consumer.InvalidJwtException;

import datatypes.DtUsuarioInterno;
import exceptions.UsuarioInexistente;
import interfaces.IUsuarioLocal;
import rest.filter.AuthenticationFilter;
import rest.filter.TokenSecurity;

@WebServlet("logininterno")
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
			 
		   if (cookie.getName().equals("x-access-token")) {
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
		LOGGER.severe(ci);
		LOGGER.severe(tipoUsuario);
		if(token != null && (tipoUsuario.equals("administrador") || tipoUsuario.equals("autoridad"))) {
			DtUsuarioInterno interno = null;
			try {
				interno = IUsuarioLocal.buscarUsuarioInterno(Integer.parseInt(ci));
				interno.setToken(token);
				IUsuarioLocal.ModificarUsuarioInterno(interno);
				LOGGER.severe("Se agrega JWT en la Base de Datos al usuario interno " + interno.getIdUsuario());
				
				String host = request.getHeader("Origin");
		        LOGGER.severe("Origin en LoginInterno: " + host);
				String urlRedirect = host + "/grupo15-web/html/menuAdministrador.html";
				JSONObject address = new JSONObject();
				address.put("url", urlRedirect);
				LOGGER.severe("Redirecting to: " + address.toJSONString());
				//response.sendRedirect(urlRedirect);
				response.setContentType("application/json");
				response.getWriter().write(address.toString());
			} catch (NumberFormatException e) {
				LOGGER.severe("Error parseando ci del token");
			} catch (UsuarioInexistente e) {
				// Denegar acceso, debe registrarse primero. Eliminar Cookie e informar
				Cookie cookie = new Cookie("x-access-token", "");
		        cookie.setMaxAge(0);
		        response.addCookie(cookie);
		        response.setContentType("application/json");
		        String url = request.getHeader("Origin") + "/grupo15-web/html/error.html";
		        LOGGER.severe("Origin en LoginInterno: " + url);
		        JSONObject content = new JSONObject();
		        content.put("url", url);
		        response.getWriter().write(content.toJSONString());
		        LOGGER.severe("Usuario no existe en BD. Retirando Cookie...");
			}
		}
		else {
			// Caso en que el usuario tiene Cookie con token pero no inicio sesion como interno --> No puede seguir, arrojar a pagina de error o index?
			LOGGER.severe("No se ha recibido el token, o el tipo de usuario no es el correcto");
			Cookie cookie = new Cookie("x-access-token", "");
	        cookie.setMaxAge(0);
	        response.addCookie(cookie);
	        response.setContentType("application/json");
	        String host = request.getHeader("Origin") + "/grupo15-web/html/error.html";
	        LOGGER.severe("Origin en LoginInterno: " + host);
	        JSONObject content = new JSONObject();
	        content.put("url", host);
	        response.getWriter().write(content.toJSONString());
		}
		
	}


	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
