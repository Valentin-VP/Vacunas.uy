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
import entities.Reserva;
import entities.Transportista;
import exceptions.AccionInvalida;
import exceptions.ReservaInexistente;
import exceptions.TransportistaInexistente;
import exceptions.TransportistaRepetido;
import interfaces.IReservaDAORemote;
import interfaces.ITransportistaDaoRemote;
import persistence.ReservaID;

@RunWith(Arquillian.class)
public class TransportistaTest {
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
	ITransportistaDaoRemote ct;
	@Resource
	UserTransaction utx;
	
	@PersistenceContext
	private EntityManager em;
	
	@OperateOnDeployment("normal")
	@Test(expected=TransportistaInexistente.class)
	@InSequence(1)
	public void testListarTransportistasSinTransportista() throws TransportistaInexistente {
		ct.listarTransportistas();
	}
	
	@OperateOnDeployment("normal")
	@Test(expected=TransportistaInexistente.class)
	@InSequence(2)
	public void testSetUrlTransportistasSinTransportista() throws TransportistaInexistente {
		ct.setURLtoTransportista(5, "url");
	}
	
	@OperateOnDeployment("normal")
	@Test(expected=TransportistaInexistente.class)
	@InSequence(3)
	public void testObtenerTransportistasSinTransportista() throws TransportistaInexistente {
		ct.obtenerTransportista(5);
	}
	
	@OperateOnDeployment("normal")
	@Test(expected=TransportistaInexistente.class)
	@InSequence(4)
	public void testGenerarTokenSinTransportista() throws TransportistaInexistente, AccionInvalida {
		ct.generarTokenTransportista(5);
	}
	
	@OperateOnDeployment("normal")
	@Test(expected=TransportistaInexistente.class)
	@InSequence(5)
	public void testIsTokenCorrectoSinTransportista() throws TransportistaInexistente {
		ct.isTokenCorrecto(5, "token");
	}
	
	@OperateOnDeployment("normal")
	@Test
	@InSequence(6)
	public void testAgregarTransportista() throws TransportistaRepetido {
		ct.agregarTransportista(1, "urlTrans");
	}
	
	@OperateOnDeployment("normal")
	@Test
	@InSequence(7)
	public void testGenerarToken() throws TransportistaInexistente, AccionInvalida {
		ct.generarTokenTransportista(1);
	}
	
	@OperateOnDeployment("normal")
	@Test
	@InSequence(8)
	public void testIsTokenCorrectoFalse() throws TransportistaInexistente {
		assertEquals(ct.isTokenCorrecto(1, "token"), false);
	}
	
	@OperateOnDeployment("normal")
	@Test
	@InSequence(9)
	public void testIsTokenCorrectoTrue() throws TransportistaInexistente {
		Transportista trans = em.find(Transportista.class, 1);
		assertEquals(ct.isTokenCorrecto(1, trans.getToken()), true);
	}
	
	@OperateOnDeployment("normal")
	@Test
	@InSequence(10)
	public void testSetUrlTransportistas() throws TransportistaInexistente {
		ct.setURLtoTransportista(1, "urlTransNuevo");
	}
	
	@OperateOnDeployment("normal")
	@Test
	@InSequence(11)
	public void testObtenerTransportistas() throws TransportistaInexistente {
		ct.obtenerTransportista(1);
	}
	
	@OperateOnDeployment("normal")
	@Test
	@InSequence(12)
	public void testListarTransportistas() throws TransportistaInexistente {
		ct.listarTransportistas();
	}
	
	//@OperateOnDeployment("normal")
	//@Test(expected=TransportistaRepetido.class)
	//@InSequence(13)
	//public void testAgregarTransportistaRepetido() throws TransportistaRepetido {
	//	ct.agregarTransportista(1, "urlTrans");
	//}
	
	@OperateOnDeployment("normal")
	@Test
	@InSequence(20)
	public void testClean() {
		try {
			utx.begin();
			Transportista trans = em.find(Transportista.class, 1);
			em.remove(trans);
			utx.commit();
		}catch(SystemException | NotSupportedException | SecurityException | IllegalStateException | RollbackException | HeuristicMixedException | HeuristicRollbackException e) {
			e.printStackTrace();
		}
	}
	
}
