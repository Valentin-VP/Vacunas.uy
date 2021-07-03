package interfaces;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;

import javax.ejb.Local;

import datatypes.DtEnfermedad;
import datatypes.DtEtapa;
import datatypes.DtPlanVacunacion;
import datatypes.DtReservaCompleto;
import datatypes.DtTareaNotificacion;
import datatypes.DtUsuarioExterno;
import datatypes.DtVacunatorio;
import datatypes.EstadoReserva;
import exceptions.AccionInvalida;
import exceptions.CupoInexistente;
import exceptions.EnfermedadInexistente;
import exceptions.EtapaInexistente;
import exceptions.PlanVacunacionInexistente;
import exceptions.ReservaInexistente;
import exceptions.UsuarioInexistente;
import exceptions.VacunatorioNoCargadoException;
import exceptions.VacunatoriosNoCargadosException;

@Local
public interface IReservaDAOLocal {
	public ArrayList<DtPlanVacunacion> seleccionarEnfermedad(String enfermedad) throws PlanVacunacionInexistente, EnfermedadInexistente;
	public ArrayList<DtEtapa> seleccionarPlanVacunacion(int idPlan, int idUser, DtUsuarioExterno datosExternosUsuario) throws PlanVacunacionInexistente, EtapaInexistente, UsuarioInexistente;
	public ArrayList<String> seleccionarFecha(LocalDate fecha, String idVacunatorio, int idPlan, int idCiudadano, DtUsuarioExterno datosExternosUsuario) throws VacunatorioNoCargadoException, PlanVacunacionInexistente, UsuarioInexistente, EtapaInexistente, CupoInexistente;
	public ArrayList<DtTareaNotificacion> confirmarReserva(int idCiudadano, String idEnfermedad, int idPlan, String idVacunatorio,
			LocalDate fecha, LocalTime hora, DtUsuarioExterno datosExternosUsuario)
			throws UsuarioInexistente, PlanVacunacionInexistente, VacunatorioNoCargadoException, EnfermedadInexistente, CupoInexistente, EtapaInexistente;
	public DtReservaCompleto obtenerReserva(int ciudadano, int plan, int etapa, LocalDateTime fecha) throws ReservaInexistente, UsuarioInexistente, EtapaInexistente;
	public ArrayList<DtReservaCompleto> listarReservasCiudadano(int ciudadano) throws ReservaInexistente, UsuarioInexistente;
	public void eliminarReserva(int ciudadano, LocalDateTime fecha, String enfermedad) throws ReservaInexistente, UsuarioInexistente, EnfermedadInexistente;
	public void cambiarEstadoReserva(int idCiudadano, LocalDateTime fecha, EstadoReserva estado) throws AccionInvalida;

	public ArrayList<DtReservaCompleto> listarReservasAEliminar(int ciudadano) throws ReservaInexistente, UsuarioInexistente;
}

