package init;

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
import datatypes.DtReglasCupos;
import datatypes.DtReserva;
import entities.ReglasCupos;
import entities.Reserva;
import exceptions.ReglasCuposCargadoException;
import exceptions.ReglasCuposNoCargadoException;
import exceptions.ReservaInexistente;
import interfaces.IControladorReglasCuposRemote;
import interfaces.IReservaDAORemote;
import persistence.ReservaID;

@RunWith(Arquillian.class)
public class ReglasCuposTest {
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
	IControladorReglasCuposRemote cr;
	@Resource
	UserTransaction utx;
	
	@PersistenceContext
	private EntityManager em;
//	
//	@OperateOnDeployment("normal")
//	@Test(expected=ReglasCuposNoCargadoException.class)
//	@InSequence(1)
//	public void testObtenerReglasCuposSinRegla() throws ReglasCuposNoCargadoException {
//		cr.obtenerReglasCupos("regla");
//	}
	
	@OperateOnDeployment("normal")
	@Test
	@InSequence(2)
	public void testAgregarReglasCupos() throws ReglasCuposCargadoException {
		cr.agregarReglasCupos("regla", 5, LocalTime.of(8, 30), LocalTime.of(20, 30));
	}
	
//	@OperateOnDeployment("normal")
//	@Test
//	@InSequence(3)
//	public void testObtenerReglasCupos() throws ReglasCuposNoCargadoException {
//		DtReglasCupos dtreglas = cr.obtenerReglasCupos("regla");
//	}
	
	@OperateOnDeployment("normal")
	@Test(expected=ReglasCuposCargadoException.class)
	@InSequence(4)
	public void testAgregarReglasCuposRepetido() throws ReglasCuposCargadoException {
		cr.agregarReglasCupos("regla", 5, LocalTime.of(8, 30), LocalTime.of(20, 30));
	}
	
}
