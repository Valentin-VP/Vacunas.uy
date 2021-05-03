package interfaces;

import java.util.List;

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
public interface IStockLocal {

	public void agregarStock(String idVacunatorio, String idVacuna, Integer cantidad)
			throws VacunatorioNoCargadoException, VacunaInexistente, CantidadNula, StockVacunaVacunatorioExistente;

	public void modificarStock(String idVacunatorio, String idVacuna, Integer cantidad, Integer descartadas,
			Integer administradas, Integer disponibles)
			throws VacunatorioNoCargadoException, VacunaInexistente, StockVacunaVacunatorioInexistente;

	public void eliminarStock(String idVacunatorio, String idVacuna);

	public DtStock obtenerStock(String idVacunatorio, String idVacuna);

	public List<DtStock> listarStock(String idVacunatorio);
}
