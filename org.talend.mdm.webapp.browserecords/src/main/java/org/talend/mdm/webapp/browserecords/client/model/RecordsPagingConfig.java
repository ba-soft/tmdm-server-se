package org.talend.mdm.webapp.browserecords.client.model;

import java.io.Serializable;

import com.google.gwt.user.client.rpc.IsSerializable;

public class RecordsPagingConfig implements IsSerializable, Serializable {

    private static final long serialVersionUID = -5336430937639916542L;

    private int limit;

    private int offset;

    private String sortDir;

    private String sortField;

    public RecordsPagingConfig() {

    }

    public int getLimit() {
        return limit;
    }

    public int getOffset() {
        return offset;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public String getSortDir() {
        return sortDir;
    }

    public String getSortField() {
        return sortField;
    }
    public void setSortDir(String sortDir) {
        this.sortDir = sortDir;
    }

    public void setSortField(String sortField) {
        this.sortField = sortField;
    }

}
