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

package logic.gametree;

import logic.board.Board;
import org.junit.Test;
import util.Move;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static util.Coords.getCoords;
import static util.Move.pass;
import static util.Move.play;
import static util.StoneColour.BLACK;
import static util.StoneColour.WHITE;

public class SimpleGameTreeTest {

	@Test
	public void playMove() {
		SimpleGameTree tree = new SimpleGameTree();
		Board board = new Board();

		tree.stepForward(play(getCoords("D4"), BLACK));
		tree.stepForward(play(getCoords("C3"), WHITE));

		board.playStone(play(getCoords("D4"), BLACK));
		board.playStone(play(getCoords("C3"), WHITE));

		assertThat(tree.getPosition(), is(board));
	}

	@Test
	public void undoMove() {
		SimpleGameTree tree = new SimpleGameTree();
		Board board = new Board();

		tree.stepForward(play(getCoords("D4"), BLACK));
		tree.stepForward(play(getCoords("C4"), WHITE));
		tree.stepForward(play(getCoords("M17"), BLACK));
		tree.stepForward(play(getCoords("R4"), WHITE));
		tree.stepForward(play(getCoords("B14"), BLACK));

		tree.stepBack();

		board.playStone(play(getCoords("D4"), BLACK));
		board.playStone(play(getCoords("M17"), BLACK));
		board.playStone(play(getCoords("C4"), WHITE));
		board.playStone(play(getCoords("R4"), WHITE));

		assertThat(tree.getPosition(), is(board));
	}

	@Test
	public void passing() {
		SimpleGameTree tree = new SimpleGameTree();

		tree.stepForward(play(getCoords("D4"), BLACK));
		tree.stepForward(play(getCoords("C4"), WHITE));
		tree.stepForward(play(getCoords("M17"), BLACK));
		tree.stepForward(play(getCoords("R4"), WHITE));
		tree.stepForward(play(getCoords("B14"), BLACK));

		Board board = tree.getPosition();

		tree.stepForward(pass(WHITE));

		assertThat(tree.getPosition(), is(board));
	}

	@Test
	public void isRoot() {
		GameTree tree = new SimpleGameTree();

		tree.stepForward(0);
		assertThat(tree.isRoot(), is(true));

		tree.stepForward(pass(BLACK));
		assertThat(tree.isRoot(), is(true));

		tree.stepForward(pass(WHITE));
		assertThat(tree.isRoot(), is(false));
	}

	@Test
	public void getLastMove() {
		Move move = play(getCoords("A1"), BLACK);
		GameTree tree = new SimpleGameTree();

		tree.stepForward(move);

		assertThat(tree.getLastMove(), is(move));

		assertThat(tree.getNumMoves(), is(1));

		assertThat(tree.getLastPosition(), is(new Board()));
	}
}
