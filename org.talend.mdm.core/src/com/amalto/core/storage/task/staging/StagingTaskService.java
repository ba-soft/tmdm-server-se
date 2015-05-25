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

package com.amalto.core.storage.task.staging;

import java.io.StringReader;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;

import com.amalto.core.storage.task.ConfigurableFilter;
import com.amalto.core.storage.task.DefaultFilter;
import com.amalto.core.storage.task.Filter;
import com.amalto.core.util.Util;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;

@Path(StagingTaskService.TASKS)
@Api(value = StagingTaskService.TASKS, description = "Operations about staging tasks")
public class StagingTaskService {

    private static final Logger LOGGER = Logger.getLogger(StagingTaskService.class);
    
    public static final String TASKS = "/tasks/staging"; //$NON-NLS-1$

    private final StagingTaskServiceDelegate delegate = new DefaultStagingTaskService();

    @GET
    @Path("/")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML, MediaType.TEXT_XML})
    @ApiOperation(value="list staging tasks", response=StagingContainerSummary.class)
    public StagingContainerSummary getContainerSummary(@Context final HttpServletResponse response) {
        StagingContainerSummary result = delegate.getContainerSummary();
        if(result == null){
            response.setStatus(HttpStatus.NO_CONTENT.value());
            return null;
        }
        return result;
    }

    @POST
    @Path("/")
    public String startValidation() {
        return delegate.startValidation();
    }

    @GET
    @Path("{container}/")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML, MediaType.TEXT_XML})
    public StagingContainerSummary getContainerSummary(@PathParam("container") String dataContainer,
                                                       @QueryParam("model") String dataModel) {
        return delegate.getContainerSummary(dataContainer, dataModel);
    }

    @POST
    @Path("{container}/")
    @Consumes(MediaType.APPLICATION_XML)
    public String startValidation(@PathParam("container") String dataContainer,
                                  @QueryParam("model") String dataModel,
                                  @Context HttpServletRequest request) {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        Filter filter;
        try {
            DocumentBuilder builder = factory.newDocumentBuilder();
            String content = IOUtils.toString(request.getInputStream());
            if (!content.isEmpty()) {
                Document doc = builder.parse(new InputSource(new StringReader(content)));
                filter = new ConfigurableFilter(doc);
            } else {
                filter = DefaultFilter.INSTANCE;
            }
        } catch (Exception e) {
            filter = DefaultFilter.INSTANCE;
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Ignored parse error for staging filter: ", e);
            }
        }
        return delegate.startValidation(dataContainer, dataModel, filter);
    }

    @GET
    @Path("{container}/execs")
    public List<String> listCompletedTaskExecutions(@PathParam("container") String dataContainer,
                                                    @QueryParam("before") String beforeDate,
                                                    @DefaultValue("1") @QueryParam("start") int start,
                                                    @DefaultValue("-1") @QueryParam("size") int size) {
        return SerializableList.create(delegate.listCompletedExecutions(dataContainer, beforeDate, start, size), "executions", "execution");
    }

    @GET
    @Path("{container}/execs/count")
    public int countCompletedTaskExecutions(@PathParam("container") String dataContainer,
                                            @QueryParam("before") String beforeDate) {
        return delegate.listCompletedExecutions(dataContainer, beforeDate, 1, -1).size();
    }

    @GET
    @Path("{container}/execs/current/")
    public ExecutionStatistics getCurrentExecutionStats(@PathParam("container") String dataContainer,
                                                        @QueryParam("model") String dataModel) {
        return delegate.getCurrentExecutionStats(dataContainer, dataModel);
    }

    @DELETE
    @Path("{container}/execs/current/")
    public void cancelCurrentExecution(@PathParam("container") String dataContainer,
                                       @QueryParam("model") String dataModel) {
        delegate.cancelCurrentExecution(dataContainer, dataModel);
    }

    @GET
    @Path("{container}/execs/{executionId}/")
    public ExecutionStatistics getExecutionStats(@PathParam("container") String dataContainer,
                                                 @QueryParam("model") String dataModel,
                                                 @PathParam("executionId") String executionId) {
        return delegate.getExecutionStats(dataContainer, dataModel, executionId);
    }
    
    @GET
    @Path("{container}/hasStaging")
    public String isSupportStaging(@PathParam("container")
            String dataContainer) {
        try {
            return String.valueOf(Util.getXmlServerCtrlLocal().supportStaging(dataContainer));
        } catch (Exception e) {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Could not confirm staging support.", e); //$NON-NLS-1$
            }
            throw new RuntimeException("Could not confirm staging support.", e); //$NON-NLS-1$
        }
    }
}
