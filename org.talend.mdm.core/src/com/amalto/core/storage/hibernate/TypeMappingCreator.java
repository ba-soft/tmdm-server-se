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

package com.amalto.core.storage.hibernate;

import com.amalto.core.metadata.*;

import javax.xml.XMLConstants;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

class TypeMappingCreator extends DefaultMetadataVisitor<TypeMapping> {

    private final LinkedList<String> prefix = new LinkedList<String>();

    private final MetadataRepository internalRepository;

    private final MappingRepository mappings;

    private TypeMapping typeMapping;

    private boolean forceKey = false;

    public TypeMappingCreator(MetadataRepository repository, MappingRepository mappings) {
        this.mappings = mappings;
        this.internalRepository = repository;
    }

    String getColumnName(FieldMetadata field, boolean addPrefix) {
        StringBuilder buffer = new StringBuilder();
        if (addPrefix) {
            for (String currentPrefix : prefix) {
                buffer.append(currentPrefix).append('_');
            }
        }
        String name = field.getName();
        // Note #1: Hibernate (starting from 4.0) internally sets a lower case letter as first letter if field starts with a
        // upper case character. To prevent any error due to missing field, lower case the field name.
        // Note #2: Prefix everything with "x_" so there won't be any conflict with database internal type names.
        // Note #3: Having '-' character is bad for Java code generation, so replace it with '_'.
        return "x_" + (buffer.toString().replace('-', '_') + name).toLowerCase(); //$NON-NLS-1$
    }

    @Override
    public TypeMapping visit(ReferenceFieldMetadata referenceField) {
        String name = getColumnName(referenceField, true);
        ComplexTypeMetadata referencedType = new SoftTypeRef(internalRepository, referenceField.getReferencedType().getNamespace(), referenceField.getReferencedType().getName(), true);
        FieldMetadata referencedField = new SoftIdFieldRef(internalRepository, referenceField.getReferencedType().getName());
        FieldMetadata foreignKeyInfoField = referenceField.hasForeignKeyInfo() ? new SoftFieldRef(internalRepository, getColumnName(referenceField.getForeignKeyInfoField(), false), referencedType) : null;

        FieldMetadata newFlattenField;
        if (referenceField.getContainingType() == referenceField.getDeclaringType()) {
            ComplexTypeMetadata database = typeMapping.getDatabase();
            newFlattenField = new ReferenceFieldMetadata(database, referenceField.isKey(), referenceField.isMany(), referenceField.isMandatory(), name, referencedType, referencedField, foreignKeyInfoField, referenceField.isFKIntegrity(), referenceField.allowFKIntegrityOverride(), referenceField.getWriteUsers(), referenceField.getHideUsers());
            database.addField(newFlattenField);
        } else {
            newFlattenField = new SoftFieldRef(internalRepository, getColumnName(referenceField, true), referenceField.getContainingType());
        }
        typeMapping.map(referenceField, newFlattenField);
        return typeMapping;
    }

    @Override
    public TypeMapping visit(ContainedComplexTypeMetadata containedType) {
        List<FieldMetadata> fields = containedType.getFields();
        for (FieldMetadata field : fields) {
            field.accept(this);
        }
        return typeMapping;
    }

    @Override
    public TypeMapping visit(ContainedTypeFieldMetadata containedField) {
        prefix.add(containedField.getName());
        {
            ContainedComplexTypeMetadata containedType = containedField.getContainedType();
            containedType.accept(this);
            for (ComplexTypeMetadata subType : containedType.getSubTypes()) {
                for (FieldMetadata subTypeField : subType.getFields()) {
                    subTypeField.accept(this);
                }
            }
        }
        prefix.removeLast();
        return typeMapping;
    }

    @Override
    public TypeMapping visit(SimpleTypeFieldMetadata simpleField) {
        boolean isKey = false;
        FieldMetadata newFlattenField;
        if (simpleField.getContainingType() == simpleField.getDeclaringType()) {
            ComplexTypeMetadata database = typeMapping.getDatabase();
            newFlattenField = new SimpleTypeFieldMetadata(database, isKey, simpleField.isMany(), simpleField.isMandatory(), getColumnName(simpleField, true), simpleField.getType(), simpleField.getWriteUsers(), simpleField.getHideUsers());
            database.addField(newFlattenField);
        } else {
            newFlattenField = new SoftFieldRef(internalRepository, getColumnName(simpleField, true), simpleField.getContainingType());
        }
        typeMapping.map(simpleField, newFlattenField);
        return typeMapping;
    }

    @Override
    public TypeMapping visit(EnumerationFieldMetadata enumField) {
        // Seems pretty strange to use an enum field as key but nothing prevents user to do this.
        boolean isKey = enumField.isKey() || forceKey;
        FieldMetadata newFlattenField;
        if (enumField.getContainingType() == enumField.getDeclaringType()) {
            ComplexTypeMetadata database = typeMapping.getDatabase();
            newFlattenField = new EnumerationFieldMetadata(database, isKey, enumField.isMany(), enumField.isMandatory(), getColumnName(enumField, true), enumField.getType(), enumField.getWriteUsers(), enumField.getHideUsers());
            database.addField(newFlattenField);
        } else {
            newFlattenField = new SoftFieldRef(internalRepository, getColumnName(enumField, true), enumField.getContainingType());
        }
        typeMapping.map(enumField, newFlattenField);
        return typeMapping;
    }

    @Override
    public TypeMapping visit(ComplexTypeMetadata complexType) {
        typeMapping = new FlatTypeMapping(complexType, TypeMappingCreator.this.mappings);
        List<FieldMetadata> fields = complexType.getFields();
        for (FieldMetadata field : fields) {
            field.accept(this);
        }
        List<FieldMetadata> keyFields = complexType.getKeyFields();
        ComplexTypeMetadata database = typeMapping.getDatabase();
        Collection<TypeMetadata> superTypes = complexType.getSuperTypes();
        for (TypeMetadata superType : superTypes) {
            database.addSuperType(new SoftTypeRef(internalRepository, superType.getNamespace(), superType.getName(), true), internalRepository);
        }
        forceKey = true;
        for (FieldMetadata keyField : keyFields) {
            database.registerKey(new SoftFieldRef(internalRepository, "x_" + keyField.getName().toLowerCase(), database));
        }
        forceKey = false;
        if (typeMapping.getUser().getKeyFields().isEmpty() && typeMapping.getUser().getSuperTypes().isEmpty()) { // Assumes super type defines key field.
            SoftTypeRef type = new SoftTypeRef(internalRepository, XMLConstants.W3C_XML_SCHEMA_NS_URI, "string", false); //$NON-NLS-1$
            database.addField(new SimpleTypeFieldMetadata(database, true, false, true, "X_TALEND_ID", type, Collections.<String>emptyList(), Collections.<String>emptyList())); //$NON-NLS-1$ //$NON-NLS-2$
        }
        return typeMapping;
    }
}
