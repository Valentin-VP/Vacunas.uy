package rest;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.logging.Logger;

import javax.annotation.security.DeclareRoles;
import javax.annotation.security.PermitAll;
import javax.ejb.EJB;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import datatypes.DtMensaje;
import datatypes.DtTransportista;
import datatypes.TransportistaInexistente;
import interfaces.ILoteDosisDaoLocal;
import interfaces.IMensajeLocal;
import interfaces.ITransportistaDaoLocal;
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
import rest.filter.ResponseBuilder;

@DeclareRoles({ "vacunador", "ciudadano", "administrador", "autoridad" })
@Path("/lotedosis")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class GestionLoteDosisRWS {
	private final Logger LOGGER = Logger.getLogger(getClass().getName());

	@EJB
	ILoteDosisDaoLocal cld;
	
	@EJB
	IMensajeLocal cm;
	
	@EJB
	ITransportistaDaoLocal ct;

	public GestionLoteDosisRWS() {

	}

	@PermitAll
	@POST
	@Path("/agregar")
	public Response crearLoteDosis(String datos) {
		try {
			JSONObject lote = new JSONObject(datos);
			/*try {
				DtTransportista t = ct.obtenerTransportista(Integer.valueOf(lote.getString("idTransportista")));
				urlTransportista = t.getUrl();
			} catch (TransportistaInexistente et) {
				return ResponseBuilder.createResponse(Response.Status.BAD_REQUEST, et.getMessage());
			}*/
			
			//String idTransportista = lote.getString("idTransportista");
			// Integer Integer idLote, String idVacunatorio, String idVacuna, Integer
			// cantidadTotal, float temperatura
			// cld.agregarLoteDosis(Integer.getInteger(lote.getString("idLote")),
			// lote.getString("idVacunatorio"), lote.getString("idVacuna"),
			// Integer.getInteger(lote.getString("cantidadTotal")));
			String soap;
			try {
				String urlTransportista;
				try {
					DtTransportista t = ct.obtenerTransportista(1);
					urlTransportista = t.getUrl();
				} catch (TransportistaInexistente et) {
					return ResponseBuilder.createResponse(Response.Status.BAD_REQUEST, et.getMessage());
				}
				//retorno = agregarLoteDosisASocio(lote.getString("idLote"), lote.getString("idVacunatorio"), lote.getString("idVacuna"),
				//		  lote.getString("idVacunatorio"), lote.getString("cantidadTotal"), urlTransportista);
				soap = agregarLoteDosisASocioSOAP("2","a", "a", "50", urlTransportista);
				
				LOGGER.info(soap);
				return Response.ok(soap).build();
			}catch (SOAPException e) {
				//e.printStackTrace();
				//LOGGER.severe(e.getMessage());
				return ResponseBuilder.createResponse(Response.Status.BAD_REQUEST, e.getMessage());
			}
		} catch (JSONException | NumberFormatException /* | LoteRepetido | VacunatorioNoCargadoException | VacunaInexistente */ e) {
			return ResponseBuilder.createResponse(Response.Status.BAD_REQUEST, e.getMessage());
		}

		// @WebParam(name = "idLote") String idLote, @WebParam(name = "idVacunatorio")
		// String idVacunatorio, @WebParam(name = "idVacuna") String idVacuna,
		// @WebParam(name = "cantidadTotal") String cantidadTotal

	}

	@PermitAll
	@POST
	@Path("/obtenerLoteSocio")
	public Response obtenerEstadoLoteDeSocio(String datos) {
		try {
			JSONObject lote = new JSONObject(datos);
			/*try {
				ct.obtenerTransportista(Integer.valueOf(lote.getString("idTransportista")));
			} catch (TransportistaInexistente et) {
				return ResponseBuilder.createResponse(Response.Status.BAD_REQUEST, et.getMessage());
			}*/
			//String idTransportista = lote.getString("idTransportista");
			String soap;
			try {
				String urlTransportista;
				try {
					DtTransportista t = ct.obtenerTransportista(1);
					urlTransportista = t.getUrl();
				} catch (TransportistaInexistente et) {
					return ResponseBuilder.createResponse(Response.Status.BAD_REQUEST, et.getMessage());
				}
				//retorno = obtenerEstadoLoteDosis(lote.getString("idLote"),
				//		 lote.getString("idVacunatorio"), lote.getString("idVacuna"), lote.getString("idVacunatorio"), urlTransportista);
				soap = obtenerEstadoLoteDosisSOAP("1","a", "a", urlTransportista);
				cm.agregarMensaje(soap);
				LOGGER.info(soap);
				
				return Response.ok(new DtMensaje(soap)).build();
			}catch (SOAPException e) {
				//e.printStackTrace();
				//LOGGER.severe(e.getMessage());
				return ResponseBuilder.createResponse(Response.Status.BAD_REQUEST, e.getMessage());
			}
		} catch (JSONException | NumberFormatException /* | LoteRepetido | VacunatorioNoCargadoException | VacunaInexistente */ e) {
			return ResponseBuilder.createResponse(Response.Status.BAD_REQUEST, e.getMessage());
		}
	}
	
	@PermitAll
	@POST
	@Path("/obtenerTodosLotesSocio")
	public Response obtenerEstadoTodosLotesDeSocio(String datos) {
		try {
			JSONObject lote = new JSONObject(datos);
			/*try {
				ct.obtenerTransportista(Integer.valueOf(lote.getString("idTransportista")));
			} catch (TransportistaInexistente et) {
				return ResponseBuilder.createResponse(Response.Status.BAD_REQUEST, et.getMessage());
			}*/
			//String idTransportista = lote.getString("idTransportista");
			ArrayList<String> soap;
			ArrayList<DtMensaje> dt = new ArrayList<>();
			try {
				String urlTransportista;
				try {
					DtTransportista t = ct.obtenerTransportista(1);
					urlTransportista = t.getUrl();
				} catch (TransportistaInexistente et) {
					return ResponseBuilder.createResponse(Response.Status.BAD_REQUEST, et.getMessage());
				}
				soap = obtenerTodosEstadoLoteDosisSOAP(urlTransportista);
				for (String s: soap) {
					cm.agregarMensaje(s);
					dt.add(new DtMensaje(s));
				}
				return Response.ok(dt).build();
			}catch (SOAPException e) {
				//e.printStackTrace();
				//LOGGER.severe(e.getMessage());
				return ResponseBuilder.createResponse(Response.Status.BAD_REQUEST, e.getMessage());
			}
		} catch (JSONException | NumberFormatException /* | LoteRepetido | VacunatorioNoCargadoException | VacunaInexistente */ e) {
			return ResponseBuilder.createResponse(Response.Status.BAD_REQUEST, e.getMessage());
		}
	}
	
	private String agregarLoteDosisASocioSOAP(String idLote, String idVacunatorio, String idVacuna, String cantidadTotal,
			String urlTransportista) throws SOAPException {
		String soapEndpointUrl = urlTransportista + "/transportista-web/NodoCentral?wsdl";
		String soapAction = "agregar";
		String error = "";
		String retorno = "";
		try {
			SOAPConnectionFactory soapConnectionFactory = SOAPConnectionFactory.newInstance();
			SOAPConnection soapConnection = soapConnectionFactory.createConnection();
			MessageFactory messageFactory = MessageFactory.newInstance();
			SOAPMessage soapMessage = messageFactory.createMessage();
			SOAPPart soapPart = soapMessage.getSOAPPart();

			String myNamespace = "nod";
			String myNamespaceURI = "webservice/nodoCentral";

			// SOAP Envelope
			SOAPEnvelope envelope = soapPart.getEnvelope();
			envelope.addNamespaceDeclaration(myNamespace, myNamespaceURI);

			// SOAP Body
			SOAPBody soapBody = envelope.getBody();
			SOAPElement soapBodyElemRecibirNuevoLote = soapBody.addChildElement("recibirNuevoLote", myNamespace);
			SOAPElement soapBodyElemIDLote = soapBodyElemRecibirNuevoLote.addChildElement("idLote");
			SOAPElement soapBodyElemIDVacunatorio = soapBodyElemRecibirNuevoLote.addChildElement("idVacunatorio");
			SOAPElement soapBodyElemIDVacuna = soapBodyElemRecibirNuevoLote.addChildElement("idVacuna");
			SOAPElement soapBodyElemCantTotal = soapBodyElemRecibirNuevoLote.addChildElement("cantidadTotal");
			soapBodyElemIDLote.addTextNode(String.valueOf(idLote));
			soapBodyElemIDVacunatorio.addTextNode(idVacunatorio);
			soapBodyElemIDVacuna.addTextNode(idVacuna);
			soapBodyElemCantTotal.addTextNode(String.valueOf(cantidadTotal));
			MimeHeaders headers = soapMessage.getMimeHeaders();
			headers.addHeader("SOAPAction", soapAction);

			soapMessage.saveChanges();
			SOAPMessage soapResponse = soapConnection.call(soapMessage, soapEndpointUrl);
			SOAPPart soapPartResponse=soapResponse.getSOAPPart();

            SOAPEnvelope envelopeResponse=soapPartResponse.getEnvelope();
            SOAPBody soapBodyResponse = envelopeResponse.getBody();
			Iterator<Node> itr = soapBodyResponse.getChildElements();
			while (itr.hasNext()) {

				Node node = (Node) itr.next();
				if (node.getNodeType() == Node.ELEMENT_NODE) {
					Element respuesta = (Element) node;
					// System.out.println(ele.getNodeName());
					switch (respuesta.getNodeName()) {
					case "ns2:recibirNuevoLoteResponse":
						NodeList listaNodesRespuesta = respuesta.getChildNodes();

						for (int i = 0; i < listaNodesRespuesta.getLength(); i++) {
							Element responseChilds = (Element) listaNodesRespuesta.item(i);

							switch (responseChilds.getNodeName()) {
							case "return":
								retorno = responseChilds.getTextContent();
								break;
							default:
								break;
							}
						}
						break;
					case "soap:Fault":
						NodeList errorNodes = respuesta.getChildNodes();
						for (int i = 0; i < errorNodes.getLength(); i++) {
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
				} else if (node.getNodeType() == Node.TEXT_NODE) {
					return null;
				}
			}

			soapConnection.close();

		} catch (Exception e) {
			System.err.println(
					"\nError occurred while sending SOAP Request to Server!\nMake sure you have the correct endpoint URL and SOAPAction!\n");
			throw new SOAPException(e.getMessage());
			// e.printStackTrace();
		}
		if (!error.equals(""))
			throw new SOAPException(error);
		return retorno;

	}
	
	private String obtenerEstadoLoteDosisSOAP(String idLote, String idVacunatorio, String idVacuna,
			String urlTransportista) throws SOAPException {
		String soapEndpointUrl = urlTransportista + "/transportista-web/NodoCentral?wsdl";
		String soapAction = "obtener";
		String error = "";
		String retorno = "";
		try {
			SOAPConnectionFactory soapConnectionFactory = SOAPConnectionFactory.newInstance();
			SOAPConnection soapConnection = soapConnectionFactory.createConnection();
			MessageFactory messageFactory = MessageFactory.newInstance();
			SOAPMessage soapMessage = messageFactory.createMessage();
			SOAPPart soapPart = soapMessage.getSOAPPart();

			String myNamespace = "nod";
			String myNamespaceURI = "webservice/nodoCentral";

			// SOAP Envelope
			SOAPEnvelope envelope = soapPart.getEnvelope();
			envelope.addNamespaceDeclaration(myNamespace, myNamespaceURI);

			// SOAP Body
			SOAPBody soapBody = envelope.getBody();
			SOAPElement soapBodyElemObtenerLote = soapBody.addChildElement("informarEstadoLoteDosis", myNamespace);
			SOAPElement soapBodyElemIDLote = soapBodyElemObtenerLote.addChildElement("idLote");
			SOAPElement soapBodyElemIDVacunatorio = soapBodyElemObtenerLote.addChildElement("idVacunatorio");
			SOAPElement soapBodyElemIDVacuna = soapBodyElemObtenerLote.addChildElement("idVacuna");
			soapBodyElemIDLote.addTextNode(String.valueOf(idLote));
			soapBodyElemIDVacunatorio.addTextNode(idVacunatorio);
			soapBodyElemIDVacuna.addTextNode(idVacuna);
			MimeHeaders headers = soapMessage.getMimeHeaders();
			headers.addHeader("SOAPAction", soapAction);

			soapMessage.saveChanges();
			SOAPMessage soapResponse = soapConnection.call(soapMessage, soapEndpointUrl);
			SOAPPart soapPartResponse=soapResponse.getSOAPPart();

            SOAPEnvelope envelopeResponse=soapPartResponse.getEnvelope();
            SOAPBody soapBodyResponse = envelopeResponse.getBody();
			Iterator<Node> itr = soapBodyResponse.getChildElements();
			while (itr.hasNext()) {

				Node node = (Node) itr.next();
				if (node.getNodeType() == Node.ELEMENT_NODE) {
					Element respuesta = (Element) node;
					// System.out.println(ele.getNodeName());
					switch (respuesta.getNodeName()) {
					case "ns2:informarEstadoLoteDosisResponse":
						NodeList listaNodesRespuesta = respuesta.getChildNodes();

						for (int i = 0; i < listaNodesRespuesta.getLength(); i++) {
							Element responseChilds = (Element) listaNodesRespuesta.item(i);

							switch (responseChilds.getNodeName()) {
							case "return":
								retorno = responseChilds.getTextContent();
								break;
							default:
								break;
							}
						}
						break;
					case "soap:Fault":
						NodeList errorNodes = respuesta.getChildNodes();
						for (int i = 0; i < errorNodes.getLength(); i++) {
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
				} else if (node.getNodeType() == Node.TEXT_NODE) {
					return null;
				}
			}

			soapConnection.close();

		} catch (Exception e) {
			System.err.println(
					"\nError occurred while sending SOAP Request to Server!\nMake sure you have the correct endpoint URL and SOAPAction!\n");
			throw new SOAPException(e.getMessage());
			// e.printStackTrace();
		}
		if (!error.equals(""))
			throw new SOAPException(error);
		return retorno;

	}
	
	private ArrayList<String> obtenerTodosEstadoLoteDosisSOAP(String urlTransportista) throws SOAPException {
		String soapEndpointUrl = urlTransportista + "/transportista-web/NodoCentral?wsdl";
		String soapAction = "listar";
		String error = "";
		ArrayList<String> retorno = new ArrayList<>();
		try {
			SOAPConnectionFactory soapConnectionFactory = SOAPConnectionFactory.newInstance();
			SOAPConnection soapConnection = soapConnectionFactory.createConnection();
			MessageFactory messageFactory = MessageFactory.newInstance();
			SOAPMessage soapMessage = messageFactory.createMessage();
			SOAPPart soapPart = soapMessage.getSOAPPart();

			String myNamespace = "nod";
			String myNamespaceURI = "webservice/nodoCentral";

			// SOAP Envelope
			SOAPEnvelope envelope = soapPart.getEnvelope();
			envelope.addNamespaceDeclaration(myNamespace, myNamespaceURI);

			// SOAP Body
			SOAPBody soapBody = envelope.getBody();
			SOAPElement soapBodyElemObtenerTodosLotes = soapBody.addChildElement("informarTodosEstadoLoteDosis", myNamespace);
			MimeHeaders headers = soapMessage.getMimeHeaders();
			headers.addHeader("SOAPAction", soapAction);

			soapMessage.saveChanges();
			SOAPMessage soapResponse = soapConnection.call(soapMessage, soapEndpointUrl);
			SOAPPart soapPartResponse=soapResponse.getSOAPPart();

            SOAPEnvelope envelopeResponse=soapPartResponse.getEnvelope();
            SOAPBody soapBodyResponse = envelopeResponse.getBody();
			Iterator<Node> itr = soapBodyResponse.getChildElements();
			while (itr.hasNext()) {

				Node node = (Node) itr.next();
				if (node.getNodeType() == Node.ELEMENT_NODE) {
					Element respuesta = (Element) node;
					// System.out.println(ele.getNodeName());
					switch (respuesta.getNodeName()) {
					case "ns2:informarTodosEstadoLoteDosisResponse":
						NodeList listaNodesRespuesta = respuesta.getChildNodes();

						for (int i = 0; i < listaNodesRespuesta.getLength(); i++) {
							Element responseChilds = (Element) listaNodesRespuesta.item(i);

							switch (responseChilds.getNodeName()) {
							case "return":
								retorno.add(responseChilds.getTextContent());
								break;
							default:
								break;
							}
						}
						break;
					case "soap:Fault":
						NodeList errorNodes = respuesta.getChildNodes();
						for (int i = 0; i < errorNodes.getLength(); i++) {
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
				} else if (node.getNodeType() == Node.TEXT_NODE) {
					return null;
				}
			}

			soapConnection.close();

		} catch (Exception e) {
			System.err.println(
					"\nError occurred while sending SOAP Request to Server!\nMake sure you have the correct endpoint URL and SOAPAction!\n");
			throw new SOAPException(e.getMessage());
			// e.printStackTrace();
		}
		if (!error.equals(""))
			throw new SOAPException(error);
		return retorno;

	}
}
