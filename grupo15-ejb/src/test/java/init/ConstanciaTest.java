package init;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
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
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.junit.InSequence;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

import controllers.ControladorReserva;
import datatypes.DtConstancia;
import datatypes.DtDireccion;
import datatypes.DtReserva;
import datatypes.EstadoReserva;
import datatypes.Sexo;
import entities.CertificadoVacunacion;
import entities.Ciudadano;
import entities.ConstanciaVacuna;
import entities.Enfermedad;
import entities.Etapa;
import entities.Laboratorio;
import entities.PlanVacunacion;
import entities.Puesto;
import entities.Reserva;
import entities.Vacuna;
import entities.Vacunatorio;
import exceptions.CertificadoInexistente;
import exceptions.ConstanciaInexistente;
import exceptions.ReservaInexistente;
import exceptions.UsuarioExistente;
import interfaces.IConstanciaVacunaDAORemote;
import interfaces.IReservaDAORemote;
import persistence.PuestoID;
import persistence.ReservaID;

@RunWith(Arquillian.class)
public class ConstanciaTest {
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
	IConstanciaVacunaDAORemote cv;
	@Resource
	UserTransaction utx;
	
	@PersistenceContext
	private EntityManager em;
	
//	@OperateOnDeployment("normal")
//	@Test(expected=ConstanciaInexistente.class)
//	@InSequence()
//	public void testModificarConstanciaSinConstancia() throws UsuarioExistente, ReservaInexistente, CertificadoInexistente, ConstanciaInexistente {
//		
//	}
	
	@OperateOnDeployment("normal")
	@Test(expected=ConstanciaInexistente.class)
	@InSequence(1)
	public void testListarConstanciasSinConstancia() throws ConstanciaInexistente {
		cv.listarConstancias();
	}
	
	@OperateOnDeployment("normal")
	@Test(expected=UsuarioExistente.class)
	@InSequence(2)
	public void testAgregarConstanciaSinUsuario() throws UsuarioExistente, ReservaInexistente, CertificadoInexistente {
		cv.agregarConstanciaVacuna("vacuna", 6, 1, LocalDate.of(2021, 3, 1), 1, 1);
	}
	
	@OperateOnDeployment("normal")
	@Test(expected=ReservaInexistente.class)
	@InSequence(3)
	public void testAgregarConstanciaSinResrva() throws UsuarioExistente, ReservaInexistente, CertificadoInexistente {
		try {
			utx.begin();
			Laboratorio lab = new Laboratorio("lab");
			Enfermedad enf = new Enfermedad("enf");
			em.persist(enf);
			em.persist(lab);
			Vacuna vacuna = new Vacuna("vacuna1", 1, 2, 6, lab, enf);
			em.persist(vacuna);
			
			LocalDate tempFechaNac = LocalDate.of(1998, 3, 5);
			Ciudadano c = new Ciudadano(1, "c1", "Lopez", tempFechaNac, "correo", null, Sexo.Otro, "sector", true);
			em.persist(c);
			ArrayList<Reserva> reservas = new ArrayList<>();
			c.setReservas(reservas);
			em.merge(c);
			
			PlanVacunacion pv = new PlanVacunacion("plan1", "plan");
			em.persist(pv);
			LocalDate tempDateInicio = LocalDate.of(2021, 6, 25);
			LocalDate tempDateFin = LocalDate.of(2021, 2, 5);
			Etapa e = new Etapa(tempDateInicio, tempDateFin, "condicion", pv);
			em.persist(e);
			pv.getEtapas().add(e);
			em.merge(pv);
			e.setVacuna(vacuna);
			em.merge(e);
			
			Vacunatorio vac = new Vacunatorio("vacunatorio1", "vac1", null, 25078965, 2.0f, 5.0f);
			em.persist(vac);
			Puesto p = new Puesto("puesto1", vac);
			em.persist(p);
			vac.getPuesto().add(p);
			em.merge(vac);
			utx.commit();
		}catch(SystemException | NotSupportedException | SecurityException | IllegalStateException | RollbackException | HeuristicMixedException | HeuristicRollbackException e) {
			e.printStackTrace();
		}
		cv.agregarConstanciaVacuna("vacuna1", 6, 1, LocalDate.of(2021, 3, 1), 1, 36);
	}
	
