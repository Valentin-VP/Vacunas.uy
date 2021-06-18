
package soap.client;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the soap.client package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _InformarEstadoLoteDosis_QNAME = new QName("http://localhost:8280/transportista-web/webservice/nodoCentral", "informarEstadoLoteDosis");
    private final static QName _InformarEstadoLoteDosisResponse_QNAME = new QName("http://localhost:8280/transportista-web/webservice/nodoCentral", "informarEstadoLoteDosisResponse");
    private final static QName _InformarTodosEstadoLoteDosis_QNAME = new QName("http://localhost:8280/transportista-web/webservice/nodoCentral", "informarTodosEstadoLoteDosis");
    private final static QName _InformarTodosEstadoLoteDosisResponse_QNAME = new QName("http://localhost:8280/transportista-web/webservice/nodoCentral", "informarTodosEstadoLoteDosisResponse");
    private final static QName _RecibirNuevoLote_QNAME = new QName("http://localhost:8280/transportista-web/webservice/nodoCentral", "recibirNuevoLote");
    private final static QName _RecibirNuevoLoteResponse_QNAME = new QName("http://localhost:8280/transportista-web/webservice/nodoCentral", "recibirNuevoLoteResponse");
    private final static QName _AccionInvalida_QNAME = new QName("http://localhost:8280/transportista-web/webservice/nodoCentral", "AccionInvalida");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: soap.client
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link InformarEstadoLoteDosis }
     * 
     */
    public InformarEstadoLoteDosis createInformarEstadoLoteDosis() {
        return new InformarEstadoLoteDosis();
    }

    /**
     * Create an instance of {@link InformarEstadoLoteDosisResponse }
     * 
     */
    public InformarEstadoLoteDosisResponse createInformarEstadoLoteDosisResponse() {
        return new InformarEstadoLoteDosisResponse();
    }

    /**
     * Create an instance of {@link InformarTodosEstadoLoteDosis }
     * 
     */
    public InformarTodosEstadoLoteDosis createInformarTodosEstadoLoteDosis() {
        return new InformarTodosEstadoLoteDosis();
    }

    /**
     * Create an instance of {@link InformarTodosEstadoLoteDosisResponse }
     * 
     */
    public InformarTodosEstadoLoteDosisResponse createInformarTodosEstadoLoteDosisResponse() {
        return new InformarTodosEstadoLoteDosisResponse();
    }

    /**
     * Create an instance of {@link RecibirNuevoLote }
     * 
     */
    public RecibirNuevoLote createRecibirNuevoLote() {
        return new RecibirNuevoLote();
    }

    /**
     * Create an instance of {@link RecibirNuevoLoteResponse }
     * 
     */
    public RecibirNuevoLoteResponse createRecibirNuevoLoteResponse() {
        return new RecibirNuevoLoteResponse();
    }

    /**
     * Create an instance of {@link AccionInvalida }
     * 
     */
    public AccionInvalida createAccionInvalida() {
        return new AccionInvalida();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link InformarEstadoLoteDosis }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link InformarEstadoLoteDosis }{@code >}
     */
    @XmlElementDecl(namespace = "http://localhost:8280/transportista-web/webservice/nodoCentral", name = "informarEstadoLoteDosis")
    public JAXBElement<InformarEstadoLoteDosis> createInformarEstadoLoteDosis(InformarEstadoLoteDosis value) {
        return new JAXBElement<InformarEstadoLoteDosis>(_InformarEstadoLoteDosis_QNAME, InformarEstadoLoteDosis.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link InformarEstadoLoteDosisResponse }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link InformarEstadoLoteDosisResponse }{@code >}
     */
    @XmlElementDecl(namespace = "http://localhost:8280/transportista-web/webservice/nodoCentral", name = "informarEstadoLoteDosisResponse")
    public JAXBElement<InformarEstadoLoteDosisResponse> createInformarEstadoLoteDosisResponse(InformarEstadoLoteDosisResponse value) {
        return new JAXBElement<InformarEstadoLoteDosisResponse>(_InformarEstadoLoteDosisResponse_QNAME, InformarEstadoLoteDosisResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link InformarTodosEstadoLoteDosis }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link InformarTodosEstadoLoteDosis }{@code >}
     */
    @XmlElementDecl(namespace = "http://localhost:8280/transportista-web/webservice/nodoCentral", name = "informarTodosEstadoLoteDosis")
    public JAXBElement<InformarTodosEstadoLoteDosis> createInformarTodosEstadoLoteDosis(InformarTodosEstadoLoteDosis value) {
        return new JAXBElement<InformarTodosEstadoLoteDosis>(_InformarTodosEstadoLoteDosis_QNAME, InformarTodosEstadoLoteDosis.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link InformarTodosEstadoLoteDosisResponse }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link InformarTodosEstadoLoteDosisResponse }{@code >}
     */
    @XmlElementDecl(namespace = "http://localhost:8280/transportista-web/webservice/nodoCentral", name = "informarTodosEstadoLoteDosisResponse")
    public JAXBElement<InformarTodosEstadoLoteDosisResponse> createInformarTodosEstadoLoteDosisResponse(InformarTodosEstadoLoteDosisResponse value) {
        return new JAXBElement<InformarTodosEstadoLoteDosisResponse>(_InformarTodosEstadoLoteDosisResponse_QNAME, InformarTodosEstadoLoteDosisResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link RecibirNuevoLote }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link RecibirNuevoLote }{@code >}
     */
    @XmlElementDecl(namespace = "http://localhost:8280/transportista-web/webservice/nodoCentral", name = "recibirNuevoLote")
    public JAXBElement<RecibirNuevoLote> createRecibirNuevoLote(RecibirNuevoLote value) {
        return new JAXBElement<RecibirNuevoLote>(_RecibirNuevoLote_QNAME, RecibirNuevoLote.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link RecibirNuevoLoteResponse }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link RecibirNuevoLoteResponse }{@code >}
     */
    @XmlElementDecl(namespace = "http://localhost:8280/transportista-web/webservice/nodoCentral", name = "recibirNuevoLoteResponse")
    public JAXBElement<RecibirNuevoLoteResponse> createRecibirNuevoLoteResponse(RecibirNuevoLoteResponse value) {
        return new JAXBElement<RecibirNuevoLoteResponse>(_RecibirNuevoLoteResponse_QNAME, RecibirNuevoLoteResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link AccionInvalida }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link AccionInvalida }{@code >}
     */
    @XmlElementDecl(namespace = "http://localhost:8280/transportista-web/webservice/nodoCentral", name = "AccionInvalida")
    public JAXBElement<AccionInvalida> createAccionInvalida(AccionInvalida value) {
        return new JAXBElement<AccionInvalida>(_AccionInvalida_QNAME, AccionInvalida.class, null, value);
    }

}
