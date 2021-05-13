package interfaces;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;

import datatypes.DtEtapa;
import exceptions.EtapaInexistente;
import exceptions.EtapaRepetida;
import exceptions.PlanVacunacionInexistente;

public interface IEtapaLocal {

	public void agregarEtapa(int idEtapa, LocalDate fIni, LocalDate fFin, String cond, int idPlan, String nombreVacuna) throws EtapaRepetida, PlanVacunacionInexistente;
	
	public ArrayList<DtEtapa> listarEtapas() throws EtapaInexistente;
	
	public DtEtapa obtenerEtapa(int id) throws EtapaInexistente;
}
