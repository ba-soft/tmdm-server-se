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
package org.talend.mdm.webapp.browserecords.client.widget.treedetail;

import java.util.ArrayList;
import java.util.List;

import org.talend.mdm.webapp.base.client.SessionAwareAsyncCallback;
import org.talend.mdm.webapp.base.client.model.ForeignKeyBean;
import org.talend.mdm.webapp.base.client.model.ItemBasePageLoadResult;
import org.talend.mdm.webapp.base.shared.TypeModel;
import org.talend.mdm.webapp.browserecords.client.BrowseRecords;
import org.talend.mdm.webapp.browserecords.client.BrowseRecordsServiceAsync;
import org.talend.mdm.webapp.browserecords.client.i18n.MessagesFactory;
import org.talend.mdm.webapp.browserecords.client.model.ForeignKeyDrawer;
import org.talend.mdm.webapp.browserecords.client.model.ItemNodeModel;
import org.talend.mdm.webapp.browserecords.client.model.Restriction;
import org.talend.mdm.webapp.browserecords.client.resources.icon.Icons;
import org.talend.mdm.webapp.browserecords.client.util.CommonUtil;
import org.talend.mdm.webapp.browserecords.client.util.Locale;
import org.talend.mdm.webapp.browserecords.client.widget.ItemPanel;
import org.talend.mdm.webapp.browserecords.client.widget.ItemsDetailPanel;
import org.talend.mdm.webapp.browserecords.client.widget.ForeignKey.ReturnCriteriaFK;
import org.talend.mdm.webapp.browserecords.client.widget.inputfield.ComboBoxField;
import org.talend.mdm.webapp.browserecords.shared.EntityModel;

import com.extjs.gxt.ui.client.Registry;
import com.extjs.gxt.ui.client.data.BaseListLoadResult;
import com.extjs.gxt.ui.client.data.BaseListLoader;
import com.extjs.gxt.ui.client.data.BaseModel;
import com.extjs.gxt.ui.client.data.BasePagingLoadConfig;
import com.extjs.gxt.ui.client.data.BasePagingLoadResult;
import com.extjs.gxt.ui.client.data.BasePagingLoader;
import com.extjs.gxt.ui.client.data.ListLoadResult;
import com.extjs.gxt.ui.client.data.ModelData;
import com.extjs.gxt.ui.client.data.PagingLoadConfig;
import com.extjs.gxt.ui.client.data.PagingLoadResult;
import com.extjs.gxt.ui.client.data.PagingLoader;
import com.extjs.gxt.ui.client.data.RpcProxy;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.FieldEvent;
import com.extjs.gxt.ui.client.event.GridEvent;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.SelectionChangedEvent;
import com.extjs.gxt.ui.client.event.SelectionChangedListener;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.Window;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.ComboBox.TriggerAction;
import com.extjs.gxt.ui.client.widget.form.FormPanel;
import com.extjs.gxt.ui.client.widget.form.TextField;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.ColumnData;
import com.extjs.gxt.ui.client.widget.grid.ColumnModel;
import com.extjs.gxt.ui.client.widget.grid.Grid;
import com.extjs.gxt.ui.client.widget.grid.GridCellRenderer;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.layout.FlowData;
import com.extjs.gxt.ui.client.widget.toolbar.PagingToolBar;
import com.extjs.gxt.ui.client.widget.toolbar.ToolBar;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.AbstractImagePrototype;

/**
 * Tree Detail : select ForeignKey UI
 */
public class ForeignKeyListWindow extends Window {

    private static final int COLUMN_WIDTH = 100;

    private static final int WINDOW_WIDTH = 450;

    private static final int WINDOW_HEIGH = 300;

    private Grid<ForeignKeyBean> relatedRecordGrid;

    private String fkKey;

    private ReturnCriteriaFK returnCriteriaFK;

    private ListStore<BaseModel> typeList = new ListStore<BaseModel>();

    private BrowseRecordsServiceAsync service = (BrowseRecordsServiceAsync) Registry.get(BrowseRecords.BROWSERECORDS_SERVICE);

    private PagingLoader<PagingLoadResult<ModelData>> loader;

    private int pageSize = 20;

    private TextField<String> filter = new TextField<String>();

    private ComboBoxField<BaseModel> typeComboBox;

    private String xPath;

    private EntityModel entityModel;

    private String foreignKey;

    private List<String> foreignKeyInfo;

    private String foreignKeyFilter;

    private String xml;

    private String currentXpath;

    public ForeignKeyListWindow() {
    }

