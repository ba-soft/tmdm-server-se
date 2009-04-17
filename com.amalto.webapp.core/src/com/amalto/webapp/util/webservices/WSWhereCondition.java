// This class was generated by the JAXRPC SI, do not edit.
// Contents subject to change without notice.
// JAX-RPC Standard Implementation (1.1.2_01, construire R40)
// Generated source version: 1.1.2

package com.amalto.webapp.util.webservices;


public class WSWhereCondition {
    protected java.lang.String leftPath;
    protected com.amalto.webapp.util.webservices.WSWhereOperator operator;
    protected java.lang.String rightValueOrPath;
    protected com.amalto.webapp.util.webservices.WSStringPredicate stringPredicate;
    protected boolean spellCheck;
    
    public WSWhereCondition() {
    }
    
    public WSWhereCondition(java.lang.String leftPath, com.amalto.webapp.util.webservices.WSWhereOperator operator, java.lang.String rightValueOrPath, com.amalto.webapp.util.webservices.WSStringPredicate stringPredicate, boolean spellCheck) {
        this.leftPath = leftPath;
        this.operator = operator;
        this.rightValueOrPath = rightValueOrPath;
        this.stringPredicate = stringPredicate;
        this.spellCheck = spellCheck;
    }
    
    public java.lang.String getLeftPath() {
        return leftPath;
    }
    
    public void setLeftPath(java.lang.String leftPath) {
        this.leftPath = leftPath;
    }
    
    public com.amalto.webapp.util.webservices.WSWhereOperator getOperator() {
        return operator;
    }
    
    public void setOperator(com.amalto.webapp.util.webservices.WSWhereOperator operator) {
        this.operator = operator;
    }
    
    public java.lang.String getRightValueOrPath() {
        return rightValueOrPath;
    }
    
    public void setRightValueOrPath(java.lang.String rightValueOrPath) {
        this.rightValueOrPath = rightValueOrPath;
    }
    
    public com.amalto.webapp.util.webservices.WSStringPredicate getStringPredicate() {
        return stringPredicate;
    }
    
    public void setStringPredicate(com.amalto.webapp.util.webservices.WSStringPredicate stringPredicate) {
        this.stringPredicate = stringPredicate;
    }
    
    public boolean isSpellCheck() {
        return spellCheck;
    }
    
    public void setSpellCheck(boolean spellCheck) {
        this.spellCheck = spellCheck;
    }
}
