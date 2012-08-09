// ============================================================================
//
// Copyright (C) 2006-2012 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.mdm.webapp.stagingarea.client.view;

import org.talend.mdm.webapp.base.client.util.UserContextUtil;
import org.talend.mdm.webapp.stagingarea.client.controller.ControllerContainer;
import org.talend.mdm.webapp.stagingarea.client.rest.RestServiceHandler;

import com.extjs.gxt.ui.client.widget.button.Button;
import com.google.gwt.junit.client.GWTTestCase;
import com.google.gwt.user.client.ui.RootPanel;

@SuppressWarnings("nls")
public class CurrentValidationViewTest extends GWTTestCase {

    @Override
    protected void gwtSetUp() throws Exception {
        super.gwtSetUp();
        RestServiceHandler.get().setClient(new ResourceMockWrapper());
    }

    public void testCurrentValidationView() {
        UserContextUtil.setDataContainer("TestDataContainer");
        UserContextUtil.setDataModel("TestDataModel");

        Button chart = new Button("chart");
        StagingContainerSummaryView.setChart(chart);

        StagingContainerSummaryView summaryView = new StagingContainerSummaryView();
        CurrentValidationView view = new CurrentValidationView();
        ControllerContainer.setCurrentValidationView(view);
        ControllerContainer.setStagingContainerSummaryView(summaryView);

        RootPanel.get().add(summaryView);
        RootPanel.get().add(view);

        ControllerContainer.get().getSummaryController().refreshView();
        ControllerContainer.get().getCurrentValidationController().refreshView();

        assertEquals(CurrentValidationView.Status.None, view.getStatus());

    }
    @Override
    public String getModuleName() {
        return "org.talend.mdm.webapp.stagingarea.Stagingarea"; //$NON-NLS-1$
    }
}
