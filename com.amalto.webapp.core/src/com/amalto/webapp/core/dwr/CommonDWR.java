package com.amalto.webapp.core.dwr;

import java.io.StringReader;
import java.io.StringWriter;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.TreeSet;

import javax.security.jacc.PolicyContextException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.directwebremoting.WebContext;
import org.directwebremoting.WebContextFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.amalto.webapp.core.util.Menu;
import com.amalto.webapp.core.util.Util;
import com.amalto.webapp.util.webservices.WSDataClusterPK;
import com.amalto.webapp.util.webservices.WSDataModelPK;
import com.amalto.webapp.util.webservices.WSGetDataModel;
import com.amalto.webapp.util.webservices.WSRegexDataClusterPKs;
import com.amalto.webapp.util.webservices.WSRegexDataModelPKs;
import com.amalto.webapp.core.bean.Configuration;
import com.sun.org.apache.xpath.internal.XPathAPI;
import com.sun.org.apache.xpath.internal.objects.XObject;
import com.sun.xml.xsom.XSAnnotation;
import com.sun.xml.xsom.XSComplexType;
import com.sun.xml.xsom.XSElementDecl;
import com.sun.xml.xsom.XSParticle;
import com.sun.xml.xsom.XSSchema;
import com.sun.xml.xsom.XSSchemaSet;
import com.sun.xml.xsom.parser.XSOMParser;
import com.sun.xml.xsom.util.DomAnnotationParserFactory;

/**
 * 
 * @author asaintguilhem
 *
 */

public class CommonDWR {

	
	//private static HashMap<String,String> xpathToLabel;
	
	public void setLanguage(String language){
		WebContext ctx = WebContextFactory.get();
		ctx.getSession().setAttribute("language",language);
	}

	//TODO Should not be used anymore
	/*
	public String getMenus(String language) throws RemoteException, Exception{
		Menu menu = Menu.getRootMenu();
		//ArrayList<String> menus = new ArrayList<String>();
		//JSONObject rows = new JSONObject();		
		String json = "{";
		int i =0;
		for (Iterator iter = menu.getSubMenus().keySet().iterator(); iter.hasNext(); ) {
			String key = (String) iter.next();
			Menu subMenu= menu.getSubMenus().get(key);
			/*menus.add(subMenu.getLabels().get(language));
			JSONObject fields = new JSONObject();
			fields.put("run",subMenu.getApplication());
			fields.put("name",subMenu.getLabels().get(language));
			fields.put("id",i);			
			rows.put(""+i,fields);*/
/*			json += "'"+i+"':{id:'"+i+"', name: '"+subMenu.getLabels().get(language)+"', desc: '', run: "+subMenu.getApplication()+"}";
			if(iter.hasNext()){
				json += ",";
			}
			i++;
		}
		json +="}";
		//return menus;	
		//'items':{id:'items', name: BROWSE_ITEMS[language], desc: '', run: browseItems}, 
		//return rows.toString();
		return json;
	}*/
	
