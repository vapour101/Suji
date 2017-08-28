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

package ui.drawer;


import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import logic.Board;
import logic.BoardScorer;
import logic.GameHandler;
import util.CoordProjector;
import util.Coords;
import util.DrawCoords;
import util.StoneColour;

import java.util.Collection;

public class BoardScoreDrawer extends BoardDrawer {

	private BoardScorer scorer;

	public BoardScoreDrawer(Canvas canvas, BoardScorer scorer) {
		super(canvas);
		this.scorer = scorer;
	}

	@Override
	public void draw(GameHandler board) {
		super.draw(board);
		drawTerritory();
	}

	@Override
	void drawStones(Board board, StoneColour colour) {
		double radius = getStoneRadius();
		GraphicsContext context = getGraphicsContext();

		Collection<Coords> stones = board.getStones(colour);
		stones.removeAll(scorer.getDeadStones(colour));

		context.setGlobalAlpha(0.5);
		drawStonesToCanvas(scorer.getDeadStones(colour), radius, colour);
		context.setGlobalAlpha(1);

		drawStonesToCanvas(stones, radius, colour);
	}

	private void drawTerritory() {
		for (StoneColour colour : StoneColour.values())
			drawTerritory(colour);
	}

	private void drawTerritory(StoneColour colour) {
		double radius = getStoneRadius() / 2;
		CoordProjector projector = getProjector();

		for (Coords stone : scorer.getTerritory(colour)) {
			DrawCoords position = projector.fromBoardCoords(stone);
			drawStoneToCanvas(position, radius, colour);
		}
	}
}
