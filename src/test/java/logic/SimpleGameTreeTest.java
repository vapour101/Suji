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

import org.junit.Test;

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

		tree.playMove(play(getCoords("D4"), BLACK));
		tree.playMove(play(getCoords("C3"), WHITE));

		board.playStone(getCoords("D4"), BLACK);
		board.playStone(getCoords("C3"), WHITE);

		assertThat(tree.getPosition(), is(board));
	}

	@Test
	public void undoMove() {
		SimpleGameTree tree = new SimpleGameTree();
		Board board = new Board();

		tree.playMove(play(getCoords("D4"), BLACK));
		tree.playMove(play(getCoords("C4"), WHITE));
		tree.playMove(play(getCoords("M17"), BLACK));
		tree.playMove(play(getCoords("R4"), WHITE));
		tree.playMove(play(getCoords("B14"), BLACK));

		tree.stepBack();

		board.playStone(getCoords("D4"), BLACK);
		board.playStone(getCoords("M17"), BLACK);
		board.playStone(getCoords("C4"), WHITE);
		board.playStone(getCoords("R4"), WHITE);

		assertThat(tree.getPosition(), is(board));
	}

	@Test
	public void passing() {
		SimpleGameTree tree = new SimpleGameTree();

		tree.playMove(play(getCoords("D4"), BLACK));
		tree.playMove(play(getCoords("C4"), WHITE));
		tree.playMove(play(getCoords("M17"), BLACK));
		tree.playMove(play(getCoords("R4"), WHITE));
		tree.playMove(play(getCoords("B14"), BLACK));

		Board board = tree.getPosition();

		tree.playMove(pass(WHITE));

		assertThat(tree.getPosition(), is(board));
	}
}
