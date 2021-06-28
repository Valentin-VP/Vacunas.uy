package interfaces;


import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;

import javax.ejb.Remote;
import datatypes.DtVacuna;
import entities.Enfermedad;
import entities.Laboratorio;
import exceptions.AccionInvalida;
import exceptions.EnfermedadInexistente;
import exceptions.LaboratorioInexistente;
import exceptions.VacunaInexistente;
import exceptions.VacunaRepetida;

@Remote
public interface IControladorVacunaRemote {

public void agregarVacuna(String nombre, int cantDosis, int tiempoEntreDosis, int expira, String laboratorio, String enfermedad) throws VacunaRepetida , LaboratorioInexistente, EnfermedadInexistente;
	
	public ArrayList<DtVacuna> listarVacunas()throws VacunaInexistente;
	
	public DtVacuna obtenerVacuna(String nombre)throws VacunaInexistente;
	
	public void modificarVacuna(String nombre, int cantDosis, int expira, int tiempoEntreDosis, String laboratorio, String enfermedad) throws VacunaInexistente, LaboratorioInexistente, EnfermedadInexistente, AccionInvalida;
	
	public void eliminarVacuna(String nombre) throws VacunaInexistente, AccionInvalida;
}
