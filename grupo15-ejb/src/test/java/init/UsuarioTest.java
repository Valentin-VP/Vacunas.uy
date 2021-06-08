package init;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

import java.time.LocalDate;
import java.util.ArrayList;
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

import org.jboss.arquillian.container.test.api.OperateOnDeployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.junit.InSequence;
import org.junit.Test;
import org.junit.runner.RunWith;

import datatypes.DtCiudadano;
import datatypes.DtUsuarioInterno;
import datatypes.DtUsuarioSoap;
import datatypes.DtVacunador;
import datatypes.Rol;
import datatypes.Sexo;
import entities.Ciudadano;
import entities.UsuarioInterno;
import entities.Vacunador;
import exceptions.UsuarioExistente;
import exceptions.UsuarioInexistente;
import interfaces.IUsuarioRemote;

@RunWith(Arquillian.class)
public class UsuarioTest {
	private final Logger LOGGER = Logger.getLogger(getClass().getName());
	
	@EJB
	IUsuarioRemote cu;
	
	@Resource
	UserTransaction utx;
	
	@PersistenceContext
	private EntityManager em;
	
	@OperateOnDeployment("normal")
	@Test
	@InSequence(1)
	public void testListarCiudadanoInexistente() {
		cu.listarCiudadanos();
	}
	
	@OperateOnDeployment("normal")
	@Test
	@InSequence(2)
	public void testListarInternoInexistente() {
		cu.listarUsuariosInternos();
	}
	
	@OperateOnDeployment("normal")
	@Test
	@InSequence(3)
	public void testListarVacunadoresInexistente() {
		cu.listarVacunadores();
	}
	
	@OperateOnDeployment("normal")
	@Test
	@InSequence(4)
	public void testListarVacunadoresSoapInexistente() {
		cu.listarVacunadoresSoap();
	}
	
