package datatypes;

import java.io.Serializable;

public class DtHora implements Serializable {

private static final long serialVersionUID = 1L;
int segundo;
int minuto;
int hora;




public DtHora() {
	super();
	// TODO Auto-generated constructor stub
}




public DtHora(int segundo, int minuto, int hora) {
	super();
	this.segundo = segundo;
	this.minuto = minuto;
	this.hora = hora;
}




public int getSegundo() {
	return segundo;
}




public void setSegundo(int segundo) {
	this.segundo = segundo;
}




public int getMinuto() {
	return minuto;
}




public void setMinuto(int minuto) {
	this.minuto = minuto;
}




public int getHora() {
	return hora;
}




public void setHora(int hora) {
	this.hora = hora;
}




public static long getSerialversionuid() {
	return serialVersionUID;
}





}
