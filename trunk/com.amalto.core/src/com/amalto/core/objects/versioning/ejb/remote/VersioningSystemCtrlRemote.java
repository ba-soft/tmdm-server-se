 /*
 * Generated by XDoclet - Do not edit!
 * this class was prodiuced by xdoclet automagically...
 */
package com.amalto.core.objects.versioning.ejb.remote;

import java.util.*;

/**
 * This class is remote adapter to VersioningSystemCtrl. It provides convenient way to access
 * facade session bean. Inverit from this class to provide reasonable caching and event handling capabilities.
 *
 * Remote facade for VersioningSystemCtrl.
 * @xdoclet-generated at 3-08-09
 * @copyright The XDoclet Team
 * @author XDoclet
 * @version ${version}
 */

public class VersioningSystemCtrlRemote extends Observable
{
    static VersioningSystemCtrlRemote _instance = null;
    public static VersioningSystemCtrlRemote getInstance() {
        if(_instance == null) {
	   _instance = new VersioningSystemCtrlRemote();
	}
	return _instance;
    }

  /**
   * cached remote session interface
   */
  com.amalto.core.objects.versioning.ejb.remote.VersioningSystemCtrl _session = null;
  /**
   * return session bean remote interface
   */
   protected com.amalto.core.objects.versioning.ejb.remote.VersioningSystemCtrl getSession() {
      try {
   	if(_session == null) {
	   _session = com.amalto.core.objects.versioning.ejb.local.VersioningSystemCtrlUtil.getHome().create();
	}
	return _session;
      } catch(Exception ex) {
        // just catch it here and return null.
        // somebody can provide better solution
	ex.printStackTrace();
	return null;
      }
   }

   public com.amalto.core.objects.versioning.ejb.VersioningSystemPOJOPK putVersioningSystem ( com.amalto.core.objects.versioning.ejb.VersioningSystemPOJO versioningSystem )
	  throws com.amalto.core.util.XtentisException, java.rmi.RemoteException
   {
        com.amalto.core.objects.versioning.ejb.VersioningSystemPOJOPK retval;
       retval =  getSession().putVersioningSystem( versioningSystem );

      return retval;

   }

   public com.amalto.core.objects.versioning.ejb.VersioningSystemPOJO getVersioningSystem ( com.amalto.core.objects.versioning.ejb.VersioningSystemPOJOPK pk )
	  throws com.amalto.core.util.XtentisException, java.rmi.RemoteException
   {
        com.amalto.core.objects.versioning.ejb.VersioningSystemPOJO retval;
       retval =  getSession().getVersioningSystem( pk );

      return retval;

   }

   public com.amalto.core.objects.versioning.ejb.VersioningSystemPOJO existsVersioningSystem ( com.amalto.core.objects.versioning.ejb.VersioningSystemPOJOPK pk )
	  throws com.amalto.core.util.XtentisException, java.rmi.RemoteException
   {
        com.amalto.core.objects.versioning.ejb.VersioningSystemPOJO retval;
       retval =  getSession().existsVersioningSystem( pk );

      return retval;

   }

   public com.amalto.core.objects.versioning.ejb.VersioningSystemPOJOPK removeVersioningSystem ( com.amalto.core.objects.versioning.ejb.VersioningSystemPOJOPK pk )
	  throws com.amalto.core.util.XtentisException, java.rmi.RemoteException
   {
        com.amalto.core.objects.versioning.ejb.VersioningSystemPOJOPK retval;
       retval =  getSession().removeVersioningSystem( pk );

      return retval;

   }

   public java.util.ArrayList getVersioningSystemPKs ( java.lang.String regex )
	  throws com.amalto.core.util.XtentisException, java.rmi.RemoteException
   {
        java.util.ArrayList retval;
       retval =  getSession().getVersioningSystemPKs( regex );

      return retval;

   }

