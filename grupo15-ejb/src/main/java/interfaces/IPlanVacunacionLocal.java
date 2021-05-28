package interfaces;

import java.util.ArrayList;

import javax.ejb.Local;

import datatypes.DtPlanFecha;
import datatypes.DtPlanVacunacion;
import exceptions.AccionInvalida;
import exceptions.EnfermedadInexistente;
import exceptions.PlanVacunacionInexistente;
import exceptions.PlanVacunacionRepetido;

@Local
public interface IPlanVacunacionLocal {
	
	public void agregarPlanVacunacion(int id, String nombre, String descripcion) throws PlanVacunacionRepetido;
	
	public ArrayList<DtPlanVacunacion> listarPlanesVacunacion() throws PlanVacunacionInexistente;
	
	public DtPlanVacunacion obtenerPlanVacunacion(int id) throws PlanVacunacionInexistente;
	
	public void agregarEnfermedadPlan(int id, String nombre) throws PlanVacunacionInexistente, EnfermedadInexistente, AccionInvalida;
	
	public void eliminarPlanVacunacion(int id) throws PlanVacunacionInexistente, AccionInvalida ;
	
	public ArrayList<DtPlanFecha> listarAgendasAbiertas() throws PlanVacunacionInexistente;
	
	public ArrayList<DtPlanFecha> listarAgendasProximas() throws PlanVacunacionInexistente;
}
