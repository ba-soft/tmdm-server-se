/*
 * Generated by XDoclet - Do not edit!
 */
package com.amalto.core.ejb.remote;

/**
 * Remote interface for DroppedItemCtrl.
 * @xdoclet-generated at 10-09-09
 * @copyright The XDoclet Team
 * @author XDoclet
 * @version ${version}
 */
public interface DroppedItemCtrl
   extends javax.ejb.EJBObject
{
   /**
    * Recover a dropped item
    * @throws XtentisException
    */
   public com.amalto.core.ejb.ItemPOJOPK recoverDroppedItem( com.amalto.core.ejb.DroppedItemPOJOPK droppedItemPOJOPK )
      throws com.amalto.core.util.XtentisException, java.rmi.RemoteException;

   /**
    * Find all dropped items pks
    * @throws XtentisException
    */
   public java.util.List findAllDroppedItemsPKs( java.lang.String regex )
      throws com.amalto.core.util.XtentisException, java.rmi.RemoteException;

   /**
    * Load a dropped item
    * @throws XtentisException
    */
   public com.amalto.core.ejb.DroppedItemPOJO loadDroppedItem( com.amalto.core.ejb.DroppedItemPOJOPK droppedItemPOJOPK )
      throws com.amalto.core.util.XtentisException, java.rmi.RemoteException;

   /**
    * Remove a dropped item
    * @throws XtentisException
    */
   public com.amalto.core.ejb.DroppedItemPOJOPK removeDroppedItem( com.amalto.core.ejb.DroppedItemPOJOPK droppedItemPOJOPK )
      throws com.amalto.core.util.XtentisException, java.rmi.RemoteException;

}
