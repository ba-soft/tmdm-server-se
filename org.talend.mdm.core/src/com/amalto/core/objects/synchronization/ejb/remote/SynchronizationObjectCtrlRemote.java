 /*
 * Generated by XDoclet - Do not edit!
 * this class was prodiuced by xdoclet automagically...
 */
package com.amalto.core.objects.synchronization.ejb.remote;

import java.util.*;

/**
 * This class is remote adapter to SynchronizationObjectCtrl. It provides convenient way to access
 * facade session bean. Inverit from this class to provide reasonable caching and event handling capabilities.
 *
 * Remote facade for SynchronizationObjectCtrl.
 * @xdoclet-generated at 30-09-09
 * @copyright The XDoclet Team
 * @author XDoclet
 * @version ${version}
 */

public class SynchronizationObjectCtrlRemote extends Observable
{
    static SynchronizationObjectCtrlRemote _instance = null;
    public static SynchronizationObjectCtrlRemote getInstance() {
        if(_instance == null) {
	   _instance = new SynchronizationObjectCtrlRemote();
	}
	return _instance;
    }

  /**
   * cached remote session interface
   */
  com.amalto.core.objects.synchronization.ejb.remote.SynchronizationObjectCtrl _session = null;
  /**
   * return session bean remote interface
   */
   protected com.amalto.core.objects.synchronization.ejb.remote.SynchronizationObjectCtrl getSession() {
      try {
   	if(_session == null) {
	   _session = com.amalto.core.objects.synchronization.ejb.local.SynchronizationObjectCtrlUtil.getHome().create();
	}
	return _session;
      } catch(Exception ex) {
        // just catch it here and return null.
        // somebody can provide better solution
	ex.printStackTrace();
	return null;
      }
   }

   public com.amalto.core.objects.synchronization.ejb.SynchronizationObjectPOJOPK putSynchronizationObject ( com.amalto.core.objects.synchronization.ejb.SynchronizationObjectPOJO synchronizationObject )
	  throws com.amalto.core.util.XtentisException, java.rmi.RemoteException
   {
        com.amalto.core.objects.synchronization.ejb.SynchronizationObjectPOJOPK retval;
       retval =  getSession().putSynchronizationObject( synchronizationObject );

      return retval;

   }

   public com.amalto.core.objects.synchronization.ejb.SynchronizationObjectPOJO getSynchronizationObject ( com.amalto.core.objects.synchronization.ejb.SynchronizationObjectPOJOPK pk )
	  throws com.amalto.core.util.XtentisException, java.rmi.RemoteException
   {
        com.amalto.core.objects.synchronization.ejb.SynchronizationObjectPOJO retval;
       retval =  getSession().getSynchronizationObject( pk );

      return retval;

   }

   public com.amalto.core.objects.synchronization.ejb.SynchronizationObjectPOJO existsSynchronizationObject ( com.amalto.core.objects.synchronization.ejb.SynchronizationObjectPOJOPK pk )
	  throws com.amalto.core.util.XtentisException, java.rmi.RemoteException
   {
        com.amalto.core.objects.synchronization.ejb.SynchronizationObjectPOJO retval;
       retval =  getSession().existsSynchronizationObject( pk );

      return retval;

   }

   public com.amalto.core.objects.synchronization.ejb.SynchronizationObjectPOJOPK removeSynchronizationObject ( com.amalto.core.objects.synchronization.ejb.SynchronizationObjectPOJOPK pk )
	  throws com.amalto.core.util.XtentisException, java.rmi.RemoteException
   {
        com.amalto.core.objects.synchronization.ejb.SynchronizationObjectPOJOPK retval;
       retval =  getSession().removeSynchronizationObject( pk );

      return retval;

   }

   public java.util.Collection getSynchronizationObjectPKs ( java.lang.String regex )
	  throws com.amalto.core.util.XtentisException, java.rmi.RemoteException
   {
        java.util.Collection retval;
       retval =  getSession().getSynchronizationObjectPKs( regex );

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
