package interfaces;

import java.util.List;

import javax.ejb.Remote;

import datatypes.DtLoteDosis;
import datatypes.EstadoLote;
import datatypes.TransportistaInexistente;
import entities.Transportista;
import exceptions.LoteInexistente;
import exceptions.LoteRepetido;

@Remote
public interface ILoteDosisDaoRemote {

	public void agregarLoteDosis(Integer idLote, Integer cantidadTotal, float temperatura) throws LoteRepetido;
	
	public DtLoteDosis obtenerLoteDosis(Integer idLote) throws LoteInexistente;
	
	public List<DtLoteDosis> listarLotesDosis();
	
	public void setTransportistaToLoteDosis(Integer idTransportista, Integer idLote) throws TransportistaInexistente;
	
	public Integer getTransportistaIdFromLoteDosis(Integer idLote);
	
	public void modificarLoteDosis(Integer idLote, Integer cantidadTotal, Integer cantidadEntregada, Integer cantidadDescartada,
			String estadoLote, float temperatura, Integer transportista) throws LoteInexistente, TransportistaInexistente;
	
	public void eliminarLoteDosis(Integer idLote) throws LoteInexistente;
}