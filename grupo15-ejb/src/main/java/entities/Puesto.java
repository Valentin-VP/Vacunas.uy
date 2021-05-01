package entities;




import javax.persistence.Entity;
import javax.persistence.Id;


@Entity
public class Puesto {
	@Id
	private String id;

	public Puesto() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Puesto(String id) {
		super();
		this.id = id;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	



}