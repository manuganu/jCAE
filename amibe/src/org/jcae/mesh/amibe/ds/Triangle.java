/* jCAE stand for Java Computer Aided Engineering. Features are : Small CAD
   modeler, Finite element mesher, Plugin architecture.

    Copyright (C) 2004,2005,2006, by EADS CRC
    Copyright (C) 2007, by EADS France

    This library is free software; you can redistribute it and/or
    modify it under the terms of the GNU Lesser General Public
    License as published by the Free Software Foundation; either
    version 2.1 of the License, or (at your option) any later version.

    This library is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
    Lesser General Public License for more details.

    You should have received a copy of the GNU Lesser General Public
    License along with this library; if not, write to the Free Software
    Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA */

package org.jcae.mesh.amibe.ds;

import org.jcae.mesh.amibe.traits.TriangleTraitsBuilder;
import java.io.Serializable;

/**
 * A triangle containing adjacency relations.
 */
public abstract class Triangle extends AbstractTriangle implements Serializable
{
	public Triangle(TriangleTraitsBuilder ttb)
	{
		super(ttb);
	}

	/**
	 * Returns the adjacent AbstractTriangle.
	 *
	 * @param num  the local number of this edge.
	 * @return the adjacent AbstractTriangle.
	 */
	public abstract AdjacencyWrapper getAdj(int num);
	
	/**
	 * Sets the AbstractTriangle adjacent to an edge.
	 *
	 * @param num  the local number of this edge.
	 * @param link  the adjacent AbstractTriangle.
	 */
	public abstract void setAdj(int num, AdjacencyWrapper link);
	
	/**
	 * Sets attributes for all edges of this triangle.
	 *
	 * @param attr  attributes to set on edges
	 */
	public abstract void setAttributes(int attr);
	
	/**
	 * Resets attributes for all edges of this triangle.
	 *
	 * @param attr  attributes to reset on edges
	 */
	public abstract void clearAttributes(int attr);
	
	/**
	 * Checks if some attributes of this triangle are set.
	 *
	 * @param attr  attributes to check
	 * @return <code>true</code> if any edge of this triangle has
	 * one of these attributes set, <code>false</code> otherwise
	 */
	public abstract boolean hasAttributes(int attr);
	
	/**
	 * Gets an <code>AbstractHalfEdge</code> instance bound to this triangle.
	 */
	public abstract AbstractHalfEdge getAbstractHalfEdge();

	/**
	 * Gets an <code>AbstractHalfEdge</code> instance bound to this triangle.
	 */
	public abstract AbstractHalfEdge getAbstractHalfEdge(AbstractHalfEdge that);

}
