package datatypes;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class DtTransportista implements Serializable{
	private static final long serialVersionUID = 1L;
	private int id;
	private String url;
	private String token;
	
	public DtTransportista() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	
	
	public DtTransportista(int id, String url, String token) {
		super();
		this.id = id;
		this.url = url;
		this.token = token;
	}



	public DtTransportista(int id, String url) {
		super();
		this.id = id;
		this.url = url;
	}


	public DtTransportista(int id) {
		super();
		this.id = id;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	
	
}
