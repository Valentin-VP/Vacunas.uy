package interfaces;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;

import javax.ejb.Remote;

import datatypes.DtEnfermedad;
import datatypes.DtEtapa;
import datatypes.DtPlanVacunacion;
import datatypes.DtReservaCompleto;
import datatypes.DtVacunatorio;
import exceptions.CupoInexistente;
import exceptions.EnfermedadInexistente;
import exceptions.EtapaInexistente;
import exceptions.PlanVacunacionInexistente;
import exceptions.ReservaInexistente;
import exceptions.UsuarioInexistente;
import exceptions.VacunatorioNoCargadoException;
import exceptions.VacunatoriosNoCargadosException;

@Remote
public interface IReservaDAORemote   {
	public ArrayList<DtEnfermedad> listarEnfermedades() throws EnfermedadInexistente;
	public ArrayList<DtPlanVacunacion> seleccionarEnfermedad(String enfermedad) throws PlanVacunacionInexistente, EnfermedadInexistente;
	public ArrayList<DtVacunatorio> listarVacunatorios() throws VacunatoriosNoCargadosException;
	public ArrayList<DtEtapa> seleccionarPlanVacunacion(int idPlan, int idUser) throws PlanVacunacionInexistente, EtapaInexistente, UsuarioInexistente;
	public ArrayList<String> seleccionarFecha(LocalDate fecha, String idVacunatorio, int idPlan, int idCiudadano) throws VacunatorioNoCargadoException, PlanVacunacionInexistente, UsuarioInexistente, EtapaInexistente;
	public void confirmarReserva(int idCiudadano, String idEnfermedad, int idPlan, String idVacunatorio,
			LocalDate fecha, LocalTime hora)
			throws UsuarioInexistente, PlanVacunacionInexistente, VacunatorioNoCargadoException, EnfermedadInexistente, CupoInexistente, EtapaInexistente;
	public DtReservaCompleto obtenerReserva(int ciudadano, int plan, int etapa, LocalDateTime fecha) throws ReservaInexistente, UsuarioInexistente, EtapaInexistente;
	public ArrayList<DtReservaCompleto> listarReservasCiudadano(int ciudadano) throws ReservaInexistente, UsuarioInexistente;
	public void eliminarReserva(int ciudadano, LocalDateTime fecha, String enfermedad) throws ReservaInexistente, UsuarioInexistente, EnfermedadInexistente ;
}
