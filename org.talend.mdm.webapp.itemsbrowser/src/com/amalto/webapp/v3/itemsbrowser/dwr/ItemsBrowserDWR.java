package com.amalto.webapp.v3.itemsbrowser.dwr;

import java.io.StringReader;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.security.jacc.PolicyContextException;
import javax.xml.transform.TransformerException;

import org.apache.commons.jxpath.JXPathContext;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.xerces.dom.ElementNSImpl;
import org.apache.xerces.dom.TextImpl;
import org.directwebremoting.WebContext;
import org.directwebremoting.WebContextFactory;
import org.exolab.castor.types.Date;
import org.exolab.castor.types.Time;
import org.jboss.dom4j.DocumentException;
import org.jboss.dom4j.io.SAXReader;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.amalto.core.ejb.ItemPOJOPK;
import com.amalto.core.objects.datacluster.ejb.DataClusterPOJOPK;
import com.amalto.core.util.LocalUser;
import com.amalto.webapp.core.bean.ComboItemBean;
import com.amalto.webapp.core.bean.Configuration;
import com.amalto.webapp.core.bean.ListRange;
import com.amalto.webapp.core.bean.UpdateReportItem;
import com.amalto.webapp.core.dwr.CommonDWR;
import com.amalto.webapp.core.json.JSONArray;
import com.amalto.webapp.core.json.JSONObject;
import com.amalto.webapp.core.util.Util;
import com.amalto.webapp.core.util.XtentisWebappException;
import com.amalto.webapp.util.webservices.WSBoolean;
import com.amalto.webapp.util.webservices.WSByteArray;
import com.amalto.webapp.util.webservices.WSConceptKey;
import com.amalto.webapp.util.webservices.WSCount;
import com.amalto.webapp.util.webservices.WSDataClusterPK;
import com.amalto.webapp.util.webservices.WSDataModelPK;
import com.amalto.webapp.util.webservices.WSDeleteItem;
import com.amalto.webapp.util.webservices.WSDropItem;
import com.amalto.webapp.util.webservices.WSDroppedItemPK;
import com.amalto.webapp.util.webservices.WSExecuteTransformerV2;
import com.amalto.webapp.util.webservices.WSExistsDataCluster;
import com.amalto.webapp.util.webservices.WSExistsItem;
import com.amalto.webapp.util.webservices.WSGetBusinessConceptKey;
import com.amalto.webapp.util.webservices.WSGetBusinessConcepts;
import com.amalto.webapp.util.webservices.WSGetDataModel;
import com.amalto.webapp.util.webservices.WSGetItem;
import com.amalto.webapp.util.webservices.WSGetTransformer;
import com.amalto.webapp.util.webservices.WSGetTransformerPKs;
import com.amalto.webapp.util.webservices.WSGetView;
import com.amalto.webapp.util.webservices.WSGetViewPKs;
import com.amalto.webapp.util.webservices.WSItem;
import com.amalto.webapp.util.webservices.WSItemPK;
import com.amalto.webapp.util.webservices.WSPutItem;
import com.amalto.webapp.util.webservices.WSPutItemWithReport;
import com.amalto.webapp.util.webservices.WSRouteItemV2;
import com.amalto.webapp.util.webservices.WSStringPredicate;
import com.amalto.webapp.util.webservices.WSTransformer;
import com.amalto.webapp.util.webservices.WSTransformerContext;
import com.amalto.webapp.util.webservices.WSTransformerContextPipelinePipelineItem;
import com.amalto.webapp.util.webservices.WSTransformerPK;
import com.amalto.webapp.util.webservices.WSTransformerV2PK;
import com.amalto.webapp.util.webservices.WSTypedContent;
import com.amalto.webapp.util.webservices.WSView;
import com.amalto.webapp.util.webservices.WSViewPK;
import com.amalto.webapp.util.webservices.WSWhereAnd;
import com.amalto.webapp.util.webservices.WSWhereCondition;
import com.amalto.webapp.util.webservices.WSWhereItem;
import com.amalto.webapp.util.webservices.WSWhereOperator;
import com.amalto.webapp.util.webservices.WSWhereOr;
import com.amalto.webapp.v3.itemsbrowser.bean.Restriction;
import com.amalto.webapp.v3.itemsbrowser.bean.TreeNode;
import com.amalto.webapp.v3.itemsbrowser.bean.View;
import com.sun.xml.xsom.XSAnnotation;
import com.sun.xml.xsom.XSComplexType;
import com.sun.xml.xsom.XSContentType;
import com.sun.xml.xsom.XSElementDecl;
import com.sun.xml.xsom.XSFacet;
import com.sun.xml.xsom.XSParticle;
import com.sun.xml.xsom.XSRestrictionSimpleType;
import com.sun.xml.xsom.XSSimpleType;
import com.sun.xml.xsom.XSTerm;
import com.sun.xml.xsom.XSType;
import com.sun.xml.xsom.impl.AnnotationImpl;
import com.sun.xml.xsom.impl.FacetImpl;

/**cluster
 * 
 * 
 * @author asaintguilhem
 *
 */

public class ItemsBrowserDWR {

	public ItemsBrowserDWR() {
		super();
	}
	
	
	/**
	 * return a list of "browse items" views
	 * @param language
	 * @return a map name->description
	 * @throws RemoteException
	 * @throws Exception
	 */
	public Map<String,String> getViewsList(String language) throws RemoteException, Exception{
		//Configuration config = Configuration.getInstance();
		Configuration config = Configuration.getInstance(true);
		String model = config.getModel();
		String [] businessConcept = Util.getPort().	getBusinessConcepts(
					new WSGetBusinessConcepts(
							new WSDataModelPK(model)
						)
					).getStrings();
		ArrayList<String> bc = new ArrayList<String>();
		for (int i = 0; i < businessConcept.length; i++) {
			bc.add(businessConcept[i]);
		}
		WSViewPK[] wsViewsPK = Util.getPort().getViewPKs(new WSGetViewPKs("Browse_items.*")).getWsViewPK();
		String[] names = new String[wsViewsPK.length];
		TreeMap<String,String> views = new TreeMap<String,String>();
		Pattern p = Pattern.compile(".*\\["+language.toUpperCase()+":(.*?)\\].*",Pattern.DOTALL);
		for (int i = 0; i < wsViewsPK.length; i++) {
			WSView wsview = Util.getPort().getView(new WSGetView(wsViewsPK[i]));
			String concept = wsview.getName().replaceAll("Browse_items_","").replaceAll("#.*","");
			names[i] = wsViewsPK[i].getPk();
			if(		//wsviews[i].getWsDataClusterPK().getPk().equals(cluster) 
					//&& wsviews[i].getWsDataModelPK().getPk().equals(model) && 
					bc.contains(concept)
					){
				
				views.put(wsview.getName(),p.matcher(wsview.getDescription()).replaceAll("$1"));
			}
		}	
		return CommonDWR.getMapSortedByValue(views);
	}
	
