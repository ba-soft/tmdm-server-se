/*
 * Generated by XDoclet - Do not edit!
 */
package com.amalto.core.ejb.local;

/**
 * Local home interface for ItemCtrl2.
 * @xdoclet-generated at 30-09-09
 * @copyright The XDoclet Team
 * @author XDoclet
 * @version ${version}
 */
public interface ItemCtrl2LocalHome
   extends javax.ejb.EJBLocalHome
{
   public static final String COMP_NAME="java:comp/env/ejb/ItemCtrl2Local";
   public static final String JNDI_NAME="amalto/local/core/itemctrl2";

   public com.amalto.core.ejb.local.ItemCtrl2Local create()
      throws javax.ejb.CreateException;

}
