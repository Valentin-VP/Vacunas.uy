package init;

import static org.junit.Assert.assertTrue;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.OperateOnDeployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.junit.InSequence;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

import controllers.ControladorReserva;
import datatypes.DtAgenda;
import datatypes.DtAsignado;
import datatypes.DtCertificadoVac;
import datatypes.DtCiudadano;
import datatypes.DtCiudadanoRegistro;
import datatypes.DtConstancia;
import datatypes.DtDatosEtapa;
import datatypes.DtDatosVacuna;
import datatypes.DtDireccion;
import datatypes.DtEnfermedad;
import datatypes.DtEtapa;
import datatypes.DtLaboratorio;
import datatypes.DtLdap;
import datatypes.DtLoteDosis;
import datatypes.DtMensaje;
import datatypes.DtPlanVacunacion;
import datatypes.DtReserva;
import datatypes.DtReservaCompleto;
import datatypes.DtUsuario;
import datatypes.DtUsuarioInterno;
import datatypes.DtUsuarioSoap;
import datatypes.DtVacuna;
import datatypes.DtVacunador;
import datatypes.DtVacunadorRegistro;
import datatypes.DtVacunatorio;
import datatypes.EstadoReserva;
import datatypes.Rol;
import datatypes.Sexo;
import entities.Reserva;
import exceptions.ReservaInexistente;
import interfaces.IReservaDAORemote;
import persistence.AgendaID;
import persistence.AsignadoID;
import persistence.EtapaID;
import persistence.HistoricoID;
import persistence.LoteDosisID;
import persistence.PuestoID;
import persistence.ReservaID;
import persistence.StockID;

@RunWith(Arquillian.class)
public class DatatypeTest {
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
	
	@OperateOnDeployment("normal")
	@Test
	@InSequence(1)
	public void testDtAgenda() {
		DtAgenda dt = new DtAgenda(LocalDate.now(), new ArrayList<DtReserva>());
		DtAgenda dt2 = new DtAgenda(null, new ArrayList<DtReserva>());
		DtAgenda dt3 = new DtAgenda(LocalDate.now().plusDays(1), null);
		DtAgenda dt4 = new DtAgenda();
		LocalDate oldFecha = dt.getFecha();
		dt.getReservas().add(new DtReserva());
		List<DtReserva> oldReservas = dt.getReservas();
		dt.setFecha(LocalDate.now().plusDays(1));
		dt.setReservas(new ArrayList<DtReserva>());
		assertTrue(new DtAgenda().equals(new DtAgenda()));
		assertTrue(dt.equals(dt));
		assertTrue(!dt.equals(LocalDate.now()));
		assertTrue(!dt2.equals(dt));
		assertTrue(!dt3.equals(dt));
		assertTrue(!dt.equals(null));
		assertTrue(!dt.equals(new DtAgenda(oldFecha, oldReservas)));
		assertTrue(!dt.equals(new DtAgenda(LocalDate.now().plusDays(1), oldReservas)));
		dt4.setFecha(dt.getFecha());
		dt4.setReservas(dt.getReservas());
		assertTrue(dt.equals(dt4));
		assertTrue(dt.hashCode()==dt.hashCode());
		assertTrue(new DtAgenda().hashCode()==new DtAgenda().hashCode());
	}
	
	@OperateOnDeployment("normal")
	@Test
	@InSequence(3)
	public void testDtAsignado() {
		DtAsignado dt = new DtAsignado("a", "a");
		DtAsignado dt2 = new DtAsignado(null, "a");
		DtAsignado dt3 = new DtAsignado("a", null);
		DtAsignado dt4 = new DtAsignado();
		assertTrue(new DtAsignado().equals(new DtAsignado()));
		assertTrue(dt.equals(dt));
		assertTrue(!dt.equals("a"));
		assertTrue(!dt2.equals(dt));
		assertTrue(!dt3.equals(dt));
		assertTrue(!dt.equals(null));
		assertTrue(!dt.equals(new DtAsignado("b", "b")));
		assertTrue(!dt.equals(new DtAsignado("a", "b")));
		dt4.setFecha(dt.getFecha());
		dt4.setIdPuesto(dt.getIdPuesto());
		assertTrue(dt.equals(dt4));
		assertTrue(dt.hashCode()==dt.hashCode());
		assertTrue(new DtAsignado().hashCode()==new DtAsignado().hashCode());
	}
	
	@OperateOnDeployment("normal")
	@Test
	@InSequence(5)
	public void testDtCertificadoVac() {
		DtCertificadoVac dt = new DtCertificadoVac(1, new ArrayList<DtConstancia>());
		DtCertificadoVac dt2 = new DtCertificadoVac(1, null);
		DtCertificadoVac dt3 = new DtCertificadoVac();
		DtCertificadoVac dt4 = new DtCertificadoVac(1, null);
		ArrayList<DtConstancia> aux = new ArrayList<DtConstancia>();
		aux.add(new DtConstancia());
		assertTrue(new DtAsignado().equals(new DtAsignado()));
		assertTrue(dt.equals(dt));
		assertTrue(!dt.equals(new DtConstancia()));
		assertTrue(!dt2.equals(dt));
		assertTrue(!dt.equals(null));
		assertTrue(!dt.equals(new DtCertificadoVac(2, new ArrayList<DtConstancia>())));
		assertTrue(!dt.equals(new DtCertificadoVac(1, aux)));
		dt3.setIdCert(dt.getIdCert());
		dt3.setConstancias(dt.getConstancias());
		assertTrue(dt.equals(dt3));
		assertTrue(dt4.equals(dt2));
		assertTrue(dt.hashCode()==dt.hashCode());
		assertTrue(dt2.hashCode()==dt2.hashCode());
	}
	
