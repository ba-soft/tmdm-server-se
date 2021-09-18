/*
 * Copyright (C) 2006-2019 Talend Inc. - www.talend.com
 *
 * This source code is available under agreement available at
 * %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
 *
 * You should have received a copy of the agreement along with this program; if not, write to Talend SA 9 rue Pages
 * 92150 Suresnes, France
 */
package com.amalto.core.servlet;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.Response;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.talend.mdm.commmon.metadata.ComplexTypeMetadata;
import org.talend.mdm.commmon.metadata.ContainedTypeFieldMetadata;
import org.talend.mdm.commmon.metadata.FieldMetadata;
import org.talend.mdm.commmon.metadata.MetadataRepository;
import org.talend.mdm.commmon.metadata.SimpleTypeFieldMetadata;
import org.talend.mdm.commmon.util.core.EUUIDCustomType;
import org.talend.mdm.commmon.util.core.MDMConfiguration;
import org.talend.mdm.commmon.util.webapp.XSystemObjects;

import com.amalto.core.load.action.DefaultLoadAction;
import com.amalto.core.load.action.LoadAction;
import com.amalto.core.load.action.OptimizedLoadAction;
import com.amalto.core.objects.datacluster.DataClusterPOJO;
import com.amalto.core.objects.datacluster.DataClusterPOJOPK;
import com.amalto.core.save.DocumentSaverContext;
import com.amalto.core.save.SaverSession;
import com.amalto.core.save.context.DocumentSaver;
import com.amalto.core.save.context.SaverContextFactory;
import com.amalto.core.server.MetadataRepositoryAdmin;
import com.amalto.core.server.ServerContext;
import com.amalto.core.server.StorageAdmin;
import com.amalto.core.server.api.DataCluster;
import com.amalto.core.server.api.XmlServer;
import com.amalto.core.storage.record.DataRecord;
import com.amalto.core.util.LocalUser;
import com.amalto.core.util.Util;
import com.amalto.core.util.XSDKey;
import com.amalto.core.util.XtentisException;

public class LoadServlet extends HttpServlet {

    private static final Logger LOG = LogManager.getLogger(LoadServlet.class);

    private static final long serialVersionUID = 1L;

    public static final Map<String, XSDKey> typeNameToKeyDef = new HashMap<>();

    private static final String PARAMETER_CLUSTER = "cluster"; //$NON-NLS-1$

    private static final String PARAMETER_CONCEPT = "concept"; //$NON-NLS-1$

    private static final String PARAMETER_DATAMODEL = "datamodel"; //$NON-NLS-1$

    private static final String PARAMETER_VALIDATE = "validate"; //$NON-NLS-1$

    private static final String PARAMETER_SMARTPK = "smartpk"; //$NON-NLS-1$

    private static final String PARAMETER_SMARTFIELDS = "smartfields"; //$NON-NLS-1$

    private static final String PARAMETER_INSERTONLY = "insertonly"; //$NON-NLS-1$

    private static final String PARAMETER_UPDATEREPORT = "updateReport"; //$NON-NLS-1$

    private static final String PARAMETER_SOURCE = "source"; //$NON-NLS-1$

    private static final Map<String, AtomicInteger> DB_REQUESTS_MAP = new HashMap<>();

    private static final Integer MAX_DB_REQUESTS;

    private static final Long WAIT_MILLISECONDS;

    static {
        MAX_DB_REQUESTS = Integer.valueOf(MDMConfiguration.getConfiguration().getProperty("bulkload.concurrent.database.requests", "25")); //$NON-NLS-1$ //$NON-NLS-2$
        WAIT_MILLISECONDS = Long.valueOf(MDMConfiguration.getConfiguration().getProperty("bulkload.concurrent.wait.milliseconds", "200")); //$NON-NLS-1$ //$NON-NLS-2$
    }

