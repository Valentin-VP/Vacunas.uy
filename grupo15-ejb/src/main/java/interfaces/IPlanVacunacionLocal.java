package interfaces;

import java.util.ArrayList;

import javax.ejb.Local;

import datatypes.DtPlanVacunacion;
import exceptions.EnfermedadInexistente;
import exceptions.PlanVacunacionInexistente;
import exceptions.PlanVacunacionRepetido;

@Local
public interface IPlanVacunacionLocal {
	
	public void agregarPlanVacunacion(int id, String nombre, String descripcion) throws PlanVacunacionRepetido;
	
	public ArrayList<DtPlanVacunacion> listarPlanesVacunacion() throws PlanVacunacionInexistente;
	
	public DtPlanVacunacion obtenerVacuna(int id) throws PlanVacunacionInexistente;
	
	public void agregarEnfermedadPlan(int id, String nombre) throws PlanVacunacionInexistente, EnfermedadInexistente;
}
