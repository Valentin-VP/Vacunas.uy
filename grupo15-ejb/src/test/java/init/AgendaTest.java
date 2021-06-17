//package init;
//
//import static org.junit.Assert.assertArrayEquals;
//
//import java.time.LocalDate;
//import java.time.LocalDateTime;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.logging.Logger;
//
//import javax.annotation.Resource;
//import javax.ejb.EJB;
//import javax.persistence.EntityManager;
//import javax.persistence.PersistenceContext;
//import javax.persistence.Query;
//import javax.transaction.HeuristicMixedException;
//import javax.transaction.HeuristicRollbackException;
//import javax.transaction.NotSupportedException;
//import javax.transaction.RollbackException;
//import javax.transaction.SystemException;
//import javax.transaction.UserTransaction;
//
//import org.jboss.arquillian.container.test.api.OperateOnDeployment;
//import org.jboss.arquillian.junit.Arquillian;
//import org.jboss.arquillian.junit.InSequence;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//
//import datatypes.DtAgenda;
//import datatypes.DtDireccion;
//import datatypes.DtReserva;
//import datatypes.DtReservaCompleto;
//import datatypes.EstadoReserva;
//import datatypes.Sexo;
//import entities.Agenda;
//import entities.Ciudadano;
//import entities.Enfermedad;
//import entities.Etapa;
//import entities.Laboratorio;
//import entities.PlanVacunacion;
//import entities.Reserva;
//import entities.Vacuna;
//import entities.Vacunatorio;
//import exceptions.AgendaInexistente;
//import exceptions.AgendaRepetida;
//import exceptions.CupoInexistente;
//import exceptions.VacunatorioNoCargadoException;
//import interfaces.IAgendaDAOLocal;
//
//@RunWith(Arquillian.class)
//public class AgendaTest {
//	private final Logger LOGGER = Logger.getLogger(getClass().getName());
//	
//	@EJB
//	IAgendaDAOLocal ca;
//	@Resource
//	UserTransaction utx;
//	
//	@PersistenceContext
//	private EntityManager em;
//	
//	@OperateOnDeployment("normal")
//	@Test(expected=VacunatorioNoCargadoException.class)
//	@InSequence(1)
//	public void testListarAgendaSinVacunatorio() throws AgendaInexistente, VacunatorioNoCargadoException {
//		ca.listarAgendas("vacunatorio1");
//	}
//	
//	@OperateOnDeployment("normal")
//	@Test(expected=VacunatorioNoCargadoException.class)
//	@InSequence(2)
//	public void testAgregarAgendaSinVacunatorio() throws AgendaRepetida, CupoInexistente, VacunatorioNoCargadoException {
//		LocalDate tempDate = LocalDate.of(1,2,3);
//		ca.agregarAgenda("vacunatorio1", tempDate);
//	}
//	
//	@OperateOnDeployment("normal")
//	@Test(expected=AgendaInexistente.class)
//	@InSequence(3)
//	public void testListarAgendasNull() throws AgendaInexistente, VacunatorioNoCargadoException {
//		DtDireccion tempDir = new DtDireccion("a","b","c");
//		Vacunatorio vacunatorio1 = new Vacunatorio("vacunatorio1", "Nest1", null, 1555897235, 1.0f, 1.0f);
//		//Vacunatorio vacunatorio1 = new Vacunatorio("vacunatorio1", "vacunatorio1", tempDir, 1555897235, 1.0f, 1.0f);
//		try {
//			utx.begin();
//			em.persist(vacunatorio1);//sacar de la base de datos en testClean
//			utx.commit();
//		} catch(SystemException | NotSupportedException | SecurityException | IllegalStateException | RollbackException | HeuristicMixedException | HeuristicRollbackException e) {
//			e.printStackTrace();
//		}
//		ca.listarAgendas("vacunatorio1");
//	}
//	
//	@OperateOnDeployment("normal")
//	@Test
//	@InSequence(4)
//	public void testAgregarAgenda() throws AgendaRepetida, CupoInexistente, VacunatorioNoCargadoException {
//		LocalDate tempDate = LocalDate.of(1,2,3);
//		ca.agregarAgenda("vacunatorio1", tempDate);
//		
//		tempDate = LocalDate.of(4,5,6);
//		//DtDireccion tempDir = new DtDireccion("g","h","i");
//		Sexo s1 = Sexo.valueOf("Masculino");
//		Ciudadano c1 = new Ciudadano(1, "c1", "ap1", tempDate, "mail", null, s1, "sector1", true);
//		PlanVacunacion pv1 = new PlanVacunacion("plan1", "plan1");
//		LocalDate tempDateInicio = LocalDate.of(4,5,6);
//		LocalDate tempDateFin = LocalDate.of(4,5,6);
//		Etapa e1 = new Etapa(1, tempDateInicio, tempDateFin, "activa", pv1);
//		try {
//			utx.begin();
//			em.persist(c1);
//			//em.persist(pv1);
//			em.persist(e1);
//			utx.commit();
//		} catch(SystemException | SecurityException | IllegalStateException | RollbackException | HeuristicMixedException | HeuristicRollbackException | NotSupportedException e) {
//			e.printStackTrace();
//		}
//	}
//	
//	@OperateOnDeployment("normal")
//	@Test(expected=AgendaRepetida.class)
//	@InSequence(5)
//	public void testAgregarAgendaError() throws AgendaRepetida, CupoInexistente, VacunatorioNoCargadoException {
//		LocalDate tempDate = LocalDate.of(1,2,3);
//		ca.agregarAgenda("vacunatorio1", tempDate);
//	}
//	
//	@OperateOnDeployment("normal")
//	@Test(expected=AgendaInexistente.class)
//	@InSequence(6)
//	public void testObtenerAgendaNull() throws AgendaInexistente {
//		LocalDate tempDate = LocalDate.of(1,2,3);
//		ca.obtenerAgenda("vacunatorio1", tempDate);
//	}
//	
//	@OperateOnDeployment("normal")
//	@Test(expected=AgendaInexistente.class)
//	@InSequence(7)
//	public void testObtenerAgendaSoapNull() throws AgendaInexistente {
//		LocalDate tempDate = LocalDate.of(1,2,3);
//		ca.obtenerAgendaSoap("vacunatorio1", tempDate);
//	}
//	
//	@OperateOnDeployment("normal")
//	@Test
//	@InSequence(8)
//	public void testObtenerAgenda() throws AgendaInexistente {
//		List<Reserva> listaReservas = new ArrayList<>();
//		Query queryA = em.createQuery("SELECT v FROM Vacunatorio v Where v.id='vacunatorio1'");
//		Vacunatorio v1 = (Vacunatorio)queryA.getSingleResult();
//		List<Agenda> agendas = v1.getAgenda();
//		LocalDate agendaDate = LocalDate.of(1,2,3);
//		Agenda a1 = new Agenda();
//		if (agendas == null)
//			System.out.println("Agenda es nulo");
//		for (Agenda a : agendas) {
//			if (a.getFecha().isEqual(agendaDate)) {
//				a.setReservas(listaReservas);
//				a1 = a;
//			}
//		}
//		Query queryC = em.createQuery("SELECT c FROM Ciudadano c WHERE c.nombre='c1'");
//		Query queryE = em.createQuery("SELECT e FROM Etapa e WHERE e.id=1");
//		Ciudadano c1 = (Ciudadano)queryC.getSingleResult();
//		Etapa e1 = (Etapa)queryE.getSingleResult();
//		
//		LocalDateTime tempDateRegistro = LocalDateTime.of(4,5,6,7,8);
//		EstadoReserva er1 = EstadoReserva.valueOf("Completada");
//		EstadoReserva er2 = EstadoReserva.valueOf("Cancelada");
//		Reserva r1 = new Reserva(tempDateRegistro, er1, e1, c1, null);
//		Reserva r2 = new Reserva(tempDateRegistro, er2, e1, c1, null);
//		
//		listaReservas.add(r1);
//		listaReservas.add(r2);
//		try {
//			utx.begin();
//			em.persist(a1);
//			utx.commit();
//		} catch(SystemException | NotSupportedException | SecurityException | IllegalStateException | RollbackException | HeuristicMixedException | HeuristicRollbackException e) {
//			e.printStackTrace();
//		}
//		ArrayList<DtReserva> tempReservas = new ArrayList<>();
//		tempReservas.add(r1.getDtReserva());
//		tempReservas.add(r2.getDtReserva());
//		LocalDate tempDate = LocalDate.of(1,2,3);
//		assertArrayEquals(tempReservas.toArray(), ca.obtenerAgenda("vacunatorio1", tempDate).toArray());
//	}
//
//	@OperateOnDeployment("normal")
//	@Test
//	@InSequence(9)
//	public void testListarAgendas() throws AgendaInexistente, VacunatorioNoCargadoException {
//		Query queryA = em.createQuery("SELECT v FROM Vacunatorio v Where v.id='vacunatorio1'");
//		Vacunatorio v = (Vacunatorio)queryA.getSingleResult();
//		List<Agenda> agendas = v.getAgenda();
//		LocalDate agendaDate = LocalDate.of(1,2,3);
//		Agenda a1 = null;
//		for (Agenda a : agendas) {
//			if (a.getFecha().isEqual(agendaDate)) {
//				a1 = a;
//			}
//		}
//		ArrayList<DtAgenda> tempAgenda = new ArrayList<>();
//		ArrayList<DtReserva> tempReservas = new ArrayList<>();
//		for(Reserva r: a1.getReservas()) {
//			tempReservas.add(r.getDtReserva());
//		}
//		DtAgenda tempDtAgenda = new DtAgenda(a1.getFecha(), tempReservas);
//		tempAgenda.add(tempDtAgenda);
//		assertArrayEquals(tempAgenda.toArray(), ca.listarAgendas("vacunatorio1").toArray());
//	}
//	
//	@OperateOnDeployment("normal")
//	@Test(expected=AssertionError.class)
//	@InSequence(10)
//	public void listarAgendasError() throws AgendaInexistente, VacunatorioNoCargadoException{
//		ArrayList<DtAgenda> tempAgenda = new ArrayList<>();
//		assertArrayEquals(tempAgenda.toArray(), ca.listarAgendas("vacunatorio1").toArray());
//	}
//	
//	@OperateOnDeployment("normal")
//	@Test
//	@InSequence(11)
//	public void testObtenerAgendaSoap() throws AgendaInexistente {
//		Query queryA = em.createQuery("SELECT v FROM Vacunatorio v Where v.id='vacunatorio1'");
//		Vacunatorio v1 = (Vacunatorio)queryA.getSingleResult();
//		List<Agenda> agendas = v1.getAgenda();
//		LocalDate agendaDate = LocalDate.of(1,2,3);
//		Agenda a1 = new Agenda();
//		for (Agenda a : agendas) {
//			if (a.getFecha().isEqual(agendaDate)) {
//				a1 = a;
//			}
//		}
//		ArrayList<DtReservaCompleto> tempReservasCompletas = new ArrayList<>();
//		for(Reserva r : a1.getReservas()) {
//			tempReservasCompletas.add(r.getDtReservaCompleto());
//		}
//		assertArrayEquals(tempReservasCompletas.toArray(), ca.obtenerAgendaSoap("vacunatorio1", agendaDate).toArray());
//	}
//	
//	@OperateOnDeployment("normal")
//	@Test
//	@InSequence(20)
//	public void testClean() {
//		try {
//			utx.begin();
//			Query queryC = em.createQuery("SELECT c FROM Ciudadano c WHERE c.nombre='c1'");
//			Query queryPv = em.createQuery("SELECT pv FROM PlanVacunacion pv WHERE pv.nombre='plan1'");
//			Query queryE = em.createQuery("SELECT e FROM Etapa e WHERE e.id=1");
//			Query queryA = em.createQuery("SELECT a FROM Vacunatorio v Where v.vacunarorio_id='vacunatorio1'");
//			Vacunatorio v1 = (Vacunatorio)queryA.getSingleResult();
//			List<Agenda> agendas = v1.getAgenda();
//			LocalDate agendaDate = LocalDate.of(1,2,3);
//			Agenda a1 = null;
//			for (Agenda a : agendas) {
//				if (a.getFecha().isEqual(agendaDate)) {
//					a1 = a;
//				}
//			}
//			Ciudadano c = (Ciudadano)queryC.getSingleResult();
//			PlanVacunacion pv = (PlanVacunacion)queryPv.getSingleResult();
//			Etapa e = (Etapa)queryE.getSingleResult();
//			em.remove(a1);
//			em.remove(c);
//			em.remove(pv);
//			em.remove(e);
//			utx.commit();
//		} catch (NotSupportedException | SystemException | SecurityException | IllegalStateException | RollbackException | HeuristicMixedException | HeuristicRollbackException e) {
//			e.printStackTrace();
//		}
//	}
//	
//}
