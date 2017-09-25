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

package ui.controller;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.util.Builder;
import org.dockfx.DockNode;

import java.io.IOException;

public abstract class SelfBuildingController implements Builder<DockNode> {

	private Parent root;
	private DockNode node;

	public SelfBuildingController() {
		root = null;
	}

	@Override
	public final DockNode build() {
		return getNode();
	}

	final DockNode getNode() {
		if ( node == null )
			constructNode();

		return node;
	}

	private void constructNode() {
		node = new DockNode(getRoot());
	}

	public final Parent getRoot() {
		if ( root == null )
			constructRoot();

		return root;
	}

	private void constructRoot() {
		FXMLLoader loader = new FXMLLoader(this.getClass().getResource(getResourcePath()));
		loader.setControllerFactory(type -> this);

		try {
			root = loader.load();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}

	abstract protected String getResourcePath();
}
