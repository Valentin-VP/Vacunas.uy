
package soap.client;

import java.net.MalformedURLException;
import java.net.URL;
import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import javax.xml.ws.WebEndpoint;
import javax.xml.ws.WebServiceClient;
import javax.xml.ws.WebServiceException;
import javax.xml.ws.WebServiceFeature;


/**
 * This class was generated by the JAX-WS RI.
 * JAX-WS RI 2.3.2
 * Generated source version: 2.2
 * 
 */
@WebServiceClient(name = "NodoCentralService", targetNamespace = "http://localhost:8280/transportista-web/webservice/nodoCentral", wsdlLocation = "http://localhost:8280/transportista-web/NodoCentral?wsdl")
public class NodoCentralService
    extends Service
{

    private final static URL NODOCENTRALSERVICE_WSDL_LOCATION;
    private final static WebServiceException NODOCENTRALSERVICE_EXCEPTION;
    private final static QName NODOCENTRALSERVICE_QNAME = new QName("http://localhost:8280/transportista-web/webservice/nodoCentral", "NodoCentralService");

    static {
        URL url = null;
        WebServiceException e = null;
        try {
            url = new URL("http://localhost:8280/transportista-web/NodoCentral?wsdl");
        } catch (MalformedURLException ex) {
            e = new WebServiceException(ex);
        }
        NODOCENTRALSERVICE_WSDL_LOCATION = url;
        NODOCENTRALSERVICE_EXCEPTION = e;
    }

    public NodoCentralService() {
        super(__getWsdlLocation(), NODOCENTRALSERVICE_QNAME);
    }

    public NodoCentralService(WebServiceFeature... features) {
        super(__getWsdlLocation(), NODOCENTRALSERVICE_QNAME, features);
    }

    public NodoCentralService(URL wsdlLocation) {
        super(wsdlLocation, NODOCENTRALSERVICE_QNAME);
    }

    public NodoCentralService(URL wsdlLocation, WebServiceFeature... features) {
        super(wsdlLocation, NODOCENTRALSERVICE_QNAME, features);
    }

    public NodoCentralService(URL wsdlLocation, QName serviceName) {
        super(wsdlLocation, serviceName);
    }

    public NodoCentralService(URL wsdlLocation, QName serviceName, WebServiceFeature... features) {
        super(wsdlLocation, serviceName, features);
    }

    /**
     * 
     * @return
     *     returns NodoCentral
     */
    @WebEndpoint(name = "nodoCentralPort")
    public NodoCentral getNodoCentralPort() {
        return super.getPort(new QName("http://localhost:8280/transportista-web/webservice/nodoCentral", "nodoCentralPort"), NodoCentral.class);
    }

    /**
     * 
     * @param features
     *     A list of {@link javax.xml.ws.WebServiceFeature} to configure on the proxy.  Supported features not in the <code>features</code> parameter will have their default values.
     * @return
     *     returns NodoCentral
     */
    @WebEndpoint(name = "nodoCentralPort")
    public NodoCentral getNodoCentralPort(WebServiceFeature... features) {
        return super.getPort(new QName("http://localhost:8280/transportista-web/webservice/nodoCentral", "nodoCentralPort"), NodoCentral.class, features);
    }

    private static URL __getWsdlLocation() {
        if (NODOCENTRALSERVICE_EXCEPTION!= null) {
            throw NODOCENTRALSERVICE_EXCEPTION;
        }
        return NODOCENTRALSERVICE_WSDL_LOCATION;
    }

}