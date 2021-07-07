package init;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertTrue;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
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
import datatypes.DtEnfermedad;
import datatypes.DtEtapa;
import datatypes.DtPlanVacunacion;
import datatypes.DtReserva;
import datatypes.DtUsuarioExterno;
import datatypes.EstadoReserva;
import datatypes.Sexo;
import entities.Agenda;
import entities.CertificadoVacunacion;
import entities.Ciudadano;
import entities.ConstanciaVacuna;
import entities.Enfermedad;
import entities.Etapa;
import entities.Laboratorio;
import entities.PlanVacunacion;
import entities.Puesto;
import entities.ReglasCupos;
import entities.Reserva;
import entities.Vacuna;
import entities.Vacunatorio;
import exceptions.AccionInvalida;
import exceptions.CupoInexistente;
import exceptions.EnfermedadInexistente;
import exceptions.EtapaInexistente;
import exceptions.PlanVacunacionInexistente;
import exceptions.ReservaInexistente;
import exceptions.UsuarioInexistente;
import exceptions.VacunatorioNoCargadoException;
import interfaces.IReservaDAORemote;
import persistence.EtapaID;
import persistence.ReservaID;

@RunWith(Arquillian.class)
public class ReservaTest {
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
	IReservaDAORemote cr;
	@Resource
	UserTransaction utx;
	
	@PersistenceContext
	private EntityManager em;
	
	
	@OperateOnDeployment("normal")
	@Test(expected=EnfermedadInexistente.class)
	@InSequence(1)
	public void testSeleccionarEnfermedadSinEnfermedad() throws PlanVacunacionInexistente, EnfermedadInexistente {
		cr.seleccionarEnfermedad("virus0");
	}
	
