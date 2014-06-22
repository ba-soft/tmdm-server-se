// This class was generated by the JAXRPC SI, do not edit.
// Contents subject to change without notice.
// JAX-RPC Standard Implementation 
// Generated source version: 1.1.2

package com.amalto.webapp.util.webservices;


import java.util.Map;
import java.util.HashMap;

public class WSGetUniverseByRevisionType {
    private java.lang.String value;
    private static Map valueMap = new HashMap();
    public static final String _ITEMString = "ITEM";
    public static final String _OBJECTString = "OBJECT";
    
    public static final java.lang.String _ITEM = new java.lang.String(_ITEMString);
    public static final java.lang.String _OBJECT = new java.lang.String(_OBJECTString);
    
    public static final WSGetUniverseByRevisionType ITEM = new WSGetUniverseByRevisionType(_ITEM);
    public static final WSGetUniverseByRevisionType OBJECT = new WSGetUniverseByRevisionType(_OBJECT);
    
    protected WSGetUniverseByRevisionType(java.lang.String value) {
        this.value = value;
        valueMap.put(this.toString(), this);
    }
    
    public java.lang.String getValue() {
        return value;
    }
    
    public static WSGetUniverseByRevisionType fromValue(java.lang.String value)
        throws java.lang.IllegalStateException {
        if (ITEM.value.equals(value)) {
            return ITEM;
        } else if (OBJECT.value.equals(value)) {
            return OBJECT;
        }
        throw new IllegalArgumentException();
    }
    
    public static WSGetUniverseByRevisionType fromString(String value)
        throws java.lang.IllegalStateException {
        WSGetUniverseByRevisionType ret = (WSGetUniverseByRevisionType)valueMap.get(value);
        if (ret != null) {
            return ret;
        }
        if (value.equals(_ITEMString)) {
            return ITEM;
        } else if (value.equals(_OBJECTString)) {
            return OBJECT;
        }
        throw new IllegalArgumentException();
    }
    
    public String toString() {
        return value.toString();
    }
    
    private Object readResolve()
        throws java.io.ObjectStreamException {
        return fromValue(getValue());
    }
    
    public boolean equals(Object obj) {
        if (!(obj instanceof WSGetUniverseByRevisionType)) {
            return false;
        }
        return ((WSGetUniverseByRevisionType)obj).value.equals(value);
    }
    
    public int hashCode() {
        return value.hashCode();
    }
}
