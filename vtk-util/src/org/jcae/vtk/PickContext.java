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
 * (C) Copyright 2008, by EADS France
 */
package org.jcae.vtk;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import org.jcae.geometry.BoundingBox;

/**
 * A PickContext instance defines picking behavior and stores pick hits.
 * This is the base class for {@link RayPicker} and {@link FrustumPicker}
 * concrete classes, which corresponds respectively to point picking and
 * rubber band selection.
 * 
 * @author Denis Barbier
 */
public abstract class PickContext
{
	private final Canvas canvas;
	private final boolean visible;
	
	/* Set of selected leaves */
	private Set<LeafNode> selectionNode = new HashSet<LeafNode>();
	
	/**
	 * Constructor.
	 * 
	 * @param canvas  underlying canvas
	 * @param visible  if <code>true</code>, only visible objects are picked,
	 * otherwise any pickable object can be picked
	 */
	public PickContext(Canvas canvas, boolean visible)
	{
		this.canvas = canvas;
		this.visible = visible;
	}

	/**
	 * Get the value of canvas.
	 *
	 * @return the value of canvas
	 */
	public Canvas getCanvas()
	{
		return canvas;
	}

	/**
	 * Tell whether only visible objects are picked.
	 * 
	 * @return <code>true</code> if only visible objects are selected,
	 * <code>false</code> otherwise.
	 */
	public boolean onlyVisible()
	{
		return visible;
	}
	
	/**
	 * Get the set of selected leaves.
	 * 
	 * @return the set of selected leaves
	 */
	public Set<LeafNode> getSelectedNodes()
	{
		return Collections.unmodifiableSet(selectionNode);
	}

	/**
	 * Add a leaf to the set of selected nodes.
	 * 
	 * @param leaf  leaf to declare as being selected
	 */
	void addToSelectedNodes(LeafNode leaf)
	{
		selectionNode.add(leaf);
	}
	
	/**
	 * Get the mouse position when button was pressed.
	 * 
	 * @return the position of mouse when button was pressed
	 */
	public abstract int [] getPressPosition();
	
	/**
	 * Get the mouse position when button was released.
	 * 
	 * @return Get the position of mouse when button was released
	 */
	public abstract int [] getReleasePosition();
	
	/**
	 * Tell whether a box intersects this picker.
	 * 
	 * @param bbox  box to be checked for
	 * @return <code>true</code> if this box is intersected by current picker,
	 * <code>false</code> otherwise.
	 */
  	public abstract boolean intersect(BoundingBox bbox);
}