   public com.amalto.core.objects.versioning.util.VersioningServiceCtrlLocalBI setDefaultVersioningSystem ( com.amalto.core.objects.versioning.ejb.VersioningSystemPOJOPK pk )
	  throws com.amalto.core.util.XtentisException, java.rmi.RemoteException
   {
        com.amalto.core.objects.versioning.util.VersioningServiceCtrlLocalBI retval;
       retval =  getSession().setDefaultVersioningSystem( pk );

      return retval;

   }

   public java.lang.String getVersioningSystemAvailability ( com.amalto.core.objects.versioning.ejb.VersioningSystemPOJOPK pk )
	  throws com.amalto.core.util.XtentisException, java.rmi.RemoteException
   {
        java.lang.String retval;
       retval =  getSession().getVersioningSystemAvailability( pk );

      return retval;

   }

   public com.amalto.core.objects.backgroundjob.ejb.BackgroundJobPOJOPK tagObjectsAsJob ( com.amalto.core.objects.versioning.ejb.VersioningSystemPOJOPK versioningSystemPOJOPK,java.lang.String tag,java.lang.String comment,java.lang.String type,java.lang.String[] instances )
	  throws com.amalto.core.util.XtentisException, java.rmi.RemoteException
   {
        com.amalto.core.objects.backgroundjob.ejb.BackgroundJobPOJOPK retval;
       retval =  getSession().tagObjectsAsJob( versioningSystemPOJOPK,tag,comment,type,instances );

      return retval;

   }

   public com.amalto.core.objects.backgroundjob.ejb.BackgroundJobPOJOPK tagItemsAsJob ( com.amalto.core.objects.versioning.ejb.VersioningSystemPOJOPK versioningSystemPOJOPK,java.lang.String tag,java.lang.String comment,com.amalto.core.ejb.ItemPOJOPK[] itemPKs )
	  throws com.amalto.core.util.XtentisException, java.rmi.RemoteException
   {
        com.amalto.core.objects.backgroundjob.ejb.BackgroundJobPOJOPK retval;
       retval =  getSession().tagItemsAsJob( versioningSystemPOJOPK,tag,comment,itemPKs );

      return retval;

   }

   public com.amalto.core.objects.backgroundjob.ejb.BackgroundJobPOJOPK restoreObjectsAsJob ( com.amalto.core.objects.versioning.ejb.VersioningSystemPOJOPK versioningSystemPOJOPK,java.lang.String tag,java.lang.String type,java.lang.String[] instances )
	  throws com.amalto.core.util.XtentisException, java.rmi.RemoteException
   {
        com.amalto.core.objects.backgroundjob.ejb.BackgroundJobPOJOPK retval;
       retval =  getSession().restoreObjectsAsJob( versioningSystemPOJOPK,tag,type,instances );

      return retval;

   }

   public com.amalto.core.objects.backgroundjob.ejb.BackgroundJobPOJOPK restoreItemsAsJob ( com.amalto.core.objects.versioning.ejb.VersioningSystemPOJOPK versioningSystemPOJOPK,java.lang.String tag,com.amalto.core.ejb.ItemPOJOPK[] itemPKs )
	  throws com.amalto.core.util.XtentisException, java.rmi.RemoteException
   {
        com.amalto.core.objects.backgroundjob.ejb.BackgroundJobPOJOPK retval;
       retval =  getSession().restoreItemsAsJob( versioningSystemPOJOPK,tag,itemPKs );

      return retval;

   }

   public com.amalto.core.webservice.WSVersioningObjectsHistory getObjectsHistory ( com.amalto.core.objects.versioning.ejb.VersioningSystemPOJOPK versioningSystemPOJOPK,java.lang.String type,java.lang.String[] instances )
	  throws com.amalto.core.util.XtentisException, java.rmi.RemoteException
   {
        com.amalto.core.webservice.WSVersioningObjectsHistory retval;
       retval =  getSession().getObjectsHistory( versioningSystemPOJOPK,type,instances );

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
