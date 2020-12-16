/*
 * Copyright (C) 2006-2019 Talend Inc. - www.talend.com
 *
 * This source code is available under agreement available at
 * %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
 *
 * You should have received a copy of the agreement along with this program; if not, write to Talend SA 9 rue Pages
 * 92150 Suresnes, France
 */
package org.talend.mdm.webapp.browserecords.client.widget;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import org.talend.mdm.webapp.browserecords.client.BrowseRecords;
import org.talend.mdm.webapp.browserecords.client.model.BreadCrumbModel;
import org.talend.mdm.webapp.browserecords.client.model.ForeignKeyTabModel;
import org.talend.mdm.webapp.browserecords.client.model.ItemBean;
import org.talend.mdm.webapp.browserecords.client.model.ItemNodeModel;
import org.talend.mdm.webapp.browserecords.client.mvc.BrowseRecordsView;
import org.talend.mdm.webapp.browserecords.client.widget.ItemsDetailPanel.ItemDetailTabPanelContentHandle;
import org.talend.mdm.webapp.browserecords.client.widget.treedetail.ForeignKeyRender;
import org.talend.mdm.webapp.browserecords.client.widget.treedetail.ForeignKeyUtil;
import org.talend.mdm.webapp.browserecords.client.widget.treedetail.ForeignKeyTreeDetail;
import org.talend.mdm.webapp.browserecords.client.widget.treedetail.TreeDetailUtil;
import org.talend.mdm.webapp.browserecords.client.i18n.MessagesFactory;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Widget;
import com.extjs.gxt.ui.client.widget.TabItem;
import com.extjs.gxt.ui.client.widget.MessageBox;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.MessageBoxEvent;
import com.extjs.gxt.ui.client.widget.Dialog;

public class BreadCrumb extends Composite {

    private BreadCrumbBar pWidget = new BreadCrumbBar();

    private ItemsDetailPanel itemsDetailPanel;

    private ItemDetailToolBar toolBar;

    private ItemNodeModel root;
    
    private boolean isCachedItem = false;

    public static String DEFAULTNAME = "Talend MDM"; //$NON-NLS-1$

    private int viewCode;

    public BreadCrumb(List<BreadCrumbModel> list, ItemsDetailPanel itemsDetailPanel) {
        this.itemsDetailPanel = itemsDetailPanel;

        boolean isFirst = true;
        for (BreadCrumbModel bcm : list) {

            HTML h = initBreadCrumb(bcm.getConcept(), bcm.getLabel(), bcm.getIds(), bcm.getPkInfo(), bcm.isIfLink(), isFirst);
            pWidget.add(h);
            isFirst = false;
        }

        initWidget(pWidget);
    }

    public BreadCrumb(List<BreadCrumbModel> list, ItemsDetailPanel itemsDetailPanel, int viewCode) {
        this(list, itemsDetailPanel);
        this.viewCode = viewCode;
    }

    public void appendBreadCrumb(String concept, String label, String ids, String pkInfo) {
        if (pWidget != null) {
            String title;
            if (label != null) {
                if (ids != null) {
                    title = label + " " + ids; //$NON-NLS-1$
                } else {
                    title = label;
                }
            } else {
                title = ids;
            }
            HTML tmph = new HTML("&nbsp;&gt;&nbsp;<a>" + title + "</a><input value=\"" + concept + "\"' type=\"hidden\">");//$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
            if (pWidget.getWidget(pWidget.getWidgetCount() - 1).getElement().getInnerHTML().equals(tmph.getHTML())) {
                return;
            }
            HTML h = initBreadCrumb(concept, label, ids, pkInfo, true, false);
            pWidget.add(h);
        }
    }

