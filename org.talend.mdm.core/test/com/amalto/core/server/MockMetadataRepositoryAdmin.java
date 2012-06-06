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

package com.amalto.core.server;

import com.amalto.core.metadata.MetadataRepository;
import org.apache.log4j.Logger;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

class MockMetadataRepositoryAdmin implements MetadataRepositoryAdmin {
    public static final Logger LOGGER = Logger.getLogger(MockMetadataRepositoryAdmin.class);
    private final Map<String, MetadataRepository> metadataRepository = new HashMap<String, MetadataRepository>();

    public MetadataRepository get(String metadataRepositoryId) {
        synchronized (metadataRepository) {
            MetadataRepository repository = metadataRepository.get(metadataRepositoryId);

            if (repository == null) {
                repository = new MetadataRepository();
                InputStream resourceAsStream = this.getClass().getResourceAsStream(metadataRepositoryId);
                if (resourceAsStream == null) {
                    String base = this.getClass().getResource(".").toString();
                    String fullFileName = base + metadataRepositoryId;
                    LOGGER.info("File " + fullFileName + " can not be found.");
                    return repository;
                }
                repository.load(resourceAsStream);
                metadataRepository.put(metadataRepositoryId, repository);
            }
            return repository;
        }
    }

    public void remove(String metadataRepositoryId) {
        metadataRepository.remove(metadataRepositoryId);
    }

    public void update(String metadataRepositoryId) {
        remove(metadataRepositoryId);
        get(metadataRepositoryId);
    }

    public void close() {
    }

    public boolean exist(String metadataRepositoryId) {
        return true;
    }
}
