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
import logic.board.BoardScorer;
import logic.gametree.ComplexGameTree;
import logic.gametree.GameTree;
import sgf.SGFWriter;
import sgf.SimpleSGFWriter;
import util.Coords;
import util.Move;
import util.StoneColour;

import java.util.Collection;

public class LocalGameHandler implements GameHandler {

	private GameTree gameTree;
	private int handicap;
	private double komi;

	LocalGameHandler() {
		this(0);
	}

	public LocalGameHandler(int handicap) {
		gameTree = new ComplexGameTree();
		this.handicap = handicap;
		komi = 0;
	}

	@Override
	public void pass() {
		playMove(Move.pass(getTurnPlayer()));
	}

	@Override
	public void undo() {
		gameTree.stepBack();
	}

	@Override
	public StoneColour getTurnPlayer() {
		if ( gameTree.getNumMoves() == 0 )
			return handicap == 0 ? StoneColour.BLACK : StoneColour.WHITE;

		return gameTree.getLastMove().getPlayer().other();
	}

	@Override
	public void setKomi(double komi) {
		this.komi = komi;
	}

	@Override
	public GameTree getGameTree() {
		return gameTree;
	}

	@Override
	public SGFWriter getSGFWriter() {
		return new SimpleSGFWriter(gameTree.getSequence());
	}

	@Override
	public BoardScorer getScorer() {
		return new BoardScorer(getBoard(), komi);
	}

	@Override
	public Board getBoard() {
		return gameTree.getPosition();
	}

	@Override
	public boolean isLegalMove(Move move) {
		if ( move.getType() == Move.Type.PASS )
			return true; //Passing is never illegal

		boolean isLegal;

		isLegal = !gameTree.getPosition().isOccupied(move.getPosition());
		isLegal &= !gameTree.getPosition().isSuicide(move.getPosition(), move.getPlayer());

		if ( isLegal && gameTree.getNumMoves() > 2 ) {
			Board previous = gameTree.getLastPosition();
			Board future = gameTree.getPosition();
			future.playStone(move.getPosition(), move.getPlayer());

			isLegal = !previous.equals(future);
		}

		return isLegal;
	}

	@Override
	public void playMove(Move move) {
		gameTree.stepForward(move);
	}

	@Override
	public Collection<Coords> getStones(StoneColour colour) {
		return getBoard().getStones(colour);
	}
}
