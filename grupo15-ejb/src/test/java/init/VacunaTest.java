package init;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

import java.time.LocalDate;
import java.util.ArrayList;
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
import datatypes.DtReserva;
import datatypes.DtVacuna;
import entities.Enfermedad;
import entities.Etapa;
import entities.Laboratorio;
import entities.LoteDosis;
import entities.PlanVacunacion;
import entities.Reserva;
import entities.Stock;
import entities.Vacuna;
import entities.Vacunatorio;
import exceptions.AccionInvalida;
import exceptions.EnfermedadInexistente;
import exceptions.LaboratorioInexistente;
import exceptions.ReservaInexistente;
import exceptions.VacunaInexistente;
import exceptions.VacunaRepetida;
import interfaces.IControladorVacunaRemote;
import interfaces.IReservaDAORemote;
import persistence.ReservaID;

@RunWith(Arquillian.class)
public class VacunaTest {
	
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
	IControladorVacunaRemote cv;
	@Resource
	UserTransaction utx;
	
	@PersistenceContext
	private EntityManager em;
	
	@OperateOnDeployment("normal")
	@Test(expected=VacunaInexistente.class)
	@InSequence(1)
	public void testListarVacunasNull() throws VacunaInexistente {
		ArrayList<DtVacuna> temp = new ArrayList<>();
		Laboratorio lab1 = new Laboratorio("lab1");
		Enfermedad enf1 = new Enfermedad("enf1");
		Laboratorio lab2 = new Laboratorio("lab2");
		Enfermedad enf2 = new Enfermedad("enf2");
		Vacunatorio vacunatorio1 = new Vacunatorio("vacunatorio1", "Nest1", null, 1555897235, 1.0f, 1.0f);
		try {
			utx.begin();
			em.persist(lab1);
			em.persist(enf1);
			em.persist(lab2);
			em.persist(enf2);
			em.persist(vacunatorio1);
			utx.commit();
		} catch(SystemException | NotSupportedException | SecurityException | IllegalStateException | RollbackException | HeuristicMixedException | HeuristicRollbackException e) {
			e.printStackTrace();
		}
		temp = cv.listarVacunas();
	}
	
	@OperateOnDeployment("normal")
	@Test(expected=LaboratorioInexistente.class)
	@InSequence(2)
	public void testAgregarVacunaLaboratorioInexistente() throws VacunaRepetida , LaboratorioInexistente, EnfermedadInexistente {
		cv.agregarVacuna("vac1", 1, 1, 1, "", "");
	}
	
	@OperateOnDeployment("normal")
	@Test(expected=EnfermedadInexistente.class)
	@InSequence(3)
	public void testAgregarVacunaEnfermedadNoInexistente() throws VacunaRepetida , LaboratorioInexistente, EnfermedadInexistente {
		cv.agregarVacuna("vac1", 1, 1, 1, "lab1", "");
	}
	
	@OperateOnDeployment("normal")
	@Test
	@InSequence(4)
	public void testAgregarVacuna() throws VacunaRepetida , LaboratorioInexistente, EnfermedadInexistente {
		try {
			utx.begin();
			Query queryV1 = em.createQuery("SELECT v FROM Vacuna v WHERE v.nombre = 'vac1'");
			Query queryV2 = em.createQuery("SELECT v FROM Vacuna v WHERE v.nombre = 'vac2'");
			Query queryVac = em.createQuery("SELECT vac FROM Vacunatorio vac WHERE vac.id = 'vacunatorio1'");
			cv.agregarVacuna("vac1", 1, 1, 1, "lab1", "enf1");
			cv.agregarVacuna("vac2", 2, 2, 2, "lab2", "enf2");
			Vacuna vac1 = (Vacuna)queryV1.getSingleResult();
			assertEquals(vac1.getNombre(), "vac1");
			Vacuna vac2 = (Vacuna)queryV2.getSingleResult();
			Vacunatorio vacunatorio = (Vacunatorio)queryVac.getSingleResult();
			LoteDosis lote = new LoteDosis(1, vacunatorio, vac2, 1, 1, 1, 1.0f);
			lote.setVacuna(vac2);
			LocalDate tempInicio = LocalDate.of(1,2,3);
			LocalDate tempFinal = LocalDate.of(2,3,4);
			PlanVacunacion pv = new PlanVacunacion("plan1", "plan");
			Etapa e = new Etapa(tempInicio, tempFinal, "activa", pv);
			pv.getEtapas().add(e);
			Stock s = new Stock(vacunatorio, vac2, 1, 1, 1, 1);
			s.setVacuna(vac2);
			vacunatorio.getStock().add(s);
			vacunatorio.getLote().add(lote);
			e.setVacuna(vac2);
		
			em.persist(pv);
			em.merge(vacunatorio);
			utx.commit();
		} catch(SystemException | NotSupportedException | SecurityException | IllegalStateException | RollbackException | HeuristicMixedException | HeuristicRollbackException e1) {
			e1.printStackTrace();
		}
	}
	