	public View getView(String viewPK, String language){
		try {
			WebContext ctx = WebContextFactory.get();
			String concept =  CommonDWR.getConceptFromBrowseItemView(viewPK);
			Configuration config = Configuration.getInstance();
			String model = config.getModel();
			View view = new View(viewPK, language);
			WSConceptKey key = Util.getPort().getBusinessConceptKey(
					new WSGetBusinessConceptKey(
							new WSDataModelPK(model),
							concept));
			String[] keys = key.getFields();
			for (int i = 0; i < keys.length; i++) {
				if(".".equals(key.getSelector()))
					keys[i] = "/"+concept+"/"+keys[i];					
				else
					keys[i] = key.getSelector()+keys[i];
			}
			view.setKeys(key.getFields());
			ctx.getSession().setAttribute("foreignKeys",key.getFields());
			view.setMetaDataTypes(getMetaDataTypes(view));
			return view;
		} catch (RemoteException e) {
			e.printStackTrace();
			return null;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	
	/**
	 * return a list of searchable elements of a browse items list
	 * @param viewPK
	 * @param language
	 * @return a map xpath->label
	 */
	/* public HashMap<String,String> getSearchables(String viewPK,String language){
		try{
			String[] searchables = new View(viewPK,language).getSearchables();
			HashMap<String,String> labelSearchables = new HashMap<String,String>();
			HashMap<String,String> xpathToLabel = CommonDWR.getFieldsByBrowseItemsView(viewPK,language,true);	
			Configuration config = Configuration.getInstance();
			String concept = CommonDWR.getConceptFromBrowseItemView( viewPK);
			xpathToLabel.put(concept,CommonDWR.getConceptLabel(config.getModel(),concept,language));
			for (int i = 0; i < searchables.length; i++) {
				labelSearchables.put(searchables[i],xpathToLabel.get(searchables[i]));
			}
			return labelSearchables.put;			
		}
		catch(Exception e){
			return null;
		}
	}*/
	
	/**
	 * return a list of viewable elements o a browse items list
	 * used for column header of a grid
	 * @param viewPK
	 * @param language
	 * @return an array of label
	 */
	
	public String[] getViewables(String viewPK, String language){		
		WebContext ctx = WebContextFactory.get();
		ctx.getSession().setAttribute("viewNameItems",null);
		try {
			Configuration config = Configuration.getInstance();
			String[] viewables = new View(viewPK,language).getViewables();
			String[] labelViewables = new String[viewables.length];
			HashMap<String,String> xpathToLabel = CommonDWR.getFieldsByDataModel(
					config.getModel(),
					CommonDWR.getConceptFromBrowseItemView(viewPK),
					language, true);
			for (int i = 0; i < viewables.length; i++) {
				labelViewables[i] = xpathToLabel.get(viewables[i]);
				//System.out.println(labelViewables[i]);
			}
			return labelViewables;
		}catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		
	}
	
	
	public TreeNode getRootNode(String concept, String language) throws RemoteException, Exception{
		Configuration config = Configuration.getInstance(true);
		String dataModelPK = config.getModel();
		Map<String,XSElementDecl> map = CommonDWR.getConceptMap(dataModelPK);
		XSElementDecl decl = map.get(concept);
		if (decl == null) {
			String err = "Concept '"+concept+"' is not found in model '"+dataModelPK+"'";
			org.apache.log4j.Logger.getLogger(this.getClass()).error(err);
			return null;
			//throw new RemoteException(err);
		}
    	XSAnnotation xsa = decl.getAnnotation();    	
    	TreeNode rootNode = new TreeNode();
		ArrayList<String> roles = Util.getAjaxSubject().getRoles();
    	rootNode.fetchAnnotations(xsa,roles, language);
    	return rootNode;
	}
	
	
	
	/**
	 * start to parse the xsd.
	 *  set the maps : idToParticle, idToXpath and the list : nodeAutorization in the session
	 * @param concept
	 * @param ids
	 * @param nodeId the id of the root node in yui tree
	 * @return an error or succes message
	 */
	public String setTree(String concept, String[] ids, int nodeId, boolean foreignKey, int docIndex, boolean refresh){
        WebContext ctx = WebContextFactory.get();	
		try {
			if(ids == null)
			{
				String[] idsExist = (String[])ctx.getSession().getAttribute("treeIdxToIDS" + docIndex);
				if(idsExist != null && idsExist.length > 0)
				{
					ids = idsExist;
				}
			}
			Configuration config = Configuration.getInstance();
			String dataModelPK = config.getModel();
			String dataClusterPK = config.getCluster();
			String xsd = Util.getPort().getDataModel(new WSGetDataModel(new WSDataModelPK(dataModelPK))).getXsdSchema();
			// get item
	        if(ids!=null){
				WSItem wsItem = Util.getPort().getItem(
						new WSGetItem(new WSItemPK(
								new WSDataClusterPK(dataClusterPK),
								concept, 
								ids
						))
				);
				Document document = Util.parse(wsItem.getContent());
				//update the node according to schema
				if("sequence".equals(com.amalto.core.util.Util.getConceptModelType(concept, xsd))) {
					Node newNode=com.amalto.core.util.Util.updateNodeBySchema(concept, xsd, document.getDocumentElement());
					document=newNode.getOwnerDocument();
				}				
				if(foreignKey) ctx.getSession().setAttribute("itemDocumentFK",document);
				else ctx.getSession().setAttribute("itemDocument"+docIndex,document);
	        }
	        else if(!refresh) {
	        	createItem(concept, docIndex);
	        }
			
			Map<String,XSElementDecl> map = com.amalto.core.util.Util.getConceptMap(xsd);
        	
        	XSComplexType xsct = (XSComplexType)(map.get(concept).getType());
        	
        	HashMap<Integer,XSParticle> idToParticle;
			if(ctx.getSession().getAttribute("idToParticle") == null) {
				idToParticle = new HashMap<Integer,XSParticle>();
			}
			else {
				idToParticle = (HashMap<Integer,XSParticle>) ctx.getSession().getAttribute("idToParticle");
			}
			idToParticle.put(nodeId,xsct.getContentType().asParticle());
			ctx.getSession().setAttribute("idToParticle",idToParticle);
			
			HashMap<Integer,String> idToXpath;
			if(ctx.getSession().getAttribute("idToXpath") == null) {
				idToXpath = new HashMap<Integer,String>();
			}
			else {
				idToXpath = (HashMap<Integer,String>) ctx.getSession().getAttribute("idToXpath");
			}
			idToXpath.put(nodeId,"/"+concept);			
			ctx.getSession().setAttribute("idToXpath",idToXpath);
			
			HashMap<String,XSParticle> xpathToParticle = new HashMap<String,XSParticle>();					
			xpathToParticle.put("/"+concept,xsct.getContentType().asParticle());			
			ctx.getSession().setAttribute("xpathToParticle",xpathToParticle);
			
			ArrayList<String> nodeAutorization = new ArrayList<String>();
			ctx.getSession().setAttribute("nodeAutorization",nodeAutorization);
			
			return "OK";
		} catch (Exception e) {
			e.printStackTrace();
			return "ERROR";
		}		
	}
	private void setChildrenWithKeyMask(int id, String language, boolean foreignKey, int docIndex, boolean maskKey, boolean choice, XSParticle xsp,ArrayList<TreeNode> list,HashMap<String,TreeNode> xpathToTreeNode) throws ParseException{
		//aiming added see 0009563
		if(xsp.getTerm().asModelGroup()!=null){ //is complex type
			XSParticle[] xsps=xsp.getTerm().asModelGroup().getChildren();
			if("choice".equals(xsp.getTerm().asModelGroup().getCompositor().toString()))
				choice = true;
			for (int i = 0; i < xsps.length; i++) {
				setChildrenWithKeyMask(id,language,foreignKey,docIndex,maskKey,choice,xsps[i],list,xpathToTreeNode);
			}
		}
		if(xsp.getTerm().asElementDecl()==null) return;
		//end
		
		WebContext ctx = WebContextFactory.get();	
		HashMap<Integer,XSParticle> idToParticle = 
			(HashMap<Integer,XSParticle>) ctx.getSession().getAttribute("idToParticle");
		HashMap<Integer,String> idToXpath = 
			(HashMap<Integer,String>) ctx.getSession().getAttribute("idToXpath");
		HashMap<String,XSParticle> xpathToParticle = 
			(HashMap<String,XSParticle>) ctx.getSession().getAttribute("xpathToParticle");
		ArrayList<String> nodeAutorization = 
			(ArrayList<String>) ctx.getSession().getAttribute("nodeAutorization");
		Document d = (Document) ctx.getSession().getAttribute("itemDocument"+docIndex);
		String[] keys = (String[]) ctx.getSession().getAttribute("foreignKeys");
		//add by ymli
		/*ArrayList<String> pathToType = 
			(ArrayList<String>) ctx.getSession().getAttribute("pathToType");*/
		
		
		ArrayList<String> roles = new ArrayList<String>();
		try {
			roles = Util.getAjaxSubject().getRoles();
		} catch (PolicyContextException e1) {
			e1.printStackTrace();
		}		

		
		if(foreignKey) d = (Document) ctx.getSession().getAttribute("itemDocumentFK");		
		TreeNode treeNode = new TreeNode();    		
		treeNode.setChoice(choice);
		String xpath = idToXpath.get(id)+"/"+xsp.getTerm().asElementDecl().getName();
		//aiming modify see 9642 some node's parent is null
		String parentxpath=idToXpath.get(id).replaceAll("\\[.*?\\]", ""); //parent xpath maybe A.fileds[1]
		if(xpathToTreeNode.containsKey(parentxpath)) {
			treeNode.setParent(xpathToTreeNode.get(parentxpath));
		}
		//end
		if(xpathToTreeNode.containsKey(idToXpath.get(id)))
			treeNode.setParent(xpathToTreeNode.get(idToXpath.get(id)));
		
		
		int maxOccurs = xsp.getMaxOccurs();   	
		//idToXpath.put(nodeCount,xpath);//keep map <node id -> xpath>  in the session
		treeNode.setName(xsp.getTerm().asElementDecl().getName());
		treeNode.setDocumentation("");
		String typeNameTmp = "";
		treeNode.setVisible(true);
		
//		treeNode.setParent(parentNode);
		
		if(xsp.getTerm().asElementDecl().getType().getName()!=null)	
			typeNameTmp = xsp.getTerm().asElementDecl().getType().getName();
		
		//annotation support
		XSAnnotation xsa = xsp.getTerm().asElementDecl().getAnnotation();
		try {
			treeNode.fetchAnnotations(xsa, roles, language);
		} catch (Exception e1) {
			e1.printStackTrace();
			System.out.println("NO ANNOT");
		}


		treeNode.setTypeName(typeNameTmp);
		treeNode.setXmlTag(xsp.getTerm().asElementDecl().getName());
		treeNode.setNodeId(nodeCount);
		treeNode.setMaxOccurs(maxOccurs);
		treeNode.setMinOccurs(xsp.getMinOccurs());
		treeNode.setNillable(xsp.getTerm().asElementDecl().isNillable());
		ArrayList<String> infos = treeNode.getForeignKeyInfo();
		String keyInfos = "";
		
		if(infos != null && treeNode.isRetrieveFKinfos()) {
			for(String keyInfo : infos)
			{
				keyInfos += keyInfo + ",";
			}
			if(keyInfos.endsWith(","))
			{
				keyInfos = keyInfos.substring(0, keyInfos.length()-1);
//				if(treeNode.getValue() != null)
				{
					try {
					   String value = StringEscapeUtils.escapeHtml(Util.getFirstTextNode(d,xpath));
					   //max occurs > 1 support and do not get foreignkeylist by here.
					   if(value != null && !"".equals(value) && !(maxOccurs<0 || maxOccurs>1)) {
					      String jasonData = Util.getForeignKeyList(0, Integer.MAX_VALUE, value,  treeNode.getForeignKey(), keyInfos, treeNode.getFkFilter(), false);
						   treeNode.setValueInfo(jasonData);
					   }
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}	
				}
			}
		}

		// this child is a complex type
		if(xsp.getTerm().asElementDecl().getType().isComplexType()==true) {    			
			XSParticle particle = xsp.getTerm().asElementDecl()
					.getType().asComplexType().getContentType().asParticle();
			idToParticle.put(nodeCount, particle);    	
			if(!treeNode.isReadOnly()){
				nodeAutorization.add(xpath);
			}
			treeNode.setType("complex");
			
			
			xpathToTreeNode.put(xpath, treeNode);
			if(maxOccurs<0 || maxOccurs>1){	//maxoccurs<0 is unbounded			
				try {
					NodeList nodeList = Util.getNodeList(d,xpath);
					for (int i = 0; i < nodeList.getLength(); i++) { 
						idToXpath.put(nodeCount,xpath+"["+(i+1)+"]");
						xpathToParticle.put(xpath+"["+(i+1)+"]",particle);
						TreeNode treeNodeTmp = (TreeNode) treeNode.clone();
						treeNodeTmp.setNodeId(nodeCount);
						idToParticle.put(nodeCount, particle);
						// TODO check addThisNode
		    			list.add(treeNodeTmp);  
						nodeCount++;
					}
					if(nodeList.getLength() == 0){
	    				idToXpath.put(nodeCount,xpath);
	    				xpathToParticle.put(xpath,particle);
	    				if(treeNode.isVisible()==true) {
	    	    			list.add(treeNode);    			
	    	    			nodeCount++; 
	    	    		} 
					}
				} catch (Exception e) {
					e.printStackTrace();
				}					
			}
			else {
				idToXpath.put(nodeCount,xpath);
				xpathToParticle.put(xpath,particle);
				if(treeNode.isVisible()==true) {
	    			list.add(treeNode);    			
	    			nodeCount++; 
	    		} 
			}
		}
		// this child is a simple type
		else {
			idToParticle.put(nodeCount, null);
			treeNode.setType("simple"); 

			// restriction support
			ArrayList<Restriction> restrictions = new ArrayList<Restriction>();
			ArrayList<String> enumeration = new ArrayList<String>();
			XSRestrictionSimpleType restirctionType = xsp.getTerm().asElementDecl().getType()
				.asSimpleType().asRestriction();
			if(restirctionType != null)
			{
				 Iterator<XSFacet> it = restirctionType.iterateDeclaredFacets();
				 while (it.hasNext()) {
					XSFacet xsf = it.next();
					if("enumeration".equals(xsf.getName())) {
						enumeration.add(StringEscapeUtils.escapeHtml(xsf.getValue().toString()));
					}
					else{
						Restriction r = new Restriction(xsf.getName(),xsf.getValue().toString());
						restrictions.add(r);
					}					
				}
			}
			treeNode.setEnumeration(enumeration);
			treeNode.setRestrictions(restrictions);
			
			// the user cannot edit any field when a foreign key is displayed
			if(foreignKey){
				treeNode.setReadOnly(true);
			}
			for (int i = 0; i < keys.length; i++) {
				if(xpath.equals(keys[i])){
					treeNode.setKey(true);
					treeNode.setKeyIndex(i);
					//treeNode.setReadOnly(true);
				}
					
			}

			
			// max occurs > 1 support
			try { 
				if(maxOccurs<0 || maxOccurs>1){
					NodeList nodeList = Util.getNodeList(d,xpath);
               
					for (int i = 0; i < nodeList.getLength(); i++) {
						if(!treeNode.isReadOnly())
							nodeAutorization.add(xpath+"["+(i+1)+"]");
						idToXpath.put(nodeCount,xpath+"["+(i+1)+"]");
						TreeNode treeNodeTmp = (TreeNode) treeNode.clone();
						
						if(nodeList.item(i).getFirstChild() != null && infos != null && treeNode.isRetrieveFKinfos() && treeNode.getForeignKey() != null) {
						   String value = StringEscapeUtils.escapeHtml(nodeList.item(i).getTextContent());
						   String jasonData = Util.getForeignKeyList(0, Integer.MAX_VALUE, value,  treeNode.getForeignKey(), keyInfos, treeNode.getFkFilter(), false);
						   treeNodeTmp.setValueInfo(jasonData);
						}
												
						if(nodeList.item(i).getFirstChild()!=null)
						{
							treeNodeTmp.setValue(nodeList.item(i).getFirstChild().getNodeValue());
							if(treeNodeTmp.getValueInfo() != null)
							{
								JSONObject jason = new JSONObject(treeNodeTmp.getValueInfo());
								JSONArray rows = (JSONArray)jason.get("rows");
								for(int n = 0; n < rows.length(); n++)
								{
									JSONObject row = (JSONObject)rows.get(n);
									String keyValue = (String)row.get("keys");
									if(keyValue.equals(treeNodeTmp.getValue()))
									{
										treeNodeTmp.setValueInfo(StringEscapeUtils.escapeHtml(row.getString("infos")));
										break;
									}
								}
								if(rows.length() == 0)
								{
									treeNodeTmp.setValueInfo(null);
								}
							}

						}
						treeNodeTmp.setNodeId(nodeCount);
						// TODO check addThisNode
		    			list.add(treeNodeTmp);  
						nodeCount++;
					}
					if(nodeList.getLength() == 0){
						if(!treeNode.isReadOnly())
							nodeAutorization.add(xpath);
    					idToXpath.put(nodeCount,xpath);
    		    		if(treeNode.isVisible()==true){
    		    			list.add(treeNode);    			
    		    			nodeCount++; 
    		    		}  
					}
				}
				else{
					if(!treeNode.isReadOnly())
						nodeAutorization.add(xpath);
					idToXpath.put(nodeCount,xpath);					
					
					treeNode.setValue(StringEscapeUtils.escapeHtml(Util.getFirstTextNode(d,xpath)));
					
					//key is readonly for editing record.
					if(treeNode.isKey() && treeNode.getValue() != null) {
					   treeNode.setReadOnly(true);
					}
					
					if(treeNode.getValueInfo() != null)
					{
						JSONObject jason = new JSONObject(treeNode.getValueInfo());
						JSONArray rows = (JSONArray)jason.get("rows");
						treeNode.setValueInfo(null);
						for(int n = 0; n < rows.length(); n++)
						{
							JSONObject row = (JSONObject)rows.get(n);
							String keyValue = (String)row.get("keys");
							String compValue=treeNode.getValue();
							if(compValue!=null&&!compValue.startsWith("["))compValue="["+compValue+"]";
							if(keyValue.equals(compValue))
							{
								treeNode.setValueInfo(StringEscapeUtils.escapeHtml(row.getString("infos")));								
								break;
							}
							else if(treeNode.getValue() == null)
							{
								treeNode.setValueInfo(null);
								break;
							}
						}
						
						if(rows.length() == 0)
						{
							treeNode.setValueInfo(null);
						}
						
					}

		    		if(treeNode.isVisible()==true){
		    			list.add(treeNode);    			
		    			nodeCount++; 
		    		}  
		    		xpathToTreeNode.put(xpath, treeNode);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		if(maskKey&&treeNode.isKey()){
			String oldPath=treeNode.getValue();
			treeNode.setValue("");
			if(treeNode.getTypeName().trim().toUpperCase().equals("UUID")||treeNode.getTypeName().trim().toUpperCase().equals("AUTO_INCREMENT")){
				treeNode.setReadOnly(true);
			}else{
				treeNode.setReadOnly(false);
			}
			
			
			HashMap<String,UpdateReportItem> updatedPath;
			if(ctx.getSession().getAttribute("updatedPath")!=null){
				updatedPath = (HashMap<String,UpdateReportItem>) ctx.getSession().getAttribute("updatedPath");
			}				
			else{
				updatedPath = new HashMap<String,UpdateReportItem>();
			}
			ctx.getSession().setAttribute("updatedPath",updatedPath);
			updatedPath.put(xpath, new UpdateReportItem(xpath,oldPath,""));
			
		}
		
	}	
	private int nodeCount; //aiming added to record the node count;
	/**
	 * give the children of a node
	 * @param id the id of the node in yui
	 * @param nodeCount the internal count of nodes in yui tree
	 * @param language
	 * @return an array of TreeNode
	 * @throws ParseException 
	 */
//	TreeNode parentNode,
	public TreeNode[] getChildren( int id, int nodeCount, String language, boolean foreignKey, int docIndex) throws ParseException{
		return getChildrenWithKeyMask(id, nodeCount, language, foreignKey, docIndex, false);
	}
		
	public TreeNode[] getChildrenWithKeyMask(int id, int nodeCount, String language, boolean foreignKey, int docIndex, boolean maskKey) throws ParseException{
		WebContext ctx = WebContextFactory.get();	
		HashMap<Integer,XSParticle> idToParticle = 
			(HashMap<Integer,XSParticle>) ctx.getSession().getAttribute("idToParticle");
		HashMap<Integer,String> idToXpath = 
			(HashMap<Integer,String>) ctx.getSession().getAttribute("idToXpath");
		HashMap<String,XSParticle> xpathToParticle = 
			(HashMap<String,XSParticle>) ctx.getSession().getAttribute("xpathToParticle");
		ArrayList<String> nodeAutorization = 
			(ArrayList<String>) ctx.getSession().getAttribute("nodeAutorization");
		Document d = (Document) ctx.getSession().getAttribute("itemDocument"+docIndex);
		String[] keys = (String[]) ctx.getSession().getAttribute("foreignKeys");
		
		HashMap<String,TreeNode> xpathToTreeNode = 
			(HashMap<String,TreeNode>)ctx.getSession().getAttribute("xpathToTreeNode");
		
		if(xpathToTreeNode==null)
			xpathToTreeNode = new HashMap<String, TreeNode>();
		
		if(foreignKey) d = (Document) ctx.getSession().getAttribute("itemDocumentFK");
		
		boolean choice = false;
		ArrayList<String> roles = new ArrayList<String>();
		try {
			roles = Util.getAjaxSubject().getRoles();
		} catch (PolicyContextException e1) {
			e1.printStackTrace();
		}
	
		XSParticle[] xsp = null;
		if(idToParticle==null) return null;
		if(idToParticle.get(id)==null){//simple type case, no children
			return null;
		}
		this.nodeCount=nodeCount;//aiming added	
		xsp = idToParticle.get(id).getTerm().asModelGroup().getChildren();
		if("choice".equals(idToParticle.get(id).getTerm().asModelGroup().getCompositor().toString()))
			choice = true;

		ArrayList<TreeNode> list = new ArrayList<TreeNode>();
		//iterate over children
    	for (int j = 0; j < xsp.length; j++) {
    		setChildrenWithKeyMask(id,language,foreignKey,docIndex,maskKey,choice,xsp[j],list,xpathToTreeNode);
		}		
    	if(xpathToTreeNode!=null){
    		ctx.getSession().setAttribute("xpathToTreeNode", xpathToTreeNode);
    	}
		return list.toArray(new TreeNode[list.size()]); 
	}
	
	private void clearChildrenValue(Node node){
		if(node.getFirstChild()!=null && node.getFirstChild().getNodeType()==Node.TEXT_NODE){
			node.getFirstChild().setNodeValue("");
		}
		NodeList list = node.getChildNodes();
		for (int i = 0; i < list.getLength(); i++) {
			clearChildrenValue(list.item(i));
		}
	}
	
	public String cloneNode(int siblingId, int newId, int docIndex) throws Exception{
		
			WebContext ctx = WebContextFactory.get();
		HashMap<Integer,XSParticle> idToParticle = 
			(HashMap<Integer,XSParticle>) ctx.getSession().getAttribute("idToParticle");
		HashMap<Integer,String> idToXpath = 
			(HashMap<Integer,String>) ctx.getSession().getAttribute("idToXpath");
		ArrayList<String> nodeAutorization = 
			(ArrayList<String>) ctx.getSession().getAttribute("nodeAutorization");
		XSParticle xsp = idToParticle.get(siblingId);
		// associate the new id node to the particle of his sibling
		idToParticle.put(newId,xsp);
		Document d = (Document) ctx.getSession().getAttribute("itemDocument"+docIndex);		
		try {
			
			Node node = Util.getNodeList(d,idToXpath.get(siblingId)).item(0);
			//System.out.println(Util.getNodeList(d,idToXpath.get(siblingId)).getLength()+" "+idToXpath.get(siblingId));
			Node nodeClone = node.cloneNode(true);
			clearChildrenValue(nodeClone);
			// simulate an "insertAfter()" which actually doesn't exist
			insertAfter(nodeClone,node);
/*			if(node.getNextSibling()!=null)
				node.getParentNode().insertBefore(nodeClone,node.getNextSibling());
			else
				node.getParentNode().appendChild(nodeClone);
*/			
			String siblingXpath = idToXpath.get(siblingId).replaceAll("\\[\\d+\\]$","");
			
			//int id = Util.getNodeList(d,siblingXpath).getLength();
			//String exist = idToXpath.get(newId);
			
			int siblingIndex = getXpathIndex(idToXpath.get(siblingId));
			
			//idToXpath.put(newId,siblingXpath+"["+id+"]");
			
			
			editXpathInidToXpathAdd(siblingId, idToXpath);
			
			idToXpath.put(newId,siblingXpath+"["+(siblingIndex+1)+"]");
			ctx.getSession().setAttribute("idToXpath", idToXpath);
			
			//updateNode(siblingIndex+1, "", siblingIndex+1);
			
			//System.out.println("clone:"+newId+" "+siblingXpath+"["+id+"]");
			UpdateReportItem ri = new UpdateReportItem(idToXpath.get(newId), "", "");
			HashMap<String, UpdateReportItem> updatedPath;
         if (ctx.getSession().getAttribute("updatedPath") != null) {
            updatedPath = (HashMap<String, UpdateReportItem>) ctx
                  .getSession().getAttribute("updatedPath");
         } else {
            updatedPath = new HashMap<String, UpdateReportItem>();
         }
         	updatedPath.put(siblingXpath+"["+(siblingIndex+1)+"]", ri);
			ctx.getSession().setAttribute("updatedPath", updatedPath);
			
			nodeAutorization.add(siblingXpath+"["+(siblingIndex+1)+"]");
			ctx.getSession().setAttribute("nodeAutorization",nodeAutorization);
			return "Cloned";
			
		} catch (Exception e) {
			e.printStackTrace();	
			return "Error";
		}
//		System.out.println("xml"+CommonDWR.getXMLStringFromDocument(d));
	
	}
	
	public void updateKeyNodesToEmptyInItemDocument(int docIndex) throws TransformerException{
		WebContext ctx = WebContextFactory.get();
		String[] keys = (String[]) ctx.getSession().getAttribute("foreignKeys");
		for (int i = 0; i < keys.length; i++) {
			try {
				Document d = (Document) ctx.getSession().getAttribute("itemDocument" + docIndex);
				//Document d2 = checkNode(keys[i], d);
				String oldValue = Util.getFirstTextNode(d, keys[i]);
				if (oldValue == null)
					Util.getNodeList(d, keys[i]).item(0).appendChild(
							d.createTextNode(""));
				else
					Util.getNodeList(d, keys[i]).item(0).getFirstChild()
							.setNodeValue("");
			} catch (Exception e) {
				e.printStackTrace();
			}
				
		}
	}
	
    public String validateItem(int docIndex) throws TransformerException
    {
    	try {
    		WebContext ctx = WebContextFactory.get();
    		Document d = (Document) ctx.getSession().getAttribute("itemDocument"+docIndex);
    		String concept = d.getDocumentElement().getLocalName();
    		String xmlCont = Util.nodeToString(d);
    		Element root=(Element)Util.parse(xmlCont).getDocumentElement();			
    		Node node=root;
        		Configuration config = Configuration.getInstance(true);
        		String schema = Util.getPort().getDataModel(
                		new WSGetDataModel(new WSDataModelPK(config.getModel()))).getXsdSchema();
            	if(com.amalto.core.util.Util.getUUIDNodes(schema, concept).size()>0){ //check uuid key exists
        	    	String dataCluster=config.getCluster();
        			node=com.amalto.core.util.Util.processUUID(root, schema, dataCluster, concept,true);
            	}
        		com.amalto.core.util.Util.validate((Element)node, schema);
    		} catch (Exception e) {
    	    	String prefix = "Unable to create/update the item " + ": ";
                String err = prefix +": "+e.getLocalizedMessage();
                //org.apache.log4j.Logger.getLogger(this.getClass()).error(err,e);
                //throw new TransformerException(e.getLocalizedMessage());
                return err;
    		}
    		
    		return "";

    }
    
	public String updateNode(int id, String content, int docIndex) throws TransformerException{
		WebContext ctx = WebContextFactory.get();
		HashMap<Integer,String> idToXpath = 
			(HashMap<Integer,String>) ctx.getSession().getAttribute("idToXpath");
		String xpath = idToXpath.get(id);
		if(xpath==null) return "Nothing to update";
		return updateNode2(xpath,StringEscapeUtils.unescapeHtml(content),docIndex);
	}
	
	public static String updateNode2(String xpath, String content, int docIndex) throws TransformerException{
		WebContext ctx = WebContextFactory.get();
		Document d = (Document) ctx.getSession().getAttribute("itemDocument"+docIndex);
		HashMap<Integer,String> idToXpath = 
			(HashMap<Integer,String>) ctx.getSession().getAttribute("idToXpath");
		ArrayList<String> nodeAutorization = 
			(ArrayList<String>) ctx.getSession().getAttribute("nodeAutorization");
		/*for (Iterator iter = nodeAutorization.iterator(); iter.hasNext();) {
			String element = (String) iter.next();
			System.out.println("autorisation "+element);
		}*/	

		//TODO
		
		int i = xpath.lastIndexOf("/");
		String subXpath = xpath.substring(0, i);
//		if(!nodeAutorization.contains(xpath) 
//				&& !nodeAutorization.contains(xpath.replaceAll("\\[.*\\]",""))&&!nodeAutorization.contains(subXpath)){
//			return "Not authorized";
//		}
		try {			
			Document d2 = checkNode(xpath, d);
			String oldValue = Util.getFirstTextNode(d,xpath);
			if(content.equals(oldValue))
				return "Nothing to update";
			//Util.getNodeList(d, xpath).item(0).setTextContent(content);
			if(oldValue==null)
				Util.getNodeList(d, xpath).item(0).appendChild(d.createTextNode(content));
			else
				Util.getNodeList(d, xpath).item(0).getFirstChild().setNodeValue(content);
			//TODO add path to session
			HashMap<String,UpdateReportItem> updatedPath;
			if(ctx.getSession().getAttribute("updatedPath")!=null){
				updatedPath = (HashMap<String,UpdateReportItem>) ctx.getSession().getAttribute("updatedPath");
			}				
			else{
				updatedPath = new HashMap<String,UpdateReportItem>();
			}			
			if(updatedPath.get(xpath)!=null) {
				oldValue = updatedPath.get(xpath).getOldValue();
			}
			UpdateReportItem item = new UpdateReportItem(xpath,oldValue,content);
			updatedPath.put(xpath,item);
			ctx.getSession().setAttribute("updatedPath",updatedPath);
			return "Node updated";
		} 
		catch (Exception e2) {
				e2.printStackTrace();
				return "Error";
		}
	}
	
	private static Document checkNode(String xpath, Document d) throws Exception{
		// try each element of the xpath and check if it exists in datamodel
		if(xpath.charAt(0)=='/') {
			xpath = xpath.substring(1);
		}
		String[] elements = xpath.split("/");
		String xpathParent = "/";
		for (int i = 0; i < elements.length; i++) {				
			if(CommonDWR.getNodeList(d, xpathParent+"/"+elements[i]).getLength()==0){
				d = createNode(xpathParent, elements[i], d);
			}
			if(i==0) xpathParent = "/"+elements[i];
			else xpathParent += "/"+elements[i];
		}
		return d;
	}
	
	private static Document createNode(String xpathParent, String nodeToBeCreated, Document d) throws Exception {
		WebContext ctx = WebContextFactory.get();
		HashMap xpathToParticle = 
			(HashMap) ctx.getSession().getAttribute("xpathToParticle");
		
		Element el = d.createElement(nodeToBeCreated);
		XSParticle[] xsp = null;
		
		xsp = ((XSParticle) xpathToParticle.get(xpathParent)).getTerm().asModelGroup().getChildren();

		String elementAfter ="";
		for (int i = 0; i < xsp.length; i++) {
			String element = xsp[i].getTerm().asElementDecl().getName();
			if(nodeToBeCreated.equals(element)){
				//System.out.println("found");		
				if(i==xsp.length-1){
					//System.out.println("case append child 1");
					Node parent = Util.getNodeList(d,xpathParent).item(0);
					parent.appendChild(el);
					return d;
				}
				for (int j = 0; j < xsp.length-i-1; j++) {
					elementAfter = xpathParent+"/"+xsp[i+j+1].getTerm().asElementDecl().getName();
					//System.out.println("element after : "+elementAfter);
					Node node = Util.getNodeList(d,elementAfter).item(0);
					if(node!=null){
						node.getParentNode().insertBefore(el,node);
						return d;
					}	
				}
				
				
				//TODO
				{
					System.out.println("case append child 2");
					Node parent = Util.getNodeList(d,xpathParent).item(0);
					parent.appendChild(el);
				}
			}
		}
		return d;
	}
	/**
	 * add by ymli
	 * if ListItem with xpath like '/PurchaseOrder/ListItems/POItem[i] is deleted, the xpath in idToXpath will be edited , 
	 * eg.
	 *  '/PurchaseOrder/ListItems/POItem[j]'(j>i), j--
	 * 
	 */
	public void editXpathInidToXpath(int id ,HashMap<Integer,String> idToXpath){
		String nodeXpath = idToXpath.get(id).replaceAll("\\[\\d+\\]$","");
		String patternXpath = nodeXpath.replaceAll("\\[", "\\\\[");
		patternXpath = patternXpath.replaceAll("\\]", "\\\\]");;
		Pattern p = Pattern.compile("(.*?)(\\[)(\\d+)(\\]$)");
		Matcher m = p.matcher(idToXpath.get(id));
		int nodeIndex = -1;
		if (m.matches()) 
			nodeIndex =  Integer.parseInt(m.group(3));
		Iterator<Integer> keys = idToXpath.keySet().iterator();
		while(keys.hasNext()){
			int key = keys.next();
			String xpath = idToXpath.get(key);
			
			if(xpath.matches(patternXpath+"\\[\\d+\\]$")){
				int pathIndex = -1;
				Matcher m1 = p.matcher(xpath);
				if (m1.matches()) 
					pathIndex =  Integer.parseInt(m1.group(3));
				if(nodeIndex<pathIndex){
					pathIndex--;
					xpath = nodeXpath+"["+pathIndex+"]";
//					keys.remove();
//					idToXpath.remove(key);
					idToXpath.put(key, xpath);
					
				}//if(nodeIndex
				
			}//if(xpath
		}//	while(keys.
	}
	
	
	/**
	 * add by ymli
	 * if ListItem with xpath like '/PurchaseOrder/ListItems/POItem[i] is add, the xpath in idToXpath will be edited , 
	 * eg.
	 *  '/PurchaseOrder/ListItems/POItem[j]'(j>i), j++
	 * 
	 */
	public void  editXpathInidToXpathAdd(int id ,HashMap<Integer,String> idToXpath){
		/*String nodeXpath = idToXpath.get(id).replace("\\[\\d+\\]$","");
		String patternXpath = nodeXpath.replaceAll("\\[", "\\\\[");
		patternXpath = patternXpath.replaceAll("\\]", "\\\\]");*/
		int beginIndex = idToXpath.get(id).lastIndexOf("[");
		int endIndex = idToXpath.get(id).lastIndexOf("]");
		
		String patternXpath = idToXpath.get(id).substring(0, beginIndex);
		
		//Pattern p = Pattern.compile("(.*?)(\\[)(\\d+)(\\])");
		//Pattern p1 = Pattern.compile("(.*?)(\\[)(\\d+)(\\])(.*?)(\\[)(\\d+)(\\])");
		Pattern p1 = Pattern.compile(patternXpath+"(\\[)(\\d+)(\\])(.*?)");
		/*Matcher m = p1.matcher(idToXpath.get(id));
		int nodeIndex = -1;
		if (m.matches()) 
			for(int i=0;i<m.groupCount();i++){
				System.out.println(m.group(i));
			}*/
			//nodeIndex =  Integer.parseInt(m.group(7));
		int nodeIndex =  Integer.parseInt(idToXpath.get(id).substring(beginIndex+1, endIndex)) ;
		
		
		
		Iterator<Integer> keys = idToXpath.keySet().iterator();
		while(keys.hasNext()){
			int key = keys.next();
			String xpath = idToXpath.get(key);
			String lastString = "";
			//if(xpath.matches(patternXpath+"\\[\\d+\\](.*?)")){
			int xpathIndex = xpath.indexOf(patternXpath);
			if(xpathIndex>=0){
				int pathIndex = -1;
				//Matcher m2 = p1.matcher(xpath);
				//if(m2.matches()){
				String lastSubString = xpath.substring(xpathIndex+patternXpath.length());
				int beginIndex1= lastSubString.indexOf("[");
				int endIndex1= lastSubString.indexOf("]");
				pathIndex = Integer.parseInt(lastSubString.substring(beginIndex1+1, endIndex1));
				lastString = lastSubString.substring(endIndex1+1);
				if(nodeIndex<pathIndex){
					pathIndex++;
					xpath = patternXpath+"["+pathIndex+"]"+lastString;
					idToXpath.put(key, xpath);
					
				}//if(nodeIndex
				
			}//if(xpath
		}//	while(keys.
	}
	
	
	/**
	 * add by ymli
	 * if ListItem with xpath like '/PurchaseOrder/ListItems/POItem[i] is deleted, the xpath in idToXpath will be edited , 
	 * eg.
	 *  '/PurchaseOrder/ListItems/POItem[j]'(j>i), j--
	 * 
	 */
	public void editUpdatedPath(HashMap<String,UpdateReportItem>updatedPath,String xpath){
		String subXpath = xpath.replaceAll("\\[\\d+\\]$", "");
		
		int b = xpath.indexOf("[")+1;
		int e = xpath.indexOf("]");
		int nodeIndex = Integer.parseInt((String) xpath.subSequence(b, e));
		Iterator<String> keys = updatedPath.keySet().iterator();
		while(keys.hasNext()){
			String key = keys.next();
			if(key.matches(subXpath+"\\[\\d+\\]$")){
				int star = key.indexOf("[")+1;
				int end = key.indexOf("]");
				int pathIndex = Integer.parseInt((String) key.subSequence(star, end));
				if(nodeIndex<pathIndex){
					UpdateReportItem report = updatedPath.get(key);
					keys.remove();
					updatedPath.remove(key);
					pathIndex--;
					xpath = subXpath+"["+pathIndex+"]";
					updatedPath.put(xpath, report);
				}
			}
		}
	}
	
	/**
	 * count the number of path which is >= index
	 * @author ymli
	 * @param updatedPath
	 * @param index
	 * @return
	 */
	private int getCountOfsmaller(HashMap<String,UpdateReportItem> updatedPath,int index){
		int count = 0;
		Set<String> keys = updatedPath.keySet();
		for(Iterator it = keys.iterator();it.hasNext();){
			Pattern p = Pattern.compile("(.*?)(\\[)(\\d+)(\\]$)");
			Matcher m = p.matcher((String)it.next());
			if(m.matches()){
				String nodeXpath = m.group(1);
				int pathIndex = -1;
				pathIndex =  Integer.parseInt(m.group(3));
				if(pathIndex>=index)
					count++;
		}
		}
		return count;
	}
	
	private int getXpathIndex(String xpath){
		int pathIndex = -1;
			Pattern p = Pattern.compile("(.*?)(\\[)(\\d+)(\\]$)");
			Matcher m = p.matcher(xpath);
			if(m.matches()){
				String nodeXpath = m.group(1);
				
				pathIndex =  Integer.parseInt(m.group(3));
				return pathIndex;
			}
			return pathIndex;
	}

	public String removeNode(int id, int docIndex, String oldValue) {
		WebContext ctx = WebContextFactory.get();
		HashMap<Integer, String> idToXpath = (HashMap<Integer, String>) ctx
				.getSession().getAttribute("idToXpath");
		Document d = (Document) ctx.getSession().getAttribute(
				"itemDocument" + docIndex);
		/*
		 * try { System.out.println("Document:"+Util.nodeToString(d)); } catch
		 * (Exception e1) { // TODO Auto-generated catch block
		 * e1.printStackTrace(); }
		 */
		try {

			// System.out.println("remove:"+id+" "+idToXpath.get(id));
			Util
					.getNodeList(d, idToXpath.get(id))
					.item(0)
					.getParentNode()
					.removeChild(Util.getNodeList(d, idToXpath.get(id)).item(0));
			// add by ymli
			HashMap<String, UpdateReportItem> updatedPath;
			if (ctx.getSession().getAttribute("updatedPath") != null) {
				updatedPath = (HashMap<String, UpdateReportItem>) ctx
						.getSession().getAttribute("updatedPath");
			} else {
				updatedPath = new HashMap<String, UpdateReportItem>();
			}
			UpdateReportItem ri = new UpdateReportItem(idToXpath.get(id),
					oldValue, "");

			// editUpdatedPath(updatedPath,idToXpath.get(id));
			editXpathInidToXpath(id, idToXpath);
			//add by ymli. fix the bug:0010576. edit the path
			String path = idToXpath.get(id);
			if (updatedPath.get(idToXpath.get(id)) != null) {
				path = updatedPath.get(idToXpath.get(id)).getPath();
				// if(path.equals(idToXpath.get(id))){

				Pattern p = Pattern.compile("(.*?)(\\[)(\\d+)(\\]$)");
				Matcher m = p.matcher(path);
				if (m.matches()) {
					String nodeXpath = m.group(1);
					int pathIndex = -1;
					pathIndex = Integer.parseInt(m.group(3));
					pathIndex += getCountOfsmaller(updatedPath, pathIndex);// updatedPath.size();
					path = nodeXpath + "[" + pathIndex + "]";
					ri.setPath(path);
				}
			}
			// }

			// updatedPath.put(idToXpath.get(id),ri);
			updatedPath.put(path, ri);
			idToXpath.remove(id);
			ctx.getSession().setAttribute("idToXpath", idToXpath);
			ctx.getSession().setAttribute("updatedPath", updatedPath);
			return "Deleted";
		} catch (Exception e) {
			e.printStackTrace();
			return "Error";
		}
	}
	

	public static boolean isDataClusterExists(String dataClusterPK)throws Exception{
		WSExistsDataCluster wsExistsDataCluster=new WSExistsDataCluster();
		wsExistsDataCluster.setWsDataClusterPK(new WSDataClusterPK(dataClusterPK));
		WSBoolean wsBoolean=Util.getPort().existsDataCluster(wsExistsDataCluster);
		return wsBoolean.is_true();
	}
	
	//back up for old revision of save item
//	public static String saveItem(String[] ids, String concept, boolean newItem, int docIndex) throws Exception{
//		WebContext ctx = WebContextFactory.get();		
//		try {		
//			Configuration config = Configuration.getInstance();
//			String dataModelPK = config.getModel();
//			String dataClusterPK = config.getCluster();
//			Document d = (Document) ctx.getSession().getAttribute("itemDocument"+docIndex);
//			String xml = CommonDWR.getXMLStringFromDocument(d);
//			xml = xml.replaceAll("<\\?xml.*?\\?>","");	
//			//<?xml version="1.0" encoding="UTF-8"?>
//			org.apache.log4j.Logger.getLogger(ItemsBrowserDWR.class).debug("saveItem() "+xml);
//			WSItemPK wsi = Util.getPort().putItem(
//					new WSPutItem(
//							new WSDataClusterPK(dataClusterPK), 
//							xml,
//							new WSDataModelPK(dataModelPK)));	
//			ctx.getSession().setAttribute("viewNameItems",null);
//			String operationType = "";
//			if(newItem==true) operationType = "CREATE";
//			else operationType = "UPDATE";
//			String result = pushUpdateReport(wsi.getIds(),concept,operationType);		
//			return result;
//		}
//		catch(Exception e){			
//			String err= "Unable to save item '"+concept+"."+Util.joinStrings(ids, ".")+"'";
//			org.apache.log4j.Logger.getLogger(ItemsBrowserDWR.class).error(err,e);
//			throw new Exception(e.getLocalizedMessage());
//		}		
//
//	}
	
	public static String saveItem(String[] ids, String concept, boolean newItem, int docIndex) throws Exception{
		WebContext ctx = WebContextFactory.get();		
		try {		
			Configuration config = Configuration.getInstance();
			String dataModelPK = config.getModel();
			String dataClusterPK = config.getCluster();
			if(dataModelPK==null||dataModelPK.trim().length()==0)throw new Exception("Data Model can't be empty!");
			if(dataClusterPK==null||dataClusterPK.trim().length()==0)throw new Exception("Data Container can't be empty!");
			Document d = (Document) ctx.getSession().getAttribute("itemDocument"+docIndex);
			String xml = CommonDWR.getXMLStringFromDocument(d);
			xml = xml.replaceAll("<\\?xml.*?\\?>","");
			xml=xml.replaceAll("\\(Auto\\)", "");			
			//<?xml version="1.0" encoding="UTF-8"?>
			org.apache.log4j.Logger.getLogger(ItemsBrowserDWR.class).debug("saveItem() "+xml);
			
			ctx.getSession().setAttribute("viewNameItems",null);
			String operationType = "";
			if(newItem==true) operationType = "CREATE";
			else operationType = "UPDATE";
			
			//check updatedPath
			HashMap<String,UpdateReportItem> updatedPath = new HashMap<String,UpdateReportItem>();
			updatedPath = (HashMap<String,UpdateReportItem>) ctx.getSession().getAttribute("updatedPath");
			if(!"DELETE".equals(operationType) && updatedPath==null){
				return "ERROR_2";
			}
			//create updateReport
//			String resultUpdateReport = createUpdateReport(ids, concept, operationType, updatedPath);
			
//			//check before saving transformer
//			boolean isBeforeSavingTransformerExist=false;
//			WSTransformerPK[] wst = Util.getPort().getTransformerPKs(new WSGetTransformerPKs("*")).getWsTransformerPK();
//			for (int i = 0; i < wst.length; i++) {
//				if(wst[i].getPk().equals("beforeSaving_"+concept)){
//					isBeforeSavingTransformerExist=true;
//					break;
//				}
//			}
//			//call before saving transformer
//			if(isBeforeSavingTransformerExist){
//				
//				try {
//					WSTransformerContext wsTransformerContext = new WSTransformerContext(
//							new WSTransformerV2PK("beforeSaving_" + concept),
//							null, null);
//					String exchangeData = mergeExchangeData(xml,resultUpdateReport);
//					//String exchangeData = resultUpdateReport;
//					WSTypedContent wsTypedContent = new WSTypedContent(null,
//							new WSByteArray(exchangeData
//									.getBytes("UTF-8")),
//							"text/xml; charset=utf-8");
//					WSExecuteTransformerV2 wsExecuteTransformerV2 = new WSExecuteTransformerV2(
//							wsTransformerContext, wsTypedContent);
//					//TODO process no plug-in issue
//					WSTransformerContextPipelinePipelineItem[] entries = Util
//							.getPort().executeTransformerV2(
//									wsExecuteTransformerV2).getPipeline()
//							.getPipelineItem();
//					String outputErrorMessage = "";
//					//Scan the entries - in priority, taka the content of the 'output_error_message' entry, 
//					for (int i = 0; i < entries.length; i++) {
//						if ("output_error_message".equals(entries[i]
//								.getVariable())) {
//							outputErrorMessage = new String(entries[i]
//									.getWsTypedContent().getWsBytes()
//									.getBytes(), "UTF-8");
//							break;
//						}
//					}
//					//handle error message
//					if (outputErrorMessage.length() > 0) {
//
//						String errorCode = "";
//						String errorMessage = "";
//						Pattern pattern = Pattern
//								.compile("<error code=['\042](.*)['\042]>(.*)</error>");
//						Matcher matcher = pattern.matcher(outputErrorMessage);
//						while (matcher.find())
//
//						{
//							errorCode = matcher.group(1);
//							errorMessage = matcher.group(2);
//
//						}
//						if (!errorCode.equals("") && !errorCode.equals("0")) {
//							errorMessage = "ERROR_3:" + errorMessage;							
//							return errorMessage;
//						}
//
//					}
//				} catch (Exception e) {
//					e.printStackTrace();
//					String err = "Unable to save item '" + concept + "." + Util.joinStrings(ids, ".") + "'" + e.getLocalizedMessage();
//                    return err;
//				}
//			}
			
			
			//put item
			boolean isUpdateThisItem=true;
			if(newItem==true) isUpdateThisItem = false;

			WSItemPK wsi = Util.getPort().putItemWithReport(new WSPutItemWithReport(
					new WSPutItem(
							new WSDataClusterPK(dataClusterPK), 
							xml,
							new WSDataModelPK(dataModelPK),isUpdateThisItem),"genericUI",true));
			synchronizeUpdateState(ctx);
			if(wsi!=null) {
				//put update report
				ctx.getSession().setAttribute("treeIdxToIDS" + docIndex, wsi.getIds());
				//update update report key
//				if(resultUpdateReport!=null && wsi!=null) {
//					resultUpdateReport=resultUpdateReport.replaceFirst("<Key>.*</Key>", "<Key>"+Util.joinStrings(wsi.getIds(),".")+"</Key>"); 					
//					return persistentUpdateReport(resultUpdateReport,true);
//				}
			}
			return "OK";
		}
		catch(Exception e){			
			String err= "Unable to save item '"+concept+"."+Util.joinStrings(ids, ".")+"'"+e.getLocalizedMessage();
			//org.apache.log4j.Logger.getLogger(ItemsBrowserDWR.class).error(err,e);
			//throw e;
			return err;
		}
	}


	private static String mergeExchangeData(String xml,
			String resultUpdateReport) {
		String exchangeData="<exchange>\n";
		exchangeData+="<report>"+resultUpdateReport+"</report>";
		exchangeData+="\n";
		exchangeData+="<item>"+xml+"</item>";
		exchangeData+="\n</exchange>";
		return exchangeData;
	}
	
	
	public String deleteItem(String concept, String[] ids) {
		WebContext ctx = WebContextFactory.get();
		try {
			Configuration config = Configuration.getInstance();
			String dataClusterPK = config.getCluster();
			
				if(com.amalto.core.util.Util.beforeDeleting(dataClusterPK,concept,ids)){
					return "OK - But go through the beforeDeleting transformer first";
				}
			
			TreeNode rootNode = getRootNode(concept, "en");
	        if(ids!=null && !rootNode.isReadOnly()){
				WSItemPK wsItem = Util.getPort().deleteItem(
						new WSDeleteItem(new WSItemPK(
								new WSDataClusterPK(dataClusterPK),
								concept, ids
								)));
				if(wsItem!=null)
					pushUpdateReport(ids,concept, "DELETE");
				else
					return "ERROR - delteItem is NULL";
				ctx.getSession().setAttribute("viewNameItems",null);
				return "OK";
	        }
	        else {
	        	return "OK - But no update report";
	        }
		}
		catch(Exception e){
			return "ERROR -" + e.getLocalizedMessage();
		}       
	}
	
	public String[] getUriArray(String concept, String[] ids){
		Configuration config;
		List<String> uriList=new ArrayList<String>();
		try {
			config = Configuration.getInstance();
		String dataClusterPK = config.getCluster();
		String content="";
		WSItemPK wsItem=new WSItemPK(new WSDataClusterPK(dataClusterPK),concept,ids);
    	if(wsItem!=null)
    		content=Util.getPort().getItem(new WSGetItem(wsItem))
					.getContent();
   	 for (Iterator iterator = parsXMLString(content).getRootElement().nodeIterator(); iterator.hasNext();) {
   		org.jboss.dom4j.Node node = ( org.jboss.dom4j.Node) iterator.next();
			if(node.getStringValue().startsWith("/imageserver"))
				{	uriList.add(node.getStringValue());
 				}
		}
    	System.out.println(uriList.toArray());
		} catch (Exception e) {
			e.printStackTrace();
		}
		String[] uriArray=new String[uriList.size()];
		for (int i = 0; i < uriList.size(); i++) {
			uriArray[i]=uriList.get(i);
		}
		return uriArray;
		}

	public String logicalDeleteItem(String concept, String[] ids, String path, int docIndex)
	{
		WebContext ctx = WebContextFactory.get();
		try {
			Configuration config = Configuration.getInstance();
			String dataClusterPK = config.getCluster();
			TreeNode rootNode = getRootNode(concept, "en");
	        if(ids!=null && !rootNode.isReadOnly()){
				Document d = (Document) ctx.getSession().getAttribute("itemDocument"+docIndex);
				String xml=null;
				if(d==null){//get item from db
					WSItem item=Util.getPort().getItem(new WSGetItem(new WSItemPK(new WSDataClusterPK(dataClusterPK), concept, ids)));
					xml=item.getContent();
				}else{
					xml = CommonDWR.getXMLStringFromDocument(d);
				}
	        	WSDroppedItemPK wsItem = Util.getPort().dropItem(
						new WSDropItem(new WSItemPK(
								new WSDataClusterPK(dataClusterPK),
								concept, ids
								), path));
				if(wsItem!=null && xml!=null)
					if("/".equalsIgnoreCase(path)){
						pushUpdateReport(ids,concept, "DELETE");
					}else{//part delete consider as 'UPDATE'																		
						xml = xml.replaceAll("<\\?xml.*?\\?>","");
						String xpath= path.replaceAll("/"+concept, "");
						JXPathContext jxpContext= JXPathContext.newContext(Util.parse(xml).getDocumentElement());
						Object oldValue=jxpContext.getValue(xpath);
						if(oldValue!=null && !oldValue.equals("")){
							UpdateReportItem item = new UpdateReportItem(path,oldValue.toString(),"");
							HashMap<String,UpdateReportItem> updatedPath = (HashMap<String,UpdateReportItem>) ctx.getSession().getAttribute("updatedPath");
							if(updatedPath==null)updatedPath = new HashMap<String,UpdateReportItem>();
							updatedPath.put(path,item);
							ctx.getSession().setAttribute("updatedPath",updatedPath);
							pushUpdateReport(ids,concept, "UPDATE");
						}
					}
				else
					return "ERROR - dropItem is NULL";
				ctx.getSession().setAttribute("viewNameItems",null);
				return "OK";
	        }
	        else {
	        	return "OK - But no update report";
	        }
		}
		catch (Exception e) {
	    	return "ERROR -" + e.getLocalizedMessage();
	    }    	
	}
	/**
	 * create an "empty" item from scratch, set every text node to empty
	 * @param viewPK
	 * @throws RemoteException
	 * @throws Exception
	 */
	private void createItem(String concept, int docIndex) throws RemoteException, Exception{
		WebContext ctx = WebContextFactory.get(); 
		Configuration config = Configuration.getInstance(); 
		String xml1 = "<"+concept+"></"+concept+">";
		Document d = Util.parse(xml1);				
		ctx.getSession().setAttribute("itemDocument"+docIndex,d);
		Map<String,XSElementDecl> map = CommonDWR.getConceptMap(config.getModel());
    	XSComplexType xsct = (XSComplexType)(map.get(concept).getType());
    	XSParticle[] xsp = xsct.getContentType().asParticle().getTerm().asModelGroup().getChildren();
    	for (int j = 0; j < xsp.length; j++) {  		
    		//why don't set up children element? FIXME
    		
    		setChilden(xsp[j], "/"+concept, docIndex);
    	}
	}

	
	private void setChilden(XSParticle xsp, String xpathParent, int docIndex) throws Exception{
		//aiming added see 0009563
		if(xsp.getTerm().asModelGroup()!=null){ //is complex type
			XSParticle[] xsps=xsp.getTerm().asModelGroup().getChildren();			
			for (int i = 0; i < xsps.length; i++) {
				setChilden(xsps[i],xpathParent, docIndex);
			}
		}
		if(xsp.getTerm().asElementDecl()==null) return;
		//end
		
		WebContext ctx = WebContextFactory.get();
		Document d = (Document) ctx.getSession().getAttribute("itemDocument"+docIndex);
		Element el = d.createElement(xsp.getTerm().asElementDecl().getName());
		Node node = Util.getNodeList(d,xpathParent).item(0);
		node.appendChild(el);
		if(xsp.getTerm().asElementDecl().getType().isComplexType()==true ){
			XSParticle particle = xsp.getTerm().asElementDecl()
			.getType().asComplexType().getContentType().asParticle();
			if(particle!=null){
				XSParticle[] xsps = particle.getTerm().asModelGroup().getChildren();
				xpathParent = xpathParent+"/"+xsp.getTerm().asElementDecl().getName();
				for (int i = 0; i < xsps.length; i++) {
					setChilden(xsps[i],xpathParent, docIndex);
				}
			}
		}		
	}
	
	
	public String countForeignKey(String xpathForeignKey) throws Exception{
		Configuration config = Configuration.getInstance();
		String conceptName = Util.getConceptFromPath(xpathForeignKey);
		return Util.getPort().count(
			new WSCount(
				new WSDataClusterPK(config.getCluster()),
				conceptName,
				null,
				-1
			)
		).getValue();
	}
	
	/**
	 * lym
	 */
	public String countForeignKey_filter(String xpathForeignKey, String fkFilter) throws Exception{
		return Util.countForeignKey_filter(xpathForeignKey, fkFilter);
	}	
	public String getForeignKeyListWithCount(int start, int limit, String value, String xpathForeignKey, String xpathInfoForeignKey, String fkFilter) 
	   throws RemoteException, Exception 
	{
	   return Util.getForeignKeyList(start, limit, value, xpathForeignKey, xpathInfoForeignKey,fkFilter, true);
	}
	
	private static String pushUpdateReport(String[] ids, String concept, String operationType)throws Exception{
		org.apache.log4j.Logger.getLogger(ItemsBrowserDWR.class).trace("pushUpdateReport() concept "+concept+" operation "+operationType);

		//check updatedPath
		WebContext ctx = WebContextFactory.get();
		HashMap<String,UpdateReportItem> updatedPath = new HashMap<String,UpdateReportItem>();
		updatedPath = (HashMap<String,UpdateReportItem>) ctx.getSession().getAttribute("updatedPath");
		if(!"DELETE".equals(operationType) && updatedPath==null){
			return "ERROR_2";
		}
		
		String xml2 = createUpdateReport(ids, concept, operationType,updatedPath);

		synchronizeUpdateState(ctx);
		return persistentUpdateReport(xml2,true);			
	}


	private static String persistentUpdateReport(String xml2,boolean routeAfterSaving) {
		if(xml2==null) return "OK";
		try {
			WSItemPK itemPK = Util.getPort().putItem(
					new WSPutItem(
							new WSDataClusterPK("UpdateReport"), 
							xml2,
							new WSDataModelPK("UpdateReport"),false));
			org.apache.log4j.Logger.getLogger(ItemsBrowserDWR.class).debug(
					"pushUpdateReport() "+xml2);
			try {
				if(routeAfterSaving)Util.getPort().routeItemV2(new WSRouteItemV2(itemPK));
			} catch (RemoteException e) {
				//e.printStackTrace();
				org.apache.log4j.Logger.getLogger(ItemsBrowserDWR.class).warn("Can not route the item, maybe there is no Routing Rule defined for this item! ");
			}
			return "OK";
		} catch (RemoteException e) {			
			e.printStackTrace();
			return "ERROR";
		} catch (XtentisWebappException e) {
			e.printStackTrace();
			return "ERROR";
		}
	}

	public static void  synchronizeUpdateState() {
        WebContext ctx = WebContextFactory.get();   
        synchronizeUpdateState(ctx);
    }
	
	private static void synchronizeUpdateState(WebContext ctx) {
		ctx.getSession().setAttribute("updatedPath",null);
		ctx.getSession().setAttribute("viewNameItems",null);
	}


	private static String createUpdateReport(String[] ids, String concept,
			String operationType, HashMap<String, UpdateReportItem> updatedPath)
			throws Exception {
		String username="";
		String revisionId="";
		
		String dataModelPK ="";
		String dataClusterPK ="";
		try {
			
			Configuration config = Configuration.getInstance();
	    	dataModelPK = config.getModel()==null?"":config.getModel();
	    	dataClusterPK = config.getCluster()==null?"":config.getCluster();
	    	
			username=Util.getLoginUserName();
	    	String universename=Util.getLoginUniverse();
	    	if(universename!=null&&universename.length()>0)revisionId=Util.getRevisionIdFromUniverse(universename, concept);
	    	
		} catch (Exception e1) {
			e1.printStackTrace();
			throw e1;
		}
		
		String key = "";
		if(ids!=null){
			for (int i = 0; i < ids.length; i++) {
				key+=ids[i];
				if(i!=ids.length-1) key+=".";
			}
		}
		String xml2 = "" +
			"<Update>"+
			"<UserName>"+username+"</UserName>"+
            "<Source>genericUI</Source>"+
            "<TimeInMillis>"+System.currentTimeMillis()+"</TimeInMillis>"+
            "<OperationType>"+StringEscapeUtils.escapeXml(operationType)+"</OperationType>"+
            "<RevisionID>"+revisionId+"</RevisionID>"+
            "<DataCluster>"+dataClusterPK+"</DataCluster>"+
            "<DataModel>"+dataModelPK+"</DataModel>"+
            "<Concept>"+StringEscapeUtils.escapeXml(concept)+"</Concept>"+
            "<Key>"+StringEscapeUtils.escapeXml(key)+"</Key>";
		if("UPDATE".equals(operationType)){
			Collection<UpdateReportItem> list = updatedPath.values();
			boolean isUpdate=false;
			for (Iterator<UpdateReportItem> iter = list.iterator(); iter.hasNext();) {				
				UpdateReportItem item = iter.next();
				String oldValue=item.getOldValue()==null?"":item.getOldValue();
				String newValue=item.getNewValue()==null?"":item.getNewValue();
				if(newValue.equals(oldValue)) continue; 
		            xml2 += 
		            "<Item>"+
		            "   <path>"+StringEscapeUtils.escapeXml(item.getPath())+"</path>"+
		            "   <oldValue>"+StringEscapeUtils.escapeXml(oldValue)+"</oldValue>"+
		            "   <newValue>"+StringEscapeUtils.escapeXml(newValue)+"</newValue>"+
		           "</Item>"; 		
		            isUpdate=true;
			}     
			if(!isUpdate) return null;
		}
        xml2 += "</Update>";
		return xml2;
	}
	
	private void insertAfter(Node newNode, Node node){
		if(node.getNextSibling()!=null)
			node.getParentNode().insertBefore(newNode,node.getNextSibling());
		else
			node.getParentNode().appendChild(newNode);
	}	
	
	public static boolean checkIfTransformerExists(String concept, String language) {
		try{
			WSTransformerPK[] wst = Util.getPort().getTransformerPKs(new WSGetTransformerPKs("*")).getWsTransformerPK();
			for (int i = 0; i < wst.length; i++) {
				if(language!=null) {
					if(wst[i].getPk().equals("Smart_view_"+concept+"_"+language.toUpperCase())){
						return true;
					}
				}else {
					if(wst[i].getPk().equals("Smart_view_"+concept)){
						return true;
					}					
				}
			}
			return false;
		}
		catch(Exception e){
			return false;
		}
	}
	public static boolean checkSmartViewExists(String concept, String language) {
		boolean ret=checkIfTransformerExists(concept, language);
		if(!ret) {
			ret=checkIfTransformerExists(concept,null);
		}
		return ret;
	}
	public boolean checkIfDocumentExists(String[] ids, String concept) throws Exception{
		Configuration config = Configuration.getInstance();
		boolean flag=Util.getPort().existsItem(
				 new WSExistsItem(new WSItemPK(new WSDataClusterPK(config.getCluster()),concept, ids))
				 ).is_true();
		return flag;
	}
	
	public int countItems(String criteria, String dataObjet) throws Exception{
		Configuration config = Configuration.getInstance();
		String[] criterias = criteria.split("[\\s]+OR[\\s]+");
		ArrayList<WSWhereItem> conditions=new ArrayList<WSWhereItem>(); 
		
		for (String cria: criterias)
		{
			ArrayList<WSWhereItem> condition=new ArrayList<WSWhereItem>(); 
			String[] subCriterias = cria.split("[\\s]+AND[\\s]+");
			for (String subCria: subCriterias)
			{
				if (subCria.startsWith("("))
				{
					subCria = subCria.substring(1);
				}
				if (subCria.endsWith(")"))
				{
					subCria = subCria.substring(0, subCria.length() -1);
				}
				
				WSWhereItem whereItem = countItem(subCria, dataObjet);
				condition.add(whereItem);
			}
			if (condition.size() > 0) {
				WSWhereAnd and = new WSWhereAnd(condition
						.toArray(new WSWhereItem[condition.size()]));
				WSWhereItem whand = new WSWhereItem(null,and,null);
				conditions.add(whand);
			}
		}
		WSWhereOr or = new WSWhereOr(conditions.toArray(new WSWhereItem[conditions.size()]));
		WSWhereItem wi = new WSWhereItem(null,null,or);

		//count items 
		int count = Integer.parseInt(Util.getPort().count(new WSCount(
				new WSDataClusterPK(config.getCluster()),
				dataObjet,
				wi,
				0
		)).getValue());
		
		WebContext ctx = WebContextFactory.get();
		ctx.getSession().setAttribute("totalCountItems", count);
		
		return count;
	}
	
	public WSWhereItem countItem(String criteria, String dataObjet) throws Exception{
		WSWhereItem wi;
		String[] filters = criteria.split(" ");
		String filterXpaths, filterOperators ,filterValues ;

		filterXpaths = filters[0];
		filterOperators = filters[1];
		if (filters.length <= 2)
		    filterValues = " ";
		else
			filterValues = filters[2];

		WSWhereCondition wc=new WSWhereCondition(
				filterXpaths,
				getOperator(filterOperators),
				filterValues,
				WSStringPredicate.NONE,
				false
				);
		//System.out.println("iterator :"+i+"field - getErrors- : " + fields[i] + " " + operator[i]);
		//System.out.println("Xpath field - getErrors- : " + giveXpath(fields[i]) + " - values : "+ regexs[i]);
		ArrayList<WSWhereItem> conditions=new ArrayList<WSWhereItem>();
		WSWhereItem item=new WSWhereItem(wc,null,null);
		conditions.add(item);
						
		if(conditions.size()==0) { 
			wi=null;
		} else {
			WSWhereAnd and=new WSWhereAnd(conditions.toArray(new WSWhereItem[conditions.size()]));
			wi=new WSWhereItem(null,and,null);
		}
		
		
		return wi;
		
	}
	
    public boolean prepareSessionForItemDetails(String concept,String language) {
    	
    	try {
    		
    		
			WebContext ctx = WebContextFactory.get();
			Configuration config = Configuration.getInstance();
			String model = config.getModel();
			
			CommonDWR.getFieldsByDataModel(model, concept, language, true);
			
			WSConceptKey key = Util.getPort().getBusinessConceptKey(
					new WSGetBusinessConceptKey(
							new WSDataModelPK(model),
							concept));
			String[] keys = key.getFields();
			for (int i = 0; i < keys.length; i++) {
				if(".".equals(key.getSelector()))
					keys[i] = "/"+concept+"/"+keys[i];					
				else
					keys[i] = key.getSelector()+keys[i];
			}
			ctx.getSession().setAttribute("foreignKeys",key.getFields());
			
			
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
    	return true;

	}
    
	private Map<String, ArrayList<String>> getMetaDataTypes(View view) throws Exception
	{
		HashMap<String, ArrayList<String>> metaDataTypes = new HashMap<String, ArrayList<String>>();
		Configuration config = Configuration.getInstance(true);
		Map<String,XSElementDecl> xsdMap = CommonDWR.getConceptMap(config.getModel());
		
		String concept = view.getViewPK();
		if(concept.contains("Browse_items_"))
			 concept = CommonDWR.getConceptFromBrowseItemView(view.getViewPK());
		
		XSElementDecl el = xsdMap.get(concept);

        for (String viewItem: view.getViewables())
        {
    		ArrayList<String> dataTypesHolder = new ArrayList<String>();
        	String[] pathSlices = viewItem.split("/");
        	XSElementDecl node = parseMetaDataTypes(el, pathSlices[0], dataTypesHolder);
        	if(pathSlices.length > 1)
        	{
            	for (int i = 1; i < pathSlices.length; i++)
            	{
            		node = parseMetaDataTypes(node, pathSlices[i], dataTypesHolder);
            	}
        	}
        	metaDataTypes.put(viewItem, dataTypesHolder);
        }

		return metaDataTypes;
	}
	
	public Map<String, List<String>> getFKvalueInfoFromXSDElem(String concept, String path) {
		Map<String, List<String>> fkHandler = new HashMap<String, List<String>>();
		
		try {
			Configuration config = Configuration.getInstance();
			String dataModelPK = config.getModel();
			Map<String,XSElementDecl> map = CommonDWR.getConceptMap(dataModelPK);
			XSElementDecl decl = map.get(concept);
			String[] pathSlices = path.split("/");
			
			if(pathSlices.length > 1) {
            	for (int i = 1; i < pathSlices.length; i++) {
            	    fkHandler = getForeignKeyInfoForXSDElem1(decl, pathSlices[i]);
            	}
			}
		} 
		catch(Exception e) {
			e.printStackTrace();
			return fkHandler;
		}
		
		return fkHandler;
	}
	
	/**
	 * get foreignKey, foreignKeyInfo, foreignKeyFilter from specify node by path.
	 * @param elemDecl
	 * @param path
	 * @return
	 */
	private Map<String, List<String>> getForeignKeyInfoForXSDElem1(XSElementDecl elemDecl, String path) {
        Map<String, List<String>> foreignKeyContents = new HashMap<String, List<String>>();
        XSType type = elemDecl.getType();
        
        if(type instanceof XSComplexType) {
            XSComplexType cmpxType = (XSComplexType)type;
            XSContentType conType = cmpxType.getContentType();
            XSParticle[] children = conType.asParticle().getTerm().asModelGroup().getChildren();
            
            for(XSParticle child : children) {
                XSTerm term = child.getTerm();
                
                if(term instanceof XSElementDecl && ((XSElementDecl)term).getName().equals(path)) {
                    XSElementDecl childElem = (XSElementDecl)child.getTerm();
                    
                    if(childElem.getAnnotation() instanceof AnnotationImpl) {
                        AnnotationImpl antnImp = (AnnotationImpl) childElem.getAnnotation();
                        ElementNSImpl ensImpl = (ElementNSImpl)antnImp.getAnnotation();
                        NodeList list = ensImpl.getChildNodes();
                        List<String> fkInfoHandler = new ArrayList<String>();
                        List<String> fkHandler = new ArrayList<String>();
                        List<String> fkFilterHandler = new ArrayList<String>();
                        List<String> fkRetrieveHandler = new ArrayList<String>();
                        
                        for(int i = 0; i < list.getLength(); i++) {
                            Node node = list.item(i);
                            
                            if(node instanceof TextImpl) {
                                TextImpl txtImpl = (TextImpl)node;
                                
                                if (txtImpl.getNextSibling() instanceof ElementNSImpl) {
                                    ElementNSImpl ens = (ElementNSImpl)txtImpl.getNextSibling();
                                    
                                    if(ens.getAttributes().getNamedItem("source").getNodeValue().equals("X_ForeignKey")) {
                                        String value = ens.getTextContent();
                                        Pattern ptn = Pattern.compile("(.*?)\\[(.*?)\\]");
                                        Matcher match = ptn.matcher(value);
                                        
                                        if(match.matches()) {
                                            value = match.group(1);
                                        }
                                        
                                        fkHandler.add(value);
                                    }
                                    else if(ens.getAttributes().getNamedItem("source").getNodeValue().equals("X_ForeignKeyInfo")) {
                                        //@temp multiply fkinfo
                                        fkInfoHandler.add(ens.getFirstChild().getNodeValue());
                                    }
                                    else if(ens.getAttributes().getNamedItem("source").getNodeValue().equals("X_ForeignKey_Filter")) {
                                        fkFilterHandler.add(ens.getFirstChild().getNodeValue());
                                    }
                                    else if(ens.getAttributes().getNamedItem("source").getNodeValue().equals("X_Retrieve_FKinfos")) {
                                        fkRetrieveHandler.add(ens.getFirstChild().getNodeValue());
                                    }
                                }
                            }
                        }
                        
                        foreignKeyContents.put("foreignKey", fkHandler);
                        foreignKeyContents.put("foreignKeyInfo", fkInfoHandler);
                        foreignKeyContents.put("foreignKeyFilter", fkFilterHandler);
                        foreignKeyContents.put("foreignKeyRetrieve", fkRetrieveHandler);
                    }
                }
            }
        }
        
        return foreignKeyContents;
    }
	
	private XSElementDecl parseMetaDataTypes(XSElementDecl elem, String pathSlice, ArrayList<String> valuesHolder)
	{
		valuesHolder.clear();
		XSContentType conType;
		if(elem == null)return null;
		XSType type = elem.getType();
		if(elem.getName().equals(pathSlice))
		{
			if(elem.getType() instanceof XSComplexType)
			{
				valuesHolder.add("complex type");
			}
			else
			{
				XSSimpleType simpType = (XSSimpleType)elem.getType();
				valuesHolder.add(simpType.getName());
			}
			return elem;
		}
		if(type instanceof XSComplexType)
		{
			XSComplexType cmpxType = (XSComplexType)type;
			conType = cmpxType.getContentType();
			XSParticle[] children = conType.asParticle().getTerm().asModelGroup().getChildren();
			for (XSParticle child : children)
			{
				if (child.getTerm() instanceof XSElementDecl)
				{
					XSElementDecl childElem = (XSElementDecl)child.getTerm();
					if(childElem.getName().equals(pathSlice))
					{
						ArrayList<String> fkContents = getForeignKeyInfoForXSDElem(childElem);
						if(fkContents.size() > 0)
						{

							valuesHolder.add("foreign key");	
							valuesHolder.addAll(fkContents);
							return childElem;
						}
						
						if(childElem.getType() instanceof XSSimpleType)
						{
							XSSimpleType simpType = (XSSimpleType)childElem.getType();
							Collection<FacetImpl> facets = (Collection<FacetImpl>)simpType.asRestriction().getDeclaredFacets();
							for (XSFacet facet : facets)
							{
								if(facet.getName().equals("enumeration"))
								{
									valuesHolder.add("enumeration");
									break;
								}
							}
							if(!valuesHolder.contains("enumeration"))
							{

								String basicName = simpType.getBaseType().getName();
								String simpTypeName = simpType.getName();
								if(simpType.getTargetNamespace().equals("http://www.w3.org/2001/XMLSchema"))
								{
									simpTypeName = "xsd:" + simpTypeName;
								}
								else
									simpTypeName = "xsd:" + basicName;
								valuesHolder.add(simpTypeName);
							}
							else if(simpType.asRestriction() != null && valuesHolder.contains("enumeration"))
							{
								Iterator<XSFacet> facetIter = simpType.asRestriction().iterateDeclaredFacets();
								while(facetIter.hasNext())
								{
									XSFacet facet = facetIter.next();
									valuesHolder.add(facet.getValue().value);
								}	
							}
						}
						else
						{							
							valuesHolder.add("complex type");
						}
						return childElem;
					}
				}
			}
		}
		else 
		{
			XSSimpleType simpType = (XSSimpleType)type;
		}
		
		return null;
		
	}
	
	private ArrayList<String> getForeignKeyInfoForXSDElem(XSElementDecl elemDecl)
	{
		ArrayList<String> foreignKeyContents = new ArrayList<String>();
		if(elemDecl.getAnnotation() instanceof AnnotationImpl)
		{
			AnnotationImpl antnImp = (AnnotationImpl)elemDecl.getAnnotation();

			ElementNSImpl ensImpl = (ElementNSImpl)antnImp.getAnnotation();
			NodeList list = ensImpl.getChildNodes();
			for (int i = 0; i < list.getLength(); i++)
			{
				Node node = list.item(i);
				if(node instanceof TextImpl)
				{
					TextImpl txtImpl = (TextImpl)node;
					if (txtImpl.getNextSibling() instanceof ElementNSImpl)
					{
						ElementNSImpl ens = (ElementNSImpl)txtImpl.getNextSibling();
						if(ens.getAttributes().getNamedItem("source").getNodeValue().equals("X_ForeignKey"))
						{
							String value = ens.getTextContent();
							Pattern ptn = Pattern.compile("(.*?)\\[(.*?)\\]");
							Matcher match = ptn.matcher(value);
							if (match.matches()) {
								value = match.group(1);
							}
							foreignKeyContents.add(0, value);
						}
						else if(ens.getAttributes().getNamedItem("source").getNodeValue().equals("X_ForeignKeyInfo"))
						{
							String fkInfo = null;
							if(foreignKeyContents.size() > 1)
							{
								fkInfo = foreignKeyContents.get(1);
							}
							if(fkInfo == null)
							{
								fkInfo = ens.getFirstChild().getNodeValue();
							}
							else
							{
								fkInfo += "," + ens.getFirstChild().getNodeValue();
							}
							foreignKeyContents.add(0, fkInfo);
						}
					}
				}
			}			
		}
		if(foreignKeyContents.size() == 1)
		{
			foreignKeyContents.add(1, "");
		}
		return foreignKeyContents;
	}
    
	private WSWhereOperator getOperator(String option){
		WSWhereOperator res = null;
		if (option.equalsIgnoreCase("CONTAINS"))
			res = WSWhereOperator.CONTAINS;
		else if (option.equalsIgnoreCase("EQUALS"))
			res = WSWhereOperator.EQUALS;
		else if (option.equalsIgnoreCase("GREATER_THAN"))
			res = WSWhereOperator.GREATER_THAN;
		else if (option.equalsIgnoreCase("GREATER_THAN_OR_EQUAL"))
			res = WSWhereOperator.GREATER_THAN_OR_EQUAL;
		else if (option.equalsIgnoreCase("JOIN"))
			res = WSWhereOperator.JOIN;
		else if (option.equalsIgnoreCase("LOWER_THAN"))
			res = WSWhereOperator.LOWER_THAN;
		else if (option.equalsIgnoreCase("LOWER_THAN_OR_EQUAL"))
			res = WSWhereOperator.LOWER_THAN_OR_EQUAL;
		else if (option.equalsIgnoreCase("NOT_EQUALS"))
			res = WSWhereOperator.NOT_EQUALS;
		else if (option.equalsIgnoreCase("STARTSWITH"))
			res = WSWhereOperator.STARTSWITH;
		else if (option.equalsIgnoreCase("STRICTCONTAINS"))
			res = WSWhereOperator.STRICTCONTAINS;
		return res;											
	}
	public static void main(String[] args) {
		//new ItemsBrowserDWR().deleteItem("Custom", new String[]{"28"});\
		String patternXpath = "/xpath1/xpath2\\[1\\]/xpaht3";
		String xpath = "/xpath1/xpath2[1]/xpaht3[3]";
		Pattern p1 = Pattern.compile(patternXpath+"\\[(\\d+)\\](.*?)");
		Matcher m = p1.matcher(xpath);
		if(m.matches()){
			for(int i= 0 ;i<m.groupCount();i++){
				System.out.println(m.group(i));
			}
		}
		
		
	}
	private org.jboss.dom4j.Document parsXMLString(String xmlString) {
        SAXReader saxReader = new SAXReader();   
        org.jboss.dom4j.Document document = null;   
        try  
        {   
            document = saxReader.read(new StringReader(xmlString));   
        } catch (DocumentException e)   
        {   
            e.printStackTrace();   
            return null;   
        }
		return document;
		
	}
	
	public ListRange getRunnableProcessList(int start, int limit, String sort,String dir, String regex) throws Exception {
		ListRange listRange = new ListRange();
		try {
			
			if(regex==null||regex.length()==0)return listRange;
			String[] inputParams=regex.split("&");
			String businessConcept=inputParams[0];
			String language=inputParams[1];
			
			//get Runnable process
			List<ComboItemBean> comboItem = new ArrayList<ComboItemBean>();
			
			WSTransformerPK[] wst = Util.getPort().getTransformerPKs(new WSGetTransformerPKs("*")).getWsTransformerPK();
			for (int i = 0; i < wst.length; i++) {
				if (wst[i].getPk().startsWith("Runnable_" + businessConcept)) {
					/*
					 * String pk=wst[i].getPk(); String text=pk;
					 * if(pk.lastIndexOf("#")==-1) {
					 * if(language.equalsIgnoreCase
					 * ("fr"))text="Action par défaut"; else
					 * text="Default Action"; }else {
					 * text=pk.substring(pk.lastIndexOf("#")+1); }
					 */

					// edit by ymli;fix the bug:0012025
					// Use the Process description instead of the '#' suffix in the run process drop-down list.
					// and if the description is null, use the default value.
					WSTransformer trans = Util.getPort().getTransformer(new WSGetTransformer(wst[i]));
					String description = trans.getDescription();
					Pattern p = Pattern.compile(".*\\["+ language.toUpperCase() + ":(.*?)\\].*",Pattern.DOTALL);
					String name = p.matcher(description).replaceAll("$1");
					if (name.equals(""))
						if (language.equalsIgnoreCase("fr"))
							name = "Action par d��faut";
						else if (language.equalsIgnoreCase("en"))
							name = "Default Action";
						else 
							name = description;
					comboItem.add(new ComboItemBean(wst[i].getPk(), name));
				}
			}
			
			listRange.setData(comboItem.toArray());
			listRange.setTotalSize(comboItem.size());
			
		} catch (Exception e) {
			String err = "Unable to get Runnable Process List! ";
			org.apache.log4j.Logger.getLogger(ItemsBrowserDWR.class).error(err,e);
			throw new Exception(e.getLocalizedMessage());
		}
		return listRange;
	}
	
	public boolean processItem(String concept, String[] ids,int docIndex, String transformerPK) throws Exception {
		try {
			if(ids.length == 0)
			{
				WebContext ctx = WebContextFactory.get();
				ids = (String[])ctx.getSession().getAttribute("treeIdxToIDS" + docIndex);
			}
			String itemAlias = concept + "." + Util.joinStrings(ids, ".");
			//create updateReport
			org.apache.log4j.Logger.getLogger(ItemsBrowserDWR.class).info(
					"Creating update-report for " + itemAlias + "'s action. ");
			String updateReport = createUpdateReport(ids, concept, "ACTION",
					null);
			//System.out.println(updateReport);
			WSTransformerContext wsTransformerContext = new WSTransformerContext(
					new WSTransformerV2PK(transformerPK), null, null);
			WSTypedContent wsTypedContent = new WSTypedContent(null,
					new WSByteArray(updateReport.getBytes("UTF-8")),
					"text/xml; charset=utf-8");
			WSExecuteTransformerV2 wsExecuteTransformerV2 = new WSExecuteTransformerV2(
					wsTransformerContext, wsTypedContent);
			//check runnable transformer 
			//we can leverage the exception mechanism also
			boolean isRunnableTransformerExist = false;
			WSTransformerPK[] wst = Util.getPort().getTransformerPKs(
					new WSGetTransformerPKs("*")).getWsTransformerPK();
			for (int i = 0; i < wst.length; i++) {
				if (wst[i].getPk().equals(transformerPK)) {
					isRunnableTransformerExist = true;
					break;
				}
			}
			//execute

			WSTransformer wsTransformer=
			Util.getPort().getTransformer(new WSGetTransformer(new WSTransformerPK(transformerPK)));
			if (wsTransformer.getPluginSpecs()==null||wsTransformer.getPluginSpecs().length==0)throw new Exception("The Plugin Specs of this process is undefined! ");
				
			if (isRunnableTransformerExist) {
				org.apache.log4j.Logger.getLogger(ItemsBrowserDWR.class).info(
						"Executing transformer for " + itemAlias
								+ "'s action. ");
				WSTransformerContextPipelinePipelineItem[] entries = Util
						.getPort().executeTransformerV2(wsExecuteTransformerV2)
						.getPipeline().getPipelineItem();
			} else {
				//return false;
				throw new Exception("The target process is not existed! ");
			}
			//store
			org.apache.log4j.Logger.getLogger(ItemsBrowserDWR.class).info(
					"Saving update-report for " + itemAlias + "'s action. ");
			
			if (!persistentUpdateReport(updateReport, true).equals("OK")) {
				//return false;
				throw new Exception("Store Update-Report failed! ");
			}
				
		} catch (Exception e) {
			String err = "Unable to launch Runnable Process! ";
			org.apache.log4j.Logger.getLogger(ItemsBrowserDWR.class).error(err,e);
			String output=e.getLocalizedMessage();
			if(e.getLocalizedMessage()==null||e.getLocalizedMessage().equals(""))output=err;
			throw new Exception(output);
		}
		return true;

	}
	/**
	 * @author ymli
	 * @param concept
	 * @return
	 * @throws RemoteException
	 * @throws XtentisWebappException
	 * @throws Exception
	 */
	public boolean isReadOnlyinItem(String concept, String[] ids) throws RemoteException, XtentisWebappException, Exception{
		
		Configuration config = Configuration.getInstance();
		String dataClusterPK = config.getCluster();	
		boolean ret=false;
		if(ids!=null) {		
			ret=LocalUser.getLocalUser().userItemCanWrite(new ItemPOJOPK(new DataClusterPOJOPK(dataClusterPK), concept, ids),dataClusterPK, concept);
			if(ret) return false;
			ret=LocalUser.getLocalUser().userItemCanRead(new ItemPOJOPK(new DataClusterPOJOPK(dataClusterPK), concept, ids));			
		}
		return ret;
	}
	/**
	 * @author ymli; fix the bug:0013463
	 * @param lang
	 * @param format
	 * @param value
	 * @return
	 * @throws ParseException 
	 * 
	 */
	public String printFormat(String lang,String format,String value,String typeName) throws ParseException{
		if(typeName==null || typeName.equals("null")|| format.equals("null") ) return value;
		Object object = Util.getTypeValue(lang,typeName, value);
		if(object instanceof Calendar || object instanceof Time ||object ==null)
		//if(Util.getTypeValue(typeName, value)==null)
			return value;
//		return com.amalto.core.util.Util.printWithFormat(new Locale(lang), format,object);
		return object.toString();
	}
	
	/***
	 * @author ymli
	 * get the format value of date
	 * @param lang
	 * @param format
	 * @param value
	 * @param typeName
	 * @return
	 * @throws ParseException
	 */
	public String printFormatDate(String lang,String format,String value,String typeName) throws ParseException{
		
		Object object = Date.parseDate(value.trim()).toCalendar();
		if(format.equals("null") ||object ==null)
			return value;
		String valueReturn = com.amalto.core.util.Util.printWithFormat(new Locale(lang), format,object).toString();
		return valueReturn;
	}
	
/**
 * @author ymli; fix the bug:0013463. validate the value from server
 * @param nodeId
 * @param value
 * @return
 */
	public String validateNode(int nodeId,String value){
		String errorMessage= null;
		WebContext ctx = WebContextFactory.get();
		HashMap<String,TreeNode> xpathToTreeNode = 
			(HashMap<String,TreeNode>)ctx.getSession().getAttribute("xpathToTreeNode");
		HashMap<Integer,String> idToXpath = 
			(HashMap<Integer,String>) ctx.getSession().getAttribute("idToXpath");
		String xpath = idToXpath.get(nodeId);
		TreeNode node = null;
		ArrayList<Restriction> restrictions = null;
		if(xpath!=null)
			node = xpathToTreeNode.get(xpath);
		if(node!=null){
			restrictions = node.getRestrictions();
		}
		if(restrictions==null) return "null";
		boolean isValidation = true;//if true, return null,else return errorMessage
		for(Restriction re : restrictions){
			if(node.getFacetErrorMsg()!=null)
				errorMessage = (String) node.getFacetErrorMsg().get("en");
			if(value.length() == 0 && node.isKey()){
				if(errorMessage == null){
					errorMessage = "The value does not comply with the facet defined in the model: "
                        + "Key should not be empty";
                isValidation = false;
                break;
				}
			}
			
			//boolean ancestor = true;//@TODO... check ancestor
			//boolean ancestor = checkAncestorMinOCcurs(node);
			if(value.length() == 0 && (node.getMinOccurs() >= 1 || checkAncestorMinOCcurs(node))){
				if(errorMessage == null){
					if(node.getMinOccurs() >=1)
						 errorMessage = "The value does not comply with the facet defined in the model: "
								+ "minOccurs"
								+": "
								+node.getMinOccurs();
					else
						errorMessage = "This item is mandatory!";
				}
				isValidation = false;
				break;
			}
			
			
			if(re.getName()!="whiteSpace")
				if (errorMessage == null)
				   errorMessage = "The value does not comply with the facet defined in the model: "
							+ re.getName()
							+ ":"
							+ re.getValue();
				if(node.getMinOccurs()>=1 ||(node.getMinOccurs()==0 && value.trim().length()!=0)){
					if(re.getName()=="minLength" && value.length() < Integer.parseInt(re.getValue())){
						isValidation = false;
						break;
					}
					if(re.getName()=="maxLength" && value.length() > Integer.parseInt(re.getValue())){
						isValidation = false;
						break;
					}
					if(re.getName() == "length" && value.length() != Integer.parseInt(re.getValue())){
						isValidation = false;
						break;
					}
					if(re.getName() == "minExclusive")
						if(!isNumeric(value)){
							errorMessage =node.getName()+ " " + "is not a valid value for double";
							isValidation =  false;
							break;
						}
						else if(Float.parseFloat(value) <= Float.parseFloat(re.getValue())){
							isValidation = false;
							break;
						}
					
					
					if(re.getName() == "minInclusive"){
						if(!isNumeric(value)){
							errorMessage = node.getName() + " " + "is not a valid value for double";
							isValidation = false;
							break;
						}
						else if(Float.parseFloat(value) < Float.parseFloat(re.getValue())){
							isValidation = false;
							break;
						}
							
					}
				
					if(re.getName() == "maxInclusive")
						if(!isNumeric(value)){
							errorMessage =node.getName()+ " " + "is not a valid value for double";
							isValidation =  false;
							break;
						}
						else if(Float.parseFloat(value) > Float.parseFloat(re.getValue())){
							isValidation = false;
							break;
						}
				}		
				
		}
		
		return isValidation?"null":errorMessage;
		//return null;
	}
	
	
	public static boolean isNumeric(String str){  
	    Pattern pattern = Pattern.compile("[0-9]+\\.?[0-9]*");  
	    return pattern.matcher(str).matches();     
	}
	/**
	 * @author ymli;
	 * check if this node is mandatory
	 * @param node
	 * @return
	 */
	private boolean checkAncestorMinOCcurs(TreeNode node){
		if(node.getParent() == null && node.getMinOccurs() >= 1)
			return true;
		else if(node.getParent() == null  && node.getMinOccurs() == 0)
			return false;
		else
			return checkAncestorMinOCcurs(node.getParent());
	}

}
