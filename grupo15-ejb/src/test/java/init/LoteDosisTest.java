package init;

import static org.junit.Assert.assertEquals;

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
import entities.Enfermedad;
import entities.Laboratorio;
import entities.LoteDosis;
import entities.Reserva;
import entities.Transportista;
import entities.Vacuna;
import entities.Vacunatorio;
import exceptions.LoteInexistente;
import exceptions.LoteRepetido;
import exceptions.ReservaInexistente;
import exceptions.TransportistaInexistente;
import exceptions.VacunaInexistente;
import exceptions.VacunatorioNoCargadoException;
import interfaces.ILoteDosisDaoRemote;
import interfaces.IReservaDAORemote;
import persistence.LoteDosisID;
import persistence.ReservaID;

@RunWith(Arquillian.class)
public class LoteDosisTest {
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
	ILoteDosisDaoRemote cl;
	@Resource
	UserTransaction utx;
	
	@PersistenceContext
	private EntityManager em;
	
	@OperateOnDeployment("normal")
	@Test(expected=LoteInexistente.class)
	@InSequence(1)
	public void testObtenerLoteDosisSinLote() throws LoteInexistente {
		Vacunatorio vacunatorio = new Vacunatorio("vacunatorio1", "vacunatorio", null, 24564357, 2.0f, 5.0f);
		Laboratorio lab = new Laboratorio("lab1");
		Enfermedad enf = new Enfermedad("enf1");
		Vacuna vacuna = new Vacuna("vacuna1", 1, 4, 5, lab, enf);
		try {
			utx.begin();
			em.persist(enf);
			em.persist(lab);
			em.persist(vacuna);
			em.persist(vacunatorio);
			utx.commit();
		}catch(SystemException | NotSupportedException | SecurityException | IllegalStateException | RollbackException | HeuristicMixedException | HeuristicRollbackException e) {
			e.printStackTrace();
		}
		cl.obtenerLoteDosis(1, "vacunatorio1", "vacuna1");
	}
	
	@OperateOnDeployment("normal")
	@Test(expected=VacunatorioNoCargadoException.class)
	@InSequence(2)
	public void testAgregarLoteDosisSinVacunatorio() throws LoteRepetido, VacunatorioNoCargadoException, VacunaInexistente {
		cl.agregarLoteDosis(1, "vacunatorio0", "vacuna1", 6);
	}
	
	@OperateOnDeployment("normal")
	@Test(expected=VacunaInexistente.class)
	@InSequence(3)
	public void testAgregarLoteDosisSinVacuna() throws LoteRepetido, VacunatorioNoCargadoException, VacunaInexistente {
		cl.agregarLoteDosis(1, "vacunatorio1", "vacuna0", 7);
	}
	
	@OperateOnDeployment("normal")
	@Test
	@InSequence(4)
	public void testAgregarLoteDosis() throws LoteRepetido, VacunatorioNoCargadoException, VacunaInexistente {
		cl.agregarLoteDosis(1, "vacunatorio1", "vacuna1", 19);
		//LoteDosis lotePrueba = new LoteDosis(1, em.find(Vacunatorio.class, "vacunatorio1"), em.find(Vacuna.class, "vacuna1"), 4, 7, 0, 20.0f);
	}
	
	@OperateOnDeployment("normal")
	@Test(expected=TransportistaInexistente.class)
	@InSequence(5)
	public void testSetTransportistaLoteDosisSinTransportista() throws TransportistaInexistente, VacunatorioNoCargadoException, VacunaInexistente {
		Transportista trans = new Transportista(1);
		try {
			utx.begin();
			em.persist(trans);
			utx.commit();
		}catch(SystemException | NotSupportedException | SecurityException | IllegalStateException | RollbackException | HeuristicMixedException | HeuristicRollbackException e) {
			e.printStackTrace();
		}
		cl.setTransportistaToLoteDosis(0, 1, "vacunatorio1", "vacuna1");
	}
	
