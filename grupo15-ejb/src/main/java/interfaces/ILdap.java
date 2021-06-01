package interfaces;


import javax.ejb.Remote;
import javax.naming.NamingException;

@Remote
public interface ILdap {

	public void addUser(String apellido, Integer ci, String nombre, String tipoUser, String password);
	public void newConnection();
	public void getAllUsers() throws NamingException;
	public  boolean authUser(String userId, String password);
	public  boolean searchUserBool(Integer ci) throws NamingException;
	public String searchType(String ci) throws NamingException;
	public void updateUserPass(String userId, String password);
}

