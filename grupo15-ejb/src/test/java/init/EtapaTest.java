package init;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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
import datatypes.DtDireccion;
import datatypes.DtEtapa;
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
import exceptions.EtapaInexistente;
import exceptions.EtapaRepetida;
import exceptions.PlanVacunacionInexistente;
import exceptions.ReservaInexistente;
import exceptions.VacunaInexistente;
import interfaces.IEtapaRemote;
import interfaces.IReservaDAORemote;
import persistence.PuestoID;
import persistence.ReservaID;

@RunWith(Arquillian.class)
public class EtapaTest {
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
	IEtapaRemote ce;
	@Resource
	UserTransaction utx;
	
	@PersistenceContext
	private EntityManager em;
	
	@OperateOnDeployment("normal")
	@Test(expected=EtapaInexistente.class)
	@InSequence(1)
	public void testListarEtapaSinEtapa() throws EtapaInexistente {
		try {
			utx.begin();
			Enfermedad enf1 = new Enfermedad("enf1");
			Enfermedad enf2 = new Enfermedad("enf2");
			Laboratorio lab1 = new Laboratorio("lab1");
			Vacunatorio vacunatorio1 = new Vacunatorio("vacunatorio1", "vac", null, 24586754, 2.0f, 3.0f);
			Ciudadano c1 = new Ciudadano(1, "ciudadano", "uno", LocalDate.of(1999, 5, 31), "correo", null, Sexo.valueOf("Femenino"), "sector", true);
			em.persist(enf1);
			em.persist(enf2);
			em.persist(lab1);
			em.persist(vacunatorio1);
			em.persist(c1);
			Vacuna vacuna1 = new Vacuna("vacuna1", 1, 5, 6, lab1, enf1);
			Vacuna vacuna2 = new Vacuna("vacuna2", 1, 5, 6, lab1, enf2);
			em.persist(vacuna1);
			em.persist(vacuna2);
			Puesto puesto1 = new Puesto("puesto1", vacunatorio1);
			em.persist(puesto1);
			PlanVacunacion pv1 = new PlanVacunacion("plan1", "primerPlan");//igual enfermedad que la vacuna
			PlanVacunacion pv2 = new PlanVacunacion("plan2", "segundoPlan");//diferente enfermedad que la vacuna
			em.persist(pv1);
			em.persist(pv2);
			pv1.setEnfermedad(enf1);
			pv2.setEnfermedad(enf2);
			em.merge(pv1);
			em.merge(pv2);
			utx.commit();
		}catch(SystemException | NotSupportedException | SecurityException | IllegalStateException | RollbackException | HeuristicMixedException | HeuristicRollbackException e) {
			e.printStackTrace();
		}
		ce.listarEtapas();
	}
	
	@OperateOnDeployment("normal")
	@Test(expected=PlanVacunacionInexistente.class)
	@InSequence(2)
	public void testAgregarEtapaSinPlan() throws EtapaRepetida, PlanVacunacionInexistente, VacunaInexistente, AccionInvalida {
		ce.agregarEtapa(LocalDate.now(), LocalDate.now().plusDays(15), "condicion", 25, "vacunaInexistente");
	}
	
	@OperateOnDeployment("normal")
	@Test(expected=VacunaInexistente.class)
	@InSequence(3)
	public void testAgregarEtapaSinVacuna() throws EtapaRepetida, PlanVacunacionInexistente, VacunaInexistente, AccionInvalida {
		Query queryPlan = em.createQuery("SELECT pv FROM PlanVacunacion pv WHERE pv.nombre='plan2'");
		PlanVacunacion plan2 = (PlanVacunacion)queryPlan.getSingleResult();
		ce.agregarEtapa(LocalDate.now(), LocalDate.now().plusDays(15), "condicion", plan2.getId(), "vacunaInexistente");
	}
	
	@OperateOnDeployment("normal")
	@Test(expected=AccionInvalida.class)
	@InSequence(4)
	public void testAgregarEtapaInvalida() throws EtapaRepetida, PlanVacunacionInexistente, VacunaInexistente, AccionInvalida {
		Query queryPlan = em.createQuery("SELECT pv FROM PlanVacunacion pv WHERE pv.nombre='plan2'");
		PlanVacunacion plan2 = (PlanVacunacion)queryPlan.getSingleResult();
		ce.agregarEtapa(LocalDate.now(), LocalDate.now().plusDays(15), "condicion", plan2.getId(), "vacuna1");
	}
	
