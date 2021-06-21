package rest;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Logger;

import javax.annotation.security.DeclareRoles;
import javax.annotation.security.PermitAll;
import javax.ejb.EJB;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import datatypes.DtLoteDosis;
import datatypes.DtMensaje;
import datatypes.DtStock;
import datatypes.DtTransportista;
import datatypes.EstadoLote;
import datatypes.TransportistaInexistente;
import exceptions.CantidadNula;
import exceptions.LoteInexistente;
import exceptions.LoteRepetido;
import exceptions.StockVacunaVacunatorioExistente;
import exceptions.StockVacunaVacunatorioInexistente;
import exceptions.VacunaInexistente;
import exceptions.VacunatorioNoCargadoException;
import interfaces.IControladorVacunatorioLocal;
import interfaces.ILoteDosisDaoLocal;
import interfaces.IMensajeLocal;
import interfaces.IStockDaoLocal;
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
	
	@EJB
	IStockDaoLocal cs;

	public GestionLoteDosisRWS() {

	}

	/*
	@PermitAll
	@POST
	@Path("/listar")
	public Response listarLoteDosis() {
		List<DtLoteDosis> retorno = cld.listarLotesDosis();
		if (retorno.isEmpty())
			return ResponseBuilder.createResponse(Response.Status.BAD_REQUEST, "Sin lotes de dosis.");
		return Response.ok().entity(retorno).build();
	}
	*/
	@PermitAll
	@GET
	@Path("/listar")
	public Response listarLoteDosis(@QueryParam("idVacuna") String idVacuna,@QueryParam("idVacunatorio") String idVacunatorio) {
		if (idVacuna == null || idVacunatorio == null) {
			return ResponseBuilder.createResponse(Response.Status.BAD_REQUEST, "No se ingresaron todos los parametros necesarios.");
		}
		List<DtLoteDosis> retorno = cld.listarLotesDosisVacunaVacunatorio(idVacunatorio, idVacuna);
		if (retorno.isEmpty())
			return ResponseBuilder.createResponse(Response.Status.BAD_REQUEST, "Sin lotes de dosis.");
		return Response.ok().entity(retorno).build();
			
	}
	
	@PermitAll
	@GET
	@Path("/obtener")
	public Response obtenerLoteDosis(@QueryParam("idLote")String idLote, @QueryParam("idVacuna") String idVacuna,@QueryParam("idVacunatorio") String idVacunatorio) {
		if (idLote == null || idVacuna == null || idVacunatorio == null) {
			return ResponseBuilder.createResponse(Response.Status.BAD_REQUEST, "No se ingresaron todos los parametros necesarios.");
		}
		try {
			DtLoteDosis retorno = cld.obtenerLoteDosis(Integer.valueOf(idLote), idVacunatorio, idVacuna);
			return Response.ok().entity(retorno).build();
		} catch (NumberFormatException | LoteInexistente e) {
			return ResponseBuilder.createResponse(Response.Status.BAD_REQUEST, e.getMessage());
		}
	}
	
	@PermitAll
	@GET
	@Path("/listarMensajes")
	public Response listarMensajesLocales() {
		return Response.ok().entity(cm.listarMensajes()).build();
	}
	
	@PermitAll
	@POST
	@Path("/modificar")
	public Response modificarLoteDosis(String datos) {
		try {
			JSONObject lote = new JSONObject(datos);
			try {
				cld.modificarLoteDosis(Integer.valueOf(lote.getString("idLote")), lote.getString("idVacunatorio"), lote.getString("idVacuna"), Integer.valueOf(lote.getString("cantidadTotal")),
						Integer.valueOf(lote.getString("cantidadEntregada")), Integer.valueOf(lote.getString("cantidadDescartada")), lote.getString("estadoLote"),
						Float.parseFloat(lote.getString("temperatura")), Integer.valueOf(lote.getString("transportista")));
				if (lote.getString("estadoLote").equals("Recibido")) {
					int cantidadReal = Integer.valueOf(lote.getString("cantidadEntregada"));
					try {
						cs.agregarStock(lote.getString("idVacunatorio"), lote.getString("idVacuna"), cantidadReal);
						return ResponseBuilder.createResponse(Response.Status.CREATED, "Se ha modificado el lote de dosis. Se agregó el stock correspondiente.");
					} catch (CantidadNula e) {
						return ResponseBuilder.createResponse(Response.Status.BAD_REQUEST, e.getMessage());
					} catch (StockVacunaVacunatorioExistente e) {
						
						try {
							DtStock dts = cs.obtenerStock(lote.getString("idVacunatorio"), lote.getString("idVacuna"));
							cs.modificarStock(lote.getString("idVacunatorio"), lote.getString("idVacuna"), dts.getCantidad()+cantidadReal,
									dts.getDescartadas(), dts.getAdministradas(), dts.getDisponibles()+cantidadReal);
						} catch (StockVacunaVacunatorioInexistente e1) {
							return ResponseBuilder.createResponse(Response.Status.BAD_REQUEST, e.getMessage());
						}
						
						return ResponseBuilder.createResponse(Response.Status.CREATED, "Se ha modificado el lote de dosis. Se agregó el stock correspondiente.");
					}
				}
				return ResponseBuilder.createResponse(Response.Status.OK, "Se ha modificado el lote de dosis. No se agregó stock al vacunatorio.");
			}catch (LoteInexistente | TransportistaInexistente | VacunatorioNoCargadoException | VacunaInexistente e) {
				return ResponseBuilder.createResponse(Response.Status.BAD_REQUEST, e.getMessage());
			}
		} catch (JSONException | NumberFormatException /* | LoteRepetido | VacunatorioNoCargadoException | VacunaInexistente */ e) {
			return ResponseBuilder.createResponse(Response.Status.BAD_REQUEST, e.getMessage());
		}
	}
	
	@PermitAll
	@POST
	@Path("/agregar")
	public Response crearLoteDosis(String datos) {
		try {
			JSONObject lote = new JSONObject(datos);
			String urlTransportista;
			try {
				DtTransportista t = ct.obtenerTransportista(Integer.valueOf(lote.getString("idTransportista")));
				urlTransportista = t.getUrl();
			} catch (TransportistaInexistente et) {
				return ResponseBuilder.createResponse(Response.Status.BAD_REQUEST, et.getMessage());
			}
			
			//String idTransportista = lote.getString("idTransportista");
			// Integer Integer idLote, String idVacunatorio, String idVacuna, Integer
			// cantidadTotal, float temperatura
			// cld.agregarLoteDosis(Integer.getInteger(lote.getString("idLote")),
			// lote.getString("idVacunatorio"), lote.getString("idVacuna"),
			// Integer.getInteger(lote.getString("cantidadTotal")));
			String soap;
			try {
				
				/*try {
					DtTransportista t = ct.obtenerTransportista(1);
					urlTransportista = t.getUrl();
				} catch (TransportistaInexistente et) {
					return ResponseBuilder.createResponse(Response.Status.BAD_REQUEST, et.getMessage());
				}*/
				//retorno = agregarLoteDosisASocio(lote.getString("idLote"), lote.getString("idVacunatorio"), lote.getString("idVacuna"),
				//		  lote.getString("idVacunatorio"), lote.getString("cantidadTotal"), urlTransportista);
				soap = agregarLoteDosisASocioSOAP(lote.getString("idLote"), lote.getString("idVacunatorio"), lote.getString("idVacuna"), lote.getString("cantidadTotal"), urlTransportista);
				//soap = agregarLoteDosisASocioSOAP("2","a", "a", "50", urlTransportista);
				//soap = agregarLoteDosisASocioSOAP("1","a", "a", "50", urlTransportista);
				//LOGGER.info(soap);
				cld.agregarLoteDosis(Integer.valueOf(lote.getString("idLote")), lote.getString("idVacunatorio"), lote.getString("idVacuna"), Integer.valueOf(lote.getString("cantidadTotal")));
				cld.setTransportistaToLoteDosis(Integer.valueOf(lote.getString("idTransportista")), Integer.valueOf(lote.getString("idLote")), lote.getString("idVacunatorio"), lote.getString("idVacuna"));
				return ResponseBuilder.createResponse(Response.Status.CREATED, "Se ha agregado el lote de dosis. Se debe modificarlo posteriormente para confirmarlo o cancelarlo.");
			}catch (SOAPException | LoteRepetido | VacunatorioNoCargadoException | VacunaInexistente | TransportistaInexistente e) {
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
	@GET
	@Path("/obtenerInfoLoteSocio")
	public Response obtenerEstadoLoteDeSocio(@QueryParam("idLote")String idLote, @QueryParam("idVacuna") String idVacuna,@QueryParam("idVacunatorio") String idVacunatorio) {
		if (idLote == null || idVacuna == null || idVacunatorio == null) {
			return ResponseBuilder.createResponse(Response.Status.BAD_REQUEST, "No se ingresaron todos los parametros necesarios.");
		}
		try {
			DtLoteDosis dt;
			try {
				dt = cld.obtenerLoteDosis(Integer.valueOf(idLote), idVacunatorio, idVacuna);
			} catch (LoteInexistente e) {
				return ResponseBuilder.createResponse(Response.Status.BAD_REQUEST, e.getMessage());
			}
			String soap;
			try {
				String urlTransportista;
				try {
					DtTransportista t = ct.obtenerTransportista(dt.getTransportista());
					urlTransportista = t.getUrl();
				} catch (TransportistaInexistente et) {
					return ResponseBuilder.createResponse(Response.Status.BAD_REQUEST, et.getMessage());
				}
				soap = obtenerEstadoLoteDosisSOAP(idLote, idVacunatorio, idVacuna, urlTransportista);
				//cm.agregarMensaje(soap); //no veo que tenga sentido guardar el mensaje a demanda porque ya lo trae cuando se  genera un evento.
				
				return Response.ok(new DtMensaje(soap)).build();
			}catch (SOAPException e) {
				return ResponseBuilder.createResponse(Response.Status.BAD_REQUEST, e.getMessage());
			}
		} catch (NumberFormatException /* | LoteRepetido | VacunatorioNoCargadoException | VacunaInexistente */ e) {
			return ResponseBuilder.createResponse(Response.Status.BAD_REQUEST, e.getMessage());
		}
	}
	
	@PermitAll
	@GET
	@Path("/obtenerInfoTodosLotesSocio")
	public Response obtenerEstadoTodosLotesDeSocio(@QueryParam("idTransportista")String idTransportista) {
		try {
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
					DtTransportista t = ct.obtenerTransportista(Integer.valueOf(idTransportista));
					urlTransportista = t.getUrl();
				} catch (TransportistaInexistente et) {
					return ResponseBuilder.createResponse(Response.Status.BAD_REQUEST, et.getMessage());
				}
				soap = obtenerTodosEstadoLoteDosisSOAP(urlTransportista);
				for (String s: soap) {
					//cm.agregarMensaje(s); //no me parece interesante volver a agregar estos mensajes
					dt.add(new DtMensaje(s));
				}
				return Response.ok(dt).build();
			}catch (SOAPException e) {
				//e.printStackTrace();
				//LOGGER.severe(e.getMessage());
				return ResponseBuilder.createResponse(Response.Status.BAD_REQUEST, e.getMessage());
			}
		} catch ( NumberFormatException /* | LoteRepetido | VacunatorioNoCargadoException | VacunaInexistente */ e) {
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
