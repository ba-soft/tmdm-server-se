/*
 * Generated by XDoclet - Do not edit!
 */
package com.amalto.core.objects.universe.ejb.remote;

/**
 * Home interface for UniverseCtrl.
 * @xdoclet-generated at 30-09-09
 * @copyright The XDoclet Team
 * @author XDoclet
 * @version ${version}
 */
public interface UniverseCtrlHome
   extends javax.ejb.EJBHome
{
   public static final String COMP_NAME="java:comp/env/ejb/UniverseCtrl";
   public static final String JNDI_NAME="amalto/remote/core/universectrl";

   public com.amalto.core.objects.universe.ejb.remote.UniverseCtrl create()
      throws javax.ejb.CreateException,java.rmi.RemoteException;

}
