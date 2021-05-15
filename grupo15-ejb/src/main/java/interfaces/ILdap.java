package interfaces;


import javax.ejb.Remote;
import javax.naming.NamingException;

@Remote
public interface ILdap {

	public void addUser(String apellido, Integer ci, String nombre, String tipoUser, String password);
	public void newConnection();
	public void getAllUsers() throws NamingException;
}

