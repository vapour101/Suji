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

package ui.controller.strategy;

import javafx.scene.canvas.Canvas;
import javafx.scene.input.MouseEvent;
import logic.gamehandler.GameHandler;
import util.CoordProjector;
import util.Coords;
import util.DrawCoords;
import util.StoneColour;

import static util.DimensionHelper.getProjector;
import static util.Move.play;

public class GamePlay implements BoardStrategy {

	private Canvas boardCanvas;
	private GameHandler game;

	public GamePlay(Canvas canvas, GameHandler gameHandler) {
		boardCanvas = canvas;
		game = gameHandler;
	}

	@Override
	public void canvasClicked(MouseEvent mouseEvent) {
		DrawCoords mousePosition = new DrawCoords(mouseEvent.getX(), mouseEvent.getY());
		CoordProjector projector = getProjector(boardCanvas);
		Coords boardPos = projector.nearestCoords(mousePosition);

		if ( !projector.isWithinBounds(mousePosition) )
			return;

		if ( game.isLegalMove(play(boardPos, getTurnPlayer())) ) {
			game.playMove(play(boardPos, getTurnPlayer()));
		}
	}

	private StoneColour getTurnPlayer() {
		return game.getTurnPlayer();
	}

	@Override
	public void canvasExit(MouseEvent mouseEvent) {
		//gameDrawer.setHoverStone(new DrawCoords(-1, -1), getTurnPlayer());
	}
}
