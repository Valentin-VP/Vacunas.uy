package init;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertArrayEquals;

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
import org.jboss.arquillian.core.api.annotation.Inject;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.junit.InSequence;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;

import controllers.ControladorEnfermedad;
import datatypes.DtEnfermedad;
import datatypes.DtEtapa;
import datatypes.DtPlanVacunacion;
import datatypes.DtVacuna;
import entities.Enfermedad;
import entities.Etapa;
import entities.Laboratorio;
import entities.PlanVacunacion;
import entities.Vacuna;
import exceptions.AccionInvalida;
import exceptions.EnfermedadInexistente;
import exceptions.EnfermedadRepetida;
import interfaces.IEnfermedadLocal;
import interfaces.IEnfermedadRemote;
import persistence.EtapaID;

@RunWith(Arquillian.class)
public class EnfermedadTest {
	private final Logger LOGGER = Logger.getLogger(getClass().getName());
	/*
	@Deployment
    public static JavaArchive createDeployment() {
        return ShrinkWrap.create(JavaArchive.class, "testEnf.jar")
            .addClasses(Enfermedad.class, EnfermedadRepetida.class, EnfermedadInexistente.class, AccionInvalida.class, IEnfermedadRemote.class, IEnfermedadLocal.class,  ControladorEnfermedad.class,
            		Vacuna.class, DtVacuna.class, PlanVacunacion.class, Etapa.class, DtEtapa.class, EtapaID.class,DtPlanVacunacion.class,DtEnfermedad.class, Laboratorio.class)
            .addAsResource("META-INF/test-persistence.xml", "META-INF/persistence.xml")
            ;
    }
	*/
	@EJB
	IEnfermedadRemote ce;
	@Resource
	UserTransaction utx;
	
	@PersistenceContext
	private EntityManager em;

	@OperateOnDeployment("normal")
	@Test(expected=EnfermedadInexistente.class)
	@InSequence(1)
	public void testListarEnfermedadNull() throws EnfermedadInexistente {
		//utx = (UserTransaction) em.getTransaction();
		ArrayList<DtEnfermedad> temp = new ArrayList<>();
		//temp.add(ce.obtenerEnfermedad("virus1"));
		ce.listarEnfermedades();
	}
	
	@OperateOnDeployment("normal")
	@Test
	@InSequence(2)
	public void testAgregarEnfermedad() throws EnfermedadRepetida {
		ce.agregarEnfermedad("virus1");
	   	ce.agregarEnfermedad("virus2");
	   	Query query1 = em.createQuery("SELECT e FROM Enfermedad e WHERE e.nombre = 'virus1'");
	   	Query query2 = em.createQuery("SELECT e FROM Enfermedad e WHERE e.nombre = 'virus2'");
	   	Enfermedad e = (Enfermedad)query1.getSingleResult();
	   	assertEquals(e.getNombre(), "virus1");
	   	e = (Enfermedad)query2.getSingleResult();
	   	Laboratorio l = new Laboratorio("lab1");
	   	try {
			utx.begin();
			em.persist(l);
		   	Vacuna v = new Vacuna("vac1", 1, 1, 1, l, e);
		   	em.persist(v);
		   	utx.commit();
		   	assertEquals(e.getNombre(), "virus2");
		} catch (NotSupportedException | SystemException | SecurityException | IllegalStateException | RollbackException | HeuristicMixedException | HeuristicRollbackException e1) {
			// TODO Auto-generated catch block
			
			e1.printStackTrace();
		}
	   	
	}
	
	@OperateOnDeployment("normal")
	@Test(expected=EnfermedadRepetida.class)
	@InSequence(3)
	public void testAgregarEnfermedadRepetida() throws EnfermedadRepetida {
	   ce.agregarEnfermedad("virus1");
	   Query queryV = em.createQuery("SELECT e FROM Enfermedad e WHERE e.nombre = 'virus1'");
	   Enfermedad e = (Enfermedad)queryV.getSingleResult();
	   assertEquals(e.getNombre(), "virus1");
	}
	
