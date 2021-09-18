/*
 * Copyright (C) 2006-2019 Talend Inc. - www.talend.com
 *
 * This source code is available under agreement available at
 * %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
 *
 * You should have received a copy of the agreement along with this program; if not, write to Talend SA 9 rue Pages
 * 92150 Suresnes, France
 */

package com.amalto.core.load.action;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Map;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.talend.mdm.commmon.util.webapp.XSystemObjects;

import com.amalto.core.load.io.XMLStreamTokenizer;
import com.amalto.core.save.DocumentSaverContext;
import com.amalto.core.save.SaverSession;
import com.amalto.core.save.context.SaverContextFactory;
import com.amalto.core.server.api.XmlServer;
import com.amalto.core.util.XSDKey;

/**
 *
 */
public class DefaultLoadAction implements LoadAction {

    private static final Logger LOGGER = LogManager.getLogger(DefaultLoadAction.class);

    private final String dataClusterName;

    private final String dataModelName;

    private final boolean needValidate;

    private final boolean updateReport;

    private final String source;

    public DefaultLoadAction(String dataClusterName, String dataModelName, boolean needValidate, boolean updateReport,
            String source) {
        this.dataClusterName = dataClusterName;
        this.dataModelName = dataModelName;
        this.needValidate = needValidate;
        this.updateReport = updateReport;
        this.source = source;
    }

    @Override
    public boolean supportValidation() {
        return true;
    }

    @Override
    public void load(InputStream stream, XSDKey keyMetadata, Map<String, String> autoFieldTypeMap, XmlServer server,
            SaverSession session) {
        try {
            SaverContextFactory contextFactory = session.getContextFactory();
            // If you wish to debug content sent to server evaluate 'IOUtils.toString(request.getInputStream())'
            XMLStreamTokenizer xmlStreamTokenizer = new XMLStreamTokenizer(stream, "UTF-8"); //$NON-NLS-1$
            while (xmlStreamTokenizer.hasMoreElements()) {
                String xmlData = xmlStreamTokenizer.nextElement();
                if (xmlData != null && xmlData.trim().length() > 0) {
                    // Note: in case you wish to change the "replace" behavior, also check
                    // com.amalto.core.save.context.BulkLoadContext.isReplace()
                    DocumentSaverContext context = contextFactory.create(dataClusterName, dataModelName, source,
                            new ByteArrayInputStream(xmlData.getBytes(StandardCharsets.UTF_8)),
                            true, // Always replace in this case (bulk load).
                            needValidate, updateReport, false, XSystemObjects.DC_PROVISIONING.getName().equals(dataClusterName)); // Enforce
                                                                                                                           // auto
                                                                                                                           // commit
                                                                                                                           // for
                                                                                                                           // users
                                                                                                                           // (for
                                                                                                                           // license
                                                                                                                           // checks).
                    context.createSaver().save(session, context);
                }
            }
        } catch (Exception e) {
            try {
                session.abort();
            } catch (Exception rollbackException) {
                LOGGER.error("Exception occurred during transaction rollback.", rollbackException);
            }
            throw new RuntimeException(e);
        }
    }

    @Override
    public void endLoad(XmlServer server) {
        // Nothing to do
    }
}