    private HTML initBreadCrumb(final String concept, final String label, final String ids, String pkInfo, boolean ifLink,
            boolean isFirst) {
        HTML h = null;
        String title;
        if (label != null) {
            if (ids != null) {
                title = label + " " + ids; //$NON-NLS-1$
            } else {
                title = label;
            }
        } else {
            title = ids;
        }
        if (ifLink) {
            h = new HTML("&nbsp;&gt;&nbsp;<a>" + title + "</a><input value=\"" + concept + "\"' type=\"hidden\">");//$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
            h.addClickHandler(new ClickHandler() {

                @Override
                public void onClick(ClickEvent event) {
                    if (isCurrentRecordChange()) {
                        MessageBox msgBox = MessageBox.confirm(MessagesFactory.getMessages().confirm_title(),
                                org.talend.mdm.webapp.base.client.i18n.BaseMessagesFactory.getMessages().msg_confirm_save_tree_detail(root.getLabel()),
                                new Listener<MessageBoxEvent>() {

                                    @Override
                                    public void handleEvent(MessageBoxEvent be) {
                                        if (Dialog.YES.equals(be.getButtonClicked().getItemId())) {
                                            toolBar.saveItemAndClose(false);
                                        }
                                        displayCachedEntity(concept, label, ids);
                                    }
                                });
                        msgBox.getDialog().setWidth(550);
                    } else {
                    	displayCachedEntity(concept, label, ids);
                    }
                    if (pWidget != null) {
                        HTML clickedHtml = (HTML) event.getSource();
                        pWidget.removeNeedless(clickedHtml);
                    }
                }

            });

        } else {
            h = new HTML((isFirst ? "" : "&nbsp;&gt;&nbsp;") + "<font>" + title + "</font>"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
        }
        h.getElement().setAttribute("titleText", title); //$NON-NLS-1$
        h.setWordWrap(false);
        return h;
    }

    private boolean isCurrentRecordChange() {
        TabItem tabItem = ItemsMainTabPanel.getInstance().getItemByItemId(BrowseRecordsView.DEFAULT_ITEMVIEW);
        if (tabItem != null) {
        	if (!isCachedItem) {
        		itemsDetailPanel = (ItemsDetailPanel) tabItem.getItemByItemId(BrowseRecordsView.DEFAULT_ITEMVIEW);
        	}
            if (itemsDetailPanel != null && itemsDetailPanel.getFirstTabWidget() != null) {
                Widget widget = itemsDetailPanel.getFirstTabWidget();
                if (widget instanceof ForeignKeyTreeDetail) {
                    final ForeignKeyTreeDetail fkTree = (ForeignKeyTreeDetail) widget;
                    toolBar = fkTree.getToolBar();
                    root = fkTree.getRootModel();
                } else {
                    ItemPanel itemPanel = (ItemPanel) widget;
                    toolBar = itemPanel.getToolBar();
                    root = itemPanel.getTree().getRootModel();
                }
                return root != null ? TreeDetailUtil.isChangeValue(root) : false;
            }
        }
        return false;
    }

    private void displayCachedEntity(String concept, String label, String ids) {
        HashMap<String, ItemPanel> map = BrowseRecords.getSession().getCurrentCachedEntity();
        String key = concept + (ids == null ? "" : ids) + itemsDetailPanel.isOutMost(); //$NON-NLS-1$
        String panelKey = ((this.viewCode == BrowseRecordsView.LINEAGE_VIEW_CODE) ? BrowseRecordsView.LINEAGE_ITEMVIEW : "") + key; //$NON-NLS-1$
        if (map != null && map.containsKey(panelKey)) {
            ItemPanel itemPanel = map.get(panelKey);
            if (itemPanel != null) {
                itemsDetailPanel.clearContent();
                itemsDetailPanel.clearBanner();
                ItemBean itemBean = itemPanel.getItem();
                isCachedItem = true;
                if (ids != null && ids.trim().length() > 0) { // Saved record
                    itemsDetailPanel.initBanner(itemBean.getPkInfoList(), itemBean.getDescription());
                    itemsDetailPanel.addTabItem(itemBean.getLabel(), itemPanel, ItemsDetailPanel.SINGLETON, itemBean.getIds());
                    itemsDetailPanel.setTreeDetail(itemPanel.getTree());
                } else { // Created record
                    List<String> pkInfoList = new ArrayList<String>();
                    pkInfoList.add(label);
                    itemsDetailPanel.initBanner(pkInfoList, itemBean.getDescription());
                    List<BreadCrumbModel> breads = new ArrayList<BreadCrumbModel>();
                    breads.add(new BreadCrumbModel("", BreadCrumb.DEFAULTNAME, null, null, false)); //$NON-NLS-1$
                    breads.add(new BreadCrumbModel(concept, label, null, null, true));
                    itemsDetailPanel.initBreadCrumb(new BreadCrumb(breads, itemsDetailPanel));
                    itemsDetailPanel
                            .addTabItem(itemBean.getLabel(), itemPanel, ItemsDetailPanel.SINGLETON, itemBean.getConcept());
                    itemsDetailPanel.setTreeDetail(itemPanel.getTree());
                }

                // FK rendering
                HashMap<String, LinkedHashMap<String, ForeignKeyTabModel>> fkMap = BrowseRecords.getSession()
                        .getCurrentCachedFKTabs();
                if (fkMap != null && fkMap.containsKey(key)) {
                    LinkedHashMap<String, ForeignKeyTabModel> fkTabMap = fkMap.get(key);
                    if (fkTabMap != null) {
                        ForeignKeyRender render = itemPanel.getTree().getFkRender();
                        for (String fkTitle : fkTabMap.keySet()) {
                            ForeignKeyTabModel fkTab = fkTabMap.get(fkTitle);
                            ItemDetailTabPanelContentHandle handle = itemsDetailPanel.addTabItem(fkTab.getFkTabTitle(),
                                    fkTab.getFkTabPanel(), ItemsDetailPanel.MULTIPLE, GWT.getModuleName() + DOM.createUniqueId());
                            render.setRelationFk(fkTab.getFkParentModel(), handle);
                            itemsDetailPanel.addFkHandler(fkTab.getFkTabPanel(), fkTab.getHandler());
                        }
                    }
                }
            }
        } else if (ids != null && ids.trim().length() > 0) {
            ForeignKeyUtil.displayForeignKey(false, concept, ids, itemsDetailPanel);
        }

    }

    public void adjust() {
        pWidget.adjust();
    }
}
