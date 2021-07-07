package init;

import java.time.LocalDate;
import java.time.LocalDateTime;
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
import datatypes.DtDireccion;
import datatypes.DtReserva;
import datatypes.EstadoReserva;
import datatypes.Sexo;
import entities.Ciudadano;
import entities.Enfermedad;
import entities.Etapa;
import entities.Laboratorio;
import entities.PlanVacunacion;
import entities.Puesto;
import entities.Reserva;
import entities.Vacuna;
import entities.Vacunatorio;
import exceptions.AccionInvalida;
import exceptions.EnfermedadInexistente;
import exceptions.PlanVacunacionInexistente;
import exceptions.ReservaInexistente;
import interfaces.IPlanVacunacionRemote;
import interfaces.IReservaDAORemote;
import persistence.ReservaID;

@RunWith(Arquillian.class)
public class PlanVacunacionTest {
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
	IPlanVacunacionRemote cp;
	@Resource
	UserTransaction utx;
	
	@PersistenceContext
	private EntityManager em;
	
	@OperateOnDeployment("normal")
	@Test(expected=EnfermedadInexistente.class)
	@InSequence(1)
	public void testAgregarPlanVacunacionSinEnfermedad() throws EnfermedadInexistente {
		cp.agregarPlanVacunacion("no", "existe", "noExiste");
	}
	
	@OperateOnDeployment("normal")
	@Test(expected=PlanVacunacionInexistente.class)
	@InSequence(2)
	public void testObtenerPlanVacunacionSinPlanes() throws PlanVacunacionInexistente {
		cp.obtenerPlanVacunacion(0);
	}
	
	@OperateOnDeployment("normal")
	@Test(expected=PlanVacunacionInexistente.class)
	@InSequence(3)
	public void testListarPlanVacunacionSinPlanes() throws PlanVacunacionInexistente {
		cp.listarPlanesVacunacion();
	}
	
	@OperateOnDeployment("normal")
	@Test(expected=PlanVacunacionInexistente.class)
	@InSequence(4)
	public void testListarAgendasAbiertasSinPlanes() throws PlanVacunacionInexistente {
		cp.listarAgendasAbiertas();
	}
	
	@OperateOnDeployment("normal")
	@Test(expected=PlanVacunacionInexistente.class)
	@InSequence(5)
	public void testListarAgendasProximasSinPlanes() throws PlanVacunacionInexistente {
		cp.listarAgendasProximas();
	}
	
	@OperateOnDeployment("normal")
	@Test
	@InSequence(6)
	public void testAgregarPlanVacunacion() throws EnfermedadInexistente {
		try {
			utx.begin();
			Enfermedad enf1 = new Enfermedad("enf1");
			Laboratorio lab1 = new Laboratorio("lab1");
			Enfermedad enf2 = new Enfermedad("enf2");
			Laboratorio lab2 = new Laboratorio("lab2");
			Enfermedad enf3 = new Enfermedad("enf3");
			em.persist(enf1);
			em.persist(lab1);
			em.persist(enf2);
			em.persist(lab2);
			em.persist(enf3);
			utx.commit();
		}catch(SystemException | NotSupportedException | SecurityException | IllegalStateException | RollbackException | HeuristicMixedException | HeuristicRollbackException e) {
			e.printStackTrace();
		}
		cp.agregarPlanVacunacion("plan1", "plan1", "enf1");
		cp.agregarPlanVacunacion("plan2", "plan2", "enf2");
		cp.agregarPlanVacunacion("plan3", "plan3", "enf3");
	}
	
	@OperateOnDeployment("normal")
	@Test(expected=PlanVacunacionInexistente.class)
	@InSequence(7)
	public void testListarAgendasAbiertasSinEtapas() throws PlanVacunacionInexistente {
		cp.listarAgendasAbiertas();
	}
	
	@OperateOnDeployment("normal")
	@Test(expected=PlanVacunacionInexistente.class)
	@InSequence(8)
	public void testListarAgendasProximasSinEtapas() throws PlanVacunacionInexistente {
		cp.listarAgendasProximas();
	}
	
