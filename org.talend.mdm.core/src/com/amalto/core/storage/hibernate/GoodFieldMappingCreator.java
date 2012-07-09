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
import org.apache.commons.lang.StringUtils;

import java.util.Collections;
import java.util.Stack;

class GoodFieldMappingCreator extends DefaultMetadataVisitor<TypeMapping> {

    private static final String GENERATED_ID = "x_talend_id";  //$NON-NLS-1$

    private final MetadataRepository internalRepository;

    private final MappingRepository mappings;

    private final Stack<ComplexTypeMetadata> currentType = new Stack<ComplexTypeMetadata>();

    private TypeMapping mapping;

    public GoodFieldMappingCreator(MetadataRepository repository, MappingRepository mappings) {
        internalRepository = repository;
        this.mappings = mappings;
    }

    private TypeMapping handleField(FieldMetadata field) {
        SimpleTypeFieldMetadata newFlattenField;
        String name = getFieldName(field);
        newFlattenField = new SimpleTypeFieldMetadata(currentType.peek(), field.isKey(), field.isMany(), field.isMandatory(), name, field.getType(), field.getWriteUsers(), field.getHideUsers());
        if (field.getDeclaringType() != field.getContainingType()) {
            newFlattenField.setDeclaringType(new SoftTypeRef(internalRepository, field.getDeclaringType().getNamespace(), field.getDeclaringType().getName()));
        }
        currentType.peek().addField(newFlattenField);
        mapping.map(field, newFlattenField);
        return null;
    }

    private String getFieldName(FieldMetadata field) {
        return "x_" + field.getName().toLowerCase(); //$NON-NLS-1$
    }

    @Override
    public TypeMapping visit(ReferenceFieldMetadata referenceField) {
        String name = referenceField.getName();
        ComplexTypeMetadata referencedType = new SoftTypeRef(internalRepository, StringUtils.EMPTY, referenceField.getReferencedType().getName());

        FieldMetadata referencedFieldCopy = new SoftIdFieldRef(internalRepository, referencedType.getName());
        FieldMetadata foreignKeyInfoFieldCopy = referenceField.hasForeignKeyInfo() ? referenceField.getForeignKeyInfoField().copy(internalRepository) : null;

        ComplexTypeMetadata database = currentType.peek();

        boolean fkIntegrity = referenceField.isFKIntegrity() && (referenceField.getReferencedType() != mapping.getUser()); // Don't enforce FK integrity for references to itself.
        ReferenceFieldMetadata newFlattenField = new ReferenceFieldMetadata(currentType.peek(),
                referenceField.isKey(),
                referenceField.isMany(),
                referenceField.isMandatory(),
                name,
                referencedType,
                referencedFieldCopy,
                foreignKeyInfoFieldCopy,
                fkIntegrity,
                referenceField.allowFKIntegrityOverride(),
                referenceField.getWriteUsers(),
                referenceField.getHideUsers());
        database.addField(newFlattenField);
        mapping.map(referenceField, newFlattenField);
        return null;
    }

    @Override
    public TypeMapping visit(ContainedComplexTypeMetadata containedType) {
        String databaseSuperType = handleContainedType(null, containedType.getContainerType().getName(), containedType);
        for (ComplexTypeMetadata subType : containedType.getSubTypes()) {
            handleContainedType(databaseSuperType, containedType.getContainerType().getName(), subType);
        }
        return null;
    }

    private String handleContainedType(String superTypeDatabaseName, String containerTypeName, ComplexTypeMetadata containedType) {
        String newTypeName = (containerTypeName.replace('-', '_') + "_2_" + containedType.getName().replace('-', '_')).toUpperCase(); //$NON-NLS-1$
        ComplexTypeMetadata newInternalType = new ComplexTypeMetadataImpl(containedType.getNamespace(),
                newTypeName,
                containedType.getWriteUsers(),
                containedType.getDenyCreate(),
                containedType.getHideUsers(),
                containedType.getDenyDelete(ComplexTypeMetadata.DeleteType.PHYSICAL),
                containedType.getDenyDelete(ComplexTypeMetadata.DeleteType.LOGICAL),
                containedType.getSchematron(),
                false);
        if (superTypeDatabaseName == null) {  // Generate a technical ID only if contained type does not have super type (subclasses will inherit it).
            newInternalType.addField(new SimpleTypeFieldMetadata(newInternalType,
                    true,
                    false,
                    true,
                    GENERATED_ID,
                    new SoftTypeRef(internalRepository, internalRepository.getUserNamespace(), "UUID"), //$NON-NLS-1$
                    containedType.getWriteUsers(),
                    containedType.getHideUsers()));
        } else {
            newInternalType.addSuperType(new SoftTypeRef(internalRepository, newInternalType.getNamespace(), superTypeDatabaseName), internalRepository);
        }

        internalRepository.addTypeMetadata(newInternalType);
        currentType.push(newInternalType);
        {
            super.visit(containedType);
        }
        currentType.pop();
        return newTypeName;
    }

    @Override
    public TypeMapping visit(ContainedTypeFieldMetadata containedField) {
        String typeName = (containedField.getDeclaringType().getName().replace('-', '_') + "_2_" + containedField.getContainedType().getName().replace('-', '_')).toUpperCase(); //$NON-NLS-1$
        SoftTypeRef typeRef = new SoftTypeRef(internalRepository,
                containedField.getDeclaringType().getNamespace(),
                typeName);
        ComplexTypeMetadata database = currentType.peek();
        ReferenceFieldMetadata newFlattenField = new ReferenceFieldMetadata(database,
                false,
                containedField.isMany(),
                containedField.isMandatory(),
                getFieldName(containedField),
                typeRef,
                new SoftIdFieldRef(internalRepository, typeName),
                null,
                false,  // No need to enforce FK in references to these technical objects.
                false,
                containedField.getWriteUsers(),
                containedField.getHideUsers());
        newFlattenField.setData("SQL_DELETE_CASCADE", "true"); //$NON-NLS-1$ //$NON-NLS-2$

        database.addField(newFlattenField);
        mapping.map(containedField, newFlattenField);
        containedField.getContainedType().accept(this);
        return null;
    }

    @Override
    public TypeMapping visit(SimpleTypeFieldMetadata simpleField) {
        return handleField(simpleField);
    }

    @Override
    public TypeMapping visit(EnumerationFieldMetadata enumField) {
        return handleField(enumField);
    }

    @Override
    public TypeMapping visit(ComplexTypeMetadata complexType) {
        mapping = new GoodFieldTypeMapping(complexType, mappings);
        ComplexTypeMetadata database = mapping.getDatabase();

        currentType.push(database);
        {
            internalRepository.addTypeMetadata(database);
            if (complexType.getKeyFields().isEmpty() && complexType.getSuperTypes().isEmpty()) { // Assumes super type will define an id.
                database.addField(new SimpleTypeFieldMetadata(database, true, false, true, GENERATED_ID, new SoftTypeRef(internalRepository, StringUtils.EMPTY, "UUID"), Collections.<String>emptyList(), Collections.<String>emptyList())); //$NON-NLS-1$
            }
            for (TypeMetadata superType : complexType.getSuperTypes()) {
                database.addSuperType(new SoftTypeRef(internalRepository, superType.getNamespace(), superType.getName()), internalRepository);
            }
            super.visit(complexType);
        }
        currentType.pop();
        if (!currentType.isEmpty()) { // This is unexpected
            throw new IllegalStateException("Type remained in process stack.");
        }
        return mapping;
    }
}
