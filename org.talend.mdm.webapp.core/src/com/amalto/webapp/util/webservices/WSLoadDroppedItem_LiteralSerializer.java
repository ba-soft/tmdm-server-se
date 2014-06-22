// This class was generated by the JAXRPC SI, do not edit.
// Contents subject to change without notice.
// JAX-RPC Standard Implementation 
// Generated source version: 1.1.2

package com.amalto.webapp.util.webservices;

import com.sun.xml.rpc.encoding.*;
import com.sun.xml.rpc.encoding.xsd.XSDConstants;
import com.sun.xml.rpc.encoding.literal.*;
import com.sun.xml.rpc.encoding.literal.DetailFragmentDeserializer;
import com.sun.xml.rpc.encoding.simpletype.*;
import com.sun.xml.rpc.encoding.soap.SOAPConstants;
import com.sun.xml.rpc.encoding.soap.SOAP12Constants;
import com.sun.xml.rpc.streaming.*;
import com.sun.xml.rpc.wsdl.document.schema.SchemaConstants;
import javax.xml.namespace.QName;
import java.util.List;
import java.util.ArrayList;

public class WSLoadDroppedItem_LiteralSerializer extends LiteralObjectSerializerBase implements Initializable  {
    private static final QName ns1_wsDroppedItemPK_QNAME = new QName("", "wsDroppedItemPK");
    private static final QName ns2_WSDroppedItemPK_TYPE_QNAME = new QName("urn-com-amalto-xtentis-webservice", "WSDroppedItemPK");
    private CombinedSerializer ns2_myWSDroppedItemPK_LiteralSerializer;
    
    public WSLoadDroppedItem_LiteralSerializer(QName type, String encodingStyle) {
        this(type, encodingStyle, false);
    }
    
    public WSLoadDroppedItem_LiteralSerializer(QName type, String encodingStyle, boolean encodeType) {
        super(type, true, encodingStyle, encodeType);
    }
    
    public void initialize(InternalTypeMappingRegistry registry) throws Exception {
        ns2_myWSDroppedItemPK_LiteralSerializer = (CombinedSerializer)registry.getSerializer("", com.amalto.webapp.util.webservices.WSDroppedItemPK.class, ns2_WSDroppedItemPK_TYPE_QNAME);
    }
    
    public Object doDeserialize(XMLReader reader,
        SOAPDeserializationContext context) throws Exception {
        com.amalto.webapp.util.webservices.WSLoadDroppedItem instance = new com.amalto.webapp.util.webservices.WSLoadDroppedItem();
        Object member=null;
        QName elementName;
        List values;
        Object value;
        
        reader.nextElementContent();
        elementName = reader.getName();
        if (reader.getState() == XMLReader.START) {
            if (elementName.equals(ns1_wsDroppedItemPK_QNAME)) {
                member = ns2_myWSDroppedItemPK_LiteralSerializer.deserialize(ns1_wsDroppedItemPK_QNAME, reader, context);
                if (member == null) {
                    throw new DeserializationException("literal.unexpectedNull");
                }
                instance.setWsDroppedItemPK((com.amalto.webapp.util.webservices.WSDroppedItemPK)member);
                reader.nextElementContent();
            } else {
                throw new DeserializationException("literal.unexpectedElementName", new Object[] { ns1_wsDroppedItemPK_QNAME, reader.getName() });
            }
        }
        else {
            throw new DeserializationException("literal.expectedElementName", reader.getName().toString());
        }
        
        XMLReaderUtil.verifyReaderState(reader, XMLReader.END);
        return (Object)instance;
    }
    
    public void doSerializeAttributes(Object obj, XMLWriter writer, SOAPSerializationContext context) throws Exception {
        com.amalto.webapp.util.webservices.WSLoadDroppedItem instance = (com.amalto.webapp.util.webservices.WSLoadDroppedItem)obj;
        
    }
    public void doSerialize(Object obj, XMLWriter writer, SOAPSerializationContext context) throws Exception {
        com.amalto.webapp.util.webservices.WSLoadDroppedItem instance = (com.amalto.webapp.util.webservices.WSLoadDroppedItem)obj;
        
        if (instance.getWsDroppedItemPK() == null) {
            throw new SerializationException("literal.unexpectedNull");
        }
        ns2_myWSDroppedItemPK_LiteralSerializer.serialize(instance.getWsDroppedItemPK(), ns1_wsDroppedItemPK_QNAME, null, writer, context);
    }
}
