package interfaces;

import java.time.LocalDateTime;
import java.util.ArrayList;

import javax.ejb.Local;

import datatypes.DtReserva;
import datatypes.EstadoReserva;
import exceptions.ReservaInexistente;
import exceptions.ReservaRepetida;

@Local
public interface IReservaDAOLocal {
	public void agregarReserva(int id, String user, LocalDateTime fecha, EstadoReserva estado) throws ReservaRepetida;
	
	public DtReserva obtenerReserva(int id)  throws ReservaInexistente;
	
	/*public DtReserva obtenerReserva(String user);

	public DtReserva obtenerReserva(Date fecha);
	
	public DtReserva obtenerReserva(EstadoReserva estado);*/
	
	public ArrayList<DtReserva> listarReservas()  throws ReservaInexistente;
}
