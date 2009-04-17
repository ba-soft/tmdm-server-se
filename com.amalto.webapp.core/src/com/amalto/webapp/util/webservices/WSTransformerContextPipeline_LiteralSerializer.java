// This class was generated by the JAXRPC SI, do not edit.
// Contents subject to change without notice.
// JAX-RPC Standard Implementation (1.1.2_01, construire R40)
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

public class WSTransformerContextPipeline_LiteralSerializer extends LiteralObjectSerializerBase implements Initializable  {
    private static final QName ns1_pipelineItem_QNAME = new QName("", "pipelineItem");
    private static final QName ns2_WSTransformerContext$2d$pipeline$2d$pipelineItem_TYPE_QNAME = new QName("urn-com-amalto-xtentis-webservice", "WSTransformerContext-pipeline-pipelineItem");
    private CombinedSerializer ns2_myWSTransformerContextPipelinePipelineItem_LiteralSerializer;
    
    public WSTransformerContextPipeline_LiteralSerializer(QName type, String encodingStyle) {
        this(type, encodingStyle, false);
    }
    
    public WSTransformerContextPipeline_LiteralSerializer(QName type, String encodingStyle, boolean encodeType) {
        super(type, true, encodingStyle, encodeType);
    }
    
    public void initialize(InternalTypeMappingRegistry registry) throws Exception {
        ns2_myWSTransformerContextPipelinePipelineItem_LiteralSerializer = (CombinedSerializer)registry.getSerializer("", com.amalto.webapp.util.webservices.WSTransformerContextPipelinePipelineItem.class, ns2_WSTransformerContext$2d$pipeline$2d$pipelineItem_TYPE_QNAME);
    }
    
    public Object doDeserialize(XMLReader reader,
        SOAPDeserializationContext context) throws Exception {
        com.amalto.webapp.util.webservices.WSTransformerContextPipeline instance = new com.amalto.webapp.util.webservices.WSTransformerContextPipeline();
        Object member=null;
        QName elementName;
        List values;
        Object value;
        
        reader.nextElementContent();
        elementName = reader.getName();
        if ((reader.getState() == XMLReader.START) && (elementName.equals(ns1_pipelineItem_QNAME))) {
            values = new ArrayList();
            for(;;) {
                elementName = reader.getName();
                if ((reader.getState() == XMLReader.START) && (elementName.equals(ns1_pipelineItem_QNAME))) {
                    value = ns2_myWSTransformerContextPipelinePipelineItem_LiteralSerializer.deserialize(ns1_pipelineItem_QNAME, reader, context);
                    if (value == null) {
                        throw new DeserializationException("literal.unexpectedNull");
                    }
                    values.add(value);
                    reader.nextElementContent();
                } else {
                    break;
                }
            }
            member = new com.amalto.webapp.util.webservices.WSTransformerContextPipelinePipelineItem[values.size()];
            member = values.toArray((Object[]) member);
            instance.setPipelineItem((com.amalto.webapp.util.webservices.WSTransformerContextPipelinePipelineItem[])member);
        }
        else {
            instance.setPipelineItem(new com.amalto.webapp.util.webservices.WSTransformerContextPipelinePipelineItem[0]);
        }
        
        XMLReaderUtil.verifyReaderState(reader, XMLReader.END);
        return (Object)instance;
    }
    
    public void doSerializeAttributes(Object obj, XMLWriter writer, SOAPSerializationContext context) throws Exception {
        com.amalto.webapp.util.webservices.WSTransformerContextPipeline instance = (com.amalto.webapp.util.webservices.WSTransformerContextPipeline)obj;
        
    }
    public void doSerialize(Object obj, XMLWriter writer, SOAPSerializationContext context) throws Exception {
        com.amalto.webapp.util.webservices.WSTransformerContextPipeline instance = (com.amalto.webapp.util.webservices.WSTransformerContextPipeline)obj;
        
        if (instance.getPipelineItem() != null) {
            for (int i = 0; i < instance.getPipelineItem().length; ++i) {
                ns2_myWSTransformerContextPipelinePipelineItem_LiteralSerializer.serialize(instance.getPipelineItem()[i], ns1_pipelineItem_QNAME, null, writer, context);
            }
        }
    }
}
