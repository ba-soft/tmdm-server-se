/*
 * Generated by XDoclet - Do not edit!
 */
package com.amalto.core.objects.role.ejb.local;

/**
 * Local home interface for RoleCtrl.
 * @xdoclet-generated at 10-09-09
 * @copyright The XDoclet Team
 * @author XDoclet
 * @version ${version}
 */
public interface RoleCtrlLocalHome
   extends javax.ejb.EJBLocalHome
{
   public static final String COMP_NAME="java:comp/env/ejb/RoleCtrlLocal";
   public static final String JNDI_NAME="amalto/local/core/rolectrl";

   public com.amalto.core.objects.role.ejb.local.RoleCtrlLocal create()
      throws javax.ejb.CreateException;

}