	@OperateOnDeployment("normal")
	@Test(expected=CertificadoInexistente.class)
	@InSequence(4)
	public void testAgregarConstanciaSinCertificado() throws UsuarioExistente, ReservaInexistente, CertificadoInexistente {
		int idEtapa = 0;
		try {
			utx.begin();
			Query queryPv = em.createQuery("SELECT pv FROM PlanVacunacion pv WHERE pv.nombre='plan1'");
			PlanVacunacion pv = (PlanVacunacion)queryPv.getSingleResult();
			Etapa e = pv.getEtapas().get(0);
			idEtapa = e.getId();
			Ciudadano c = em.find(Ciudadano.class, 1);
			Vacunatorio vac = em.find(Vacunatorio.class, "vacunatorio1");
			Puesto p = em.find(Puesto.class, new PuestoID("puesto1", vac.getId()));
			Reserva r = new Reserva(LocalDateTime.now(), EstadoReserva.EnProceso, e, c, p);
			em.persist(r);
			c.getReservas().add(r);
			em.merge(c);
			utx.commit();
		}catch(SystemException | NotSupportedException | SecurityException | IllegalStateException | RollbackException | HeuristicMixedException | HeuristicRollbackException e) {
			e.printStackTrace();
		}
		cv.agregarConstanciaVacuna("vacuna1", 6, 1, LocalDate.of(2021, 3, 1), 1, idEtapa);
	}
	
	@OperateOnDeployment("normal")
	@Test
	@InSequence(5)
	public void testAgregarConstancia() throws UsuarioExistente, ReservaInexistente, CertificadoInexistente {
		int idEtapa = 0;
		try {
			utx.begin();
			Query queryPv = em.createQuery("SELECT pv FROM PlanVacunacion pv WHERE pv.nombre='plan1'");
			PlanVacunacion pv = (PlanVacunacion)queryPv.getSingleResult();
			Etapa e = pv.getEtapas().get(0);
			idEtapa = e.getId();
			Ciudadano c = em.find(Ciudadano.class, 1);
			CertificadoVacunacion cert = new CertificadoVacunacion();
			em.persist(cert);
			c.setCertificado(cert);
			em.merge(c);
			utx.commit();
		}catch(SystemException | NotSupportedException | SecurityException | IllegalStateException | RollbackException | HeuristicMixedException | HeuristicRollbackException e) {
			e.printStackTrace();
		}
		cv.agregarConstanciaVacuna("vacuna1", 6, 1, LocalDate.now().minusDays(2), 1, idEtapa);
	}
	
//	@OperateOnDeployment("normal")
//	@Test(expected=ConstanciaInexistente.class)
//	@InSequence(6)
//	public void testObtenerConstanciaSinConstancia() throws ConstanciaInexistente {
//		cv.obtenerConstancia(37);
//	}
//	
//	@OperateOnDeployment("normal")
//	@Test
//	@InSequence(7)
//	public void testObtenerConstancia() throws ConstanciaInexistente {
//		try {
//			utx.begin();
//			ConstanciaVacuna tempConst = em.find(Ciudadano.class, 1).getCertificado().getConstancias().get(0);
//			int id = tempConst.getIdConstVac();
//			DtConstancia dtcons = new DtConstancia(id, 1, 6, LocalDate.of(2021, 2, 5), tempConst.getVacuna(), tempConst.getReserva().getDtReserva());
//			assertEquals(cv.obtenerConstancia(id).getIdConstVac(), dtcons.getIdConstVac());
//			utx.commit();
//		}catch(SystemException | NotSupportedException | SecurityException | IllegalStateException | RollbackException | HeuristicMixedException | HeuristicRollbackException e) {
//			e.printStackTrace();
//		}
//	}
	
	@OperateOnDeployment("normal")
	@Test
	@InSequence(8)
	public void testListarConstancia() throws ConstanciaInexistente {
		try {
			utx.begin();
			cv.listarConstancias();
			utx.commit();
		}catch(SystemException | NotSupportedException | SecurityException | IllegalStateException | RollbackException | HeuristicMixedException | HeuristicRollbackException e) {
			e.printStackTrace();
		}
	}
	
	@OperateOnDeployment("normal")
	@Test
	@InSequence(9)
	public void testListarConstanciasPeriodo() {
		assertEquals(1, cv.listarConstanciasPeriodo(4));
	}
	
	@OperateOnDeployment("normal")
	@Test
	@InSequence(10)
	public void testListarConstanciasPorVacuna() {
		Map<String, String> constancias = new HashMap<String,String>();
		constancias.put("vacuna1", String.valueOf(1));
		assertEquals(cv.listarConstanciaPorVacuna().size(), constancias.size());
	}
	
	@OperateOnDeployment("normal")
	@Test
	@InSequence(11)
	public void testListarConstanciasPorEnfermedad() {
		Map<String, String> constancias = new HashMap<String,String>();
		constancias.put("enf1", String.valueOf(1));
		assertEquals(cv.listarConstanciaPorEnfermedad().size(), constancias.size());
	}
	
