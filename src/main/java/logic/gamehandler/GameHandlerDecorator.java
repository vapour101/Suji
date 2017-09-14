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

package logic.gamehandler;


import logic.board.Board;
import logic.board.Scorer;
import logic.gametree.GameTree;
import sgf.SGFWriter;
import util.Coords;
import util.Move;
import util.StoneColour;

import java.util.Collection;

public class GameHandlerDecorator implements GameHandler {

	private GameHandler instance;

	protected GameHandlerDecorator(GameHandler gameHandler) {
		this.instance = gameHandler;
	}

	@Override
	public void pass() {
		instance.pass();
	}

	@Override
	public void undo() {
		instance.undo();
	}

	@Override
	public StoneColour getTurnPlayer() {
		return instance.getTurnPlayer();
	}

	@Override
	public void setKomi(double komi) {
		instance.setKomi(komi);
	}

	@Override
	public Board getBoard() {
		return instance.getBoard();
	}

	@Override
	public boolean isLegalMove(Move move) {
		return instance.isLegalMove(move);
	}

	@Override
	public void playMove(Move move) {
		instance.playMove(move);
	}

	@Override
	public Collection<Coords> getStones(StoneColour colour) {
		return instance.getStones(colour);
	}

	@Override
	public GameTree getGameTree() {
		return instance.getGameTree();
	}

	@Override
	public SGFWriter getSGFWriter() {
		return instance.getSGFWriter();
	}

	@Override
	public Scorer getScorer() {
		return instance.getScorer();
	}
}
