package init;

import static org.junit.Assert.assertEquals;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
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
import javax.persistence.Query;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.OperateOnDeployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.junit.InSequence;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

import controllers.ControladorReserva;
import datatypes.DtAsignado;
import datatypes.DtReserva;
import datatypes.Sexo;
import entities.Asignado;
import entities.Puesto;
import entities.Reserva;
import entities.Vacunador;
import entities.Vacunatorio;
import exceptions.FechaIncorrecta;
import exceptions.ReservaInexistente;
import exceptions.SinPuestosLibres;
import exceptions.UsuarioInexistente;
import exceptions.VacunadorSinAsignar;
import exceptions.VacunatorioNoCargadoException;
import interfaces.IControladorVacunadorRemote;
import interfaces.IReservaDAORemote;
import persistence.ReservaID;

@RunWith(Arquillian.class)
public class VacunadorTest {
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
	IControladorVacunadorRemote cv;
	@Resource
	UserTransaction utx;
	
	@PersistenceContext
	private EntityManager em;
	
	@OperateOnDeployment("normal")
	@Test(expected=UsuarioInexistente.class)
	@InSequence(1)
	public void testAsignarVacunadorVacunatorioSinVacunador() throws UsuarioInexistente, VacunatorioNoCargadoException, SinPuestosLibres, FechaIncorrecta {
		LocalDate tempDate = LocalDate.of(2021, 6, 16);
		LocalDate birthDate = LocalDate.of(1999, 4, 7);
		Sexo s1 = Sexo.valueOf("Femenino");
		Vacunador vac = new Vacunador(1, "vac1", "vac1", birthDate, "mail", null, s1);
		Vacunatorio vacunatorio1 = new Vacunatorio("vacunatorio1", "vacunatorio1", null, 23423567, 2.0f, 3.0f);
		try {
			utx.begin();
			em.persist(vac);
			em.persist(vacunatorio1);
			utx.commit();
		}catch(SystemException | SecurityException | IllegalStateException | RollbackException | HeuristicMixedException | HeuristicRollbackException | NotSupportedException e) {
			e.printStackTrace();
		}
		cv.asignarVacunadorAVacunatorio(0, "", tempDate);
	}
	
	@OperateOnDeployment("normal")
	@Test(expected=SinPuestosLibres.class)
	@InSequence(2)
	public void testAsignarVacunadorVacunatorioSinPuesto() throws UsuarioInexistente, VacunatorioNoCargadoException, SinPuestosLibres, FechaIncorrecta {
		LocalDate tempDate = LocalDate.of(2021, 7, 1);
		cv.asignarVacunadorAVacunatorio(1, "vacunatorio1", tempDate);
	}
	
	@OperateOnDeployment("normal")
	@Test(expected=VacunatorioNoCargadoException.class)
	@InSequence(3)
	public void testAsignarVacunadorVacunatorioSinVacunatorio() throws UsuarioInexistente, VacunatorioNoCargadoException, SinPuestosLibres, FechaIncorrecta {
		Query queryVac = em.createQuery("SELECT v FROM Vacunatorio v WHERE v.id='vacunatorio1'");
		Vacunatorio vacunatorio1 = (Vacunatorio)queryVac.getSingleResult();
		Puesto puesto1 = new Puesto("puesto1", vacunatorio1);
		ArrayList<Puesto> puestos = new ArrayList<>();
		puestos.add(puesto1);
		vacunatorio1.setPuesto(puestos);
		try {
			utx.begin();
			em.merge(vacunatorio1);
			//em.merge(puesto1);
			utx.commit();
		}catch(SystemException | NotSupportedException | SecurityException | IllegalStateException | RollbackException | HeuristicMixedException | HeuristicRollbackException e) {
			e.printStackTrace();
		}
		LocalDate tempDate = LocalDate.of(2021, 6, 16);
		cv.asignarVacunadorAVacunatorio(1, "vacunatorio2", tempDate);
	}
	
	@OperateOnDeployment("normal")
	@Test(expected=FechaIncorrecta.class)
	@InSequence(4)
	public void testAsignarVacunadorVacunatorioFechaIncorrecta() throws UsuarioInexistente, VacunatorioNoCargadoException, SinPuestosLibres, FechaIncorrecta {
		LocalDate tempDate = LocalDate.of(2021, 5, 4);
		cv.asignarVacunadorAVacunatorio(1, "vacunatorio1", tempDate);
	}
	
	@OperateOnDeployment("normal")
	@Test(expected=FechaIncorrecta.class)
	@InSequence(5)
	public void testAsignarVacunadorVacunatorioAsignado() throws UsuarioInexistente, VacunatorioNoCargadoException, SinPuestosLibres, FechaIncorrecta {
		Query queryPuesto = em.createQuery("SELECT p FROM Puesto p WHERE p.id='puesto1'");//deberia ser un find con puestoID
		Query queryVacunador = em.createQuery("SELECT v FROM Vacunador v WHERE v.id=1");
		Vacunador vacunador1 = (Vacunador)queryVacunador.getSingleResult();
		Puesto puesto1 = (Puesto)queryPuesto.getSingleResult();
		LocalDate tempDateAsig = LocalDate.of(2021, 7, 1);
		Asignado asignado1 = new Asignado(tempDateAsig, vacunador1, puesto1);
		List<Asignado> asignados = new ArrayList<>();
		asignados.add(asignado1);
		vacunador1.setAsignado(asignados);
		try {
			utx.begin();
			em.merge(asignado1);
			em.merge(vacunador1);
			utx.commit();
		}catch(SystemException | NotSupportedException | SecurityException | IllegalStateException | RollbackException | HeuristicMixedException | HeuristicRollbackException e) {
			e.printStackTrace();
		}
		cv.asignarVacunadorAVacunatorio(1, "vacunatorio1", tempDateAsig);
	}
	
