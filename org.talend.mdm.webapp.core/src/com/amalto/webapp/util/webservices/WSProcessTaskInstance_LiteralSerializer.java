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

public class WSProcessTaskInstance_LiteralSerializer extends LiteralObjectSerializerBase implements Initializable  {
    private static final QName ns1_uuid_QNAME = new QName("", "uuid");
    private static final QName ns3_string_TYPE_QNAME = SchemaConstants.QNAME_TYPE_STRING;
    private CombinedSerializer ns3_myns3_string__java_lang_String_String_Serializer;
    private static final QName ns1_status_QNAME = new QName("", "status");
    private static final QName ns1_candidates_QNAME = new QName("", "candidates");
    private static final QName ns1_name_QNAME = new QName("", "name");
    private static final QName ns1_readyDate_QNAME = new QName("", "readyDate");
    private static final QName ns1_processName_QNAME = new QName("", "processName");
    private static final QName ns1_processVersion_QNAME = new QName("", "processVersion");
    private static final QName ns1_processInstanceNb_QNAME = new QName("", "processInstanceNb");
    private static final QName ns1_processInstanceUUID_QNAME = new QName("", "processInstanceUUID");
    private static final QName ns1_processDefineUUID_QNAME = new QName("", "processDefineUUID");
    
    public WSProcessTaskInstance_LiteralSerializer(QName type, String encodingStyle) {
        this(type, encodingStyle, false);
    }
    
    public WSProcessTaskInstance_LiteralSerializer(QName type, String encodingStyle, boolean encodeType) {
        super(type, true, encodingStyle, encodeType);
    }
    
    public void initialize(InternalTypeMappingRegistry registry) throws Exception {
        ns3_myns3_string__java_lang_String_String_Serializer = (CombinedSerializer)registry.getSerializer("", java.lang.String.class, ns3_string_TYPE_QNAME);
    }
    
    public Object doDeserialize(XMLReader reader,
        SOAPDeserializationContext context) throws Exception {
        com.amalto.webapp.util.webservices.WSProcessTaskInstance instance = new com.amalto.webapp.util.webservices.WSProcessTaskInstance();
        Object member=null;
        QName elementName;
        List values;
        Object value;
        
        reader.nextElementContent();
        while (reader.getState() == XMLReader.START) {
            elementName = reader.getName();
            if (elementName.equals(ns1_uuid_QNAME)) {
                member = ns3_myns3_string__java_lang_String_String_Serializer.deserialize(ns1_uuid_QNAME, reader, context);
                if (member == null) {
                    throw new DeserializationException("literal.unexpectedNull");
                }
                instance.setUuid((java.lang.String)member);
                reader.nextElementContent();
            }
            else if (elementName.equals(ns1_status_QNAME)) {
                member = ns3_myns3_string__java_lang_String_String_Serializer.deserialize(ns1_status_QNAME, reader, context);
                if (member == null) {
                    throw new DeserializationException("literal.unexpectedNull");
                }
                instance.setStatus((java.lang.String)member);
                reader.nextElementContent();
            }
            else if (elementName.equals(ns1_candidates_QNAME)) {
                member = ns3_myns3_string__java_lang_String_String_Serializer.deserialize(ns1_candidates_QNAME, reader, context);
                instance.setCandidates((java.lang.String)member);
                reader.nextElementContent();
            }
            else if (elementName.equals(ns1_name_QNAME)) {
                member = ns3_myns3_string__java_lang_String_String_Serializer.deserialize(ns1_name_QNAME, reader, context);
                if (member == null) {
                    throw new DeserializationException("literal.unexpectedNull");
                }
                instance.setName((java.lang.String)member);
                reader.nextElementContent();
            }
            else if (elementName.equals(ns1_readyDate_QNAME)) {
                member = ns3_myns3_string__java_lang_String_String_Serializer.deserialize(ns1_readyDate_QNAME, reader, context);
                if (member == null) {
                    throw new DeserializationException("literal.unexpectedNull");
                }
                instance.setReadyDate((java.lang.String)member);
                reader.nextElementContent();
            }
            else if (elementName.equals(ns1_processName_QNAME)) {
                member = ns3_myns3_string__java_lang_String_String_Serializer.deserialize(ns1_processName_QNAME, reader, context);
                if (member == null) {
                    throw new DeserializationException("literal.unexpectedNull");
                }
                instance.setProcessName((java.lang.String)member);
                reader.nextElementContent();
            }
            else if (elementName.equals(ns1_processVersion_QNAME)) {
                member = ns3_myns3_string__java_lang_String_String_Serializer.deserialize(ns1_processVersion_QNAME, reader, context);
                if (member == null) {
                    throw new DeserializationException("literal.unexpectedNull");
                }
                instance.setProcessVersion((java.lang.String)member);
                reader.nextElementContent();
            }
            else if (elementName.equals(ns1_processInstanceNb_QNAME)) {
                member = ns3_myns3_string__java_lang_String_String_Serializer.deserialize(ns1_processInstanceNb_QNAME, reader, context);
                if (member == null) {
                    throw new DeserializationException("literal.unexpectedNull");
                }
                instance.setProcessInstanceNb((java.lang.String)member);
                reader.nextElementContent();
            }
            else if (elementName.equals(ns1_processInstanceUUID_QNAME)) {
                member = ns3_myns3_string__java_lang_String_String_Serializer.deserialize(ns1_processInstanceUUID_QNAME, reader, context);
                if (member == null) {
                    throw new DeserializationException("literal.unexpectedNull");
                }
                instance.setProcessInstanceUUID((java.lang.String)member);
                reader.nextElementContent();
            }
            else if (elementName.equals(ns1_processDefineUUID_QNAME)) {
                member = ns3_myns3_string__java_lang_String_String_Serializer.deserialize(ns1_processDefineUUID_QNAME, reader, context);
                if (member == null) {
                    throw new DeserializationException("literal.unexpectedNull");
                }
                instance.setProcessDefineUUID((java.lang.String)member);
                reader.nextElementContent();
            }
            else {
                throw new DeserializationException("literal.unexpectedElementName", new Object[] { elementName, reader.getName()});
            }
        }
        
        XMLReaderUtil.verifyReaderState(reader, XMLReader.END);
        return (Object)instance;
    }
    
