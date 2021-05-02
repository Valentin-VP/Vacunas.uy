package datatypes;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class DtTransportista implements Serializable{
	private static final long serialVersionUID = 1L;
	private int id;
	
	public DtTransportista() {
		super();
		// TODO Auto-generated constructor stub
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
	
	
}
