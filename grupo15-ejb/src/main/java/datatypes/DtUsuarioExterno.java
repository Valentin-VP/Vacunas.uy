package datatypes;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class DtUsuarioExterno implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private String ci;
	private String fechaNac;
	private String tipoSector;
	private boolean enfermedadesPrevias;

	public DtUsuarioExterno() {}

	public DtUsuarioExterno(String ci, String fechaNac, String tipoSector, boolean enfermedadesPrevias) {
		super();
		this.ci = ci;
		this.fechaNac = fechaNac;
		this.tipoSector = tipoSector;
		this.enfermedadesPrevias = enfermedadesPrevias;
	}

	public String getCi() {
		return ci;
	}

	public void setCi(String ci) {
		this.ci = ci;
	}

	public String getFechaNac() {
		return fechaNac;
	}

	public void setFechaNac(String fechaNac) {
		this.fechaNac = fechaNac;
	}

	public String getTipoSector() {
		return tipoSector;
	}

	public void setTipoSector(String tipoSector) {
		this.tipoSector = tipoSector;
	}

	public boolean isEnfermedadesPrevias() {
		return enfermedadesPrevias;
	}

	public void setEnfermedadesPrevias(boolean enfermedadesPrevias) {
		this.enfermedadesPrevias = enfermedadesPrevias;
	}
	
	

}
