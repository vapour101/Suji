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

package logic.board;

import org.junit.Test;
import util.Coords;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import static util.Coords.getCoords;
import static util.Move.pass;
import static util.Move.play;
import static util.StoneColour.BLACK;
import static util.StoneColour.WHITE;

public class BoardTest {

	@Test
	public void equals() {
		Board board1 = new Board();
		Board board2 = new Board();

		board1.playStone(play(getCoords("D4"), BLACK));
		board1.playStone(play(getCoords("C4"), BLACK));
		board1.playStone(play(getCoords("M17"), WHITE));
		board1.playStone(play(getCoords("R4"), WHITE));
		board1.playStone(play(getCoords("B14"), BLACK));

		board2.playStone(play(getCoords("R4"), WHITE));
		board2.playStone(play(getCoords("B14"), BLACK));
		board2.playStone(play(getCoords("C4"), BLACK));
		board2.playStone(play(getCoords("D4"), BLACK));
		board2.playStone(play(getCoords("M17"), WHITE));

		assertThat(board1, is(board2));

		assertThat(board1, is(board1));
		assertThat(board1, is(not(getCoords("D4"))));
	}

	@Test
	public void playMoves() {
		Board board = new Board();

		Coords coords = getCoords("D4");
		board.playStone(play(coords, BLACK));
		assertThat(board.getStones(BLACK), hasItems(coords));

		coords = getCoords("E5");
		board.playStone(play(coords, WHITE));
		assertThat(board.getStones(WHITE), hasItems(coords));
	}

	@Test
	public void findingChains() {
		Board board = new Board();

		String[] stones = {"D3", "D4", "D5", "E6"};

		for (String stone : stones)
			board.playStone(play(getCoords(stone), BLACK));

		stones = new String[]{"F5", "F6"};
		for (String stone : stones)
			board.playStone(play(getCoords(stone), WHITE));

		assertThat(board.getChainAtCoords(getCoords("D4")).getStones(),
				   hasItems(getCoords("D3"), getCoords("D4"), getCoords("D5")));

		assertThat(board.getChainAtCoords(getCoords("D3")).getStones(),
				   hasItems(getCoords("D3"), getCoords("D4"), getCoords("D5")));

		assertThat(board.getChainAtCoords(getCoords("E6")).getStones(), hasItem(getCoords("E6")));

		assertThat(board.getChainAtCoords(getCoords("D4")).size(), is(3));
		assertThat(board.getChainAtCoords(getCoords("D5")).size(), is(3));
		assertThat(board.getChainAtCoords(getCoords("E6")).size(), is(1));
		assertThat(board.getChainAtCoords(getCoords("F5")).size(), is(2));

		assertThat(board.getChainAtCoords(getCoords("F5")).getStones(), hasItems(getCoords("F5"), getCoords("F6")));

		assertThat(board.getChainAtCoords(getCoords("A1")), nullValue());
	}

	@Test
	public void blockOccupiedSameColourBlack() {
		Board board = new Board();

		board.playStone(play(getCoords("C4"), BLACK));

		try {
			board.playStone(play(getCoords("C4"), BLACK));
			fail("Board did not throw exception when playing on top of an existing stone with a stone of the same colour.");
		}
		catch (Exception e) {
			assertThat(e, instanceOf(IllegalArgumentException.class));
		}
	}

	@Test
	public void blockOccupiedDifferentColourBlack() {
		Board board = new Board();

		board.playStone(play(getCoords("C4"), BLACK));

		try {
			board.playStone(play(getCoords("C4"), WHITE));
			fail("Board did not throw exception when playing on top of an existing stone with a stone of a different colour.");
		}
		catch (Exception e) {
			assertThat(e, instanceOf(IllegalArgumentException.class));
		}
	}

	@Test
	public void blockOccupiedSameColourWhite() {
		Board board = new Board();

		board.playStone(play(getCoords("C4"), WHITE));

		try {
			board.playStone(play(getCoords("C4"), WHITE));
			fail("Board did not throw exception when playing on top of an existing stone with a stone of the same colour.");
		}
		catch (Exception e) {
			assertThat(e, instanceOf(IllegalArgumentException.class));
		}
	}

	@Test
	public void blockOccupiedDifferentColourWhite() {
		Board board = new Board();

		board.playStone(play(getCoords("C4"), WHITE));

		try {
			board.playStone(play(getCoords("C4"), BLACK));
			fail("Board did not throw exception when playing on top of an existing stone with a stone of a different colour.");
		}
		catch (Exception e) {
			assertThat(e, instanceOf(IllegalArgumentException.class));
		}
	}

	@Test
	public void simpleCapturing() {
		Board board = new Board();

		assertThat(board.getCaptures(BLACK), is(0));
		assertThat(board.getCaptures(WHITE), is(0));

		board.playStone(play(getCoords("D4"), BLACK));
		board.playStone(play(getCoords("D3"), WHITE));
		board.playStone(play(getCoords("D5"), WHITE));
		board.playStone(play(getCoords("C4"), WHITE));

		assertThat(board.getStones(BLACK).size(), is(1));
		assertThat(board.getStones(WHITE).size(), is(3));
		assertThat(board.getCaptures(BLACK), is(0));
		assertThat(board.getCaptures(WHITE), is(0));

		board.playStone(play(getCoords("E4"), WHITE));

		assertThat(board.getCaptures(WHITE), is(1));
		assertThat(board.getStones(BLACK).size(), is(0));


		board = new Board();

		assertThat(board.getCaptures(BLACK), is(0));
		assertThat(board.getCaptures(WHITE), is(0));

		board.playStone(play(getCoords("D4"), WHITE));
		board.playStone(play(getCoords("D3"), BLACK));
		board.playStone(play(getCoords("D5"), BLACK));
		board.playStone(play(getCoords("C4"), BLACK));

		assertThat(board.getStones(BLACK).size(), is(3));
		assertThat(board.getStones(WHITE).size(), is(1));
		assertThat(board.getCaptures(BLACK), is(0));
		assertThat(board.getCaptures(WHITE), is(0));

		board.playStone(play(getCoords("E4"), BLACK));

		assertThat(board.getCaptures(BLACK), is(1));
		assertThat(board.getStones(WHITE).size(), is(0));
	}

	@Test
	public void passing() {
		Board board = new Board();

		assertThat(board.isSuicide(pass(BLACK)), is(false));

		board.playStone(pass(BLACK));

		assertThat(board, is(new Board()));
	}
}
