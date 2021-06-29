package interfaces;

import java.time.LocalDate;
import java.time.LocalTime;

import javax.ejb.Local;

@Local
public interface IFirestoreLocal {
	
	public void iniciarConexion();
	public void nuevaNotificacion(String mobileToken, String vacunatorio, String puesto, LocalDate fecha, LocalTime hora);

}
