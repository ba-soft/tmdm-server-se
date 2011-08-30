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
package org.talend.mdm.webapp.browserecords.client.widget;

import java.util.Iterator;
import java.util.List;

import org.talend.mdm.webapp.browserecords.client.BrowseRecords;
import org.talend.mdm.webapp.browserecords.client.BrowseRecordsEvents;
import org.talend.mdm.webapp.browserecords.client.BrowseRecordsServiceAsync;
import org.talend.mdm.webapp.browserecords.client.creator.ItemCreator;
import org.talend.mdm.webapp.browserecords.client.i18n.MessagesFactory;
import org.talend.mdm.webapp.browserecords.client.model.ItemBaseModel;
import org.talend.mdm.webapp.browserecords.client.model.ItemBean;
import org.talend.mdm.webapp.browserecords.client.model.ItemResult;
import org.talend.mdm.webapp.browserecords.client.model.MultipleCriteria;
import org.talend.mdm.webapp.browserecords.client.model.QueryModel;
import org.talend.mdm.webapp.browserecords.client.model.SimpleCriterion;
import org.talend.mdm.webapp.browserecords.client.resources.icon.Icons;
import org.talend.mdm.webapp.browserecords.client.util.Locale;
import org.talend.mdm.webapp.browserecords.client.util.UserSession;
import org.talend.mdm.webapp.browserecords.client.util.ViewUtil;
import org.talend.mdm.webapp.browserecords.client.widget.SearchPanel.AdvancedSearchPanel;
import org.talend.mdm.webapp.browserecords.client.widget.SearchPanel.SimpleCriterionPanel;
import org.talend.mdm.webapp.browserecords.client.widget.inputfield.ComboBoxField;
import org.talend.mdm.webapp.browserecords.shared.EntityModel;
import org.talend.mdm.webapp.browserecords.shared.ViewBean;

import com.extjs.gxt.ui.client.Registry;
import com.extjs.gxt.ui.client.Style.HorizontalAlignment;
import com.extjs.gxt.ui.client.data.BaseListLoader;
import com.extjs.gxt.ui.client.data.ListLoadResult;
import com.extjs.gxt.ui.client.data.ListLoader;
import com.extjs.gxt.ui.client.data.RpcProxy;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.MenuEvent;
import com.extjs.gxt.ui.client.event.MessageBoxEvent;
import com.extjs.gxt.ui.client.event.SelectionChangedEvent;
import com.extjs.gxt.ui.client.event.SelectionChangedListener;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.mvc.Dispatcher;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.Dialog;
import com.extjs.gxt.ui.client.widget.HorizontalPanel;
import com.extjs.gxt.ui.client.widget.MessageBox;
import com.extjs.gxt.ui.client.widget.Window;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.button.ToggleButton;
import com.extjs.gxt.ui.client.widget.form.ComboBox;
import com.extjs.gxt.ui.client.widget.form.FormPanel;
import com.extjs.gxt.ui.client.widget.form.ComboBox.TriggerAction;
import com.extjs.gxt.ui.client.widget.menu.Menu;
import com.extjs.gxt.ui.client.widget.menu.MenuItem;
import com.extjs.gxt.ui.client.widget.toolbar.FillToolItem;
import com.extjs.gxt.ui.client.widget.toolbar.SeparatorToolItem;
import com.extjs.gxt.ui.client.widget.toolbar.ToolBar;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.AbstractImagePrototype;

public class ItemsToolBar extends ToolBar {

    private final static int PAGE_SIZE = 10;

    private boolean isSimple;

    private static String userCluster = null;

    private SimpleCriterionPanel<?> simplePanel;

    private AdvancedSearchPanel advancedPanel;

    private ComboBoxField<ItemBaseModel> entityCombo = new ComboBoxField<ItemBaseModel>();

    public final Button searchBut = new Button(MessagesFactory.getMessages().search_btn());

    private final ToggleButton advancedBut = new ToggleButton(MessagesFactory.getMessages().advsearch_btn());

    private final Button managebookBtn = new Button();

    private final Button bookmarkBtn = new Button();

    private Button createBtn = new Button(MessagesFactory.getMessages().create_btn());

    private Button menu = new Button(MessagesFactory.getMessages().delete_btn());