	@OperateOnDeployment("normal")
	@Test
	@InSequence(7)
	public void testDtCiudadanoRegistro() {
		DtCiudadanoRegistro dt = new DtCiudadanoRegistro("a", "a", "a", "a", "a", "a", "a", "a", "a");
		DtCiudadanoRegistro dt2 = new DtCiudadanoRegistro();
		dt2.setApellido(dt.getApellido());
		dt2.setBarrio(dt.getBarrio());
		dt2.setDepartamento(dt.getDepartamento());
		dt2.setDireccion(dt.getDireccion());
		dt2.setEmail(dt.getEmail());
		dt2.setFechaNac(dt.getFechaNac());
		dt2.setId(dt.getId());
		dt2.setNombre(dt.getNombre());
		dt2.setSexo(dt.getSexo());
		assertTrue(dt.equals(dt));
	}
	
	@OperateOnDeployment("normal")
	@Test
	@InSequence(9)
	public void testDtUsuario() {
		DtUsuario dt = new DtUsuario("a", "a", LocalDate.now(), 0, "a", new DtDireccion(), Sexo.Otro);
		DtUsuario dt2 = new DtUsuario("a", "a", LocalDate.now(), 0, "a", new DtDireccion(), Sexo.Otro, "a");
		DtUsuario dt3 = new DtUsuario(null, null, null, 0, null, null, null, null);
		assertTrue(dt3.hashCode()==dt3.hashCode());
		assertTrue(new DtUsuario().equals(new DtUsuario()));
		assertTrue(dt.equals(dt));
		assertTrue(!dt.equals(new DtDireccion()));
		assertTrue(!dt.equals(null));
		assertTrue(!dt2.equals(dt));
		assertTrue(!dt.equals(new DtUsuario("b", "a", LocalDate.now(), 0, "a", new DtDireccion(), Sexo.Otro)));
		assertTrue(!dt.equals(new DtUsuario("a", "b", LocalDate.now(), 0, "a", new DtDireccion(), Sexo.Otro)));
		assertTrue(!dt.equals(new DtUsuario("a", "a", LocalDate.now().plusDays(1), 0, "a", new DtDireccion(), Sexo.Otro)));
		assertTrue(!dt.equals(new DtUsuario("a", "a", LocalDate.now(), 1, "a", new DtDireccion(), Sexo.Otro)));
		assertTrue(!dt.equals(new DtUsuario("a", "a", LocalDate.now(), 0, "b", new DtDireccion(), Sexo.Otro)));
		assertTrue(!dt.equals(new DtUsuario("a", "a", LocalDate.now(), 0, "a", new DtDireccion("","",""), Sexo.Otro)));
		assertTrue(!dt.equals(new DtUsuario("a", "a", LocalDate.now(), 0, "a", new DtDireccion(), Sexo.Masculino)));
		dt.setToken("a");
		assertTrue(dt.equals(dt2));
		assertTrue(!dt3.equals(dt));
		dt3.setApellido(dt.getApellido());
		assertTrue(!dt3.equals(dt));
		dt3.setDireccion(dt.getDireccion());
		assertTrue(!dt3.equals(dt));
		dt3.setEmail(dt.getEmail());
		assertTrue(!dt3.equals(dt));
		dt3.setFechaNac(dt.getFechaNac());
		assertTrue(!dt3.equals(dt));
		dt3.setNombre(dt.getNombre());
		assertTrue(!dt3.equals(dt));
		dt3.setSexo(dt.getSexo());
		assertTrue(!dt3.equals(dt));
		dt3.setIdUsuario(dt.getIdUsuario());
		dt3.setToken(dt.getToken());
		assertTrue(dt3.hashCode()==dt3.hashCode());
	}
	
	@OperateOnDeployment("normal")
	@Test
	@InSequence(11)
	public void testDtCiudadano() {
		DtCiudadano dt = new DtCiudadano(0, "a", "a", LocalDate.now(), "a", new DtDireccion(), Sexo.Otro, "a", true);
		DtCiudadano dt2 = new DtCiudadano(0, "a", "a", LocalDate.now(), "a", new DtDireccion(), Sexo.Otro, "b", "b", false);
		DtCiudadano dt3 = new DtCiudadano(0, "a", "a", LocalDate.now(), "a", new DtDireccion(), Sexo.Otro, "a", "a", true, "a", new ArrayList<DtReserva>(), new DtCertificadoVac());
		DtCiudadano dt4 = new DtCiudadano();
		dt.setTipoSector(dt2.getTipoSector());
		dt.setAutenticado(dt2.isAutenticado());
		dt.setMobileToken(dt3.getMobileToken());
		dt.setReservas(dt3.getReservas());
		dt.setCertificado(dt3.getCertificado());
		assertTrue(dt.equals(dt));
	}
	
	@OperateOnDeployment("normal")
	@Test
	@InSequence(13)
	public void testDtVacunador() {
		DtVacunador dt = new DtVacunador("a", "a", LocalDate.now(), 0, "a", new DtDireccion(), Sexo.Otro);
		DtVacunador dt2 = new DtVacunador("a", "a", LocalDate.now(), 0, "a", new DtDireccion(), Sexo.Otro, "a");
		DtVacunador dt3 = new DtVacunador();
		assertTrue(dt.equals(dt));
		assertTrue(!dt.equals("a"));
		assertTrue(!dt2.equals(dt));
		dt.setToken(dt2.getToken());
		assertTrue(dt.equals(dt2));
		assertTrue(dt.hashCode()==dt2.hashCode());
	}
	
	@OperateOnDeployment("normal")
	@Test
	@InSequence(15)
	public void testDtUsuarioInterno() {
		DtUsuarioInterno dt = new DtUsuarioInterno("a", "a", LocalDate.now(), 0, "a", new DtDireccion(), Sexo.Otro);
		DtUsuarioInterno dt2 = new DtUsuarioInterno("a", "a", LocalDate.now(), 0, "a", new DtDireccion(), Sexo.Otro, Rol.Administrador);
		DtUsuarioInterno dt3 = new DtUsuarioInterno("a", "a", LocalDate.now(), 0, "a", new DtDireccion(), Sexo.Otro, Rol.Administrador, "a");
		DtUsuarioInterno dt4 = new DtUsuarioInterno(null, null, null, 0, null, null, null, null, null);
		assertTrue(dt4.hashCode()==dt4.hashCode());
		assertTrue(new DtUsuarioInterno().equals(new DtUsuarioInterno()));
		assertTrue(dt.equals(dt));
		assertTrue(!dt.equals(new DtDireccion()));
		assertTrue(!dt2.equals(dt));
		assertTrue(!dt.equals(new DtUsuarioInterno("a", "a", LocalDate.now(), 0, "a", new DtDireccion(), Sexo.Otro, Rol.Autoridad)));
		dt.setRol(dt2.getRol());
		assertTrue(dt.equals(dt2));
		assertTrue(dt.hashCode()==dt2.hashCode());
	}
	
