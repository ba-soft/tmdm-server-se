/*
 * Copyright (C) 2006-2019 Talend Inc. - www.talend.com
 *
 * This source code is available under agreement available at
 * %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
 *
 * You should have received a copy of the agreement along with this program; if not, write to Talend SA 9 rue Pages
 * 92150 Suresnes, France
 */

package com.amalto.core.storage.services;

import static com.amalto.core.query.user.UserQueryBuilder.eq;
import static com.amalto.core.query.user.UserQueryBuilder.from;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.talend.mdm.commmon.metadata.ComplexTypeMetadata;
import org.talend.mdm.commmon.metadata.MetadataRepository;
import org.talend.mdm.commmon.metadata.compare.Change;
import org.talend.mdm.commmon.metadata.compare.Compare;
import org.talend.mdm.commmon.metadata.compare.ImpactAnalyzer;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.amalto.commons.core.datamodel.synchronization.DMUpdateEvent;
import com.amalto.commons.core.datamodel.synchronization.DataModelChangeNotifier;
import com.amalto.core.audit.MDMAuditLogger;
import com.amalto.core.objects.datamodel.DataModelPOJO;
import com.amalto.core.objects.datamodel.DataModelPOJOPK;
import com.amalto.core.objects.marshalling.MarshallingFactory;
import com.amalto.core.query.user.UserQueryBuilder;
import com.amalto.core.save.SaverSession;
import com.amalto.core.server.MetadataRepositoryAdmin;
import com.amalto.core.server.ServerContext;
import com.amalto.core.server.StorageAdmin;
import com.amalto.core.storage.Storage;
import com.amalto.core.storage.StorageResults;
import com.amalto.core.storage.StorageType;
import com.amalto.core.storage.record.DataRecord;
import com.amalto.core.storage.record.DataRecordReader;
import com.amalto.core.storage.record.XmlDOMDataRecordReader;
import com.amalto.core.util.LocalUser;
import com.amalto.core.util.LocaleUtil;
import com.amalto.core.util.Util;
import com.amalto.core.util.XtentisException;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;


@Path("/system/models")
@Api(value="Models management", tags="Administration")
public class SystemModels {

    private static final Logger LOGGER = LogManager.getLogger(SystemModels.class);

    private final Storage systemStorage;

    private final ComplexTypeMetadata dataModelType;

    public SystemModels() {
        StorageAdmin storageAdmin = ServerContext.INSTANCE.get().getStorageAdmin();
        systemStorage = storageAdmin.get(StorageAdmin.SYSTEM_STORAGE, StorageType.SYSTEM);
        MetadataRepository repository = systemStorage.getMetadataRepository();
        dataModelType = repository.getComplexType("data-model-pOJO"); //$NON-NLS-1$
    }

    private static void reloadDataModel(String modelName) {
        // Force update in metadata repository admin
        MetadataRepositoryAdmin metadataRepositoryAdmin = ServerContext.INSTANCE.get().getMetadataRepositoryAdmin();
        metadataRepositoryAdmin.update(modelName);
        // Invalidate concept session
        SaverSession session = SaverSession.newSession();
        session.getSaverSource().invalidateTypeCache(modelName);
        session.end();
    }

    @GET
    @Path("/entities/{entityName}")
    @ApiOperation("Get the details of an entity according its identifier.")
    public Response getEntityDetails(@ApiParam("Entity name") @PathParam("entityName") String entityName) {
        try {
            MetadataRepositoryAdmin metadataRepositoryAdmin = ServerContext.INSTANCE.get().getMetadataRepositoryAdmin();
            String dataModelId = ServiceUtil.getDataModelNameByEntityName(metadataRepositoryAdmin,
                    ServiceUtil.getDataModelNames(), entityName);
            if (StringUtils.isNotEmpty(dataModelId)) {
                Map<String, String> map = new HashMap<>();
                map.put("entity", entityName); //$NON-NLS-1$
                map.put("dataModelId", dataModelId); //$NON-NLS-1$
                return Response.ok(map).type(MediaType.APPLICATION_JSON_TYPE).build();
            } else {
                return ServiceUtil.getErrorResponse(new NotFoundException(), StringUtils.EMPTY);
            }
        } catch (Exception e) {
            return ServiceUtil.getErrorResponse(e, "Could not get entity details."); //$NON-NLS-1$
        }

    }

