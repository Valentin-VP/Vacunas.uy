package datatypes;

public class DtMensaje {

private int IdMensaje;
private String contenido;
	
	public DtMensaje() {
		super();
	}
	
	public DtMensaje(int IdMensaje, String contenido) {
		super();
		this.IdMensaje = IdMensaje;
		this.contenido = contenido;
		
	}

	public int getIdMensaje() {
		return IdMensaje;
	}

	public void setIdMensaje(int idMensaje) {
		IdMensaje = idMensaje;
	}

	public String getContenido() {
		return contenido;
	}

	public void setContenido(String contenido) {
		this.contenido = contenido;
	}
	

	
	
	
	
}