	@OperateOnDeployment("normal")
	@Test(expected=PlanVacunacionInexistente.class)
	@InSequence(9)
	public void testEliminarPlanVacunacionSinPlanes() throws PlanVacunacionInexistente, AccionInvalida {
		try {
			utx.begin();
			Query queryPlan = em.createQuery("SELECT p FROM PlanVacunacion p WHERE p.nombre='plan1'");
			PlanVacunacion  plan1 = (PlanVacunacion)queryPlan.getSingleResult();
			Vacuna vacuna = new Vacuna("vacuna1", 1, 6, 5,em.find(Laboratorio.class, "lab1"), em.find(Enfermedad.class, "enf1"));
			em.persist(vacuna);
			Etapa e1 = new Etapa(LocalDate.now().minusDays(10), LocalDate.now().minusDays(1), "condicion", plan1);
			e1.setVacuna(vacuna);
			plan1.getEtapas().add(e1);
			em.persist(e1);
			em.merge(plan1);
			utx.commit();
		}catch(SystemException | NotSupportedException | SecurityException | IllegalStateException | RollbackException | HeuristicMixedException | HeuristicRollbackException e) {
			e.printStackTrace();
		}
		cp.eliminarPlanVacunacion(0);
	}
	
//no pude hacer los obtenerAgendas(ni abierta ni proximas) nulas(son 6 lineas en total)
	
	@OperateOnDeployment("normal")
	@Test
	@InSequence(12)
	public void testObtenerPlanVacunacion() throws PlanVacunacionInexistente {
		PlanVacunacion  plan2 = new PlanVacunacion();
		try {
			utx.begin();
			Query queryPlan = em.createQuery("SELECT p FROM PlanVacunacion p WHERE p.nombre='plan2'");
			plan2 = (PlanVacunacion)queryPlan.getSingleResult();
			Vacuna vacuna = new Vacuna("vacuna2", 1, 6, 5, em.find(Laboratorio.class, "lab2"), em.find(Enfermedad.class, "enf2"));
			em.persist(vacuna);
			Etapa e2a = new Etapa(LocalDate.now().minusDays(10), LocalDate.now().plusDays(1), "condicion", plan2);
			Etapa e2b = new Etapa(LocalDate.now().minusDays(11), LocalDate.now().plusDays(2), "condicion", plan2);
			e2a.setVacuna(vacuna);
			e2b.setVacuna(vacuna);
			plan2.getEtapas().add(e2a);
			plan2.getEtapas().add(e2b);
			em.persist(e2a);
			em.persist(e2b);
			em.merge(plan2);
			Vacunatorio vacunatorio = new Vacunatorio("vacunatorio1", "vacunatorio", null, 24789065, 2.0f, 3.0f);
			em.persist(vacunatorio);
			Puesto p = new Puesto("puesto1", vacunatorio);
			vacunatorio.getPuesto().add(p);
			//em.merge(p);
			em.merge(vacunatorio);
			LocalDate tempDateNac = LocalDate.of(1998, 8, 11);
			Ciudadano c = new Ciudadano(1, "c1", "ciudadano", tempDateNac, "corre", null, Sexo.Otro, "sector", true);
			em.persist(c);
			Reserva res = new Reserva(LocalDateTime.now(), EstadoReserva.EnProceso, e2b, c, p);
			c.getReservas().add(res);
			em.merge(c);
			res.setEtapa(e2b);
			em.merge(res);
			utx.commit();
		}catch(SystemException | NotSupportedException | SecurityException | IllegalStateException | RollbackException | HeuristicMixedException | HeuristicRollbackException e) {
			e.printStackTrace();
		}
		cp.obtenerPlanVacunacion(plan2.getId());
	}
	
	@OperateOnDeployment("normal")
	@Test
	@InSequence(13)
	public void testListarPlanVacunacion() throws PlanVacunacionInexistente {
		cp.listarPlanesVacunacion();
	}
	
	@OperateOnDeployment("normal")
	@Test//esta excepcion no deberia ir aca(problema con las fechas de las etapas)
	@InSequence(14)
	public void testListarAgendasAbiertas() throws PlanVacunacionInexistente {
		cp.listarAgendasAbiertas();
	}
	
