/**
 * GetCampaignOpensResponse.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package services.webservice.rspread;

public class GetCampaignOpensResponse  implements java.io.Serializable {
    private services.webservice.rspread.GetCampaignOpensResponseGetCampaignOpensResult getCampaignOpensResult;

    public GetCampaignOpensResponse() {
    }

    public GetCampaignOpensResponse(
           services.webservice.rspread.GetCampaignOpensResponseGetCampaignOpensResult getCampaignOpensResult) {
           this.getCampaignOpensResult = getCampaignOpensResult;
    }


    /**
     * Gets the getCampaignOpensResult value for this GetCampaignOpensResponse.
     * 
     * @return getCampaignOpensResult
     */
    public services.webservice.rspread.GetCampaignOpensResponseGetCampaignOpensResult getGetCampaignOpensResult() {
        return getCampaignOpensResult;
    }


    /**
     * Sets the getCampaignOpensResult value for this GetCampaignOpensResponse.
     * 
     * @param getCampaignOpensResult
     */
    public void setGetCampaignOpensResult(services.webservice.rspread.GetCampaignOpensResponseGetCampaignOpensResult getCampaignOpensResult) {
        this.getCampaignOpensResult = getCampaignOpensResult;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof GetCampaignOpensResponse)) return false;
        GetCampaignOpensResponse other = (GetCampaignOpensResponse) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.getCampaignOpensResult==null && other.getGetCampaignOpensResult()==null) || 
             (this.getCampaignOpensResult!=null &&
              this.getCampaignOpensResult.equals(other.getGetCampaignOpensResult())));
        __equalsCalc = null;
        return _equals;
    }

    private boolean __hashCodeCalc = false;
    public synchronized int hashCode() {
        if (__hashCodeCalc) {
            return 0;
        }
        __hashCodeCalc = true;
        int _hashCode = 1;
        if (getGetCampaignOpensResult() != null) {
            _hashCode += getGetCampaignOpensResult().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(GetCampaignOpensResponse.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://service.reasonablespread.com/", ">GetCampaignOpensResponse"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("getCampaignOpensResult");
        elemField.setXmlName(new javax.xml.namespace.QName("http://service.reasonablespread.com/", "GetCampaignOpensResult"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://service.reasonablespread.com/", ">>GetCampaignOpensResponse>GetCampaignOpensResult"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
    }

    /**
     * Return type metadata object
     */
    public static org.apache.axis.description.TypeDesc getTypeDesc() {
        return typeDesc;
    }

    /**
     * Get Custom Serializer
     */
    public static org.apache.axis.encoding.Serializer getSerializer(
           java.lang.String mechType, 
           java.lang.Class _javaType,  
           javax.xml.namespace.QName _xmlType) {
        return 
          new  org.apache.axis.encoding.ser.BeanSerializer(
            _javaType, _xmlType, typeDesc);
    }

    /**
     * Get Custom Deserializer
     */
    public static org.apache.axis.encoding.Deserializer getDeserializer(
           java.lang.String mechType, 
           java.lang.Class _javaType,  
           javax.xml.namespace.QName _xmlType) {
        return 
          new  org.apache.axis.encoding.ser.BeanDeserializer(
            _javaType, _xmlType, typeDesc);
    }

}
