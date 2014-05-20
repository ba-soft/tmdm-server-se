// ============================================================================
//
// Copyright (C) 2006-2014 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.mdm.webapp.welcomeportal.client.widget;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.talend.mdm.webapp.base.client.SessionAwareAsyncCallback;
import org.talend.mdm.webapp.welcomeportal.client.WelcomePortal;
import org.talend.mdm.webapp.welcomeportal.client.rest.StatisticsRestServiceHandler;

import com.extjs.gxt.ui.client.event.IconButtonEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.widget.button.ToolButton;
import com.extjs.gxt.ui.client.widget.custom.Portal;
import com.extjs.gxt.ui.client.widget.form.FieldSet;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONObject;
import com.googlecode.gflot.client.DataPoint;
import com.googlecode.gflot.client.PlotModel;
import com.googlecode.gflot.client.Series;
import com.googlecode.gflot.client.SeriesHandler;
import com.googlecode.gflot.client.options.AxesOptions;
import com.googlecode.gflot.client.options.AxisOptions;
import com.googlecode.gflot.client.options.BarSeriesOptions;
import com.googlecode.gflot.client.options.BarSeriesOptions.BarAlignment;
import com.googlecode.gflot.client.options.CategoriesAxisOptions;
import com.googlecode.gflot.client.options.GlobalSeriesOptions;
import com.googlecode.gflot.client.options.LegendOptions;
import com.googlecode.gflot.client.options.LineSeriesOptions;
import com.googlecode.gflot.client.options.PlotOptions;

public class JournalChart extends ChartPortlet {

    private static String JOURNAL_ACTION_CREATE = "create"; //$NON-NLS-1$

    private static String JOURNAL_ACTION_UPDATE = "update"; //$NON-NLS-1$

    private Map<String, Map<String, Integer>> journalData;

    public JournalChart(Portal portal) {

        super(WelcomePortal.CHART_JOURNAL, portal);

        this.getHeader().addTool(new ToolButton("x-tool-refresh", new SelectionListener<IconButtonEvent>() { //$NON-NLS-1$

                    @Override
                    public void componentSelected(IconButtonEvent ce) {

                        service.getCurrentDataContainer(new SessionAwareAsyncCallback<String>() {

                            @Override
                            public void onSuccess(String dataContainer) {

                                StatisticsRestServiceHandler.getInstance().getContainerJournalStats(dataContainer,
                                        new SessionAwareAsyncCallback<JSONArray>() {

                                            @Override
                                            public void onSuccess(JSONArray jsonArray) {
                                                journalData = parseJSONData(jsonArray);
                                                refreshPlot();
                                            }
                                        });
                            }
                        });

                    }

                }));

        initChart();
    }

    @Override
    public void refresh() {

        service.getCurrentDataContainer(new SessionAwareAsyncCallback<String>() {

            @Override
            public void onSuccess(String dataContainer) {

                StatisticsRestServiceHandler.getInstance().getContainerJournalStats(dataContainer,
                        new SessionAwareAsyncCallback<JSONArray>() {

                            @Override
                            public void onSuccess(JSONArray jsonArray) {
                                journalData = parseJSONData(jsonArray);
                                refreshPlot();
                            }
                        });
            }
        });

    }

    private void initChart() {
        final FieldSet set = (FieldSet) this.getItemByItemId(WelcomePortal.CHART_JOURNAL + "Set"); //$NON-NLS-1$

        service.getCurrentDataContainer(new SessionAwareAsyncCallback<String>() {

            @Override
            public void onSuccess(String dataContainer) {

                StatisticsRestServiceHandler.getInstance().getContainerJournalStats(dataContainer,
                        new SessionAwareAsyncCallback<JSONArray>() {

                            @Override
                            public void onSuccess(JSONArray jsonArray) {
                                journalData = parseJSONData(jsonArray);
                                initPlot(journalData);
                                set.add(plot);
                                // FIXME: needed?
                                set.layout(true);
                            }
                        });
            }
        });
    }

