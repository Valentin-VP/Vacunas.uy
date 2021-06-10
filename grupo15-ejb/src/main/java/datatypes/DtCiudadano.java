package datatypes;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class DtCiudadano extends DtUsuario {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String TipoSector;
	private boolean autenticado;
	private String mobileToken = null;
	private List<DtReserva> reservas = new ArrayList<DtReserva>();
	private DtCertificadoVac certificado = null;

	public DtCiudadano() {
		super();
	}

	public DtCiudadano(int IdUsuario, String nombre, String apellido, LocalDate fechaNac, String email,
			DtDireccion direccion, Sexo sexo, String TipoSector, Boolean autenticado) {
		super(nombre, apellido, fechaNac, IdUsuario, email, direccion, sexo);
		this.autenticado = autenticado;
		this.TipoSector = TipoSector;

	}

	public DtCiudadano(int IdUsuario, String nombre, String apellido, LocalDate fechaNac, String email,
			DtDireccion direccion, Sexo sexo, String token, String TipoSector, Boolean autenticado) {
		super(nombre, apellido, fechaNac, IdUsuario, email, direccion, sexo, token);
		this.autenticado = autenticado;
		this.TipoSector = TipoSector;

	}

	public DtCiudadano(int IdUsuario, String nombre, String apellido, LocalDate fechaNac, String email,
			DtDireccion direccion, Sexo sexo, String token, String TipoSector, Boolean autenticado,
			String mobileToken, List<DtReserva> reservas, DtCertificadoVac certificado) {
		super(nombre, apellido, fechaNac, IdUsuario, email, direccion, sexo, token);
		this.autenticado = autenticado;
		this.TipoSector = TipoSector;
		this.mobileToken = mobileToken;
		this.setReservas(reservas);
		this.certificado = certificado;
	}

	public String getTipoSector() {
		return TipoSector;
	}

	public void setTipoSector(String tipoSector) {
		TipoSector = tipoSector;
	}

	public boolean isAutenticado() {
		return autenticado;
	}

	public void setAutenticado(boolean autenticado) {
		this.autenticado = autenticado;
	}

	public DtCertificadoVac getCertificado() {
		return certificado;
	}

	public void setCertificado(DtCertificadoVac certificado) {
		this.certificado = certificado;
	}

	public List<DtReserva> getReservas() {
		return reservas;
	}

	public void setReservas(List<DtReserva> reservas) {
		this.reservas = reservas;
	}

	public String getMobileToken() {
		return mobileToken;
	}

	public void setMobileToken(String mobileToken) {
		this.mobileToken = mobileToken;
	}

}
