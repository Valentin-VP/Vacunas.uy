package interfaces;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;

import datatypes.DtEtapa;
import exceptions.AccionInvalida;
import exceptions.EtapaInexistente;
import exceptions.EtapaRepetida;
import exceptions.PlanVacunacionInexistente;
import exceptions.VacunaInexistente;

public interface IEtapaLocal {

	public void agregarEtapa(LocalDate fIni, LocalDate fFin, String cond, int idPlan, String nombreVacuna) throws EtapaRepetida, PlanVacunacionInexistente, VacunaInexistente, AccionInvalida;
	
	public ArrayList<DtEtapa> listarEtapas() throws EtapaInexistente;
	
	public DtEtapa obtenerEtapa(int id, int idPlan) throws EtapaInexistente;
	
	public void eliminarEtapa(int id, int idPlan) throws EtapaInexistente, AccionInvalida;
	public void modificarEtapa(int id, int plan, LocalDate fechaInicio, LocalDate fechaFin, String condicion) throws EtapaInexistente;
}