    private Button uploadBtn = new Button(MessagesFactory.getMessages().itemsBrowser_Import_Export());

    private BrowseRecordsServiceAsync service = (BrowseRecordsServiceAsync) Registry.get(BrowseRecords.BROWSERECORDS_SERVICE);

    private ItemsToolBar instance = this;

    private List<ItemBaseModel> userCriteriasList;

    private ListStore<ItemBaseModel> tableList = new ListStore<ItemBaseModel>();

    private boolean advancedPanelVisible = false;

    private boolean bookmarkShared = false;

    private String bookmarkName = null;

    private ItemBaseModel currentModel = null;

    private ComboBox<ItemBaseModel> combo = null;

    private ViewBean tableView;

    /*************************************/

    public ItemsToolBar() {
        // init user saved model
        userCluster = BrowseRecords.getSession().getAppHeader().getDatacluster();
        this.setBorders(false);
        initToolBar();
    }

    public void setQueryModel(QueryModel qm) {
        qm.setDataClusterPK(userCluster);
        qm.setView(BrowseRecords.getSession().getCurrentView());
        qm.setModel(BrowseRecords.getSession().getCurrentEntityModel());
        if (isSimple) {
            SimpleCriterion simpCriterion = simplePanel.getCriteria();
            MultipleCriteria criteriaStore = (MultipleCriteria) BrowseRecords.getSession().get(
                    UserSession.CUSTOMIZE_CRITERION_STORE);
            if (criteriaStore == null) {
                criteriaStore = new MultipleCriteria();
                criteriaStore.setOperator("AND"); //$NON-NLS-1$
            } else {
                BrowseRecords.getSession().getCustomizeCriterionStore().getChildren().clear();
            }
            criteriaStore.add(simpCriterion);
            BrowseRecords.getSession().put(UserSession.CUSTOMIZE_CRITERION_STORE, criteriaStore);
            qm.setCriteria(simplePanel.getCriteria().toString());
        } else
            qm.setCriteria(advancedPanel.getCriteria());
    }

    public void updateToolBar(ViewBean viewBean) {
        simplePanel.updateFields(viewBean);
        if (advancedPanel != null) {
            advancedPanel.setView(viewBean);
            advancedPanel.cleanCriteria();
        }
        // reset search results
        ItemsListPanel list = (ItemsListPanel) instance.getParent();
        if (list != null)
            list.resetGrid();

        searchBut.setEnabled(true);
        advancedBut.setEnabled(true);
        managebookBtn.setEnabled(true);
        bookmarkBtn.setEnabled(true);

        createBtn.setEnabled(false);
        menu.setEnabled(false);
        String concept = ViewUtil.getConceptFromBrowseItemView(entityCombo.getValue().get("value").toString());//$NON-NLS-1$
        if (!viewBean.getBindingEntityModel().getMetaDataTypes().get(concept).isDenyCreatable())
            createBtn.setEnabled(true);
        boolean denyLogicalDelete = viewBean.getBindingEntityModel().getMetaDataTypes().get(concept).isDenyLogicalDeletable();
        boolean denyPhysicalDelete = viewBean.getBindingEntityModel().getMetaDataTypes().get(concept).isDenyPhysicalDeleteable();

        if (denyLogicalDelete && denyPhysicalDelete)
            menu.setEnabled(false);
        else {
            menu.setEnabled(true);
            if (denyPhysicalDelete)
                menu.getMenu().getItemByItemId("physicalDelMenuInGrid").setEnabled(false); //$NON-NLS-1$
            else
                menu.getMenu().getItemByItemId("physicalDelMenuInGrid").setEnabled(true); //$NON-NLS-1$
            if (denyLogicalDelete)
                menu.getMenu().getItemByItemId("logicalDelMenuInGrid").setEnabled(false); //$NON-NLS-1$
            else
                menu.getMenu().getItemByItemId("logicalDelMenuInGrid").setEnabled(true); //$NON-NLS-1$
        }

        uploadBtn.setEnabled(false);
        boolean denyUploadFile = viewBean.getBindingEntityModel().getMetaDataTypes().get(concept).isDenyLogicalDeletable();

        if (denyUploadFile)
            uploadBtn.setEnabled(false);
        else {
            uploadBtn.setEnabled(true);
        }

        updateUserCriteriasList();
    }

