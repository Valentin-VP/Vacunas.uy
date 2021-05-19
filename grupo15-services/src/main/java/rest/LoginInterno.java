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

import datatypes.DtUsuarioInterno;
import exceptions.UsuarioInexistente;
import interfaces.IUsuarioLocal;
import rest.filter.AuthenticationFilter;

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
			throw new ServletException("No se detecto Cookie al llegar a servlet LoginInterno");
		}
		String ci = request.getHeader(AuthenticationFilter.HEADER_PROPERTY_ID);
		String tipoUsuario = request.getHeader(AuthenticationFilter.HEADER_PROPERTY_TIPO);
		// Si lo anterior no funciona, probar lo siguiente
		//String tipoUsuario = TokenSecurity.getTipoUsuarioClaim(TokenSecurity.validateJwtToken(token));
		if(token != null && (tipoUsuario.equals("administrador") || tipoUsuario.equals("autoridad"))) {
			DtUsuarioInterno interno = null;
			try {
				interno = IUsuarioLocal.buscarUsuarioInterno(Integer.parseInt(ci));
				interno.setToken(token);
				IUsuarioLocal.ModificarUsuarioInterno(interno);
			} catch (NumberFormatException e) {
				e.printStackTrace();
			} catch (UsuarioInexistente e) {
				e.printStackTrace();
			}
			LOGGER.severe("Se agrega JWT en la Base de Datos al usuario interno " + interno.getIdUsuario());
		}
		else {
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
