package interfaces;


import javax.ejb.Local;
import javax.naming.NamingException;

@Local
public interface ILdapLocal {

	public void addUser(String apellido, Integer ci, String nombre, String tipoUser, String password);
	public void newConnection();
	public void getAllUsers() throws NamingException;
	public  boolean authUser(String userId, String password);
	public  boolean searchUserBool(Integer ci) throws NamingException;
	public String searchType(String ci) throws NamingException;
	public void updateUserPass(String userId, String password);
	public void deleteUser(Integer ci);
}

