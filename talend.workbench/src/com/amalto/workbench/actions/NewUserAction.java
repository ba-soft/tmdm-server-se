package com.amalto.workbench.actions;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.widgets.Shell;

import com.amalto.workbench.dialogs.NewUserWizard;
import com.amalto.workbench.models.TreeObject;
import com.amalto.workbench.utils.IConstants;
import com.amalto.workbench.utils.ImageCache;
import com.amalto.workbench.utils.LocalTreeObjectRepository;
import com.amalto.workbench.views.ServerView;

public class NewUserAction extends Action {
	private ServerView view = null;
	public NewUserAction(ServerView view){
		super();
		this.view = view;
		setImageDescriptor(ImageCache.getImage( "icons/add_obj.gif"));
		setText("New");
		setToolTipText("Create a new Role");
	}
	public void run() {
		super.run();
		ISelection selection = view.getViewer().getSelection();
        TreeObject xobject = (TreeObject)((IStructuredSelection)selection).getFirstElement();
        xobject = LocalTreeObjectRepository.getInstance().registerNewTreeObject(xobject);  
		NewUserWizard wizard = new NewUserWizard(xobject,view);
		WizardDialog dialog = new WizardDialog(view.getSite().getShell(), wizard);
		dialog.open();
	}
}
