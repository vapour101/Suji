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
import util.StoneColour;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static util.Coords.getCoords;

public class GameTreeTest {

	@Test
	public void playMove() {
		GameTree tree = new GameTree();
		Board board = new Board();

		tree.playMove(getCoords("D4"), StoneColour.BLACK);
		tree.playMove(getCoords("C3"), StoneColour.WHITE);

		board.playStone(getCoords("D4"), StoneColour.BLACK);
		board.playStone(getCoords("C3"), StoneColour.WHITE);

		assertThat(tree.getPosition(), is(board));
	}
}