    public int getSuccessItemsNumber(List<ItemResult> results) {
        int itemSuccessNumber = 0;
        for (ItemResult result : results) {
            if (result.getStatus() == ItemResult.SUCCESS) {
                itemSuccessNumber++;
            }
        }
        return itemSuccessNumber;
    }

    public int getFailureItemsNumber(List<ItemResult> results) {
        int itemFailureNumber = 0;
        for (ItemResult result : results) {
            if (result.getStatus() == ItemResult.FAILURE) {
                itemFailureNumber++;
            }
        }
        return itemFailureNumber;
    }

    public int getSelectItemNumber() {
        int number = 0;
        ItemsListPanel list = (ItemsListPanel) instance.getParent();
        number = list.getGrid().getSelectionModel().getSelectedItems().size();
        return number;
    }

    private void initToolBar() {
        createBtn.setIcon(AbstractImagePrototype.create(Icons.INSTANCE.Create()));
        createBtn.setEnabled(false);
        add(createBtn);
        createBtn.addSelectionListener(new SelectionListener<ButtonEvent>() {

            @Override
            public void componentSelected(ButtonEvent ce) {
                String concept = ViewUtil.getConceptFromBrowseItemView(entityCombo.getValue().get("value").toString());//$NON-NLS-1$

                EntityModel entityModel = (EntityModel) BrowseRecords.getSession().getCurrentEntityModel();
                ItemBean item = ItemCreator.createDefaultItemBean(concept, entityModel);

                // TODO
                // AppEvent evt = new AppEvent(ItemsEvents.ViewItemForm, item);
                // evt.setData(ItemsView.ITEMS_FORM_TARGET, ItemsView.TARGET_IN_NEW_TAB);
                // Dispatcher.forwardEvent(evt);
            }

        });

        menu.setIcon(AbstractImagePrototype.create(Icons.INSTANCE.Delete()));
        Menu sub = new Menu();
        MenuItem delMenu = new MenuItem(MessagesFactory.getMessages().delete_btn());
        delMenu.setId("physicalDelMenuInGrid");//$NON-NLS-1$
        delMenu.setIcon(AbstractImagePrototype.create(Icons.INSTANCE.Delete()));

        // TODO duplicate with recordToolbar
        delMenu.addSelectionListener(new SelectionListener<MenuEvent>() {

            @Override
            public void componentSelected(MenuEvent ce) {

                if (((ItemsListPanel) instance.getParent()).getGrid() == null) {
                    com.google.gwt.user.client.Window.alert(MessagesFactory.getMessages().select_delete_item_record());
                } else {
                    if (getSelectItemNumber() == 0) {
                        com.google.gwt.user.client.Window.alert(MessagesFactory.getMessages().select_delete_item_record());
                    } else {
                        MessageBox.confirm(MessagesFactory.getMessages().confirm_title(), MessagesFactory.getMessages()
                                .delete_confirm(), new Listener<MessageBoxEvent>() {

                            final ItemsListPanel list = (ItemsListPanel) instance.getParent();

                            public void handleEvent(MessageBoxEvent be) {
                                if (be.getButtonClicked().getItemId().equals(Dialog.YES)) {
                                    if (list.getGrid() != null) {
                                        service.deleteItemBeans(list.getGrid().getSelectionModel().getSelectedItems(),
                                                new AsyncCallback<List<ItemResult>>() {

                                                    public void onFailure(Throwable caught) {
                                                        Dispatcher.forwardEvent(BrowseRecordsEvents.Error, caught);
                                                    }

                                                    public void onSuccess(List<ItemResult> results) {
                                                        StringBuffer msgs = new StringBuffer();

                                                        int successNum = getSuccessItemsNumber(results);
                                                        int failureNum = getFailureItemsNumber(results);

                                                        if (successNum == 1 && failureNum == 0) {
                                                            String msg = results.iterator().next().getDescription();
                                                            MessageBox.info(MessagesFactory.getMessages().info_title(),
                                                                    pickOutISOMessage(msg.toString()), null);
                                                        } else if (successNum > 1 && failureNum == 0) {
                                                            msgs.append(MessagesFactory.getMessages().delete_item_record_success(
                                                                    successNum));
                                                            MessageBox.info(MessagesFactory.getMessages().info_title(),
                                                                    msgs.toString(), null);
                                                        } else if (successNum == 0 && failureNum == 1) {
                                                            String msg = results.iterator().next().getDescription();
                                                            MessageBox.alert(MessagesFactory.getMessages().error_title(),
                                                                    pickOutISOMessage(msg), null);
                                                        } else if (successNum == 0 && failureNum > 1) {
                                                            msgs.append(MessagesFactory.getMessages().delete_item_record_failure(
                                                                    failureNum));
                                                            MessageBox.alert(MessagesFactory.getMessages().error_title(),
                                                                    msgs.toString(), null);
                                                        } else if (successNum > 0 && failureNum > 0) {
                                                            msgs.append(MessagesFactory.getMessages().delete_item_record_success(
                                                                    successNum)
                                                                    + "\n");//$NON-NLS-1$
                                                            msgs.append(MessagesFactory.getMessages().delete_item_record_failure(
                                                                    failureNum)
                                                                    + "\n");//$NON-NLS-1$
                                                            MessageBox.info(MessagesFactory.getMessages().info_title(),
                                                                    msgs.toString(), null);
                                                        }

                                                        list.getStore().getLoader().load();
                                                    }

                                                });
                                    }

                                }
                            }

                            private String pickOutISOMessage(String message) {
                                String identy = "[" + Locale.getLanguage()//$NON-NLS-1$
                                        .toUpperCase() + ":";//$NON-NLS-1$
                                int mask = message.indexOf(identy);
                                if (mask != -1) {
                                    String snippet = message.substring(mask + identy.length());
                                    if (!snippet.isEmpty()) {
                                        String pickOver = "";//$NON-NLS-1$
                                        boolean enclosed = false;
                                        for (int j = 0; j < snippet.trim().length(); j++) {
                                            String c = snippet.trim().charAt(j) + "";//$NON-NLS-1$
                                            if ("]".equals(c)) {//$NON-NLS-1$
                                                if (!pickOver.isEmpty()) {
                                                    enclosed = true;
                                                    break;
                                                }
                                            } else {
                                                pickOver += c;
                                            }
                                        }

                                        if (enclosed)
                                            return pickOver;
                                    }
                                }
                                return message;
                            }
                        });
                    }
                }
            }
        });

        MenuItem trashMenu = new MenuItem(MessagesFactory.getMessages().trash_btn());
        trashMenu.setId("logicalDelMenuInGrid");//$NON-NLS-1$
        trashMenu.setIcon(AbstractImagePrototype.create(Icons.INSTANCE.Send_to_trash()));
        trashMenu.addSelectionListener(new SelectionListener<MenuEvent>() {

            @Override
            public void componentSelected(MenuEvent ce) {
                final MessageBox box = MessageBox.prompt(MessagesFactory.getMessages().path(), MessagesFactory.getMessages()
                        .path_desc(), new Listener<MessageBoxEvent>() {

                    public void handleEvent(MessageBoxEvent be) {
                        if (be.getButtonClicked().getItemId().equals(Dialog.OK)) {
                            final ItemsListPanel list = (ItemsListPanel) instance.getParent();
                            if (list.getGrid() != null) {
                                service.logicalDeleteItems(list.getGrid().getSelectionModel().getSelectedItems(), "/", //$NON-NLS-1$
                                        new AsyncCallback<List<ItemResult>>() {

                                            public void onFailure(Throwable caught) {
                                                Dispatcher.forwardEvent(BrowseRecordsEvents.Error, caught);
                                            }

                                            public void onSuccess(List<ItemResult> results) {
                                                for (ItemResult result : results) {
                                                    if (result.getStatus() == ItemResult.FAILURE) {
                                                        MessageBox.alert(MessagesFactory.getMessages().error_title(),
                                                                result.getDescription(), null);
                                                        return;
                                                    }
                                                }
                                                list.getStore().getLoader().load();
                                            }

                                        });

                            }
                        }
                    }
                });
                box.getTextBox().setValue("/"); //$NON-NLS-1$
            }
        });

        sub.add(trashMenu);
        sub.add(delMenu);

        menu.setMenu(sub);
        menu.setEnabled(false);
        add(menu);

        uploadBtn.setIcon(AbstractImagePrototype.create(Icons.INSTANCE.Save()));
        uploadBtn.setId("uploadMenuInGrid"); //$NON-NLS-1$

        uploadBtn.addSelectionListener(new SelectionListener<ButtonEvent>() {

            @Override
            public void componentSelected(ButtonEvent ce) {
                // TODO
            }
        });

        uploadBtn.setEnabled(false);
        add(uploadBtn);

        add(new FillToolItem());

        // add entity combo
        RpcProxy<List<ItemBaseModel>> Entityproxy = new RpcProxy<List<ItemBaseModel>>() {

            @Override
            public void load(Object loadConfig, AsyncCallback<List<ItemBaseModel>> callback) {
                service.getViewsList(Locale.getLanguage(), callback);
            }
        };

        if (BrowseRecords.getSession().getEntitiyModelList() == null) {
            service.getViewsList(Locale.getLanguage(),
                    new AsyncCallback<List<ItemBaseModel>>() {

                        public void onSuccess(List<ItemBaseModel> modelList) {
                    BrowseRecords.getSession().put(UserSession.ENTITY_MODEL_LIST, modelList);
                        }

                        public void onFailure(Throwable caught) {
                    Dispatcher.forwardEvent(BrowseRecordsEvents.Error, caught);
                        }
                    });
        }

        ListLoader<ListLoadResult<ItemBaseModel>> Entityloader = new BaseListLoader<ListLoadResult<ItemBaseModel>>(Entityproxy);

        HorizontalPanel entityPanel = new HorizontalPanel();
        final ListStore<ItemBaseModel> list = new ListStore<ItemBaseModel>(Entityloader);

        entityCombo.setAutoWidth(true);
        entityCombo.setEmptyText(MessagesFactory.getMessages().empty_entity());
        entityCombo.setLoadingText(MessagesFactory.getMessages().loading());
        entityCombo.setStore(list);
        entityCombo.setDisplayField("name");//$NON-NLS-1$
        entityCombo.setValueField("value");//$NON-NLS-1$
        entityCombo.setForceSelection(true);
        entityCombo.setTriggerAction(TriggerAction.ALL);
        entityCombo.setId("EntityComboBox");//$NON-NLS-1$
        entityCombo.setStyleAttribute("padding-right", "17px"); //$NON-NLS-1$ //$NON-NLS-2$

        entityCombo.addSelectionChangedListener(new SelectionChangedListener<ItemBaseModel>() {

            @Override
            public void selectionChanged(SelectionChangedEvent<ItemBaseModel> se) {
                String viewPk = se.getSelectedItem().get("value").toString();//$NON-NLS-1$
                Dispatcher.forwardEvent(BrowseRecordsEvents.GetView, viewPk);
            }

        });
        entityPanel.add(entityCombo);
        add(entityPanel);
        simplePanel = new SimpleCriterionPanel(null, null, searchBut);
        add(simplePanel);

        // add simple search button
        searchBut.setEnabled(false);
        searchBut.addSelectionListener(new SelectionListener<ButtonEvent>() {

            @Override
            public void componentSelected(ButtonEvent ce) {
                if (simplePanel.getCriteria() != null) {
                    isSimple = true;
                    String viewPk = entityCombo.getValue().get("value");//$NON-NLS-1$
                    Dispatcher.forwardEvent(BrowseRecordsEvents.SearchView, viewPk);
                    resizeAfterSearch();
                } else {
                    MessageBox.alert(MessagesFactory.getMessages().error_title(), MessagesFactory.getMessages()
                            .advsearch_lessinfo(), new Listener<MessageBoxEvent>() {

                        public void handleEvent(MessageBoxEvent be) {
                            simplePanel.focusField();
                        }
                    });
                }
            }

        });
        add(searchBut);

        add(new SeparatorToolItem());

        // add advanced search button
        advancedBut.setEnabled(false);
        advancedBut.addSelectionListener(new SelectionListener<ButtonEvent>() {

            @Override
            public void componentSelected(ButtonEvent ce) {
                // show advanced Search panel
                advancedPanelVisible = !advancedPanelVisible;
                advancedPanel.setVisible(advancedPanelVisible);
                advancedPanel.getButtonBar().getItemByItemId("updateBookmarkBtn").setVisible(false); //$NON-NLS-1$

                if (((ItemsListPanel) instance.getParent()).gridContainer != null)
                    ((ItemsListPanel) instance.getParent()).gridContainer.setHeight(instance.getParent().getOffsetHeight()
                            - instance.getOffsetHeight() - advancedPanel.getOffsetHeight());
                if (isSimple) {
                    MultipleCriteria criteriaStore = (MultipleCriteria) BrowseRecords.getSession().get(
                            UserSession.CUSTOMIZE_CRITERION_STORE);
                    criteriaStore.requestShowAppearance();
                    advancedPanel.setCriteria(criteriaStore.toString());
                    //  advancedPanel.setCriteria("((" + simplePanel.getCriteria().toString() + "))"); //$NON-NLS-1$ //$NON-NLS-2$
                    //                    advancedPanel.setCriteriaAppearance("((" + simplePanel.getCriteria().toString() + "))", "((" //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
                    //                            + simplePanel.getCriteria().toAppearanceString() + "))"); //$NON-NLS-1$
                }

            }

        });
        add(advancedBut);

        add(new SeparatorToolItem());

        // add bookmark management button
        managebookBtn.setIcon(AbstractImagePrototype.create(Icons.INSTANCE.Display()));
        managebookBtn.setTitle(MessagesFactory.getMessages().bookmarkmanagement_heading());
        managebookBtn.setEnabled(false);
        managebookBtn.addSelectionListener(new SelectionListener<ButtonEvent>() {

            @Override
            public void componentSelected(ButtonEvent ce) {
                // TODO
            }

        });
        add(managebookBtn);

        // add bookmark save button
        bookmarkBtn.setIcon(AbstractImagePrototype.create(Icons.INSTANCE.Save()));
        bookmarkBtn.setTitle(MessagesFactory.getMessages().advsearch_bookmark());
        bookmarkBtn.setEnabled(false);
        bookmarkBtn.addSelectionListener(new SelectionListener<ButtonEvent>() {

            @Override
            public void componentSelected(ButtonEvent ce) {
                // TODO
                // showBookmarkSavedWin(true);
            }

        });
        add(bookmarkBtn);

        initAdvancedPanel();
    }

