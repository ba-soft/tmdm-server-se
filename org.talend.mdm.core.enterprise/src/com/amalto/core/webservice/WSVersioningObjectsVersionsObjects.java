// This class was generated by the JAXRPC SI, do not edit.
// Contents subject to change without notice.
// JAX-RPC Standard Implementation ��1.1.2_01������� R40��
// Generated source version: 1.1.2

package com.amalto.core.webservice;


public class WSVersioningObjectsVersionsObjects {
    protected java.lang.String type;
    protected java.lang.String name;
    protected com.amalto.core.webservice.WSVersioningHistoryEntry[] wsVersionEntries;
    
    public WSVersioningObjectsVersionsObjects() {
    }
    
    public WSVersioningObjectsVersionsObjects(java.lang.String type, java.lang.String name, com.amalto.core.webservice.WSVersioningHistoryEntry[] wsVersionEntries) {
        this.type = type;
        this.name = name;
        this.wsVersionEntries = wsVersionEntries;
    }
    
    public java.lang.String getType() {
        return type;
    }
    
    public void setType(java.lang.String type) {
        this.type = type;
    }
    
    public java.lang.String getName() {
        return name;
    }
    
    public void setName(java.lang.String name) {
        this.name = name;
    }
    
    public com.amalto.core.webservice.WSVersioningHistoryEntry[] getWsVersionEntries() {
        return wsVersionEntries;
    }
    
    public void setWsVersionEntries(com.amalto.core.webservice.WSVersioningHistoryEntry[] wsVersionEntries) {
        this.wsVersionEntries = wsVersionEntries;
    }
}
