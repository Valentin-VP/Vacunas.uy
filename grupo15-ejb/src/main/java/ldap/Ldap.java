package ldap;


import java.util.Properties;

import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.BasicAttribute;
import javax.naming.directory.BasicAttributes;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;

public class Ldap {
	
	DirContext connection;
	
	
	
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
		ldap.getAllUsers();
		//ldap.searchUser();
		//System.out.println(authUser("12345678","1234"));
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
		

public void addUser(String apellido, Integer ci, String nombre, String tipoUser) {
	Attributes attributes = new BasicAttributes();
	Attribute attribute = new BasicAttribute("objectClass");
	attribute.add("inetOrgPerson");
	attributes.put(attribute);
	attributes.put("userid",ci);
	attributes.put("cn",nombre);
	attributes.put("sn",apellido);
	attributes.put("employeeType", tipoUser);
	
	try {
		connection.createSubcontext("userid="+ci+ ",ou=users,ou=system", attributes);
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

public static boolean authUser(String userId, String password) {
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

	}