	@OperateOnDeployment("normal")
	@Test(expected=PlanVacunacionInexistente.class)
	@InSequence(3)
	public void testSeleccionarEnfermedadSinPlan() throws PlanVacunacionInexistente, EnfermedadInexistente {
		try {
			utx.begin();
			Enfermedad e1 = new Enfermedad("virus1");
			Enfermedad e2 = new Enfermedad("virus2");
			
			em.persist(e1);
			em.persist(e2);

			utx.commit();
			
		} catch (HeuristicRollbackException | NotSupportedException | SystemException | SecurityException | IllegalStateException | RollbackException | HeuristicMixedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		cr.seleccionarEnfermedad("virus1");
	}
	
	@OperateOnDeployment("normal")
	@Test
	@InSequence(5)
	public void testSeleccionarEnfermedadSinCura() throws EnfermedadInexistente, PlanVacunacionInexistente {
		try {
			utx.begin();
			Enfermedad e1 = em.find(Enfermedad.class, "virus1");
			Laboratorio l = new Laboratorio("lab1");
			Vacuna v = new Vacuna("vac1", 1, 1, 1, l, e1);
			PlanVacunacion pv1 = new PlanVacunacion("pv1", "pv1");
			PlanVacunacion pv2 = new PlanVacunacion("pv2", "pv2");
			PlanVacunacion pv3 = new PlanVacunacion("pv3", "pv3");
			Etapa e_pv1 = new Etapa(LocalDate.of(2021, 1, 1), LocalDate.of(2022, 1, 1), "1|30|industria|no", pv1);
			Etapa e_pv3 = new Etapa(LocalDate.of(2021, 1, 1), LocalDate.of(2022, 1, 1), "100|300|todos|si", pv3);
			e_pv1.setVacuna(v);
			e_pv3.setVacuna(v);
			Ciudadano c1 = new Ciudadano(21111111, "Ciudadano", "DeTest Uno", LocalDate.of(2000, 1, 1), "c@1", null, Sexo.Otro, "Sector123456789" , false);
			c1.setCertificado(new CertificadoVacunacion());
			Ciudadano c2 = new Ciudadano(21111112, "Ciudadano", "DeTest Dos", LocalDate.of(1960, 1, 1), "c@2", null, Sexo.Otro, "Sector123456789" , false);
			c2.setCertificado(new CertificadoVacunacion());
			pv1.addEtapa(e_pv1);
			pv3.addEtapa(e_pv3);
			pv2.setEtapas(new ArrayList<Etapa>());
			pv1.setEnfermedad(e1);
			pv3.setEnfermedad(e1);
			em.persist(l);
			em.persist(v);
			//em.persist(e_pv1);
			//em.persist(e_pv3);
			em.persist(pv1);
			em.persist(pv2);
			em.persist(pv3);
			em.persist(c1);
			em.persist(c2);
			utx.commit();
		} catch (NotSupportedException | SystemException | SecurityException | IllegalStateException | RollbackException | HeuristicMixedException | HeuristicRollbackException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		assertArrayEquals(new DtEnfermedad[0], cr.seleccionarEnfermedad("virus2").toArray());
	}
	
	@OperateOnDeployment("normal")
	@Test
	@InSequence(7)
	public void testSeleccionarEnfermedad() throws PlanVacunacionInexistente, EnfermedadInexistente {
		cr.seleccionarEnfermedad("virus1");
	}
	
	@OperateOnDeployment("normal")
	@Test(expected=PlanVacunacionInexistente.class)
	@InSequence(9)
	public void testSeleccionarPlanSinPlan() throws PlanVacunacionInexistente, EtapaInexistente, UsuarioInexistente {
		cr.seleccionarPlanVacunacion(0, 0, new DtUsuarioExterno("21111111", "", "industria", false));
	}
	
	@OperateOnDeployment("normal")
	@Test(expected=EtapaInexistente.class)
	@InSequence(11)
	public void testSeleccionarPlanSinEtapas() throws PlanVacunacionInexistente, EtapaInexistente, UsuarioInexistente {
		Query queryPv = em.createQuery("SELECT pv FROM PlanVacunacion pv WHERE pv.nombre='pv2'");
		PlanVacunacion pv = (PlanVacunacion) queryPv.getSingleResult();
		cr.seleccionarPlanVacunacion(pv.getId(), 0, new DtUsuarioExterno("21111111", "", "industria", false));
	}
	
	@OperateOnDeployment("normal")
	@Test(expected=UsuarioInexistente.class)
	@InSequence(13)
	public void testSeleccionarPlanSinUsuario() throws PlanVacunacionInexistente, EtapaInexistente, UsuarioInexistente {
		Query queryPv = em.createQuery("SELECT pv FROM PlanVacunacion pv WHERE pv.nombre='pv1'");
		PlanVacunacion pv = (PlanVacunacion) queryPv.getSingleResult();
		cr.seleccionarPlanVacunacion(pv.getId(), 0, new DtUsuarioExterno("21111111", "", "industria", false));
	}
	
	@OperateOnDeployment("normal")
	@Test(expected=EtapaInexistente.class)
	@InSequence(15)
	public void testSeleccionarPlanSinEtapasAccesibles() throws PlanVacunacionInexistente, EtapaInexistente, UsuarioInexistente {
		Query queryPv = em.createQuery("SELECT pv FROM PlanVacunacion pv WHERE pv.nombre='pv3'");
		PlanVacunacion pv = (PlanVacunacion) queryPv.getSingleResult();
		cr.seleccionarPlanVacunacion(pv.getId(), 21111112, new DtUsuarioExterno("21111112", "", "industria", false));
	}
	
	@OperateOnDeployment("normal")
	@Test(expected=EtapaInexistente.class)
	@InSequence(17)
	public void testSeleccionarPlanUsuarioYaTieneReserva() throws PlanVacunacionInexistente, EtapaInexistente, UsuarioInexistente {
		try {
			utx.begin();
			Ciudadano c1 = em.find(Ciudadano.class, 21111111);
			Query queryPv = em.createQuery("SELECT pv FROM PlanVacunacion pv WHERE pv.nombre='pv1'");
			PlanVacunacion pv = (PlanVacunacion) queryPv.getSingleResult();
			Etapa e_pv1 = pv.getEtapas().get(0);
			Reserva r1 = new Reserva(LocalDateTime.of(LocalDate.now(), LocalTime.of(20, 0, 0)), EstadoReserva.EnProceso, e_pv1, c1, null);
			c1.getReservas().add(r1);
			em.merge(c1);
			em.merge(r1);
			utx.commit();
			
			cr.seleccionarPlanVacunacion(pv.getId(), 21111111, new DtUsuarioExterno("21111111", "", "industria", false));
			

		} catch (NotSupportedException | SystemException | SecurityException | IllegalStateException | RollbackException | HeuristicMixedException | HeuristicRollbackException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@OperateOnDeployment("normal")
	@Test
	@InSequence(19)
	public void testSeleccionarPlan() throws PlanVacunacionInexistente, EtapaInexistente, UsuarioInexistente {
		try {
			utx.begin();
			Ciudadano c1 = em.find(Ciudadano.class, 21111111);
			Query queryPv = em.createQuery("SELECT pv FROM PlanVacunacion pv WHERE pv.nombre='pv1'");
			PlanVacunacion pv = (PlanVacunacion) queryPv.getSingleResult();
			Reserva r1 = em.find(Reserva.class, new ReservaID(LocalDateTime.of(LocalDate.now(), LocalTime.of(20, 0, 0)), new EtapaID(pv.getEtapas().get(0).getId(),pv.getId()), 21111111));
			c1.getReservas().remove(r1);
			em.merge(c1);
			em.remove(r1);
			utx.commit();
			cr.seleccionarPlanVacunacion(pv.getId(), 21111111, new DtUsuarioExterno("21111111", "", "industria", false));
		} catch (NotSupportedException | SystemException | SecurityException | IllegalStateException | RollbackException | HeuristicMixedException | HeuristicRollbackException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	
	@OperateOnDeployment("normal")
	@Test(expected=VacunatorioNoCargadoException.class)
	@InSequence(21)
	public void testSeleccionarFechaSinVacunatorio() throws VacunatorioNoCargadoException, PlanVacunacionInexistente, UsuarioInexistente, EtapaInexistente, CupoInexistente {
		Query queryPv = em.createQuery("SELECT pv FROM PlanVacunacion pv WHERE pv.nombre='pv1'");
		PlanVacunacion pv = (PlanVacunacion) queryPv.getSingleResult();
		cr.seleccionarFecha(LocalDate.now().plusDays(1), "", pv.getId(), 21111111, new DtUsuarioExterno("21111111", "", "industria", false));
	}
	
	@OperateOnDeployment("normal")
	@Test(expected=PlanVacunacionInexistente.class)
	@InSequence(23)
	public void testSeleccionarFechaSinPlan() throws VacunatorioNoCargadoException, PlanVacunacionInexistente, UsuarioInexistente, EtapaInexistente, CupoInexistente {
		try {
			utx.begin();
			ReglasCupos rc1 = new ReglasCupos("rc1", 15,  LocalTime.of(0, 0, 0),  LocalTime.of(23, 59, 59));
			Vacunatorio vact1 = new Vacunatorio("vact1", "vact1_n", null, 0, 0F, 0F);
			vact1.setReglasCupos(rc1);
			em.persist(rc1);
			em.persist(vact1);
			utx.commit();
		} catch (NotSupportedException | SystemException | SecurityException | IllegalStateException | RollbackException | HeuristicMixedException | HeuristicRollbackException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		cr.seleccionarFecha(LocalDate.now().plusDays(1), "vact1", 0, 21111111, new DtUsuarioExterno("21111111", "", "industria", false));
	}
	
	@OperateOnDeployment("normal")
	@Test(expected=EtapaInexistente.class)
	@InSequence(25)
	public void testSeleccionarFechaSinEtapas() throws VacunatorioNoCargadoException, PlanVacunacionInexistente, UsuarioInexistente, EtapaInexistente, CupoInexistente {
		Query queryPv = em.createQuery("SELECT pv FROM PlanVacunacion pv WHERE pv.nombre='pv2'");
		PlanVacunacion pv = (PlanVacunacion) queryPv.getSingleResult();
		cr.seleccionarFecha(LocalDate.now().plusDays(1), "vact1", pv.getId(), 21111111, new DtUsuarioExterno("21111111", "", "industria", false));
	}
	
	@OperateOnDeployment("normal")
	@Test(expected=UsuarioInexistente.class)
	@InSequence(27)
	public void testSeleccionarFechaSinUsuario() throws VacunatorioNoCargadoException, PlanVacunacionInexistente, UsuarioInexistente, EtapaInexistente, CupoInexistente {
		Query queryPv = em.createQuery("SELECT pv FROM PlanVacunacion pv WHERE pv.nombre='pv1'");
		PlanVacunacion pv = (PlanVacunacion) queryPv.getSingleResult();
		cr.seleccionarFecha(LocalDate.now().plusDays(1), "vact1", pv.getId(), 0, new DtUsuarioExterno("21111111", "", "industria", false));
	}
	
	@OperateOnDeployment("normal")
	@Test(expected=EtapaInexistente.class)
	@InSequence(29)
	public void testSeleccionarFechaSinEtapasAccesibles() throws VacunatorioNoCargadoException, PlanVacunacionInexistente, UsuarioInexistente, EtapaInexistente, CupoInexistente {
		Query queryPv = em.createQuery("SELECT pv FROM PlanVacunacion pv WHERE pv.nombre='pv1'");
		PlanVacunacion pv = (PlanVacunacion) queryPv.getSingleResult();
		cr.seleccionarFecha(LocalDate.now().plusDays(1), "vact1", pv.getId(), 21111112, new DtUsuarioExterno("21111112", "", "industria", false));
	}
	
	@OperateOnDeployment("normal")
	@Test(expected=CupoInexistente.class)
	@InSequence(31)
	public void testSeleccionarFechaSinFechaValida() throws VacunatorioNoCargadoException, PlanVacunacionInexistente, UsuarioInexistente, EtapaInexistente, CupoInexistente {
		Query queryPv = em.createQuery("SELECT pv FROM PlanVacunacion pv WHERE pv.nombre='pv1'");
		PlanVacunacion pv = (PlanVacunacion) queryPv.getSingleResult();
		cr.seleccionarFecha(LocalDate.now(), "vact1", pv.getId(), 21111111, new DtUsuarioExterno("21111112", "", "industria", false));
	}
	
	@OperateOnDeployment("normal")
	@Test
	@InSequence(33)
	public void testSeleccionarFechaSinPuestos() throws VacunatorioNoCargadoException, PlanVacunacionInexistente, UsuarioInexistente, EtapaInexistente, CupoInexistente{
		Query queryPv = em.createQuery("SELECT pv FROM PlanVacunacion pv WHERE pv.nombre='pv1'");
		PlanVacunacion pv = (PlanVacunacion) queryPv.getSingleResult();
		assertArrayEquals(new String[0], cr.seleccionarFecha(LocalDate.now().plusDays(1), "vact1", pv.getId(), 21111111, new DtUsuarioExterno("21111111", "", "industria", false)).toArray());
	}
	
	@OperateOnDeployment("normal")
	@Test
	@InSequence(35)
	public void testSeleccionarFecha() throws VacunatorioNoCargadoException, PlanVacunacionInexistente, UsuarioInexistente, EtapaInexistente, CupoInexistente {
		PlanVacunacion pv = null;
		try {
			utx.begin();
			Query queryPv = em.createQuery("SELECT pv FROM PlanVacunacion pv WHERE pv.nombre='pv1'");
			PlanVacunacion temp = (PlanVacunacion) queryPv.getSingleResult();
			pv = temp;
			Vacunatorio vact1 = em.find(Vacunatorio.class, "vact1");
			Puesto p1_vact1 = new Puesto("p1_vact1", vact1);
			vact1.getPuesto().add(p1_vact1);
			em.merge(vact1);
			utx.commit();
		} catch (NotSupportedException | SystemException | SecurityException | IllegalStateException | RollbackException | HeuristicMixedException | HeuristicRollbackException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
		ArrayList<String> temp = new ArrayList<>();
		LocalTime init = LocalTime.of(0, 0, 0);
		temp.add(init.format(formatter));
		for (int i = 0; i < 94; i++) {
			init = init.plusMinutes(15);
			temp.add(init.format(formatter));
		}
		
		assertArrayEquals(temp.toArray(), cr.seleccionarFecha(LocalDate.now().plusDays(1), "vact1", pv.getId() , 21111111, new DtUsuarioExterno("21111111", "", "industria", false)).toArray());
	}
	
	
	
	//arranca confirmar
	@OperateOnDeployment("normal")
	@Test(expected=UsuarioInexistente.class)
	@InSequence(37)
	public void testConfirmarReservaSinUsuario() throws UsuarioInexistente, PlanVacunacionInexistente, VacunatorioNoCargadoException, EnfermedadInexistente, CupoInexistente, EtapaInexistente {
		Query queryPv = em.createQuery("SELECT pv FROM PlanVacunacion pv WHERE pv.nombre='pv1'");
		PlanVacunacion pv = (PlanVacunacion) queryPv.getSingleResult();
		cr.confirmarReserva(0, "virus1", pv.getId(), "vact1", LocalDate.now().plusDays(1), LocalTime.of(10, 0, 0), new DtUsuarioExterno("21111111", "", "industria", false));
	}
	
	@OperateOnDeployment("normal")
	@Test(expected=EnfermedadInexistente.class)
	@InSequence(39)
	public void testConfirmarReservaSinEnfermedad() throws UsuarioInexistente, PlanVacunacionInexistente, VacunatorioNoCargadoException, EnfermedadInexistente, CupoInexistente, EtapaInexistente {
		Query queryPv = em.createQuery("SELECT pv FROM PlanVacunacion pv WHERE pv.nombre='pv1'");
		PlanVacunacion pv = (PlanVacunacion) queryPv.getSingleResult();
		cr.confirmarReserva(21111111, "virus0", pv.getId(), "vact1", LocalDate.now().plusDays(1), LocalTime.of(10, 0, 0), new DtUsuarioExterno("21111111", "", "industria", false));
	}
	
	@OperateOnDeployment("normal")
	@Test(expected=PlanVacunacionInexistente.class)
	@InSequence(41)
	public void testConfirmarReservaSinPlan() throws UsuarioInexistente, PlanVacunacionInexistente, VacunatorioNoCargadoException, EnfermedadInexistente, CupoInexistente, EtapaInexistente {
		cr.confirmarReserva(21111111, "virus1", 0, "vact1", LocalDate.now().plusDays(1), LocalTime.of(10, 0, 0), new DtUsuarioExterno("21111111", "", "industria", false));
	}
	
	@OperateOnDeployment("normal")
	@Test(expected=EnfermedadInexistente.class)
	@InSequence(43)
	public void testConfirmarReservaSinEnfermedadEnPlan() throws UsuarioInexistente, PlanVacunacionInexistente, VacunatorioNoCargadoException, EnfermedadInexistente, CupoInexistente, EtapaInexistente {
		Query queryPv = em.createQuery("SELECT pv FROM PlanVacunacion pv WHERE pv.nombre='pv1'");
		PlanVacunacion pv = (PlanVacunacion) queryPv.getSingleResult();
		cr.confirmarReserva(21111111, "virus2", pv.getId(), "vact1", LocalDate.now().plusDays(1), LocalTime.of(10, 0, 0), new DtUsuarioExterno("21111111", "", "industria", false));
	}
	
	@OperateOnDeployment("normal")
	@Test(expected=VacunatorioNoCargadoException.class)
	@InSequence(45)
	public void testConfirmarReservaSinVacunatorio() throws UsuarioInexistente, PlanVacunacionInexistente, VacunatorioNoCargadoException, EnfermedadInexistente, CupoInexistente, EtapaInexistente {
		Query queryPv = em.createQuery("SELECT pv FROM PlanVacunacion pv WHERE pv.nombre='pv1'");
		PlanVacunacion pv = (PlanVacunacion) queryPv.getSingleResult();
		cr.confirmarReserva(21111111, "virus1", pv.getId(), "vact0", LocalDate.now().plusDays(1), LocalTime.of(10, 0, 0), new DtUsuarioExterno("21111111", "", "industria", false));
	}
	
	//@OperateOnDeployment("normal")
	//@Test(expected=EtapaInexistente.class)
	//@InSequence(47)
	//public void testConfirmarReservaSinEtapasEnPlan() throws UsuarioInexistente, PlanVacunacionInexistente, VacunatorioNoCargadoException, EnfermedadInexistente, CupoInexistente, EtapaInexistente {
	//	Query queryPv = em.createQuery("SELECT pv FROM PlanVacunacion pv WHERE pv.nombre='pv2'");
	//	PlanVacunacion pv = (PlanVacunacion) queryPv.getSingleResult();
	//	cr.confirmarReserva(21111111, "virus1", pv.getId(), "vact1", LocalDate.now().plusDays(1), LocalTime.of(10, 0, 0), new DtUsuarioExterno("21111111", "", "industria", false));
	//}
	
	@OperateOnDeployment("normal")
	@Test(expected=CupoInexistente.class)
	@InSequence(49)
	public void testConfirmarReservaSinFechaValida() throws UsuarioInexistente, PlanVacunacionInexistente, VacunatorioNoCargadoException, EnfermedadInexistente, CupoInexistente, EtapaInexistente {
		Query queryPv = em.createQuery("SELECT pv FROM PlanVacunacion pv WHERE pv.nombre='pv1'");
		PlanVacunacion pv = (PlanVacunacion) queryPv.getSingleResult();
		cr.confirmarReserva(21111111, "virus1", pv.getId(), "vact1", LocalDate.now(), LocalTime.of(10, 0, 0), new DtUsuarioExterno("21111111", "", "industria", false));
	}
	
	@OperateOnDeployment("normal")
	@Test(expected=EtapaInexistente.class)
	@InSequence(51)
	public void testConfirmarReservaSinEtapasAccesibles() throws UsuarioInexistente, PlanVacunacionInexistente, VacunatorioNoCargadoException, EnfermedadInexistente, CupoInexistente, EtapaInexistente {
		Query queryPv = em.createQuery("SELECT pv FROM PlanVacunacion pv WHERE pv.nombre='pv1'");
		PlanVacunacion pv = (PlanVacunacion) queryPv.getSingleResult();
		cr.confirmarReserva(21111112, "virus1", pv.getId(), "vact1", LocalDate.now().plusDays(1), LocalTime.of(10, 0, 0), new DtUsuarioExterno("21111112", "", "industria", false));
	}
	
	@OperateOnDeployment("normal")
	@Test(expected=EtapaInexistente.class)
	@InSequence(53)
	public void testConfirmarReservaConReservaParaEnfermedad() throws UsuarioInexistente, PlanVacunacionInexistente, VacunatorioNoCargadoException, EnfermedadInexistente, CupoInexistente, EtapaInexistente {
		try {
			utx.begin();
			Ciudadano c1 = em.find(Ciudadano.class, 21111111);
			Query queryPv = em.createQuery("SELECT pv FROM PlanVacunacion pv WHERE pv.nombre='pv1'");
			PlanVacunacion pv = (PlanVacunacion) queryPv.getSingleResult();
			Etapa e_pv1 = pv.getEtapas().get(0);
			Reserva r1 = new Reserva(LocalDateTime.of(LocalDate.now().plusDays(1), LocalTime.of(20, 0, 0)), EstadoReserva.EnProceso, e_pv1, c1, null);
			c1.getReservas().add(r1);
			em.merge(c1);
			em.merge(r1);
			utx.commit();
			cr.confirmarReserva(21111111, "virus1", pv.getId(), "vact1", LocalDate.now().plusDays(1), LocalTime.of(10, 0, 0), new DtUsuarioExterno("21111111", "", "industria", false));
		} catch (NotSupportedException | SystemException | SecurityException | IllegalStateException | RollbackException | HeuristicMixedException | HeuristicRollbackException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	
	//@OperateOnDeployment("normal")
	//@Test(expected=CupoInexistente.class)
	//@InSequence(55)
	//public void testConfirmarReservaConReservaParaVacuna() throws UsuarioInexistente, PlanVacunacionInexistente, VacunatorioNoCargadoException, EnfermedadInexistente, CupoInexistente, EtapaInexistente {
	//	Query queryPv = em.createQuery("SELECT pv FROM PlanVacunacion pv WHERE pv.nombre='pv3'");
	//	PlanVacunacion pv = (PlanVacunacion) queryPv.getSingleResult();
	//	cr.confirmarReserva(21111111, "virus2", pv.getId(), "vact1", LocalDate.now().plusDays(1), LocalTime.of(20, 0, 0), new DtUsuarioExterno("21111111", "", "industria", false));
	//}
	
	@OperateOnDeployment("normal")
	@Test
	@InSequence(56)
	public void testLimpiarReserva()  {
		try {
			utx.begin();
			Ciudadano c1 = em.find(Ciudadano.class, 21111111);
			Query queryPv = em.createQuery("SELECT pv FROM PlanVacunacion pv WHERE pv.nombre='pv1'");
			PlanVacunacion pv = (PlanVacunacion) queryPv.getSingleResult();
			Reserva r1 = em.find(Reserva.class, new ReservaID(LocalDateTime.of(LocalDate.now().plusDays(1), LocalTime.of(20, 0, 0)), new EtapaID(pv.getEtapas().get(0).getId(),pv.getId()), 21111111));
			c1.getReservas().remove(r1);
			em.merge(c1);
			//em.remove(r1);
			utx.commit();
		} catch (NotSupportedException | SystemException | SecurityException | IllegalStateException | RollbackException | HeuristicMixedException | HeuristicRollbackException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@OperateOnDeployment("normal")
	@Test
	@InSequence(57)
	public void testConfirmarReserva() throws UsuarioInexistente, PlanVacunacionInexistente, VacunatorioNoCargadoException, EnfermedadInexistente, CupoInexistente, EtapaInexistente {
		
		try {
			utx.begin();
			Ciudadano c1 = em.find(Ciudadano.class, 21111111);
			Query queryPv = em.createQuery("SELECT pv FROM PlanVacunacion pv WHERE pv.nombre='pv1'");
			PlanVacunacion pv = (PlanVacunacion) queryPv.getSingleResult();
			cr.confirmarReserva(21111111, "virus1", pv.getId(), "vact1", LocalDate.now().plusDays(1), LocalTime.of(20, 0, 0), new DtUsuarioExterno("21111111", "", "industria", false));
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
			Query queryA = em.createQuery("SELECT a FROM Agenda a WHERE a.fecha='" +LocalDate.now().plusDays(1).format(formatter)+ "' AND vacunatorio_id='vact1'");
			Agenda a = (Agenda) queryA.getSingleResult();
			Reserva r1 = em.find(Reserva.class, new ReservaID(LocalDateTime.of(LocalDate.now().plusDays(1), LocalTime.of(20, 0, 0)), new EtapaID(pv.getEtapas().get(0).getId(),pv.getId()), 21111111));
			c1.getReservas().remove(r1);
			a.getReservas().remove(r1);
			em.merge(a);
			em.merge(c1);
			em.remove(r1);
			utx.commit();
		} catch (NotSupportedException | SystemException | SecurityException | IllegalStateException | RollbackException | HeuristicMixedException | HeuristicRollbackException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	//termina confirmar
	
	
	
	@OperateOnDeployment("normal")
	@Test(expected=UsuarioInexistente.class)
	@InSequence(59)
	public void testObtenerReservaSinUsuario() throws ReservaInexistente, UsuarioInexistente, EtapaInexistente {
		try {
			utx.begin();
			Query queryPv = em.createQuery("SELECT pv FROM PlanVacunacion pv WHERE pv.nombre='pv1'");
			PlanVacunacion pv = (PlanVacunacion) queryPv.getSingleResult();
			cr.obtenerReserva(0, pv.getId(), pv.getEtapas().get(0).getId(), LocalDateTime.of(LocalDate.now().plusDays(1), LocalTime.of(20, 0,0)));
			utx.commit();
		} catch (NotSupportedException | SystemException | SecurityException | IllegalStateException | RollbackException | HeuristicMixedException | HeuristicRollbackException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@OperateOnDeployment("normal")
	@Test(expected=EtapaInexistente.class)
	@InSequence(61)
	public void testObtenerReservaSinEtapa() throws ReservaInexistente, UsuarioInexistente, EtapaInexistente {
		Query queryPv = em.createQuery("SELECT pv FROM PlanVacunacion pv WHERE pv.nombre='pv1'");
		PlanVacunacion pv = (PlanVacunacion) queryPv.getSingleResult();
		cr.obtenerReserva(21111111, pv.getId(), 0, LocalDateTime.of(LocalDate.now().plusDays(1), LocalTime.of(20, 0,0)));
	}
	
	@OperateOnDeployment("normal")
	@Test(expected=ReservaInexistente.class)
	@InSequence(63)
	public void testObtenerReservaSinReserva() throws ReservaInexistente, UsuarioInexistente, EtapaInexistente {
		try {
			utx.begin();
			Query queryPv = em.createQuery("SELECT pv FROM PlanVacunacion pv WHERE pv.nombre='pv1'");
			PlanVacunacion pv = (PlanVacunacion) queryPv.getSingleResult();
			cr.obtenerReserva(21111111, pv.getId(), pv.getEtapas().get(0).getId(), LocalDateTime.of(LocalDate.now().plusDays(1), LocalTime.of(20, 0,0)));
			utx.commit();
		} catch (NotSupportedException | SystemException | SecurityException | IllegalStateException | RollbackException | HeuristicMixedException | HeuristicRollbackException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@OperateOnDeployment("normal")
	@Test
	@InSequence(65)
	public void testObtenerReserva() throws ReservaInexistente, UsuarioInexistente, EtapaInexistente, PlanVacunacionInexistente, VacunatorioNoCargadoException, EnfermedadInexistente, CupoInexistente {
		try {
			utx.begin();
			Ciudadano c1 = em.find(Ciudadano.class, 21111111);
			Query queryPv = em.createQuery("SELECT pv FROM PlanVacunacion pv WHERE pv.nombre='pv1'");
			PlanVacunacion pv = (PlanVacunacion) queryPv.getSingleResult();
			
			cr.confirmarReserva(21111111, "virus1", pv.getId(), "vact1", LocalDate.now().plusDays(1), LocalTime.of(20, 0, 0), new DtUsuarioExterno("21111111", "", "industria", false));
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
			Query queryA = em.createQuery("SELECT a FROM Agenda a WHERE a.fecha='" +LocalDate.now().plusDays(1).format(formatter)+ "' AND vacunatorio_id='vact1'");
			Agenda a = (Agenda) queryA.getSingleResult();
			Reserva r1 = em.find(Reserva.class, new ReservaID(LocalDateTime.of(LocalDate.now().plusDays(1), LocalTime.of(20, 0, 0)), new EtapaID(pv.getEtapas().get(0).getId(),pv.getId()), 21111111));
			cr.obtenerReserva(21111111, pv.getId(), pv.getEtapas().get(0).getId(), LocalDateTime.of(LocalDate.now().plusDays(1), LocalTime.of(20, 0,0)));
			c1.getReservas().remove(r1);
			a.getReservas().remove(r1);
			em.merge(a);
			em.merge(c1);
			em.remove(r1);
			utx.commit();
		} catch (NotSupportedException | SystemException | SecurityException | IllegalStateException | RollbackException | HeuristicMixedException | HeuristicRollbackException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@OperateOnDeployment("normal")
	@Test(expected=UsuarioInexistente.class)
	@InSequence(67)
	public void testListarReservasCiudadanoSinCiudadano() throws ReservaInexistente, UsuarioInexistente {
		cr.listarReservasCiudadano(0);
	}
	
	@OperateOnDeployment("normal")
	@Test(expected=UsuarioInexistente.class)
	@InSequence(68)
	public void testListarReservasAEliminarSinCiudadano() throws ReservaInexistente, UsuarioInexistente {
		cr.listarReservasAEliminar(0);
	}
	
	@OperateOnDeployment("normal")
	@Test(expected=ReservaInexistente.class)
	@InSequence(69)
	public void testListarReservasCiudadanoSinReservas() throws ReservaInexistente, UsuarioInexistente {
		cr.listarReservasCiudadano(21111111);
	}
	
	@OperateOnDeployment("normal")
	@Test(expected=ReservaInexistente.class)
	@InSequence(70)
	public void testListarReservasAEliminarSinReservas() throws ReservaInexistente, UsuarioInexistente {
		cr.listarReservasAEliminar(21111111);
	}
	
	@OperateOnDeployment("normal")
	@Test
	@InSequence(71)
	public void testListarReservasCiudadano() throws ReservaInexistente, UsuarioInexistente, PlanVacunacionInexistente, VacunatorioNoCargadoException, EnfermedadInexistente, CupoInexistente, EtapaInexistente {
		try {
			utx.begin();
			Ciudadano c1 = em.find(Ciudadano.class, 21111111);
			Query queryPv = em.createQuery("SELECT pv FROM PlanVacunacion pv WHERE pv.nombre='pv1'");
			PlanVacunacion pv = (PlanVacunacion) queryPv.getSingleResult();
			cr.confirmarReserva(21111111, "virus1", pv.getId(), "vact1", LocalDate.now().plusDays(1), LocalTime.of(20, 0, 0), new DtUsuarioExterno("21111111", "", "industria", false));
			Reserva r1 = em.find(Reserva.class, new ReservaID(LocalDateTime.of(LocalDate.now().plusDays(1), LocalTime.of(20, 0, 0)), new EtapaID(pv.getEtapas().get(0).getId(),pv.getId()), 21111111));
			assertTrue(cr.listarReservasCiudadano(21111111).size()==1);
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
			Query queryA = em.createQuery("SELECT a FROM Agenda a WHERE a.fecha='" +LocalDate.now().plusDays(1).format(formatter)+ "' AND vacunatorio_id='vact1'");
			Agenda a = (Agenda) queryA.getSingleResult();
			c1.getReservas().remove(r1);
			a.getReservas().remove(r1);
			em.merge(a);
			em.merge(c1);
			em.remove(r1);
			utx.commit();
		} catch (NotSupportedException | SystemException | SecurityException | IllegalStateException | RollbackException | HeuristicMixedException | HeuristicRollbackException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@OperateOnDeployment("normal")
	@Test
	@InSequence(72)
	public void testListarReservasAEliminarCiudadano() throws ReservaInexistente, UsuarioInexistente, PlanVacunacionInexistente, VacunatorioNoCargadoException, EnfermedadInexistente, CupoInexistente, EtapaInexistente {
		try {
			utx.begin();
			Ciudadano c1 = em.find(Ciudadano.class, 21111111);
			Query queryPv = em.createQuery("SELECT pv FROM PlanVacunacion pv WHERE pv.nombre='pv1'");
			PlanVacunacion pv = (PlanVacunacion) queryPv.getSingleResult();
			cr.confirmarReserva(21111111, "virus1", pv.getId(), "vact1", LocalDate.now().plusDays(1), LocalTime.of(20, 0, 0), new DtUsuarioExterno("21111111", "", "industria", false));
			Reserva r1 = em.find(Reserva.class, new ReservaID(LocalDateTime.of(LocalDate.now().plusDays(1), LocalTime.of(20, 0, 0)), new EtapaID(pv.getEtapas().get(0).getId(),pv.getId()), 21111111));
			assertTrue(cr.listarReservasAEliminar(21111111).size()==1);
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
			Query queryA = em.createQuery("SELECT a FROM Agenda a WHERE a.fecha='" +LocalDate.now().plusDays(1).format(formatter)+ "' AND vacunatorio_id='vact1'");
			Agenda a = (Agenda) queryA.getSingleResult();
			c1.getReservas().remove(r1);
			a.getReservas().remove(r1);
			em.merge(a);
			em.merge(c1);
			em.remove(r1);
			utx.commit();
		} catch (NotSupportedException | SystemException | SecurityException | IllegalStateException | RollbackException | HeuristicMixedException | HeuristicRollbackException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@OperateOnDeployment("normal")
	@Test(expected=UsuarioInexistente.class)
	@InSequence(73)
	public void testEliminarReservaSinUsuario() throws ReservaInexistente, UsuarioInexistente, EnfermedadInexistente {
		cr.eliminarReserva(0, LocalDateTime.of(LocalDate.now().plusDays(1), LocalTime.of(20, 0, 0)), "virus1");
	}
	
	@OperateOnDeployment("normal")
	@Test(expected=EnfermedadInexistente.class)
	@InSequence(75)
	public void testEliminarReservaSinEnfermedad() throws ReservaInexistente, UsuarioInexistente, EnfermedadInexistente {
		cr.eliminarReserva(21111111, LocalDateTime.of(LocalDate.now().plusDays(1), LocalTime.of(20, 0, 0)), "virus0");
	}
	
	@OperateOnDeployment("normal")
	@Test(expected=ReservaInexistente.class)
	@InSequence(77)
	public void testEliminarReservaSinReserva() throws ReservaInexistente, UsuarioInexistente, EnfermedadInexistente {
		cr.eliminarReserva(21111111, LocalDateTime.of(LocalDate.now().plusDays(1), LocalTime.of(20, 0, 0)), "virus1");
	}
	
	@OperateOnDeployment("normal")
	@Test
	@InSequence(79)
	public void testEliminarReserva() throws ReservaInexistente, UsuarioInexistente, EnfermedadInexistente, PlanVacunacionInexistente, VacunatorioNoCargadoException, CupoInexistente, EtapaInexistente {
			Query queryPv = em.createQuery("SELECT pv FROM PlanVacunacion pv WHERE pv.nombre='pv1'");
			PlanVacunacion pv = (PlanVacunacion) queryPv.getSingleResult();
			cr.confirmarReserva(21111111, "virus1", pv.getId(), "vact1", LocalDate.now().plusDays(1), LocalTime.of(20, 0, 0), new DtUsuarioExterno("21111111", "", "industria", false));
			cr.eliminarReserva(21111111, LocalDateTime.of(LocalDate.now().plusDays(1), LocalTime.of(20, 0, 0)), "virus1");
	}
	
	@OperateOnDeployment("normal")
	@Test(expected=AccionInvalida.class)
	@InSequence(81)
	public void testCambiarEstadoReservaSinUsuario()throws AccionInvalida {
		cr.cambiarEstadoReserva(0, LocalDateTime.of(LocalDate.now().plusDays(1), LocalTime.of(20, 0, 0)), EstadoReserva.Completada);
	}
	
	@OperateOnDeployment("normal")
	@Test(expected=AccionInvalida.class)
	@InSequence(83)
	public void testCambiarEstadoReservaSinReservas()throws AccionInvalida {
		cr.cambiarEstadoReserva(21111111, LocalDateTime.of(LocalDate.now().plusDays(1), LocalTime.of(20, 0, 0)), EstadoReserva.Completada);
	}
	
	@OperateOnDeployment("normal")
	@Test(expected=AccionInvalida.class)
	@InSequence(85)
	public void testCambiarEstadoReservaSinPermiso()throws AccionInvalida, UsuarioInexistente, PlanVacunacionInexistente, VacunatorioNoCargadoException, EnfermedadInexistente, CupoInexistente, EtapaInexistente {
		Query queryPv = em.createQuery("SELECT pv FROM PlanVacunacion pv WHERE pv.nombre='pv1'");
		PlanVacunacion pv = (PlanVacunacion) queryPv.getSingleResult();
		cr.confirmarReserva(21111111, "virus1", pv.getId(), "vact1", LocalDate.now().plusDays(1), LocalTime.of(20, 0, 0), new DtUsuarioExterno("21111111", "", "industria", false));
		cr.cambiarEstadoReserva(21111111, LocalDateTime.of(LocalDate.now(), LocalTime.of(20, 0, 0)), EstadoReserva.Completada);
	}
	
	@OperateOnDeployment("normal")
	@Test
	@InSequence(87)
	public void testCambiarEstadoReserva()throws AccionInvalida, ReservaInexistente, UsuarioInexistente, EnfermedadInexistente {
		cr.cambiarEstadoReserva(21111111, LocalDateTime.of(LocalDate.now().plusDays(1), LocalTime.of(20, 0, 0)), EstadoReserva.Completada);
		try {
			utx.begin();
			Ciudadano c1 = em.find(Ciudadano.class, 21111111);
			List<ConstanciaVacuna> cv = c1.getCertificado().getConstancias();
			for (ConstanciaVacuna c: cv) {
				c.setReserva(null);
				em.merge(c);
			}
			c1.getCertificado().getConstancias().clear();
			em.merge(c1);
			utx.commit();
			cr.eliminarReserva(21111111, LocalDateTime.of(LocalDate.now().plusDays(1), LocalTime.of(20, 0, 0)), "virus1");
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
			Query queryPv1 = em.createQuery("SELECT pv FROM PlanVacunacion pv WHERE pv.nombre='pv1'");
			PlanVacunacion pv1 = (PlanVacunacion) queryPv1.getSingleResult();
			Query queryPv2 = em.createQuery("SELECT pv FROM PlanVacunacion pv WHERE pv.nombre='pv2'");
			PlanVacunacion pv2 = (PlanVacunacion) queryPv2.getSingleResult();
			Query queryPv3 = em.createQuery("SELECT pv FROM PlanVacunacion pv WHERE pv.nombre='pv3'");
			PlanVacunacion pv3 = (PlanVacunacion) queryPv3.getSingleResult();
			em.remove(em.find(Ciudadano.class, 21111111));
			em.remove(em.find(Ciudadano.class, 21111112));
			em.remove(em.find(PlanVacunacion.class, pv1.getId()));
			em.remove(em.find(PlanVacunacion.class, pv2.getId()));
			em.remove(em.find(PlanVacunacion.class, pv3.getId()));
			em.remove(em.find(Vacuna.class, "vac1"));
			em.remove(em.find(Laboratorio.class, "lab1"));
			em.remove(em.find(Enfermedad.class, "virus1"));
			em.remove(em.find(Enfermedad.class, "virus2"));
			
			em.remove(em.find(Vacunatorio.class, "vact1"));
			em.remove(em.find(ReglasCupos.class, "rc1"));
			utx.commit();
			
		} catch (NotSupportedException | SystemException | SecurityException | IllegalStateException | RollbackException | HeuristicMixedException | HeuristicRollbackException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
}