    public String getForeignKeyFilter() {
        return foreignKeyFilter;
    }

    public void setForeignKeyFilter(String foreignKeyFilter) {
        this.foreignKeyFilter = foreignKeyFilter;
    }

    public void setForeignKeyInfos(String fk, List<String> fkInfo) {
        this.foreignKey = fk;
        this.foreignKeyInfo = fkInfo;
    }

    protected void setEntityModel(EntityModel entityModel) {
        this.entityModel = entityModel;
        this.fkKey = entityModel.getConceptName();
    }

    protected EntityModel getEntityModel() {
        return entityModel;
    }

    public String getFkKey() {
        return fkKey;
    }

    public void setFkKey(String fkKey) {
        this.fkKey = fkKey;
    }

    public ReturnCriteriaFK getReturnCriteriaFK() {
        return returnCriteriaFK;
    }

    public void setReturnCriteriaFK(ReturnCriteriaFK returnCriteriaFK) {
        this.returnCriteriaFK = returnCriteriaFK;
    }

    private String getFilterValue() {
        String value = filter.getRawValue();
        if (value == null || value.trim().length() == 0) {
            value = ".*"; //$NON-NLS-1$
        }
        return value;
    }

    protected TypeModel buildTypeModel() {
        TypeModel typeModel = getEntityModel().getMetaDataTypes().get(fkKey);
        typeModel.setForeignkey(this.foreignKey);
        typeModel.setForeignKeyInfo(this.foreignKeyInfo);
        typeModel.setRetrieveFKinfos(true);
        typeModel.setFkFilter(this.foreignKeyFilter);
        return typeModel;
    }

