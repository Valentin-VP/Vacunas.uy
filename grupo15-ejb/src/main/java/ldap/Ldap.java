package ldap;


import java.util.Properties;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.BasicAttribute;
import javax.naming.directory.BasicAttributes;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;
import javax.naming.directory.ModificationItem;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;

import interfaces.ILdap;
import interfaces.ILdapLocal;

@Stateless
@LocalBean
public class Ldap implements ILdap, ILdapLocal{
	
	DirContext connection;
	
	 public Ldap() {
	        // TODO Auto-generated constructor stub
	    }
	
	
	public void newConnection() {
		
		Properties env = new Properties();
		env.put(DirContext.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
		env.put(DirContext.PROVIDER_URL, "ldap://localhost:10389");
		env.put(DirContext.SECURITY_AUTHENTICATION, "simple");
		env.put(DirContext.SECURITY_PRINCIPAL, "uid=admin,ou=system");
		env.put(DirContext.SECURITY_CREDENTIALS, "secret");
		
		try {
		connection = new InitialDirContext(env);
			System.out.println("Hello World"+ connection);
		} catch (NamingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
	}
	
	public static void main(String[] args) throws NamingException {

		Ldap ldap = new Ldap();
		ldap.newConnection();
		//ldap.deleteUser();
	//	ldap.getAllUsers();
		//ldap.searchUser();
		//System.out.println(authUser("12345678","12"));
		//ldap.updateUserPass("12345678", "12");
		
		//ldap.addUser("Rodriguez", 22222222, "Jose", "Autoridad", "123");
		ldap.searchType("11111111");
	}
	
	public void getAllUsers() throws NamingException {
		String searchFilter = "(objectClass=inetOrgPerson)";
		String [] reqAtt = {"userId","userPassword", "cn"};
		SearchControls controls= new SearchControls();
		controls.setSearchScope(SearchControls.SUBTREE_SCOPE);
		controls.setReturningAttributes(reqAtt);
		NamingEnumeration users = connection.search("ou=users,ou=system", searchFilter, controls);
		SearchResult result = null;
		while (users.hasMore()){
			result = (SearchResult)users.next();
			Attributes attr = result.getAttributes();
			System.out.println(attr.get("userId"));
			System.out.println(attr.get("userPassword"));
			System.out.println(attr.get("cn"));
		}
	}
		

public void addUser(String apellido, Integer ci, String nombre, String tipoUser, String password) {
	//Ldap ldap = new Ldap();
	this.newConnection();
	String cedula = ci.toString();
	Attributes attributes = new BasicAttributes();
	Attribute attribute = new BasicAttribute("objectClass");
	attribute.add("inetOrgPerson");
	attributes.put(attribute);
	attributes.put("userid",cedula);
	attributes.put("cn",nombre);
	attributes.put("sn",apellido);
	attributes.put("employeeType", tipoUser);
	attributes.put("userPassword", password);
	System.out.print(cedula);
	try {
		connection.createSubcontext("userid="+cedula+ ",ou=users,ou=system", attributes);
		System.out.println("Usuario ingresado correctamente");
	} catch (NamingException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	
}
	
	
//ADD USER HARDCOREADO
//public void addUser() {
//	Attributes attributes = new BasicAttributes();
//	Attribute attribute = new BasicAttribute("objectClass");
//	attribute.add("inetOrgPerson");
//	attributes.put(attribute);
//	attributes.put("userid","12345");
//	attributes.put("cn","asdf");
//	attributes.put("sn","asdfas");
//	attributes.put("employeeType", "tipoUser");
//	
//	try {
//		connection.createSubcontext("userid="+"12345"+ ",ou=users,ou=system", attributes);
//	} catch (NamingException e) {
//		// TODO Auto-generated catch block
//		e.printStackTrace();
//	}
	
//	}

public void deleteUser(Integer ci) {
	try {
		connection.destroySubcontext("userid="+ci+ ",ou=users,ou=system");
	} catch (NamingException e) {
		System.out.println("Usuario eliminado correctamente");
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
}

//
////DELETE USER HARDCOREADO
//public void deleteUser() {
//	try {
//		connection.destroySubcontext("userid=12345,ou=users,ou=system");
//	} catch (NamingException e) {
//		System.out.println("Usuario eliminado correctamente");
//		// TODO Auto-generated catch block
//		e.printStackTrace();
//	}
//}

public void searchUser(Integer ci) throws NamingException {
	String searchFilter = "(userId="+ci+")";
	String [] reqAtt = {"userId","userPassword", "cn"};
	SearchControls controls= new SearchControls();
	controls.setSearchScope(SearchControls.SUBTREE_SCOPE);
	controls.setReturningAttributes(reqAtt);
	NamingEnumeration users = connection.search("ou=users,ou=system", searchFilter, controls);
	SearchResult result = null;
	while (users.hasMore()){
		result = (SearchResult)users.next();
		Attributes attr = result.getAttributes();
		System.out.println(attr.get("userId"));
		System.out.println(attr.get("userPassword"));
		System.out.println(attr.get("cn"));
	}
}


public String searchType(String ci) throws NamingException {
    String searchFilter = "(userId="+ci+")";
    String [] reqAtt = {"employeeType"};
    SearchControls controls= new SearchControls();
    controls.setSearchScope(SearchControls.SUBTREE_SCOPE);
    controls.setReturningAttributes(reqAtt);
    NamingEnumeration users = connection.search("ou=users,ou=system", searchFilter, controls);
    SearchResult result = null;
  
        result = (SearchResult)users.next();
        Attributes attr = result.getAttributes();
    //    System.out.println(attr.get("userId"));
    //    System.out.println(attr.get("userPassword"));
      //  System.out.println(attr.get("employeeType"));
        
    String tipoEmpleado = (attr.get("employeeType").toString());
	return tipoEmpleado;
}


//SEARCH USER HARDCOREADO
//public void searchUser() throws NamingException {
//	String searchFilter = "(userId=12345678)";
//	String [] reqAtt = {"userId","userPassword", "cn"};
//	SearchControls controls= new SearchControls();
//	controls.setSearchScope(SearchControls.SUBTREE_SCOPE);
//	controls.setReturningAttributes(reqAtt);
//	NamingEnumeration users = connection.search("ou=users,ou=system", searchFilter, controls);
//	SearchResult result = null;
//	while (users.hasMore()){
//		result = (SearchResult)users.next();
//		Attributes attr = result.getAttributes();
//		System.out.println(attr.get("userId"));
//		System.out.println(attr.get("userPassword"));
//		System.out.println(attr.get("cn"));
//	}
//}

public boolean authUser(String userId, String password) {
	try {
	Properties env = new Properties();
	env.put(DirContext.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
	env.put(DirContext.PROVIDER_URL, "ldap://localhost:10389");
	env.put(DirContext.SECURITY_AUTHENTICATION, "simple");
	env.put(DirContext.SECURITY_PRINCIPAL, "userid="+userId+",ou=users,ou=system");
	env.put(DirContext.SECURITY_CREDENTIALS, password);
	
		DirContext con = new InitialDirContext(env);
		con.close();
		return true;
	} catch (Exception e) {
		// TODO Auto-generated catch block
	System.out.println(e.getMessage());
		return false;
	
	}
	


//
//env.put(Context.SECURITY_PRINCIPAL, "cn="+username+",ou=users,ou=system");  //check the DN correctly
//env.put(Context.SECURITY_CREDENTIALS, password);
//DirContext con = new InitialDirContext(env);
//System.out.println("success");
//con.close();
//return true;
//}catch (Exception e) {
//// TODO: handle exception
//System.out.println(e.getMessage());
//System.out.println("failed: "+e.getMessage());
//return false;
//}
}
public void updateUserPass(String userId, String password) {
	try {
		String ruta=",ou=users,ou=system";
		ModificationItem[] mods= new ModificationItem[1];
		mods[0] = new ModificationItem(DirContext.REPLACE_ATTRIBUTE, new BasicAttribute("userPassword", password));// if you want, then you can delete the old password and after that you can replace with new password 
		connection.modifyAttributes("userId="+userId +ruta, mods);//try to form DN dynamically
		System.out.println("Se modifico el password");
	}catch (Exception e) {
		System.out.println("failed: "+e.getMessage());
	}
}

	}


