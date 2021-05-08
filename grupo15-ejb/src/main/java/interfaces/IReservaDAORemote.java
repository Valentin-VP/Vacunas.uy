package interfaces;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;

import javax.ejb.Remote;

import datatypes.DtEnfermedad;
import datatypes.DtEtapa;
import datatypes.DtPlanVacunacion;
import datatypes.DtVacunatorio;
import exceptions.CupoInexistente;
import exceptions.EnfermedadInexistente;
import exceptions.EtapaInexistente;
import exceptions.PlanVacunacionInexistente;
import exceptions.UsuarioExistente;
import exceptions.VacunatorioNoCargadoException;
import exceptions.VacunatoriosNoCargadosException;

@Remote
public interface IReservaDAORemote   {
	public ArrayList<DtEnfermedad> listarEnfermedades() throws EnfermedadInexistente;
	public ArrayList<DtPlanVacunacion> seleccionarEnfermedad(String enfermedad) throws PlanVacunacionInexistente;
	public ArrayList<DtVacunatorio> listarVacunatorios() throws VacunatoriosNoCargadosException;
	public ArrayList<DtEtapa> seleccionarPlanVacunacion(int idPlan) throws PlanVacunacionInexistente, EtapaInexistente;
	public ArrayList<LocalTime> seleccionarFecha(LocalDate fecha, String idVacunatorio) throws VacunatorioNoCargadoException;
	public void confirmarReserva(int idCiudadano, String idEnfermedad, int idPlan, String idVacunatorio,
			LocalDate fecha, LocalTime hora)
			throws UsuarioExistente, PlanVacunacionInexistente, VacunatorioNoCargadoException, EnfermedadInexistente, CupoInexistente;
}