	@OperateOnDeployment("normal")
	@Test
	@InSequence(6)
	public void testAsignarVacunadorVacunatorio() throws UsuarioInexistente, VacunatorioNoCargadoException, SinPuestosLibres, FechaIncorrecta {
		try {
			utx.begin();
			LocalDate tempDate = LocalDate.of(2021, 8, 6);
			cv.asignarVacunadorAVacunatorio(1, "vacunatorio1", tempDate);
			Query queryV = em.createQuery("SELECT v FROM Vacunador v WHERE v.id=1");
			Vacunador vacunador1 = (Vacunador)queryV.getSingleResult();
			Query queryP = em.createQuery("SELECT p FROM Puesto p WHERE p.id='puesto1'");//deberia ser find con puestoID
			Puesto puesto1 = (Puesto)queryP.getSingleResult();
			List<Asignado> asignados = vacunador1.getAsignado();
			tempDate = LocalDate.of(2021, 7, 1);
			for(Asignado a: asignados) {
				if(a.getFecha().equals(tempDate)) {
					Asignado compAsignado = new Asignado(tempDate, vacunador1, puesto1);	
					assertEquals(a.getFecha(), compAsignado.getFecha());//no se si esta bien
				}
			}
			utx.commit();
		}catch(SystemException | NotSupportedException | SecurityException | IllegalStateException | RollbackException | HeuristicMixedException | HeuristicRollbackException e) {
			e.printStackTrace();
		}
	}
	
	@OperateOnDeployment("normal")
	@Test(expected=UsuarioInexistente.class)
	@InSequence(7)
	public void testConsultarPuestoAsignadoVacunadorSinVacunador() throws UsuarioInexistente, VacunatorioNoCargadoException, VacunadorSinAsignar {
		LocalDate tempDate = LocalDate.of(2021, 7, 1);
		cv.consultarPuestoAsignadoVacunador(0, "", tempDate);
	}
	
	@OperateOnDeployment("normal")
	@Test(expected=VacunatorioNoCargadoException.class)
	@InSequence(8)
	public void testConsultarPuestoAsignadoVacunadorSinVacunatorio() throws UsuarioInexistente, VacunatorioNoCargadoException, VacunadorSinAsignar {
		LocalDate tempDate = LocalDate.of(2021, 7, 1);
		cv.consultarPuestoAsignadoVacunador(1, "vacunatorio2", tempDate);
	}
	
	@OperateOnDeployment("normal")
	@Test(expected=VacunadorSinAsignar.class)
	@InSequence(9)
	public void testConsultarPuestoAsignadoVacunadorSinAsignar() throws UsuarioInexistente, VacunatorioNoCargadoException, VacunadorSinAsignar {
		LocalDate tempDate = LocalDate.of(2021, 8, 8);
		cv.consultarPuestoAsignadoVacunador(1, "vacunatorio1", tempDate);
	}
	
	@OperateOnDeployment("normal")
	@Test(expected=VacunadorSinAsignar.class)
	@InSequence(10)
	public void testConsultarPuestoAsignadoVacunadorSinAsignarAlVacunatorio() throws UsuarioInexistente, VacunatorioNoCargadoException, VacunadorSinAsignar {
		LocalDate tempDate = LocalDate.of(2021, 7, 1);
		Vacunatorio vacunatorio2 = new Vacunatorio("vacunatorio2", "vacunatorio2", null, 26785433, 3.0f, 4.0f);
		try {
			utx.begin();
			em.persist(vacunatorio2);
			utx.commit();
		}catch(SystemException | NotSupportedException | SecurityException | IllegalStateException | RollbackException | HeuristicMixedException | HeuristicRollbackException e) {
			e.printStackTrace();
		}
		cv.consultarPuestoAsignadoVacunador(1, "vacunatorio2", tempDate);
	}
	
	@OperateOnDeployment("normal")
	@Test
	@InSequence(11)
	public void testConsultarPuestoAsignadoVacunador() throws UsuarioInexistente, VacunatorioNoCargadoException, VacunadorSinAsignar {
		LocalDate tempDate = LocalDate.of(2021, 7, 1);
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
		DtAsignado dta = new DtAsignado(tempDate.format(formatter), "puesto1");
		assertEquals(dta.getFecha(), cv.consultarPuestoAsignadoVacunador(1, "vacunatorio1", tempDate).getFecha());
		assertEquals(dta.getIdPuesto(), cv.consultarPuestoAsignadoVacunador(1, "vacunatorio1", tempDate).getIdPuesto());
	}
	
	@OperateOnDeployment("normal")
	@Test
	@InSequence(50)
	public void testClean() {
		try {
			utx.begin();
			Query queryVac = em.createQuery("SELECT v FROM Vacunador v WHERE v.id=1");
			Query queryV1 = em.createQuery("SELECT v FROM Vacunatorio v WHERE v.id='vacunatorio1'");
			Query queryV2 = em.createQuery("SELECT v FROM Vacunatorio v WHERE v.id='vacunatorio2'");

			Vacunador vac = (Vacunador)queryVac.getSingleResult();
			Vacunatorio v1 = (Vacunatorio)queryV1.getSingleResult();
			Vacunatorio v2 = (Vacunatorio)queryV2.getSingleResult();
			for(Asignado a: vac.getAsignado()) {
					em.remove(a);
			}
			for(Puesto p: v1.getPuesto()) {
				em.remove(p);
			}
			em.remove(vac);
			em.remove(v2);
			em.remove(v1);
			utx.commit();
		}catch(SystemException | NotSupportedException | SecurityException | IllegalStateException | RollbackException | HeuristicMixedException | HeuristicRollbackException e) {
			e.printStackTrace();
		}
	}
	
}