    private void initPlot(Map<String, Map<String, Integer>> journalData) {
        super.initPlot();
        PlotModel model = plot.getModel();
        PlotOptions plotOptions = plot.getOptions();
        Set<String> entityNames = journalData.keySet();
        List<String> entityNamesSorted = sort(entityNames);

        plotOptions
                .setGlobalSeriesOptions(
                        GlobalSeriesOptions
                                .create()
                                .setLineSeriesOptions(LineSeriesOptions.create().setShow(false).setFill(true))
                                .setBarsSeriesOptions(
                                        BarSeriesOptions.create().setShow(true).setBarWidth(0.6)
                                                .setAlignment(BarAlignment.CENTER)).setStack(true))
                .setYAxesOptions(AxesOptions.create().addAxisOptions(AxisOptions.create().setTickDecimals(0).setMinimum(0)))
                .setXAxesOptions(
                        AxesOptions.create().addAxisOptions(
                                CategoriesAxisOptions.create().setCategories(
                                        entityNamesSorted.toArray(new String[entityNamesSorted.size()]))));

        plotOptions.setLegendOptions(LegendOptions.create().setShow(true));

        // create series
        SeriesHandler seriesCreation = model.addSeries(Series.of("Creation")); //$NON-NLS-1$
        SeriesHandler seriesUpdate = model.addSeries(Series.of("Update")); //$NON-NLS-1$

        // add data
        for (String entityName : entityNamesSorted) {
            seriesCreation.add(DataPoint.of(entityName, journalData.get(entityName).get(JOURNAL_ACTION_CREATE)));
            seriesUpdate.add(DataPoint.of(entityName, journalData.get(entityName).get(JOURNAL_ACTION_UPDATE)));
        }
    }

    @Override
    protected void updatePlot() {
        PlotModel model = plot.getModel();
        PlotOptions plotOptions = plot.getOptions();
        Set<String> entityNames = journalData.keySet();
        List<String> entityNamesSorted = sort(entityNames);

        plotOptions.setXAxesOptions(AxesOptions.create().addAxisOptions(
                CategoriesAxisOptions.create().setCategories(entityNamesSorted.toArray(new String[entityNamesSorted.size()]))));

        List<? extends SeriesHandler> series = model.getHandlers();
        assert series.size() == 2;
        SeriesHandler seriesCreation = series.get(0);
        SeriesHandler seriesUpdate = series.get(1);

        seriesCreation.clear();
        seriesUpdate.clear();
        for (String appName : entityNamesSorted) {
            seriesCreation.add(DataPoint.of(appName, journalData.get(appName).get(JOURNAL_ACTION_CREATE)));
            seriesUpdate.add(DataPoint.of(appName, journalData.get(appName).get(JOURNAL_ACTION_UPDATE)));
        }
    }

    private Map<String, Map<String, Integer>> parseJSONData(JSONArray jsonArray) {
        JSONObject currentJSONObj;
        JSONArray events;
        JSONObject creations;
        JSONObject updates;

        int numOfEntities = jsonArray.size();
        List<String> entities = new ArrayList<String>(numOfEntities);
        Map<String, Map<String, Integer>> journalData = new HashMap<String, Map<String, Integer>>(numOfEntities);

        for (int i = 0; i < numOfEntities; i++) {
            currentJSONObj = (JSONObject) jsonArray.get(i);
            Set<String> entityNames = currentJSONObj.keySet();
            String entityName = entityNames.iterator().next();
            entities.add(entityName);
            events = currentJSONObj.get(entityName).isArray();
            assert (events.size() == 2);

            creations = events.get(0).isObject();
            updates = events.get(1).isObject();

            Map<String, Integer> eventMap = new HashMap<String, Integer>(2);
            int numOfUpdates = 0;
            int numOfCreates = 0;
            JSONArray createArray = creations.get("creations").isArray(); //$NON-NLS-1$
            JSONObject curCreate;
            for (int j = 0; j < createArray.size(); j++) {
                curCreate = createArray.get(j).isObject();
                numOfCreates += (int) curCreate.get("create").isNumber().getValue(); //$NON-NLS-1$
            }

            JSONArray updateArray = updates.get("updates").isArray(); //$NON-NLS-1$
            JSONObject curUpdate;
            for (int k = 0; k < updateArray.size(); k++) {
                curUpdate = updateArray.get(k).isObject();
                numOfUpdates += (int) curUpdate.get("update").isNumber().getValue(); //$NON-NLS-1$
            }

            eventMap.put("create", numOfCreates); //$NON-NLS-1$
            eventMap.put("update", numOfUpdates); //$NON-NLS-1$
            journalData.put(entityName, eventMap);
        }
        return journalData;
    }
}
