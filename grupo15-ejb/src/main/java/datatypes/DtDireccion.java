package datatypes;

import java.io.Serializable;

public class DtDireccion implements Serializable {

private static final long serialVersionUID = 1L;
String direccion;
String barrio;
String departamento;




public DtDireccion() {
	super();
	// TODO Auto-generated constructor stub
}




public DtDireccion(String direccion, String barrio, String departamento) {
	super();
	this.direccion = direccion;
	this.barrio = barrio;
	this.departamento = departamento;
}




public String getDireccion() {
	return direccion;
}




public void setDireccion(String direccion) {
	this.direccion = direccion;
}




public String getBarrio() {
	return barrio;
}




public void setBarrio(String barrio) {
	this.barrio = barrio;
}




public String getDepartamento() {
	return departamento;
}




public void setDepartamento(String departamento) {
	this.departamento = departamento;
}

}
