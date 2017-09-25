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

import javafx.scene.canvas.Canvas;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import logic.score.Scorer;
import ui.controller.sidebar.ScorePaneController;
import util.CoordProjector;
import util.Coords;
import util.DrawCoords;

import static util.DimensionHelper.getProjector;

public class Scoring implements BoardStrategy {

	private Canvas boardCanvas;
	private Scorer boardScorer;
	private ScorePaneController scorePane;

	public Scoring(Canvas canvas, Scorer scorer, ScorePaneController scorePaneController) {
		boardCanvas = canvas;
		boardScorer = scorer;
		scorePane = scorePaneController;
	}

	@Override
	public void canvasClicked(MouseEvent mouseEvent) {
		DrawCoords mousePosition = new DrawCoords(mouseEvent.getX(), mouseEvent.getY());
		CoordProjector projector = getProjector(boardCanvas);
		Coords boardPos = projector.nearestCoords(mousePosition);

		if ( !projector.isWithinBounds(mousePosition) )
			return;

		scorePane.enableButtons();
		if ( mouseEvent.getButton() == MouseButton.PRIMARY )
			boardScorer.markGroupDead(boardPos);
		else if ( mouseEvent.getButton() == MouseButton.SECONDARY )
			boardScorer.unmarkGroupDead(boardPos);
	}

	@Override
	public void canvasHover(MouseEvent mouseEvent) {

	}

	@Override
	public void canvasExit(MouseEvent mouseEvent) {

	}
}