    @Override
    protected void onRender(Element parent, int pos) {
        super.onRender(parent, pos);
        final TypeModel typeModel = buildTypeModel();

        final boolean hasForeignKeyFilter = this.foreignKeyFilter != null && this.foreignKeyFilter.trim().length() > 0 ? true
                : false;

        xPath = typeModel.getXpath();
        RpcProxy<PagingLoadResult<ForeignKeyBean>> proxy = new RpcProxy<PagingLoadResult<ForeignKeyBean>>() {

            @Override
            public void load(final Object loadConfig, final AsyncCallback<PagingLoadResult<ForeignKeyBean>> callback) {
                PagingLoadConfig config = (PagingLoadConfig) loadConfig;
                config.set("xml", xml); //$NON-NLS-1$
                config.set("currentXpath", currentXpath); //$NON-NLS-1$
                config.set("dataObject", entityModel.getConceptName()); //$NON-NLS-1$
                service.getForeignKeyList((PagingLoadConfig) loadConfig, typeModel, BrowseRecords.getSession().getAppHeader()
                        .getDatacluster(), hasForeignKeyFilter, getFilterValue(),
                        new SessionAwareAsyncCallback<ItemBasePageLoadResult<ForeignKeyBean>>() {

                            @Override
                            protected void doOnFailure(Throwable caught) {
                                callback.onFailure(caught);
                            }

                            public void onSuccess(ItemBasePageLoadResult<ForeignKeyBean> result) {
                                callback.onSuccess(new BasePagingLoadResult<ForeignKeyBean>(result.getData(), result.getOffset(),
                                        result.getTotalLength()));
                            }

                        });

            }
        };

        RpcProxy<BaseListLoadResult<BaseModel>> proxy1 = new RpcProxy<BaseListLoadResult<BaseModel>>() {

            @Override
            public void load(final Object loadConfig, final AsyncCallback<BaseListLoadResult<BaseModel>> callback) {
                service.getForeignKeyPolymTypeList(typeModel.getForeignkey(), Locale.getLanguage(),
                        new SessionAwareAsyncCallback<List<Restriction>>() {

                            @Override
                            protected void doOnFailure(Throwable caught) {
                                callback.onFailure(caught);
                            }

                            public void onSuccess(List<Restriction> result) {
                                List<BaseModel> list = new ArrayList<BaseModel>();
                                for (Restriction re : result) {
                                    BaseModel model = new BaseModel();
                                    model.set("name", re.getName());//$NON-NLS-1$
                                    model.set("value", re.getValue());//$NON-NLS-1$
                                    list.add(model);
                                }
                                callback.onSuccess(new BaseListLoadResult<BaseModel>(list));
                            }
                        });
            }
        };
        BaseListLoader<ListLoadResult<Restriction>> loader1 = new BaseListLoader<ListLoadResult<Restriction>>(proxy1);
        typeList = new ListStore<BaseModel>(loader1);
        loader = new BasePagingLoader<PagingLoadResult<ModelData>>(proxy);
        loader.setRemoteSort(true);
        final ListStore<ForeignKeyBean> store = new ListStore<ForeignKeyBean>(loader);

        FormPanel panel = new FormPanel();
        panel.setFrame(false);
        panel.setLayout(new FitLayout());
        panel.setHeaderVisible(false);
        panel.setSize(WINDOW_WIDTH, WINDOW_HEIGH);
        panel.setHeaderVisible(false);

        filter.addListener(Events.KeyUp, new Listener<FieldEvent>() {

            public void handleEvent(FieldEvent be) {
                if (be.getKeyCode() == KeyCodes.KEY_UP) {
                    ForeignKeyBean fkBean = relatedRecordGrid.getSelectionModel().getSelectedItem();
                    if (fkBean == null) {
                        relatedRecordGrid.getSelectionModel().select(store.getCount() - 1, true);
                    } else {
                        relatedRecordGrid.getSelectionModel().selectPrevious(false);
                        filter.focus();
                    }
                    return;
                }
                if (be.getKeyCode() == KeyCodes.KEY_DOWN) {
                    ForeignKeyBean fkBean = relatedRecordGrid.getSelectionModel().getSelectedItem();
                    if (fkBean == null) {
                        relatedRecordGrid.getSelectionModel().select(0, true);
                    } else {
                        relatedRecordGrid.getSelectionModel().selectNext(false);
                        filter.focus();
                    }
                    return;
                }
                if (be.getKeyCode() == KeyCodes.KEY_ENTER) {
                    ForeignKeyBean fkBean = relatedRecordGrid.getSelectionModel().getSelectedItem();
                    fkBean.setForeignKeyPath(xPath);
                    fkBean.setDisplayInfo(fkBean.toString() != null ? fkBean.toString() : fkBean.getId());
                    returnCriteriaFK.setCriteriaFK(fkBean);
                    closeOrHideWindow();
                }
                if (be.getKeyCode() == KeyCodes.KEY_LEFT || be.getKeyCode() == KeyCodes.KEY_RIGHT) {
                    return;
                }
                loader.load(0, pageSize);
            }
        });
        filter.setWidth(WINDOW_WIDTH - 80);

        ToolBar toolBar = new ToolBar();

        typeComboBox = new ComboBoxField<BaseModel>();
        typeComboBox.setDisplayField("name"); //$NON-NLS-1$
        typeComboBox.setValueField("value"); //$NON-NLS-1$
        typeComboBox.setStore(typeList);
        typeComboBox.setTriggerAction(TriggerAction.ALL);
        typeComboBox.setEmptyText(MessagesFactory.getMessages().label_select_type());
        typeComboBox.setId("DerivedTypeComboBox"); //$NON-NLS-1$

        typeComboBox.addSelectionChangedListener(new SelectionChangedListener<BaseModel>() {

            @Override
            public void selectionChanged(SelectionChangedEvent<BaseModel> se) {
                String targetType = se.getSelectedItem().get("value").toString();//$NON-NLS-1$
                StringBuffer sb = new StringBuffer();
                for (int i = 0; i < typeModel.getForeignKeyInfo().size(); i++) {
                    sb.append(typeModel.getForeignKeyInfo().get(i));
                    if (i < typeModel.getForeignKeyInfo().size() - 1 && i >= 0) {
                        sb.append(",");//$NON-NLS-1$
                    }
                }
                String fkInfo = sb.toString();
                service.switchForeignKeyType(targetType, typeModel.getForeignkey(), fkInfo, getFilterValue(),
                        new SessionAwareAsyncCallback<ForeignKeyDrawer>() {

                            public void onSuccess(ForeignKeyDrawer fkDrawer) {
                                typeModel.setForeignkey(fkDrawer.getXpathForeignKey());
                                List<String> fkinfo = new ArrayList<String>();
                                if (fkDrawer.getXpathInfoForeignKey() != null) {
                                    String[] foreignKeyList = fkDrawer.getXpathInfoForeignKey().split(","); //$NON-NLS-1$
                                    for (int i = 0; i < foreignKeyList.length; i++)
                                        fkinfo.add(foreignKeyList[i]);
                                }

                                typeModel.setForeignKeyInfo(fkinfo);
                                loader.load(0, pageSize);
                            }
                        });
            }
        });
        toolBar.add(typeComboBox);

        Button filterBtn = new Button();
        filterBtn.setIcon(AbstractImagePrototype.create(Icons.INSTANCE.funnel()));
        filterBtn.setWidth(30);
        filter.setWidth(200);
        typeComboBox.setWidth(WINDOW_WIDTH - 250);
        toolBar.add(filterBtn);
        toolBar.add(filter);
        panel.setTopComponent(toolBar);

        List<ColumnConfig> columns = new ArrayList<ColumnConfig>();
        // build columns by specify store
        final PagingToolBar pageToolBar = new PagingToolBar(pageSize);
        pageToolBar.bind(loader);
        pageToolBar.setEnabled(true);

        // change label display
        boolean retrieveFKinfos = typeModel.isRetrieveFKinfos();
        boolean isDisplayKeyInfo = false;
        if (retrieveFKinfos) {
            List<String> foreignKeyInfo = typeModel.getForeignKeyInfo();
            if (foreignKeyInfo.contains(typeModel.getForeignkey()))
                isDisplayKeyInfo = true;
            for (String info : foreignKeyInfo) {
                columns.add(new ColumnConfig(CommonUtil.getElementFromXpath(info), CommonUtil.getElementFromXpath(info),
                        COLUMN_WIDTH));
            }
        }
        if (columns.size() == 0) {
            columns.add(new ColumnConfig("i", CommonUtil.getElementFromXpath(typeModel.getXpath()), COLUMN_WIDTH)); //$NON-NLS-1$
            isDisplayKeyInfo = true;
        }

        // fix bug TMDM-2829
        if (!isDisplayKeyInfo) {
            ColumnConfig columnConfig = columns.get(0);
            columnConfig.setRenderer(new GridCellRenderer<ForeignKeyBean>() {

                public Object render(final ForeignKeyBean fkBean, String property, ColumnData config, int rowIndex, int colIndex,
                        ListStore<ForeignKeyBean> store, Grid<ForeignKeyBean> grid) {
                    String result = ""; //$NON-NLS-1$
                    if (fkBean != null){
                        if (fkBean.get(property) != null && !"".equals(fkBean.get(property))){ //$NON-NLS-1$
                            result = fkBean.get(property) + "-"; //$NON-NLS-1$
                        }
                        return result = result + fkBean.getId();
                    }
                    return result;               
                }
            });
        }

        ColumnModel cm = new ColumnModel(columns);
        relatedRecordGrid = new Grid<ForeignKeyBean>(store, cm);
        relatedRecordGrid.getView().setForceFit(true);
        relatedRecordGrid.setLoadMask(true);
        relatedRecordGrid.setBorders(false);
        relatedRecordGrid.setStateful(true);
        relatedRecordGrid.setStateId("relatedRecordGrid"); //$NON-NLS-1$
        relatedRecordGrid.addListener(Events.Attach, new Listener<GridEvent<ForeignKeyBean>>() {

            public void handleEvent(GridEvent<ForeignKeyBean> be) {
                PagingLoadConfig config = new BasePagingLoadConfig();
                config.setOffset(0);
                config.setLimit(pageSize);
                loader.load(config);
            }
        });

        relatedRecordGrid.addListener(Events.OnDoubleClick, new Listener<GridEvent<ForeignKeyBean>>() {

            public void handleEvent(final GridEvent<ForeignKeyBean> be) {
                ForeignKeyBean fkBean = be.getModel();
                fkBean.setForeignKeyPath(xPath);
                returnCriteriaFK.setCriteriaFK(fkBean);
                closeOrHideWindow();
            }
        });

        panel.add(relatedRecordGrid);
        panel.setBottomComponent(pageToolBar);

        Button cancelBtn = new Button(MessagesFactory.getMessages().cancel_btn());
        cancelBtn.addSelectionListener(new SelectionListener<ButtonEvent>() {

            @Override
            public void componentSelected(ButtonEvent ce) {
                closeOrHideWindow();
            }
        });
        addButton(cancelBtn);
        add(panel, new FlowData(5));

    }

    public void show(EntityModel entityModel, ItemsDetailPanel detailPanel, String xPath) {
        if (this.foreignKeyFilter != null && this.foreignKeyFilter.trim().length() > 0) {
            ItemPanel itemPanel = (ItemPanel) detailPanel.getFirstTabWidget();
            ItemNodeModel root = (ItemNodeModel) itemPanel.getTree().getRootModel();
            xml = CommonUtil.toXML(root, itemPanel.getViewBean());
        }
        if (xPath != null)
            this.currentXpath = xPath;
        this.setEntityModel(entityModel);
        show();
    }

    protected void closeOrHideWindow() {
        hide(null);
    }

    public void setCurrentXpath(String currentXpath) {
        this.currentXpath = currentXpath;
    }

}
