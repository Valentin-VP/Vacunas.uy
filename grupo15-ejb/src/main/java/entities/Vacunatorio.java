package entities;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import datatypes.DtDireccion;

@Entity
public class Vacunatorio {
	@Id
	private String id;
	@Column(nullable = false)
	private String nombre;
	private DtDireccion dtDir;
	private Integer telefono;
	private Float latitud;
	private Float longitud;
	//@OneToMany(cascade = CascadeType.ALL)
	//private List<ReglasCupos> reglasCupos = new ArrayList<>();
	@OneToOne
	private ReglasCupos reglasCupos;
	@OneToMany(mappedBy = "vacunatorio", cascade = CascadeType.ALL)
	private List<Puesto> puesto = new ArrayList<Puesto>();
	
	@OneToMany(cascade = CascadeType.ALL)
	private List<Stock> stock = new ArrayList<Stock>();
	@OneToMany(mappedBy = "vacunatorio", cascade = CascadeType.ALL)
	private List<Agenda> agenda = new ArrayList<Agenda>();
	
	@OneToMany(cascade = CascadeType.ALL)
	private List<LoteDosis> lote = new ArrayList<LoteDosis>();

	public Vacunatorio(String id, String nombre, DtDireccion dtDir, Integer telefono, Float latitud, Float longitud) {
		super();
		this.id = id;
		this.nombre = nombre;
		this.dtDir = dtDir;
		this.telefono = telefono;
		this.latitud = latitud;
		this.longitud = longitud;

	}

	public Vacunatorio() {
		super();
		// TODO Auto-generated constructor stub
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

	public Float getLongitud() {
		return longitud;
	}

	public void setLongitud(Float longitud) {
		this.longitud = longitud;
	}

	public ReglasCupos getReglasCupos() {
		return reglasCupos;
	}

	public void setReglasCupos(ReglasCupos reglasCupos) {
		this.reglasCupos = reglasCupos;
	}

	public List<Puesto> getPuesto() {
		return puesto;
	}

	public void setPuesto(ArrayList<Puesto> puesto) {
		this.puesto = puesto;
	}

	public List<Stock> getStock() {
		return stock;
	}

	public void setStock(ArrayList<Stock> stock) {
		this.stock = stock;
	}

	public List<Agenda> getAgenda() {
		return agenda;
	}

	public void setAgenda(ArrayList<Agenda> agenda) {
		this.agenda = agenda;
	}

	public List<LoteDosis> getLote() {
		return lote;
	}

	public void setLote(List<LoteDosis> lote) {
		this.lote = lote;
	}

}