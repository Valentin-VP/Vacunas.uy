package datatypes;

import java.io.Serializable;

import entities.Vacunatorio;

public class DtPuesto implements Serializable {

private static final long serialVersionUID = 1L;
private String id;
private DtVacunatorio vacunatorio;




public DtPuesto() {
	super();
	// TODO Auto-generated constructor stub
}




public DtPuesto(String id, DtVacunatorio vacunatorio) {
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




public DtVacunatorio getVacunatorio() {
	return vacunatorio;
}




public void setVacunatorio(DtVacunatorio vacunatorio) {
	this.vacunatorio = vacunatorio;
}




}
