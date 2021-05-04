package interfaces;

import java.time.LocalDate;
import java.util.ArrayList;

import javax.ejb.Local;

import datatypes.DtConstancia;
import datatypes.DtReserva;
import exceptions.CertificadoInexistente;
import exceptions.ConstanciaInexistente;
import exceptions.ReservaInexistente;
import exceptions.UsuarioExistente;

@Local
public interface IConstanciaVacunaDAOLocal {
	public void agregarConstanciaVacuna(String vacuna, int periodoInmunidad, int dosisRecibidas, LocalDate fechaUltimaDosis, DtReserva reserva) throws UsuarioExistente, ReservaInexistente, CertificadoInexistente;
	public void modificarConstanciaVacuna(int idConst, String vacuna, int periodoInmunidad, int dosisRecibidas, LocalDate fechaUltimaDosis, DtReserva reserva) throws UsuarioExistente, ReservaInexistente, CertificadoInexistente, ConstanciaInexistente;
	public DtConstancia obtenerConstancia(int idConst) throws ConstanciaInexistente;
	public ArrayList<DtConstancia> listarConstancias() throws ConstanciaInexistente;
}