    public void doSerializeAttributes(Object obj, XMLWriter writer, SOAPSerializationContext context) throws Exception {
        com.amalto.webapp.util.webservices.WSProcessTaskInstance instance = (com.amalto.webapp.util.webservices.WSProcessTaskInstance)obj;
        
    }
    public void doSerialize(Object obj, XMLWriter writer, SOAPSerializationContext context) throws Exception {
        com.amalto.webapp.util.webservices.WSProcessTaskInstance instance = (com.amalto.webapp.util.webservices.WSProcessTaskInstance)obj;
        
        if (instance.getUuid() == null) {
            throw new SerializationException("literal.unexpectedNull");
        }
        ns3_myns3_string__java_lang_String_String_Serializer.serialize(instance.getUuid(), ns1_uuid_QNAME, null, writer, context);
        if (instance.getStatus() == null) {
            throw new SerializationException("literal.unexpectedNull");
        }
        ns3_myns3_string__java_lang_String_String_Serializer.serialize(instance.getStatus(), ns1_status_QNAME, null, writer, context);
        ns3_myns3_string__java_lang_String_String_Serializer.serialize(instance.getCandidates(), ns1_candidates_QNAME, null, writer, context);
        if (instance.getName() == null) {
            throw new SerializationException("literal.unexpectedNull");
        }
        ns3_myns3_string__java_lang_String_String_Serializer.serialize(instance.getName(), ns1_name_QNAME, null, writer, context);
        if (instance.getReadyDate() == null) {
            throw new SerializationException("literal.unexpectedNull");
        }
        ns3_myns3_string__java_lang_String_String_Serializer.serialize(instance.getReadyDate(), ns1_readyDate_QNAME, null, writer, context);
        if (instance.getProcessName() == null) {
            throw new SerializationException("literal.unexpectedNull");
        }
        ns3_myns3_string__java_lang_String_String_Serializer.serialize(instance.getProcessName(), ns1_processName_QNAME, null, writer, context);
        if (instance.getProcessVersion() == null) {
            throw new SerializationException("literal.unexpectedNull");
        }
        ns3_myns3_string__java_lang_String_String_Serializer.serialize(instance.getProcessVersion(), ns1_processVersion_QNAME, null, writer, context);
        if (instance.getProcessInstanceNb() == null) {
            throw new SerializationException("literal.unexpectedNull");
        }
        ns3_myns3_string__java_lang_String_String_Serializer.serialize(instance.getProcessInstanceNb(), ns1_processInstanceNb_QNAME, null, writer, context);
        if (instance.getProcessInstanceUUID() == null) {
            throw new SerializationException("literal.unexpectedNull");
        }
        ns3_myns3_string__java_lang_String_String_Serializer.serialize(instance.getProcessInstanceUUID(), ns1_processInstanceUUID_QNAME, null, writer, context);
        if (instance.getProcessDefineUUID() == null) {
            throw new SerializationException("literal.unexpectedNull");
        }
        ns3_myns3_string__java_lang_String_String_Serializer.serialize(instance.getProcessDefineUUID(), ns1_processDefineUUID_QNAME, null, writer, context);
    }
}
