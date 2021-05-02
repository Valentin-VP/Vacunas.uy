package controllers;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.EJB;
import javax.ejb.PostActivate;
import javax.ejb.PrePassivate;
import javax.ejb.Remove;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import datatypes.DtLoteDosis;
import datatypes.DtTransportista;
import datatypes.EstadoLote;
import datatypes.TransportistaInexistente;
import exceptions.LoteInexistente;
import exceptions.LoteRepetido;
import interfaces.ILoteDosisDaoLocal;
import interfaces.ILoteDosisDaoRemote;
import entities.LoteDosis;
import entities.Transportista;

@Stateless
public class ControladorLoteDosis implements ILoteDosisDaoRemote, ILoteDosisDaoLocal {

	@PersistenceContext(name = "test")
	private EntityManager em;

	@EJB
	ControladorTransportista controladorTransportista;

	@Override
	public void agregarLoteDosis(Integer idLote, Integer cantidadTotal, float temperatura) throws LoteRepetido {
		try {
			obtenerLoteDosis(idLote);
		} catch (LoteInexistente e) {
			LoteDosis loteDosis = new LoteDosis(idLote, cantidadTotal, 0, 0, temperatura);
			em.persist(loteDosis);
		}

	}

	@Override
	public DtLoteDosis obtenerLoteDosis(Integer idLote) throws LoteInexistente {
		DtLoteDosis dtLoteDosis = null;
		LoteDosis lote = em.find(LoteDosis.class, idLote);
		if (lote != null) {
			dtLoteDosis = new DtLoteDosis(lote.getIdLote(), lote.getCantidadTotal(), lote.getCantidadEntregada(),
					lote.getCantidadDescartada(), lote.getEstadoLote().toString(), lote.getTemperatura());
		} else {
			throw new LoteInexistente("No se encontró un Lote con ese ID");
		}
		return dtLoteDosis;
	}

	@Override
	public List<DtLoteDosis> listarLotesDosis() {
		List<DtLoteDosis> dtLotesDosis = new ArrayList<DtLoteDosis>();
		Query query = em.createQuery("select lote from LoteDosis lote");
		@SuppressWarnings("unchecked")
		List<LoteDosis> lotesDosis = (List<LoteDosis>) query.getResultList();
		for (LoteDosis lote : lotesDosis) {
			DtLoteDosis dtLoteDosis = new DtLoteDosis(lote.getIdLote(), lote.getCantidadTotal(),
					lote.getCantidadEntregada(), lote.getCantidadDescartada(), lote.getEstadoLote().toString(),
					lote.getTemperatura());
			dtLotesDosis.add(dtLoteDosis);
		}
		return dtLotesDosis;
	}

	@Override
	public void setTransportistaToLoteDosis(Integer idTransportista, Integer idLote) throws TransportistaInexistente {
		// Asocia un Transportista a un LoteDosis.
		// PRE: Ya debe existir el LoteDosis y el transportista
		if (existeLoteDosis(idLote)) {
			LoteDosis lote = em.find(LoteDosis.class, idLote);
			Transportista transportista = null;
			/*
			 * // >> Forma 1), pedir al ControladorTransportista
			 * 
			 * try { DtTransportista dtTransportista =
			 * controladorTransportista.obtenerTransportista(idTransportista); // El
			 * ControladorTransportista me retorna un DtTransportista, y yo preciso asociar
			 * un Transportista // A priori no sirve porque no puedo volver a crear otro
			 * Transportista } catch (TransportistaInexistente e) { // Transportista no
			 * existe, arrojar excepcion }
			 */
			// >> Forma 2), acceder directamente con el Entity Manager
			transportista = em.find(Transportista.class, idTransportista);
			if (transportista != null) {
				lote.setTransportista(transportista);
				em.persist(lote);
			} else {
				// Transportista no existe, arrojar excepcion
				throw new TransportistaInexistente("No existe el Transportista con ID " + idTransportista);
			}
		}

	}

	@Override
	public Integer getTransportistaIdFromLoteDosis(Integer idLote) {
		Integer idTransportista = 0;
		LoteDosis lote = em.find(LoteDosis.class, idLote);
		if (lote != null) {
			idTransportista = lote.getTransportista().getId();
		}
		// Si no existe el Lote, o no tiene Transportista asociado, se retorna 0
		return idTransportista;
	}

	@Override
	public void modificarLoteDosis(Integer idLote, Integer cantidadTotal, Integer cantidadEntregada,
			Integer cantidadDescartada, String estadoLote, float temperatura, Integer transportista)
			throws LoteInexistente, TransportistaInexistente {
		LoteDosis lote = em.find(LoteDosis.class, idLote);
		if (lote != null) {
			try {
				
				lote.setCantidadTotal(cantidadTotal);
				lote.setCantidadEntregada(cantidadEntregada);
				lote.setCantidadEntregada(cantidadEntregada);
				lote.setEstadoLote(EstadoLote.valueOf(estadoLote)); // valueOf() method does a case-sensitive match of
																	// the argument supplied to it, so passing a value
																	// that doesn't match the case of any of the
																	// original enum‘s values would lead to an
																	// IllegalArgumentException
				lote.setTemperatura(temperatura);
				Transportista nuevoTransportista = em.find(Transportista.class, transportista);
				if (nuevoTransportista == null) {
					throw new TransportistaInexistente("No existe el Transportista con ID " + transportista);
				}
				lote.setTransportista(nuevoTransportista);
				em.merge(lote);
			} catch (IllegalArgumentException e) {
				throw new LoteInexistente("Error en Enum de EstadoLote: " + estadoLote);
			} catch (TransportistaInexistente e) {
				throw e;
			}
		} else {
			throw new LoteInexistente("No se encontró un Lote con ese ID");
		}
	}
	
	@Override
	public void eliminarLoteDosis(Integer idLote) throws LoteInexistente {
		LoteDosis lote = em.find(LoteDosis.class, idLote);
		if (lote != null) {
			em.remove(lote);
		}else {
			throw new LoteInexistente("No se encontró un Lote con ese ID");
		}
		
	}

	private boolean existeLoteDosis(Integer idLoteDosis) {
		for (DtLoteDosis lote : listarLotesDosis()) {
			if (lote.getIdLote() == idLoteDosis) {
				return true;
			}
		}
		return false;
	}

	// Ciclo de Vida del EJB

	@PostConstruct
	public void PostConstruct() {

		System.out.println("PostConstruct");
	}

	@PostActivate
	public void PostActivate() {

		System.out.println("PostActivate");
	}

	@PrePassivate
	public void PrePassivate() {

		System.out.println("PrePassivate");
	}

	@PreDestroy
	public void shutdown() {

		System.out.println("PreDestroy");
	}

	@Remove
	public void remove() {

		System.out.println("Remove");
	}

}
