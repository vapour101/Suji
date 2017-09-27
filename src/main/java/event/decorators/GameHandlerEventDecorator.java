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

package event.decorators;

import event.GameEvent;
import logic.board.Board;
import logic.gamehandler.GameHandler;
import logic.gamehandler.GameHandlerDecorator;
import logic.gametree.GameTree;
import logic.score.Scorer;
import util.Move;

public class GameHandlerEventDecorator extends GameHandlerDecorator {

	public GameHandlerEventDecorator(GameHandler game) {
		super(game);
	}

	@Override
	public void pass() {

		GameTree tree = getGameTree();
		boolean gameOver = false;
		if ( tree.getNumMoves() > 0 && tree.getLastMove().getType() == Move.Type.PASS )
			gameOver = true;

		super.pass();

		if ( gameOver )
			fireGameOverEvent();
		else
			fireGameEvent();
	}

	@Override
	public void undo() {
		Board previousPosition = getBoard();
		super.undo();

		if ( !previousPosition.equals(getBoard()) )
			fireGameEvent();
	}

	@Override
	public void playMove(Move move) {
		Board previousPosition = getBoard();
		super.playMove(move);

		if ( !previousPosition.equals(getBoard()) )
			fireGameEvent();
	}

	@Override
	public Scorer getScorer() {
		return new ScorerEventDecorator(super.getScorer());
	}

	private void fireGameOverEvent() {
		GameEvent.fireGameEvent(this, GameEvent.GAMEOVER);
	}

	private void fireGameEvent() {
		GameEvent.fireGameEvent(this, GameEvent.GAME);
	}
}
