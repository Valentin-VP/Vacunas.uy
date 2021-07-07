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

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import datatypes.DtDireccion;
import datatypes.DtReserva;
import entities.Reserva;
import entities.Vacunatorio;
import exceptions.AccionInvalida;
import exceptions.ReglasCuposCargadoException;
import exceptions.ReservaInexistente;
import exceptions.VacunatorioCargadoException;
import exceptions.VacunatorioNoCargadoException;
import exceptions.VacunatoriosNoCargadosException;
import interfaces.IControladorVacunatorioRemote;
import interfaces.IReservaDAORemote;
import persistence.ReservaID;

@RunWith(Arquillian.class)
public class VacunatorioTest {
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
	IControladorVacunatorioRemote cv;
	@Resource
	UserTransaction utx;
	
	@PersistenceContext
	private EntityManager em;
	
	@OperateOnDeployment("normal")
	@Test(expected=VacunatoriosNoCargadosException.class)
	@InSequence(1)
	public void testListarVacunatoriosSinVacunatorios() throws VacunatoriosNoCargadosException {
		cv.listarVacunatorio();
	}
	
	@OperateOnDeployment("normal")
	@Test(expected=VacunatorioNoCargadoException.class)
	@InSequence(2)
	public void testObtenerVacunatoriosSinVacunatorios() throws VacunatorioNoCargadoException {
		cv.obtenerVacunatorio("vacunatorioInexistente");
	}
	
	@OperateOnDeployment("normal")
	@Test(expected=VacunatorioNoCargadoException.class)
	@InSequence(3)
	public void testModificarVacunatoriosSinVacunatorios() throws VacunatorioNoCargadoException {
		cv.modificarVacunatorio("vacunatorioInexistente", "vac1", null, 23456587, 2.0f, 3.0f, "url");
	}
	
	@OperateOnDeployment("normal")
	@Test(expected=VacunatorioNoCargadoException.class)
	@InSequence(4)
	public void testGenerarTokenSinVacunatorios() throws VacunatorioNoCargadoException, AccionInvalida {
		cv.generarTokenVacunatorio("vacunatorioInexistente");
	}
	
	@OperateOnDeployment("normal")
	@Test(expected=VacunatorioNoCargadoException.class)
	@InSequence(5)
	public void testIsTokenCorrectoSinVacunatorios() throws VacunatorioNoCargadoException {
		cv.isTokenCorrecto("vacunatorioInexistente", "token");
	}
	
	@OperateOnDeployment("normal")
	@Test(expected=VacunatorioNoCargadoException.class)
	@InSequence(6)
	public void testSetUrlSinVacunatorios() throws VacunatorioNoCargadoException {
		cv.setURLtoVacunatorio("vacunatorioInexistente", "url");
	}
	
	@OperateOnDeployment("normal")
	@Test(expected=VacunatorioNoCargadoException.class)
	@InSequence(7)
	public void testSetReglasSinVacunatorio() throws VacunatorioNoCargadoException, ReglasCuposCargadoException {
		cv.agregarReglasCupos("vacunatorioInexistente", "vac1", 3456, LocalTime.now(), LocalTime.now().plusHours(8));
	}
	
	@OperateOnDeployment("normal")
	@Test
	@InSequence(8)
	public void testAgregarVacunatorio() throws VacunatorioCargadoException {
		cv.agregarVacunatorio("vacunatorio1", "vac1", null, 24587968, 2.0f, 3.0f, "urlVac");
	}
	
	@OperateOnDeployment("normal")
	@Test
	@InSequence(9)
	public void testSetUrl() throws VacunatorioNoCargadoException {
		cv.setURLtoVacunatorio("vacunatorio1", "urlVacNuevo");
	}
	
	@OperateOnDeployment("normal")
	@Test
	@InSequence(10)
	public void testSetReglas() throws VacunatorioNoCargadoException, ReglasCuposCargadoException {
		cv.agregarReglasCupos("vacunatorio1", "vac1", 3456, LocalTime.now(), LocalTime.now().plusHours(8));
	}
	
	@OperateOnDeployment("normal")
	@Test(expected=ReglasCuposCargadoException.class)
	@InSequence(11)
	public void testSetReglasConReglas() throws VacunatorioNoCargadoException, ReglasCuposCargadoException {
		cv.agregarReglasCupos("vacunatorio1", "vac1", 3456, LocalTime.now(), LocalTime.now().plusHours(8));
	}
	
	@OperateOnDeployment("normal")
	@Test
	@InSequence(12)
	public void testGenerarToken() throws VacunatorioNoCargadoException, AccionInvalida {
		cv.generarTokenVacunatorio("vacunatorio1");
	}
	
	@OperateOnDeployment("normal")
	@Test
	@InSequence(13)
	public void testIsTokenCorrecto() throws VacunatorioNoCargadoException {
		Vacunatorio vac1 = em.find(Vacunatorio.class, "vacunatorio1");
		cv.isTokenCorrecto("vacunatorio1", vac1.getToken());
	}
	
	@OperateOnDeployment("normal")
	@Test
	@InSequence(14)
	public void testObtenerVacunatorio() throws VacunatorioNoCargadoException {
		cv.obtenerVacunatorio("vacunatorio1");
	}
	
	@OperateOnDeployment("normal")
	@Test
	@InSequence(15)
	public void testListarVacunatorios() throws VacunatoriosNoCargadosException {
		cv.listarVacunatorio();
	}
	
	@OperateOnDeployment("normal")
	@Test
	@InSequence(16)
	public void testModificarVacunatorios() throws VacunatorioNoCargadoException {
		cv.modificarVacunatorio("vacunatorio1", "vacunatorio1", null, 25467698, 2.0f, 3.0f, "urlVac");
	}
	
	@OperateOnDeployment("normal")
	@Test(expected=VacunatorioCargadoException.class)
	@InSequence(17)
	public void testAgregarVacunatorioRepetido() throws VacunatorioCargadoException {
		cv.agregarVacunatorio("vacunatorio1", "vac1", null, 24587968, 2.0f, 3.0f, "urlVac");
	}
	
	@OperateOnDeployment("normal")
	@Test
	@InSequence(20)
	public void testClean() {
		try {
			utx.begin();
			Vacunatorio vac1 = em.find(Vacunatorio.class, "vacunatorio1");
			em.remove(vac1);
			utx.commit();
		}catch(SystemException | NotSupportedException | SecurityException | IllegalStateException | RollbackException | HeuristicMixedException | HeuristicRollbackException e) {
			e.printStackTrace();
		}
	}
	
}