	@OperateOnDeployment("normal")
	@Test
	@InSequence(17)
	public void testDtUsuarioSoap() {
		DtUsuarioSoap dt = new DtUsuarioSoap("a", "a", "a", 0, "a", new DtDireccion(), Sexo.Otro);
		DtUsuarioSoap dt2 = new DtUsuarioSoap("a", "a", "a", 0, "a", new DtDireccion(), Sexo.Otro, "a");
		DtUsuarioSoap dt3 = new DtUsuarioSoap(null, null, null, 0, null, null, null, null);
		assertTrue(dt3.hashCode()==dt3.hashCode());
		assertTrue(new DtUsuario().equals(new DtUsuario()));
		assertTrue(dt.equals(dt));
		assertTrue(!dt.equals(new DtDireccion()));
		assertTrue(!dt.equals(null));
		assertTrue(!dt2.equals(dt));
		assertTrue(!dt.equals(new DtUsuarioSoap("b", "a", "a", 0, "a", new DtDireccion(), Sexo.Otro)));
		assertTrue(!dt.equals(new DtUsuarioSoap("a", "b", "a", 0, "a", new DtDireccion(), Sexo.Otro)));
		assertTrue(!dt.equals(new DtUsuarioSoap("a", "a", "b", 0, "a", new DtDireccion(), Sexo.Otro)));
		assertTrue(!dt.equals(new DtUsuarioSoap("a", "a", "a", 1, "a", new DtDireccion(), Sexo.Otro)));
		assertTrue(!dt.equals(new DtUsuarioSoap("a", "a", "a", 0, "b", new DtDireccion(), Sexo.Otro)));
		assertTrue(!dt.equals(new DtUsuarioSoap("a", "a", "a", 0, "a", new DtDireccion("","",""), Sexo.Otro)));
		assertTrue(!dt.equals(new DtUsuarioSoap("a", "a", "a", 0, "a", new DtDireccion(), Sexo.Masculino)));
		dt.setToken("a");
		assertTrue(dt.equals(dt2));
		assertTrue(!dt3.equals(dt));
		dt3.setApellido(dt.getApellido());
		assertTrue(!dt3.equals(dt));
		dt3.setDireccion(dt.getDireccion());
		assertTrue(!dt3.equals(dt));
		dt3.setEmail(dt.getEmail());
		assertTrue(!dt3.equals(dt));
		dt3.setFechaNac(dt.getFechaNac());
		assertTrue(!dt3.equals(dt));
		dt3.setNombre(dt.getNombre());
		assertTrue(!dt3.equals(dt));
		dt3.setSexo(dt.getSexo());
		assertTrue(!dt3.equals(dt));
		dt3.setIdUsuario(dt.getIdUsuario());
		dt3.setToken(dt.getToken());
		assertTrue(dt3.hashCode()==dt3.hashCode());
	}
	
	@OperateOnDeployment("normal")
	@Test
	@InSequence(19)
	public void testDtReservaCompleto() {
		DtReservaCompleto dt = new DtReservaCompleto("a", "a", "a", "a", EstadoReserva.Completada, "a", "a", "a", "a", "a", "a");
		DtReservaCompleto dt2 = new DtReservaCompleto(null, null, null, null, null, null, null, null, null, null, null);
		assertTrue(dt2.hashCode()==dt2.hashCode());
		assertTrue(new DtUsuario().equals(new DtUsuario()));
		assertTrue(dt.equals(dt));
		assertTrue(!dt.equals(new DtDireccion()));
		assertTrue(!dt.equals(null));
		assertTrue(!dt2.equals(dt));
		assertTrue(!dt.equals(new DtReservaCompleto("b", "a", "a", "a", EstadoReserva.Completada, "a", "a", "a", "a", "a", "a")));
		assertTrue(!dt.equals(new DtReservaCompleto("a", "b", "a", "a", EstadoReserva.Completada, "a", "a", "a", "a", "a", "a")));
		assertTrue(!dt.equals(new DtReservaCompleto("a", "a", "b", "a", EstadoReserva.Completada, "a", "a", "a", "a", "a", "a")));
		assertTrue(!dt.equals(new DtReservaCompleto("a", "a", "a", "b", EstadoReserva.Completada, "a", "a", "a", "a", "a", "a")));
		assertTrue(!dt.equals(new DtReservaCompleto("a", "a", "a", "a", EstadoReserva.Cancelada, "a", "a", "a", "a", "a", "a")));
		assertTrue(!dt.equals(new DtReservaCompleto("a", "a", "a", "a", EstadoReserva.Completada, "b", "a", "a", "a", "a", "a")));
		assertTrue(!dt.equals(new DtReservaCompleto("a", "a", "a", "a", EstadoReserva.Completada, "a", "b", "a", "a", "a", "a")));
		assertTrue(!dt.equals(new DtReservaCompleto("a", "a", "a", "a", EstadoReserva.Completada, "a", "a", "b", "a", "a", "a")));
		assertTrue(!dt.equals(new DtReservaCompleto("a", "a", "a", "a", EstadoReserva.Completada, "a", "a", "a", "b", "a", "a")));
		assertTrue(!dt.equals(new DtReservaCompleto("a", "a", "a", "a", EstadoReserva.Completada, "a", "a", "a", "a", "b", "a")));
		assertTrue(!dt.equals(new DtReservaCompleto("a", "a", "a", "a", EstadoReserva.Completada, "a", "a", "a", "a", "a", "b")));
		assertTrue(!dt2.equals(dt));
		dt2.setDescEtapa(dt.getDescEtapa());
		assertTrue(!dt2.equals(dt));
		dt2.setDescPlan(dt.getDescPlan());
		assertTrue(!dt2.equals(dt));
		dt2.setEnfermedad(dt.getEnfermedad());
		assertTrue(!dt2.equals(dt));
		dt2.setEstado(dt.getEstado());
		assertTrue(!dt2.equals(dt));
		dt2.setFecha(dt.getFecha());
		assertTrue(!dt2.equals(dt));
		dt2.setIdCiudadano(dt.getIdCiudadano());
		assertTrue(!dt2.equals(dt));
		dt2.setIdEtapa(dt.getIdEtapa());
		assertTrue(!dt2.equals(dt));
		dt2.setIdPlan(dt.getIdPlan());
		assertTrue(!dt2.equals(dt));
		dt2.setPuesto(dt.getPuesto());
		assertTrue(!dt2.equals(dt));
		dt2.setUsuario(dt.getUsuario());
		assertTrue(!dt2.equals(dt));
		dt2.setVacuna(dt.getVacuna());
		assertTrue(dt2.equals(dt));
		assertTrue(dt.hashCode()==dt2.hashCode());
	}
	
