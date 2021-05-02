package datatypes;

import java.io.Serializable;

import entities.Vacunatorio;

public class DtPuesto implements Serializable {

private static final long serialVersionUID = 1L;
private String id;
private Vacunatorio vacunatorio;




public DtPuesto() {
	super();
	// TODO Auto-generated constructor stub
}




public DtPuesto(String id, Vacunatorio vacunatorio) {
	super();
	this.id = id;
	this.vacunatorio = vacunatorio;
}




public String getId() {
	return id;
}




public void setId(String id) {
	this.id = id;
}




public Vacunatorio getVacunatorio() {
	return vacunatorio;
}




public void setVacunatorio(Vacunatorio vacunatorio) {
	this.vacunatorio = vacunatorio;
}




}
