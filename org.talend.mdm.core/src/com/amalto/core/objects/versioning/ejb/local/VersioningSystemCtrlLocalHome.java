/*
 * Generated by XDoclet - Do not edit!
 */
package com.amalto.core.objects.versioning.ejb.local;

/**
 * Local home interface for VersioningSystemCtrl.
 * @xdoclet-generated at 30-09-09
 * @copyright The XDoclet Team
 * @author XDoclet
 * @version ${version}
 */
public interface VersioningSystemCtrlLocalHome
   extends javax.ejb.EJBLocalHome
{
   public static final String COMP_NAME="java:comp/env/ejb/VersioningSystemCtrlLocal";
   public static final String JNDI_NAME="amalto/local/core/versioningsystemctrl";

   public com.amalto.core.objects.versioning.ejb.local.VersioningSystemCtrlLocal create()
      throws javax.ejb.CreateException;

}
