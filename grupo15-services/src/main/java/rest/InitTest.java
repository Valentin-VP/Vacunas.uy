package rest;

import java.time.LocalDate;
import java.time.LocalTime;

import javax.annotation.security.PermitAll;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import datatypes.DtDireccion;
import datatypes.DtUsuarioExterno;
import datatypes.ErrorInfo;
import datatypes.Rol;
import datatypes.Sexo;
import datatypes.TransportistaInexistente;
import exceptions.AccionInvalida;
import exceptions.CantidadNula;
import exceptions.CertificadoInexistente;
import exceptions.CupoInexistente;
import exceptions.EnfermedadInexistente;
import exceptions.EnfermedadRepetida;
import exceptions.EtapaInexistente;
import exceptions.EtapaRepetida;
import exceptions.LaboratorioInexistente;
import exceptions.LaboratorioRepetido;
import exceptions.PlanVacunacionInexistente;
import exceptions.PuestoCargadoException;
import exceptions.ReglasCuposCargadoException;
import exceptions.ReservaInexistente;
import exceptions.StockVacunaVacunatorioExistente;
import exceptions.StockVacunaVacunatorioInexistente;
import exceptions.TransportistaRepetido;
import exceptions.UsuarioExistente;
import exceptions.UsuarioInexistente;
import exceptions.VacunaInexistente;
import exceptions.VacunaRepetida;
import exceptions.VacunatorioCargadoException;
import exceptions.VacunatorioNoCargadoException;
import interfaces.IAgendaDAOLocal;
import interfaces.ICertificadoVacunacionDAOLocal;
import interfaces.IConstanciaVacunaDAOLocal;
import interfaces.IControladorPuestoLocal;
import interfaces.IControladorReglasCuposLocal;
import interfaces.IControladorVacunaLocal;
import interfaces.IControladorVacunadorLocal;
import interfaces.IControladorVacunatorioLocal;
import interfaces.IEnfermedadLocal;
import interfaces.IEtapaRemote;
import interfaces.IHistoricoDaoLocal;
import interfaces.ILaboratorioLocal;
import interfaces.IPlanVacunacionLocal;
import interfaces.IReservaDAOLocal;
import interfaces.IStockDaoLocal;
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
	IStockDaoLocal csl;
	@EJB
	IHistoricoDaoLocal ch;
	
	
	@EJB
	IPlanVacunacionLocal plan;
	@EJB
	IEtapaRemote etapa;
	
	
	@EJB
	IUsuarioLocal uc;
	@EJB
	IReservaDAOLocal cr;
	@EJB
	ICertificadoVacunacionDAOLocal cert;
	@EJB
	IControladorVacunadorLocal vc;
	
	@EJB
	IConstanciaVacunaDAOLocal cv;
	
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
			vact.agregarVacunatorio("vact1", "Nest1", new DtDireccion("Av. Italia 1111", "Brooks", "Melbourne"), 1555897235, 1.0f, 1.0f, "http://localhost:8180");
			vact.agregarReglasCupos("vact1", "1", 15,  LocalTime.of(0, 0, 0),  LocalTime.of(23, 59, 59));
			vact.generarTokenVacunatorio("vact1");
			//vact.agregarVacunatorio("vact2", "Nest2", new DtDireccion("Av. Italia 1112", "Brooks", "Melbourne"), 1555897235, 1.0f, 1.0f);
			//vact.agregarReglasCupos("vact2", "2", 20,  LocalTime.of(10, 0, 0),  LocalTime.of(22, 0, 0));
			//vact.agregarVacunatorio("vact3", "Nest3", new DtDireccion("Av. Italia 1113", "Brooks", "Melbourne"), 1555897235, 1.0f, 1.0f);
			//vact.agregarReglasCupos("vact3", "3", 30,  LocalTime.of(10, 0, 0),  LocalTime.of(20, 0, 0));
			vact.agregarVacunatorio("terminal", "Terminal tres cruces", null, 24088601, Float.parseFloat("-34.893906"), Float.parseFloat("-56.166912"), "http://localhost:8180");				
			vact.agregarVacunatorio("1234", "Palacio legislativo", null, 12091274, Float.parseFloat("-34.892418"), Float.parseFloat("-56.186604"), "http://localhost:8180");
			vact.agregarVacunatorio("carrasco", "Aeropuerto de Carrasco", null, 26040329, Float.parseFloat("-34.837273"), Float.parseFloat("-56.016018"), "http://localhost:8180");
			vact.agregarVacunatorio("Clinicas", "Hospital de Clinicas", null, 45745, Float.parseFloat("-34.890743"), Float.parseFloat("-56.15231"), "http://localhost:8180");
									
			
			uc.agregarUsuarioVacunador(11111111, "Vacunador", "DeTest Uno", LocalDate.now(), "v@1", new DtDireccion("Av. Vcd 1001", "Brooks", "Melbourne"), Sexo.Otro);
			uc.agregarUsuarioVacunador(11111112, "Vacunador", "DeTest Dos", LocalDate.now(), "v@2", new DtDireccion("Av. Vcd 1002", "Brooks", "Melbourne"), Sexo.Otro);
			uc.agregarUsuarioVacunador(11111113, "Vacunador", "DeTest Tres", LocalDate.now(), "v@3", new DtDireccion("Av. Vcd 1003", "Brooks", "Melbourne"), Sexo.Otro);
			uc.agregarUsuarioVacunador(11111114, "Vacunador", "DeTest Cuatro", LocalDate.now(), "v@4", new DtDireccion("Av. Vcd 1004", "Brooks", "Melbourne"), Sexo.Otro);
			uc.agregarUsuarioVacunador(45946590, "Rodrigo", "Castro", LocalDate.now(), "rodrigo@castro", new DtDireccion("Av. Vcd RRRR", "Brooks", "Melbourne"), Sexo.Otro);
			uc.agregarUsuarioVacunador(54657902, "Nicolás", "Méndez", LocalDate.now(), "nicolas@mendez", new DtDireccion("Av. Vcd NNNN", "Brooks", "Melbourne"), Sexo.Otro);
			uc.agregarUsuarioVacunador(48585559, "Nohelia", "Yanibelli", LocalDate.now(), "nohelia@yanibelli", new DtDireccion("Av. Vcd YYYY", "Brooks", "Melbourne"), Sexo.Otro);
			uc.agregarUsuarioCiudadano(21111111, "Ciudadano", "DeTest Uno", LocalDate.of(2000, 1, 1), "c@1", new DtDireccion("Av. Cdd 2001", "Brooks", "Melbourne"), Sexo.Otro, "Sector123456789" , false);
			uc.agregarUsuarioCiudadano(21111112, "Ciudadano", "DeTest Dos", LocalDate.of(1960, 1, 1), "c@2", new DtDireccion("Av. Cdd 2002", "Brooks", "Melbourne"), Sexo.Otro, "Sector123456789" , false);
			uc.agregarUsuarioCiudadano(21111113, "Ciudadano", "DeTest Tres", LocalDate.of(1900, 1, 1), "c@3", new DtDireccion("Av. Cdd 2003", "Brooks", "Melbourne"), Sexo.Otro, "Sector123456789" , false);
			uc.agregarUsuarioCiudadano(21111114, "Ciudadano", "DeTest Cuatro", LocalDate.of(1995, 1, 1), "c@4", new DtDireccion("Av. Cdd 2004", "Brooks", "Melbourne"), Sexo.Otro, "Sector123456789" , false);
			uc.agregarUsuarioCiudadano(54657902, "Nicolás", "Méndez", LocalDate.of(1995, 1, 1), "nicolas@mendez", new DtDireccion("Av. Cdd NNNN", "Brooks", "Melbourne"), Sexo.Otro, "Sector123456789" , false);
			uc.agregarUsuarioCiudadano(49457795, "Valentin", "Vasconcellos", LocalDate.of(1995, 1, 1), "valentin@vasconcellos", new DtDireccion("Av. Cdd VVVV", "Brooks", "Melbourne"), Sexo.Otro, "Sector123456789" , false);
			uc.agregarUsuarioCiudadano(48585559, "Nohelia", "Yanibelli", LocalDate.of(1995, 1, 1), "nohelia@yanibelli", new DtDireccion("Av. Cdd YYYY", "Brooks", "Melbourne"), Sexo.Otro, "Sector123456789" , false);
			uc.agregarUsuarioInterno(54657902, "Nicolás", "Méndez", LocalDate.of(1995, 1, 1), "nicolas@mendez", new DtDireccion("Av. Cdd NNNN", "Brooks", "Melbourne"), Sexo.Otro, Rol.Administrador);
			lab.agregarLaboratorio("lab1");
			lab.agregarLaboratorio("lab2");
			trs.agregarTransportista(1, "http://localhost:8280");
			trs.generarTokenTransportista(1);
			trs.agregarTransportista(2, "http://localhost:8280");
			vch.agregarVacuna("vacuna1Virus1", 1, 60, 12, "lab1", "virus1");
			vch.agregarVacuna("vacuna2Virus1", 1, 60, 12, "lab2", "virus1");
			vch.agregarVacuna("vacuna3Virus1", 1, 60, 12, "lab1", "virus1");
			vch.agregarVacuna("vacuna4Virus1", 1, 60, 12, "lab2", "virus1");
			vch.agregarVacuna("vacuna1Virus2", 3, 60, 12, "lab1", "virus2");
			vch.agregarVacuna("vacuna2Virus2", 2, 60, 12, "lab2", "virus2");
			vch.agregarVacuna("vacuna3Virus2", 4, 60, 12, "lab1", "virus2");
			
			csl.agregarStock("vact1", "vacuna1Virus1", 400);
			csl.modificarStock("vact1", "vacuna1Virus1", 400, 0, 0, 400);
			ch.persistirHistorico(LocalDate.now(), 200, 0, 200, 0, "vact1", "vacuna1Virus1");
			ch.persistirHistorico(LocalDate.now().minusMonths(1), 100, 0, 100, 0, "vact1", "vacuna1Virus1");
			ch.persistirHistorico(LocalDate.now().minusMonths(3), 200, 0, 200, 0, "vact1", "vacuna1Virus1");
			
			csl.agregarStock("terminal", "vacuna1Virus1", 100);
			csl.modificarStock("terminal", "vacuna1Virus1", 100, 20, 0, 80);
			ch.persistirHistorico(LocalDate.now(), 50, 0, 50, 0, "terminal", "vacuna1Virus1");
			ch.persistirHistorico(LocalDate.now().minusMonths(2), 25, 0, 25, 0, "terminal", "vacuna1Virus1");
			ch.persistirHistorico(LocalDate.now().minusMonths(4), 25, 20, 5, 0, "terminal", "vacuna1Virus1");
			
			csl.agregarStock("vact1", "vacuna2Virus1", 100);
			csl.modificarStock("vact1", "vacuna2Virus1", 100, 10, 0, 90);
			ch.persistirHistorico(LocalDate.now(), 40, 0, 40, 0, "vact1", "vacuna2Virus1");
			ch.persistirHistorico(LocalDate.now(), 10, 0, 10, 0, "vact1", "vacuna2Virus1");
			ch.persistirHistorico(LocalDate.now().minusMonths(1), 25, 10, 15, 0, "vact1", "vacuna2Virus1");
			ch.persistirHistorico(LocalDate.now().minusMonths(2), 25, 0, 25, 0, "vact1", "vacuna2Virus1");
			
			csl.agregarStock("carrasco", "vacuna2Virus1", 500);
			csl.modificarStock("carrasco", "vacuna2Virus1", 500, 0, 0, 500);
			ch.persistirHistorico(LocalDate.now(), 50, 0, 50, 0, "carrasco", "vacuna2Virus1");
			ch.persistirHistorico(LocalDate.now().minusMonths(2), 150, 0, 150, 0, "carrasco", "vacuna2Virus1");
			ch.persistirHistorico(LocalDate.now().minusMonths(5), 200, 0, 200, 0, "carrasco", "vacuna2Virus1");
			ch.persistirHistorico(LocalDate.now().minusMonths(8), 100, 0, 100, 0, "carrasco", "vacuna2Virus1");
			
			csl.agregarStock("vact1", "vacuna3Virus1", 300);
			csl.modificarStock("vact1", "vacuna3Virus1", 300, 50, 50, 200);
			ch.persistirHistorico(LocalDate.now(), 100, 50, 25, 25, "vact1", "vacuna3Virus1");
			ch.persistirHistorico(LocalDate.now().minusMonths(1), 50, 0, 25, 25, "vact1", "vacuna3Virus1");
			ch.persistirHistorico(LocalDate.now().minusMonths(2), 100, 0, 100, 0, "vact1", "vacuna3Virus1");
			ch.persistirHistorico(LocalDate.now().minusMonths(10), 50, 0, 50, 0, "vact1", "vacuna3Virus1");
			
			csl.agregarStock("vact1", "vacuna4Virus1", 400);
			csl.modificarStock("vact1", "vacuna4Virus1", 400, 0, 0, 400);
			ch.persistirHistorico(LocalDate.now(), 300, 0, 300, 0, "vact1", "vacuna4Virus1");
			ch.persistirHistorico(LocalDate.now().minusMonths(3), 100, 0, 100, 0, "vact1", "vacuna4Virus1");
			
			csl.agregarStock("terminal", "vacuna4Virus1", 50);
			csl.modificarStock("terminal", "vacuna4Virus1", 50, 0, 50, 0);
			ch.persistirHistorico(LocalDate.now().minusMonths(3), 50, 0, 0, 50, "terminal", "vacuna4Virus1");
			
			csl.agregarStock("carrasco", "vacuna4Virus1", 100);
			csl.modificarStock("carrasco", "vacuna4Virus1", 100, 0, 0, 100);
			ch.persistirHistorico(LocalDate.now(), 90, 0, 90, 0, "carrasco", "vacuna4Virus1");
			ch.persistirHistorico(LocalDate.now().minusMonths(2), 10, 0, 10, 0, "carrasco", "vacuna4Virus1");
			
			csl.agregarStock("vact1", "vacuna1Virus2", 600);
			csl.modificarStock("vact1", "vacuna1Virus2", 600, 0, 0, 600);
			ch.persistirHistorico(LocalDate.now(), 100, 0, 100, 0, "vact1", "vacuna1Virus2");
			ch.persistirHistorico(LocalDate.now().minusMonths(1), 50, 0, 50, 0, "vact1", "vacuna1Virus2");
			ch.persistirHistorico(LocalDate.now().minusMonths(2), 100, 0, 100, 0, "vact1", "vacuna1Virus2");
			ch.persistirHistorico(LocalDate.now().minusMonths(3), 150, 0, 150, 0, "vact1", "vacuna1Virus2");
			ch.persistirHistorico(LocalDate.now().minusMonths(4), 20, 0, 20, 0, "vact1", "vacuna1Virus2");
			ch.persistirHistorico(LocalDate.now().minusMonths(5), 180, 0, 180, 0, "vact1", "vacuna1Virus2");
			
			csl.agregarStock("carrasco", "vacuna1Virus2", 400);
			csl.modificarStock("carrasco", "vacuna1Virus2", 400, 100, 100, 200);
			ch.persistirHistorico(LocalDate.now(), 50, 0, 50, 0, "carrasco", "vacuna1Virus2");
			ch.persistirHistorico(LocalDate.now().minusMonths(2), 50, 50, 0, 0, "carrasco", "vacuna1Virus2");
			ch.persistirHistorico(LocalDate.now().minusMonths(4), 100, 0, 50, 50, "carrasco", "vacuna1Virus2");
			ch.persistirHistorico(LocalDate.now().minusMonths(6), 100, 25, 50, 25, "carrasco", "vacuna1Virus2");
			ch.persistirHistorico(LocalDate.now().minusMonths(8), 50, 25, 0, 25, "carrasco", "vacuna1Virus2");
			ch.persistirHistorico(LocalDate.now().minusMonths(10), 50, 0, 50, 0, "carrasco", "vacuna1Virus2");
			
			csl.agregarStock("vact1", "vacuna2Virus2", 200);
			csl.modificarStock("vact1", "vacuna2Virus2", 200, 0, 0, 200);
			ch.persistirHistorico(LocalDate.now().minusMonths(5), 10, 0, 10, 0, "vact1", "vacuna2Virus2");
			ch.persistirHistorico(LocalDate.now().minusMonths(8), 190, 0, 190, 0, "vact1", "vacuna2Virus2");
			
			
			
			
			plan.agregarPlanVacunacion("plan1virus1", "descripcion plan1virus1", "virus1");
			plan.agregarPlanVacunacion("plan2virus1", "descripcion plan2virus1", "virus1");
			plan.agregarPlanVacunacion("plan3virus2", "descripcion plan3virus2", "virus2");
			//plan.agregarPlanVacunacion(id, nombre, descripcion);
			//plan.agregarEnfermedadPlan(8, "virus1");
			//plan.agregarEnfermedadPlan(9, "virus1");
			//plan.agregarEnfermedadPlan(10, "virus2");
			//plan.agregarEnfermedadPlan(id, nombre);
			etapa.agregarEtapa(LocalDate.of(2021, 1, 1), LocalDate.of(2022, 1, 1), "18|50|todos|si", 8, "vacuna1Virus1");
			etapa.agregarEtapa(LocalDate.of(2021, 1, 1), LocalDate.of(2022, 1, 1), "51|80|industria|si", 8, "vacuna1Virus1");
			etapa.agregarEtapa(LocalDate.of(2021, 1, 1), LocalDate.of(2022, 1, 1), "18|50|salud|si", 9, "vacuna1Virus1");
			etapa.agregarEtapa(LocalDate.of(2021, 1, 1), LocalDate.of(2022, 1, 1), "18|500|industria|no", 10, "vacuna2Virus2");
			//etapa.agregarEtapa(fIni, fFin, cond, idPlan, nombreVacuna);
			
			//agd.agregarAgenda("vact1", LocalDate.now());
			
			//cert.agregarCertificadoVacunacion(21111111, new ArrayList<DtConstancia>());
			//cert.agregarCertificadoVacunacion(21111112, new ArrayList<DtConstancia>());
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
			//cr.confirmarReserva(idCiudadano, idEnfermedad, idPlan, idVacunatorio, fecha, hora);
			cr.confirmarReserva(54657902, "virus1", 8, "vact1", LocalDate.now().plusDays(1), LocalTime.of(20, 00, 00), new DtUsuarioExterno("54657902", "", "industria", false));
			cr.confirmarReserva(54657902, "virus2", 10, "vact1", LocalDate.now().plusDays(1), LocalTime.of(22, 00, 00), new DtUsuarioExterno("54657902", "", "industria", false));
			
			cr.confirmarReserva(48585559, "virus1", 8, "vact1", LocalDate.now().plusDays(1), LocalTime.of(23, 30, 00), new DtUsuarioExterno("48585559", "", "salud", true));
			System.out.println("reserva 1");
			cr.confirmarReserva(48585559, "virus2", 10, "vact1", LocalDate.now().plusDays(330), LocalTime.of(13, 30, 00), new DtUsuarioExterno("48585559", "", "industria", false));
			System.out.println("reserva 2");
			
			cr.confirmarReserva(49457795, "virus1", 8, "vact1", LocalDate.now().plusDays(1), LocalTime.of(23, 30, 00), new DtUsuarioExterno("49457795", "", "salud", false));
			System.out.println("reserva 3");
			cr.confirmarReserva(49457795, "virus2", 10, "vact1", LocalDate.now().plusDays(330), LocalTime.of(13, 30, 00), new DtUsuarioExterno("49457795", "", "industria", false));
			System.out.println("reserva 4");
			
			//creo las constancias
			try {
				cv.agregarConstanciaVacuna("vacuna1Virus1", 4, 2, LocalDate.now().plusDays(100), 48585559, 11);
				cv.agregarConstanciaVacuna("vacuna2Virus2", 5, 2, LocalDate.now().plusDays(250), 48585559, 14);
				cv.agregarConstanciaVacuna("vacuna1Virus1", 4, 2, LocalDate.now().plusDays(100), 49457795, 11);
				cv.agregarConstanciaVacuna("vacuna2Virus2", 5, 2, LocalDate.now().plusDays(250), 49457795, 14);
				//cv.agregarConstanciaVacuna(vacuna, periodoInmunidad, dosisRecibidas, fechaUltimaDosis, idUser, idEtapa);
			} catch (ReservaInexistente e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (CertificadoInexistente e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			//
			return Response.ok().build();
		} catch (EnfermedadRepetida | VacunatorioCargadoException | UsuarioExistente | LaboratorioRepetido | TransportistaRepetido | VacunaRepetida | LaboratorioInexistente | EnfermedadInexistente | EtapaRepetida | PlanVacunacionInexistente | VacunatorioNoCargadoException | ReglasCuposCargadoException |  PuestoCargadoException | AccionInvalida | VacunaInexistente | UsuarioInexistente | CupoInexistente | EtapaInexistente | CantidadNula | StockVacunaVacunatorioExistente | StockVacunaVacunatorioInexistente | TransportistaInexistente e) {
			// TODO Auto-generated catch block
			return Response.serverError().entity(new ErrorInfo(200, e.getMessage())).status(200).build();
		}
		
	}

}
