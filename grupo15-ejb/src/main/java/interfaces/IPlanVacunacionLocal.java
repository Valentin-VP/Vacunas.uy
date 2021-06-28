package interfaces;

import java.util.ArrayList;

import javax.ejb.Local;

import datatypes.DtPlanFecha;
import datatypes.DtPlanVacunacion;
import datatypes.DtVacuna;
import exceptions.AccionInvalida;
import exceptions.EnfermedadInexistente;
import exceptions.PlanVacunacionInexistente;

@Local
public interface IPlanVacunacionLocal {
	
	public void agregarPlanVacunacion(String nombre, String descripcion, String idEnfermedad) throws EnfermedadInexistente;
	
	public ArrayList<DtPlanVacunacion> listarPlanesVacunacion() throws PlanVacunacionInexistente;
	
	public DtPlanVacunacion obtenerPlanVacunacion(int id) throws PlanVacunacionInexistente;
	
	//public void agregarEnfermedadPlan(int id, String nombre) throws PlanVacunacionInexistente, EnfermedadInexistente, AccionInvalida;
	
	public void eliminarPlanVacunacion(int id) throws PlanVacunacionInexistente, AccionInvalida ;
	
	public ArrayList<DtPlanFecha> listarAgendasAbiertas() throws PlanVacunacionInexistente;
	
	public ArrayList<DtPlanFecha> listarAgendasProximas() throws PlanVacunacionInexistente;
	
	public void modificarPlan(int id, String nombre, String descripcion) throws PlanVacunacionInexistente;
	
	public ArrayList<DtVacuna> obtenerVacunasDeEnfermedadDePlan(int id) throws AccionInvalida, PlanVacunacionInexistente;
}