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

package ui.dialog;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import org.dockfx.DockNode;
import ui.controller.NewLocalGameController;

import java.io.IOException;

public class LocalGameDialog {

	public static DockNode build() {
		FXMLLoader loader = new FXMLLoader(LocalGameDialog.class.getResource("/newLocalGame.fxml"));

		Parent dialog = null;
		try {
			dialog = loader.load();
		}
		catch (IOException e) {
			e.printStackTrace();
		}

		DockNode node = new DockNode(dialog, "New Local Game");

		NewLocalGameController controller = loader.<NewLocalGameController>getController();
		controller.setNode(node);

		return node;
	}
}
