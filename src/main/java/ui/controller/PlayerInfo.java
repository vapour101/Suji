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
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import ogs.GameList;
import ogs.OGSReference;
import ogs.REST;
import util.LogHelper;

import java.net.URL;
import java.util.ResourceBundle;

public class PlayerInfo extends SelfBuildingController implements Initializable{

	public ImageView blackAvatar;
	public ImageView whiteAvatar;
	public Label blackName;
	public Label whiteName;

	private GameList.Game game;

	PlayerInfo(GameList.Game gameMeta) {
		game = gameMeta;
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		blackName.setText(game.getBlackName());
		whiteName.setText(game.getWhiteName());

		int black = game.getBlackPlayer().getId();
		int white = game.getWhitePlayer().getId();

		LogHelper.finest("BlackID =  " + black);
		LogHelper.finest("WhiteID =  " + white);

		REST.requestPlayerIcon(black, s -> this.setBlackAvatar(s));
		//REST.requestPlayerIcon(white, this::setWhiteAvatar);
	}

	private void setBlackAvatar(String url) {
		blackAvatar.setImage(new Image(url, true));
	}

	private void setWhiteAvatar(String url) {
		whiteAvatar.setImage(new Image(url, true));
	}

	@Override
	protected String getResourcePath() {
		return "/playerInfo.fxml";
	}
}
