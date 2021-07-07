package init;

import static org.junit.Assert.assertTrue;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.logging.Logger;

import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
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
import datatypes.DtStock;
import entities.Enfermedad;
import entities.Historico;
import entities.Laboratorio;
import entities.ReglasCupos;
import entities.Reserva;
import entities.Stock;
import entities.Vacuna;
import entities.Vacunatorio;
import exceptions.CantidadNula;
import exceptions.ReservaInexistente;
import exceptions.StockVacunaVacunatorioExistente;
import exceptions.StockVacunaVacunatorioInexistente;
import exceptions.VacunaInexistente;
import exceptions.VacunatorioNoCargadoException;
import interfaces.IReservaDAORemote;
import interfaces.IStockDaoRemote;
import persistence.ReservaID;
import persistence.StockID;

@RunWith(Arquillian.class)
public class StockTest {
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
	IStockDaoRemote cs;
	
	@Resource
	UserTransaction utx;
	
	@PersistenceContext
	private EntityManager em;
	
	@OperateOnDeployment("normal")
	@Test(expected=CantidadNula.class)
	@InSequence(1)
	public void testAgregarStockCantidadIncorrecta() throws VacunatorioNoCargadoException, VacunaInexistente, CantidadNula, StockVacunaVacunatorioExistente {
		cs.agregarStock("vact1", "vac1", 0);
	}
	
	@OperateOnDeployment("normal")
	@Test(expected=VacunatorioNoCargadoException.class)
	@InSequence(3)
	public void testAgregarStockSinVacunatorio() throws VacunatorioNoCargadoException, VacunaInexistente, CantidadNula, StockVacunaVacunatorioExistente {
		cs.agregarStock("vact0", "vac1", 1);
	}
	
