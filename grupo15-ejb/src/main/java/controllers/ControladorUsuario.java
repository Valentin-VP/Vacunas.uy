package controllers;

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
import datatypes.DtCiudadano;
import datatypes.DtDireccion;
import datatypes.DtUsuario;
import datatypes.DtUsuarioInterno;
import datatypes.DtVacunador;
import datatypes.Rol;
import datatypes.Sexo;
import entities.Ciudadano;
import entities.Usuario;
import entities.UsuarioInterno;
import entities.Vacunador;



@Stateless 
public class ControladorUsuario implements IUsuarioRemote, IUsuarioLocal{

	@PersistenceContext(name = "test")
	private EntityManager em;
	private DtUsuario usuario;
	
		@Override
		public ArrayList<DtCiudadano> listarCiudadanos() {
			//ManejadorUsuario mu = ManejadorUsuario.getInstancia();
			//ArrayList<Usuario> u = mu.listarUsuarios();
			ArrayList<DtCiudadano> Usu = new ArrayList<>();
			
			
			Query q = em.createQuery("select u from Ciudadano u");
			ArrayList<Ciudadano> usuarios = (ArrayList<Ciudadano>) q.getResultList();
			
			for(Ciudadano u: usuarios) {
				Usu.add(new DtCiudadano(u.getIdUsuario(),u.getNombre(),u.getApellido(),u.getFechaNac(), u.getEmail(), u.getDireccion(), u.getSexo(), u.getTipoSector(), u.isAutenticado()));
			}
				
			return Usu;
		}
		
		@Override
		public ArrayList<DtUsuarioInterno> listarUsuariosInternos() {
			//ManejadorUsuario mu = ManejadorUsuario.getInstancia();
			//ArrayList<Usuario> u = mu.listarUsuarios();
			ArrayList<DtUsuarioInterno> Usu = new ArrayList<>();

			Query q1 = em.createQuery("select u from UsuarioInterno u");
			List<UsuarioInterno> usuarios = (List<UsuarioInterno>) q1.getResultList();
			
			
			for(UsuarioInterno u: usuarios) {
				Usu.add(new DtUsuarioInterno(u.getNombre(),u.getApellido(),u.getFechaNac(),u.getIdUsuario(), u.getEmail(), u.getDireccion(), u.getSexo(), u.getPassword(), u.getRol()));
			}
				
			return Usu;
		}
		

		@Override
		public ArrayList<DtVacunador> listarVacunadores() {
			//ManejadorUsuario mu = ManejadorUsuario.getInstancia();
			//ArrayList<Usuario> u = mu.listarUsuarios();
			ArrayList<DtVacunador> Usu = new ArrayList<>();

			Query q2 = em.createQuery("select v from Vacunador v");
			List<Vacunador> usuarios = (List<Vacunador>) q2.getResultList();
			
			
			for(Vacunador u: usuarios) {
				Usu.add(new DtVacunador(u.getNombre(),u.getApellido(),u.getFechaNac(),u.getIdUsuario(), u.getEmail(), u.getDireccion(), u.getSexo()));
			}
				
			return Usu;
		}

		@Override
		public void agregarUsuarioVacunador(int IdUsuario, String nombre, String apellido, Date fechaNac, String email, DtDireccion direccion, Sexo sexo) throws UsuarioExistente {
				//ManejadorUsuario mu = ManejadorUsuario.getInstancia();
				
				if (em.find(Vacunador.class,IdUsuario) != null) {
					throw new UsuarioExistente("Ya existe el usuario ingresado");
				}
				
				Vacunador usu = new Vacunador(IdUsuario, nombre, apellido, fechaNac, email, direccion, sexo);
				//mu.agregarUsuario(usu);
				
				em.persist(usu);
			
		}
		
		@Override
		public void agregarUsuarioInterno(int IdUsuario, String nombre, String apellido, Date fechaNac, String email, DtDireccion direccion, Sexo sexo, String password, Rol rol) throws UsuarioExistente {
			//ManejadorUsuario mu = ManejadorUsuario.getInstancia();
			
			if (em.find(UsuarioInterno.class,IdUsuario) != null) {
				throw new UsuarioExistente("Ya existe el usuario ingresado");
			}
			
			UsuarioInterno usu = new UsuarioInterno(IdUsuario, nombre, apellido, fechaNac,email, direccion,sexo,  password, rol);
			//mu.agregarUsuario(usu);
			
			em.persist(usu);
		
	}
		
