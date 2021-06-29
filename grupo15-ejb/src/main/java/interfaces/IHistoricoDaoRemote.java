package interfaces;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

import javax.ejb.Remote;

import datatypes.DtHistoricoStock;
import entities.Stock;
import exceptions.StockVacunaVacunatorioInexistente;
import exceptions.VacunaInexistente;
import exceptions.VacunatorioNoCargadoException;

@Remote
public interface IHistoricoDaoRemote {
	public void persistirHistorico(LocalDate fecha, Integer cantidad, Integer descartadas, Integer disponibles,
			Integer administradas, String idVacunatorioStock, String idVacunaStock)
			throws VacunatorioNoCargadoException, VacunaInexistente, StockVacunaVacunatorioInexistente ;
	
	public void agregarHistorico(LocalDate fecha, Integer cantidad, Integer descartadas, Integer disponibles,
			Integer administradas, String idVacunatorioStock, String idVacunaStock)
			throws VacunatorioNoCargadoException, VacunaInexistente, StockVacunaVacunatorioInexistente;

	// Revisar luego, probablemente la fecha y stock asociados no deben poder
	// cambiarse
	public void modificarHistorico(LocalDate fecha, Integer cantidad, Integer descartadas, Integer disponibles,
			Integer administradas, String idVacunatorioStock, String idVacunaStock)
			throws VacunatorioNoCargadoException, VacunaInexistente, StockVacunaVacunatorioInexistente;

	public void eliminarHistorico(LocalDate fecha, String idVacunatorioStock, String idVacunaStock)
			throws VacunatorioNoCargadoException, VacunaInexistente, StockVacunaVacunatorioInexistente;

	public DtHistoricoStock obtenerHistorico(LocalDate fecha, String idVacunatorioStock, String idVacunaStock)
			throws VacunatorioNoCargadoException, VacunaInexistente, StockVacunaVacunatorioInexistente;

	public List<DtHistoricoStock> listarHistoricos(String idVacunatorioStock, String idVacunaStock)
			throws VacunatorioNoCargadoException, VacunaInexistente;
}
