// ============================================================================
//
// Copyright (C) 2006-2011 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package com.amalto.core.plugin.base.mimecontent;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

/**
 * DOC fliu class global comment. Detailled comment
 */
public class CompiledParameters implements Serializable {

    private String mimetype;

    public String getMimeType() {
        return mimetype;
    }

    public void setMimeType(String type) {
        this.mimetype = type;
    }

    public String serialize() throws IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutputStream ois = new ObjectOutputStream(bos);
        ois.writeObject(this);
        return new BASE64Encoder().encode(bos.toByteArray());
    }

    public static CompiledParameters deserialize(String base64String) throws IOException, ClassNotFoundException {
        if (base64String == null || base64String.length() == 0)
            return new CompiledParameters();
        byte[] bytes = new BASE64Decoder().decodeBuffer(base64String);
        ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
        ObjectInputStream ois = new ObjectInputStream(bis);
        return (CompiledParameters) ois.readObject();
    }
}
