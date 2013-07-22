/*
 * Copyright (C) 2006-2013 Talend Inc. - www.talend.com
 *
 * This source code is available under agreement available at
 * %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
 *
 * You should have received a copy of the agreement
 * along with this program; if not, write to Talend SA
 * 9 rue Pages 92150 Suresnes, France
 */

package com.amalto.core.server;

import com.amalto.core.storage.Storage;
import com.amalto.core.storage.transaction.StorageTransaction;
import com.amalto.core.storage.transaction.Transaction;
import org.apache.commons.lang.NotImplementedException;
import org.apache.log4j.Logger;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

class MDMTransaction implements Transaction {

    private static final Logger LOGGER = Logger.getLogger(MDMTransaction.class);

    private final String id = UUID.randomUUID().toString();

    private final Map<Storage, StorageTransaction> storageTransactions = new HashMap<Storage, StorageTransaction>();

    private final Lifetime lifetime;

    MDMTransaction(Lifetime lifetime) {
        this.lifetime = lifetime;
    }

    private void transactionComplete() {
        ServerContext.INSTANCE.get().getTransactionManager().remove(this);
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void begin() {
        for (StorageTransaction storageTransaction : storageTransactions.values()) {
            storageTransaction.autonomous().begin();
        }
    }

    @Override
    public void commit() {
        try {
            for (StorageTransaction storageTransaction : storageTransactions.values()) {
                storageTransaction.autonomous().commit();
            }
        } finally {
            transactionComplete();
        }
    }

    @Override
    public void rollback() {
        try {
            for (StorageTransaction storageTransaction : storageTransactions.values()) {
                storageTransaction.autonomous().rollback();
            }
        } finally {
            transactionComplete();
        }
    }

    @Override
    public StorageTransaction exclude(Storage storage) {
        StorageTransaction transaction = storageTransactions.remove(storage);
        if (storageTransactions.isEmpty()) {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Transaction '" + getId() + "' has no more storage transaction. Removing it.");
            }
            transactionComplete();
        }
        return transaction;
    }

    public StorageTransaction include(Storage storage) {
        StorageTransaction storageTransaction = storageTransactions.get(storage);
        if (storageTransaction == null) {
            storageTransaction = storage.newStorageTransaction();
            storageTransactions.put(storage, storageTransaction);
        }
        switch (lifetime) {
            case AD_HOC:
                return storageTransaction.autonomous();
            case LONG:
                return storageTransaction.dependent();
            default:
                throw new NotImplementedException("No support for life time '" + lifetime + "'");
        }
    }

    @Override
    public String toString() {
        return "MDMTransaction{" +
                "id='" + id + '\'' +
                ", lifetime=" + lifetime +
                '}';
    }
}
