
package soap.client;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Clase Java para informarEstadoLoteDosis complex type.
 * 
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
 * 
 * <pre>
 * &lt;complexType name="informarEstadoLoteDosis"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="idLote" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="idVacunatorio" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="idVacuna" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "informarEstadoLoteDosis", propOrder = {
    "idLote",
    "idVacunatorio",
    "idVacuna"
})
public class InformarEstadoLoteDosis {

    protected String idLote;
    protected String idVacunatorio;
    protected String idVacuna;

    /**
     * Obtiene el valor de la propiedad idLote.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIdLote() {
        return idLote;
    }

    /**
     * Define el valor de la propiedad idLote.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIdLote(String value) {
        this.idLote = value;
    }

    /**
     * Obtiene el valor de la propiedad idVacunatorio.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIdVacunatorio() {
        return idVacunatorio;
    }

    /**
     * Define el valor de la propiedad idVacunatorio.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIdVacunatorio(String value) {
        this.idVacunatorio = value;
    }

    /**
     * Obtiene el valor de la propiedad idVacuna.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIdVacuna() {
        return idVacuna;
    }

    /**
     * Define el valor de la propiedad idVacuna.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIdVacuna(String value) {
        this.idVacuna = value;
    }

}
