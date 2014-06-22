/*
 * Copyright (C) 2006-2014 Talend Inc. - www.talend.com
 *
 * This source code is available under agreement available at
 * %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
 *
 * You should have received a copy of the agreement
 * along with this program; if not, write to Talend SA
 * 9 rue Pages 92150 Suresnes, France
 */

package com.amalto.core.save.context;

import com.amalto.core.history.MutableDocument;
import com.amalto.core.history.accessor.Accessor;
import org.talend.mdm.commmon.metadata.ComplexTypeMetadata;
import org.w3c.dom.Document;

import javax.xml.XMLConstants;
import java.util.Date;

class ChangeTypeAction extends AbstractChangeTypeAction {

    public ChangeTypeAction(Date date, String source, String userName, String path, ComplexTypeMetadata previousType, ComplexTypeMetadata newType) {
        super(date, source, userName, path, previousType, newType);
    }

    public MutableDocument perform(MutableDocument document) {
        if (!hasChangedType) {
            document.createAccessor(path).touch();
            return document;
        }
        // Ensure xsi prefix is declared
        Document domDocument = document.asDOM();
        String xsi = domDocument.lookupNamespaceURI("xsi"); //$NON-NLS-1$
        if (xsi == null) {
            domDocument.getDocumentElement().setAttributeNS(XMLConstants.XMLNS_ATTRIBUTE_NS_URI, "xmlns:xsi", XMLConstants.W3C_XML_SCHEMA_INSTANCE_NS_URI);  //$NON-NLS-1$
        }

        Accessor typeAccessor = document.createAccessor(path + "/@xsi:type"); //$NON-NLS-1$
        String typeName = newType.getName();
        if (typeAccessor.exist() && !typeName.equals(typeAccessor.get())) {
            for (String currentPathToDelete : pathToClean) {
                Accessor accessor = document.createAccessor(path + '/' + currentPathToDelete);
                if (accessor.exist()) {
                    accessor.delete();
                }
            }
        }
        typeAccessor.createAndSet(typeName);
        return document;
    }

    public MutableDocument undo(MutableDocument document) {
        if (!hasChangedType) {
            document.createAccessor(path).touch();
            return document;
        }
        Accessor accessor = document.createAccessor(path + "/@xsi:type"); //$NON-NLS-1$
        accessor.delete();
        return document;
    }

    public String getDetails() {
        if (hasChangedType) {
            return "Change type to " + newType.getName(); //$NON-NLS-1$
        } else {
            return "Change type to " + newType.getName() + " (NO OP)"; //$NON-NLS-1$ //$NON-NLS-2$
        }
    }

    @Override
    public String toString() {
        return "ChangeTypeAction{" +  //$NON-NLS-1$
                "path='" + path + '\'' + //$NON-NLS-1$
                ", newType=" + newType + //$NON-NLS-1$
                ", changedType= " + hasChangedType + //$NON-NLS-1$
                '}';
    }
}
