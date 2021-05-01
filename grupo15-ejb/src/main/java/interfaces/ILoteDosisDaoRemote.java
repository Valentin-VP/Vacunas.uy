package interfaces;

import java.util.List;

import javax.ejb.Remote;

import datatypes.DtLoteDosis;
import exceptions.LoteInexistente;
import exceptions.LoteRepetido;

@Remote
public interface ILoteDosisDaoRemote {

	public void agregarLoteDosis(Integer idLote, Integer cantidadDosis, float temperatura, String descripcion) throws LoteRepetido;
	
	public DtLoteDosis obtenerLoteDosis(Integer idLote) throws LoteInexistente;
	
	public List<DtLoteDosis> listarLotesDosis();
	
}