	@OperateOnDeployment("normal")
	@Test(expected=VacunaInexistente.class)
	@InSequence(5)
	public void testAgregarStockSinVacuna() throws VacunatorioNoCargadoException, VacunaInexistente, CantidadNula, StockVacunaVacunatorioExistente {
		try {
			utx.begin();
			ReglasCupos rc1 = new ReglasCupos("rc1", 15,  LocalTime.of(0, 0, 0),  LocalTime.of(23, 59, 59));
			Vacunatorio vact1 = new Vacunatorio("vact1", "vact1_n", null, 0, 0F, 0F);
			vact1.setReglasCupos(rc1);
			em.persist(rc1);
			em.persist(vact1);
			utx.commit();
			cs.agregarStock("vact1", "vac0", 1);
		} catch (NotSupportedException | SystemException | SecurityException | IllegalStateException | RollbackException | HeuristicMixedException | HeuristicRollbackException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@OperateOnDeployment("normal")
	@Test(expected=StockVacunaVacunatorioInexistente.class)
	@InSequence(7)
	public void testModificarStockSinStock() throws VacunatorioNoCargadoException, VacunaInexistente, StockVacunaVacunatorioInexistente{
		try {
			utx.begin();
			Enfermedad e1 = new Enfermedad("virus1");
			Laboratorio l = new Laboratorio("lab1");
			Vacuna v = new Vacuna("vac1", 1, 1, 1, l, e1);
			em.persist(e1);
			em.persist(l);
			em.persist(v);
			utx.commit();
			cs.modificarStock("vact1", "vac1", 100, 0, 0, 100);
		} catch (NotSupportedException | SystemException | SecurityException | IllegalStateException | RollbackException | HeuristicMixedException | HeuristicRollbackException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@OperateOnDeployment("normal")
	@Test(expected=StockVacunaVacunatorioInexistente.class)
	@InSequence(9)
	public void testObtenerStockSinStock() throws VacunatorioNoCargadoException, VacunaInexistente, StockVacunaVacunatorioInexistente {
		cs.obtenerStock("vact1", "vac1");
	}
	
	@OperateOnDeployment("normal")
	@Test
	@InSequence(11)
	public void testAgregarStock() throws VacunatorioNoCargadoException, VacunaInexistente, CantidadNula, StockVacunaVacunatorioExistente {
		cs.agregarStock("vact1", "vac1", 100);
	}
	
	@OperateOnDeployment("normal")
	@Test(expected=StockVacunaVacunatorioExistente.class)
	@InSequence(13)
	public void testAgregarStockRepetido() throws VacunatorioNoCargadoException, VacunaInexistente, CantidadNula, StockVacunaVacunatorioExistente {
		cs.agregarStock("vact1", "vac1", 100);
	}
	
	@OperateOnDeployment("normal")
	@Test(expected=VacunaInexistente.class)
	@InSequence(15)
	public void testModificarStockSinVacuna() throws VacunatorioNoCargadoException, VacunaInexistente, StockVacunaVacunatorioInexistente {
		cs.modificarStock("vact1", "vac0", 100, 0, 0, 100);
	}
	
	@OperateOnDeployment("normal")
	@Test(expected=VacunatorioNoCargadoException.class)
	@InSequence(17)
	public void testModificarStockSinVacunatorio() throws VacunatorioNoCargadoException, VacunaInexistente, StockVacunaVacunatorioInexistente {
		cs.modificarStock("vact0", "vac1", 100, 0, 0, 100);
	}
	
	@OperateOnDeployment("normal")
	@Test
	@InSequence(19)
	public void testModificarStock() throws VacunatorioNoCargadoException, VacunaInexistente, StockVacunaVacunatorioInexistente {
		cs.modificarStock("vact1", "vac1", 200, 0, 0, 200);
	}
	
	@OperateOnDeployment("normal")
	@Test
	@InSequence(21)
	public void testObtenerStock() throws VacunatorioNoCargadoException, VacunaInexistente, StockVacunaVacunatorioInexistente {
		assertTrue(cs.obtenerStock("vact1", "vac1")!=null);
	}
	
	@OperateOnDeployment("normal")
	@Test
	@InSequence(23)
	public void testObtenerStockConHistorico() throws VacunatorioNoCargadoException, VacunaInexistente, StockVacunaVacunatorioInexistente {
		try {
			utx.begin();
			Stock s = em.find(Stock.class, new StockID("vact1", "vac1"));
			//LocalDate fecha, Integer cantidad, Integer descartadas, Integer disponibles,
			//Integer administradas, Stock stock
			Historico h = new Historico(LocalDate.now(), 100,0,100,0, s);
			s.getHistoricos().add(h);
			em.merge(s);
			utx.commit();
			assertTrue(cs.obtenerStock("vact1", "vac1")!=null);
		} catch (NotSupportedException | SystemException | SecurityException | IllegalStateException | RollbackException | HeuristicMixedException | HeuristicRollbackException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@OperateOnDeployment("normal")
	@Test
	@InSequence(25)
	public void testStockActualTodos() throws VacunatorioNoCargadoException, VacunaInexistente, StockVacunaVacunatorioInexistente {
		assertTrue(cs.getStockActual("virus1", "vac1", "vact1").size()==1);
	}
	
	@OperateOnDeployment("normal")
	@Test
	@InSequence(27)
	public void testStockActualSoloEnfermedad() throws VacunatorioNoCargadoException, VacunaInexistente, StockVacunaVacunatorioInexistente {
		assertTrue(cs.getStockActual("virus1", "", "").size()==1);
	}
	
	@OperateOnDeployment("normal")
	@Test
	@InSequence(29)
	public void testStockActualEnfermedadVacuna() throws VacunatorioNoCargadoException, VacunaInexistente, StockVacunaVacunatorioInexistente {
		assertTrue(cs.getStockActual("virus1", "vac1", "").size()==1);
	}
	
	@OperateOnDeployment("normal")
	@Test
	@InSequence(31)
	public void testStockActualEnfermedadVacunatorio() throws VacunatorioNoCargadoException, VacunaInexistente, StockVacunaVacunatorioInexistente {
		assertTrue(cs.getStockActual("virus1", "", "vact1").size()==1);
	}
	
	@OperateOnDeployment("normal")
	@Test
	@InSequence(33)
	public void testStockActualInexistente() throws VacunatorioNoCargadoException, VacunaInexistente, StockVacunaVacunatorioInexistente {
		assertTrue(cs.getStockActual("virus2", "vac2", "vact2").isEmpty());
	}
	
	@OperateOnDeployment("normal")
	@Test
	@InSequence(35)
	public void testGetHistoricoSoloUno() throws VacunatorioNoCargadoException, VacunaInexistente, StockVacunaVacunatorioInexistente {
		assertTrue(cs.getHistoricoStock("virus1", "vac1", "vact1", LocalDate.now().minusMonths(12), LocalDate.now()).size()==1);
	}
	
	@OperateOnDeployment("normal")
	@Test
	@InSequence(37)
	public void testGetHistoricoDosEnMismoMes() throws VacunatorioNoCargadoException, VacunaInexistente, StockVacunaVacunatorioInexistente {
		try {
			utx.begin();
			Stock s = em.find(Stock.class, new StockID("vact1", "vac1"));
			//LocalDate fecha, Integer cantidad, Integer descartadas, Integer disponibles,
			//Integer administradas, Stock stock
			Historico h = new Historico(LocalDate.now().minusDays(1), 100,0,100,0, s);
			s.getHistoricos().add(h);
			em.merge(s);
			utx.commit();
			assertTrue(cs.getHistoricoStock("virus1", "vac1", "vact1", LocalDate.now().minusMonths(12), LocalDate.now()).size()==1);
		} catch (NotSupportedException | SystemException | SecurityException | IllegalStateException | RollbackException | HeuristicMixedException | HeuristicRollbackException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@OperateOnDeployment("normal")
	@Test
	@InSequence(500)
	public void testClean() {
		try {
			utx.begin();
			em.remove(em.find(Vacunatorio.class, "vact1"));
			em.remove(em.find(ReglasCupos.class, "rc1"));
			em.remove(em.find(Vacuna.class, "vac1"));
			em.remove(em.find(Laboratorio.class, "lab1"));
			em.remove(em.find(Enfermedad.class, "virus1"));
			utx.commit();
		} catch (NotSupportedException | SystemException | SecurityException | IllegalStateException | RollbackException | HeuristicMixedException | HeuristicRollbackException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
