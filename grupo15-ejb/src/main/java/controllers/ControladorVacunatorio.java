package controllers;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SecureRandom;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import interfaces.IControladorVacunatorioLocal;
import interfaces.IControladorVacunatorioRemote;
import datatypes.DtDireccion;
import entities.ReglasCupos;
import entities.Vacunatorio;
import datatypes.DtVacunatorio;
import exceptions.AccionInvalida;
import exceptions.ReglasCuposCargadoException;
import exceptions.VacunatorioCargadoException;
import exceptions.VacunatorioNoCargadoException;
import exceptions.VacunatoriosNoCargadosException;

@Stateless
public class ControladorVacunatorio implements IControladorVacunatorioLocal, IControladorVacunatorioRemote {
	@PersistenceContext(name = "test")
	private EntityManager em;

	public void agregarVacunatorio(String id, String nombre, DtDireccion dtDir, Integer telefono, Float latitud,
			Float longitud, String url) throws VacunatorioCargadoException {

		Vacunatorio vac = em.find(Vacunatorio.class, id);

		if (vac == null) {
			Vacunatorio vacNew = new Vacunatorio(id, nombre, dtDir, telefono, latitud, longitud, url);

			em.persist(vacNew);

		} else {
			throw new VacunatorioCargadoException("El vacunatorio " + id + " ya existe en el sistema\n");

		}
	}
	
	public void setURLtoVacunatorio(String id, String url) throws VacunatorioNoCargadoException {
		Vacunatorio vac = em.find(Vacunatorio.class, id);

		if (vac == null) {
			throw new VacunatorioNoCargadoException("El vacunatorio " + id + " no existe en el sistema");
		}else {
			vac.setUrl(url);
			em.merge(vac);
		}
	}
	
	public void generarTokenVacunatorio(String id) throws VacunatorioNoCargadoException, AccionInvalida {
		Vacunatorio vac = em.find(Vacunatorio.class, id);

		if (vac == null) {
			throw new VacunatorioNoCargadoException("El vacunatorio " + id + " no existe en el sistema");
		}else {
			byte[] salt;
			try {
				salt = getSalt();
				String encoded = getSecurePassword(id, salt);
				//String encoded = Base64.getEncoder().encodeToString(id.getBytes());
				vac.setToken(encoded);
				em.merge(vac);
			} catch (NoSuchAlgorithmException | NoSuchProviderException e) {
				throw new AccionInvalida(e.getMessage());
			}
		}
	}
	
	public boolean isTokenCorrecto(String id, String token) throws VacunatorioNoCargadoException {
		Vacunatorio vac = em.find(Vacunatorio.class, id);

		if (vac == null) {
			throw new VacunatorioNoCargadoException("El vacunatorio " + id + " no existe en el sistema");
		}else {
			if (vac.getToken().equals(token))
				return true;
			return false;
		}
	}

	public void agregarReglasCupos(String idVac, String id, Integer duracionTurno, LocalTime horaApertura,
			LocalTime horaCierre) throws VacunatorioNoCargadoException, ReglasCuposCargadoException {
		Vacunatorio vac = em.find(Vacunatorio.class, idVac);

		if (vac == null) {
			throw new VacunatorioNoCargadoException("El vacunatorio " + id + " no existe en el sistema");
		} else {
			if (vac.getReglasCupos() != null) {
				throw new ReglasCuposCargadoException("La regla de cupo ya existe en el sistema");
			} else {
				ReglasCupos reglasNew = new ReglasCupos(id, duracionTurno, horaApertura, horaCierre);
				vac.setReglasCupos(reglasNew);
				em.merge(vac);
				em.persist(reglasNew);
			}
		}
	}

	public DtVacunatorio obtenerVacunatorio(String id) throws VacunatorioNoCargadoException {

		Vacunatorio vac = em.find(Vacunatorio.class, id);

		if (vac == null) {

			throw new VacunatorioNoCargadoException("El vacunatorio " + id + " no existe en el sistema\n");

		} else {
			DtVacunatorio dtVac = new DtVacunatorio(vac.getId(), vac.getNombre(), vac.getDtDir(), vac.getTelefono(),
					vac.getLatitud(), vac.getLongitud(), vac.getUrl(), vac.getToken());
			return dtVac;
		}

	}

	public ArrayList<DtVacunatorio> listarVacunatorio() throws VacunatoriosNoCargadosException {

		Query query = em.createQuery("SELECT v FROM Vacunatorio v");
		List<Vacunatorio> aux = query.getResultList();
		ArrayList<DtVacunatorio> vac = new ArrayList<DtVacunatorio>();

		for (Vacunatorio v : aux) {

			DtVacunatorio dtVac = new DtVacunatorio(v.getId(), v.getNombre(), v.getDtDir(), v.getTelefono(),
					v.getLatitud(), v.getLongitud(), v.getUrl(), v.getToken());
			vac.add(dtVac);
		}
		if (aux.isEmpty()) {
			throw new VacunatoriosNoCargadosException("No existen vacunatorios en el sistema\n");
		} else {

			return vac;
		}

	}

	public void modificarVacunatorio(String id, String nombre, DtDireccion dtDir, Integer telefono, Float latitud,
			Float longitud, String url) throws VacunatorioNoCargadoException {

		Vacunatorio vac = em.find(Vacunatorio.class, id);
		if (vac==null) {
			throw new VacunatorioNoCargadoException("El vacunatorio " + id + " no existe en el sistema\n");
		}
		vac.setNombre(nombre);
		vac.setDtDir(dtDir);
		vac.setLatitud(latitud);
		vac.setLongitud(longitud);
		vac.setTelefono(telefono);
		vac.setUrl(url);
		em.merge(vac);
	}

	public void eliminarVacunatorio(String id) throws VacunatorioNoCargadoException {

		Vacunatorio vac = em.find(Vacunatorio.class, id);

		if (vac == null) {

			throw new VacunatorioNoCargadoException("El vacunatorio " + id + " no existe en el sistema\n");

		} else {
			em.remove(vac);
		}

	}
	
	private String getSecurePassword(String passwordToHash, byte[] salt){
        String generatedPassword = null;
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(salt);
            byte[] bytes = md.digest(passwordToHash.getBytes());
            StringBuilder sb = new StringBuilder();
            for(int i=0; i< bytes.length ;i++)
            {
                sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
            }
            generatedPassword = sb.toString();
        } 
        catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return generatedPassword;
    }

	private byte[] getSalt() throws NoSuchAlgorithmException, NoSuchProviderException{
        SecureRandom sr = SecureRandom.getInstance("SHA1PRNG", "SUN");
        byte[] salt = new byte[16];
        sr.nextBytes(salt);
        return salt;
    }
}
