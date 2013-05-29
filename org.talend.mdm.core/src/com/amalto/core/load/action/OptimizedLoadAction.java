/*
 * Copyright (C) 2006-2012 Talend Inc. - www.talend.com
 *
 * This source code is available under agreement available at
 * %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
 *
 * You should have received a copy of the agreement
 * along with this program; if not, write to Talend SA
 * 9 rue Pages 92150 Suresnes, France
 */

package com.amalto.core.load.action;

import com.amalto.core.load.LoadParser;
import com.amalto.core.load.context.StateContext;
import com.amalto.core.load.io.XMLRootInputStream;
import com.amalto.core.save.SaverSession;
import com.amalto.core.server.XmlServer;
import com.amalto.core.util.XSDKey;
import org.apache.log4j.Logger;
import org.talend.mdm.commmon.util.core.EUUIDCustomType;

import java.io.InputStream;

/**
 *
 */
public class OptimizedLoadAction implements LoadAction {
    private static final Logger log = Logger.getLogger(OptimizedLoadAction.class);
    private final String dataClusterName;
    private final String typeName;
    private final String dataModelName;
    private final boolean needAutoGenPK;
    private StateContext context;

    public OptimizedLoadAction(String dataClusterName, String typeName, String dataModelName, boolean needAutoGenPK) {
        this.dataClusterName = dataClusterName;
        this.typeName = typeName;
        this.dataModelName = dataModelName;
        this.needAutoGenPK = needAutoGenPK;
    }

    public boolean supportValidation() {
        return false;
    }

    public boolean supportAutoGenPK() {
        return true;
    }

    public void load(InputStream stream, XSDKey keyMetadata, XmlServer server, SaverSession session) throws Exception {
        if (!".".equals(keyMetadata.getSelector())) { //$NON-NLS-1$
            throw new UnsupportedOperationException("Selector '" + keyMetadata.getSelector() + "' isn't supported.");
        }

        if (needAutoGenPK) {
            String[] idFieldTypes = keyMetadata.getFieldTypes();
            for (String idFieldType : idFieldTypes) {
                if (!EUUIDCustomType.AUTO_INCREMENT.getName().equals(idFieldType) && !EUUIDCustomType.UUID.getName().equals(idFieldType)) {
                    throw new UnsupportedOperationException("No support for key field type '" + idFieldType + "' with autogen pk on.");
                }
            }
        }

        // Creates a load parser callback that loads data in server using a SAX handler
        ServerParserCallback callback = new ServerParserCallback(server, dataClusterName);

        java.io.InputStream inputStream = new XMLRootInputStream(stream, "root"); //$NON-NLS-1$
        LoadParser.Configuration configuration = new LoadParser.Configuration(typeName, keyMetadata.getFields(), needAutoGenPK, dataClusterName, dataModelName);
        context = LoadParser.parse(inputStream, configuration, callback);

        if (log.isDebugEnabled()) {
            log.debug("Number of documents loaded: " + callback.getCount()); //$NON-NLS-1$
        }
    }

    public void endLoad(XmlServer server) {
        if (context != null) {
            // This call should clean up everything (incl. save counter state in case of autogen pk).
            context.close(server);
        }
    }
}