	@OperateOnDeployment("normal")
	@Test
	@InSequence(4)
	public void testObtenerEnfermedad() throws EnfermedadInexistente {
		assertEquals("virus1", ce.obtenerEnfermedad("virus1").getNombre());
	}
	
	@OperateOnDeployment("normal")
	@Test(expected=EnfermedadInexistente.class)
	@InSequence(5)
	public void testObtenerEnfermedadNull() throws EnfermedadInexistente {
		assertEquals("virus0", ce.obtenerEnfermedad("virus0").getNombre());
	}
	
	@OperateOnDeployment("normal")
	@Test
	@InSequence(6)
	public void testListarEnfermedad() throws EnfermedadInexistente {
		ArrayList<DtEnfermedad> temp = new ArrayList<>();
		temp.add(ce.obtenerEnfermedad("virus1"));
		temp.add(ce.obtenerEnfermedad("virus2"));
		assertArrayEquals(temp.toArray(), ce.listarEnfermedades().toArray());
	}
	
	@OperateOnDeployment("normal")
	@Test(expected=AssertionError.class)
	@InSequence(7)
	public void testListarEnfermedadError() throws EnfermedadInexistente {
		ArrayList<DtEnfermedad> temp = new ArrayList<>();
		temp.add(ce.obtenerEnfermedad("virus1"));
		assertArrayEquals(temp.toArray(), ce.listarEnfermedades().toArray());
	}
	
	@OperateOnDeployment("normal")
	@Test//(expected=AssertionError.class)
	@InSequence(8)
	public void testEliminarEnfermedad() throws EnfermedadInexistente, AccionInvalida {
		ce.eliminarEnfermedad("virus1");
	}
	
	@OperateOnDeployment("normal")
	@Test(expected=EnfermedadInexistente.class)
	@InSequence(9)
	public void testEliminarEnfermedadNull() throws EnfermedadInexistente, AccionInvalida {
		ce.eliminarEnfermedad("virus1");
	}
	
	@OperateOnDeployment("normal")
	@Test(expected=AccionInvalida.class)
	@InSequence(10)
	public void testEliminarEnfermedadConVacuna() throws EnfermedadInexistente, AccionInvalida {
		ce.eliminarEnfermedad("virus2");
	}
	
	@OperateOnDeployment("normal")
	@Test(expected=AccionInvalida.class)
	@InSequence(11)
	public void testEliminarEnfermedadConPlan() throws EnfermedadInexistente, AccionInvalida {
		try {
			utx.begin();
			PlanVacunacion pv = new PlanVacunacion("pv_n1", "pv_d1");
			Query queryE = em.createQuery("SELECT e FROM Enfermedad e WHERE e.nombre = 'virus2'");
			Enfermedad e = (Enfermedad) queryE.getSingleResult();
			pv.setEnfermedad(e);
			em.persist(pv);
			utx.commit();
		} catch (NotSupportedException | SystemException | SecurityException | IllegalStateException | RollbackException | HeuristicMixedException | HeuristicRollbackException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		ce.eliminarEnfermedad("virus2");
	}
	
	@OperateOnDeployment("normal")
	@Test
	@InSequence(50)
	public void testClean() {
		try {
			utx.begin();
			Query queryE = em.createQuery("SELECT e FROM Enfermedad e WHERE e.nombre = 'virus2'");
			Query queryPV = em.createQuery("SELECT pv FROM PlanVacunacion pv WHERE pv.id = '1'");
			Query queryV = em.createQuery("SELECT v FROM Vacuna v WHERE v.nombre = 'vac1'");
			Query queryL = em.createQuery("SELECT l FROM Laboratorio l WHERE l.nombre = 'lab1'");
			PlanVacunacion pv = (PlanVacunacion) queryPV.getSingleResult();
			Enfermedad e = (Enfermedad) queryE.getSingleResult();
			Vacuna v = (Vacuna) queryV.getSingleResult();
			Laboratorio l = (Laboratorio) queryL.getSingleResult();
			em.remove(pv);
			em.remove(v);
			em.remove(e);
			utx.commit();
		} catch (NotSupportedException | SystemException | SecurityException | IllegalStateException | RollbackException | HeuristicMixedException | HeuristicRollbackException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
