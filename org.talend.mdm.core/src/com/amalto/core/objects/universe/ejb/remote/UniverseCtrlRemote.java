 /*
 * Generated by XDoclet - Do not edit!
 * this class was prodiuced by xdoclet automagically...
 */
package com.amalto.core.objects.universe.ejb.remote;

import java.util.*;

/**
 * This class is remote adapter to UniverseCtrl. It provides convenient way to access
 * facade session bean. Inverit from this class to provide reasonable caching and event handling capabilities.
 *
 * Remote facade for UniverseCtrl.
 * @xdoclet-generated at 30-09-09
 * @copyright The XDoclet Team
 * @author XDoclet
 * @version ${version}
 */

public class UniverseCtrlRemote extends Observable
{
    static UniverseCtrlRemote _instance = null;
    public static UniverseCtrlRemote getInstance() {
        if(_instance == null) {
	   _instance = new UniverseCtrlRemote();
	}
	return _instance;
    }

  /**
   * cached remote session interface
   */
  com.amalto.core.objects.universe.ejb.remote.UniverseCtrl _session = null;
  /**
   * return session bean remote interface
   */
   protected com.amalto.core.objects.universe.ejb.remote.UniverseCtrl getSession() {
      try {
   	if(_session == null) {
	   _session = com.amalto.core.objects.universe.ejb.local.UniverseCtrlUtil.getHome().create();
	}
	return _session;
      } catch(Exception ex) {
        // just catch it here and return null.
        // somebody can provide better solution
	ex.printStackTrace();
	return null;
      }
   }

   public com.amalto.core.objects.universe.ejb.UniversePOJOPK putUniverse ( com.amalto.core.objects.universe.ejb.UniversePOJO universe )
	  throws com.amalto.core.util.XtentisException, java.rmi.RemoteException
   {
        com.amalto.core.objects.universe.ejb.UniversePOJOPK retval;
       retval =  getSession().putUniverse( universe );

      return retval;

   }

   public com.amalto.core.webservice.WSUniversePKArray getUniverseByRevision ( java.lang.String name,java.lang.String revision,java.lang.String type )
	  throws com.amalto.core.util.XtentisException, java.rmi.RemoteException
   {
        com.amalto.core.webservice.WSUniversePKArray retval;
       retval =  getSession().getUniverseByRevision( name,revision,type );

      return retval;

   }

   public com.amalto.core.objects.universe.ejb.UniversePOJO getUniverse ( com.amalto.core.objects.universe.ejb.UniversePOJOPK pk )
	  throws com.amalto.core.util.XtentisException, java.rmi.RemoteException
   {
        com.amalto.core.objects.universe.ejb.UniversePOJO retval;
       retval =  getSession().getUniverse( pk );

      return retval;

   }

   public com.amalto.core.objects.universe.ejb.UniversePOJO existsUniverse ( com.amalto.core.objects.universe.ejb.UniversePOJOPK pk )
	  throws com.amalto.core.util.XtentisException, java.rmi.RemoteException
   {
        com.amalto.core.objects.universe.ejb.UniversePOJO retval;
       retval =  getSession().existsUniverse( pk );

      return retval;

   }

   public com.amalto.core.objects.universe.ejb.UniversePOJOPK removeUniverse ( com.amalto.core.objects.universe.ejb.UniversePOJOPK pk )
	  throws com.amalto.core.util.XtentisException, java.rmi.RemoteException
   {
        com.amalto.core.objects.universe.ejb.UniversePOJOPK retval;
       retval =  getSession().removeUniverse( pk );

      return retval;

   }

   public java.util.Collection getAllCreatedRevisions ( com.amalto.core.objects.universe.ejb.UniversePOJOPK pk )
	  throws java.rmi.RemoteException
   {
        java.util.Collection retval;
       retval =  getSession().getAllCreatedRevisions( pk );

      return retval;

   }

   public java.util.Collection getAllQuotedRevisions ( com.amalto.core.objects.universe.ejb.UniversePOJOPK pk )
	  throws java.rmi.RemoteException
   {
        java.util.Collection retval;
       retval =  getSession().getAllQuotedRevisions( pk );

      return retval;

   }

   public com.amalto.core.objects.universe.ejb.UniversePOJOPK getUniverseCreator ( com.amalto.core.objects.universe.ejb.RevisionPOJOPK pk )
	  throws java.rmi.RemoteException
   {
        com.amalto.core.objects.universe.ejb.UniversePOJOPK retval;
       retval =  getSession().getUniverseCreator( pk );

      return retval;

   }

   public java.util.Collection getUniverseQuoter ( com.amalto.core.objects.universe.ejb.RevisionPOJOPK pk )
	  throws java.rmi.RemoteException
   {
        java.util.Collection retval;
       retval =  getSession().getUniverseQuoter( pk );

      return retval;

   }

   public java.util.Collection getUniversePKs ( java.lang.String regex )
	  throws com.amalto.core.util.XtentisException, java.rmi.RemoteException
   {
        java.util.Collection retval;
       retval =  getSession().getUniversePKs( regex );

      return retval;

   }

  /**
   * override this method to provide feedback to interested objects
   * in case collections were changed.
   */
  public void invalidate() {

  	setChanged();
	notifyObservers();
  }
}
