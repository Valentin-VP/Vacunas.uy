package interfaces;

import java.util.Date;
import java.util.List;

import javax.ejb.Local;

import datatypes.DtHistoricoStock;
import entities.Stock;
import exceptions.StockVacunaVacunatorioInexistente;
import exceptions.VacunaInexistente;
import exceptions.VacunatorioNoCargadoException;

@Local
public interface IHistoricoDaoLocal {
	public void agregarHistorico(Date fecha, Integer cantidad, Integer descartadas, Integer disponibles,
			Integer administradas, String idVacunatorioStock, String idVacunaStock)
			throws VacunatorioNoCargadoException, VacunaInexistente, StockVacunaVacunatorioInexistente;

	// Revisar luego, probablemente la fecha y stock asociados no deben poder
	// cambiarse
	public void modificarHistorico(Date fecha, Integer cantidad, Integer descartadas, Integer disponibles,
			Integer administradas, String idVacunatorioStock, String idVacunaStock)
			throws VacunatorioNoCargadoException, VacunaInexistente, StockVacunaVacunatorioInexistente;

	public void eliminarHistorico(Date fecha, String idVacunatorioStock, String idVacunaStock)
			throws VacunatorioNoCargadoException, VacunaInexistente, StockVacunaVacunatorioInexistente;

	public DtHistoricoStock obtenerHistorico(Date fecha, String idVacunatorioStock, String idVacunaStock)
			throws VacunatorioNoCargadoException, VacunaInexistente, StockVacunaVacunatorioInexistente;

	public List<DtHistoricoStock> listarHistoricos(String idVacunatorioStock, String idVacunaStock)
			throws VacunatorioNoCargadoException, VacunaInexistente;
}
