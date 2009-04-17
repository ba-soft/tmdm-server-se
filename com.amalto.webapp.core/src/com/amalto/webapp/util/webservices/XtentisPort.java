// This class was generated by the JAXRPC SI, do not edit.
// Contents subject to change without notice.
// JAX-RPC Standard Implementation (1.1.2_01, construire R40)
// Generated source version: 1.1.2

package com.amalto.webapp.util.webservices;

public interface XtentisPort extends java.rmi.Remote {
    public com.amalto.webapp.util.webservices.WSVersion getComponentVersion(com.amalto.webapp.util.webservices.WSGetComponentVersion wsGetComponentVersion) throws 
         java.rmi.RemoteException;
    public com.amalto.webapp.util.webservices.WSString ping(com.amalto.webapp.util.webservices.WSPing wsPing) throws 
         java.rmi.RemoteException;
    public com.amalto.webapp.util.webservices.WSString logout(com.amalto.webapp.util.webservices.WSLogout wsLogout) throws 
         java.rmi.RemoteException;
    public com.amalto.webapp.util.webservices.WSInt initMDM(com.amalto.webapp.util.webservices.WSInitData initData) throws 
         java.rmi.RemoteException;
    public com.amalto.webapp.util.webservices.WSDataModelPKArray getDataModelPKs(com.amalto.webapp.util.webservices.WSRegexDataModelPKs regexp) throws 
         java.rmi.RemoteException;
    public com.amalto.webapp.util.webservices.WSDataModel getDataModel(com.amalto.webapp.util.webservices.WSGetDataModel wsDataModelget) throws 
         java.rmi.RemoteException;
    public com.amalto.webapp.util.webservices.WSBoolean existsDataModel(com.amalto.webapp.util.webservices.WSExistsDataModel wsDataModelExists) throws 
         java.rmi.RemoteException;
    public com.amalto.webapp.util.webservices.WSDataModelPK putDataModel(com.amalto.webapp.util.webservices.WSPutDataModel wsDataModel) throws 
         java.rmi.RemoteException;
    public com.amalto.webapp.util.webservices.WSDataModelPK deleteDataModel(com.amalto.webapp.util.webservices.WSDeleteDataModel wsDeleteDataModel) throws 
         java.rmi.RemoteException;
    public com.amalto.webapp.util.webservices.WSString checkSchema(com.amalto.webapp.util.webservices.WSCheckSchema wsSchema) throws 
         java.rmi.RemoteException;
    public com.amalto.webapp.util.webservices.WSString deleteBusinessConcept(com.amalto.webapp.util.webservices.WSDeleteBusinessConcept wsDeleteBusinessConcept) throws 
         java.rmi.RemoteException;
    public com.amalto.webapp.util.webservices.WSStringArray getBusinessConcepts(com.amalto.webapp.util.webservices.WSGetBusinessConcepts wsGetBusinessConcepts) throws 
         java.rmi.RemoteException;
    public com.amalto.webapp.util.webservices.WSString putBusinessConcept(com.amalto.webapp.util.webservices.WSPutBusinessConcept wsPutBusinessConcept) throws 
         java.rmi.RemoteException;
    public com.amalto.webapp.util.webservices.WSString putBusinessConceptSchema(com.amalto.webapp.util.webservices.WSPutBusinessConceptSchema wsPutBusinessConceptSchema) throws 
         java.rmi.RemoteException;
    public com.amalto.webapp.util.webservices.WSConceptKey getBusinessConceptKey(com.amalto.webapp.util.webservices.WSGetBusinessConceptKey wsGetBusinessConceptKey) throws 
         java.rmi.RemoteException;
    public com.amalto.webapp.util.webservices.WSDataClusterPKArray getDataClusterPKs(com.amalto.webapp.util.webservices.WSRegexDataClusterPKs regexp) throws 
         java.rmi.RemoteException;
    public com.amalto.webapp.util.webservices.WSDataCluster getDataCluster(com.amalto.webapp.util.webservices.WSGetDataCluster wsDataClusterPK) throws 
         java.rmi.RemoteException;
    public com.amalto.webapp.util.webservices.WSBoolean existsDataCluster(com.amalto.webapp.util.webservices.WSExistsDataCluster wsExistsDataCluster) throws 
         java.rmi.RemoteException;
    public com.amalto.webapp.util.webservices.WSDataClusterPK putDataCluster(com.amalto.webapp.util.webservices.WSPutDataCluster wsDataCluster) throws 
         java.rmi.RemoteException;
    public com.amalto.webapp.util.webservices.WSDataClusterPK deleteDataCluster(com.amalto.webapp.util.webservices.WSDeleteDataCluster wsDeleteDataCluster) throws 
         java.rmi.RemoteException;
    public com.amalto.webapp.util.webservices.WSStringArray getConceptsInDataCluster(com.amalto.webapp.util.webservices.WSGetConceptsInDataCluster wsGetConceptsInDataCluster) throws 
         java.rmi.RemoteException;
    public com.amalto.webapp.util.webservices.WSViewPKArray getViewPKs(com.amalto.webapp.util.webservices.WSGetViewPKs regexp) throws 
         java.rmi.RemoteException;
    public com.amalto.webapp.util.webservices.WSView getView(com.amalto.webapp.util.webservices.WSGetView wsViewPK) throws 
         java.rmi.RemoteException;
    public com.amalto.webapp.util.webservices.WSBoolean existsView(com.amalto.webapp.util.webservices.WSExistsView wsViewPK) throws 
         java.rmi.RemoteException;
    public com.amalto.webapp.util.webservices.WSViewPK putView(com.amalto.webapp.util.webservices.WSPutView wsView) throws 
         java.rmi.RemoteException;
    public com.amalto.webapp.util.webservices.WSViewPK deleteView(com.amalto.webapp.util.webservices.WSDeleteView wsViewDel) throws 
         java.rmi.RemoteException;
    public com.amalto.webapp.util.webservices.WSString getBusinessConceptValue(com.amalto.webapp.util.webservices.WSGetBusinessConceptValue wsGetBusinessConceptValue) throws 
         java.rmi.RemoteException;
    public com.amalto.webapp.util.webservices.WSStringArray getFullPathValues(com.amalto.webapp.util.webservices.WSGetFullPathValues wsGetFullPathValues) throws 
         java.rmi.RemoteException;
    public com.amalto.webapp.util.webservices.WSItem getItem(com.amalto.webapp.util.webservices.WSGetItem wsGetItem) throws 
         java.rmi.RemoteException;
    public com.amalto.webapp.util.webservices.WSBoolean existsItem(com.amalto.webapp.util.webservices.WSExistsItem wsExistsItem) throws 
         java.rmi.RemoteException;
    public com.amalto.webapp.util.webservices.WSStringArray getItems(com.amalto.webapp.util.webservices.WSGetItems wsGetItems) throws 
         java.rmi.RemoteException;
    public com.amalto.webapp.util.webservices.WSItemPKsByCriteriaResponse getItemPKsByCriteria(com.amalto.webapp.util.webservices.WSGetItemPKsByCriteria wsGetItemPKsByCriteria) throws 
         java.rmi.RemoteException;
    public com.amalto.webapp.util.webservices.WSStringArray viewSearch(com.amalto.webapp.util.webservices.WSViewSearch wsViewSearch) throws 
         java.rmi.RemoteException;
    public com.amalto.webapp.util.webservices.WSStringArray xPathsSearch(com.amalto.webapp.util.webservices.WSXPathsSearch wsXPathsSearch) throws 
         java.rmi.RemoteException;
    public com.amalto.webapp.util.webservices.WSString count(com.amalto.webapp.util.webservices.WSCount wsCount) throws 
         java.rmi.RemoteException;
    public com.amalto.webapp.util.webservices.WSStringArray quickSearch(com.amalto.webapp.util.webservices.WSQuickSearch wsQuickSearch) throws 
         java.rmi.RemoteException;
    public com.amalto.webapp.util.webservices.WSItemPK putItem(com.amalto.webapp.util.webservices.WSPutItem wsPutItem) throws 
         java.rmi.RemoteException;
    public com.amalto.webapp.util.webservices.WSPipeline extractUsingTransformer(com.amalto.webapp.util.webservices.WSExtractUsingTransformer wsExtractUsingTransformer) throws 
         java.rmi.RemoteException;
    public com.amalto.webapp.util.webservices.WSPipeline extractUsingTransformerThruView(com.amalto.webapp.util.webservices.WSExtractUsingTransformerThruView wsExtractUsingTransformerThruView) throws 
         java.rmi.RemoteException;
    public com.amalto.webapp.util.webservices.WSItemPK deleteItem(com.amalto.webapp.util.webservices.WSDeleteItem wsDeleteItem) throws 
         java.rmi.RemoteException;
    public com.amalto.webapp.util.webservices.WSInt deleteItems(com.amalto.webapp.util.webservices.WSDeleteItems wsDeleteItems) throws 
         java.rmi.RemoteException;
    public com.amalto.webapp.util.webservices.WSStringArray runQuery(com.amalto.webapp.util.webservices.WSRunQuery wsRunQuery) throws 
         java.rmi.RemoteException;
    public com.amalto.webapp.util.webservices.WSConnectorInteractionResponse connectorInteraction(com.amalto.webapp.util.webservices.WSConnectorInteraction wsConnectorInteraction) throws 
         java.rmi.RemoteException;
    public com.amalto.webapp.util.webservices.WSRoutingRulePKArray getRoutingRulePKs(com.amalto.webapp.util.webservices.WSGetRoutingRulePKs regexp) throws 
         java.rmi.RemoteException;
    public com.amalto.webapp.util.webservices.WSRoutingRule getRoutingRule(com.amalto.webapp.util.webservices.WSGetRoutingRule wsRoutingRulePK) throws 
         java.rmi.RemoteException;
    public com.amalto.webapp.util.webservices.WSBoolean existsRoutingRule(com.amalto.webapp.util.webservices.WSExistsRoutingRule wsExistsRoutingRule) throws 
         java.rmi.RemoteException;
    public com.amalto.webapp.util.webservices.WSRoutingRulePK putRoutingRule(com.amalto.webapp.util.webservices.WSPutRoutingRule wsRoutingRule) throws 
         java.rmi.RemoteException;
    public com.amalto.webapp.util.webservices.WSRoutingRulePK deleteRoutingRule(com.amalto.webapp.util.webservices.WSDeleteRoutingRule wsRoutingRuleDel) throws 
         java.rmi.RemoteException;
    public com.amalto.webapp.util.webservices.WSString serviceAction(com.amalto.webapp.util.webservices.WSServiceAction wsServiceAction) throws 
         java.rmi.RemoteException;
    public com.amalto.webapp.util.webservices.WSString getServiceConfiguration(com.amalto.webapp.util.webservices.WSServiceGetConfiguration wsGetConfiguration) throws 
         java.rmi.RemoteException;
    public com.amalto.webapp.util.webservices.WSString putServiceConfiguration(com.amalto.webapp.util.webservices.WSServicePutConfiguration wsPutConfiguration) throws 
         java.rmi.RemoteException;
    public com.amalto.webapp.util.webservices.WSServicesList getServicesList(com.amalto.webapp.util.webservices.WSGetServicesList wsGetServicesList) throws 
         java.rmi.RemoteException;
    public com.amalto.webapp.util.webservices.WSStoredProcedure getStoredProcedure(com.amalto.webapp.util.webservices.WSGetStoredProcedure wsGetStoredProcedure) throws 
         java.rmi.RemoteException;
    public com.amalto.webapp.util.webservices.WSBoolean existsStoredProcedure(com.amalto.webapp.util.webservices.WSExistsStoredProcedure wsExistsStoredProcedure) throws 
         java.rmi.RemoteException;
    public com.amalto.webapp.util.webservices.WSStoredProcedurePKArray getStoredProcedurePKs(com.amalto.webapp.util.webservices.WSRegexStoredProcedure regex) throws 
         java.rmi.RemoteException;
    public com.amalto.webapp.util.webservices.WSStoredProcedurePK putStoredProcedure(com.amalto.webapp.util.webservices.WSPutStoredProcedure wsStoredProcedure) throws 
         java.rmi.RemoteException;
    public com.amalto.webapp.util.webservices.WSStoredProcedurePK deleteStoredProcedure(com.amalto.webapp.util.webservices.WSDeleteStoredProcedure wsStoredProcedureDelete) throws 
         java.rmi.RemoteException;
    public com.amalto.webapp.util.webservices.WSStringArray executeStoredProcedure(com.amalto.webapp.util.webservices.WSExecuteStoredProcedure wsExecuteStoredProcedure) throws 
         java.rmi.RemoteException;
    public com.amalto.webapp.util.webservices.WSTransformer getTransformer(com.amalto.webapp.util.webservices.WSGetTransformer wsGetTransformer) throws 
         java.rmi.RemoteException;
    public com.amalto.webapp.util.webservices.WSBoolean existsTransformer(com.amalto.webapp.util.webservices.WSExistsTransformer wsExistsTransformer) throws 
         java.rmi.RemoteException;
    public com.amalto.webapp.util.webservices.WSTransformerPKArray getTransformerPKs(com.amalto.webapp.util.webservices.WSGetTransformerPKs regex) throws 
         java.rmi.RemoteException;
    public com.amalto.webapp.util.webservices.WSTransformerPK putTransformer(com.amalto.webapp.util.webservices.WSPutTransformer wsTransformer) throws 
         java.rmi.RemoteException;
    public com.amalto.webapp.util.webservices.WSTransformerPK deleteTransformer(com.amalto.webapp.util.webservices.WSDeleteTransformer wsTransformerDelete) throws 
         java.rmi.RemoteException;
    public com.amalto.webapp.util.webservices.WSPipeline processBytesUsingTransformer(com.amalto.webapp.util.webservices.WSProcessBytesUsingTransformer wsProcessBytesUsingTransformer) throws 
         java.rmi.RemoteException;
    public com.amalto.webapp.util.webservices.WSPipeline processFileUsingTransformer(com.amalto.webapp.util.webservices.WSProcessFileUsingTransformer wsProcessFileUsingTransformer) throws 
         java.rmi.RemoteException;
    public com.amalto.webapp.util.webservices.WSBackgroundJobPK processBytesUsingTransformerAsBackgroundJob(com.amalto.webapp.util.webservices.WSProcessBytesUsingTransformerAsBackgroundJob wsProcessBytesUsingTransformerAsBackgroundJob) throws 
         java.rmi.RemoteException;
    public com.amalto.webapp.util.webservices.WSBackgroundJobPK processFileUsingTransformerAsBackgroundJob(com.amalto.webapp.util.webservices.WSProcessFileUsingTransformerAsBackgroundJob wsProcessFileUsingTransformerAsBackgroundJob) throws 
         java.rmi.RemoteException;
    public com.amalto.webapp.util.webservices.WSTransformerV2 getTransformerV2(com.amalto.webapp.util.webservices.WSGetTransformerV2 wsGetTransformerV2) throws 
         java.rmi.RemoteException;
    public com.amalto.webapp.util.webservices.WSBoolean existsTransformerV2(com.amalto.webapp.util.webservices.WSExistsTransformerV2 wsExistsTransformerV2) throws 
         java.rmi.RemoteException;
    public com.amalto.webapp.util.webservices.WSTransformerV2PKArray getTransformerV2PKs(com.amalto.webapp.util.webservices.WSGetTransformerV2PKs regex) throws 
         java.rmi.RemoteException;
    public com.amalto.webapp.util.webservices.WSTransformerV2PK putTransformerV2(com.amalto.webapp.util.webservices.WSPutTransformerV2 wsTransformerV2) throws 
         java.rmi.RemoteException;
    public com.amalto.webapp.util.webservices.WSTransformerV2PK deleteTransformerV2(com.amalto.webapp.util.webservices.WSDeleteTransformerV2 wsDeleteTransformerV2) throws 
         java.rmi.RemoteException;
    public com.amalto.webapp.util.webservices.WSTransformerContext executeTransformerV2(com.amalto.webapp.util.webservices.WSExecuteTransformerV2 wsExecuteTransformerV2) throws 
         java.rmi.RemoteException;
    public com.amalto.webapp.util.webservices.WSBackgroundJobPK executeTransformerV2AsJob(com.amalto.webapp.util.webservices.WSExecuteTransformerV2AsJob wsExecuteTransformerV2AsJob) throws 
         java.rmi.RemoteException;
    public com.amalto.webapp.util.webservices.WSTransformerContext extractThroughTransformerV2(com.amalto.webapp.util.webservices.WSExtractThroughTransformerV2 wsExtractThroughTransformerV2) throws 
         java.rmi.RemoteException;
    public com.amalto.webapp.util.webservices.WSBoolean existsTransformerPluginV2(com.amalto.webapp.util.webservices.WSExistsTransformerPluginV2 wsExistsTransformerPluginV2) throws 
         java.rmi.RemoteException;
    public com.amalto.webapp.util.webservices.WSString getTransformerPluginV2Configuration(com.amalto.webapp.util.webservices.WSTransformerPluginV2GetConfiguration wsGetConfiguration) throws 
         java.rmi.RemoteException;
    public com.amalto.webapp.util.webservices.WSString putTransformerPluginV2Configuration(com.amalto.webapp.util.webservices.WSTransformerPluginV2PutConfiguration wsPutConfiguration) throws 
         java.rmi.RemoteException;
    public com.amalto.webapp.util.webservices.WSTransformerPluginV2Details getTransformerPluginV2Details(com.amalto.webapp.util.webservices.WSGetTransformerPluginV2Details wsGetTransformerPluginV2Details) throws 
         java.rmi.RemoteException;
    public com.amalto.webapp.util.webservices.WSTransformerPluginV2SList getTransformerPluginV2SList(com.amalto.webapp.util.webservices.WSGetTransformerPluginV2SList wsGetTransformerPluginV2SList) throws 
         java.rmi.RemoteException;
    public com.amalto.webapp.util.webservices.WSRole getRole(com.amalto.webapp.util.webservices.WSGetRole wsGetRole) throws 
         java.rmi.RemoteException;
    public com.amalto.webapp.util.webservices.WSBoolean existsRole(com.amalto.webapp.util.webservices.WSExistsRole wsExistsRole) throws 
         java.rmi.RemoteException;
    public com.amalto.webapp.util.webservices.WSRolePKArray getRolePKs(com.amalto.webapp.util.webservices.WSGetRolePKs regex) throws 
         java.rmi.RemoteException;
    public com.amalto.webapp.util.webservices.WSRolePK putRole(com.amalto.webapp.util.webservices.WSPutRole wsRole) throws 
         java.rmi.RemoteException;
    public com.amalto.webapp.util.webservices.WSRolePK deleteRole(com.amalto.webapp.util.webservices.WSDeleteRole wsRoleDelete) throws 
         java.rmi.RemoteException;
    public com.amalto.webapp.util.webservices.WSStringArray getObjectsForRoles(com.amalto.webapp.util.webservices.WSGetObjectsForRoles wsRoleDelete) throws 
         java.rmi.RemoteException;
    public com.amalto.webapp.util.webservices.WSMenu getMenu(com.amalto.webapp.util.webservices.WSGetMenu wsGetMenu) throws 
         java.rmi.RemoteException;
    public com.amalto.webapp.util.webservices.WSBoolean existsMenu(com.amalto.webapp.util.webservices.WSExistsMenu wsExistsMenu) throws 
         java.rmi.RemoteException;
    public com.amalto.webapp.util.webservices.WSMenuPKArray getMenuPKs(com.amalto.webapp.util.webservices.WSGetMenuPKs regex) throws 
         java.rmi.RemoteException;
    public com.amalto.webapp.util.webservices.WSMenuPK putMenu(com.amalto.webapp.util.webservices.WSPutMenu wsMenu) throws 
         java.rmi.RemoteException;
    public com.amalto.webapp.util.webservices.WSMenuPK deleteMenu(com.amalto.webapp.util.webservices.WSDeleteMenu wsMenuDelete) throws 
         java.rmi.RemoteException;
    public com.amalto.webapp.util.webservices.WSVersioningObjectsHistory versioningGetObjectsHistory(com.amalto.webapp.util.webservices.WSVersioningGetObjectsHistory wsVersioningGetObjectsHistory) throws 
         java.rmi.RemoteException;
    public com.amalto.webapp.util.webservices.WSVersioningItemsHistory versioningGetItemsHistory(com.amalto.webapp.util.webservices.WSVersioningGetItemsHistory wsVersioningGetItemsHistory) throws 
         java.rmi.RemoteException;
    public com.amalto.webapp.util.webservices.WSVersioningSystemConfiguration getVersioningSystemConfiguration(com.amalto.webapp.util.webservices.WSGetVersioningSystemConfiguration wsGetVersioningSystemConfiguration) throws 
         java.rmi.RemoteException;
    public com.amalto.webapp.util.webservices.WSString putVersioningSystemConfiguration(com.amalto.webapp.util.webservices.WSPutVersioningSystemConfiguration wsPutVersioningSystemConfiguration) throws 
         java.rmi.RemoteException;
    public com.amalto.webapp.util.webservices.WSVersioningInfo versioningGetInfo(com.amalto.webapp.util.webservices.WSVersioningGetInfo wsVersioningGetInfo) throws 
         java.rmi.RemoteException;
    public com.amalto.webapp.util.webservices.WSBackgroundJobPK versioningTagObjects(com.amalto.webapp.util.webservices.WSVersioningTagObjects wsVersioningTagObjects) throws 
         java.rmi.RemoteException;
    public com.amalto.webapp.util.webservices.WSBackgroundJobPK versioningTagItems(com.amalto.webapp.util.webservices.WSVersioningTagItems wsVersioningTagItems) throws 
         java.rmi.RemoteException;
    public com.amalto.webapp.util.webservices.WSBackgroundJobPK versioningRestoreObjects(com.amalto.webapp.util.webservices.WSVersioningRestoreObjects wsVersioningRestoreObjects) throws 
         java.rmi.RemoteException;
    public com.amalto.webapp.util.webservices.WSBackgroundJobPK versioningRestoreItems(com.amalto.webapp.util.webservices.WSVersioningRestoreItems wsVersioningRestoreItems) throws 
         java.rmi.RemoteException;
    public com.amalto.webapp.util.webservices.WSBackgroundJobPKArray findBackgroundJobPKs(com.amalto.webapp.util.webservices.WSFindBackgroundJobPKs status) throws 
         java.rmi.RemoteException;
    public com.amalto.webapp.util.webservices.WSBackgroundJob getBackgroundJob(com.amalto.webapp.util.webservices.WSGetBackgroundJob wsGetBackgroundJob) throws 
         java.rmi.RemoteException;
    public com.amalto.webapp.util.webservices.WSBackgroundJobPK putBackgroundJob(com.amalto.webapp.util.webservices.WSPutBackgroundJob wsPutBackgroundJob) throws 
         java.rmi.RemoteException;
    public com.amalto.webapp.util.webservices.WSRoutingOrderV2 getRoutingOrderV2(com.amalto.webapp.util.webservices.WSGetRoutingOrderV2 wsGetRoutingOrderV2) throws 
         java.rmi.RemoteException;
    public com.amalto.webapp.util.webservices.WSRoutingOrderV2 existsRoutingOrderV2(com.amalto.webapp.util.webservices.WSExistsRoutingOrderV2 wsExistsRoutingOrder) throws 
         java.rmi.RemoteException;
    public com.amalto.webapp.util.webservices.WSRoutingOrderV2PK deleteRoutingOrderV2(com.amalto.webapp.util.webservices.WSDeleteRoutingOrderV2 wsDeleteRoutingOrder) throws 
         java.rmi.RemoteException;
    public com.amalto.webapp.util.webservices.WSRoutingOrderV2PK executeRoutingOrderV2Asynchronously(com.amalto.webapp.util.webservices.WSExecuteRoutingOrderV2Asynchronously wsExecuteRoutingOrderAsynchronously) throws 
         java.rmi.RemoteException;
    public com.amalto.webapp.util.webservices.WSString executeRoutingOrderV2Synchronously(com.amalto.webapp.util.webservices.WSExecuteRoutingOrderV2Synchronously wsExecuteRoutingOrderSynchronously) throws 
         java.rmi.RemoteException;
    public com.amalto.webapp.util.webservices.WSRoutingOrderV2PKArray getRoutingOrderV2PKsByCriteria(com.amalto.webapp.util.webservices.WSGetRoutingOrderV2PKsByCriteria wsGetRoutingOrderV2PKsByCriteria) throws 
         java.rmi.RemoteException;
    public com.amalto.webapp.util.webservices.WSRoutingOrderV2Array getRoutingOrderV2SByCriteria(com.amalto.webapp.util.webservices.WSGetRoutingOrderV2SByCriteria wsGetRoutingOrderV2SByCriteria) throws 
         java.rmi.RemoteException;
    public com.amalto.webapp.util.webservices.WSRoutingRulePKArray routeItemV2(com.amalto.webapp.util.webservices.WSRouteItemV2 wsRouteItem) throws 
         java.rmi.RemoteException;
    public com.amalto.webapp.util.webservices.WSRoutingEngineV2Status routingEngineV2Action(com.amalto.webapp.util.webservices.WSRoutingEngineV2Action wsRoutingEngineAction) throws 
         java.rmi.RemoteException;
    public com.amalto.webapp.util.webservices.WSUniverse getUniverse(com.amalto.webapp.util.webservices.WSGetUniverse wsGetUniverse) throws 
         java.rmi.RemoteException;
    public com.amalto.webapp.util.webservices.WSBoolean existsUniverse(com.amalto.webapp.util.webservices.WSExistsUniverse wsExistsUniverse) throws 
         java.rmi.RemoteException;
    public com.amalto.webapp.util.webservices.WSUniversePKArray getUniversePKs(com.amalto.webapp.util.webservices.WSGetUniversePKs regex) throws 
         java.rmi.RemoteException;
    public com.amalto.webapp.util.webservices.WSUniversePK putUniverse(com.amalto.webapp.util.webservices.WSPutUniverse wsUniverse) throws 
         java.rmi.RemoteException;
    public com.amalto.webapp.util.webservices.WSUniversePK deleteUniverse(com.amalto.webapp.util.webservices.WSDeleteUniverse wsUniverseDelete) throws 
         java.rmi.RemoteException;
    public com.amalto.webapp.util.webservices.WSStringArray getObjectsForUniverses(com.amalto.webapp.util.webservices.WSGetObjectsForUniverses regex) throws 
         java.rmi.RemoteException;
    public com.amalto.webapp.util.webservices.WSUniverse getCurrentUniverse(com.amalto.webapp.util.webservices.WSGetCurrentUniverse wsGetCurrentUniverse) throws 
         java.rmi.RemoteException;
    public com.amalto.webapp.util.webservices.WSSynchronizationPlan getSynchronizationPlan(com.amalto.webapp.util.webservices.WSGetSynchronizationPlan wsGetSynchronizationPlan) throws 
         java.rmi.RemoteException;
    public com.amalto.webapp.util.webservices.WSBoolean existsSynchronizationPlan(com.amalto.webapp.util.webservices.WSExistsSynchronizationPlan wsExistsSynchronizationPlan) throws 
         java.rmi.RemoteException;
    public com.amalto.webapp.util.webservices.WSSynchronizationPlanPKArray getSynchronizationPlanPKs(com.amalto.webapp.util.webservices.WSGetSynchronizationPlanPKs regex) throws 
         java.rmi.RemoteException;
    public com.amalto.webapp.util.webservices.WSSynchronizationPlanPK putSynchronizationPlan(com.amalto.webapp.util.webservices.WSPutSynchronizationPlan wsSynchronizationPlan) throws 
         java.rmi.RemoteException;
    public com.amalto.webapp.util.webservices.WSSynchronizationPlanPK deleteSynchronizationPlan(com.amalto.webapp.util.webservices.WSDeleteSynchronizationPlan wsSynchronizationPlanDelete) throws 
         java.rmi.RemoteException;
    public com.amalto.webapp.util.webservices.WSStringArray getObjectsForSynchronizationPlans(com.amalto.webapp.util.webservices.WSGetObjectsForSynchronizationPlans regex) throws 
         java.rmi.RemoteException;
    public com.amalto.webapp.util.webservices.WSStringArray getSynchronizationPlanObjectsAlgorithms(com.amalto.webapp.util.webservices.WSGetSynchronizationPlanObjectsAlgorithms regex) throws 
         java.rmi.RemoteException;
    public com.amalto.webapp.util.webservices.WSStringArray getSynchronizationPlanItemsAlgorithms(com.amalto.webapp.util.webservices.WSGetSynchronizationPlanItemsAlgorithms regex) throws 
         java.rmi.RemoteException;
    public com.amalto.webapp.util.webservices.WSSynchronizationPlanStatus synchronizationPlanAction(com.amalto.webapp.util.webservices.WSSynchronizationPlanAction wsSynchronizationPlanAction) throws 
         java.rmi.RemoteException;
    public com.amalto.webapp.util.webservices.WSStringArray synchronizationGetUnsynchronizedObjectsIDs(com.amalto.webapp.util.webservices.WSSynchronizationGetUnsynchronizedObjectsIDs wsSynchronizationGetUnsynchronizedObjectsIDs) throws 
         java.rmi.RemoteException;
    public com.amalto.webapp.util.webservices.WSString synchronizationGetObjectXML(com.amalto.webapp.util.webservices.WSSynchronizationGetObjectXML wsSynchronizationGetObjectXML) throws 
         java.rmi.RemoteException;
    public com.amalto.webapp.util.webservices.WSString synchronizationPutObjectXML(com.amalto.webapp.util.webservices.WSSynchronizationPutObjectXML wsSynchronizationPutObjectXML) throws 
         java.rmi.RemoteException;
    public com.amalto.webapp.util.webservices.WSItemPKArray synchronizationGetUnsynchronizedItemPKs(com.amalto.webapp.util.webservices.WSSynchronizationGetUnsynchronizedItemPKs wsSynchronizationGetUnsynchronizedItemPKs) throws 
         java.rmi.RemoteException;
    public com.amalto.webapp.util.webservices.WSString synchronizationGetItemXML(com.amalto.webapp.util.webservices.WSSynchronizationGetItemXML wsSynchronizationGetItemXML) throws 
         java.rmi.RemoteException;
    public com.amalto.webapp.util.webservices.WSItemPK synchronizationPutItemXML(com.amalto.webapp.util.webservices.WSSynchronizationPutItemXML wsSynchronizationPutItemXML) throws 
         java.rmi.RemoteException;
    public com.amalto.webapp.util.webservices.WSSynchronizationItem getSynchronizationItem(com.amalto.webapp.util.webservices.WSGetSynchronizationItem wsGetSynchronizationItem) throws 
         java.rmi.RemoteException;
    public com.amalto.webapp.util.webservices.WSBoolean existsSynchronizationItem(com.amalto.webapp.util.webservices.WSExistsSynchronizationItem wsExistsSynchronizationItem) throws 
         java.rmi.RemoteException;
    public com.amalto.webapp.util.webservices.WSSynchronizationItemPKArray getSynchronizationItemPKs(com.amalto.webapp.util.webservices.WSGetSynchronizationItemPKs regex) throws 
         java.rmi.RemoteException;
    public com.amalto.webapp.util.webservices.WSSynchronizationItemPK putSynchronizationItem(com.amalto.webapp.util.webservices.WSPutSynchronizationItem wsSynchronizationItem) throws 
         java.rmi.RemoteException;
    public com.amalto.webapp.util.webservices.WSSynchronizationItemPK deleteSynchronizationItem(com.amalto.webapp.util.webservices.WSDeleteSynchronizationItem wsSynchronizationItemDelete) throws 
         java.rmi.RemoteException;
    public com.amalto.webapp.util.webservices.WSSynchronizationItem resolveSynchronizationItem(com.amalto.webapp.util.webservices.WSResolveSynchronizationItem wsResolveSynchronizationItem) throws 
         java.rmi.RemoteException;
}
