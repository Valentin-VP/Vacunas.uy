package interfaces;

import java.util.ArrayList;
import javax.ejb.Local;
import datatypes.DtVacuna;
import exceptions.EnfermedadInexistente;
import exceptions.LaboratorioInexistente;
import exceptions.VacunaInexistente;
import exceptions.VacunaRepetida;

@Local
public interface IControladorVacunaLocal {

	public void agregarVacuna(String nombre, Integer cantDosis, int dia, int mes, int anio, String laboratorio, String enfermedad)throws VacunaRepetida, LaboratorioInexistente, EnfermedadInexistente;
	
	public ArrayList<DtVacuna> listarVacunas()throws VacunaInexistente;
	
	public DtVacuna obtenerVacuna(String nombre)throws VacunaInexistente;
}