	@OperateOnDeployment("normal")
	@Test(expected=EtapaInexistente.class)
	@InSequence(5)
	public void testObtenerEtapaSinEtapa() throws EtapaInexistente {
		ce.obtenerEtapa(6, 7);
	}
	
	@OperateOnDeployment("normal")
	@Test(expected=EtapaInexistente.class)
	@InSequence(6)
	public void testEliminarEtapaSinEtapa() throws EtapaInexistente, AccionInvalida {
		ce.eliminarEtapa(6, 7);
	}
	
	@OperateOnDeployment("normal")
	@Test
	@InSequence(7)
	public void testAgregarEtapa() throws EtapaRepetida, PlanVacunacionInexistente, VacunaInexistente, AccionInvalida {
		Query queryPlan1 = em.createQuery("SELECT pv FROM PlanVacunacion pv WHERE pv.nombre='plan1'");
		PlanVacunacion plan1 = (PlanVacunacion)queryPlan1.getSingleResult();
		ce.agregarEtapa(LocalDate.now(), LocalDate.now().plusDays(15), "condicion", plan1.getId(), "vacuna1");
		Query queryPlan2 = em.createQuery("SELECT pv FROM PlanVacunacion pv WHERE pv.nombre='plan2'");
		PlanVacunacion plan2 = (PlanVacunacion)queryPlan2.getSingleResult();
		ce.agregarEtapa(LocalDate.now(), LocalDate.now().plusDays(15), "condicion", plan2.getId(), "vacuna2");
	}
	
	@OperateOnDeployment("normal")
	@Test
	@InSequence(8)
	public void testObtenerEtapa() throws EtapaInexistente {
		DtEtapa dtEtapa = new DtEtapa();
		int etapaId = 0;
		int planId = 0;
		try {
			utx.begin();
			Query queryPlan = em.createQuery("SELECT pv FROM PlanVacunacion pv WHERE pv.nombre='plan1'");
			PlanVacunacion plan1 = (PlanVacunacion)queryPlan.getSingleResult();
			Etapa etapa = plan1.getEtapas().get(0);
			Vacunatorio vac = em.find(Vacunatorio.class, "vacunatorio1");
			Reserva res = new Reserva(LocalDateTime.now(), EstadoReserva.EnProceso, etapa, em.find(Ciudadano.class, 1), em.find(Puesto.class, new PuestoID("plan1", vac.getId())));
			em.persist(res);
			etapaId = etapa.getId();
			planId = plan1.getId();
			dtEtapa = new DtEtapa(etapa.getId(), etapa.getFechaInicio().toString(), etapa.getFechaFin().toString(), etapa.getCondicion(), plan1.getId(), etapa.getVacuna().getNombre());
			utx.commit();
		}catch(SystemException | NotSupportedException | SecurityException | IllegalStateException | RollbackException | HeuristicMixedException | HeuristicRollbackException e) {
			e.printStackTrace();
		}
		assertEquals(dtEtapa.getId(), ce.obtenerEtapa(etapaId, planId).getId());
	}
	
	@OperateOnDeployment("normal")
	@Test(expected=AccionInvalida.class)
	@InSequence(9)
	public void testEliminarEtapaInvalida() throws EtapaInexistente, AccionInvalida {
		int etapaId = 0;
		int planId = 0;
		try {
			utx.begin();
			Query queryPlan = em.createQuery("SELECT pv FROM PlanVacunacion pv WHERE pv.nombre='plan1'");
			PlanVacunacion plan1 = (PlanVacunacion)queryPlan.getSingleResult();
			Etapa etapa = plan1.getEtapas().get(0);
			etapaId = etapa.getId();
			planId = plan1.getId();
			utx.commit();
		}catch(SystemException | NotSupportedException | SecurityException | IllegalStateException | RollbackException | HeuristicMixedException | HeuristicRollbackException e) {
			e.printStackTrace();
		}
		ce.eliminarEtapa(etapaId, planId);
	}
	
//	@OperateOnDeployment("normal")
//	@Test(expected=EtapaRepetida.class)
//	@InSequence(10)
//	public void testAgregarEtapaRepetida()throws EtapaRepetida, PlanVacunacionInexistente, VacunaInexistente, AccionInvalida {
//		Query queryPlan = em.createQuery("SELECT pv FROM PlanVacunacion pv WHERE pv.nombre='plan1'");
//		PlanVacunacion plan1 = (PlanVacunacion)queryPlan.getSingleResult();
//		ce.agregarEtapa(LocalDate.now(), LocalDate.now().plusDays(15), "condicion", plan1.getId(), "vacuna1");
//	}
	
