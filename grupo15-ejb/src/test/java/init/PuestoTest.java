package init;

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
import datatypes.DtDireccion;
import datatypes.DtReserva;
import entities.Puesto;
import entities.Reserva;
import entities.Vacunatorio;
import exceptions.PuestoCargadoException;
import exceptions.PuestoNoCargadoException;
import exceptions.PuestoNoCargadosException;
import exceptions.ReservaInexistente;
import exceptions.VacunatorioNoCargadoException;
import interfaces.IControladorPuestoRemote;
import interfaces.IReservaDAORemote;
import persistence.PuestoID;
import persistence.ReservaID;

@RunWith(Arquillian.class)
public class PuestoTest {
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
	IControladorPuestoRemote cp;
	@Resource
	UserTransaction utx;
	
	@PersistenceContext
	private EntityManager em;
	
	@OperateOnDeployment("normal")
	@Test(expected=PuestoNoCargadosException.class)
	@InSequence(1)
	public void testListarPuestoSinPuesto() throws PuestoNoCargadosException {
		Vacunatorio vacunatorio = new Vacunatorio("vacunatorio1", "vacunatorio", null, 2345678, 2.0f, 3.0f);
		try {
			utx.begin();
			em.persist(vacunatorio);
			utx.commit();
		}catch(SystemException | NotSupportedException | SecurityException | IllegalStateException | RollbackException | HeuristicMixedException | HeuristicRollbackException e) {
			e.printStackTrace();
		}
		cp.listarPuestos("vacunatorio0");
	}
	
//	@OperateOnDeployment("normal")
//	@Test(expected=PuestoNoCargadoException.class)
//	@InSequence(2)
//	public void testObtenerPuestoSinPuesto() throws PuestoNoCargadoException, VacunatorioNoCargadoException {
//		cp.obtenerPuesto("puesto0", "vacunatorio1");
//	}
	
	@OperateOnDeployment("normal")
	@Test(expected=VacunatorioNoCargadoException.class)
	@InSequence(3)
	public void testAgregarPuestoSinVacunatorio() throws PuestoCargadoException, VacunatorioNoCargadoException {
		cp.agregarPuesto("puesto1", "vacunatorio0");
	}
	
	
	@OperateOnDeployment("normal")
	@Test
	@InSequence(4)
	public void testAgregarPuesto() throws PuestoCargadoException, VacunatorioNoCargadoException {
		try {
			utx.begin();
			cp.agregarPuesto("puesto1", "vacunatorio1");
			utx.commit();
		}catch(SystemException | NotSupportedException | SecurityException | IllegalStateException | RollbackException | HeuristicMixedException | HeuristicRollbackException e){
			e.printStackTrace();
		}
	}
	
	@OperateOnDeployment("normal")
	@Test
	@InSequence(5)
	public void testListarPuestos() throws PuestoNoCargadosException {
		cp.listarPuestos("vacunatorio1");
	}
	
//	@OperateOnDeployment("normal")
//	@Test
//	@InSequence(6)
//	public void testObtenerPuesto() throws VacunatorioNoCargadoException, PuestoNoCargadoException {
//		try {
//			utx.begin();
//			cp.obtenerPuesto("puesto1", "vacunatorio1");
//			utx.commit();
//		}catch(SystemException | NotSupportedException | SecurityException | IllegalStateException | RollbackException | HeuristicMixedException | HeuristicRollbackException e){
//			e.printStackTrace();
//		}
//	}
//	
//	@OperateOnDeployment("normal")
//	@Test(expected=VacunatorioNoCargadoException.class)
//	@InSequence(7)
//	public void testObtenerPuestoSinVacunatorio() throws PuestoNoCargadoException, VacunatorioNoCargadoException {
//		cp.obtenerPuesto("puesto1", "vacunatorio0");
//	}
	
	@OperateOnDeployment("normal")
	@Test(expected=PuestoCargadoException.class)
	@InSequence(8)
	public void testAgregarPuestoRepetido() throws PuestoCargadoException, VacunatorioNoCargadoException {
		cp.agregarPuesto("puesto1", "vacunatorio1");
	}
	
	@OperateOnDeployment("normal")
	@Test
	@InSequence(10)
	public void testClean() {
		try {
			utx.begin();
			Vacunatorio vacunatorio = em.find(Vacunatorio.class, "vacunatorio1");
			Puesto puesto = em.find(Puesto.class, new PuestoID("puesto1", "vacunatorio1"));
			em.remove(puesto);
			em.remove(vacunatorio);
			utx.commit();
		}catch(SystemException | NotSupportedException | SecurityException | IllegalStateException | RollbackException | HeuristicMixedException | HeuristicRollbackException e) {
			e.printStackTrace();
		}
	}
	
}
