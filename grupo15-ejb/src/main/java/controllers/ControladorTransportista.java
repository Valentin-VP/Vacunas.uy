package controllers;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import datatypes.DtTransportista;
import datatypes.TransportistaInexistente;
import entities.Transportista;
import exceptions.TransportistaRepetido;
import interfaces.ITransportistaDaoLocal;
import interfaces.ITransportistaDaoRemote;

@Stateless
public class ControladorTransportista implements ITransportistaDaoLocal, ITransportistaDaoRemote {
	
	@PersistenceContext(name = "test")
	private EntityManager em;

	@Override
	public void agregarTransportista(Integer id)
			throws TransportistaRepetido {
		try {
			obtenerTransportista(id);
		} catch (TransportistaInexistente e) {
			Transportista transportista = new Transportista (id);
			em.persist(transportista);
		}
	}

	@Override
	public DtTransportista obtenerTransportista(Integer id) throws TransportistaInexistente {
		return null;
	}

	@Override
	public List<DtTransportista> listarTransportistas() {
		return null;
	}

}
