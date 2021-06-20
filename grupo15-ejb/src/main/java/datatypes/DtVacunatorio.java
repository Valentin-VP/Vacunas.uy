package datatypes;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlRootElement;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

@XmlRootElement
public class DtVacunatorio implements Serializable {
	private static final long serialVersionUID = 1L;
	private String id;
	private String nombre;
	private DtDireccion dtDir;
	private Integer telefono;
	private Float latitud;
	private Float longitud;
	private String url;
	private String token;
	
	public DtVacunatorio() {
		super();
		// TODO Auto-generated constructor stub
	}



	public DtVacunatorio(String id, String nombre, DtDireccion dtDir, Integer telefono, Float latitud, Float longitud) {
		super();
		this.id = id;
		this.nombre = nombre;
		this.dtDir = dtDir;
		this.telefono = telefono;
		this.latitud = latitud;
		this.longitud = longitud;
	}



	public DtVacunatorio(String id, String nombre, DtDireccion dtDir, Integer telefono, Float latitud, Float longitud,
			String url) {
		super();
		this.id = id;
		this.nombre = nombre;
		this.dtDir = dtDir;
		this.telefono = telefono;
		this.latitud = latitud;
		this.longitud = longitud;
		this.url = url;
	}



	public DtVacunatorio(String id, String nombre, DtDireccion dtDir, Integer telefono, Float latitud, Float longitud,
			String url, String token) {
		super();
		this.id = id;
		this.nombre = nombre;
		this.dtDir = dtDir;
		this.telefono = telefono;
		this.latitud = latitud;
		this.longitud = longitud;
		this.url = url;
		this.token = token;
	}



	public String getUrl() {
		return url;
	}



	public void setUrl(String url) {
		this.url = url;
	}



	public String getId() {
		return id;
	}



	public void setId(String id) {
		this.id = id;
	}



	public String getNombre() {
		return nombre;
	}



	public void setNombre(String nombre) {
		this.nombre = nombre;
	}



	public DtDireccion getDtDir() {
		return dtDir;
	}



	public void setDtDir(DtDireccion dtDir) {
		this.dtDir = dtDir;
	}



	public Integer getTelefono() {
		return telefono;
	}



	public void setTelefono(Integer telefono) {
		this.telefono = telefono;
	}



	public Float getLatitud() {
		return latitud;
	}



	public void setLatitud(Float latitud) {
		this.latitud = latitud;
	}



	public String getToken() {
		return token;
	}



	public void setToken(String token) {
		this.token = token;
	}



	public Float getLongitud() {
		return longitud;
	}



	public void setLongitud(Float longitud) {
		this.longitud = longitud;
	}
	
	public JSONObject toJson() throws JSONException {
		JSONObject jsonObject = new JSONObject();
		jsonObject.put( "id", this.id );
		jsonObject.put( "nombre", this.nombre );
		jsonObject.put( "dtDir", this.dtDir.toJson() );
		jsonObject.put( "telefono", this.telefono );
		jsonObject.put( "latitud", this.latitud );
		jsonObject.put( "longitud", this.longitud );
		jsonObject.put( "url", this.url );
		jsonObject.put( "token", this.token );
		return jsonObject;
	}

}