	@OperateOnDeployment("normal")
	@Test
	@InSequence(21)
	public void testDtReserva() {
		DtReserva dt = new DtReserva("a", EstadoReserva.Completada, "a", "a", "a");
		DtReserva dt2 = new DtReserva("a", EstadoReserva.Completada, "a", "a", "a", "a");
		DtReserva dt3 = new DtReserva("a", "a", EstadoReserva.Completada, "a", "a", "a");
		DtReserva dt4 = new DtReserva(null, null, null, null, null);
		dt4.setVacunatorio(null);
		dt4.setVacuna(null);
		assertTrue(dt4.hashCode()==dt4.hashCode());
		assertTrue(new DtUsuario().equals(new DtUsuario()));
		assertTrue(dt.equals(dt));
		assertTrue(!dt.equals(new DtDireccion()));
		assertTrue(!dt.equals(null));
		assertTrue(!dt2.equals(dt));
		assertTrue(!dt.equals(new DtReserva("b", EstadoReserva.Completada, "a", "a", "a")));
		assertTrue(!dt.equals(new DtReserva("a", EstadoReserva.Cancelada, "a", "a", "a")));
		assertTrue(!dt.equals(new DtReserva("a", EstadoReserva.Completada, "b", "a", "a")));
		assertTrue(!dt.equals(new DtReserva("a", EstadoReserva.Completada, "a", "b", "a")));
		assertTrue(!dt.equals(new DtReserva("a", EstadoReserva.Completada, "a", "a", "b")));
		assertTrue(!dt.equals(new DtReserva("a", EstadoReserva.Completada, "a", "a", "a", "b")));
		dt.setVacunatorio(dt3.getVacunatorio());
		assertTrue(!dt.equals(new DtReserva("a", "b", EstadoReserva.Completada, "a", "a", "a")));
		assertTrue(!dt4.equals(dt));
		dt.setVacuna(dt2.getVacuna());
		dt4.setEstado(dt.getEstado());
		assertTrue(!dt4.equals(dt));
		dt4.setEtapa(dt.getEtapa());
		assertTrue(!dt4.equals(dt));
		dt4.setFecha(dt.getFecha());
		assertTrue(!dt4.equals(dt));
		dt4.setPuesto(dt.getPuesto());
		assertTrue(!dt4.equals(dt));
		dt4.setUsuario(dt.getUsuario());
		assertTrue(!dt4.equals(dt));
		dt4.setVacuna(dt.getVacuna());
		assertTrue(!dt4.equals(dt));
		dt4.setVacunatorio(dt.getVacunatorio());
		assertTrue(dt4.equals(dt));
		assertTrue(dt.hashCode()==dt4.hashCode());
	}
	
	@OperateOnDeployment("normal")
	@Test
	@InSequence(23)
	public void testDtConstancia() {
		DtConstancia dt = new DtConstancia(0,0,0, LocalDate.now(), "a", new DtReserva());
		DtConstancia dt2 = new DtConstancia(1,1,1, LocalDate.now().plusDays(1), "b", new DtReserva(), "a");
		DtConstancia dt3 = new DtConstancia(0, 0, 0, null, null, null, null);
		
		assertTrue(dt3.hashCode()==dt3.hashCode());
		assertTrue(new DtConstancia().equals(new DtConstancia()));
		assertTrue(dt.equals(dt));
		assertTrue(!dt.equals(new DtDireccion()));
		assertTrue(!dt.equals(null));
		dt.setEnfermedad(dt2.getEnfermedad());
		assertTrue(!dt2.equals(dt));
		assertTrue(!dt.equals(new DtConstancia(1,0,0, LocalDate.now(), "a", new DtReserva(), "a")));
		assertTrue(!dt.equals(new DtConstancia(0,1,0, LocalDate.now(), "a", new DtReserva(), "a")));
		assertTrue(!dt.equals(new DtConstancia(0,0,1, LocalDate.now(), "a", new DtReserva(), "a")));
		assertTrue(!dt.equals(new DtConstancia(0,0,0, LocalDate.now().plusDays(1), "a", new DtReserva(), "a")));
		assertTrue(!dt.equals(new DtConstancia(0,0,0, LocalDate.now(), "b", new DtReserva(), "a")));
		assertTrue(!dt.equals(new DtConstancia(0,0,0, LocalDate.now(), "a", new DtReserva("", null, "", "", ""), "a")));
		
		assertTrue(!dt.equals(new DtConstancia(0,0,0, LocalDate.now(), "a", new DtReserva(), "b")));
		assertTrue(!dt3.equals(dt));
		dt3.setEnfermedad(dt.getEnfermedad());
		assertTrue(!dt3.equals(dt));
		dt3.setFechaUltimaDosis(dt.getFechaUltimaDosis());
		assertTrue(!dt3.equals(dt));
		dt3.setReserva(dt.getReserva());
		assertTrue(!dt3.equals(dt));
		dt3.setVacuna(dt.getVacuna());
		dt3.setDosisRecibidas(dt.getDosisRecibidas());
		dt3.setIdConstVac(dt.getIdConstVac());
		dt3.setPeriodoInmunidad(dt.getPeriodoInmunidad());
		assertTrue(dt3.equals(dt));
		assertTrue(dt.hashCode()==dt3.hashCode());
	}
	