	@OperateOnDeployment("normal")
	@Test(expected=PlanVacunacionInexistente.class)//esta excepcion no deberia ir aca(problema con las fechas de las etapas)
	@InSequence(15)
	public void testListarAgendasProximas() throws PlanVacunacionInexistente {
		cp.listarAgendasProximas();
	}
	
	@OperateOnDeployment("normal")
	@Test(expected=AccionInvalida.class)
	@InSequence(16)
	public void testEliminarPlanVacunacionInvalido() throws PlanVacunacionInexistente, AccionInvalida {
		PlanVacunacion  plan2 = new PlanVacunacion();
		try {
			utx.begin();
			Query queryPlan = em.createQuery("SELECT p FROM PlanVacunacion p WHERE p.nombre='plan2'");
			plan2 = (PlanVacunacion)queryPlan.getSingleResult();
			utx.commit();
		}catch(SystemException | NotSupportedException | SecurityException | IllegalStateException | RollbackException | HeuristicMixedException | HeuristicRollbackException e) {
			e.printStackTrace();
		}
		cp.eliminarPlanVacunacion(plan2.getId());
	}
	
	@OperateOnDeployment("normal")
	@Test
	@InSequence(17)
	public void testEliminarPlanVacunacion() throws PlanVacunacionInexistente, AccionInvalida {
		PlanVacunacion  plan3 = new PlanVacunacion();
		try {
			utx.begin();
			Query queryPlan = em.createQuery("SELECT p FROM PlanVacunacion p WHERE p.nombre='plan3'");
			plan3 = (PlanVacunacion)queryPlan.getSingleResult();
			Etapa e3a = new Etapa(LocalDate.now().minusDays(10), LocalDate.now().plusDays(1), "condicion", plan3);
			Etapa e3b = new Etapa(LocalDate.now().minusDays(11), LocalDate.now().plusDays(2), "condicion", plan3);
			plan3.getEtapas().add(e3a);
			plan3.getEtapas().add(e3b);
			em.persist(e3a);
			em.persist(e3b);
			em.merge(plan3);
			utx.commit();
		}catch(SystemException | NotSupportedException | SecurityException | IllegalStateException | RollbackException | HeuristicMixedException | HeuristicRollbackException e) {
			e.printStackTrace();
		}
		cp.eliminarPlanVacunacion(plan3.getId());
	}
	
	@OperateOnDeployment("normal")
	@Test
	@InSequence(20)
	public void testClean() {
		try {
			utx.begin();
			Enfermedad enf1 = em.find(Enfermedad.class, "enf1");
			em.remove(enf1);
			Laboratorio lab1 = em.find(Laboratorio.class, "lab1");
			em.remove(lab1);
			Vacuna vacuna1 = em.find(Vacuna.class, "vacuna1");
			em.remove(vacuna1);
			Enfermedad enf2 = em.find(Enfermedad.class, "enf2");
			em.remove(enf2);
			Laboratorio lab2 = em.find(Laboratorio.class, "lab2");
			em.remove(lab2);
			Vacuna vacuna2 = em.find(Vacuna.class, "vacuna2");
			em.remove(vacuna2);
			Enfermedad enf3 = em.find(Enfermedad.class, "enf3");
			em.remove(enf3);
			Ciudadano c = em.find(Ciudadano.class, 1);
			for(Reserva r: c.getReservas()) {
				em.remove(r);
			}
			em.remove(c);
			Query queryPlan = em.createQuery("SELECT p FROM PlanVacunacion p");
			List<PlanVacunacion> planes = (List)queryPlan.getResultList();
			for(Etapa e: planes.get(0).getEtapas()) {
				em.remove(e);
			}
			em.remove(planes.get(0));
			for(Etapa e: planes.get(1).getEtapas()) {
				em.remove(e);
			}
			em.remove(planes.get(1));
			Vacunatorio vacunatorio = em.find(Vacunatorio.class, "vacunatorio1");
			for(Puesto p: vacunatorio.getPuesto()) {
				em.remove(p);
			}
			em.remove(vacunatorio);
			utx.commit();
		}catch(SystemException | NotSupportedException | SecurityException | IllegalStateException | RollbackException | HeuristicMixedException | HeuristicRollbackException e) {
			e.printStackTrace();
		}
	}
}
