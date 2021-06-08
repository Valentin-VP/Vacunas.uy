package init;

import org.eu.ingwar.tools.arquillian.extension.suite.annotations.ArquillianSuiteDeployment;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

import controllers.ControladorReserva;
import datatypes.DtReserva;
import entities.Reserva;
import exceptions.ReservaInexistente;
import interfaces.IReservaDAORemote;
import persistence.ReservaID;


@RunWith(Arquillian.class)
@ArquillianSuiteDeployment
public class InitSuite {
	@Deployment(name = "normal", order = 1)
    public static JavaArchive createNormalDeployment() {
        return ShrinkWrap.create(JavaArchive.class)
            //.addClasses(EnfermedadTest.class, ReservaTest.class)
            .addPackages(false, Reserva.class.getPackage(), IReservaDAORemote.class.getPackage(), ControladorReserva.class.getPackage(), DtReserva.class.getPackage(), Reserva.class.getPackage(), ReservaID.class.getPackage(), ReservaInexistente.class.getPackage())
            .addPackage(InitSuite.class.getPackage())
            .addAsResource("META-INF/test-persistence.xml", "META-INF/persistence.xml")
            ;
    }
	
	@Test
	public void init(){
		
	}
}
