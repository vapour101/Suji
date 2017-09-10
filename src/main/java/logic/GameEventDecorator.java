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

package logic;

import event.GameEvent;
import util.Coords;
import util.Move;
import util.StoneColour;

import java.util.Collection;

public class GameEventDecorator implements GameHandler {

	private GameHandler instance;

	public GameEventDecorator(GameHandler game) {
		instance = game;
	}

	@Override
	public boolean isLegalMove(Move move) {
		return instance.isLegalMove(move);
	}

	@Override
	public void playMove(Move move) {
		Board previousPosition = instance.getBoard();
		instance.playMove(move);

		if ( !previousPosition.equals(instance.getBoard()) )
			fireGameEvent();
	}

	@Override
	public void pass() {

		GameTree tree = instance.getGameTree();
		boolean gameOver = false;
		if ( tree.getMoveNumber() > 0 && tree.getLastMove().getType() == Move.Type.PASS )
			gameOver = true;

		instance.pass();

		if ( gameOver )
			fireGameOverEvent();
		else
			fireGameEvent();
	}

	@Override
	public void undo() {
		Board previousPosition = instance.getBoard();
		instance.undo();

		if ( !previousPosition.equals(instance.getBoard()) )
			fireGameEvent();
	}

	@Override
	public Collection<Coords> getStones(StoneColour colour) {
		return instance.getStones(colour);
	}

	@Override
	public Board getBoard() {
		return instance.getBoard();
	}

	@Override
	public StoneColour getTurnPlayer() {
		return instance.getTurnPlayer();
	}

	@Override
	public GameTree getGameTree() {
		return instance.getGameTree();
	}

	private void fireGameOverEvent() {
		GameEvent.fireGameEvent(this, GameEvent.GAMEOVER);
	}

	private void fireGameEvent() {
		GameEvent.fireGameEvent(this, GameEvent.ANY);
	}
}
