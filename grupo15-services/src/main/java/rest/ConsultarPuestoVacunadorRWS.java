package rest;

import java.util.Iterator;
import java.util.logging.Logger;

import javax.annotation.security.DeclareRoles;
import javax.annotation.security.PermitAll;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.ws.rs.Consumes;
import javax.ws.rs.CookieParam;
import javax.ws.rs.GET;
import javax.ws.rs.NotAuthorizedException;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Cookie;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.Response.Status;

import org.jose4j.jwt.consumer.InvalidJwtException;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import datatypes.DtAsignado;
import datatypes.ErrorInfo;
import exceptions.VacunatoriosNoCargadosException;
import interfaces.IControladorVacunadorLocal;
import interfaces.IControladorVacunatorioLocal;
import jakarta.xml.soap.MessageFactory;
import jakarta.xml.soap.MimeHeaders;
import jakarta.xml.soap.Node;
import jakarta.xml.soap.SOAPBody;
import jakarta.xml.soap.SOAPConnection;
import jakarta.xml.soap.SOAPConnectionFactory;
import jakarta.xml.soap.SOAPElement;
import jakarta.xml.soap.SOAPEnvelope;
import jakarta.xml.soap.SOAPException;
import jakarta.xml.soap.SOAPMessage;
import jakarta.xml.soap.SOAPPart;
import rest.filter.TokenSecurity;