	@OperateOnDeployment("normal")
	@Test(expected=VacunaRepetida.class)
	@InSequence(5)
	public void testAgregarVacunaError() throws VacunaRepetida , LaboratorioInexistente, EnfermedadInexistente {
		cv.agregarVacuna("vac1", 1, 1, 1, "lab1", "enf1");
	}
	
	@OperateOnDeployment("normal")
	@Test(expected=VacunaInexistente.class)
	@InSequence(6)
	public void testObtenerVacunaNull() throws VacunaInexistente {
		DtVacuna dtTemp = cv.obtenerVacuna("vac0");
	}
	
	@OperateOnDeployment("normal")
	@Test
	@InSequence(7)
	public void testObtenerVacuna() throws VacunaInexistente {
		DtVacuna dtTemp = cv.obtenerVacuna("vac1");
	}
	
	@OperateOnDeployment("normal")
	@Test
	@InSequence(8)
	public void testListarVacunas() throws VacunaInexistente {
		ArrayList<DtVacuna> arrayTemp = new ArrayList<>();
		arrayTemp.add(cv.obtenerVacuna("vac1"));
		arrayTemp.add(cv.obtenerVacuna("vac2"));
		assertArrayEquals(arrayTemp.toArray(), cv.listarVacunas().toArray());
	}
	
	@OperateOnDeployment("normal")
	@Test(expected=AssertionError.class)
	@InSequence(9)
	public void testListarVacunasError() throws VacunaInexistente{
		ArrayList<DtVacuna> arrayTemp = new ArrayList<>();
		arrayTemp.add(cv.obtenerVacuna("vac1"));
		assertArrayEquals(arrayTemp.toArray(), cv.listarVacunas().toArray());
	}
	
	@OperateOnDeployment("normal")
	@Test(expected=EnfermedadInexistente.class)
	@InSequence(10)
	public void testModificarVacunaSinEnfermedad() throws VacunaInexistente, LaboratorioInexistente, EnfermedadInexistente, AccionInvalida {
		cv.modificarVacuna("vac2", 3, 3, 3, "lab1", "enfo");
	}
	
	@OperateOnDeployment("normal")
	@Test(expected=LaboratorioInexistente.class)
	@InSequence(11)
	public void testModificarVacunaSinLaboratorio() throws VacunaInexistente, LaboratorioInexistente, EnfermedadInexistente, AccionInvalida {
		cv.modificarVacuna("vac2", 3, 3, 3, "lab0", "enf1");
	}
	
	@OperateOnDeployment("normal")
	@Test(expected=AccionInvalida.class)
	@InSequence(12)
	public void testModificarVacunaConEtapasAsociadas() throws VacunaInexistente, LaboratorioInexistente, EnfermedadInexistente, AccionInvalida {
		cv.modificarVacuna("vac2", 3, 3, 3, "lab1", "enf1");
	}
	
	@OperateOnDeployment("normal")
	@Test
	@InSequence(13)
	public void testModificarVacuna() throws VacunaInexistente, LaboratorioInexistente, EnfermedadInexistente, AccionInvalida {
		cv.modificarVacuna("vac1", 3, 3, 3, "lab1", "enf1");
	}
	
