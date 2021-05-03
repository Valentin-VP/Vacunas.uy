package interfaces;

import java.util.ArrayList;

import javax.ejb.Remote;

import datatypes.DtPlanVacunacion;
import exceptions.PlanVacunacionInexistente;
import exceptions.PlanVacunacionRepetido;

@Remote
public interface IPlanVacunacionRemote {

	public interface IPlanVacunacionLocal {
		
		public void agregarPlanVacunacion(int id, String nombre, String descripcion) throws PlanVacunacionRepetido;
		
		public ArrayList<DtPlanVacunacion> listarPlanesVacunacion() throws PlanVacunacionInexistente;
		
		public DtPlanVacunacion obtenerVacuna(int id) throws PlanVacunacionInexistente;
	}
}