	@OperateOnDeployment("normal")
	@Test
	@InSequence(6)
	public void testSetTransportistaLoteDosis() throws TransportistaInexistente, VacunatorioNoCargadoException, VacunaInexistente {
		cl.setTransportistaToLoteDosis(1, 1, "vacunatorio1", "vacuna1");
	}
	
	//@OperateOnDeployment("normal")
	//@Test(expected=LoteRepetido.class)
	//@InSequence(7)
	//public void testAgregarLoteDosisRepetido() throws LoteRepetido, VacunatorioNoCargadoException, VacunaInexistente {
	//	cl.agregarLoteDosis(1, "vacunatorio1", "vacuna1", 68);
	//}
	
	@OperateOnDeployment("normal")
	@Test
	@InSequence(8)
	public void testObtenerLoteDosis() throws LoteInexistente {
		LoteDosis lotePrueba = new LoteDosis(1, em.find(Vacunatorio.class, "vacunatorio1"), em.find(Vacuna.class, "vacuna1"), 4, 7, 0, 20.0f);
		assertEquals(lotePrueba.getIdLote(), cl.obtenerLoteDosis(1, "vacunatorio1", "vacuna1").getIdLote());
	}
	
//	@OperateOnDeployment("normal")
//	@Test
//	@InSequence(9)
//	public void testGetTransportistaLoteDosis() throws VacunatorioNoCargadoException, VacunaInexistente {
//		cl.getTransportistaIdFromLoteDosis(1, "vacunatorio1", "vacuna1");
//	}
	
	@OperateOnDeployment("normal")
	@Test(expected=LoteInexistente.class)
	@InSequence(10)
	public void testModificarLoteDosisSinLote() throws LoteInexistente, TransportistaInexistente, VacunatorioNoCargadoException, VacunaInexistente {
		cl.modificarLoteDosis(0, "vacunatorio1", "vacuna1", 7, 7, 0, "Recibido", 20.0f, 1);
	}
	
	@OperateOnDeployment("normal")
	@Test
	@InSequence(11)
	public void testListarLoteDosis() {
		cl.listarLotesDosis();
	}
	
	@OperateOnDeployment("normal")
	@Test(expected=TransportistaInexistente.class)
	@InSequence(12)
	public void testModificarLoteDosisSinTransportista() throws LoteInexistente, TransportistaInexistente, VacunatorioNoCargadoException, VacunaInexistente {
		cl.modificarLoteDosis(1, "vacunatorio1", "vacuna1", 7, 7, 0, "Recibido", 20.0f, 0);
	}
	
	@OperateOnDeployment("normal")
	@Test
	@InSequence(13)
	public void testModificarLoteDosis() throws LoteInexistente, TransportistaInexistente, VacunatorioNoCargadoException, VacunaInexistente {
		cl.modificarLoteDosis(1, "vacunatorio1", "vacuna1", 7, 7, 0, "Recibido", 20.0f, 1);
	}
	
	@OperateOnDeployment("normal")
	@Test
	@InSequence(20)
	public void testClean() {//ojo con el orden
		try {
			utx.begin();
			Laboratorio lab1 = em.find(Laboratorio.class, "lab1");
			Enfermedad enf1 = em.find(Enfermedad.class, "enf1");
			em.remove(enf1);
			em.remove(lab1);
			LoteDosis lote = em.find(LoteDosis.class, new LoteDosisID(1, "vacunatorio1", "vacuna1"));
			em.remove(lote);
			Vacuna vac = em.find(Vacuna.class, "vacuna1");
			em.remove(vac);
			Vacunatorio vacunatorio = em.find(Vacunatorio.class, "vacunatorio1");
			em.remove(vacunatorio);
			Transportista trans = em.find(Transportista.class, 1);
			em.remove(trans);
			utx.commit();
		}catch(SystemException | NotSupportedException | SecurityException | IllegalStateException | RollbackException | HeuristicMixedException | HeuristicRollbackException e) {
			e.printStackTrace();
		}
	}
	
}
