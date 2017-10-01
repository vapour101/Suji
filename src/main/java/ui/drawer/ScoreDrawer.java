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


import event.ScoreEvent;
import javafx.event.EventHandler;
import logic.board.Board;
import logic.gamehandler.GameHandler;
import logic.score.Scorer;
import util.Coords;
import util.StoneColour;

import java.util.Collection;

public class ScoreDrawer extends GameDrawer {

	private Scorer scorer;
	private EventHandler<ScoreEvent> scoreChangeHandler = this::onScoreChange;

	public ScoreDrawer(GameHandler game, GameDrawer clone, Scorer scorer) {
		super(clone);
		setUpScorer(scorer);
		game.subscribe(ScoreEvent.SCORE, scoreChangeHandler);
	}

	private void setUpScorer(Scorer scorer) {
		this.scorer = scorer;
	}

	private void onScoreChange(ScoreEvent event) {
		if ( event.getScorer() != scorer )
			return;
		redraw();
	}

	@Override
	public void draw(Board board) {
		super.draw(board);
		drawTerritory();
	}

	@Override
	void drawStones(Board board, StoneColour colour) {
		StoneDrawer drawer = getStoneDrawer();

		Collection<Coords> stones = board.getStones(colour);
		stones.removeAll(scorer.getDeadStones(colour));

		drawer.drawGhostStones(scorer.getDeadStones(colour), colour);

		drawer.drawStones(stones, colour);
	}

	private void drawTerritory() {
		for (StoneColour colour : StoneColour.values())
			drawTerritory(colour);
	}

	private void drawTerritory(StoneColour colour) {
		StoneDrawer drawer = getStoneDrawer();

		drawer.drawStones(scorer.getTerritory(colour), colour, 0.5);
	}
}
