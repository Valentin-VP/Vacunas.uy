package controllers;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import datatypes.DtTransportista;
import datatypes.TransportistaInexistente;
import entities.Transportista;
import exceptions.AccionInvalida;
import exceptions.TransportistaRepetido;
import interfaces.ITransportistaDaoLocal;
import interfaces.ITransportistaDaoRemote;

@Stateless
public class ControladorTransportista implements ITransportistaDaoLocal, ITransportistaDaoRemote {
	
	@PersistenceContext(name = "test")
	private EntityManager em;

	public void agregarTransportista(Integer id, String url) throws TransportistaRepetido {
		try {
			obtenerTransportista(id);
		} catch (TransportistaInexistente e) {
			Transportista transportista = new Transportista (id, url);
			em.persist(transportista);
		}
	}
	
	public void setURLtoTransportista(Integer id, String url) throws TransportistaInexistente {
		Transportista t = em.find(Transportista.class, id);
		if (t==null)
			throw new TransportistaInexistente("No existe tal transportista.");
		t.setUrl(url);
		em.merge(t);
	}
	
	public void generarTokenTransportista(Integer id) throws TransportistaInexistente, AccionInvalida {
		Transportista t = em.find(Transportista.class, id);
		if (t==null)
			throw new TransportistaInexistente("No existe tal transportista.");
		byte[] salt;
		try {
			salt = getSalt();
			String encoded = getSecurePassword(String.valueOf(id), salt);
			//String encoded = Base64.getEncoder().encodeToString(id.toString().getBytes());
			t.setToken(encoded);
			em.merge(t);
		} catch (NoSuchAlgorithmException | NoSuchProviderException e) {
			throw new AccionInvalida(e.getMessage());
		}
	}
	
	public boolean isTokenCorrecto(Integer id, String token) throws TransportistaInexistente {
		Transportista t = em.find(Transportista.class, id);
		if (t==null)
			throw new TransportistaInexistente("No existe tal transportista.");
		if (t.getToken().equals(token))
			return true;
		return false;
	}

	public DtTransportista obtenerTransportista(Integer id) throws TransportistaInexistente {
		Transportista t = em.find(Transportista.class, id);
		if (t==null)
			throw new TransportistaInexistente("No existe tal transportista.");
		return new DtTransportista(t.getId(), t.getUrl(), t.getToken());
	}

	public List<DtTransportista> listarTransportistas() throws TransportistaInexistente {
		Query query = em.createQuery("SELECT t FROM Transportista t");
		@SuppressWarnings("unchecked")
		List<Transportista> aux = query.getResultList();
		ArrayList<DtTransportista> retorno = new ArrayList<>();
		if (aux.isEmpty())
			throw new TransportistaInexistente("No existe ningun transportista.");
		for (Transportista t: aux) {
			retorno.add(new DtTransportista(t.getId(), t.getUrl(), t.getToken()));
		}
		return retorno;
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
