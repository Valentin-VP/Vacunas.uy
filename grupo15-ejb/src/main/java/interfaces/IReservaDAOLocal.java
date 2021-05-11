package interfaces;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;

import javax.ejb.Local;

import datatypes.DtEnfermedad;
import datatypes.DtEtapa;
import datatypes.DtPlanVacunacion;
import datatypes.DtVacunatorio;
import exceptions.CupoInexistente;
import exceptions.EnfermedadInexistente;
import exceptions.EtapaInexistente;
import exceptions.PlanVacunacionInexistente;
import exceptions.UsuarioInexistente;
import exceptions.VacunatorioNoCargadoException;
import exceptions.VacunatoriosNoCargadosException;

@Local
public interface IReservaDAOLocal {
	public ArrayList<DtEnfermedad> listarEnfermedades() throws EnfermedadInexistente;
	public ArrayList<DtPlanVacunacion> seleccionarEnfermedad(String enfermedad) throws PlanVacunacionInexistente, EnfermedadInexistente;
	public ArrayList<DtVacunatorio> listarVacunatorios() throws VacunatoriosNoCargadosException;
	public ArrayList<DtEtapa> seleccionarPlanVacunacion(int idPlan, int idUser) throws PlanVacunacionInexistente, EtapaInexistente;
	public ArrayList<LocalTime> seleccionarFecha(LocalDate fecha, String idVacunatorio) throws VacunatorioNoCargadoException;
	public void confirmarReserva(int idCiudadano, String idEnfermedad, int idPlan, String idVacunatorio,
			LocalDate fecha, LocalTime hora)
			throws UsuarioInexistente, PlanVacunacionInexistente, VacunatorioNoCargadoException, EnfermedadInexistente, CupoInexistente;
}

