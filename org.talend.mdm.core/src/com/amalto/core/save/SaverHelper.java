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

package com.amalto.core.save;

import com.amalto.core.save.context.DocumentSaver;
import com.amalto.core.save.context.SaverContextFactory;
import com.amalto.core.webservice.WSPutItem;

import java.io.ByteArrayInputStream;
import java.io.UnsupportedEncodingException;

public class SaverHelper {
    public static DocumentSaver saveItem(WSPutItem wsPutItem,
                                         SaverSession session,
                                         String dataClusterName,
                                         String dataModelName) throws UnsupportedEncodingException {
        return saveItem(wsPutItem.getXmlString(), session, !wsPutItem.getIsUpdate(), dataClusterName, dataModelName);
    }

    public static DocumentSaver saveItem(String xmlString,
                                         SaverSession session,
                                         boolean isReplace,
                                         String dataClusterName,
                                         String dataModelName) throws UnsupportedEncodingException {
        SaverContextFactory contextFactory = session.getContextFactory();
        DocumentSaverContext context = contextFactory.create(dataClusterName,
                dataModelName,
                isReplace,
                new ByteArrayInputStream(xmlString.getBytes("UTF-8"))); //$NON-NLS-1$
        DocumentSaver saver = context.createSaver();
        saver.save(session, context);
        return saver;
    }

    public static DocumentSaver saveItemWithReport(WSPutItem wsPutItem,
                                                   SaverSession session,
                                                   String dataClusterName,
                                                   String dataModelName,
                                                   String changeSource,
                                                   boolean beforeSaving) throws UnsupportedEncodingException {
        return saveItemWithReport(wsPutItem.getXmlString(), session, !wsPutItem.getIsUpdate(), dataClusterName, dataModelName, changeSource, beforeSaving);
    }

    public static DocumentSaver saveItemWithReport(String xmlString,
                                                   SaverSession session,
                                                   boolean isReplace,
                                                   String dataClusterName,
                                                   String dataModelName,
                                                   String changeSource,
                                                   boolean beforeSaving) throws UnsupportedEncodingException {
        SaverContextFactory contextFactory = session.getContextFactory();
        DocumentSaverContext context = contextFactory.create(dataClusterName,
                dataModelName,
                changeSource,
                new ByteArrayInputStream(xmlString.getBytes("UTF-8")), //$NON-NLS-1$
                isReplace,
                true, // Always validate
                true, // Always generate an update report
                beforeSaving);
        DocumentSaver saver = context.createSaver();
        saver.save(session, context);
        return saver;
    }
}