	@OperateOnDeployment("normal")
	@Test
	@InSequence(12)
	public void testFiltroPorVacuna() {
		assertEquals(cv.filtroPorVacuna(4, "vacuna1"), 1);
	}
	
	@OperateOnDeployment("normal")
	@Test
	@InSequence(13)
	public void testFiltroPorEnfermedad() {
		assertEquals(cv.filtroPorEnfermedad(4, "enf"), 1);
	}
	
//	@OperateOnDeployment("normal")
//	@Test(expected=UsuarioExistente.class)
//	@InSequence()
//	public void testModificarConstanciaSinUsuario() throws UsuarioExistente, ReservaInexistente, CertificadoInexistente, ConstanciaInexistente {
//		//le paso un id de usuario que no existe
//	}
//	
//	@OperateOnDeployment("normal")
//	@Test(expected=ReservaInexistente.class)
//	@InSequence()
//	public void testModificarConstanciaSinReserva() throws UsuarioExistente, ReservaInexistente, CertificadoInexistente, ConstanciaInexistente {
//		//le paso un idEtapa que no existe
//	}
//	
//	@OperateOnDeployment("normal")
//	@Test
//	@InSequence()
//	public void testModificarConstancia() throws UsuarioExistente, ReservaInexistente, CertificadoInexistente, ConstanciaInexistente {
//		//test funcional, no contemblo la posibilidad SinCertificado(1 linea)
//	}
	
	@OperateOnDeployment("normal")
	@Test
	@InSequence(14)
	public void testFiltroPorPlan() {
		int planId = 0;
		try {
			utx.begin();
			Query queryPv = em.createQuery("SELECT pv FROM PlanVacunacion pv WHERE pv.nombre='plan1'");
			PlanVacunacion pv = (PlanVacunacion)queryPv.getSingleResult();
			planId = pv.getId();
			utx.commit();
		}catch(SystemException | NotSupportedException | SecurityException | IllegalStateException | RollbackException | HeuristicMixedException | HeuristicRollbackException e) {
			e.printStackTrace();
		}
		assertEquals(cv.filtroPorPlan(2, String.valueOf(planId)), 1);
	}
	
	@OperateOnDeployment("normal")
	@Test
	@InSequence(15)
	public void testFiltroPorPlanYVacuna() {
		int planId = 0;
		try {
			utx.begin();
			Query queryPv = em.createQuery("SELECT pv FROM PlanVacunacion pv WHERE pv.nombre='plan1'");
			PlanVacunacion pv = (PlanVacunacion)queryPv.getSingleResult();
			planId = pv.getId();
			utx.commit();
		}catch(SystemException | NotSupportedException | SecurityException | IllegalStateException | RollbackException | HeuristicMixedException | HeuristicRollbackException e) {
			e.printStackTrace();
		}
		assertEquals(cv.filtroPorPlanYVacuna(2, String.valueOf(planId), "vacuna1"), 1);
	}
	
	@OperateOnDeployment("normal")
	@Test
	@InSequence(20)
	public void testClean() {
		try {
			utx.begin();
			Laboratorio lab = em.find(Laboratorio.class, "lab");
			em.remove(lab);
			Enfermedad enf = em.find(Enfermedad.class, "enf");
			em.remove(enf);
			Vacuna vacuna = em.find(Vacuna.class, "vacuna1");
			em.remove(vacuna);
			Vacunatorio vacunatorio = em.find(Vacunatorio.class, "vacunatorio1");
			Puesto p = em.find(Puesto.class, new PuestoID("puesto1", vacunatorio.getId()));
			em.remove(p);
			em.remove(vacunatorio);
			Query queryPv = em.createQuery("SELECT pv FROM PlanVacunacion pv WHERE pv.nombre='plan1'");
			PlanVacunacion pv = (PlanVacunacion)queryPv.getSingleResult();
			Ciudadano ciudadano = em.find(Ciudadano.class, 1);
			for(Reserva r: ciudadano.getReservas()) {
				r.setEtapa(null);
				em.remove(r);
			}
			for(Etapa e: pv.getEtapas()) {
				em.remove(e);
			}
			em.remove(pv);
			for(ConstanciaVacuna c: ciudadano.getCertificado().getConstancias()) {
				em.remove(c);
			}
			em.remove(ciudadano.getCertificado());
			em.remove(ciudadano);
			utx.commit();
		}catch(SystemException | NotSupportedException | SecurityException | IllegalStateException | RollbackException | HeuristicMixedException | HeuristicRollbackException e) {
			e.printStackTrace();
		}
	}
	
}
