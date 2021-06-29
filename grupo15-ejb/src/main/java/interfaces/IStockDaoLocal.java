package interfaces;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.ejb.Local;

import datatypes.DtStock;
import entities.Vacuna;
import entities.Vacunatorio;
import exceptions.CantidadNula;
import exceptions.StockVacunaVacunatorioExistente;
import exceptions.StockVacunaVacunatorioInexistente;
import exceptions.VacunaInexistente;
import exceptions.VacunatorioNoCargadoException;

@Local
public interface IStockDaoLocal {

	public void agregarStock(String idVacunatorio, String idVacuna, Integer cantidad)
			throws VacunatorioNoCargadoException, VacunaInexistente, CantidadNula, StockVacunaVacunatorioExistente;

	public void modificarStock(String idVacunatorio, String idVacuna, Integer cantidad, Integer descartadas,
			Integer administradas, Integer disponibles)
			throws VacunatorioNoCargadoException, VacunaInexistente, StockVacunaVacunatorioInexistente;

	public void eliminarStock(String idVacunatorio, String idVacuna)
			throws VacunatorioNoCargadoException, VacunaInexistente, StockVacunaVacunatorioInexistente;

	public DtStock obtenerStock(String idVacunatorio, String idVacuna)
			throws VacunatorioNoCargadoException, VacunaInexistente, StockVacunaVacunatorioInexistente;

	public List<DtStock> listarStock(String idVacunatorio) throws VacunatorioNoCargadoException;
	
	public List<DtStock> getStockActual(String enfermedad, String vacuna, String vacunatorio) ;
	
	public Map<String, Map<String, String>> getHistoricoStock(String enfermedad, String vacuna, String vacunatorio, LocalDate fechaInicio, LocalDate fechaFin);
}
