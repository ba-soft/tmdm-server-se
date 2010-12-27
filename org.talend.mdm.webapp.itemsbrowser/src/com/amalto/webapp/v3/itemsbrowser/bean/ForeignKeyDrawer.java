// ============================================================================
//
// Copyright (C) 2006-2010 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package com.amalto.webapp.v3.itemsbrowser.bean;

import java.io.Serializable;


/**
 * DOC HSHU  class global comment. Detailled comment
 */
public class ForeignKeyDrawer implements Serializable{
    
    private String xpathForeignKey;
    
    private String xpathInfoForeignKey;
    
    private String fkFilter;

    public String getXpathForeignKey() {
        return xpathForeignKey;
    }

    
    public void setXpathForeignKey(String xpathForeignKey) {
        this.xpathForeignKey = xpathForeignKey;
    }

    
    public String getXpathInfoForeignKey() {
        return xpathInfoForeignKey;
    }

    
    public void setXpathInfoForeignKey(String xpathInfoForeignKey) {
        this.xpathInfoForeignKey = xpathInfoForeignKey;
    }

    
    public String getFkFilter() {
        return fkFilter;
    }

    
    public void setFkFilter(String fkFilter) {
        this.fkFilter = fkFilter;
    }

}