    @GET
    @Path("{model}")
    @ApiOperation("Returns the requested data model XML schema")
    public Response getSchema(@ApiParam("The model name") @PathParam("model") String modelName) {
        if (!isSystemStorageAvailable()) {
            return ServiceUtil.getErrorResponse(new Exception(), "System storage is not available."); //$NON-NLS-1$
        }
        UserQueryBuilder qb = from(dataModelType).where(eq(dataModelType.getField("name"), modelName)); //$NON-NLS-1$
        systemStorage.begin();
        try {
            StorageResults results = systemStorage.fetch(qb.getSelect());
            Iterator<DataRecord> iterator = results.iterator();
            String modelContent = StringUtils.EMPTY;
            if (iterator.hasNext()) {
                DataRecord model = iterator.next();
                if (iterator.hasNext()) {
                    return ServiceUtil.getErrorResponse(new IllegalStateException(),
                            "Found multiple data models for '" + modelName + "'."); //$NON-NLS-1$ //$NON-NLS-2$
                }
                modelContent = String.valueOf(model.get("schema")); //$NON-NLS-1$
            }
            systemStorage.commit();
            if (StringUtils.isEmpty(modelContent)) {
                return ServiceUtil.getErrorResponse(new NotFoundException(), StringUtils.EMPTY);
            }
            return Response.ok(modelContent).build();
        } catch (Exception e) {
            systemStorage.rollback();
            return ServiceUtil.getErrorResponse(e, "Could not get data model."); //$NON-NLS-1$
        }
    }

    @POST
    @Path("/")
    @ApiOperation("Create a new data model given its name and XSD provided as request content")
    @Consumes({ MediaType.APPLICATION_XML, MediaType.TEXT_XML })
    public Response createDataModel(@ApiParam("New model name") @QueryParam("name") String modelName, InputStream dataModel) {
        String user = null;
        try {
            user = LocalUser.getLocalUser().getUsername();
            DataModelPOJO oldDataModel = DataModelPOJO.load(DataModelPOJO.class, new DataModelPOJOPK(modelName));
            DataModelPOJO dataModelPOJO = new DataModelPOJO(modelName);
            dataModelPOJO.setSchema(IOUtils.toString(dataModel, "UTF-8")); //$NON-NLS-1$
            dataModelPOJO.store();
            // synchronize with outer agents
            DataModelChangeNotifier dmUpdateEventNotifier = DataModelChangeNotifier.createInstance();
            dmUpdateEventNotifier.notifyChange(new DMUpdateEvent(modelName));
            if (oldDataModel == null) {
                MDMAuditLogger.dataModelCreated(user, dataModelPOJO);
            } else {
                MDMAuditLogger.dataModelModified(user, oldDataModel, dataModelPOJO);
            }
            return Response.ok().build();
        } catch (Exception e) {
            String errorMsg = "An error occurred while creating Data Model."; //$NON-NLS-1$
            RuntimeException ex = new RuntimeException(errorMsg, e); // $NON-NLS-1$
            MDMAuditLogger.dataModelCreationFailed(user, modelName, ex);
            return ServiceUtil.getErrorResponse(ex, errorMsg);
        }
    }

