package interfaces;

import java.time.LocalDateTime;
import java.util.ArrayList;

import javax.ejb.Local;

import datatypes.DtReserva;
import datatypes.EstadoReserva;
import exceptions.PuestoNoCargadoException;
import exceptions.ReservaInexistente;
import exceptions.ReservaRepetida;
import exceptions.UsuarioExistente;

@Local
public interface IReservaDAOLocal {
	public void agregarReserva(int usuario, int etapa, String puesto, LocalDateTime fecha, EstadoReserva estado) throws ReservaRepetida, PuestoNoCargadoException, ReservaInexistente, UsuarioExistente;
	
	public void modificarReserva(int usuario, int etapa, String puesto, LocalDateTime fecha, EstadoReserva estado) throws ReservaInexistente, PuestoNoCargadoException, UsuarioExistente;
	
	 public void eliminarReserva(int usuario, int etapa) throws ReservaInexistente, UsuarioExistente;
	
	 public DtReserva obtenerReserva(int usuario, int etapa) throws ReservaInexistente, UsuarioExistente ;
	
	/*public DtReserva obtenerReserva(String user);

	public DtReserva obtenerReserva(Date fecha);
	
	public DtReserva obtenerReserva(EstadoReserva estado);*/
	
	 public ArrayList<DtReserva> listarReservasGenerales()  throws ReservaInexistente;
}

