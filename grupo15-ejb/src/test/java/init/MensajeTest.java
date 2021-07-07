package init;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

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
import datatypes.DtMensaje;
import datatypes.DtReserva;
import entities.Mensaje;
import entities.Reserva;
import exceptions.MensajeExistente;
import exceptions.ReservaInexistente;
import interfaces.IMensajeRemote;
import interfaces.IReservaDAORemote;
import persistence.ReservaID;

@RunWith(Arquillian.class)
public class MensajeTest {
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
	IMensajeRemote cm;
	@Resource
	UserTransaction utx;
	
	@PersistenceContext
	private EntityManager em;
	
//	@OperateOnDeployment("normal")
//	@Test(expected=MensajeExistente.class)
//	@InSequence(1)
//	public void testBuscarMensajeSinMensaje() throws MensajeExistente {
//		cm.BuscarMensaje(5);
//	}
//	
//	@OperateOnDeployment("normal")
//	@Test(expected=MensajeExistente.class)
//	@InSequence(2)
//	public void testModificarMensajeSinMensaje() throws MensajeExistente {
//		DtMensaje dtMensaje = new DtMensaje();
//		try {
//			utx.begin();
//			dtMensaje = new DtMensaje(5, "contenido");
//			utx.commit();
//		}catch(SystemException | NotSupportedException | SecurityException | IllegalStateException | RollbackException | HeuristicMixedException | HeuristicRollbackException e) {
//			e.printStackTrace();
//		}
//		cm.ModificarMensaje(dtMensaje);
//	}
//	
//	@OperateOnDeployment("normal")
//	@Test(expected=MensajeExistente.class)
//	@InSequence(3)
//	public void testEliminarMensajeSinMensaje() throws MensajeExistente {
//		cm.EliminarMensaje(5);
//	}
	
	@OperateOnDeployment("normal")
	@Test
	@InSequence(4)
	public void testAgregarMensaje() {
		cm.agregarMensaje("contenidoMensaje");
	}
	
//	@OperateOnDeployment("normal")
//	@Test
//	@InSequence(5)
//	public void testBuscarMensaje() throws MensajeExistente {
//		DtMensaje dtMensaje = new DtMensaje();
//		int mensajeId = 0;
//		try {
//			utx.begin();
//			Query queryM = em.createQuery("SELECT m FROM Mensaje m");
//			List<Mensaje> mensajes = (List)queryM.getResultList();
//			mensajeId = mensajes.get(0).getIdMensaje();
//			dtMensaje = new DtMensaje(mensajeId, "contenidoMensaje");
//			utx.commit();
//		}catch(SystemException | NotSupportedException | SecurityException | IllegalStateException | RollbackException | HeuristicMixedException | HeuristicRollbackException e) {
//			e.printStackTrace();
//		}
//		cm.BuscarMensaje(mensajeId);
//		//assertEquals(cm.BuscarMensaje(mensajeId), dtMensaje);
//	}
//	
//	@OperateOnDeployment("normal")
//	@Test
//	@InSequence(6)
//	public void testModificarMensaje() throws MensajeExistente {
//		Query queryM = em.createQuery("SELECT m FROM Mensaje m");
//		List<Mensaje> mensajes = (List)queryM.getResultList();
//		int mensajeId = mensajes.get(0).getIdMensaje();
//		DtMensaje dtMensaje = new DtMensaje(mensajeId, "nuevoContenido");
//		cm.ModificarMensaje(dtMensaje);
//	}
	
	@OperateOnDeployment("normal")
	@Test
	@InSequence(7)
	public void testListarMensajes() {
			Query queryM = em.createQuery("SELECT m FROM Mensaje m");
			ArrayList<Mensaje> mensajes = (ArrayList)queryM.getResultList();
			//assertArrayEquals(mensajes.toArray(), cm.listarMensajes().toArray());
			cm.listarMensajes();
	}
	
//	@OperateOnDeployment("normal")
//	@Test
//	@InSequence(8)
//	public void testEliminarMensaje() throws MensajeExistente {
//		Query queryM = em.createQuery("SELECT m FROM Mensaje m");
//		List<Mensaje> mensajes = (List)queryM.getResultList();
//		int mensajeId = mensajes.get(0).getIdMensaje();
//		cm.EliminarMensaje(mensajeId);
//	}
	
//	@OperateOnDeployment("normal")
//	@Test
//	@InSequence()
//	public void testClean() {
//		
//	}
	
}
