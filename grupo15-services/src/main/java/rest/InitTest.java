package rest;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;

import javax.annotation.security.PermitAll;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import datatypes.DtConstancia;
import datatypes.DtDireccion;
import datatypes.ErrorInfo;
import datatypes.Sexo;
import exceptions.AccionInvalida;
import exceptions.CertificadoRepetido;
import exceptions.ConstanciaInexistente;
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
import exceptions.VacunaInexistente;
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
@Produces(MediaType.APPLICATION_JSON)
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
	@PermitAll
	public Response alta() {
		try {
			es.agregarEnfermedad("virus1");
			es.agregarEnfermedad("virus2");
			vact.agregarVacunatorio("vact1", "Nest1", new DtDireccion("Av. Italia 1111", "Brooks", "Melbourne"), 1555897235, 1.0f, 1.0f);
			vact.agregarReglasCupos("vact1", "1", 15,  LocalTime.of(0, 0, 0),  LocalTime.of(23, 59, 59));
			//vact.agregarVacunatorio("vact2", "Nest2", new DtDireccion("Av. Italia 1112", "Brooks", "Melbourne"), 1555897235, 1.0f, 1.0f);
			//vact.agregarReglasCupos("vact2", "2", 20,  LocalTime.of(10, 0, 0),  LocalTime.of(22, 0, 0));
			//vact.agregarVacunatorio("vact3", "Nest3", new DtDireccion("Av. Italia 1113", "Brooks", "Melbourne"), 1555897235, 1.0f, 1.0f);
			//vact.agregarReglasCupos("vact3", "3", 30,  LocalTime.of(10, 0, 0),  LocalTime.of(20, 0, 0));
			
			uc.agregarUsuarioVacunador(11111111, "Vacunador", "DeTest Uno", LocalDate.now(), "v@1", new DtDireccion("Av. Vcd 1001", "Brooks", "Melbourne"), Sexo.Otro);
			uc.agregarUsuarioVacunador(11111112, "Vacunador", "DeTest Dos", LocalDate.now(), "v@2", new DtDireccion("Av. Vcd 1002", "Brooks", "Melbourne"), Sexo.Otro);
			uc.agregarUsuarioVacunador(11111113, "Vacunador", "DeTest Tres", LocalDate.now(), "v@3", new DtDireccion("Av. Vcd 1003", "Brooks", "Melbourne"), Sexo.Otro);
			uc.agregarUsuarioVacunador(11111114, "Vacunador", "DeTest Cuatro", LocalDate.now(), "v@4", new DtDireccion("Av. Vcd 1004", "Brooks", "Melbourne"), Sexo.Otro);
			uc.agregarUsuarioVacunador(45946590, "Rodrigo", "Castro", LocalDate.now(), "rodrigo@castro", new DtDireccion("Av. Vcd RRRR", "Brooks", "Melbourne"), Sexo.Otro);
			uc.agregarUsuarioVacunador(54657902, "Nicolás", "Méndez", LocalDate.now(), "nicolas@mendez", new DtDireccion("Av. Vcd NNNN", "Brooks", "Melbourne"), Sexo.Otro);
			uc.agregarUsuarioCiudadano(21111111, "Ciudadano", "DeTest Uno", LocalDate.of(2000, 1, 1), "c@1", new DtDireccion("Av. Cdd 2001", "Brooks", "Melbourne"), Sexo.Otro, "Sector123456789" , false);
			uc.agregarUsuarioCiudadano(21111112, "Ciudadano", "DeTest Dos", LocalDate.of(1960, 1, 1), "c@2", new DtDireccion("Av. Cdd 2002", "Brooks", "Melbourne"), Sexo.Otro, "Sector123456789" , false);
			uc.agregarUsuarioCiudadano(21111113, "Ciudadano", "DeTest Tres", LocalDate.of(1900, 1, 1), "c@3", new DtDireccion("Av. Cdd 2003", "Brooks", "Melbourne"), Sexo.Otro, "Sector123456789" , false);
			uc.agregarUsuarioCiudadano(21111114, "Ciudadano", "DeTest Cuatro", LocalDate.of(1995, 1, 1), "c@4", new DtDireccion("Av. Cdd 2004", "Brooks", "Melbourne"), Sexo.Otro, "Sector123456789" , false);
			uc.agregarUsuarioCiudadano(54657902, "Nicolás", "Méndez", LocalDate.of(1995, 1, 1), "nicolas@mendez", new DtDireccion("Av. Cdd NNNN", "Brooks", "Melbourne"), Sexo.Otro, "Sector123456789" , false);
			uc.agregarUsuarioCiudadano(49457795, "Valentin", "Vasconcellos", LocalDate.of(1995, 1, 1), "valentin@vasconcellos", new DtDireccion("Av. Cdd VVVV", "Brooks", "Melbourne"), Sexo.Otro, "Sector123456789" , false);
			lab.agregarLaboratorio("lab1");
			lab.agregarLaboratorio("lab2");
			trs.agregarTransportista(1);
			trs.agregarTransportista(2);
			vch.agregarVacuna("vacuna1Virus1", 1, 60, 1999, "lab1", "virus1");
			vch.agregarVacuna("vacuna2Virus2", 3, 60, 1999, "lab1", "virus2");
			
			plan.agregarPlanVacunacion(1, "plan1virus1", "descripcion plan1virus1");
			plan.agregarPlanVacunacion(2, "plan2virus1", "descripcion plan2virus1");
			plan.agregarPlanVacunacion(3, "plan3virus2", "descripcion plan3virus2");
			plan.agregarEnfermedadPlan(1, "virus1");
			plan.agregarEnfermedadPlan(2, "virus1");
			plan.agregarEnfermedadPlan(3, "virus2");
			etapa.agregarEtapa(1, LocalDate.of(2021, 1, 1), LocalDate.of(2022, 1, 1), "18|50", 1, "vacuna1Virus1");
			etapa.agregarEtapa(2, LocalDate.of(2021, 1, 1), LocalDate.of(2022, 1, 1), "51|80", 1, "vacuna1Virus1");
			etapa.agregarEtapa(3, LocalDate.of(2021, 1, 1), LocalDate.of(2022, 1, 1), "18|50", 2, "vacuna1Virus1");
			etapa.agregarEtapa(4, LocalDate.of(2021, 1, 1), LocalDate.of(2022, 1, 1), "18|500", 3, "vacuna2Virus2");
			
			//agd.agregarAgenda("vact1", LocalDate.now());
			
			cert.agregarCertificadoVacunacion(21111111, new ArrayList<DtConstancia>());
			cert.agregarCertificadoVacunacion(21111112, new ArrayList<DtConstancia>());
			pst.agregarPuesto("puesto1vact1", "vact1");
			pst.agregarPuesto("puesto2vact1", "vact1");
			pst.agregarPuesto("puesto3vact1", "vact1");
			//pst.agregarPuesto("puesto1vact2", "vact2");
			//pst.agregarPuesto("puesto2vact2", "vact2");
			//pst.agregarPuesto("puesto1vact3", "vact3");
			
			/*vc.asignarVacunadorAVacunatorio(11111111, "vact2", LocalDate.now());
			vc.asignarVacunadorAVacunatorio(11111111, "vact2", LocalDate.now().plusDays(1));
			vc.asignarVacunadorAVacunatorio(11111112, "vact2", LocalDate.now());
			vc.asignarVacunadorAVacunatorio(11111113, "vact3", LocalDate.now());
			vc.asignarVacunadorAVacunatorio(11111112, "vact2", LocalDate.now().plusDays(1));
			vc.asignarVacunadorAVacunatorio(11111114, "vact3", LocalDate.now().plusDays(1));*/
			return Response.ok().build();
		} catch (EnfermedadRepetida | VacunatorioCargadoException | UsuarioExistente | LaboratorioRepetido | TransportistaRepetido | VacunaRepetida | LaboratorioInexistente | EnfermedadInexistente | PlanVacunacionRepetido | EtapaRepetida | PlanVacunacionInexistente | VacunatorioNoCargadoException | ReglasCuposCargadoException | CertificadoRepetido | ConstanciaInexistente | PuestoCargadoException | AccionInvalida | VacunaInexistente e) {
			// TODO Auto-generated catch block
			return Response.serverError().entity(new ErrorInfo(200, e.getMessage())).status(200).build();
		}
		
	}

}
