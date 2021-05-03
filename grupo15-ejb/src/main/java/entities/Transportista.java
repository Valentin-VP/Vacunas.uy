package entities;

import java.util.ArrayList;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity
public class Transportista {
	
	@Id @Column(nullable=false)
	Integer id;

	public Transportista() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Transportista(Integer id) {
		super();
		this.id = id;
	}

	public Integer getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
}
