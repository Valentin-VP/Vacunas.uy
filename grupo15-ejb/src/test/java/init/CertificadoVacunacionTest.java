package init;

import static org.junit.Assert.assertEquals;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
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
import datatypes.DtCertificadoVac;
import datatypes.DtConstancia;
import datatypes.DtDireccion;
import datatypes.DtReserva;
import datatypes.EstadoReserva;
import datatypes.Sexo;
import entities.CertificadoVacunacion;
//import datatypes.DtDireccion;
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
import exceptions.ReservaInexistente;
import exceptions.UsuarioExistente;
import interfaces.ICertificadoVacunacionDAORemote;
import interfaces.IReservaDAORemote;
import persistence.EtapaID;
import persistence.ReservaID;

@RunWith(Arquillian.class)
public class CertificadoVacunacionTest {
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
	ICertificadoVacunacionDAORemote cv;
	@Resource
	UserTransaction utx;
	
	@PersistenceContext
	private EntityManager em;
	
	@OperateOnDeployment("normal")
	@Test(expected=UsuarioExistente.class)
	@InSequence(1)
	public void testObtenerCertificadoVacunacionSinUsuario() throws CertificadoInexistente, UsuarioExistente {
		try {
			utx.begin();
			List<ConstanciaVacuna> listConstancias= new ArrayList<ConstanciaVacuna>();
			LocalDate tempDateNac = LocalDate.of(1998, 4, 5);
			Ciudadano c1 = new Ciudadano(1, "c1", "c1", tempDateNac, "correo", null, Sexo.Masculino, "sector", true);
			Ciudadano c2 = new Ciudadano(2, "c2", "c2", tempDateNac, "correo", null, Sexo.Femenino, "sector", true);
			em.persist(c1);
			CertificadoVacunacion cvac = new CertificadoVacunacion();
			Laboratorio lab = new Laboratorio("lab1");
			Enfermedad enf = new Enfermedad("enf1");
			Vacunatorio vacunatorio = new Vacunatorio("vacunatorio1", "vacunatorio1", null, 23445673, 2.0f, 3.0f);
			Puesto puesto = new Puesto("puesto1", vacunatorio);
			vacunatorio.getPuesto().add(puesto);
			em.persist(vacunatorio);
			PlanVacunacion pv = new PlanVacunacion("plan1", "plan");
			em.persist(pv);
			LocalDate tempDateInicio = LocalDate.of(2021, 2, 1);
			LocalDate tempDateFin = LocalDate.of(2021, 4, 1);
			Etapa e = new Etapa(tempDateInicio, tempDateFin, "condicion", pv);
			em.persist(e);
			pv.getEtapas().add(e);
			em.merge(pv);
			em.persist(enf);
			em.persist(lab);
			Vacuna vac = new Vacuna("vac", 2, 6, 1, lab, enf);
			em.persist(vac);
			e.setVacuna(vac);
			em.merge(e);
			//
			LocalDateTime tempDateRegistro = LocalDateTime.now(); 
			Reserva res = new Reserva(tempDateRegistro, EstadoReserva.Completada, e, c1, puesto);
			em.persist(res);
			c1.getReservas().add(res);
			LocalDate tempDateUltima = LocalDate.of(2021, 2, 25);
			ConstanciaVacuna constanciaVac = new ConstanciaVacuna(6, 1, tempDateUltima, vac.getNombre(), res);
			em.persist(constanciaVac);
			listConstancias.add(constanciaVac);
			cvac.setConstancias(listConstancias);
			c1.setCertificado(cvac);
			em.merge(c1);
			em.persist(c2);
			utx.commit();
		}catch(SystemException | NotSupportedException | SecurityException | IllegalStateException | RollbackException | HeuristicMixedException | HeuristicRollbackException e) {
			e.printStackTrace();
		}
		cv.obtenerCertificadoVacunacion(3);
	}
	
	@OperateOnDeployment("normal")
	@Test(expected=CertificadoInexistente.class)
	@InSequence(2)
	public void testObtenerCertificadoVacunacionSinCertificado() throws CertificadoInexistente, UsuarioExistente {
		cv.obtenerCertificadoVacunacion(2);
	}
	
	@OperateOnDeployment("normal")
	@Test
	@InSequence(3)
	public void testObtenerCertificadoVacunacion() throws CertificadoInexistente, UsuarioExistente {
		try {
			utx.begin();
			LocalDate tempDateUltima = LocalDate.of(2021, 2, 25);
			Vacuna vac = em.find(Vacuna.class, "vac");
			Query queryP = em.createQuery("SELECT p FROM PlanVacunacion p");
			PlanVacunacion plan = (PlanVacunacion)queryP.getResultList().get(0);
			Etapa e = plan.getEtapas().get(0);
			Query queryR = em.createQuery("SELECT r FROM Reserva r");
			Reserva reserva = (Reserva)queryR.getResultList().get(0);
			//Reserva reserva = em.find(Reserva.class, new ReservaID(LocalDateTime.now(), new EtapaID(e.getId(), plan.getId()), 1));
			DtReserva res = reserva.getDtReserva();
			ArrayList<DtConstancia> dtc = new ArrayList<>();
			int idConst = em.find(Ciudadano.class, 1).getCertificado().getConstancias().get(0).getIdConstVac();
			DtConstancia dtconstancia = new DtConstancia(idConst, 6, 1, tempDateUltima, vac.getNombre(), res, vac.getEnfermedad().getNombre());
			dtc.add(dtconstancia);
			DtCertificadoVac dtvc = new DtCertificadoVac(em.find(Ciudadano.class, 1).getCertificado().getIdCert(), dtc);
			cv.obtenerCertificadoVacunacion(1);
			//assertEquals(cv.obtenerCertificadoVacunacion(1), dtvc);
			utx.commit();
		}catch(SystemException | NotSupportedException | SecurityException | IllegalStateException | RollbackException | HeuristicMixedException | HeuristicRollbackException e) {
			e.printStackTrace();
		}
	}
	
	@OperateOnDeployment("normal")
	@Test
	@InSequence(4)
	public void testClean() {
		try {
			utx.begin();
			Ciudadano c1 = em.find(Ciudadano.class, 1);
			Query queryP = em.createQuery("SELECT p FROM PlanVacunacion p");
			PlanVacunacion pv = (PlanVacunacion)queryP.getResultList().get(0);
			for(Reserva r: c1.getReservas()) {
				em.remove(r);
			}
			for(Etapa e: pv.getEtapas()) {
				em.remove(e);
			}
			Vacunatorio vacunatorio = em.find(Vacunatorio.class, "vacunatorio1");
			for(Puesto p :vacunatorio.getPuesto()) {
				em.remove(p);
			}
			Vacuna vac = em.find(Vacuna.class, "vac");
			Enfermedad enf = em.find(Enfermedad.class, "enf1");
			Laboratorio lab = em.find(Laboratorio.class, "lab1");
			CertificadoVacunacion cv = c1.getCertificado();
			for(ConstanciaVacuna c :cv.getConstancias()) {
				em.remove(c);
			}
			Ciudadano c2 = em.find(Ciudadano.class, 2);
			em.remove(cv);
			em.remove(c1);
			em.remove(c2);
			em.remove(pv);
			em.remove(enf);
			em.remove(lab);
			em.remove(vac);
			em.remove(vacunatorio);
			utx.commit();
		}catch(SystemException | NotSupportedException | SecurityException | IllegalStateException | RollbackException | HeuristicMixedException | HeuristicRollbackException e) {
			e.printStackTrace();
		}
	}

}