	@OperateOnDeployment("normal")
	@Test
	@InSequence(25)
	public void testDtEtapa() {
		DtEtapa dt = new DtEtapa(0,"a","a", "a",0, "a");
		DtEtapa dt2 = new DtEtapa(0, null, null, null, 0, null);
		
		assertTrue(dt2.hashCode()==dt2.hashCode());
		assertTrue(new DtEtapa().equals(new DtEtapa()));
		assertTrue(dt.equals(dt));
		assertTrue(!dt.equals(new DtPlanVacunacion()));
		assertTrue(!dt.equals(null));
		assertTrue(!dt.equals(new DtEtapa(1,"a","a", "a",0, "a")));
		assertTrue(!dt.equals(new DtEtapa(0,"b","a", "a",0, "a")));
		assertTrue(!dt.equals(new DtEtapa(0,"a","b", "a",0, "a")));
		assertTrue(!dt.equals(new DtEtapa(0,"a","a", "b",0, "a")));
		assertTrue(!dt.equals(new DtEtapa(0,"a","a", "a",1, "a")));
		assertTrue(!dt.equals(new DtEtapa(0,"a","a", "a",0, "b")));
		
		assertTrue(!dt2.equals(dt));
		dt2.setCondicion(dt.getCondicion());
		assertTrue(!dt2.equals(dt));
		dt2.setFechaFin(dt.getFechaFin());
		assertTrue(!dt2.equals(dt));
		dt2.setFechaInicio(dt.getFechaInicio());
		assertTrue(!dt2.equals(dt));
		dt2.setVacuna(dt.getVacuna());
		dt2.setDtPvac(dt.getPlanVac());
		dt2.setId(dt.getId());
		assertTrue(dt2.equals(dt));
		assertTrue(dt.hashCode()==dt2.hashCode());
	}
	
	@OperateOnDeployment("normal")
	@Test
	@InSequence(25)
	public void testDtPlanVacunacion() {
		DtPlanVacunacion dt = new DtPlanVacunacion(0,"a","a", new ArrayList<DtEtapa>());
		DtPlanVacunacion dt2 = new DtPlanVacunacion(0,"a","a", new ArrayList<DtEtapa>(), "a");
		DtPlanVacunacion dt3 = new DtPlanVacunacion(0, null, null, null, null);
		ArrayList<DtEtapa> aux = new ArrayList<DtEtapa>();
		aux.add(new DtEtapa());
		assertTrue(dt3.hashCode()==dt3.hashCode());
		assertTrue(new DtPlanVacunacion().equals(new DtPlanVacunacion()));
		assertTrue(dt.equals(dt));
		assertTrue(!dt.equals(new DtEtapa()));
		assertTrue(!dt.equals(null));
		assertTrue(!dt.equals(new DtPlanVacunacion(1,"a","a", new ArrayList<DtEtapa>(), "a")));
		assertTrue(!dt.equals(new DtPlanVacunacion(0,"b","a", new ArrayList<DtEtapa>(), "a")));
		assertTrue(!dt.equals(new DtPlanVacunacion(0,"a","b", new ArrayList<DtEtapa>(), "a")));
		assertTrue(!dt.equals(new DtPlanVacunacion(0,"a","a", aux, "a")));
		dt.setEnfermedad(dt2.getEnfermedad());
		assertTrue(!dt.equals(new DtPlanVacunacion(0,"a","a", new ArrayList<DtEtapa>(), "b")));
		assertTrue(!dt3.equals(dt));
		dt3.setDescripcion(dt.getDescripcion());
		assertTrue(!dt3.equals(dt));
		dt3.setEnfermedad(dt.getEnfermedad());
		assertTrue(!dt3.equals(dt));
		dt3.setEtapa(dt.getEtapa());
		assertTrue(!dt3.equals(dt));
		dt3.setNombre(dt.getNombre());
		dt3.setId(dt.getId());
		assertTrue(dt3.equals(dt));
		assertTrue(dt.hashCode()==dt3.hashCode());
	}
	
	@OperateOnDeployment("normal")
	@Test
	@InSequence(27)
	public void testDtLaboratorio() {
		DtLaboratorio dt = new DtLaboratorio("a");
		DtLaboratorio dt2 = new DtLaboratorio(null);
		assertTrue(dt2.hashCode()==dt2.hashCode());
		assertTrue(new DtLaboratorio().equals(new DtLaboratorio()));
		assertTrue(dt.equals(dt));
		assertTrue(!dt.equals(new DtVacuna()));
		assertTrue(!dt.equals(null));
		assertTrue(!dt.equals(new DtLaboratorio("b")));
		assertTrue(!dt2.equals(dt));
		dt2.setNombre(dt.getNombre());
		assertTrue(dt2.equals(dt));
		assertTrue(dt.hashCode()==dt2.hashCode());
	}
	
	@OperateOnDeployment("normal")
	@Test
	@InSequence(29)
	public void testDtEnfermedad() {
		DtEnfermedad dt = new DtEnfermedad("a");
		DtEnfermedad dt2 = new DtEnfermedad(null);
		assertTrue(dt2.hashCode()==dt2.hashCode());
		assertTrue(new DtEnfermedad().equals(new DtEnfermedad()));
		assertTrue(dt.equals(dt));
		assertTrue(!dt.equals(new DtVacuna()));
		assertTrue(!dt.equals(null));
		assertTrue(!dt.equals(new DtEnfermedad("b")));
		assertTrue(!dt2.equals(dt));
		dt2.setNombre(dt.getNombre());
		assertTrue(dt2.equals(dt));
		assertTrue(dt.hashCode()==dt2.hashCode());
	}
	
