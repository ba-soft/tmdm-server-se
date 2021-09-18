/*
 * Copyright (C) 2006-2019 Talend Inc. - www.talend.com
 * 
 * This source code is available under agreement available at
 * %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
 * 
 * You should have received a copy of the agreement along with this program; if not, write to Talend SA 9 rue Pages
 * 92150 Suresnes, France
 */

package com.amalto.core.storage.hibernate;

import static com.amalto.core.query.user.UserQueryBuilder.count;
import static com.amalto.core.query.user.UserQueryBuilder.emptyOrNull;
import static com.amalto.core.query.user.UserQueryBuilder.not;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.amalto.core.storage.HibernateStorageUtils;
import com.amalto.core.storage.datasource.RDBMSDataSource;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.talend.mdm.commmon.metadata.*;
import org.talend.mdm.commmon.metadata.compare.AddChange;
import org.talend.mdm.commmon.metadata.compare.Change;
import org.talend.mdm.commmon.metadata.compare.Compare;
import org.talend.mdm.commmon.metadata.compare.HibernateStorageImpactAnalyzer;
import org.talend.mdm.commmon.metadata.compare.ModifyChange;
import org.talend.mdm.commmon.metadata.compare.RemoveChange;

import com.amalto.core.query.user.UserQueryBuilder;
import com.amalto.core.storage.StorageResults;
import org.talend.mdm.commmon.util.core.CommonUtil;

@SuppressWarnings("nls")
public class HibernateStorageDataAnaylzer extends HibernateStorageImpactAnalyzer {

    private static final Logger LOGGER = LogManager.getLogger(HibernateStorageDataAnaylzer.class);

    private HibernateStorage storage;

    public HibernateStorageDataAnaylzer(HibernateStorage storage) {
        this.storage = storage;
    }

    @Override
    public Map<Impact, List<Change>> analyzeImpacts(Compare.DiffResults diffResult) {
        // Modify actions
        for (ModifyChange modifyAction : diffResult.getModifyChanges()) {
            MetadataVisitable element = modifyAction.getElement();
            if (element instanceof FieldMetadata) {
                FieldMetadata previous = (FieldMetadata) modifyAction.getPrevious();
                FieldMetadata current = (FieldMetadata) modifyAction.getCurrent();

                if (current.isMandatory() && !previous.isMandatory() && !(element instanceof ContainedTypeFieldMetadata)) {
                    int count = fetchFieldCountOfNull(previous.getContainingType().getEntity(), previous);
                    modifyAction.addData(Change.HAS_NULL_VALUE, count > 0);
                }

                RDBMSDataSource.DataSourceDialect dialect = ((RDBMSDataSource) storage.getDataSource()).getDialectName();
                if (element instanceof SimpleTypeFieldMetadata) {
                    String fieldType = MetadataUtils.getSuperConcreteType(((FieldMetadata) element).getType()).getName();
                    if (fieldType.equals("string")) {
                        Object oldLengthObj = CommonUtil.getSuperTypeMaxLength(previous.getType(), previous.getType());
                        Object newLengthObj = CommonUtil.getSuperTypeMaxLength(current.getType(), current.getType());

                        int oldLength = Integer.parseInt((oldLengthObj == null ? STRING_DEFAULT_LENGTH : (String) oldLengthObj));
                        int newLength = Integer.parseInt((newLengthObj == null ? STRING_DEFAULT_LENGTH : (String) newLengthObj));

                        //increase length
                        if (newLength > oldLength && newLength > dialect.getTextLimit() && oldLength <= dialect.getTextLimit()
                                && (HibernateStorageUtils.isDB2(dialect) || HibernateStorageUtils.isOracle(dialect))) {
                            modifyAction.addData(ModifyChange.CHANGE_TO_CLOB, true);
                        } else if (oldLength > newLength && newLength > dialect.getTextLimit()) {
                            //decrease length
                            modifyAction.addData(ModifyChange.TEXT_TO_TEXT, true);
                        }
                    }
                }
            }
        }

        Map<RemoveChange, ComplexTypeMetadata> renamedReferenceFieldMap = getRenamedFKFieldMap(diffResult);
        for (Map.Entry<RemoveChange, ComplexTypeMetadata> entry : renamedReferenceFieldMap.entrySet()) {
            RemoveChange removeChange = entry.getKey();
            int count = fetchFieldCountOfNotNull(entry.getValue(), (FieldMetadata) removeChange.getElement());
            removeChange.setContainsData(count > 0);
        }
        return super.analyzeImpacts(diffResult);
    }

    private Map<RemoveChange, ComplexTypeMetadata> getRenamedFKFieldMap(Compare.DiffResults diffResult) {
        Map<FieldMetadata, RemoveChange> removeReferenceFieldMap = new HashMap<>();
        Map<RemoveChange, ComplexTypeMetadata> renamedReferenceFieldMap = new HashMap<>();
        for (RemoveChange removeAction : diffResult.getRemoveChanges()) {
            MetadataVisitable element = removeAction.getElement();
            if (element instanceof ReferenceFieldMetadata) {
                removeReferenceFieldMap.put(((ReferenceFieldMetadata) element).getReferencedField(), removeAction);
            }
        }
        if (removeReferenceFieldMap.isEmpty()) {
            return renamedReferenceFieldMap;
        }
        for (AddChange addAction : diffResult.getAddChanges()) {
            MetadataVisitable element = addAction.getElement();
            if (element instanceof ReferenceFieldMetadata) {
                ReferenceFieldMetadata referenceField = ((ReferenceFieldMetadata) element);
                FieldMetadata referencedField = referenceField.getReferencedField();
                if (removeReferenceFieldMap.containsKey(referencedField)) {
                    renamedReferenceFieldMap.put((removeReferenceFieldMap.get(referencedField)),
                            referenceField.getContainingType().getEntity());
                }
            }
        }
        return renamedReferenceFieldMap;
    }

    private int fetchFieldCountOfNotNull(ComplexTypeMetadata entry, FieldMetadata field) {
        UserQueryBuilder qb = UserQueryBuilder.from(entry).select(count()).where(not(emptyOrNull(field)));
        return fetchFieldCountByUserQuery(qb);
    }

    private int fetchFieldCountOfNull(ComplexTypeMetadata entry, FieldMetadata field) {
        UserQueryBuilder qb = UserQueryBuilder.from(entry).select(count()).where(emptyOrNull(field));
        return fetchFieldCountByUserQuery(qb);
    }

    private int fetchFieldCountByUserQuery(UserQueryBuilder qb) {
        int count = 0;
        storage.begin();
        StorageResults results = null;
        try {
            results = storage.fetch(qb.getSelect());
            count = results.getCount();
        } catch (Exception e) {
            LOGGER.error("Failed to fetch count of field.", e); //$NON-NLS-1$
        } finally {
            if (results != null) {
                results.close();
            }
            storage.commit();
        }
        return count;
    }
}
