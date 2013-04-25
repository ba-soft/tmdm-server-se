// ============================================================================
//
// Copyright (C) 2006-2012 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================

/*
 * Created on 9 août 2005
 */
package com.amalto.xmlserver.interfaces;

import java.io.Serializable;

import org.w3c.dom.Document;

/**
 * @author Bruno Grieder
 * 
 * A condition found in an XQuery where clause
 * 
 */
public class WhereCondition implements IWhereItem, Serializable {

    public static final String FULLTEXTSEARCH = "FULLTEXTSEARCH";

    public static final String CONTAINS = "CONTAINS";

    public static String STRICTCONTAINS = "STRICTCONTAINS";

    public static String STARTSWITH = "STARTSWITH";

    public static String JOINS = "JOINS";

    public static String CONTAINS_TEXT_OF = "CONTAINS_TEXT_OF";

    public static String EMPTY_NULL = "Is Empty Or Null";

    public static final String EQUALS = "=";

    public static final String NOT_EQUALS = "!=";

    public static final String GREATER_THAN = ">";

    public static final String GREATER_THAN_OR_EQUAL = ">=";

    public static final String LOWER_THAN = "<";

    public static final String LOWER_THAN_OR_EQUAL = "<=";

    public static final String NO_OPERATOR = "NO OP";

    public static final String PRE_NONE = "&"; // default

    public static final String PRE_OR = "|";

    public static final String PRE_AND = "&";

    public static final String PRE_STRICTAND = "+";

    public static final String PRE_EXACTLY = "=";

    public static final String PRE_NOT = "!";

    String leftPath;

    String operator;

    String rightValueOrPath;

    String stringPredicate;

    boolean spellCheck;

    private boolean isRightValueXPath;

    /**
     * Constructor used by Castor to unmarshall the WhereCondition
     */
    public WhereCondition() {
    }

    /**
     * @param leftPath
     * @param operator
     * @param rightValueOrPath
     * @param stringPredicate
     */
    public WhereCondition(String leftPath, String operator, String rightValueOrPath, String stringPredicate) {
        this(leftPath, operator, rightValueOrPath, stringPredicate, true);
    }

    /**
     * @param leftPath
     * @param operator
     * @param rightValueOrPath
     * @param stringPredicate
     * @param spellCheck
     */
    public WhereCondition(String leftPath, String operator, String rightValueOrPath, String stringPredicate, boolean spellCheck) {
        super();
        this.leftPath = leftPath;
        this.operator = operator;
        setRightValueOrPath(rightValueOrPath);
        this.stringPredicate = stringPredicate;
        this.spellCheck = spellCheck;
    }

    public String getLeftPath() {
        return leftPath;
    }

    public void setLeftPath(String leftPath) {
        this.leftPath = leftPath;
    }

    public String getOperator() {
        return operator;
    }

    // Used by unmarshaller
    public void setOperator(String operator) {
        this.operator = operator;
    }

    public String getRightValueOrPath() {
        return rightValueOrPath;
    }

    public void setRightValueOrPath(String rightValueOrPath) {
        // Quoted values (either simple or double) are considered as literal values
        // TODO To be refactored to support true literal/Xpath differentiation
        if (rightValueOrPath != null) {
            if (rightValueOrPath.length() > 1 && ((rightValueOrPath.startsWith("\"") && rightValueOrPath.endsWith("\"")) //$NON-NLS-1$ //$NON-NLS-2$
                    || (rightValueOrPath.startsWith("'") && rightValueOrPath.endsWith("'")))) { //$NON-NLS-1$ //$NON-NLS-2$
                isRightValueXPath = false;
                rightValueOrPath = rightValueOrPath.substring(1, rightValueOrPath.length() - 1);
                // Escape any potential '\' character
                rightValueOrPath = rightValueOrPath.replaceAll("\\\\", "\\\\\\\\"); //$NON-NLS-1$ //$NON-NLS-2$
            } else if(rightValueOrPath.contains("/")) { //$NON-NLS-1$
                isRightValueXPath = true;
            }    
        }
        
        if (rightValueOrPath != null && !rightValueOrPath.startsWith("^") && (null != this.operator && this.operator.equals(WhereCondition.STARTSWITH))) { //$NON-NLS-1$
            this.rightValueOrPath = "^" + rightValueOrPath; //$NON-NLS-1$
        } else {
            this.rightValueOrPath = rightValueOrPath;
        }
    }

    public boolean isRightValueXPath() {
        return this.isRightValueXPath;
    }

    public String getStringPredicate() {
        return stringPredicate;
    }

    // Used by unmarshaller
    public void setStringPredicate(String stringPredicate) {
        this.stringPredicate = stringPredicate;
    }

    /**
     * @return Returns the spellCheck.
     */
    public boolean isSpellCheck() {
        return spellCheck;
    }

    private String xmlEncode(String unEncoded) {
        String encoded = unEncoded;
        encoded = encoded.replaceAll("&", "&amp;");
        encoded = encoded.replaceAll(">", "&gt;");
        encoded = encoded.replaceAll("<", "&lt;");
        return encoded;
    }

    public String serialize() {
        return "<wherecondition>" + "	<leftpath>" + xmlEncode(getLeftPath()) + "</leftpath>" + "	<operator>"
                + xmlEncode(getOperator()) + "</operator>" + "	<rightvalueorpath>" + xmlEncode(getRightValueOrPath())
                + "</rightvalueorpath>" + "	<stringpredicate>" + xmlEncode(getStringPredicate()) + "</stringpredicate>"
                + "	<spellcheck>" + (spellCheck ? "yes" : "no") + "</spellcheck>" + "</wherecondition>";
    }

    public static WhereCondition deserialize(String xml) throws XmlServerException {
        Document d = Util.parse(xml);
        return new WhereCondition(Util.getFirstTextNode(d.getDocumentElement(), "./leftpath"), Util.getFirstTextNode(d
                .getDocumentElement(), "./operator"), Util.getFirstTextNode(d.getDocumentElement(), "./rightvalueorpath"), Util
                .getFirstTextNode(d.getDocumentElement(), "./stringpredicate"), "yes".equals(Util.getFirstTextNode(d
                .getDocumentElement(), "./spellcheck")));
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    public String toString() {
        return "(" + getLeftPath() + " " + getOperator() + " " + getRightValueOrPath() + " " + getStringPredicate() + " "
                + isSpellCheck() + ")";
    }
}