	@OperateOnDeployment("normal")
	@Test
	@InSequence(31)
	public void testDtVacuna() {
		DtVacuna dt = new DtVacuna("a",0 ,0, 0, new DtLaboratorio(), new DtEnfermedad());
		DtVacuna dt2 = new DtVacuna(null, 0, 0, 0, null, null);
		assertTrue(dt2.hashCode()==dt2.hashCode());
		assertTrue(new DtVacuna().equals(new DtVacuna()));
		assertTrue(dt.equals(dt));
		assertTrue(!dt.equals(new DtEtapa()));
		assertTrue(!dt.equals(null));
		assertTrue(!dt.equals(new DtVacuna("b",0 ,0, 0, new DtLaboratorio(), new DtEnfermedad())));
		assertTrue(!dt.equals(new DtVacuna("a",1 ,0, 0, new DtLaboratorio(), new DtEnfermedad())));
		assertTrue(!dt.equals(new DtVacuna("a",0 ,1, 0, new DtLaboratorio(), new DtEnfermedad())));
		assertTrue(!dt.equals(new DtVacuna("a",0 ,0, 1, new DtLaboratorio(), new DtEnfermedad())));
		assertTrue(!dt.equals(new DtVacuna("a",0 ,0, 0, new DtLaboratorio("a"), new DtEnfermedad())));
		assertTrue(!dt.equals(new DtVacuna("a",0 ,0, 0, new DtLaboratorio(), new DtEnfermedad(""))));
		assertTrue(!dt2.equals(dt));
		dt2.setDtEnf(dt.getDtEnf());
		assertTrue(!dt2.equals(dt));
		dt2.setDtLab(dt.getDtLab());
		assertTrue(!dt2.equals(dt));
		dt2.setNombre(dt.getNombre());
		dt2.setCantDosis(dt.getCantDosis());
		dt2.setExpira(dt.getExpira());
		dt2.setTiempoEntreDosis(dt.getTiempoEntreDosis());
		assertTrue(dt2.equals(dt));
		assertTrue(dt.hashCode()==dt2.hashCode());
	}
	
	@OperateOnDeployment("normal")
	@Test
	@InSequence(33)
	public void testDtVacunatorio() {
		DtVacunatorio dt = new DtVacunatorio("a","a" ,new DtDireccion(), 0, 0.0F, 0.0F, "a", "a");
		DtVacunatorio dt2 = new DtVacunatorio();
		DtVacunatorio dt3 = new DtVacunatorio("a","a" ,new DtDireccion(), 0, 0.0F, 0.0F, "a");
		DtVacunatorio dt4 = new DtVacunatorio("a","a" ,new DtDireccion(), 0, 0.0F, 0.0F);
		dt2.setDtDir(dt.getDtDir());
		dt2.setId(dt.getId());
		dt2.setLatitud(dt.getLatitud());
		dt2.setLongitud(dt.getLongitud());
		dt2.setNombre(dt.getNombre());
		dt2.setTelefono(dt.getTelefono());
		dt2.setToken(dt.getToken());
		dt2.setUrl(dt.getUrl());
		assertTrue(dt.hashCode()==dt.hashCode());
		assertTrue(!dt3.equals(dt4));
	}
	
	@OperateOnDeployment("normal")
	@Test
	@InSequence(35)
	public void testDtLdap() {
		DtLdap dt = new DtLdap("a","a" ,"a", "a", "a", "a", "a", "a","a");
		DtLdap dt2 = new DtLdap();
		DtLdap dt3 = new DtLdap("a",0 ,"a", "a", "a");
		dt.setCi(0);
		dt2.setApellido(dt.getApellido());
		dt2.setBarrio(dt.getBarrio());
		dt2.setCi(dt.getCi());
		dt2.setDepartamento(dt.getDepartamento());
		dt2.setDireccion(dt.getDireccion());
		dt2.setEmail(dt.getEmail());
		dt2.setFecha(dt.getFecha());
		dt2.setNombre(dt.getNombre());
		dt2.setPassword(dt.getPassword());
		dt2.setSexo(dt.getSexo());
		dt2.setTipoUser(dt.getTipoUser());
		assertTrue(dt.hashCode()==dt.hashCode());
		assertTrue(!dt.equals(dt3));
	}
	
	@OperateOnDeployment("normal")
	@Test
	@InSequence(37)
	public void testDtLoteDosis() {
		DtLoteDosis dt = new DtLoteDosis(0,"a" ,"a", 0, 0, 0, "a", 0.0F,0);
		DtLoteDosis dt2 = new DtLoteDosis();
		DtLoteDosis dt3 = new DtLoteDosis(0,"a" ,"a", 0, 0, 0, "a", 0.0F);
		dt2.setCantidadDescartada(dt.getCantidadDescartada());
		dt2.setCantidadEntregada(dt.getCantidadEntregada());
		dt2.setCantidadTotal(dt.getCantidadTotal());
		dt2.setEstadoLote(dt.getEstadoLote());
		dt2.setIdLote(dt.getIdLote());
		dt2.setIdVacuna(dt.getIdVacuna());
		dt2.setIdVacunatorio(dt.getIdVacunatorio());
		dt2.setTemperatura(dt.getTemperatura());
		dt2.setTransportista(dt.getTransportista());
		assertTrue(dt.hashCode()==dt.hashCode());
		assertTrue(!dt.equals(dt3));
	}
	