	@OperateOnDeployment("normal")
	@Test
	@InSequence(5)
	public void testListarCiudadano() {
		try {
			utx.begin();
			Vacunador v1 = new Vacunador(11111111, "Vacunador", "DeTest Uno", LocalDate.of(2000, 1, 1), "v@1", null, Sexo.Otro);
			Ciudadano c1 = new Ciudadano(21111111, "Ciudadano", "DeTest Uno", LocalDate.of(2000, 1, 1), "c@1", null, Sexo.Otro, "Sector123456789" , false);
			UsuarioInterno i1 = new UsuarioInterno(31111111, "Interno", "DeTest Uno", LocalDate.of(2000, 1, 1), "i@1", null, Sexo.Otro, Rol.Administrador);
			em.persist(v1);
			em.persist(c1);
			em.persist(i1);
			utx.commit();
		} catch (NotSupportedException | SystemException | SecurityException | IllegalStateException | RollbackException | HeuristicMixedException | HeuristicRollbackException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		ArrayList<DtCiudadano> dt = new ArrayList<>();
		dt.add(new DtCiudadano(21111111, "Ciudadano", "DeTest Uno", LocalDate.of(2000, 1, 1), "c@1", null, Sexo.Otro, "Sector123456789" , false));
		assertArrayEquals(dt.toArray(), cu.listarCiudadanos().toArray());
	}
	
	@OperateOnDeployment("normal")
	@Test
	@InSequence(6)
	public void testListarInterno() {
		ArrayList<DtUsuarioInterno> dt = new ArrayList<>();
		dt.add(new DtUsuarioInterno("Interno", "DeTest Uno", LocalDate.of(2000, 1, 1), 31111111, "i@1", null, Sexo.Otro, Rol.Administrador));
		assertArrayEquals(dt.toArray(), cu.listarUsuariosInternos().toArray());
	}
	
	@OperateOnDeployment("normal")
	@Test
	@InSequence(7)
	public void testListarVacunadores() {
		ArrayList<DtVacunador> dt = new ArrayList<>();
		dt.add(new DtVacunador("Vacunador", "DeTest Uno", LocalDate.of(2000, 1, 1), 11111111, "v@1", null, Sexo.Otro));
		assertArrayEquals(dt.toArray(), cu.listarVacunadores().toArray());
	}
	
	@OperateOnDeployment("normal")
	@Test
	@InSequence(8)
	public void testListarVacunadoresSoap() {
		ArrayList<DtUsuarioSoap> dt = new ArrayList<>();
		dt.add(new DtUsuarioSoap("Vacunador", "DeTest Uno", "01-01-2000", 11111111, "v@1", null, Sexo.Otro));
		assertArrayEquals(dt.toArray(), cu.listarVacunadoresSoap().toArray());
	}
	
	@OperateOnDeployment("normal")
	@Test(expected=UsuarioExistente.class)
	@InSequence(9)
	public void testAgregarVacunadorRepetido() throws UsuarioExistente {
		cu.agregarUsuarioVacunador(11111111, "Vacunador", "DeTest Uno", LocalDate.of(2000, 1, 1), "v@1", null, Sexo.Otro);
	}
	
	@OperateOnDeployment("normal")
	@Test(expected=UsuarioExistente.class)
	@InSequence(10)
	public void testAgregarCiudadanoRepetido() throws UsuarioExistente {
		cu.agregarUsuarioCiudadano(21111111, "Ciudadano", "DeTest Uno", LocalDate.of(2000, 1, 1), "c@1", null, Sexo.Otro, "Sector123456789" , false);
	}
	
	@OperateOnDeployment("normal")
	@Test(expected=UsuarioExistente.class)
	@InSequence(11)
	public void testAgregarInternoRepetido() throws UsuarioExistente {
		cu.agregarUsuarioInterno(31111111, "Interno", "DeTest Uno", LocalDate.of(2000, 1, 1), "i@1", null, Sexo.Otro, Rol.Administrador);
	}
	
	@OperateOnDeployment("normal")
	@Test
	@InSequence(12)
	public void testAgregarVacunador() throws UsuarioExistente {
		cu.agregarUsuarioVacunador(11111112, "Vacunador", "DeTest Dos", LocalDate.of(2000, 1, 2), "v@2", null, Sexo.Otro);
	}
	
	@OperateOnDeployment("normal")
	@Test
	@InSequence(13)
	public void testAgregarCiudadano() throws UsuarioExistente {
		cu.agregarUsuarioCiudadano(21111112, "Ciudadano", "DeTest Dos", LocalDate.of(2000, 1, 2), "c@2", null, Sexo.Otro, "Sector123456789" , false);
	}
	
	@OperateOnDeployment("normal")
	@Test
	@InSequence(14)
	public void testAgregarInterno() throws UsuarioExistente {
		cu.agregarUsuarioInterno(31111112, "Interno", "DeTest Dos", LocalDate.of(2000, 1, 2), "i@2", null, Sexo.Otro, Rol.Administrador);
	}
	
	@OperateOnDeployment("normal")
	@Test(expected=UsuarioInexistente.class)
	@InSequence(15)
	public void testBuscarVacunadorInexistente() throws UsuarioInexistente {
		cu.buscarVacunador(21111112);
	}
	
	@OperateOnDeployment("normal")
	@Test(expected=UsuarioInexistente.class)
	@InSequence(16)
	public void testBuscarVacunadorSoapInexistente() throws UsuarioInexistente {
		cu.buscarVacunadorSoap(31111112);
	}
	
	@OperateOnDeployment("normal")
	@Test(expected=UsuarioInexistente.class)
	@InSequence(17)
	public void testBuscarCiudadanoInexistente() throws UsuarioInexistente {
		cu.buscarCiudadano(11111112);
	}
	
	@OperateOnDeployment("normal")
	@Test(expected=UsuarioInexistente.class)
	@InSequence(18)
	public void testBuscarInternoInexistente() throws UsuarioInexistente {
		cu.buscarUsuarioInterno(21111112);
	}
	
	@OperateOnDeployment("normal")
	@Test
	@InSequence(19)
	public void testBuscarVacunador() throws UsuarioInexistente {
		DtVacunador dt = new DtVacunador("Vacunador", "DeTest Dos", LocalDate.of(2000, 1, 2), 11111112, "v@2", null, Sexo.Otro);
		assertEquals(dt, cu.buscarVacunador(11111112));
	}
	
	@OperateOnDeployment("normal")
	@Test
	@InSequence(20)
	public void testBuscarVacunadorSoap() throws UsuarioInexistente {
		DtUsuarioSoap dt = new DtUsuarioSoap("Vacunador", "DeTest Dos", "02-01-2000", 11111112, "v@2", null, Sexo.Otro);
		assertEquals(dt, cu.buscarVacunadorSoap(11111112));
	}
	
	@OperateOnDeployment("normal")
	@Test
	@InSequence(21)
	public void testBuscarCiudadano() throws UsuarioInexistente {
		DtCiudadano dt = new DtCiudadano(21111112, "Ciudadano", "DeTest Dos", LocalDate.of(2000, 1, 2), "c@2", null, Sexo.Otro, "Sector123456789" , false);
		assertEquals(dt, cu.buscarCiudadano(21111112));
	}
	
	@OperateOnDeployment("normal")
	@Test
	@InSequence(22)
	public void testBuscarInterno() throws UsuarioInexistente {
		DtUsuarioInterno dt = new DtUsuarioInterno("Interno", "DeTest Dos", LocalDate.of(2000, 1, 2), 31111112, "i@2", null, Sexo.Otro, Rol.Administrador);
		assertEquals(dt, cu.buscarUsuarioInterno(31111112));
	}
	
	@OperateOnDeployment("normal")
	@Test(expected=UsuarioInexistente.class)
	@InSequence(25)
	public void testModificarVacunadorInexistente() throws UsuarioInexistente {
		cu.ModificarVacunador(new DtVacunador("Vacunador", "DeTest Dos", LocalDate.of(2000, 1, 2), 31111112, "v@2", null, Sexo.Otro));
	}
	
	@OperateOnDeployment("normal")
	@Test(expected=UsuarioInexistente.class)
	@InSequence(26)
	public void testModificarCiudadanoInexistente() throws UsuarioInexistente {
		cu.ModificarCiudadano(new DtCiudadano(11111112, "Ciudadano", "DeTest Dos", LocalDate.of(2000, 1, 2), "c@2", null, Sexo.Otro, "Sector123456789" , false));
	}
	
	@OperateOnDeployment("normal")
	@Test(expected=UsuarioInexistente.class)
	@InSequence(27)
	public void testModificarInternoInexistente() throws UsuarioInexistente {
		cu.ModificarUsuarioInterno(new DtUsuarioInterno("Interno", "DeTest Dos", LocalDate.of(2000, 1, 2), 21111112, "i@2", null, Sexo.Otro, Rol.Administrador));
	}
	
	@OperateOnDeployment("normal")
	@Test
	@InSequence(28)
	public void testModificarVacunador() throws UsuarioInexistente {
		Vacunador v = new Vacunador(11111112, "Vacunador Modificado", "DeTest Modificado Dos", LocalDate.of(2005, 1, 2), "v@2", null, Sexo.Otro);
		cu.ModificarVacunador(new DtVacunador("Vacunador Modificado", "DeTest Modificado Dos", LocalDate.of(2005, 1, 2), 11111112, "v@2", null, Sexo.Otro));
	}
	
	@OperateOnDeployment("normal")
	@Test
	@InSequence(29)
	public void testModificarCiudadano() throws UsuarioInexistente {
		Ciudadano c = new Ciudadano(21111112, "Ciudadano Modificado", "DeTest Modificado Dos", LocalDate.of(2005, 1, 2), "c@2", null, Sexo.Femenino, "Modificado123456789" , false);
		cu.ModificarCiudadano(new DtCiudadano(21111112, "Ciudadano Modificado", "DeTest Modificado Dos", LocalDate.of(2005, 1, 2), "c@2", null, Sexo.Femenino, "Modificado123456789" , false));
	}
	
	@OperateOnDeployment("normal")
	@Test
	@InSequence(30)
	public void testModificarInterno() throws UsuarioInexistente {
		UsuarioInterno i = new UsuarioInterno(31111112, "Interno Modificado", "DeTest Modificado Dos", LocalDate.of(2005, 1, 2), "c@2", null, Sexo.Femenino, Rol.Autoridad);
		cu.ModificarUsuarioInterno(new DtUsuarioInterno("Interno Modificado", "DeTest Modificado Dos", LocalDate.of(2005, 1, 2), 31111112, "i@2", null, Sexo.Masculino, Rol.Autoridad));
	}
	
	@OperateOnDeployment("normal")
	@Test
	@InSequence(50)
	public void testClean()  {
		try {
			utx.begin();
			em.remove(em.find(Vacunador.class, 11111111));
			em.remove(em.find(Ciudadano.class, 21111111));
			em.remove(em.find(UsuarioInterno.class, 31111111));
			em.remove(em.find(Vacunador.class, 11111112));
			em.remove(em.find(Ciudadano.class, 21111112));
			em.remove(em.find(UsuarioInterno.class, 31111112));
			utx.commit();
		} catch (NotSupportedException | SystemException | SecurityException | IllegalStateException | RollbackException | HeuristicMixedException | HeuristicRollbackException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