    private void updateUserCriteriasList() {
        service.getUserCriterias(entityCombo.getValue().get("value").toString(), //$NON-NLS-1$
                new AsyncCallback<List<ItemBaseModel>>() {

                    public void onFailure(Throwable caught) {
                        Dispatcher.forwardEvent(BrowseRecordsEvents.Error, caught);
                    }

                    public void onSuccess(List<ItemBaseModel> list) {
                        userCriteriasList = list;
                    }

                });
    }

    private boolean ifManage(ItemBaseModel model) {
        // only the shared bookmark could be managed
        Iterator<ItemBaseModel> i = userCriteriasList.iterator();
        while (i.hasNext()) {
            if ((i.next()).get("value").equals( //$NON-NLS-1$
                    model.get("value").toString())) { //$NON-NLS-1$    
                return true;
            }
        }

        return false;
    }

    private void doSearch(final ItemBaseModel model, final Window winBookmark) {
        service.getCriteriaByBookmark(model.get("value").toString(), //$NON-NLS-1$
                new AsyncCallback<String>() {

                    public void onFailure(Throwable caught) {
                        Dispatcher.forwardEvent(BrowseRecordsEvents.Error, caught);
                    }

                    public void onSuccess(String arg0) {
                        isSimple = false;
                        if (advancedPanel == null) {
                            advancedPanel = new AdvancedSearchPanel(simplePanel.getView(), null);
                        }
                        advancedPanel.setCriteria(arg0);
                        String viewPk = entityCombo.getValue().get("value"); //$NON-NLS-1$
                        Dispatcher.forwardEvent(BrowseRecordsEvents.SearchView, viewPk);
                        winBookmark.close();
                    }

                });

    }

