/*
 * Project Info:  http://jcae.sourceforge.net
 *
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program; if not, write to the Free Software Foundation, Inc.,
 * 59 Temple Place, Suite 330, Boston, MA 02111-1307, USA.
 *
 * (C) Copyright 2009, by EADS France
 */

package org.jcae.netbeans.mesh.bora;

import java.io.IOException;
import java.util.Collections;
import java.util.NoSuchElementException;
import java.util.Set;
import javax.swing.event.ChangeListener;
import org.openide.WizardDescriptor;
import org.openide.WizardDescriptor.Panel;
import org.openide.filesystems.FileObject;
import org.openide.loaders.DataObject;
import org.openide.loaders.TemplateWizard;

/**
 *
 * @author Jerome Robert
 */
public class BoraNewWizard implements WizardDescriptor.InstantiatingIterator<WizardDescriptor> {
	private TemplateWizard wizard;
	
	public Set<BoraDataObject> instantiate() throws IOException {
		FileObject fo = wizard.getTargetFolder().getPrimaryFile();
		FileObject m = fo.createFolder(wizard.getTargetName()+".bora");
		BoraDataObject mdo = (BoraDataObject) DataObject.find(m);
		mdo.save();
		return Collections.singleton(mdo);
	}

	public void initialize(WizardDescriptor wizard) {
		if(wizard instanceof TemplateWizard)
			this.wizard = ((TemplateWizard)wizard);
	}

	public void uninitialize(WizardDescriptor wizard) {
		wizard = null;
	}

	public Panel<WizardDescriptor> current() {
		return wizard.targetChooser();
	}

	public String name() {
		return "";
	}

	public boolean hasNext() {
		return false;
	}

	public boolean hasPrevious() {
		return false;
	}

	public void nextPanel() {
		throw new NoSuchElementException();
	}

	public void previousPanel() {
		throw new NoSuchElementException();
	}

	public void addChangeListener(ChangeListener l) {}

	public void removeChangeListener(ChangeListener l) {}
}
