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
	public void draw(Board board) {
		super.draw(board);
		drawTerritory();
	}

	@Override
	void drawStones(Board board) {
		double radius = getStoneRadius();
		GraphicsContext context = getGraphicsContext();

		Collection<Coords> blackStones = board.getBlackStones();
		Collection<Coords> whiteStones = board.getWhiteStones();

		blackStones.removeAll(scorer.getDeadStones(StoneColour.BLACK));
		whiteStones.removeAll(scorer.getDeadStones(StoneColour.WHITE));

		context.setGlobalAlpha(0.5);

		drawStones(scorer.getDeadStones(StoneColour.BLACK), radius, StoneColour.BLACK);
		drawStones(scorer.getDeadStones(StoneColour.WHITE), radius, StoneColour.WHITE);

		context.setGlobalAlpha(1);

		drawStones(blackStones, radius, StoneColour.BLACK);
		drawStones(whiteStones, radius, StoneColour.WHITE);
	}

	private void drawTerritory() {
		double radius = getStoneRadius() / 2;
		DrawCoords offset = new DrawCoords(0, 0);
		CoordProjector projector = getProjector();

		for (Coords stone : scorer.getTerritory(StoneColour.BLACK)) {
			DrawCoords pos = projector.fromBoardCoords(stone);
			pos.applyOffset(offset);
			drawStone(pos, radius, StoneColour.BLACK);
		}
		for (Coords stone : scorer.getTerritory(StoneColour.WHITE)) {
			DrawCoords pos = projector.fromBoardCoords(stone);
			pos.applyOffset(offset);
			drawStone(pos, radius, StoneColour.WHITE);
		}
	}
}