    public LoadServlet() {
        super();
    }

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
    }

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8"); //$NON-NLS-1$
        response.setContentType("text/html; charset=UTF-8"); //$NON-NLS-1$
        response.setCharacterEncoding("UTF-8"); //$NON-NLS-1$
        // configure writer depending on logger configuration.
        PrintWriter writer = configureWriter(response);
        writer.write("<html><body>"); //$NON-NLS-1$
        writer.write("<p><b>Load data into MDM</b><br/>Check server log output to determine when load is completed</b></p>"); //$NON-NLS-1$
        String dataClusterName = request.getParameter(PARAMETER_CLUSTER);
        String typeName = request.getParameter(PARAMETER_CONCEPT);
        String dataModelName = request.getParameter(PARAMETER_DATAMODEL);
        boolean needValidate = Boolean.parseBoolean(request.getParameter(PARAMETER_VALIDATE));
        boolean needAutoGenPK = Boolean.parseBoolean(request.getParameter(PARAMETER_SMARTPK));
        boolean insertOnly = Boolean.parseBoolean(request.getParameter(PARAMETER_INSERTONLY));

        try {
            if (!LocalUser.getLocalUser().userCanRead(DataClusterPOJO.class, dataClusterName)) {
                response.setStatus(Response.Status.FORBIDDEN.getStatusCode());
                throw new ServletException("User doesn't have 'read' access for container '" + dataClusterName + "'."); //$NON-NLS-1$ //$NON-NLS-2$
            }
        } catch (XtentisException e) {
            String message = "Unable to check 'read' access for container '" + dataClusterName + "'."; //$NON-NLS-1$ //$NON-NLS-2$
            LOG.warn(message);
            throw new ServletException(message);
        }

        boolean updateReport = Boolean.parseBoolean(request.getParameter(PARAMETER_UPDATEREPORT));
        String source = request.getParameter(PARAMETER_SOURCE);
        ServletInputStream inputStream = request.getInputStream();

        LoadAction loadAction = getLoadAction(dataClusterName, typeName, dataModelName, needValidate, needAutoGenPK, updateReport,
                source);
        if (needValidate && !loadAction.supportValidation()) {
            throw new ServletException(new UnsupportedOperationException("XML Validation isn't supported")); //$NON-NLS-1$
        }
        // Get xml server and key information
        MetadataRepositoryAdmin repositoryAdmin = ServerContext.INSTANCE.get().getMetadataRepositoryAdmin();
        MetadataRepository repository = repositoryAdmin.get(dataModelName);
        ComplexTypeMetadata type = repository.getComplexType(typeName);
        XSDKey keyMetadata = getTypeKey(type.getKeyFields());
        Map<String, String> autoFieldTypeMap = getAutoFieldTypeMap(type.getFields());

        DataRecord.CheckExistence.set(!insertOnly);
        bulkLoadSave(dataClusterName, dataModelName, inputStream, loadAction, keyMetadata, autoFieldTypeMap);
        writer.write("</body></html>"); //$NON-NLS-1$
    }

    private void bulkLoadSave(String dataClusterName, String dataModelName, InputStream inputStream, LoadAction loadAction,
            XSDKey keyMetadata, Map<String, String> autoFieldTypeMap) throws ServletException {
        XmlServer server = Util.getXmlServerCtrlLocal();

        SaverSession session = SaverSession.newSession();
        SaverContextFactory contextFactory = session.getContextFactory();
        DocumentSaverContext context = contextFactory
                .createBulkLoad(dataClusterName, dataModelName, keyMetadata, autoFieldTypeMap, inputStream, loadAction, server);
        DocumentSaver saver = context.createSaver();

        // Wait until less that MAX_THREADS running
        synchronized (LoadServlet.class) {
            AtomicInteger dbRequests = getDbRequests(dataClusterName);
            try {
                while (dbRequests.get() >= MAX_DB_REQUESTS) {
                    if (LOG.isDebugEnabled()) {
                        LOG.debug("Up to " + dbRequests + " db requests, wait for " + WAIT_MILLISECONDS + " ms.");
                    }
                    Thread.sleep(WAIT_MILLISECONDS);
                }
                int newDbRequests = increaseDbRequests(dataClusterName);
                if (LOG.isDebugEnabled()) {
                    LOG.debug("Add 1 db request, currently " + newDbRequests + " requests left.");
                }
            } catch (InterruptedException e) {
                LOG.error("Waiting to start db request meets exception.", e);
            }
        }

        try {
            session.begin(dataClusterName);
            saver.save(session, context);
            session.end();
            // End the load (might persist counter state in case of autogen pk).
            loadAction.endLoad(server);
        } catch (Exception e) {
            try {
                session.abort();
            } catch (Exception rollbackException) {
                LOG.error("Ignoring rollback exception", rollbackException); //$NON-NLS-1$
            }
            throw new ServletException(e);
        } finally {
            DataRecord.CheckExistence.remove();
            // Decrease total threads
            int newDbRequests = decreaseDbRequests(dataClusterName);
            if (LOG.isDebugEnabled()) {
                LOG.debug("Finish 1 db request, currently " + newDbRequests + " requests left.");
            }
        }
    }

    protected int increaseDbRequests(String dataClusterName) {
        return DB_REQUESTS_MAP.get(dataClusterName).incrementAndGet();
    }

    protected int decreaseDbRequests(String dataClusterName) {
        return DB_REQUESTS_MAP.get(dataClusterName).decrementAndGet();
    }

    protected AtomicInteger getDbRequests(String dataClusterName) {
        AtomicInteger value = DB_REQUESTS_MAP.get(dataClusterName);
        if (value == null) {
            value = new AtomicInteger(0);
            DB_REQUESTS_MAP.put(dataClusterName, value);
        }
        return value;
    }

    protected LoadAction getLoadAction(String dataClusterName, String typeName, String dataModelName, boolean needValidate,
            boolean needAutoGenPK, boolean updateReport, String source) {
        // Test if the data cluster actually exists
        DataClusterPOJO dataCluster = getDataCluster(dataClusterName);
        if (dataCluster == null) {
            throw new IllegalArgumentException("Data cluster '" + dataClusterName + "' does not exist."); //$NON-NLS-1$ //$NON-NLS-2$
        }
        if (dataClusterName.endsWith(StorageAdmin.STAGING_SUFFIX)) {
            updateReport = false;
        }
        LoadAction loadAction;
        if (needValidate || updateReport || XSystemObjects.DC_PROVISIONING.getName().equals(dataClusterName)) {
            loadAction = new DefaultLoadAction(dataClusterName, dataModelName, needValidate, updateReport, source);
        } else {
            loadAction = new OptimizedLoadAction(dataClusterName, typeName, dataModelName, needAutoGenPK);
        }
        if (LOG.isDebugEnabled()) {
            LOG.debug("Load action selected for load: " + loadAction.getClass().getName() //$NON-NLS-1$
                    + " / needValidate:" + needValidate + ")"); //$NON-NLS-1$ //$NON-NLS-2$
        }
        return loadAction;
    }

    protected DataClusterPOJO getDataCluster(String dataClusterName) {
        DataClusterPOJO dataCluster;
        try {
            DataCluster dataClusterCtrlLocal = Util.getDataClusterCtrlLocal();
            DataClusterPOJOPK dataClusterPOJOPK = new DataClusterPOJOPK(dataClusterName);
            dataCluster = dataClusterCtrlLocal.existsDataCluster(dataClusterPOJOPK);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return dataCluster;
    }

    private XSDKey getTypeKey(Collection<FieldMetadata> fieldList) {
        String[] fields = new String[fieldList.size()];
        String[] fieldTypes = new String[fieldList.size()];
        int i = 0;
        for (FieldMetadata keyField : fieldList) {
            fields[i] = keyField.getPath();
            String name = keyField.getType().getName();
            // See TMDM-6687
            if (EUUIDCustomType.AUTO_INCREMENT.getName().equals(name) || EUUIDCustomType.UUID.getName().equals(name)) {
                fieldTypes[i] = name;
            } else {
                fieldTypes[i] = "xsd:" + name; //$NON-NLS-1$
            }
            i++;
        }
        return new XSDKey(".", fields, fieldTypes); //$NON-NLS-1$
    }

    /**
     * Filter the AUTO_INCREMENT/UUID type field from the entity's all list.
     * if contains the complex type, it also filter.
     * @param fieldList all field list
     * @return all AUTO_INCREMENT/UUID field type, include this type field existed in the complex type
     */
    private Map<String, String> getAutoFieldTypeMap(Collection<FieldMetadata> fieldList) {
        Map<String, String> autoFieldTypeMap = new HashMap<>();
        for (FieldMetadata field : fieldList) {
            if (field.isKey()) {
                continue;
            }
            List<FieldMetadata> fieldMetadataList = new ArrayList<>();
            getFieldIntactName(field, fieldMetadataList);
            for (FieldMetadata subField : fieldMetadataList) {
                autoFieldTypeMap.put(subField.getPath(), subField.getType().getName());
            }
        } return autoFieldTypeMap;
    }

    private void getFieldIntactName(FieldMetadata field, List<FieldMetadata> fieldList) {
        if (field instanceof SimpleTypeFieldMetadata) {
            String name = field.getType().getName();
            if (EUUIDCustomType.AUTO_INCREMENT.getName().equals(name) || EUUIDCustomType.UUID.getName().equals(name)) {
                fieldList.add(field);
            }
        } else if (field instanceof ContainedTypeFieldMetadata) {
            ContainedTypeFieldMetadata containedTypeFieldMetadata = (ContainedTypeFieldMetadata) field;
            for (FieldMetadata subField : containedTypeFieldMetadata.getContainedType().getFields()) {
                getFieldIntactName(subField, fieldList);
            }
        }
    }

    /**
     * Returns a writer that does not print anything if logger hasn't DEBUG level.
     *
     * @param resp A servlet response output.
     * @return The same {@link HttpServletResponse} if debug <b>is</b> enabled, or a no-op one if debug is disabled.
     * @throws IOException Thrown by {@link javax.servlet.http.HttpServletResponse#getWriter()}.
     */
    private static PrintWriter configureWriter(HttpServletResponse resp) throws IOException {
        PrintWriter writer = resp.getWriter();
        if (LOG.isDebugEnabled()) {
            return writer;
        } else {
            return new NoOpPrintWriter(writer);
        }
    }

    /**
     * A {@link PrintWriter} implementation that intercepts all write method calls.
     *
     * @see LoadServlet#configureWriter(javax.servlet.http.HttpServletResponse)
     */
    private static class NoOpPrintWriter extends PrintWriter {

        private NoOpPrintWriter(PrintWriter writer) {
            super(writer);
        }

        @Override
        public void write(int c) {
            // Nothing to do (debug isn't enabled)
        }

        @Override
        public void write(char[] buf, int off, int len) {
            // Nothing to do (debug isn't enabled)
        }

        @Override
        public void write(char[] buf) {
            // Nothing to do (debug isn't enabled)
        }

        @Override
        public void write(String s, int off, int len) {
            // Nothing to do (debug isn't enabled)
        }

        @Override
        public void write(String s) {
            // Nothing to do (debug isn't enabled)
        }
    }
}