	@OperateOnDeployment("normal")
	@Test(expected=VacunaInexistente.class)
	@InSequence(14)
	public void testEliminarVacunaNull() throws VacunaInexistente, AccionInvalida {
		cv.eliminarVacuna("vac4");
	}
	
	@OperateOnDeployment("normal")
	@Test(expected=AccionInvalida.class)
	@InSequence(15)
	public void testEliminarVacunaConLote() throws VacunaInexistente, AccionInvalida {//no funciona por tema de id
		cv.eliminarVacuna("vac2");
	}
	
	@OperateOnDeployment("normal")
	@Test(expected=AccionInvalida.class)
	@InSequence(16)
	public void testEliminarVacunaConStock() throws VacunaInexistente, AccionInvalida {//no funciona por tema de id
		cv.eliminarVacuna("vac2");
	}

	@OperateOnDeployment("normal")
	@Test(expected=AccionInvalida.class)
	@InSequence(17)
	public void testEliminarVacunaConEtapa() throws VacunaInexistente, AccionInvalida {
		cv.eliminarVacuna("vac2");
	}
	
	@OperateOnDeployment("normal")
	@Test
	@InSequence(18)
	public void testEliminarVacuna() throws VacunaInexistente, AccionInvalida {
		cv.eliminarVacuna("vac1");
	}
	
	@OperateOnDeployment("normal")
	@Test(expected=VacunaInexistente.class)
	@InSequence(19)
	public void testModificarVacunaError() throws VacunaInexistente, LaboratorioInexistente, EnfermedadInexistente, AccionInvalida {
		cv.modificarVacuna("vac1", 2, 2, 2, "lab2", "enf2");
	}
	
	@OperateOnDeployment("normal")
	@Test
	@InSequence(20)
	public void testClean() {
		try {
			utx.begin();
			Query queryVac = em.createQuery("SELECT vac FROM Vacunatorio vac WHERE vac.id = 'vacunatorio1'");
			Query queryLab1 = em.createQuery("SELECT l FROM Laboratorio l WHERE l.nombre = 'lab1'");
			Query queryEnf1 = em.createQuery("SELECT enf FROM Enfermedad enf WHERE enf.nombre = 'enf1'");
			Query queryLab2 = em.createQuery("SELECT l FROM Laboratorio l WHERE l.nombre = 'lab2'");
			Query queryEnf2 = em.createQuery("SELECT enf FROM Enfermedad enf WHERE enf.nombre = 'enf2'");
			Query queryPv = em.createQuery("SELECT pv FROM PlanVacunacion pv WHERE pv.nombre = 'plan1'");
			Query queryVacuna = em.createQuery("SELECT v FROM Vacuna v WHERE v.id='vac2'");
			Vacuna vacuna = (Vacuna)queryVacuna.getSingleResult();
			Vacunatorio vac = (Vacunatorio)queryVac.getSingleResult();
			for(Stock s: vac.getStock()) {
				em.remove(s);
			}
			for(LoteDosis lote: vac.getLote()) {
				em.remove(lote);
			}
			Laboratorio lab1 = (Laboratorio)queryLab1.getSingleResult();
			Enfermedad enf1 = (Enfermedad)queryEnf1.getSingleResult();
			Laboratorio lab2 = (Laboratorio)queryLab2.getSingleResult();
			Enfermedad enf2 = (Enfermedad)queryEnf2.getSingleResult();
			PlanVacunacion pv = (PlanVacunacion)queryPv.getSingleResult();
			for(Etapa e: pv.getEtapas()) {
				em.remove(e);
			}
			em.remove(vac);
			em.remove(lab2);
			em.remove(enf2);
			em.remove(pv);
			em.remove(vacuna);
			em.remove(lab1);
			em.remove(enf1);
			utx.commit();
		} catch(SystemException | SecurityException | IllegalStateException | RollbackException | HeuristicMixedException | HeuristicRollbackException | NotSupportedException e1) {
			e1.printStackTrace();
		}
	}
	
}
