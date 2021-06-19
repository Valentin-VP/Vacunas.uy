package entities;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;


@Entity
public class Transportista {
	
	@Id @Column(nullable=false)
	Integer id;
	
	String url;
	
	public Transportista() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Transportista(Integer id) {
		super();
		this.id = id;
	}

	
	
	public Transportista(Integer id, String url) {
		super();
		this.id = id;
		this.url = url;
	}

	public Integer getId() {
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
	
	
}
