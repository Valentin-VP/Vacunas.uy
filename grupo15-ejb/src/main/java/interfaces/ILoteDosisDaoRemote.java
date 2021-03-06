package interfaces;

import java.util.List;

import javax.ejb.Remote;

import datatypes.DtLoteDosis;
import datatypes.EstadoLote;
import entities.Transportista;
import exceptions.LoteInexistente;
import exceptions.LoteRepetido;
import exceptions.TransportistaInexistente;
import exceptions.VacunaInexistente;
import exceptions.VacunatorioNoCargadoException;

@Remote
public interface ILoteDosisDaoRemote {

	public void agregarLoteDosis(Integer idLote, String idVacunatorio, String idVacuna, Integer cantidadTotal) throws LoteRepetido, VacunatorioNoCargadoException, VacunaInexistente;
	
	public DtLoteDosis obtenerLoteDosis(Integer idLote, String idVacunatorio, String idVacuna) throws LoteInexistente;
	
	public List<DtLoteDosis> listarLotesDosis();
	
	public void setTransportistaToLoteDosis(Integer idTransportista, Integer idLote, String idVacunatorio, String idVacuna) throws TransportistaInexistente, VacunatorioNoCargadoException, VacunaInexistente;
	
	public Integer getTransportistaIdFromLoteDosis(Integer idLote, String idVacunatorio, String idVacuna) throws VacunatorioNoCargadoException, VacunaInexistente;
	
	public void modificarLoteDosis(Integer idLote, String idVacunatorio, String idVacuna, Integer cantidadTotal, Integer cantidadEntregada, Integer cantidadDescartada,
			String estadoLote, float temperatura, Integer transportista) throws LoteInexistente, TransportistaInexistente, VacunatorioNoCargadoException, VacunaInexistente;
	
	public void eliminarLoteDosis(Integer idLote, String idVacunatorio, String idVacuna) throws LoteInexistente, VacunatorioNoCargadoException, VacunaInexistente;

	public List<DtLoteDosis> listarLotesDosisVacunaVacunatorio(String idVacunatorio, String idVacuna);
}