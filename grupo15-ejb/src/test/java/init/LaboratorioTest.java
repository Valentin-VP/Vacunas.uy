package init;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

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
import datatypes.DtLaboratorio;
import datatypes.DtReserva;
import entities.Enfermedad;
import entities.Laboratorio;
import entities.Reserva;
import entities.Vacuna;
import exceptions.AccionInvalida;
import exceptions.LaboratorioInexistente;
import exceptions.LaboratorioRepetido;
import exceptions.ReservaInexistente;
import interfaces.ILaboratorioRemote;
import interfaces.IReservaDAORemote;
import persistence.ReservaID;

@RunWith(Arquillian.class)
public class LaboratorioTest {
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
	ILaboratorioRemote cl;
	@Resource
	UserTransaction utx;
	
	@PersistenceContext
	private EntityManager em;
	
	@OperateOnDeployment("normal")
	@Test(expected=LaboratorioInexistente.class)
	@InSequence(1)
	public void testListarLaboratoriosNull() throws LaboratorioInexistente {
		ArrayList<DtLaboratorio> tempLabs = new ArrayList<>();
		cl.listarLaboratorios();
	}
	
	@OperateOnDeployment("normal")
	@Test
	@InSequence(2)
	public void testAgregarLaboratorio() throws LaboratorioRepetido{
		cl.agregarLaboratorio("testLab1");
		cl.agregarLaboratorio("testLab2");
	   	Laboratorio lab = em.find(Laboratorio.class, "testLab1");
	   	assertEquals(lab.getNombre(), "testLab1");
	   	lab = em.find(Laboratorio.class, "testLab2");
	   	Enfermedad e = new Enfermedad("enfermedad1");
	   	try {
			utx.begin();
			em.persist(e);
		   	Vacuna v = new Vacuna("vacuna1", 1, 1, 1, lab, e);
		   	em.persist(v);
		   	utx.commit();
		   	assertEquals(lab.getNombre(), "testLab2");
		} catch (NotSupportedException | SystemException | SecurityException | IllegalStateException | RollbackException | HeuristicMixedException | HeuristicRollbackException e1) {
			e1.printStackTrace();
		}
	}
	
	@OperateOnDeployment("normal")
	@Test(expected=LaboratorioRepetido.class)
	@InSequence(3)
	public void testAgregarLaboratorioRepetido() throws LaboratorioRepetido {
		cl.agregarLaboratorio("testLab2");
	}
	
	@OperateOnDeployment("normal")
	@Test
	@InSequence(4)
	public void testObtenerLaboratorio() throws LaboratorioInexistente {
		assertEquals("testLab1", cl.obtenerLaboratorio("testLab1").getNombre());
	}
	
	@OperateOnDeployment("normal")
	@Test(expected=LaboratorioInexistente.class)
	@InSequence(5)
	public void testObtenerLaboratorioNull() throws LaboratorioInexistente {
		assertEquals("testLab0", cl.obtenerLaboratorio("testLab0").getNombre());
	}
	
	@OperateOnDeployment("normal")
	@Test
	@InSequence(6)
	public void testListarLaboratorios() throws LaboratorioInexistente {
		ArrayList<DtLaboratorio> temporal  = new ArrayList<>();
		temporal.add(cl.obtenerLaboratorio("testLab1"));
		temporal.add(cl.obtenerLaboratorio("testLab2"));
		assertArrayEquals(temporal.toArray(), cl.listarLaboratorios().toArray());
	}
	
	@OperateOnDeployment("normal")
	@Test(expected=AssertionError.class)
	@InSequence(7)
	public void testListarLaboratoriosError() throws LaboratorioInexistente {
		ArrayList<DtLaboratorio> temporal  = new ArrayList<>();
		temporal.add(cl.obtenerLaboratorio("testLab1"));
		assertArrayEquals(temporal.toArray(), cl.listarLaboratorios().toArray());
	}
	
	@OperateOnDeployment("normal")
	@Test
	@InSequence(8)
	public void testEliminarLaboratorio() throws LaboratorioInexistente, AccionInvalida {
		cl.eliminarLaboratorio("testLab1");
	}
	
	@OperateOnDeployment("normal")
	@Test(expected=LaboratorioInexistente.class)
	@InSequence(9)
	public void testEliminarLaboratorioNull() throws LaboratorioInexistente, AccionInvalida {
		cl.eliminarLaboratorio("testLab0");
	}
	
	@OperateOnDeployment("normal")
	@Test(expected=AccionInvalida.class)
	@InSequence(10)
	public void testEliminarLaboratorioConVacuna() throws LaboratorioInexistente, AccionInvalida {
		cl.eliminarLaboratorio("testLab2");
	}
	
	@OperateOnDeployment("normal")
	@Test
	@InSequence(20)
	public void testClean() {
			try {
				utx.begin();
				Laboratorio l = em.find(Laboratorio.class, "testLab2");
				Vacuna v = em.find(Vacuna.class, "vacuna1");
				Enfermedad e = em.find(Enfermedad.class, "enfermedad1"); 
				em.remove(l);
				em.remove(v);
				em.remove(e);
				utx.commit();
			} catch (NotSupportedException | SystemException | SecurityException | IllegalStateException | RollbackException | HeuristicMixedException | HeuristicRollbackException e) {
				e.printStackTrace();
			}
			
	}
	
}
