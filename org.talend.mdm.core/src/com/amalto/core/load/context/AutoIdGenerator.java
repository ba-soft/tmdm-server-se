/*
 * Copyright (C) 2006-2016 Talend Inc. - www.talend.com
 *
 * This source code is available under agreement available at
 * %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
 *
 * You should have received a copy of the agreement
 * along with this program; if not, write to Talend SA
 * 9 rue Pages 92150 Suresnes, France
 */

package com.amalto.core.load.context;

import com.amalto.core.server.XmlServer;

/**
 *
 */
public interface AutoIdGenerator {
    /**
     * <p>
     * Generate an automatic id for the <code>conceptName</code> (a.k.a. type name) in the <code>dataCluster</code>
     * identified by <code>dataClusterName</code>.
     * </p>
     * <p>
     * Implementations of this interface may not use the parameters to generate ids.
     * </p>
     *
     * @param dataClusterName A data cluster name.
     * @param conceptName     A concept name (type name).
     * @param keyElementName  The key element name that value must be generated by this generator.
     * @return A automatically generated id valid for the <code>conceptName</code> in <code>dataClusterName</code>
     */
    String generateId(String dataClusterName, String conceptName, String keyElementName);

    /**
     * Tells the auto id generator to save its state (usually the last id that has been generated).
     */
    void saveState(XmlServer server);
}
