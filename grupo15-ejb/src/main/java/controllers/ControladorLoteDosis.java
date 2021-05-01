package controllers;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.PostActivate;
import javax.ejb.PrePassivate;
import javax.ejb.Remove;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import datatypes.DtLoteDosis;
import exceptions.LoteInexistente;
import exceptions.LoteRepetido;
import interfaces.ILoteDosisDaoLocal;
import interfaces.ILoteDosisDaoRemote;
import entities.LoteDosis;

@Stateless
public class ControladorLoteDosis implements ILoteDosisDaoRemote, ILoteDosisDaoLocal {

	@PersistenceContext(name = "GinnyPU")
	private EntityManager em;

	@Override
	public void agregarLoteDosis(Integer idLote, Integer cantidadDosis, float temperatura, String descripcion)
			throws LoteRepetido {
		try {
			obtenerLoteDosis(idLote);
		} catch (LoteInexistente e) {
			LoteDosis loteDosis = new LoteDosis(idLote, cantidadDosis, temperatura, descripcion);
			em.persist(loteDosis);
		}
		
	}

	@Override
	public DtLoteDosis obtenerLoteDosis(Integer idLote) throws LoteInexistente {
		DtLoteDosis dtLoteDosis = null;
		LoteDosis lote = em.find(LoteDosis.class, idLote);
		if (lote != null) {
			dtLoteDosis = new DtLoteDosis(lote.getIdLote(), lote.getCantidadDosis(), lote.getTemperatura(),
					lote.getDescripcion(), lote.getEstadoLote().toString());
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
			DtLoteDosis dtLoteDosis = new DtLoteDosis(lote.getIdLote(), lote.getCantidadDosis(), lote.getTemperatura(),
					lote.getDescripcion(), lote.getEstadoLote().toString());
			dtLotesDosis.add(dtLoteDosis);
		}
		return dtLotesDosis;
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

