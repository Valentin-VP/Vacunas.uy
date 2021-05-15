package rest;

import java.io.IOException;
import java.util.logging.Logger;

import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jose4j.lang.JoseException;

import datatypes.DtCiudadano;
import datatypes.DtUsuarioInterno;
import datatypes.DtVacunador;
import exceptions.UsuarioExistente;
import exceptions.UsuarioInexistente;
import interfaces.IUsuarioLocal;
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
		// Verificar que en el REST tengan estos nombres los headers agregados
		String ci = request.getParameter("ci");
		String tipoUsuario = request.getParameter("tipousuario");
		String token = null;
		try {
			token = TokenSecurity.generateJwtToken(ci, tipoUsuario);	
		} catch (JoseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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
			LOGGER.severe("Se agrega JWT al usuario interno " + interno.getIdUsuario());
			response.setHeader("token", token);
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