    @PUT
    @Path("{model}")
    @ApiOperation("Updates the requested model with the XSD provided as request content")
    @Consumes({ MediaType.APPLICATION_XML, MediaType.TEXT_XML })
    public Response updateModel(@ApiParam("Model name") @PathParam("model") String modelName,
            @ApiParam("Update model even if HIGH or MEDIUM impacts were found") @QueryParam("force") boolean force, InputStream dataModel) {
        String user = null;
        DataModelPOJO oldDataModel = null;
        // Prepare data for audit log
        try {
            user = LocalUser.getLocalUser().getUsername();
            oldDataModel = DataModelPOJO.load(DataModelPOJO.class, new DataModelPOJOPK(modelName));
        } catch (XtentisException e) {
            LOGGER.error("An error occurred while fetching Data Model.", e); //$NON-NLS-1$
        }
        if (!isSystemStorageAvailable()) { // If no system storage is available, store new schema version.
            try {
                DataModelPOJO dataModelPOJO = new DataModelPOJO(modelName);
                dataModelPOJO.setSchema(IOUtils.toString(dataModel, "UTF-8")); //$NON-NLS-1$
                dataModelPOJO.store();
                MDMAuditLogger.dataModelModified(user, oldDataModel, dataModelPOJO, true);
                return Response.ok().build();
            } catch (Exception e) {
                RuntimeException ex = new RuntimeException("An error occurred while updating Data Model.", e); //$NON-NLS-1$
                MDMAuditLogger.dataModelModificationFailed(user, modelName, ex);
                return ServiceUtil.getErrorResponse(ex, "An error occurred while updating Data Model."); //$NON-NLS-1$
            }
        }
        StorageAdmin storageAdmin = ServerContext.INSTANCE.get().getStorageAdmin();
        Storage storage = storageAdmin.get(modelName, StorageType.MASTER);
        if (storage == null) {
            return ServiceUtil.getErrorResponse(new NotFoundException(), StringUtils.EMPTY);
        }
        // Parses new data model version for comparison with existing
        MetadataRepository previousRepository = storage.getMetadataRepository();
        MetadataRepository newRepository = new MetadataRepository();
        String content;
        try {
            content = IOUtils.toString(dataModel, "UTF-8"); //$NON-NLS-1$
            newRepository.load(new ByteArrayInputStream(content.getBytes("UTF-8"))); //$NON-NLS-1$
        } catch (IOException e) {
            return ServiceUtil.getErrorResponse(new RuntimeException(), "Could not read data model from body."); //$NON-NLS-1$
        }
        // Ask the storage to adapt its structure following the changes
        storage.adapt(newRepository, force);
        if (storageAdmin.supportStaging(modelName)) {
            Storage stagingStorage = storageAdmin.get(modelName, StorageType.STAGING);
            if (stagingStorage != null) {
                // TMDM-7312: Don't forget to add staging types (otherwise, staging storage will complain about removed
                // types).
                MetadataRepository stagingRepository = newRepository.copy();
                stagingRepository.load(MetadataRepositoryAdmin.class.getResourceAsStream("stagingInternalTypes.xsd")); //$NON-NLS-1$
                stagingStorage.adapt(stagingRepository, force);
            } else {
                LOGGER.warn("No SQL staging storage defined for data model '" + modelName //$NON-NLS-1$
                        + "'. No SQL staging storage to update."); //$NON-NLS-1$
            }
        } else {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Storage '" + modelName + "' does not support staging (forbidden by storage admin)."); //$NON-NLS-1$ //$NON-NLS-2$
            }
        }
        // Update the system storage with the new data model (if there was any change).
        Compare.DiffResults diffResults = Compare.compare(previousRepository, newRepository);
        if (!diffResults.getActions().isEmpty()) {

            DataModelPOJO updatedDataModelPOJO = new DataModelPOJO(modelName);
            updatedDataModelPOJO.setDescription(oldDataModel.getDescription());
            updatedDataModelPOJO.setDigest(oldDataModel.getDigest());
            updatedDataModelPOJO.setName(oldDataModel.getName());
            updatedDataModelPOJO.setSchema(content);
            systemStorage.begin();
            try {
                // Marshal
                StringWriter sw = new StringWriter();
                MarshallingFactory.getInstance().getMarshaller(updatedDataModelPOJO.getClass()).marshal(updatedDataModelPOJO, sw);
                Document document = Util.parse(sw.toString());
                DataRecordReader<Element> reader = new XmlDOMDataRecordReader();
                DataRecord record = reader.read(newRepository, dataModelType, document.getDocumentElement());
                record.set(dataModelType.getField("schema"), content); //$NON-NLS-1$
                systemStorage.update(record);
                systemStorage.commit();

                reloadDataModel(modelName);
                // Add audit log
                MDMAuditLogger.dataModelModified(user, oldDataModel, updatedDataModelPOJO, true);
            } catch (Exception e) {
                systemStorage.rollback();
                MDMAuditLogger.dataModelModificationFailed(user, modelName, e);
                return ServiceUtil.getErrorResponse(new RuntimeException(), "Could not update data model."); //$NON-NLS-1$
            }
        }
        // synchronize with outer agents
        DataModelChangeNotifier dmUpdateEventNotifier = DataModelChangeNotifier.createInstance();
        dmUpdateEventNotifier.notifyChange(new DMUpdateEvent(modelName));
        return Response.ok().build();
    }


    private boolean isSystemStorageAvailable() {
        return !dataModelType.getName().isEmpty();
    }

    @POST
    @Path("{model}")
    @ApiOperation("Get impacts of the model update with the new XSD provided as request content. Changes will not be performed !")
    @Consumes({ MediaType.APPLICATION_XML, MediaType.TEXT_XML })
    public Response analyzeModelChange(@ApiParam("Model name") @PathParam("model") String modelName,
            @ApiParam("Optional language to get localized result") @QueryParam("lang") String locale,
            InputStream dataModel) {
        DataModelPOJO dataModelPOJO;
        try {
            dataModelPOJO = DataModelPOJO.load(DataModelPOJO.class, new DataModelPOJOPK(modelName));
        } catch (XtentisException e) {
            return ServiceUtil.getErrorResponse(new RuntimeException(), "An error occurred while fetching Data Model.");//$NON-NLS-1$
        }
        Map<ImpactAnalyzer.Impact, List<Change>> impacts;
        List<String> typeNamesToDrop = new ArrayList<String>();
        if (!isSystemStorageAvailable()) {
            impacts = new EnumMap<>(ImpactAnalyzer.Impact.class);
            for (ImpactAnalyzer.Impact impact : impacts.keySet()) {
                impacts.put(impact, Collections.<Change> emptyList());
            }
        } else {
            StorageAdmin storageAdmin = ServerContext.INSTANCE.get().getStorageAdmin();
            Storage storage = storageAdmin.get(modelName, StorageType.MASTER);
            if (storage == null || dataModelPOJO == null) {
                return ServiceUtil.getErrorResponse(new NotFoundException(), StringUtils.EMPTY);
            }

            if (storage.getType() == StorageType.SYSTEM) {
                return ServiceUtil.getErrorResponse(new IllegalArgumentException(), "No model update for system storage"); //$NON-NLS-1$
            }
            // Compare new data model with existing data model
            MetadataRepository previousRepository = storage.getMetadataRepository();
            MetadataRepository newRepository = new MetadataRepository();
            newRepository.load(dataModel);
            Compare.DiffResults diffResults = Compare.compare(previousRepository, newRepository);
            // Analyzes impacts on the select storage
            impacts = storage.getImpactAnalyzer().analyzeImpacts(diffResults);
            List<ComplexTypeMetadata> typesToDrop = storage.findSortedTypesToDrop(diffResults, true);
            Set<String> tableNamesToDrop = storage.findTablesToDrop(typesToDrop);
            for (String tableName : tableNamesToDrop) {
                if (previousRepository.getInstantiableTypes().contains(previousRepository.getComplexType(tableName))) {
                    typeNamesToDrop.add(tableName);
                }
            }
        }
        // Serialize results to XML
        StringWriter resultAsXml = new StringWriter();
        XMLStreamWriter writer = null;
        try {
            writer = XMLOutputFactory.newFactory().createXMLStreamWriter(resultAsXml);
            writer.writeStartElement("result"); //$NON-NLS-1$
            {
                for (Map.Entry<ImpactAnalyzer.Impact, List<Change>> category : impacts.entrySet()) {
                    writer.writeStartElement(category.getKey().name().toLowerCase());
                    List<Change> changes = category.getValue();
                    for (Change change : changes) {
                        writer.writeStartElement("change"); //$NON-NLS-1$
                        {
                            writer.writeStartElement("message"); //$NON-NLS-1$
                            {
                                Locale messageLocale;
                                if (StringUtils.isEmpty(locale)) {
                                    messageLocale = Locale.getDefault();
                                } else {
                                    messageLocale = LocaleUtil.getLocale(locale);
                                }
                                writer.writeCharacters(change.getMessage(messageLocale));
                            }
                            writer.writeEndElement();
                        }
                        writer.writeEndElement();
                    }
                    writer.writeEndElement();
                }
                writer.writeStartElement("entitiesToDrop"); //$NON-NLS-1$
                for (String typeName : typeNamesToDrop) {
                    writer.writeStartElement("entity"); //$NON-NLS-1$
                    writer.writeCharacters(typeName);
                    writer.writeEndElement();
                }
                writer.writeEndElement();
            }
            writer.writeEndElement();
        } catch (XMLStreamException e) {
            return ServiceUtil.getErrorResponse(e, e.getMessage());
        } finally {
            if (writer != null) {
                try {
                    writer.flush();
                } catch (XMLStreamException e) {
                    if (LOGGER.isDebugEnabled()) {
                        LOGGER.debug("Could not flush XML content.", e); //$NON-NLS-1$
                    }
                }
            }
        }
        return Response.ok(resultAsXml.toString()).build();
    }

}
