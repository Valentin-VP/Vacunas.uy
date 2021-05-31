package datatypes;

import java.io.Serializable;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

public class DtDireccion implements Serializable {

	private static final long serialVersionUID = 1L;
	private String direccion;
	private String barrio;
	private String departamento;

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

	public JSONObject toJson() throws JSONException {
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("direccion", this.direccion);
		jsonObject.put("barrio", this.barrio);
		jsonObject.put("departamento", this.departamento);
		return jsonObject;
	}
}