		@Override
		public void agregarUsuarioCiudadano(int IdUsuario, String nombre, String apellido, Date fechaNac, String email, DtDireccion direccion, Sexo sexo, String TipoSector, Boolean autenticado) throws UsuarioExistente {
			//ManejadorUsuario mu = ManejadorUsuario.getInstancia();
			
			if (em.find(Ciudadano.class,IdUsuario) != null) {
				throw new UsuarioExistente("Ya existe el usuario ingresado");
			}
			
			Ciudadano usu = new Ciudadano(IdUsuario, nombre, apellido, fechaNac, email, direccion, sexo, TipoSector, autenticado);
			//mu.agregarUsuario(usu);
			
			em.persist(usu);
		
	}
		
	

		@Override
		public DtUsuario buscarUsuario(int IdUsuario) {
				//ManejadorUsuario mu = ManejadorUsuario.getInstancia();
				Usuario usu = em.find(Usuario.class,IdUsuario);
				DtUsuario dt = new DtUsuario(usu.getNombre(),usu.getApellido(), usu.getFechaNac(),usu.getIdUsuario(), usu.getEmail(), usu.getDireccion(), usu.getSexo());
				return dt;
		}



		@Override
		public void EliminarUsuario(int IdUsuario) throws UsuarioExistente {	

			if (em.find(Usuario.class,IdUsuario) == null) {
				throw new UsuarioExistente("No existe el usuario ingresado");
			}
			
			Usuario usu = em.find(Usuario.class,IdUsuario);
			em.remove(usu);
		}
		
		@Override
		public void ModificarVacunador(DtVacunador vacunador) throws UsuarioExistente {
			
			
			if (em.find(Vacunador.class,vacunador.getIdUsuario()) == null) {
				throw new UsuarioExistente("No existe el usuario ingresado");
			}
			
			Vacunador vac = em.find(Vacunador.class,vacunador.getIdUsuario());
			
			
			vac.setIdUsuario(vacunador.getIdUsuario());
			vac.setNombre(vacunador.getNombre());
			vac.setApellido(vacunador.getApellido());
			vac.setDireccion(vacunador.getDireccion());
			vac.setEmail(vacunador.getEmail());
			vac.setFechaNac(vacunador.getFechaNac());
			vac.setSexo(vacunador.getSexo());
			em.persist(vac);
			
			}
		
		@Override
		public void ModificarUsuarioInterno(DtUsuarioInterno usu) throws UsuarioExistente {
			
			
			if (em.find(UsuarioInterno.class,usu.getIdUsuario()) == null) {
				throw new UsuarioExistente("No existe el usuario ingresado");
			}
			
			UsuarioInterno u = em.find(UsuarioInterno.class,usu.getIdUsuario());
			
			
			u.setIdUsuario(usu.getIdUsuario());
			u.setNombre(usu.getNombre());
			u.setApellido(usu.getApellido());
			u.setDireccion(usu.getDireccion());
			u.setEmail(usu.getEmail());
			u.setFechaNac(usu.getFechaNac());
			u.setSexo(usu.getSexo());
			em.persist(u);
			
			}
		
		@Override
	public void ModificarCiudadano(DtCiudadano ciudadano) throws UsuarioExistente {
	
	
	if (em.find(Ciudadano.class,ciudadano.getIdUsuario()) == null) {
		throw new UsuarioExistente("No existe el usuario ingresado");
	}
	
	Ciudadano ciu = em.find(Ciudadano.class,ciudadano.getIdUsuario());
	
	
	ciu.setIdUsuario(ciudadano.getIdUsuario());
	ciu.setNombre(ciudadano.getNombre());
	ciu.setApellido(ciudadano.getApellido());
	ciu.setDireccion(ciudadano.getDireccion());
	ciu.setEmail(ciudadano.getEmail());
	ciu.setFechaNac(ciudadano.getFechaNac());
	ciu.setSexo(ciudadano.getSexo());
	em.persist(ciu);
	
	}
				
		}
		 
	

