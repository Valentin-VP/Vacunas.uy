package controllers;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import datatypes.DtHistoricoStock;
import entities.Historico;
import entities.Stock;
import entities.Vacuna;
import entities.Vacunatorio;
import exceptions.StockVacunaVacunatorioInexistente;
import exceptions.VacunaInexistente;
import exceptions.VacunatorioNoCargadoException;
import interfaces.IHistoricoDaoLocal;
import interfaces.IHistoricoDaoRemote;

@Stateless
public class ControladorHistorico implements IHistoricoDaoLocal, IHistoricoDaoRemote {

	@PersistenceContext(name = "test")
	private EntityManager em;

	public ControladorHistorico() {
	}

	@Override
	public void agregarHistorico(Date fecha, Integer cantidad, Integer descartadas, Integer disponibles,
			Integer administradas, String idVacunatorioStock, String idVacunaStock)
			throws VacunatorioNoCargadoException, VacunaInexistente, StockVacunaVacunatorioInexistente {
		// Se reciben todas las cantidades porque resume el stock de una fecha en
		// concreto, donde esos valores ya fueron registrados
		Vacunatorio vacunatorio = em.find(Vacunatorio.class, idVacunatorioStock);
		if (vacunatorio == null) {
			throw new VacunatorioNoCargadoException("No existe el vacunatorio con ID " + idVacunatorioStock);
		}
		if (em.find(Vacuna.class, idVacunaStock) == null) {
			throw new VacunaInexistente("No existe la vacuna con ID " + idVacunaStock);
		}
		for (Stock stock : vacunatorio.getStock()) {
			if (stock.getVacuna().getNombre().equals(idVacunaStock)) {
				Historico historico = new Historico(fecha, cantidad, descartadas, disponibles, administradas, stock);
				stock.getHistoricos().add(historico);
				em.merge(stock);
				em.persist(historico);
				return;
			}
		}
	}

	@Override
	public void modificarHistorico(Date fecha, Integer cantidad, Integer descartadas, Integer disponibles,
			Integer administradas, String idVacunatorioStock, String idVacunaStock)
			throws VacunatorioNoCargadoException, VacunaInexistente, StockVacunaVacunatorioInexistente {
		Vacunatorio vacunatorio = em.find(Vacunatorio.class, idVacunatorioStock);
		if (vacunatorio == null) {
			throw new VacunatorioNoCargadoException("No existe el vacunatorio con ID " + idVacunatorioStock);
		}
		if (em.find(Vacuna.class, idVacunaStock) == null) {
			throw new VacunaInexistente("No existe la vacuna con ID " + idVacunaStock);
		}
		for (Stock stock : vacunatorio.getStock()) {
			if (stock.getVacuna().getNombre().equals(idVacunaStock)) {
				for (Historico historico : stock.getHistoricos()) {
					if (historico.getFecha().compareTo(fecha) == 0) {
						historico.setCantidad(cantidad);
						historico.setDescartadas(descartadas);
						historico.setDisponibles(disponibles);
						historico.setAdministradas(administradas);
						em.persist(historico);
						break;
					}
				}
				em.merge(stock);
				return;
			}
		}
		throw new StockVacunaVacunatorioInexistente(String.format(
				"No se encontro Stock de la Vacuna %s en el Vacunatorio %s", idVacunaStock, idVacunatorioStock));
	}

	@Override
	public void eliminarHistorico(Date fecha, String idVacunatorioStock, String idVacunaStock)
			throws VacunatorioNoCargadoException, VacunaInexistente, StockVacunaVacunatorioInexistente {
		Vacunatorio vacunatorio = em.find(Vacunatorio.class, idVacunatorioStock);
		int index = 0;
		if (vacunatorio == null) {
			throw new VacunatorioNoCargadoException("No existe el vacunatorio con ID " + idVacunatorioStock);
		}
		if (em.find(Vacuna.class, idVacunaStock) == null) {
			throw new VacunaInexistente("No existe la vacuna con ID " + idVacunaStock);
		}
		for (Stock stock : vacunatorio.getStock()) {
			if (stock.getVacuna().getNombre().equals(idVacunaStock)) {
				for (Historico historico : stock.getHistoricos()) {
					if (historico.getFecha().compareTo(fecha) == 0) {
						em.remove(historico);
						break;
					}
					index++;
				}
				stock.getHistoricos().remove(index);
				em.merge(stock);
				return;
			}
		}
		throw new StockVacunaVacunatorioInexistente(String.format(
				"No se encontro Stock de la Vacuna %s en el Vacunatorio %s", idVacunaStock, idVacunatorioStock));
	}

	@Override
	public DtHistoricoStock obtenerHistorico(Date fecha, String idVacunatorioStock, String idVacunaStock)
			throws VacunatorioNoCargadoException, VacunaInexistente, StockVacunaVacunatorioInexistente {
		Vacunatorio vacunatorio = em.find(Vacunatorio.class, idVacunatorioStock);
		if (vacunatorio == null) {
			throw new VacunatorioNoCargadoException("No existe el vacunatorio con ID " + idVacunatorioStock);
		}
		if (em.find(Vacuna.class, idVacunaStock) == null) {
			throw new VacunaInexistente("No existe la vacuna con ID " + idVacunaStock);
		}
		for (Stock stock : vacunatorio.getStock()) {
			if (stock.getVacuna().getNombre().equals(idVacunaStock)) {
				for (Historico historico : stock.getHistoricos()) {
					if (historico.getFecha().compareTo(fecha) == 0) {
						return (new DtHistoricoStock(historico.getFecha(), historico.getCantidad(),
								historico.getDescartadas(), historico.getDisponibles(), historico.getAdministradas(),
								historico.getStock().getVacuna().getNombre(),
								historico.getStock().getVacunatorio().getNombre()));
					}
				}
			}
		}
		throw new StockVacunaVacunatorioInexistente(String.format(
				"No se encontro Stock de la Vacuna %s en el Vacunatorio %s", idVacunaStock, idVacunatorioStock));
	}

	@Override
	public List<DtHistoricoStock> listarHistoricos(String idVacunatorioStock, String idVacunaStock)
			throws VacunatorioNoCargadoException, VacunaInexistente {
		List<DtHistoricoStock> historicos = new ArrayList<DtHistoricoStock>();
		Vacunatorio vacunatorio = em.find(Vacunatorio.class, idVacunatorioStock);
		if (vacunatorio == null) {
			throw new VacunatorioNoCargadoException("No existe el vacunatorio con ID " + idVacunatorioStock);
		}
		if (em.find(Vacuna.class, idVacunaStock) == null) {
			throw new VacunaInexistente("No existe la vacuna con ID " + idVacunaStock);
		}
		for (Stock stock : vacunatorio.getStock()) {
			if (stock.getVacuna().getNombre().equals(idVacunaStock)) {
				for (Historico historico : stock.getHistoricos()) {
					historicos.add(new DtHistoricoStock(historico.getFecha(), historico.getCantidad(),
							historico.getDescartadas(), historico.getDisponibles(), historico.getAdministradas(),
							historico.getStock().getVacuna().getNombre(),
							historico.getStock().getVacunatorio().getNombre()));
				}
			}
		}
		return historicos;
	}

}
