//package init;
//
//import static org.junit.Assert.assertArrayEquals;
//import static org.junit.Assert.assertNull;
//
//import java.time.LocalDate;
//import java.time.LocalDateTime;
//import java.time.LocalTime;
//import java.time.format.DateTimeFormatter;
//import java.util.ArrayList;
//import java.util.logging.Logger;
//
//import javax.annotation.Resource;
//import javax.ejb.EJB;
//import javax.persistence.EntityManager;
//import javax.persistence.PersistenceContext;
//import javax.transaction.HeuristicMixedException;
//import javax.transaction.HeuristicRollbackException;
//import javax.transaction.NotSupportedException;
//import javax.transaction.RollbackException;
//import javax.transaction.SystemException;
//import javax.transaction.UserTransaction;
//
//import org.jboss.arquillian.container.test.api.Deployment;
//import org.jboss.arquillian.container.test.api.OperateOnDeployment;
//import org.jboss.arquillian.junit.Arquillian;
//import org.jboss.arquillian.junit.InSequence;
//import org.jboss.shrinkwrap.api.ShrinkWrap;
//import org.jboss.shrinkwrap.api.spec.JavaArchive;
//import org.junit.After;
//import org.junit.Before;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//
//import controllers.ControladorReserva;
//import datatypes.DtDireccion;
//import datatypes.DtEnfermedad;
//import datatypes.DtEtapa;
//import datatypes.DtPlanVacunacion;
//import datatypes.DtReserva;
//import datatypes.DtUsuarioExterno;
//import datatypes.EstadoReserva;
//import datatypes.Sexo;
//import entities.Ciudadano;
//import entities.Enfermedad;
//import entities.Etapa;
//import entities.Laboratorio;
//import entities.PlanVacunacion;
//import entities.Puesto;
//import entities.ReglasCupos;
//import entities.Reserva;
//import entities.Vacuna;
//import entities.Vacunatorio;
//import exceptions.CupoInexistente;
//import exceptions.EnfermedadInexistente;
//import exceptions.EtapaInexistente;
//import exceptions.PlanVacunacionInexistente;
//import exceptions.ReservaInexistente;
//import exceptions.UsuarioInexistente;
//import exceptions.VacunatorioNoCargadoException;
//import interfaces.IReservaDAORemote;
//import persistence.EtapaID;
//import persistence.ReservaID;
////ARREGLAR TESTS QUE CAMBIARON!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
////ARREGLAR TESTS QUE CAMBIARON!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
////ARREGLAR TESTS QUE CAMBIARON!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
////ARREGLAR TESTS QUE CAMBIARON!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
////ARREGLAR TESTS QUE CAMBIARON!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
////ARREGLAR TESTS QUE CAMBIARON!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
////ARREGLAR TESTS QUE CAMBIARON!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
////ARREGLAR TESTS QUE CAMBIARON!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
////ARREGLAR TESTS QUE CAMBIARON!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
////ARREGLAR TESTS QUE CAMBIARON!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
////ARREGLAR TESTS QUE CAMBIARON!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
//
//@RunWith(Arquillian.class)
//public class ReservaTest {
//	private final Logger LOGGER = Logger.getLogger(getClass().getName());
//	/*
//	@Deployment
//    public static JavaArchive createDeployment() {
//        return ShrinkWrap.create(JavaArchive.class, "testRes.jar")
//            //.addClasses(Reserva.class, IReservaDAORemote.class, IReservaDAOLocal.class,  ControladorReserva.class,
//           // 		Vacuna.class, DtVacuna.class, PlanVacunacion.class, Etapa.class, DtEtapa.class, EtapaID.class, ReservaID.class,DtPlanVacunacion.class,DtEnfermedad.class, DtReserva.class,
//            //		DtReservaCompleto.class, ReservaRepetida.class, ReservaInexistente.class, UsuarioInexistente.class, EtapaInexistente.class, PlanVacunacionInexistente.class, VacunatorioNoCargadoException.class, VacunatoriosNoCargadosException.class, EnfermedadInexistente.class, CupoInexistente.class, AccionInvalida.class)
//            //.addPackages(Reserva.class.getPackage(), IReservaDAORemote.class.getPackage(), ControladorReserva.class.getPackage(), DtReserva.class.getPackage(), Reserva.class.getPackage(), ReservaID.class.getPackage(), ReservaInexistente.class.getPackage())
//            .addPackages(false, Reserva.class.getPackage(), IReservaDAORemote.class.getPackage(), ControladorReserva.class.getPackage(), DtReserva.class.getPackage(), Reserva.class.getPackage(), ReservaID.class.getPackage(), ReservaInexistente.class.getPackage())
//            .addAsResource("META-INF/test-persistence.xml", "META-INF/persistence.xml")
//            ;
//    }
//	*/
//	@EJB
//	IReservaDAORemote cr;
//	@Resource
//	UserTransaction utx;
//	
//	@PersistenceContext
//	private EntityManager em;
//
//	@OperateOnDeployment("normal")
//	@Test(expected=EnfermedadInexistente.class)
//	@InSequence(3)
//	public void testSeleccionarEnfermedadInexistente() throws EnfermedadInexistente, PlanVacunacionInexistente {
//		cr.seleccionarEnfermedad("virus0");
//	}
//	
//	@OperateOnDeployment("normal")
//	@Test(expected=PlanVacunacionInexistente.class)
//	@InSequence(4)
//	public void testSeleccionarEnfermedadSinPlanes() throws EnfermedadInexistente, PlanVacunacionInexistente {
//		try {
//			utx.begin();
//			Enfermedad e1 = new Enfermedad("virus1");
//			Enfermedad e2 = new Enfermedad("virus2");
//			
//			em.persist(e1);
//			em.persist(e2);
//
//			utx.commit();
//			
//		} catch (NotSupportedException | SystemException | SecurityException | IllegalStateException | RollbackException | HeuristicMixedException | HeuristicRollbackException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		cr.seleccionarEnfermedad("virus1");
//	}
//	
//	@OperateOnDeployment("normal")
//	@Test
//	@InSequence(5)
//	public void testSeleccionarEnfermedadSinCura() throws EnfermedadInexistente, PlanVacunacionInexistente {
//		try {
//			utx.begin();
//			Enfermedad e1 = em.find(Enfermedad.class, "virus1");
//			Laboratorio l = new Laboratorio("lab1");
//			Vacuna v = new Vacuna("vac1", 1, 1, 1, l, e1);
//			PlanVacunacion pv1 = new PlanVacunacion("pv_n1", "pv_d1");
//			PlanVacunacion pv2 = new PlanVacunacion("pv_n2", "pv_d2");
//			PlanVacunacion pv3 = new PlanVacunacion("pv_n3", "pv_d3");
//			Etapa e_pv1 = new Etapa(1, LocalDate.of(2021, 1, 1), LocalDate.of(2022, 1, 1), "1|30|industria|no", pv1);
//			Etapa e_pv3 = new Etapa(3, LocalDate.of(2021, 1, 1), LocalDate.of(2022, 1, 1), "100|300|todos|si", pv3);
//			e_pv1.setVacuna(v);
//			e_pv3.setVacuna(v);
//			Ciudadano c1 = new Ciudadano(21111111, "Ciudadano", "DeTest Uno", LocalDate.of(2000, 1, 1), "c@1", null, Sexo.Otro, "Sector123456789" , false);
//			Ciudadano c2 = new Ciudadano(21111112, "Ciudadano", "DeTest Dos", LocalDate.of(1960, 1, 1), "c@2", null, Sexo.Otro, "Sector123456789" , false);
//			pv1.addEtapa(e_pv1);
//			pv3.addEtapa(e_pv3);
//			pv1.setEnfermedad(e1);
//			pv3.setEnfermedad(e1);
//			em.persist(l);
//			em.persist(v);
//			em.persist(e_pv1);
//			em.persist(e_pv3);
//			em.persist(pv1);
//			em.persist(pv2);
//			em.persist(pv3);
//			em.persist(c1);
//			em.persist(c2);
//			utx.commit();
//		} catch (NotSupportedException | SystemException | SecurityException | IllegalStateException | RollbackException | HeuristicMixedException | HeuristicRollbackException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		
//		
//		assertArrayEquals(new DtEnfermedad[0], cr.seleccionarEnfermedad("virus2").toArray());
//	}
//	
//	@OperateOnDeployment("normal")
//	@Test
//	@InSequence(6)
//	public void testSeleccionarEnfermedad() throws EnfermedadInexistente, PlanVacunacionInexistente {
//		ArrayList<DtPlanVacunacion> temp = new ArrayList<>();
//		ArrayList<DtEtapa> dtEtapa1 = new ArrayList<>();
//		ArrayList<DtEtapa> dtEtapa3 = new ArrayList<>();
//		//dtEtapa1.add(new DtEtapa(1, LocalDate.of(2021, 1, 1), LocalDate.of(2022, 1, 1), new DtPlanVacunacion()));
//		//dtEtapa3.add(new DtEtapa(3, LocalDate.of(2021, 1, 1), LocalDate.of(2022, 1, 1), new DtPlanVacunacion()));
//		//temp.add(new DtPlanVacunacion(1, "pv_n1", "pv_d1", dtEtapa1));
//		//temp.add(new DtPlanVacunacion(3, "pv_n3", "pv_d3", dtEtapa3));
//		//assertArrayEquals(temp.toArray(), cr.seleccionarEnfermedad("virus1").toArray());
//	}
//	
//	@OperateOnDeployment("normal")
//	@Test(expected=PlanVacunacionInexistente.class)
//	@InSequence(7)
//	public void testSeleccionarPlanInexistente() throws EtapaInexistente, UsuarioInexistente, PlanVacunacionInexistente {
//		cr.seleccionarPlanVacunacion(0, 0, new DtUsuarioExterno("21111111", "", "industria", false));
//	}
//	
//	@OperateOnDeployment("normal")
//	@Test(expected=EtapaInexistente.class)
//	@InSequence(8)
//	public void testSeleccionarPlanSinEtapas() throws EtapaInexistente, UsuarioInexistente, PlanVacunacionInexistente {
//		cr.seleccionarPlanVacunacion(2, 0, new DtUsuarioExterno("21111111", "", "industria", false));
//	}
//	
//	@OperateOnDeployment("normal")
//	@Test(expected=UsuarioInexistente.class)
//	@InSequence(9)
//	public void testSeleccionarPlanUsuarioInexistente() throws EtapaInexistente, UsuarioInexistente, PlanVacunacionInexistente {
//		cr.seleccionarPlanVacunacion(1, 0, new DtUsuarioExterno("21111111", "", "industria", false));
//	}
//	
//	@OperateOnDeployment("normal")
//	@Test(expected=EtapaInexistente.class)
//	@InSequence(10)
//	public void testSeleccionarPlanUsuarioInabilitado() throws EtapaInexistente, UsuarioInexistente, PlanVacunacionInexistente {
//		cr.seleccionarPlanVacunacion(1, 21111112, new DtUsuarioExterno("21111112", "", "industria", false));
//	}
//	
//	@OperateOnDeployment("normal")
//	@Test(expected=EtapaInexistente.class)
//	@InSequence(11)
//	public void testSeleccionarPlanUsuarioConReserva() throws EtapaInexistente, UsuarioInexistente, PlanVacunacionInexistente {
//		try {
//			utx.begin();
//			Ciudadano c1 = em.find(Ciudadano.class, 21111111);
//			PlanVacunacion pv1 = em.find(PlanVacunacion.class, 1);
//			Etapa e_pv1 = pv1.getEtapas().get(0);
//			Reserva r1 = new Reserva(LocalDateTime.of(LocalDate.now(), LocalTime.of(20, 0, 0)), EstadoReserva.EnProceso, e_pv1, c1, null);
//			c1.getReservas().add(r1);
//			em.merge(c1);
//			em.merge(r1);
//			utx.commit();
//			
//			cr.seleccionarPlanVacunacion(1, 21111111, new DtUsuarioExterno("21111111", "", "industria", false));
//			
//
//		} catch (NotSupportedException | SystemException | SecurityException | IllegalStateException | RollbackException | HeuristicMixedException | HeuristicRollbackException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		
//	}
//	
//	@OperateOnDeployment("normal")
//	@Test
//	@InSequence(12)
//	public void testSeleccionarPlan() throws EtapaInexistente, UsuarioInexistente, PlanVacunacionInexistente {
//		try {
//			utx.begin();
//			Ciudadano c1 = em.find(Ciudadano.class, 21111111);
//			Reserva r1 = em.find(Reserva.class, new ReservaID(LocalDateTime.of(LocalDate.now(), LocalTime.of(20, 0, 0)), new EtapaID(1,1), 21111111));
//			c1.getReservas().remove(r1);
//			em.merge(c1);
//			em.remove(r1);
//			utx.commit();
//		} catch (NotSupportedException | SystemException | SecurityException | IllegalStateException | RollbackException | HeuristicMixedException | HeuristicRollbackException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		
//		assertArrayEquals(new DtEtapa[0], cr.seleccionarPlanVacunacion(1, 21111111, new DtUsuarioExterno("21111111", "", "industria", false)).toArray());
//	}
//	
//	@OperateOnDeployment("normal")
//	@Test(expected=VacunatorioNoCargadoException.class)
//	@InSequence(13)
//	public void testSeleccionarFechaSinVacunatorio() throws VacunatorioNoCargadoException, PlanVacunacionInexistente, UsuarioInexistente, EtapaInexistente, CupoInexistente{
//			cr.seleccionarFecha(LocalDate.now().plusDays(1), "", 1, 21111111, new DtUsuarioExterno("21111111", "", "industria", false));
//	}
//	
//	@OperateOnDeployment("normal")
//	@Test(expected=PlanVacunacionInexistente.class)
//	@InSequence(14)
//	public void testSeleccionarFechaPlanInexistente() throws VacunatorioNoCargadoException, PlanVacunacionInexistente, UsuarioInexistente, EtapaInexistente, CupoInexistente{
//		try {
//			utx.begin();
//			ReglasCupos rc1 = new ReglasCupos("rc1", 15,  LocalTime.of(0, 0, 0),  LocalTime.of(23, 59, 59));
//			Vacunatorio vact1 = new Vacunatorio("vact1", "vact1_n", null, 0, 0F, 0F);
//			vact1.setReglasCupos(rc1);
//			em.persist(rc1);
//			em.persist(vact1);
//			utx.commit();
//		} catch (NotSupportedException | SystemException | SecurityException | IllegalStateException | RollbackException | HeuristicMixedException | HeuristicRollbackException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		cr.seleccionarFecha(LocalDate.now().plusDays(1), "vact1", 0, 21111111, new DtUsuarioExterno("21111111", "", "industria", false));
//	}
//	
//	@OperateOnDeployment("normal")
//	@Test(expected=EtapaInexistente.class)
//	@InSequence(15)
//	public void testSeleccionarFechaPlanSinEtapas() throws VacunatorioNoCargadoException, PlanVacunacionInexistente, UsuarioInexistente, EtapaInexistente, CupoInexistente{
//		cr.seleccionarFecha(LocalDate.now().plusDays(1), "vact1", 2, 21111111, new DtUsuarioExterno("21111111", "", "industria", false));
//	}
//	
//	@OperateOnDeployment("normal")
//	@Test(expected=UsuarioInexistente.class)
//	@InSequence(16)
//	public void testSeleccionarFechaUsuarioInexistente() throws VacunatorioNoCargadoException, PlanVacunacionInexistente, UsuarioInexistente, EtapaInexistente, CupoInexistente{
//		cr.seleccionarFecha(LocalDate.now().plusDays(1), "vact1", 1, 0, new DtUsuarioExterno("21111111", "", "industria", false));
//	}
//	
//	@OperateOnDeployment("normal")
//	@Test(expected=EtapaInexistente.class)
//	@InSequence(17)
//	public void testSeleccionarFechaUsuarioInabilitado() throws VacunatorioNoCargadoException, PlanVacunacionInexistente, UsuarioInexistente, EtapaInexistente, CupoInexistente{
//		cr.seleccionarFecha(LocalDate.now().plusDays(1), "vact1", 1, 21111112, new DtUsuarioExterno("21111112", "", "industria", false));
//	}
//	
//	@OperateOnDeployment("normal")
//	@Test(expected=CupoInexistente.class)
//	@InSequence(18)
//	public void testSeleccionarFechaIncorrecta() throws VacunatorioNoCargadoException, PlanVacunacionInexistente, UsuarioInexistente, EtapaInexistente, CupoInexistente{
//		cr.seleccionarFecha(LocalDate.now(), "vact1", 1, 21111111, new DtUsuarioExterno("21111112", "", "industria", false));
//	}
//	
//	@OperateOnDeployment("normal")
//	@Test
//	@InSequence(19)
//	public void testSeleccionarFechaSinPuestos() throws VacunatorioNoCargadoException, PlanVacunacionInexistente, UsuarioInexistente, EtapaInexistente, CupoInexistente{
//		assertArrayEquals(new String[0], cr.seleccionarFecha(LocalDate.now().plusDays(1), "vact1", 1, 21111111, new DtUsuarioExterno("21111111", "", "industria", false)).toArray());
//	}
//	
//	@OperateOnDeployment("normal")
//	@Test
//	@InSequence(20)
//	public void testSeleccionarFecha() throws VacunatorioNoCargadoException, PlanVacunacionInexistente, UsuarioInexistente, EtapaInexistente, CupoInexistente{
//		try {
//			utx.begin();
//			Vacunatorio vact1 = em.find(Vacunatorio.class, "vact1");
//			Puesto p1_vact1 = new Puesto("p1_vact1", vact1);
//			vact1.getPuesto().add(p1_vact1);
//			em.merge(vact1);
//			utx.commit();
//		} catch (NotSupportedException | SystemException | SecurityException | IllegalStateException | RollbackException | HeuristicMixedException | HeuristicRollbackException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
//		ArrayList<String> temp = new ArrayList<>();
//		LocalTime init = LocalTime.of(0, 0, 0);
//		temp.add(init.format(formatter));
//		for (int i = 0; i < 94; i++) {
//			init = init.plusMinutes(15);
//			temp.add(init.format(formatter));
//		}
//		
//		assertArrayEquals(temp.toArray(), cr.seleccionarFecha(LocalDate.now().plusDays(1), "vact1", 1, 21111111, new DtUsuarioExterno("21111111", "", "industria", false)).toArray());
//	}
//	
//	@OperateOnDeployment("normal")
//	@Test
//	@InSequence(50)
//	public void testClean()  {
//		try {
//			utx.begin();
//			
//			em.remove(em.find(PlanVacunacion.class, 1));
//			em.remove(em.find(PlanVacunacion.class, 2));
//			em.remove(em.find(PlanVacunacion.class, 3));
//			em.remove(em.find(Vacuna.class, "vac1"));
//			em.remove(em.find(Laboratorio.class, "lab1"));
//			em.remove(em.find(Enfermedad.class, "virus1"));
//			em.remove(em.find(Enfermedad.class, "virus2"));
//			em.remove(em.find(Ciudadano.class, 21111111));
//			em.remove(em.find(Ciudadano.class, 21111112));
//			em.remove(em.find(Vacunatorio.class, "vact1"));
//			em.remove(em.find(ReglasCupos.class, "rc1"));
//			utx.commit();
//			
//		} catch (NotSupportedException | SystemException | SecurityException | IllegalStateException | RollbackException | HeuristicMixedException | HeuristicRollbackException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	}
//}
