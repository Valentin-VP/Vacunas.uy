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

import org.jboss.arquillian.container.test.api.OperateOnDeployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.junit.InSequence;
import org.junit.Test;
import org.junit.runner.RunWith;

import datatypes.DtLaboratorio;
import entities.Enfermedad;
import entities.Laboratorio;
import entities.Vacuna;
import exceptions.AccionInvalida;
import exceptions.LaboratorioInexistente;
import exceptions.LaboratorioRepetido;
import interfaces.ILaboratorioRemote;

@RunWith(Arquillian.class)
public class LaboratorioTest {
	private final Logger LOGGER = Logger.getLogger(getClass().getName());
	
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
		Query query1 = em.createQuery("SELECT l FROM Laboratorio l WHERE l.nombre = 'testLab1'");
	   	Query query2 = em.createQuery("SELECT l FROM Laboratorio l WHERE l.nombre = 'testLab2'");
	   	Laboratorio lab = (Laboratorio)query1.getSingleResult();
	   	assertEquals(lab.getNombre(), "testLab1");
	   	lab = (Laboratorio)query2.getSingleResult();
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
//		Query queryL = em.createQuery("SELECT l FROM Laboratorio l WHERE l.nombre = 'testLab1'");
//		Laboratorio lab = (Laboratorio)queryL.getSingleResult();
//		assertEquals(lab.getNombre(), "testLab1");
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
				Query queryL = em.createQuery("SELECT l FROM Laboratorio l WHERE l.nombre = 'testLab2'");
				Query queryV = em.createQuery("SELECT v FROM Vacuna v WHERE v.nombre = 'vacuna1'");
				Query queryE = em.createQuery("SELECT e FROM Enfermedad e WHERE e.nombre = 'enfermedad1'");
				Laboratorio l = (Laboratorio)queryL.getSingleResult();
				Vacuna v = (Vacuna)queryV.getSingleResult();
				Enfermedad e = (Enfermedad)queryE.getSingleResult();
				em.remove(l);
				em.remove(v);
				em.remove(e);
				utx.commit();
			} catch (NotSupportedException | SystemException | SecurityException | IllegalStateException | RollbackException | HeuristicMixedException | HeuristicRollbackException e) {
				e.printStackTrace();
			}
			
	}
	
}
