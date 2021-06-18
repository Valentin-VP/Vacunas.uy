package controllers;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import interfaces.IUsuarioLocal;
import interfaces.IUsuarioRemote;
import exceptions.UsuarioExistente;
import exceptions.UsuarioInexistente;
import datatypes.DtCertificadoVac;
import datatypes.DtCiudadano;
import datatypes.DtConstancia;
import datatypes.DtDireccion;
import datatypes.DtReserva;
import datatypes.DtUsuario;
import datatypes.DtUsuarioInterno;
import datatypes.DtUsuarioSoap;
import datatypes.DtVacunador;
import datatypes.EstadoReserva;
import datatypes.Rol;
import datatypes.Sexo;
import datatypes.TipoUsuario;
import entities.CertificadoVacunacion;
import entities.Ciudadano;
import entities.ConstanciaVacuna;
import entities.Reserva;
import entities.Usuario;
import entities.UsuarioInterno;
import entities.Vacunador;

@Stateless
public class ControladorUsuario implements IUsuarioRemote, IUsuarioLocal {

	@PersistenceContext(name = "test")
	private EntityManager em;

	
	public ArrayList<DtCiudadano> listarCiudadanos() {
		// ManejadorUsuario mu = ManejadorUsuario.getInstancia();
		// ArrayList<Usuario> u = mu.listarUsuarios();
		ArrayList<DtCiudadano> Usu = new ArrayList<>();

		Query q = em.createQuery("select u from Ciudadano u");
		@SuppressWarnings("unchecked")
		ArrayList<Ciudadano> usuarios = (ArrayList<Ciudadano>) q.getResultList();

		for (Ciudadano u : usuarios) {
			Usu.add(new DtCiudadano(u.getIdUsuario(), u.getNombre(), u.getApellido(), u.getFechaNac(), u.getEmail(),
					u.getDireccion(), u.getSexo(), u.getTipoSector(), u.isAutenticado()));
		}

		return Usu;
	}

	
	public ArrayList<DtUsuarioInterno> listarUsuariosInternos() {
		// ManejadorUsuario mu = ManejadorUsuario.getInstancia();
		// ArrayList<Usuario> u = mu.listarUsuarios();
		ArrayList<DtUsuarioInterno> Usu = new ArrayList<>();

		Query q1 = em.createQuery("select u from UsuarioInterno u");
		@SuppressWarnings("unchecked")
		List<UsuarioInterno> usuarios = (List<UsuarioInterno>) q1.getResultList();

		for (UsuarioInterno u : usuarios) {
			Usu.add(new DtUsuarioInterno(u.getNombre(), u.getApellido(), u.getFechaNac(), u.getIdUsuario(),
					u.getEmail(), u.getDireccion(), u.getSexo(), u.getRol()));
		}

		return Usu;
	}

	
	public ArrayList<DtVacunador> listarVacunadores() {
		// ManejadorUsuario mu = ManejadorUsuario.getInstancia();
		// ArrayList<Usuario> u = mu.listarUsuarios();
		ArrayList<DtVacunador> Usu = new ArrayList<>();

		Query q2 = em.createQuery("select v from Vacunador v");
		@SuppressWarnings("unchecked")
		List<Vacunador> usuarios = (List<Vacunador>) q2.getResultList();

		for (Vacunador u : usuarios) {
			Usu.add(new DtVacunador(u.getNombre(), u.getApellido(), u.getFechaNac(), u.getIdUsuario(), u.getEmail(),
					u.getDireccion(), u.getSexo()));
		}

		return Usu;
	}
	
	
	public ArrayList<DtUsuarioSoap> listarVacunadoresSoap() {
		ArrayList<DtUsuarioSoap> Usu = new ArrayList<>();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
		Query q2 = em.createQuery("select v from Vacunador v");
		@SuppressWarnings("unchecked")
		List<Vacunador> usuarios = (List<Vacunador>) q2.getResultList();

		for (Vacunador u : usuarios) {
			Usu.add(new DtUsuarioSoap(u.getNombre(), u.getApellido(), u.getFechaNac().format(formatter), u.getIdUsuario(), u.getEmail(),
					u.getDireccion(), u.getSexo()));
		}

		return Usu;
	}

	
	public void agregarUsuarioVacunador(int IdUsuario, String nombre, String apellido, LocalDate fechaNac, String email,
			DtDireccion direccion, Sexo sexo) throws UsuarioExistente {
		// ManejadorUsuario mu = ManejadorUsuario.getInstancia();

		if (em.find(Vacunador.class, IdUsuario) != null) {
			throw new UsuarioExistente("Ya existe el usuario ingresado");
		}

		Vacunador usu = new Vacunador(IdUsuario, nombre, apellido, fechaNac, email, direccion, sexo);
		// mu.agregarUsuario(usu);

		em.persist(usu);

	}

	
	public void agregarUsuarioInterno(int IdUsuario, String nombre, String apellido, LocalDate fechaNac, String email,
			DtDireccion direccion, Sexo sexo, Rol rol) throws UsuarioExistente {
		// ManejadorUsuario mu = ManejadorUsuario.getInstancia();

		if (em.find(UsuarioInterno.class, IdUsuario) != null) {
			throw new UsuarioExistente("Ya existe el usuario ingresado");
		}

		UsuarioInterno usu = new UsuarioInterno(IdUsuario, nombre, apellido, fechaNac, email, direccion, sexo, rol);
		// mu.agregarUsuario(usu);

		em.persist(usu);

	}

	
	public void agregarUsuarioCiudadano(int IdUsuario, String nombre, String apellido, LocalDate fechaNac, String email,
			DtDireccion direccion, Sexo sexo, String TipoSector, Boolean autenticado) throws UsuarioExistente {
		// ManejadorUsuario mu = ManejadorUsuario.getInstancia();

		if (em.find(Ciudadano.class, IdUsuario) != null) {
			throw new UsuarioExistente("Ya existe el usuario ingresado");
		}

		Ciudadano usu = new Ciudadano(IdUsuario, nombre, apellido, fechaNac, email, direccion, sexo, TipoSector,
				autenticado);
		// mu.agregarUsuario(usu);
		CertificadoVacunacion cv = new CertificadoVacunacion();
		usu.setCertificado(cv);
		em.persist(usu);

	}

	
	public DtUsuario buscarUsuario(int IdUsuario) {
		Usuario usu = em.find(Usuario.class, IdUsuario);
		DtUsuario dt = new DtUsuario(usu.getNombre(), usu.getApellido(), usu.getFechaNac(), usu.getIdUsuario(),
				usu.getEmail(), usu.getDireccion(), usu.getSexo(), usu.getToken());
		return dt;
	}

	
	public DtCiudadano buscarCiudadano(int id) throws UsuarioInexistente {
		Ciudadano ciudadano = em.find(Ciudadano.class, id);
		if (ciudadano == null) {
			throw new UsuarioInexistente("No se encuentra el ciudadano con ID " + id);
		}
		// Reservas
		List<DtReserva> reservas = new ArrayList<DtReserva>();
		for (Reserva res: ciudadano.getReservas()) {
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
			reservas.add(new DtReserva(res.getFechaRegistro().format(formatter), res.getPuesto().getVacunatorio().getId() ,res.getEstado(), 
					Integer.toString(res.getEtapa().getId()), Integer.toString(res.getCiudadano().getIdUsuario()),
					res.getPuesto().getId()));
		}
		// Certificado --> si es != null
		ArrayList<DtConstancia> constancias = new ArrayList<DtConstancia>();
		DtCertificadoVac certificado = null;
		if (ciudadano.getCertificado() != null) {
			for(ConstanciaVacuna constancia: ciudadano.getCertificado().getConstancias()) {
				constancias.add(new DtConstancia(constancia.getIdConstVac(), constancia.getPeriodoInmunidad(),
						constancia.getDosisRecibidas(), constancia.getFechaUltimaDosis(), constancia.getVacuna(), 
						constancia.getReserva().getDtReserva()));
			}
			certificado = new DtCertificadoVac(ciudadano.getCertificado().getIdCert(), constancias);
		}
		
		// Ciudadano
		DtCiudadano dt = new DtCiudadano(ciudadano.getIdUsuario(), ciudadano.getNombre(), ciudadano.getApellido(),
				ciudadano.getFechaNac(), ciudadano.getEmail(), ciudadano.getDireccion(), ciudadano.getSexo(),
				ciudadano.getToken(), ciudadano.getTipoSector(), ciudadano.isAutenticado(), ciudadano.getMobiletoken(),
				reservas, certificado);
		return dt;
	}

	
	public DtVacunador buscarVacunador(int id) throws UsuarioInexistente {
		Vacunador vacunador = em.find(Vacunador.class, id);
		if (vacunador == null) {
			throw new UsuarioInexistente("No se encuentra el vacunador con ID " + id);
		}
		DtVacunador dt = new DtVacunador(vacunador.getNombre(), vacunador.getApellido(), vacunador.getFechaNac(),
				vacunador.getIdUsuario(), vacunador.getEmail(), vacunador.getDireccion(), vacunador.getSexo(),
				vacunador.getToken());
		return dt;
	}
	
	
	public DtUsuarioInterno buscarUsuarioInterno(int id) throws UsuarioInexistente {
		UsuarioInterno interno = em.find(UsuarioInterno.class, id);
		if (interno == null) {
			throw new UsuarioInexistente("No se encuentra el usuario interno con ID " + id);
		}
		DtUsuarioInterno dt = new DtUsuarioInterno(interno.getNombre(), interno.getApellido(), interno.getFechaNac(),
				interno.getIdUsuario(), interno.getEmail(), interno.getDireccion(), interno.getSexo(), interno.getRol(),
				interno.getToken());
		return dt;
	}

	
	public DtUsuarioSoap buscarVacunadorSoap(int id) throws UsuarioInexistente {
		Vacunador vacunador = em.find(Vacunador.class, id);
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
		if (vacunador == null) {
			throw new UsuarioInexistente("No se encuentra el vacunador con ID " + id);
		}
		DtUsuarioSoap dt = new DtUsuarioSoap(vacunador.getNombre(), vacunador.getApellido(), vacunador.getFechaNac().format(formatter),
				vacunador.getIdUsuario(), vacunador.getEmail(), vacunador.getDireccion(), vacunador.getSexo());
		return dt;
	}
	
	
	public void EliminarUsuario(int IdUsuario) throws UsuarioInexistente {

		if (em.find(Usuario.class, IdUsuario) == null) {
			throw new UsuarioInexistente("No existe el usuario ingresado");
		}

		Usuario usu = em.find(Usuario.class, IdUsuario);
		em.remove(usu);
	}

	
	public void ModificarVacunador(DtVacunador vacunador) throws UsuarioInexistente {

		if (em.find(Vacunador.class, vacunador.getIdUsuario()) == null) {
			throw new UsuarioInexistente("No existe el usuario ingresado");
		}

		Vacunador vac = em.find(Vacunador.class, vacunador.getIdUsuario());

		vac.setIdUsuario(vacunador.getIdUsuario());
		vac.setNombre(vacunador.getNombre());
		vac.setApellido(vacunador.getApellido());
		vac.setDireccion(vacunador.getDireccion());
		vac.setEmail(vacunador.getEmail());
		vac.setFechaNac(vacunador.getFechaNac());
		vac.setSexo(vacunador.getSexo());
		vac.setToken(vacunador.getToken());
		em.persist(vac);

	}

	
	public void ModificarUsuarioInterno(DtUsuarioInterno usu) throws UsuarioInexistente {

		if (em.find(UsuarioInterno.class, usu.getIdUsuario()) == null) {
			throw new UsuarioInexistente("No existe el usuario ingresado");
		}

		UsuarioInterno u = em.find(UsuarioInterno.class, usu.getIdUsuario());

		u.setIdUsuario(usu.getIdUsuario());
		u.setNombre(usu.getNombre());
		u.setApellido(usu.getApellido());
		u.setDireccion(usu.getDireccion());
		u.setEmail(usu.getEmail());
		u.setFechaNac(usu.getFechaNac());
		u.setSexo(usu.getSexo());
		u.setToken(usu.getToken());
		em.persist(u);

	}

	
	public void ModificarCiudadano(DtCiudadano ciudadano) throws UsuarioInexistente {

		if (em.find(Ciudadano.class, ciudadano.getIdUsuario()) == null) {
			throw new UsuarioInexistente("No existe el usuario ingresado");
		}

		Ciudadano ciu = em.find(Ciudadano.class, ciudadano.getIdUsuario());

		ciu.setIdUsuario(ciudadano.getIdUsuario());
		ciu.setNombre(ciudadano.getNombre());
		ciu.setApellido(ciudadano.getApellido());
		ciu.setDireccion(ciudadano.getDireccion());
		ciu.setEmail(ciudadano.getEmail());
		ciu.setFechaNac(ciudadano.getFechaNac());
		ciu.setSexo(ciudadano.getSexo());
		ciu.setToken(ciudadano.getToken());
		ciu.setMobiletoken(ciudadano.getMobileToken());
		em.persist(ciu);

	}

	public void borrarToken(String ci) throws UsuarioInexistente {
		Ciudadano ciudadano = em.find(Ciudadano.class, Integer.valueOf(ci));
		if (ciudadano != null) {
			ciudadano.setToken(null);
			em.persist(ciudadano);
		}else{
			throw new UsuarioInexistente("No se encuentra el ciudadano con ID " + ci);
		}
	}
	
}
