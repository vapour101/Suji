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

import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import ogs.Playerdata;
import org.dockfx.DockNode;
import org.dockfx.DockPos;
import ui.Main;
import util.WebHelper;

import java.net.URL;
import java.util.ResourceBundle;

public class OGSPlayer extends DockNodeController implements Initializable {

	public ImageView avatar;
	public Label username;
	public Label rank;

	private int id;

	public OGSPlayer(int playerID) {
		id = playerID;
	}

	private void onPlayerdata(Playerdata player) {
		username.setText(player.getUsername());
		rank.setText(player.getRankString());

		WebHelper.requestImage(player.getAvatarURL(128), avatar::setImage);
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		Playerdata.requestPlayerdata(id, this::onPlayerdata);
	}

	@Override
	protected String getResourcePath() {
		return "/fxml/ogsPlayer.fxml";
	}

	@Override
	public void dock() {
		synchronized (OGSPlayer.class) {
			DockNode node = getDockNode();
			node.setTitle("Player Info");
			node.dock(Main.instance.dockPane, DockPos.RIGHT);
		}
	}
}