	public static String[] getClusters(){
		try {
			WSDataClusterPK[] wsDataClustersPK = Util.getPort().getDataClusterPKs(
					new WSRegexDataClusterPKs("*")
					).getWsDataClusterPKs();
			ArrayList<String> list = new ArrayList<String>();			
			for (int i = 0; i < wsDataClustersPK.length; i++) {
				if(!"MDMCONF".equals(wsDataClustersPK[i].getPk())
						&& !"PROVISIONING".equals(wsDataClustersPK[i].getPk())
						&& !"amaltoOBJECTSjcaadapters".equals(wsDataClustersPK[i].getPk())
						&& !"UpdateReport".equals(wsDataClustersPK[i].getPk())
						&& !"b2box CROSSREFERENCING".equals(wsDataClustersPK[i].getPk())
						)
					list.add(wsDataClustersPK[i].getPk());
			}
			return  (String[])list.toArray(new String[list.size()]);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static String[] getModels(){
		try {
			WSDataModelPK[] wsDataModelsPK = Util.getPort().getDataModelPKs(
					new WSRegexDataModelPKs("*")
					).getWsDataModelPKs();
			ArrayList<String> list = new ArrayList<String>();
			for (int i = 0; i < wsDataModelsPK.length; i++) {
				if(!"REPORTING".equals(wsDataModelsPK[i].getPk())
						&& !"PROVISIONING".equals(wsDataModelsPK[i].getPk())
						&& !"CONF".equals(wsDataModelsPK[i].getPk())
						&& !"UpdateReport".equals(wsDataModelsPK[i].getPk())
						&& !"b2box CROSSREFERENCING".equals(wsDataModelsPK[i].getPk())
						)
					list.add(wsDataModelsPK[i].getPk());
			}
			return  (String[])list.toArray(new String[list.size()]);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public String getUsername() throws Exception{
		try {
			String xml = Util.getAjaxSubject().getXml();
			Document d = Util.parse(xml);
			String givenname = Util.getFirstTextNode(d,"//givenname");
			String familyname = Util.getFirstTextNode(d,"//familyname");
			String name = "";
			if(familyname!=null && givenname!=null) name = givenname+" "+familyname;
			else name= Util.getAjaxSubject().getUsername();
			return name;
		} catch (PolicyContextException e) {
			e.printStackTrace();
			return "";
		}	
	}
	
	public boolean isTechnicalAdmin(){
		return true;
	}
	
	public String setClusterAndModel(String cluster, String model){
		try {
			Configuration.initialize(cluster,model);
			return "Done";
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(cluster+" "+model);
		return "ERROR";
	}
	
	public String getCluster(){
		try {
			Configuration config = Configuration.getInstance();
			return config.getCluster();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}		
	}
	
	public String getModel(){
		try {
			Configuration config = Configuration.getInstance();
			return config.getModel();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}	
	}
/*	
	public static HashMap<String, String> getFieldsByView(String language, boolean includeComplex) 
		throws RemoteException,	Exception {
		Configuration config = Configuration.getInstance();
		HashMap<String, String> xpathToLabel = new HashMap<String,String>();
		String[] concepts = Util.getPort().getBusinessConcepts(new WSGetBusinessConcepts(new WSDataModelPK(config.getModel()))).getStrings();
		for (int i = 0; i < concepts.length; i++) {
			xpathToLabel.putAll(getFieldsByDataModel(config.getModel(), concepts[i], language,includeComplex));
		}		
		return xpathToLabel;
	}	
	
	public static HashMap<String,String> getFieldsByBrowseItemsView(String viewPK, String language, boolean includeComplex) 
			throws RemoteException, Exception{
        String concept = getConceptFromBrowseItemView(viewPK);
        Configuration config = Configuration.getInstance();       
        return getFieldsByDataModel(config.getModel(),concept,language, includeComplex);
	}
*/	
	public static String getConceptLabel(String dataModelPK, String concept, String language)
		throws RemoteException, Exception{
		String x_Label = "X_Label_"+language.toUpperCase();
		Map<String,XSElementDecl> map = getConceptMap(dataModelPK);	
    	return getLabel(map.get(concept),x_Label);
	}
	
	public static HashMap<String,String> getFieldsByDataModel(
			String dataModelPK, String concept, String language, boolean includeComplex) 
			throws RemoteException, Exception{
		
		WebContext ctx = WebContextFactory.get();
		String x_Label = "X_Label_"+language.toUpperCase();    	
		Map<String,XSElementDecl> map = getConceptMap(dataModelPK);
    	XSComplexType xsct = (XSComplexType)(map.get(concept).getType());
    	HashMap<String,String> xpathToLabel = new HashMap<String,String>();  
    	xpathToLabel.put(concept,getLabel(map.get(concept),x_Label));
    	//xpathToLabel.put(concept,CommonDWR.getConceptLabel(dataModelPK,concept,language));
    	XSParticle[] xsp = xsct.getContentType().asParticle().getTerm().asModelGroup().getChildren();
    	for (int j = 0; j < xsp.length; j++) {  
   			getChildren(xsp[j],""+concept,x_Label,includeComplex,xpathToLabel);
    	}
    	ctx.getSession().setAttribute("xpathToLabel",xpathToLabel);
    	return xpathToLabel; 
	}
	
	private static void getChildren(XSParticle xsp, String xpathParent, String x_Label, boolean includeComplex,HashMap<String,String> xpathToLabel){
		if(xsp.getTerm().asElementDecl().getType().isComplexType()==false || includeComplex==true){
			xpathToLabel.put(
					xpathParent+"/"+xsp.getTerm().asElementDecl().getName(),
					getLabel(xsp.getTerm().asElementDecl(),x_Label)
					);	
		}		
		if(xsp.getTerm().asElementDecl().getType().isComplexType()==true ){
			XSParticle particle = xsp.getTerm().asElementDecl()
			.getType().asComplexType().getContentType().asParticle();
			XSParticle[] xsps = particle.getTerm().asModelGroup().getChildren();
			for (int i = 0; i < xsps.length; i++) {
				getChildren(xsps[i],xpathParent+"/"+xsp.getTerm().asElementDecl().getName(),x_Label, includeComplex, xpathToLabel);
			}
		}		 
	}
	
	private static String getLabel(XSElementDecl xsed, String x_Label){
		String label = xsed.getName();
		try{
			XSAnnotation xsa = xsed.getAnnotation();
			Element el = (Element)xsa.getAnnotation();
			NodeList list = el.getChildNodes();			
			for (int k = 0; k < list.getLength(); k++) {
				if("appinfo".equals(list.item(k).getLocalName())){
					String appinfoSource = list.item(k).getAttributes().getNamedItem("source").getNodeValue();					
					if(x_Label.equals(appinfoSource)){							
						label = list.item(k).getFirstChild().getNodeValue();		
						//System.out.println("xlabel found :"+label);		
						break;
					}
				}
			}	
		}
		catch(Exception e){
			//System.out.println("no annotation");
		}
		return label;
	}

	public static Map<String,XSElementDecl> getConceptMap(String dataModelPK) 
		throws RemoteException, Exception{
		String xsd = Util.getPort().getDataModel(
        		new WSGetDataModel(new WSDataModelPK(dataModelPK))).getXsdSchema();
		XSOMParser reader = new XSOMParser();
		reader.setAnnotationParser(new DomAnnotationParserFactory());
        reader.parse(new StringReader(xsd));
        XSSchemaSet xss = reader.getResult();
    	Collection xssList = xss.getSchemas();
    	Map<String,XSElementDecl> map = null ;
    	for (Iterator iter = xssList.iterator(); iter.hasNext();) {
    		XSSchema schema = (XSSchema) iter.next();
    		map = schema.getElementDecls();
		}   
    	return map;
	}
	
	public static String getConceptFromBrowseItemView(String viewPK){
        String concept = viewPK.replaceAll("Browse_items_","");
        concept = concept.replaceAll("#.*","");
        return concept;
	}
	
	public static String getXMLStringFromDocument(Document d) throws TransformerException{
		StringWriter writer = new StringWriter();
		TransformerFactory.newInstance().newTransformer()
		.transform(new DOMSource(d), new StreamResult(writer));
		return writer.toString();
	}
	
	public static NodeList getNodeList(Node contextNode, String xPath) throws TransformerException{
	    XObject xo = XPathAPI.eval(
	    		contextNode,
				xPath
		    );
	    if (xo.getType() != XObject.CLASS_NODESET) return null;
	    return xo.nodelist();
	}
	
	public static LinkedHashMap<String,String> getMapSortedByValue(Map<String,String> map){
		TreeSet<Map.Entry> set = new TreeSet<Map.Entry>(new Comparator() {
			public int compare(Object obj, Object obj1) {
				return ((Comparable) ((Map.Entry) obj).getValue())
						.compareTo(((Map.Entry) obj1).getValue());
			}
		});
		set.addAll(map.entrySet());
		LinkedHashMap<String, String> sortedMap = new LinkedHashMap<String, String>();
		for (Iterator i = set.iterator(); i.hasNext();) {
			Map.Entry entry = (Map.Entry) i.next();
			sortedMap.put((String) entry.getKey(), (String) entry.getValue());
		}
		
		return sortedMap;		
	}
}