@DeclareRoles({"vacunador", "ciudadano", "administrador", "autoridad"})
@RequestScoped
@Path("/puestovac")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class ConsultarPuestoVacunadorRWS{

	/**
	 * 
	 */
	@EJB
	IControladorVacunadorLocal vs;
	
	@EJB
	IControladorVacunatorioLocal vacs;
	
	private final Logger LOGGER = Logger.getLogger(getClass().getName());

	public ConsultarPuestoVacunadorRWS() {
		// TODO Auto-generated constructor stub
	}
	
	
	@GET
	@Path("/vac")
	@PermitAll
	public Response listarVacunatorios(@CookieParam("x-access-token") Cookie cookie){
		try {
			String token = cookie.getValue();
			String ci = null;
			try {
				ci = TokenSecurity.getIdClaim(TokenSecurity.validateJwtToken(token));
			} catch (InvalidJwtException e) {
				e.printStackTrace();
			}
	        if( ci == null)
	            throw new NotAuthorizedException("No se encuentra CI en token de Cookie - Unauthorized!");
			LOGGER.info("Cedula obtenida en REST: " + ci);
			return Response.ok(vacs.listarVacunatorio()).status(200).build();
		} catch (VacunatoriosNoCargadosException e) {
			return Response.serverError().entity(new ErrorInfo(200, e.getMessage())).status(200).build();
		}

	}
	
	/*
	@GET
	@Path("/asignado") //agregar fecha
	public DtAsignado consultarPuestoVacunador(@QueryParam("user") int idVacunador, @QueryParam("vact") String idVacunatorio, @QueryParam("date") Date fecha){
		try {
			LocalDate f = LocalDate.from(fecha.toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
			Date nuevaFecha = Date.from(Instant.from(f));
			return vs.consultarPuestoAsignadoVacunador(idVacunador, idVacunatorio, nuevaFecha);
		} catch (VacunatorioNoCargadoException | UsuarioInexistente | VacunadorSinAsignar e) {
			return null;
		}
	}
*/
	//2021-05-12 
	@GET
	@Path("/asignado") //agregar fecha
	@PermitAll
	public Response consultarPuestoVacunador(@CookieParam("x-access-token") Cookie cookie, @QueryParam("vact") String idVacunatorio, @QueryParam("date") String fecha){
		if (idVacunatorio==null || fecha==null) {
			System.out.println("G15Services/Soap/ConsultarPuestoVaucnadorRWS/consultarPuestoVacunador : idVacunatorio || fecha NULL");
			ResponseBuilder rb = Response.status(Status.BAD_REQUEST);
			return rb.build();
		}

		String token = cookie.getValue();
		String ci = null;
		try {
			ci = TokenSecurity.getIdClaim(TokenSecurity.validateJwtToken(token));
		} catch (InvalidJwtException e) {
			e.printStackTrace();
		}
        if( ci == null)
            throw new NotAuthorizedException("No se encuentra CI en token de Cookie - Unauthorized!");
		LOGGER.info("Cedula obtenida en REST: " + ci);
		
		String soapEndpointUrl = "http://localhost:8180/vacunatorio-services/AsignadoSoap?wsdl";
        String soapAction = "consultar";
        DtAsignado dt;
		try {
			System.out.println("G15Services/Soap/ConsultarPuestoVaucnadorRWS/consultarPuestoVacunador : " + fecha);
			dt = callSoapWebService(soapEndpointUrl, soapAction, Integer.parseInt(ci), fecha);
	        if (dt!=null) {
	        	System.out.println("G15Services/Soap/ConsultarPuestoVaucnadorRWS/consultarPuestoVacunador : " + dt.getIdPuesto());
	        	return Response.ok(dt).build();
	        }
	        else
	        	return Response.ok(new ErrorInfo(200, "Error")).build();
		} catch (SOAPException e) {
			// TODO Auto-generated catch block
			return Response.serverError().entity(new ErrorInfo(200, e.getMessage())).status(200).build();
		}
		/*try {
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
			LocalDate f = LocalDate.parse(fecha, formatter);
			
			//LocalDate f = LocalDate.from(fecha.toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
			//Date nuevaFecha = Date.from(f.atStartOfDay(ZoneId.systemDefault()).toInstant());
			return Response.ok(vs.consultarPuestoAsignadoVacunador(idVacunador, idVacunatorio, f)).build();
		} catch (VacunatorioNoCargadoException | UsuarioInexistente | VacunadorSinAsignar e) {
			return Response.serverError().entity(new ErrorInfo(200, e.getMessage())).status(200).build();
			//return Response.serverError().entity(e.getMessage()).status(400).build();
			//ResponseBuilder rb = Response.status(Status.BAD_REQUEST);
			//return rb.entity(e.getMessage()).build();
		}*/
	}
	
	private static void createSoapEnvelope(SOAPMessage soapMessage, int idVacunador, String fecha) throws SOAPException {
        SOAPPart soapPart = soapMessage.getSOAPPart();

        String myNamespace = "ws";
        String myNamespaceURI = "http://localhost:8180/vacunatorio-services/webservice/asignacionService";

        // SOAP Envelope
        SOAPEnvelope envelope = soapPart.getEnvelope();
        envelope.addNamespaceDeclaration(myNamespace, myNamespaceURI);

            /*
            Constructed SOAP Request Message:
            <SOAP-ENV:Envelope xmlns:SOAP-ENV="http://schemas.xmlsoap.org/soap/envelope/" xmlns:myNamespace="http://www.webserviceX.NET">
                <SOAP-ENV:Header/>
                <SOAP-ENV:Body>
                    <myNamespace:GetInfoByCity>
                        <myNamespace:USCity>New York</myNamespace:USCity>
                    </myNamespace:GetInfoByCity>
                </SOAP-ENV:Body>
            </SOAP-ENV:Envelope>
            */

        // SOAP Body
        SOAPBody soapBody = envelope.getBody();
        SOAPElement soapBodyElem = soapBody.addChildElement("consultarAsignacionVacunador", myNamespace);
        SOAPElement soapBodyElemC1 = soapBodyElem.addChildElement("arg0");
        SOAPElement soapBodyElemC2 = soapBodyElem.addChildElement("arg1");
        soapBodyElemC1.addTextNode(String.valueOf(idVacunador));
        soapBodyElemC2.addTextNode(fecha);
    }

    private static DtAsignado callSoapWebService(String soapEndpointUrl, String soapAction, int idVacunador, String fecha) throws SOAPException{
    	String error = "";
    	DtAsignado retorno = new DtAsignado();
    	try {
            SOAPConnectionFactory soapConnectionFactory = SOAPConnectionFactory.newInstance();
            SOAPConnection soapConnection = soapConnectionFactory.createConnection();

            SOAPMessage soapResponse = soapConnection.call(createSOAPRequest(soapAction, idVacunador, fecha), soapEndpointUrl);
            
            
            SOAPPart soapPart=soapResponse.getSOAPPart();

            SOAPEnvelope envelope=soapPart.getEnvelope();
            SOAPBody soapBody = envelope.getBody();
            Iterator<Node> itr=soapBody.getChildElements();
            while (itr.hasNext()) {

                Node node=(Node)itr.next();
                if (node.getNodeType()==Node.ELEMENT_NODE) {
                    Element ele=(Element)node;
                    //System.out.println(ele.getNodeName());
                    switch (ele.getNodeName()) {
                    
	                  case "ns2:consultarAsignacionVacunadorResponse":
	                     NodeList listaNodesResponse = ele.getChildNodes();

	                     for(int i=0;i<listaNodesResponse.getLength();i++){
						    Element responseChilds = (Element) listaNodesResponse.item(i);
						   
							switch (responseChilds.getNodeName()) {
							
							case "return":
							    NodeList returnChilds = responseChilds.getChildNodes();
							    for(int j=0;j<returnChilds.getLength();j++){
							      Element item = (Element) returnChilds.item(j);
							       switch (item.getNodeName()) {
							       case "fecha":
							    	   retorno.setFecha(item.getTextContent());
							           break;
							        case "idPuesto":
							        	retorno.setIdPuesto(item.getTextContent());
							           break;
							       default:
							          break;
							       }
							    }
							    break;
							default:
							   break;
							}
	                     }
	                 break;
	                  case "soap:Fault":
	                	  
	                	  NodeList errorNodes = ele.getChildNodes();
		                     for(int i=0;i<errorNodes.getLength();i++){	
							    Element errorChilds = (Element) errorNodes.item(i);
								switch (errorChilds.getNodeName()) {
								case "faultstring":
									error = errorChilds.getTextContent();
								}
		                     }
	                	  break;
                  default:
                	  
                     break;
                  }

                } else if (node.getNodeType()==Node.TEXT_NODE) {
                	return null;
                }
            }
           
            soapConnection.close();

        } catch (Exception e) {
            System.err.println("\nError occurred while sending SOAP Request to Server!\nMake sure you have the correct endpoint URL and SOAPAction!\n");
            //e.printStackTrace();
            return null;
        }
		if (!error.equals(""))
			throw new SOAPException(error);
        return retorno;
    }

    private static SOAPMessage createSOAPRequest(String soapAction, int idVacunador, String fecha) throws Exception {
        MessageFactory messageFactory = MessageFactory.newInstance();
        SOAPMessage soapMessage = messageFactory.createMessage();

        createSoapEnvelope(soapMessage, idVacunador, fecha);

        MimeHeaders headers = soapMessage.getMimeHeaders();
        headers.addHeader("SOAPAction", soapAction);

        soapMessage.saveChanges();

        return soapMessage;
    }
}
