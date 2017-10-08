/*
 * Copyright (c) 2017
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package logic.gametree;

import util.Move;

import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

public class TreeNode {

	private Vector<TreeNode> children;
	private TreeNode parent;
	private Map<PropertyType, Object> properties;

	TreeNode() {
		parent = null;
		children = new Vector<>();
		properties = new HashMap<>();
	}

	public Vector<TreeNode> getChildren() {
		return children;
	}

	public void addChild(TreeNode child) {
		child.setParent(this);
		children.add(child);
	}

	public int getDepth() {
		if ( getParent() == null )
			return 1;

		return getParent().getDepth() + 1;
	}

	public TreeNode getParent() {
		return parent;
	}

	public void setParent(TreeNode parent) {
		if ( this.parent != null ) {
			parent.removeChild(this);
		}

		this.parent = parent;
	}

	public void removeChild(TreeNode child) {
		children.remove(child);
	}

	public Move getMove() {
		if ( !hasMove() )
			return null;

		return (Move) properties.get(PropertyType.MOVE);
	}

	public void setMove(Move move) {
		properties.put(PropertyType.MOVE, move);
	}

	public boolean hasMove() {
		return properties.containsKey(PropertyType.MOVE);
	}

	public String getComment() {
		if ( !hasComment() )
			return null;

		return (String) properties.get(PropertyType.COMMENT);
	}

	public void setComment(String comment) {
		properties.put(PropertyType.COMMENT, comment);
	}

	public boolean hasComment() {
		return properties.containsKey(PropertyType.COMMENT);
	}

	protected enum PropertyType {
		MOVE, COMMENT
	}
}
