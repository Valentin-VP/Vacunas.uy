//package init;
//
//import static org.junit.Assert.assertArrayEquals;
//import static org.junit.Assert.assertEquals;
//
//import java.time.LocalDate;
//import java.util.ArrayList;
//import java.util.logging.Logger;
//
//import javax.annotation.Resource;
//import javax.ejb.EJB;
//import javax.persistence.EntityManager;
//import javax.persistence.PersistenceContext;
//import javax.persistence.Query;
//import javax.transaction.HeuristicMixedException;
//import javax.transaction.HeuristicRollbackException;
//import javax.transaction.NotSupportedException;
//import javax.transaction.RollbackException;
//import javax.transaction.SystemException;
//import javax.transaction.UserTransaction;
//
//import org.jboss.arquillian.container.test.api.OperateOnDeployment;
//import org.jboss.arquillian.junit.Arquillian;
//import org.jboss.arquillian.junit.InSequence;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//
//import datatypes.DtVacuna;
//import entities.Enfermedad;
//import entities.Etapa;
//import entities.Laboratorio;
//import entities.LoteDosis;
//import entities.PlanVacunacion;
//import entities.Stock;
//import entities.Vacuna;
//import entities.Vacunatorio;
//import exceptions.AccionInvalida;
//import exceptions.EnfermedadInexistente;
//import exceptions.LaboratorioInexistente;
//import exceptions.VacunaInexistente;
//import exceptions.VacunaRepetida;
//import interfaces.IControladorVacunaRemote;
//
//@RunWith(Arquillian.class)
//public class VacunaTest {
//	
//	private final Logger LOGGER = Logger.getLogger(getClass().getName());
//	
//	@EJB
//	IControladorVacunaRemote cv;
//	@Resource
//	UserTransaction utx;
//	
//	@PersistenceContext
//	private EntityManager em;
//	
//	@OperateOnDeployment("normal")
//	@Test(expected=VacunaInexistente.class)
//	@InSequence(1)
//	public void testListarVacunasNull() throws VacunaInexistente {
//		ArrayList<DtVacuna> temp = new ArrayList<>();
//		Laboratorio lab1 = new Laboratorio("lab1");
//		Enfermedad enf1 = new Enfermedad("enf1");
//		Vacunatorio vacunatorio1 = new Vacunatorio("vacunatorio1", "Nest1", null, 1555897235, 1.0f, 1.0f);
//		//PlanVacunacion PVac = new PlanVacunacion(1, "a", "b");
//		try {
//			utx.begin();
//			em.persist(lab1);
//			em.persist(enf1);
//			//em.persist(PVac);
//			em.persist(vacunatorio1);
//			utx.commit();
//		} catch(SystemException | NotSupportedException | SecurityException | IllegalStateException | RollbackException | HeuristicMixedException | HeuristicRollbackException e) {
//			e.printStackTrace();
//		}
//		temp = cv.listarVacunas();
//	}
//	
//	@OperateOnDeployment("normal")
//	@Test(expected=LaboratorioInexistente.class)
//	@InSequence(2)
//	public void testAgregarVacunaLaboratorioInexistente() throws VacunaRepetida , LaboratorioInexistente, EnfermedadInexistente {
//		cv.agregarVacuna("vac1", 1, 1, 1, "", "");
//	}
//	
//	@OperateOnDeployment("normal")
//	@Test(expected=EnfermedadInexistente.class)
//	@InSequence(3)
//	public void testAgregarVacunaEnfermedadNoInexistente() throws VacunaRepetida , LaboratorioInexistente, EnfermedadInexistente {
//		cv.agregarVacuna("vac1", 1, 1, 1, "lab1", "");
//	}
//	
//	@OperateOnDeployment("normal")
//	@Test
//	@InSequence(4)
//	public void testAgregarVacuna() throws VacunaRepetida , LaboratorioInexistente, EnfermedadInexistente {
//		Query queryV1 = em.createQuery("SELECT v FROM Vacuna v WHERE v.nombre = 'vac1'");
//		Query queryV2 = em.createQuery("SELECT v FROM Vacuna v WHERE v.nombre = 'vac2'");
//		Query queryVac = em.createQuery("SELECT vac FROM Vacunatorio vac WHERE vac.id = 'vacunatorio1'");
//		cv.agregarVacuna("vac1", 1, 1, 1, "lab1", "enf1");
//		cv.agregarVacuna("vac2", 2, 2, 2, "lab1", "enf1");
//		Vacuna vac1 = (Vacuna)queryV1.getSingleResult();
//		assertEquals(vac1.getNombre(), "vac1");
//		Vacuna vac2 = (Vacuna)queryV2.getSingleResult();
//		Vacunatorio vacunatorio = (Vacunatorio)queryVac.getSingleResult();
//		LoteDosis lote = new LoteDosis(1, vacunatorio, vac2, 1, 1, 1, 1.0f);
//		LocalDate tempInicio = LocalDate.of(1,2,3);
//		LocalDate tempFinal = LocalDate.of(2, 3, 4);
//		PlanVacunacion pv = new PlanVacunacion("a", "b");
//		Etapa e = new Etapa(1, tempInicio, tempFinal, "a", pv);
//		Stock s = new Stock(vacunatorio, vac2, 1, 1, 1, 1);
//		e.setVacuna(vac2);
//		try {
//			utx.begin();
//			em.persist(lote);
//			em.persist(e);
//			em.persist(s);
//			utx.commit();
//		} catch(SystemException | NotSupportedException | SecurityException | IllegalStateException | RollbackException | HeuristicMixedException | HeuristicRollbackException e1) {
//			e1.printStackTrace();
//		}
//	}
//	
//	@OperateOnDeployment("normal")
//	@Test(expected=VacunaRepetida.class)
//	@InSequence(5)
//	public void testAgregarVacunaError() throws VacunaRepetida , LaboratorioInexistente, EnfermedadInexistente {
//		cv.agregarVacuna("vac1", 1, 1, 1, "lab1", "enf1");
//	}
//	
//	@OperateOnDeployment("normal")
//	@Test(expected=VacunaInexistente.class)
//	@InSequence(6)
//	public void testObtenerVacunaNull() throws VacunaInexistente {
//		DtVacuna dtTemp = new DtVacuna();
//		dtTemp = cv.obtenerVacuna("vac0");
//	}
//	
//	@OperateOnDeployment("normal")
//	@Test
//	@InSequence(7)
//	public void testObtenerVacuna() throws VacunaInexistente {
//		DtVacuna dtTemp = new DtVacuna();
//		dtTemp = cv.obtenerVacuna("vac1");
//	}
//	
//	@OperateOnDeployment("normal")
//	@Test
//	@InSequence(8)
//	public void testListarVacunas() throws VacunaInexistente {
//		ArrayList<DtVacuna> arrayTemp = new ArrayList<>();
//		arrayTemp.add(cv.obtenerVacuna("vac1"));
//		arrayTemp.add(cv.obtenerVacuna("vac2"));
//		assertArrayEquals(arrayTemp.toArray(), cv.listarVacunas().toArray());
//	}
//	
//	@OperateOnDeployment("normal")
//	@Test(expected=AssertionError.class)
//	@InSequence(9)
//	public void testListarVacunasError() throws VacunaInexistente{
//		ArrayList<DtVacuna> arrayTemp = new ArrayList<>();
//		arrayTemp.add(cv.obtenerVacuna("vac1"));
//		assertArrayEquals(arrayTemp.toArray(), cv.listarVacunas().toArray());
//	}
//	
//	
//	@OperateOnDeployment("normal")
//	@Test
//	@InSequence(10)
//	public void testModificarVacuna() throws VacunaInexistente {
//		cv.modificarVacuna("vac2", 3, 3, 3);
//	}
//	
//	@OperateOnDeployment("normal")
//	@Test(expected=VacunaInexistente.class)
//	@InSequence(11)
//	public void testEliminarVacunaNull() throws VacunaInexistente, AccionInvalida {
//		cv.eliminarVacuna("vac4");
//	}
//	
//	@OperateOnDeployment("normal")
//	@Test(expected=AccionInvalida.class)
//	@InSequence(12)
//	public void testEliminarVacunaConLote() throws VacunaInexistente, AccionInvalida {
//		Query queryLote = em.createQuery("SELECT lote FROM LoteDosis lote WHERE lote.vacuna='vac2'");
//		LoteDosis lote = (LoteDosis)queryLote.getSingleResult();
//		lote.setVacuna(null);
//		cv.eliminarVacuna("vac2");
//	}
//	
//	@OperateOnDeployment("normal")
//	@Test(expected=AccionInvalida.class)
//	@InSequence(13)
//	public void testEliminarVacunaConStock() throws VacunaInexistente, AccionInvalida {
//		Query queryS = em.createQuery("SELECT s FROM Stock s WHERE s.vacuna='vac2'");
//		Stock s = (Stock)queryS.getSingleResult();
//		s.setVacuna(null);
//		cv.eliminarVacuna("vac2");
//	}
//
//	@OperateOnDeployment("normal")
//	@Test(expected=AccionInvalida.class)
//	@InSequence(14)
//	public void testEliminarVacunaConEtapa() throws VacunaInexistente, AccionInvalida {
//		Query queryE = em.createQuery("SELECT e FROM Etapa e WHERE e.vacuna='vac2'");
//		Etapa e = (Etapa)queryE.getSingleResult();
//		e.setVacuna(null);
//		cv.eliminarVacuna("vac2");
//	}
//	
//	@OperateOnDeployment("normal")
//	@Test
//	@InSequence(15)
//	public void testEliminarVacuna() throws VacunaInexistente, AccionInvalida {
//		cv.eliminarVacuna("vac1");
//	}
//	
//	@OperateOnDeployment("normal")
//	@Test(expected=VacunaInexistente.class)
//	@InSequence(16)
//	public void testModificarVacunaError() throws VacunaInexistente {
//		cv.modificarVacuna("vac1", 2, 2, 2);
//	}
//	
//	@OperateOnDeployment("normal")
//	@Test
//	@InSequence(20)
//	public void testClean() {
//		Query queryVac = em.createQuery("SELECT vac FROM Vacunatorio vac WHERE vac.id = 'vacunatorio1'");
//		Query queryLab = em.createQuery("SELECT l FROM Laboratorio l WHERE l.nombre = 'lab1'");
//		Query queryEnf = em.createQuery("SELECT enf FROM Enfermedad enf WHERE enf.nombre = 'enf1'");
//		Query queryE = em.createQuery("SELECT e FROM Etapa e WHERE e.vacuna = '1'");
//		Query queryS = em.createQuery("SELECT s FROM Stock s WHERE s.vacunatorio = 'vacunatorio1'");
//		Query queryPv = em.createQuery("SELECT pv FROM PlanVacunacion pv WHERE pv.nombre = 'a'");
//		Query queryLote = em.createQuery("SELECT lote FROM LoteDosis lote WHERE lote.idLote = 1");
//		Vacunatorio vac = (Vacunatorio)queryVac.getSingleResult();
//		Laboratorio lab = (Laboratorio)queryLab.getSingleResult();
//		Enfermedad enf = (Enfermedad)queryEnf.getSingleResult();
//		Etapa e = (Etapa)queryE.getSingleResult();
//		Stock s = (Stock)queryS.getSingleResult();
//		PlanVacunacion pv = (PlanVacunacion)queryPv.getSingleResult();
//		LoteDosis ld = (LoteDosis)queryLote.getSingleResult();
//		try {
//			utx.begin();
//			em.remove(vac);
//			em.remove(lab);
//			em.remove(enf);
//			em.remove(e);
//			em.remove(s);
//			em.remove(pv);
//			em.remove(ld);
//			utx.commit();
//		} catch(SystemException | SecurityException | IllegalStateException | RollbackException | HeuristicMixedException | HeuristicRollbackException | NotSupportedException e1) {
//			e1.printStackTrace();
//		}
//	}
//	
//}
