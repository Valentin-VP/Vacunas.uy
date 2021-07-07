package init;

import static org.junit.Assert.assertNull;
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
import datatypes.DtHistoricoStock;
import datatypes.DtReserva;
import entities.Enfermedad;
import entities.Laboratorio;
import entities.ReglasCupos;
import entities.Reserva;
import entities.Stock;
import entities.Vacuna;
import entities.Vacunatorio;
import exceptions.ReservaInexistente;
import exceptions.StockVacunaVacunatorioInexistente;
import exceptions.VacunaInexistente;
import exceptions.VacunatorioNoCargadoException;
import interfaces.IHistoricoDaoRemote;
import interfaces.IReservaDAORemote;
import persistence.ReservaID;

@RunWith(Arquillian.class)
public class HistoricoTest {
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
	IHistoricoDaoRemote ch;
	
	@Resource
	UserTransaction utx;
	
	@PersistenceContext
	private EntityManager em;
	
	@OperateOnDeployment("normal")
	@Test
	@InSequence(1)
	public void testObtenerHistoricoNull() throws VacunatorioNoCargadoException, VacunaInexistente, StockVacunaVacunatorioInexistente {
		assertNull(ch.obtenerHistorico(LocalDate.now(), "vact1", "vac1"));
	}
	
	@OperateOnDeployment("normal")
	@Test(expected=VacunatorioNoCargadoException.class)
	@InSequence(3)
	public void testPersistirHistoricoSinVacunatorio() throws VacunatorioNoCargadoException, VacunaInexistente, StockVacunaVacunatorioInexistente {
		ch.persistirHistorico(LocalDate.now(), 100, 0, 100, 0, "vact0", "vac1");
	}
	
	@OperateOnDeployment("normal")
	@Test(expected=VacunaInexistente.class)
	@InSequence(5)
	public void testPersistirHistoricoSinVacuna() throws VacunatorioNoCargadoException, VacunaInexistente, StockVacunaVacunatorioInexistente {
		try {
			utx.begin();
			ReglasCupos rc1 = new ReglasCupos("rc1", 15,  LocalTime.of(0, 0, 0),  LocalTime.of(23, 59, 59));
			Vacunatorio vact1 = new Vacunatorio("vact1", "vact1_n", null, 0, 0F, 0F);
			vact1.setReglasCupos(rc1);
			em.persist(rc1);
			em.persist(vact1);
			utx.commit();
			ch.persistirHistorico(LocalDate.now(), 100, 0, 100, 0, "vact1", "vac0");
		} catch (NotSupportedException | SystemException | SecurityException | IllegalStateException | RollbackException | HeuristicMixedException | HeuristicRollbackException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@OperateOnDeployment("normal")
	@Test
	@InSequence(7)
	public void testPersistirHistorico() throws VacunatorioNoCargadoException, VacunaInexistente, StockVacunaVacunatorioInexistente {
		try {
			utx.begin();
			Enfermedad e1 = new Enfermedad("virus1");
			Laboratorio l = new Laboratorio("lab1");
			Vacuna v = new Vacuna("vac1", 1, 1, 1, l, e1);
			Vacunatorio vact1 = em.find(Vacunatorio.class, "vact1");
			Stock s = new Stock(vact1, v, 100, 0, 0, 100);
			vact1.getStock().add(s);
			em.persist(e1);
			em.persist(l);
			em.persist(v);
			utx.commit();
			ch.persistirHistorico(LocalDate.now(), 100, 0, 100, 0, "vact1", "vac1");
		} catch (NotSupportedException | SystemException | SecurityException | IllegalStateException | RollbackException | HeuristicMixedException | HeuristicRollbackException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@OperateOnDeployment("normal")
	@Test
	@InSequence(9)
	public void testPersistirHistoricoMismoDia() throws VacunatorioNoCargadoException, VacunaInexistente, StockVacunaVacunatorioInexistente {
		ch.persistirHistorico(LocalDate.now(), 50, 0, 50, 0, "vact1", "vac1");
	}
	
	@OperateOnDeployment("normal")
	@Test
	@InSequence(11)
	public void testPersistirHistoricoOtroDia() throws VacunatorioNoCargadoException, VacunaInexistente, StockVacunaVacunatorioInexistente {
		ch.persistirHistorico(LocalDate.now().minusDays(1), 500, 0, 500, 0, "vact1", "vac1");
	}
	
	@OperateOnDeployment("normal")
	@Test
	@InSequence(13)
	public void testObtenerHistorico() throws VacunatorioNoCargadoException, VacunaInexistente, StockVacunaVacunatorioInexistente {
		DtHistoricoStock h = ch.obtenerHistorico(LocalDate.now(), "vact1", "vac1");
		assertTrue(h.getDisponibles()==150);
	}
	
	@OperateOnDeployment("normal")
	@Test
	@InSequence(13)
	public void testObtenerHistoricoOtroDia() throws VacunatorioNoCargadoException, VacunaInexistente, StockVacunaVacunatorioInexistente {
		DtHistoricoStock h = ch.obtenerHistorico(LocalDate.now().minusDays(1), "vact1", "vac1");
		assertTrue(h.getDisponibles()==500);
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