	@OperateOnDeployment("normal")
	@Test
	@InSequence(39)
	public void testDtMensaje() {
		DtMensaje dt = new DtMensaje(0, "a");
		DtMensaje dt2 = new DtMensaje(0, null);
		DtMensaje dt3 = new DtMensaje("a");
		dt3.setIdMensaje(dt.getIdMensaje());
		assertTrue(dt2.hashCode()==dt2.hashCode());
		assertTrue(new DtMensaje().equals(new DtMensaje()));
		assertTrue(dt.equals(dt));
		assertTrue(!dt.equals(new DtVacuna()));
		assertTrue(!dt.equals(null));
		assertTrue(!dt.equals(new DtMensaje(1, "a")));
		assertTrue(!dt.equals(new DtMensaje(0, "b")));
		assertTrue(!dt2.equals(dt));
		dt2.setContenido(dt.getContenido());
		assertTrue(dt2.equals(dt));
		assertTrue(dt.hashCode()==dt2.hashCode());
	}
	
	@OperateOnDeployment("normal")
	@Test
	@InSequence(39)
	public void testDtVacunadorRegistro() {
		DtVacunadorRegistro dt = new DtVacunadorRegistro("a", "a","a","a","a","a","a","a","a");
		DtVacunadorRegistro dt2 = new DtVacunadorRegistro();
		dt2.setApellido(dt.getApellido());
		dt2.setBarrio(dt.getBarrio());
		dt2.setDepartamento(dt.getDepartamento());
		dt2.setDireccion(dt.getDireccion());
		dt2.setEmail(dt.getEmail());
		dt2.setFechaNac(dt.getFechaNac());
		dt2.setId(dt.getId());
		dt2.setNombre(dt.getNombre());
		dt2.setSexo(dt.getSexo());
		assertTrue(dt.hashCode()==dt.hashCode());
	}
	
	@OperateOnDeployment("normal")
	@Test
	@InSequence(41)
	public void testDtDatosEtapa() {
		DtDatosEtapa dt = new DtDatosEtapa("a","a","a","a","a","a");
		DtDatosEtapa dt2 = new DtDatosEtapa();
		dt2.setCond(dt.getCond());
		dt2.setfFin(dt.getfFin());
		dt2.setfIni(dt.getfIni());
		dt2.setIdEtapa(dt.getIdEtapa());
		dt2.setIdPlan(dt.getIdPlan());
		dt2.setIdVacuna(dt.getIdVacuna());
		assertTrue(dt.hashCode()==dt.hashCode());
	}
	
	@OperateOnDeployment("normal")
	@Test
	@InSequence(43)
	public void testDtDatosVacuna() {
		DtDatosVacuna dt = new DtDatosVacuna("a","a","a","a","a","a");
		DtDatosVacuna dt2 = new DtDatosVacuna();
		dt2.setCantDosis(dt.getCantDosis());
		dt2.setEnfermedad(dt.getEnfermedad());
		dt2.setExpira(dt.getExpira());
		dt2.setLaboratorio(dt.getLaboratorio());
		dt2.setNombre(dt.getNombre());
		dt2.setTiempoEntreDosis(dt.getTiempoEntreDosis());
		assertTrue(dt.hashCode()==dt.hashCode());
	}
	
	@OperateOnDeployment("normal")
	@Test
	@InSequence(101)
	public void testLoteDosisID() {
		LoteDosisID dt = new LoteDosisID(0, "a", "a");
		LoteDosisID dt2 = new LoteDosisID(null, null, null);
		assertTrue(dt2.hashCode()==dt2.hashCode());
		assertTrue(new LoteDosisID().equals(new LoteDosisID()));
		assertTrue(dt.equals(dt));
		assertTrue(!dt.equals(new ReservaID()));
		assertTrue(!dt.equals(null));
		assertTrue(!dt.equals(new LoteDosisID(1, "a", "a")));
		assertTrue(!dt.equals(new LoteDosisID(0, "b", "a")));
		assertTrue(!dt.equals(new LoteDosisID(0, "a", "b")));
		assertTrue(!dt2.equals(dt));
		dt2.setIdLote(dt.getIdLote());
		assertTrue(!dt2.equals(dt));
		dt2.setVacuna(dt.getVacuna());
		assertTrue(!dt2.equals(dt));
		dt2.setVacunatorio(dt.getVacunatorio());
		assertTrue(dt2.equals(dt));
		assertTrue(dt.hashCode()==dt2.hashCode());
	}
	
	@OperateOnDeployment("normal")
	@Test
	@InSequence(102)
	public void testEtapaID() {
		EtapaID dt = new EtapaID(1, 1);
		EtapaID dt2 = new EtapaID();
		dt2.setId(dt.getId());
		dt2.setPlanVacunacion(dt.getPlanVacunacion());
		assertTrue(new EtapaID().equals(new EtapaID()));
		assertTrue(dt.equals(dt));
		assertTrue(!dt.equals(new ReservaID()));
		assertTrue(!dt.equals(null));
		assertTrue(!dt.equals(new EtapaID(0, 1)));
		assertTrue(!dt.equals(new EtapaID(1, 0)));
		assertTrue(dt.equals(new EtapaID(1, 1)));
		assertTrue(dt.hashCode()==dt2.hashCode());
	}
	
	@OperateOnDeployment("normal")
	@Test
	@InSequence(103)
	public void testReservaID() {
		ReservaID dt = new ReservaID(LocalDateTime.of(LocalDate.now(), LocalTime.of(20, 0, 0)), new EtapaID(), 0);
		ReservaID dt2 = new ReservaID(null, null, 1);
		assertTrue(dt2.hashCode()==dt2.hashCode());
		assertTrue(new ReservaID().equals(new ReservaID()));
		assertTrue(dt.equals(dt));
		assertTrue(!dt.equals(new EtapaID()));
		assertTrue(!dt.equals(null));
		assertTrue(!dt.equals(new ReservaID(LocalDateTime.of(LocalDate.now(), LocalTime.of(10, 0, 0)), new EtapaID(), 0)));
		assertTrue(!dt.equals(new ReservaID(LocalDateTime.of(LocalDate.now(), LocalTime.of(20, 0, 0)), new EtapaID(1,1), 0)));
		assertTrue(!dt.equals(new ReservaID(LocalDateTime.of(LocalDate.now(), LocalTime.of(20, 0, 0)), new EtapaID(), 1)));
		assertTrue(!dt2.equals(dt));
		dt2.setEtapa(dt.getEtapa());
		assertTrue(!dt2.equals(dt));
		dt2.setFechaRegistro(dt.getFechaRegistro());
		dt2.setCiudadano(dt.getCiudadano());
		assertTrue(dt2.equals(dt));
		assertTrue(dt.hashCode()==dt2.hashCode());
	}
	
