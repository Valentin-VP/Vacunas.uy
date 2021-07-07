package init;

import static org.junit.Assert.assertArrayEquals;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.HeuristicMixedException;
import javax.transaction.HeuristicRollbackException;
import javax.transaction.NotSupportedException;
import javax.transaction.RollbackException;
import javax.transaction.SystemException;
import javax.transaction.UserTransaction;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.OperateOnDeployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.junit.InSequence;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

import controllers.ControladorReserva;
import datatypes.DtAgenda;
import datatypes.DtDireccion;
import datatypes.DtReserva;
import datatypes.DtReservaCompleto;
import datatypes.EstadoReserva;
import datatypes.Sexo;
import entities.Agenda;
import entities.Ciudadano;
import entities.Enfermedad;
import entities.Etapa;
import entities.Laboratorio;
import entities.PlanVacunacion;
import entities.Puesto;
import entities.Reserva;
import entities.Vacuna;
import entities.Vacunatorio;
import exceptions.AgendaInexistente;
import exceptions.AgendaRepetida;
import exceptions.CupoInexistente;
import exceptions.ReservaInexistente;
import exceptions.VacunatorioNoCargadoException;
import interfaces.IAgendaDAORemote;
import interfaces.IReservaDAORemote;
import persistence.PuestoID;
import persistence.ReservaID;

@RunWith(Arquillian.class)
public class AgendaTest {
	private final Logger LOGGER = Logger.getLogger(getClass().getName());
	
	@Deployment(name = "normal", order = 1)
    public static JavaArchive createNormalDeployment() {
        return ShrinkWrap.create(JavaArchive.class)
            //.addClasses(EnfermedadTest.class, LaboratorioTest.class, UsuarioTest.class, AgendaTest.class, VacunaTest.class, VacunadorTest.class)
            .addPackages(false, /*ReservaTest.class.getPackage(),*/Reserva.class.getPackage(), IReservaDAORemote.class.getPackage(), ControladorReserva.class.getPackage(), DtReserva.class.getPackage(), Reserva.class.getPackage(), ReservaID.class.getPackage(), ReservaInexistente.class.getPackage())
            .addPackage(InitSuite.class.getPackage())
            .addAsResource("META-INF/test-persistence.xml", "META-INF/persistence.xml")
            ;
    }
	
	@EJB
	IAgendaDAORemote ca;
	@Resource
	UserTransaction utx;
	
	@PersistenceContext
	private EntityManager em;
	
	@OperateOnDeployment("normal")
	@Test(expected=VacunatorioNoCargadoException.class)
	@InSequence(1)
	public void testListarAgendaSinVacunatorio() throws AgendaInexistente, VacunatorioNoCargadoException {
		ca.listarAgendas("vacunatorio1");
	}
	
	@OperateOnDeployment("normal")
	@Test(expected=VacunatorioNoCargadoException.class)
	@InSequence(2)
	public void testAgregarAgendaSinVacunatorio() throws AgendaRepetida, CupoInexistente, VacunatorioNoCargadoException {
		LocalDate tempDate = LocalDate.of(1,2,3);
		ca.agregarAgenda("vacunatorio1", tempDate);
	}
	
	@OperateOnDeployment("normal")
	@Test(expected=AgendaInexistente.class)
	@InSequence(3)
	public void testListarAgendasNull() throws AgendaInexistente, VacunatorioNoCargadoException {
//		DtDireccion tempDir = new DtDireccion("a","b","c");
		LocalDate tempDate = LocalDate.of(1,2,3);
		Vacunatorio vacunatorio1 = new Vacunatorio("vacunatorio1", "Nest1", null, 1555897235, 1.0f, 1.0f);
		Sexo s1 = Sexo.valueOf("Masculino");
		Ciudadano c1 = new Ciudadano(1, "c1", "ap1", tempDate, "mail", null, s1, "sector1", true);
		PlanVacunacion pv1 = new PlanVacunacion("plan1", "plan1");
		LocalDate tempDateInicio = LocalDate.of(4,5,6);
		LocalDate tempDateFin = LocalDate.of(4,5,6);
		Etapa e1 = new Etapa(tempDateInicio, tempDateFin, "activa", pv1);
		pv1.getEtapas().add(e1);
		Puesto puesto1 = new Puesto("puesto1", vacunatorio1);
		vacunatorio1.getPuesto().add(puesto1);
		Enfermedad enf = new Enfermedad("enf");
		Laboratorio lab = new Laboratorio("lab");
		pv1.setEnfermedad(enf);
		Vacuna vac1 = new Vacuna("vac1", 1, 1, 1, lab, enf);
		e1.setVacuna(vac1);
		try {
			utx.begin();
			em.persist(c1);
			em.persist(vacunatorio1);
			em.persist(enf);
			em.persist(lab);
			em.persist(vac1);
			em.persist(pv1);
			utx.commit();
		} catch(SystemException | NotSupportedException | SecurityException | IllegalStateException | RollbackException | HeuristicMixedException | HeuristicRollbackException e) {
			e.printStackTrace();
		}
		ca.listarAgendas("vacunatorio1");
	}
	