	@OperateOnDeployment("normal")
	@Test
	@InSequence(10)
	public void testListarEtapa() throws EtapaInexistente {
		ArrayList<Integer> compEtapas = new ArrayList<>();
		Query query = em.createQuery("SELECT e FROM Etapa e ORDER BY id ASC");
		@SuppressWarnings("unchecked")
		ArrayList<Etapa> etapas = (ArrayList)query.getResultList();
		for(Etapa e: etapas) {
			DtEtapa dtetapa = new DtEtapa(e.getId(), e.getFechaInicio().toString(), e.getFechaFin().toString(), e.getCondicion(), e.getPlanVacunacion().getId(), e.getVacuna().getNombre());
			System.out.println(e.getFechaFin().toString() + "fecha test");//imprimo fecha test
			compEtapas.add(dtetapa.getId());
		}
		ArrayList<Integer> etapasComp = new ArrayList<>();
		for(DtEtapa dte: ce.listarEtapas()) {
			etapasComp.add(dte.getId());
		}
		assertArrayEquals(compEtapas.toArray(), etapasComp.toArray());
	}
	
	@OperateOnDeployment("normal")
	@Test
	@InSequence(11)
	public void testEliminarEtapa() throws EtapaInexistente, AccionInvalida {
		int etapaId = 0;
		int planId = 0;
		try {
			utx.begin();
			Query queryPlan = em.createQuery("SELECT pv FROM PlanVacunacion pv WHERE pv.nombre='plan2'");
			PlanVacunacion plan2 = (PlanVacunacion)queryPlan.getSingleResult();
			Etapa etapa = plan2.getEtapas().get(0);
			etapaId = etapa.getId();
			planId = plan2.getId();
			utx.commit();
		}catch(SystemException | NotSupportedException | SecurityException | IllegalStateException | RollbackException | HeuristicMixedException | HeuristicRollbackException e) {
			e.printStackTrace();
		}
		ce.eliminarEtapa(etapaId, planId);
	}
	
	@OperateOnDeployment("normal")
	@Test
	@InSequence(20)
	public void testClean() {
		try {
			utx.begin();
			Enfermedad e1 = em.find(Enfermedad.class, "enf1");
			Enfermedad e2 = em.find(Enfermedad.class, "enf2");
			em.remove(e1);
			em.remove(e2);
			Laboratorio l1 = em.find(Laboratorio.class, "lab1");
			em.remove(l1);
			Vacunatorio vacunatorio1 = em.find(Vacunatorio.class, "vacunatorio1");
			Puesto p = em.find(Puesto.class, new PuestoID("puesto1", "vacunatorio1"));
			em.remove(p);
			em.remove(vacunatorio1);
			Vacuna vacuna1 = em.find(Vacuna.class, "vacuna1");
			Vacuna vacuna2 = em.find(Vacuna.class, "vacuna2");
			em.remove(vacuna1);
			em.remove(vacuna2);
			Query queryPlan1 = em.createQuery("SELECT pv FROM PlanVacunacion pv WHERE pv.nombre='plan1'");
			PlanVacunacion plan1 = (PlanVacunacion)queryPlan1.getSingleResult();
			Query queryPlan2 = em.createQuery("SELECT pv FROM PlanVacunacion pv WHERE pv.nombre='plan2'");
			PlanVacunacion plan2 = (PlanVacunacion)queryPlan2.getSingleResult();
			em.remove(plan2);
			Ciudadano c1 = em.find(Ciudadano.class, 1);
			Etapa etapa = plan1.getEtapas().get(0);
			Query queryR = em.createQuery("SELECT r FROM Reserva r");
			@SuppressWarnings("unchecked")
			List<Reserva> reservas = queryR.getResultList();
			for (Reserva r: reservas) {
				if (r.getEtapa().equals(etapa)) {
					em.remove(r);
				}
			}
			em.remove(etapa);
			em.remove(c1);
			em.remove(plan1);
			utx.commit();
		}catch(SystemException | NotSupportedException | SecurityException | IllegalStateException | RollbackException | HeuristicMixedException | HeuristicRollbackException e) {
			e.printStackTrace();
		}
	}
	
}