	@OperateOnDeployment("normal")
	@Test
	@InSequence(104)
	public void testPuestoID() {
		PuestoID dt = new PuestoID("a", "a");
		PuestoID dt2 = new PuestoID(null, null);
		assertTrue(dt2.hashCode()==dt2.hashCode());
		assertTrue(new PuestoID().equals(new PuestoID()));
		assertTrue(dt.equals(dt));
		assertTrue(!dt.equals(new EtapaID()));
		assertTrue(!dt.equals(null));
		assertTrue(!dt.equals(new PuestoID("b", "a")));
		assertTrue(!dt.equals(new PuestoID("a", "b")));
		assertTrue(!dt2.equals(dt));
		dt2.setId(dt.getId());
		assertTrue(!dt2.equals(dt));
		dt2.setVacunatorio(dt.getVacunatorio());
		assertTrue(dt2.equals(dt));
		assertTrue(dt.hashCode()==dt2.hashCode());
	}
	
	@OperateOnDeployment("normal")
	@Test
	@InSequence(105)
	public void testAsignadoID() {
		AsignadoID dt = new AsignadoID();
		dt.setFecha(LocalDate.now());
		dt.setPuesto(new PuestoID());
		dt.setVacunador(0);
		AsignadoID dt2 = new AsignadoID();
		dt2.setVacunador(1);
		AsignadoID dt3 = new AsignadoID();
		dt3.setFecha(LocalDate.now().plusDays(1));
		dt3.setPuesto(new PuestoID());
		dt3.setVacunador(0);
		AsignadoID dt4 = new AsignadoID();
		dt.setFecha(LocalDate.now());
		dt.setPuesto(new PuestoID("",""));
		dt.setVacunador(0);
		AsignadoID dt5 = new AsignadoID();
		dt.setFecha(LocalDate.now());
		dt.setPuesto(new PuestoID());
		dt.setVacunador(1);
		assertTrue(dt2.hashCode()==dt2.hashCode());
		assertTrue(new AsignadoID().equals(new AsignadoID()));
		assertTrue(dt.equals(dt));
		assertTrue(!dt.equals(new EtapaID()));
		assertTrue(!dt.equals(null));
		assertTrue(!dt.equals(dt3));
		assertTrue(!dt.equals(dt4));
		assertTrue(!dt.equals(dt5));
		assertTrue(!dt2.equals(dt));
		dt2.setFecha(dt.getFecha());
		assertTrue(!dt2.equals(dt));
		dt2.setPuesto(dt.getPuesto());
		dt2.setVacunador(dt.getVacunador());
		assertTrue(dt2.equals(dt));
		assertTrue(dt.hashCode()==dt2.hashCode());
	}
	
	@OperateOnDeployment("normal")
	@Test
	@InSequence(106)
	public void testStockID() {
		StockID dt = new StockID("a", "a");
		StockID dt2 = new StockID(null, null);
		assertTrue(dt2.hashCode()==dt2.hashCode());
		assertTrue(new PuestoID().equals(new PuestoID()));
		assertTrue(dt.equals(dt));
		assertTrue(!dt.equals(new EtapaID()));
		assertTrue(!dt.equals(null));
		assertTrue(!dt.equals(new StockID("b", "a")));
		assertTrue(!dt.equals(new StockID("a", "b")));
		assertTrue(!dt2.equals(dt));
		dt2.setVacuna(dt.getVacuna());
		assertTrue(!dt2.equals(dt));
		dt2.setVacunatorio(dt.getVacunatorio());
		assertTrue(dt2.equals(dt));
		assertTrue(dt.hashCode()==dt2.hashCode());
	}
	
	@OperateOnDeployment("normal")
	@Test
	@InSequence(107)
	public void testHistoricoID() {
		HistoricoID dt = new HistoricoID(LocalDate.now(), new StockID());
		HistoricoID dt2 = new HistoricoID(null, null);
		assertTrue(dt2.hashCode()==dt2.hashCode());
		assertTrue(new HistoricoID().equals(new HistoricoID()));
		assertTrue(dt.equals(dt));
		assertTrue(!dt.equals(new EtapaID()));
		assertTrue(!dt.equals(null));
		assertTrue(!dt.equals(new HistoricoID(LocalDate.now().minusDays(1), new StockID())));
		assertTrue(!dt.equals(new HistoricoID(LocalDate.now(), new StockID("", ""))));
		assertTrue(!dt2.equals(dt));
		dt2.setFecha(dt.getFecha());
		assertTrue(!dt2.equals(dt));
		dt2.setStock(dt.getStock());
		assertTrue(dt2.equals(dt));
		assertTrue(dt.hashCode()==dt2.hashCode());
	}
	
	@OperateOnDeployment("normal")
	@Test
	@InSequence(108)
	public void testAgendaID() {
		AgendaID dt = new AgendaID(LocalDate.now(), "a");
		AgendaID dt2 = new AgendaID(null, null);
		assertTrue(dt2.hashCode()==dt2.hashCode());
		assertTrue(new AgendaID().equals(new AgendaID()));
		assertTrue(dt.equals(dt));
		assertTrue(!dt.equals(new EtapaID()));
		assertTrue(!dt.equals(null));
		assertTrue(!dt.equals(new AgendaID(LocalDate.now().minusDays(1), "a")));
		assertTrue(!dt.equals(new AgendaID(LocalDate.now(), "b")));
		assertTrue(!dt2.equals(dt));
		dt2.setFecha(dt.getFecha());
		assertTrue(!dt2.equals(dt));
		dt2.setVacunatorio(dt.getVacunatorio());
		assertTrue(dt2.equals(dt));
		assertTrue(dt.hashCode()==dt2.hashCode());
	}
}