    public FormPanel getAdvancedPanel() {
        return advancedPanel;
    }

    private void resizeAfterSearch() {
        advancedPanelVisible = false;
        advancedPanel.setVisible(advancedPanelVisible);
        advancedBut.toggle(advancedPanelVisible);
        // resize result grid
        if (((ItemsListPanel) instance.getParent()).gridContainer != null)
            ((ItemsListPanel) instance.getParent()).gridContainer.setHeight(instance.getParent().getOffsetHeight()
                    - instance.getOffsetHeight() - advancedPanel.getOffsetHeight());
    }

    private void initAdvancedPanel() {
        if (advancedPanel == null) {
            Button searchBtn = new Button(MessagesFactory.getMessages().search_btn());
            advancedPanel = new AdvancedSearchPanel(simplePanel.getView(), searchBtn);
            advancedPanel.setItemId("advancedPanel"); //$NON-NLS-1$
            advancedPanel.setButtonAlign(HorizontalAlignment.CENTER);

            searchBtn.setItemId("searchBtn"); //$NON-NLS-1$
            searchBtn.addSelectionListener(new SelectionListener<ButtonEvent>() {

                @Override
                public void componentSelected(ButtonEvent ce) {
                    if (advancedPanel.getCriteria() == null || advancedPanel.getCriteria().equals("")) //$NON-NLS-1$
                        MessageBox.alert(MessagesFactory.getMessages().warning_title(), MessagesFactory.getMessages()
                                .search_expression_notempty(), null);
                    else {
                        isSimple = false;
                        String viewPk = entityCombo.getValue().get("value"); //$NON-NLS-1$
                        Dispatcher.forwardEvent(BrowseRecordsEvents.SearchView, viewPk);
                        resizeAfterSearch();
                    }
                }

            });
            advancedPanel.addButton(searchBtn);

            Button advancedBookmarkBtn = new Button(MessagesFactory.getMessages().advsearch_bookmark());
            advancedBookmarkBtn.setItemId("advancedBookmarkBtn"); //$NON-NLS-1$
            advancedBookmarkBtn.addSelectionListener(new SelectionListener<ButtonEvent>() {

                @Override
                public void componentSelected(ButtonEvent ce) {
                    // TODO
                    // showBookmarkSavedWin(false);
                }

            });
            advancedPanel.addButton(advancedBookmarkBtn);

            Button updateBookmarkBtn = new Button(MessagesFactory.getMessages().bookmark_update());
            updateBookmarkBtn.setItemId("updateBookmarkBtn"); //$NON-NLS-1$
            updateBookmarkBtn.setVisible(false);
            updateBookmarkBtn.addSelectionListener(new SelectionListener<ButtonEvent>() {

                @Override
                public void componentSelected(ButtonEvent ce) {
                    // TODO
                    // saveBookmark(bookmarkName, bookmarkShared, advancedPanel.getCriteria(), null);
                }

            });
            advancedPanel.addButton(updateBookmarkBtn);

            Button cancelBtn = new Button(MessagesFactory.getMessages().button_reset());
            cancelBtn.setItemId("cancelBtn"); //$NON-NLS-1$
            cancelBtn.addSelectionListener(new SelectionListener<ButtonEvent>() {

                @Override
                public void componentSelected(ButtonEvent ce) {
                    advancedPanel.cleanCriteria();

                    if (((ItemsListPanel) instance.getParent()).gridContainer != null)
                        ((ItemsListPanel) instance.getParent()).gridContainer.setHeight(instance.getParent().getOffsetHeight()
                                - instance.getOffsetHeight() - advancedPanel.getOffsetHeight());
                }

            });
            advancedPanel.addButton(cancelBtn);
            advancedPanel.setVisible(false);
        }
    }

    public void addOption(ItemBaseModel model) {
        tableList.add(model);
        combo.setStore(tableList);

        combo.setValue(model);
    }

    public ItemBaseModel getCurrentModel() {
        return currentModel;
    }

    public void setCurrentModel(ItemBaseModel currentModel) {
        this.currentModel = currentModel;
    }

    public ViewBean getTableView() {
        return tableView;
    }

    public void setTableView(ViewBean tableView) {
        this.tableView = tableView;
    }
}