	@OperateOnDeployment("normal")
	@Test(expected=AgendaInexistente.class)
	@InSequence(4)
	public void testObtenerAgendaNull() throws AgendaInexistente {
		LocalDate tempDate = LocalDate.now().plusDays(17);
		ca.obtenerAgenda("vacunatorio1", tempDate);
	}
	
	@OperateOnDeployment("normal")
	@Test(expected=AgendaInexistente.class)
	@InSequence(5)
	public void testObtenerAgendaSoapNull() throws AgendaInexistente {
		LocalDate tempDate = LocalDate.now().plusDays(17);
		ca.obtenerAgendaSoap("vacunatorio1", tempDate);
	}
	
	@OperateOnDeployment("normal")
	@Test
	@InSequence(6)
	public void testAgregarAgenda() throws AgendaRepetida, CupoInexistente, VacunatorioNoCargadoException {
		LocalDate tempDate = LocalDate.now();
		ca.agregarAgenda("vacunatorio1", tempDate);
	}
	
	@OperateOnDeployment("normal")
	@Test(expected=AgendaRepetida.class)
	@InSequence(7)
	public void testAgregarAgendaError() throws AgendaRepetida, CupoInexistente, VacunatorioNoCargadoException {
		LocalDate tempDate = LocalDate.now();
		ca.agregarAgenda("vacunatorio1", tempDate);
	}
	
	@OperateOnDeployment("normal")
	@Test
	@InSequence(8)
	public void testObtenerAgenda() throws AgendaInexistente {
		try {
			utx.begin();
			List<Reserva> listaReservas = new ArrayList<>();
			Ciudadano c1 = em.find(Ciudadano.class, 1);
			Query queryPv = em.createQuery("SELECT pv FROM PlanVacunacion pv WHERE pv.nombre='plan1'");
			PlanVacunacion pv = (PlanVacunacion)queryPv.getSingleResult();
			Etapa e1 = pv.getEtapas().get(0);
			Puesto puesto1 = em.find(Puesto.class, new PuestoID("puesto1", "vacunatorio1"));
			LocalDateTime tempDateRegistro = LocalDateTime.of(4,5,6,7,8);
			LocalDateTime tempDateRegistroPrima = LocalDateTime.of(2,3,4,5,6);
			EstadoReserva er1 = EstadoReserva.valueOf("Completada");
			EstadoReserva er2 = EstadoReserva.valueOf("Cancelada");
			Reserva r1 = new Reserva(tempDateRegistro, er1, e1, c1, puesto1);
			Reserva r2 = new Reserva(tempDateRegistroPrima, er2, e1, c1, puesto1);
			
			listaReservas.add(r1);
			listaReservas.add(r2);
		
			c1.getReservas().add(r1);
			c1.getReservas().add(r2);
			Vacunatorio v1 = em.find(Vacunatorio.class, "vacunatorio1");
			List<Agenda> agendas = v1.getAgenda();
			LocalDate agendaDate = LocalDate.now();
			for (Agenda a : agendas) {
				if (a.getFecha().isEqual(agendaDate)) {
					//a.setReservas(listaReservas);
					a.getReservas().add(r1);
					a.getReservas().add(r2);
					em.merge(c1);
					em.merge(v1);
					em.merge(r1);
					em.merge(r2);
				}
			}
			utx.commit();
			ArrayList<DtReserva> tempReservas = new ArrayList<>();
			tempReservas.add(r1.getDtReserva());
			tempReservas.add(r2.getDtReserva());
			assertArrayEquals(tempReservas.toArray(), ca.obtenerAgenda("vacunatorio1", LocalDate.now()).toArray());
		} catch(SystemException | NotSupportedException | SecurityException | IllegalStateException | RollbackException | HeuristicMixedException | HeuristicRollbackException e) {	
			e.printStackTrace();
		}
	}

