package rest;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Date;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.ws.rs.POST;
import javax.ws.rs.Path;

import datatypes.DtConstancia;
import datatypes.DtDireccion;
import datatypes.Sexo;
import exceptions.AgendaRepetida;
import exceptions.CertificadoRepetido;
import exceptions.ConstanciaInexistente;
import exceptions.CupoInexistente;
import exceptions.EnfermedadInexistente;
import exceptions.EnfermedadRepetida;
import exceptions.EtapaRepetida;
import exceptions.FechaIncorrecta;
import exceptions.LaboratorioInexistente;
import exceptions.LaboratorioRepetido;
import exceptions.PlanVacunacionInexistente;
import exceptions.PlanVacunacionRepetido;
import exceptions.PuestoCargadoException;
import exceptions.ReglasCuposCargadoException;
import exceptions.SinPuestosLibres;
import exceptions.TransportistaRepetido;
import exceptions.UsuarioExistente;
import exceptions.UsuarioInexistente;
import exceptions.VacunaRepetida;
import exceptions.VacunatorioCargadoException;
import exceptions.VacunatorioNoCargadoException;
import interfaces.IAgendaDAOLocal;
import interfaces.ICertificadoVacunacionDAOLocal;
import interfaces.IControladorPuestoLocal;
import interfaces.IControladorReglasCuposLocal;
import interfaces.IControladorVacunaLocal;
import interfaces.IControladorVacunadorLocal;
import interfaces.IControladorVacunatorioLocal;
import interfaces.IEnfermedadLocal;
import interfaces.IEtapaRemote;
import interfaces.ILaboratorioLocal;
import interfaces.IPlanVacunacionLocal;
import interfaces.ITransportistaDaoLocal;
import interfaces.IUsuarioLocal;

@RequestScoped
@Path("/test")
public class InitTest {
	@EJB
	IEnfermedadLocal es;
	@EJB
	ILaboratorioLocal lab;
	
	@EJB
	IControladorVacunatorioLocal vact;
	@EJB
	IControladorReglasCuposLocal rgl;
	@EJB
	IAgendaDAOLocal agd;
	@EJB
	IControladorPuestoLocal pst;
	
	@EJB
	IControladorVacunaLocal vch;
	
	@EJB
	IPlanVacunacionLocal plan;
	@EJB
	IEtapaRemote etapa;
	
	
	@EJB
	IUsuarioLocal uc;
	@EJB
	ICertificadoVacunacionDAOLocal cert;
	@EJB
	IControladorVacunadorLocal vc;
	
	
	
	@EJB
	ITransportistaDaoLocal trs;
	
	
	public InitTest() {
		// TODO Auto-generated constructor stub
	}
	
	@POST
	@Path("/alta")
	public void alta() {
		try {
			es.agregarEnfermedad("virus1");
			vact.agregarVacunatorio("vact1", "Nest1", new DtDireccion("Av. Italia 1111", "Brooks", "Melbourne"), 1555897235, 1.0f, 1.0f);
			vact.agregarReglasCupos("vact1", "1", 15,  LocalTime.of(10, 0, 0),  LocalTime.of(18, 0, 0));
			vact.agregarVacunatorio("vact2", "Nest2", new DtDireccion("Av. Italia 1112", "Brooks", "Melbourne"), 1555897235, 1.0f, 1.0f);
			vact.agregarReglasCupos("vact2", "2", 20,  LocalTime.of(10, 0, 0),  LocalTime.of(18, 0, 0));
			
			uc.agregarUsuarioVacunador(11111111, "Vacunador", "DeTest Uno", Date.from(Instant.now()), "v@1", new DtDireccion("Av. Vcd 1001", "Brooks", "Melbourne"), Sexo.Otro);
			uc.agregarUsuarioVacunador(11111112, "Vacunador", "DeTest Dos", Date.from(Instant.now()), "v@2", new DtDireccion("Av. Vcd 1002", "Brooks", "Melbourne"), Sexo.Otro);
			uc.agregarUsuarioCiudadano(21111111, "Ciudadano", "DeTest Uno", Date.from(Instant.now()), "c@1", new DtDireccion("Av. Cdd 2001", "Brooks", "Melbourne"), Sexo.Otro, "Sector123456789" , false);
			uc.agregarUsuarioCiudadano(21111112, "Ciudadano", "DeTest Dos", Date.from(Instant.now()), "c@2", new DtDireccion("Av. Cdd 2002", "Brooks", "Melbourne"), Sexo.Otro, "Sector123456789" , false);
			lab.agregarLaboratorio("lab1");
			lab.agregarLaboratorio("lab2");
			trs.agregarTransportista(1);
			trs.agregarTransportista(2);
			vch.agregarVacuna("vacuna1paraVirus1", 1, 1, 1, 1999, "lab1", "virus1");
			
			plan.agregarPlanVacunacion(1, "plan1virus1", "descripcion plan1virus1");
			plan.agregarPlanVacunacion(2, "plan2virus1", "descripcion plan2virus1");
			plan.agregarEnfermedadPlan(1, "virus1");
			plan.agregarEnfermedadPlan(2, "virus1");
			etapa.agregarEtapa(1, Date.from(Instant.now()), Date.from(Instant.now()), "18|50", 1, "vacuna1paraVirus1");
			etapa.agregarEtapa(2, Date.from(Instant.now()), Date.from(Instant.now()), "18|50", 1, "vacuna1paraVirus1");
			
			agd.agregarAgenda("vact1", LocalDate.now());
			
			cert.agregarCertificadoVacunacion(21111111, new ArrayList<DtConstancia>());
			cert.agregarCertificadoVacunacion(21111112, new ArrayList<DtConstancia>());
			pst.agregarPuesto("puesto1vact1", "vact1");
			
			vc.asignarVacunadorAVacunatorio(11111111, "vact1", Date.from(Instant.now()));
			
		} catch (EnfermedadRepetida | VacunatorioCargadoException | UsuarioExistente | LaboratorioRepetido | TransportistaRepetido | VacunaRepetida | LaboratorioInexistente | EnfermedadInexistente | PlanVacunacionRepetido | EtapaRepetida | PlanVacunacionInexistente | VacunatorioNoCargadoException | ReglasCuposCargadoException | AgendaRepetida | CupoInexistente | CertificadoRepetido | ConstanciaInexistente | PuestoCargadoException | UsuarioInexistente | SinPuestosLibres | FechaIncorrecta e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
