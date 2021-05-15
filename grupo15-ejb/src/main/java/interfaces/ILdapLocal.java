package interfaces;


import javax.ejb.Local;
import javax.naming.NamingException;

@Local
public interface ILdapLocal {

	public void addUser(String apellido, Integer ci, String nombre, String tipoUser, String password);
	public void newConnection();
	public void getAllUsers() throws NamingException;
}