	@OperateOnDeployment("normal")
	@Test
	@InSequence(9)
	public void testListarAgendas() throws AgendaInexistente, VacunatorioNoCargadoException {
		try {
			utx.begin();
			Vacunatorio v = em.find(Vacunatorio.class, "vacunatorio1");
			List<Agenda> agendas = v.getAgenda();
			ArrayList<DtAgenda> tempAgenda = new ArrayList<>();
			ArrayList<DtReserva> tempReservas = new ArrayList<>();
			for (Agenda a : agendas) {
				if (a.getFecha().isEqual(LocalDate.now())) {
					for(Reserva r: a.getReservas()) {
						tempReservas.add(r.getDtReserva());
					}
				}
				DtAgenda tempDtAgenda = new DtAgenda(a.getFecha(), tempReservas);
				tempAgenda.add(tempDtAgenda);
			}
			assertArrayEquals(tempAgenda.toArray(), ca.listarAgendas("vacunatorio1").toArray());
			utx.commit();
		}catch(SystemException | NotSupportedException | SecurityException | IllegalStateException | RollbackException | HeuristicMixedException | HeuristicRollbackException e) {
			e.printStackTrace();
		}
	}
	
	@OperateOnDeployment("normal")
	@Test(expected=AssertionError.class)
	@InSequence(10)
	public void listarAgendasError() throws AgendaInexistente, VacunatorioNoCargadoException{
		ArrayList<DtAgenda> tempAgenda = new ArrayList<>();
		assertArrayEquals(tempAgenda.toArray(), ca.listarAgendas("vacunatorio1").toArray());
	}
	
	@OperateOnDeployment("normal")
	@Test
	@InSequence(11)
	public void testObtenerAgendaSoap() throws AgendaInexistente {
		try {
			utx.begin();
			Vacunatorio v1 = em.find(Vacunatorio.class, "vacunatorio1");
			List<Agenda> agendas = v1.getAgenda();
			ArrayList<DtReservaCompleto> tempReservasCompletas = new ArrayList<>();
			for (Agenda a : agendas) {
				if (a.getFecha().isEqual(LocalDate.now())) {
					for(Reserva r : a.getReservas()) {
						tempReservasCompletas.add(r.getDtReservaCompleto());
					}
				}
			}
			assertArrayEquals(tempReservasCompletas.toArray(), ca.obtenerAgendaSoap("vacunatorio1", LocalDate.now()).toArray());
			utx.commit();
		}catch(SystemException | NotSupportedException | SecurityException | IllegalStateException | RollbackException | HeuristicMixedException | HeuristicRollbackException e) {
			e.printStackTrace();
		}
	}
	
	@OperateOnDeployment("normal")
	@Test
	@InSequence(20)
	public void testClean() {
		try {
			utx.begin();
			Vacunatorio v1 = em.find(Vacunatorio.class, "vacunatorio1");
			Ciudadano c = em.find(Ciudadano.class, 1);
			Enfermedad enf = em.find(Enfermedad.class, "enf");
			Laboratorio lab = em.find(Laboratorio.class, "lab");
			Vacuna vac = em.find(Vacuna.class, "vac1");
			
			Query queryPV = em.createQuery("SELECT pv FROM PlanVacunacion pv WHERE pv.nombre='plan1'");
			PlanVacunacion pv = (PlanVacunacion)queryPV.getSingleResult();
			em.remove(lab);
			em.remove(enf);
			for(Agenda a: v1.getAgenda()) {
				em.remove(a);
			}
			for(Puesto p: v1.getPuesto()) {
				em.remove(p);
			}
			em.remove(v1);
			em.remove(vac);
			for(Reserva res :c.getReservas()) {
				em.remove(res);
			}
			for(Etapa e: pv.getEtapas()) {
				em.remove(e);
			}
			em.remove(c);
			em.remove(pv);
			utx.commit();
		} catch (NotSupportedException | SystemException | SecurityException | IllegalStateException | RollbackException | HeuristicMixedException | HeuristicRollbackException e) {
			e.printStackTrace();
		}
	}
	
}
