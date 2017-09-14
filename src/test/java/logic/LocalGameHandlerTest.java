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

import logic.board.Board;
import org.junit.Test;
import util.Move;

import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static util.Coords.getCoords;
import static util.Move.play;
import static util.StoneColour.BLACK;
import static util.StoneColour.WHITE;

public class LocalGameHandlerTest {

	private static final String[] koBoard = {"C4", "D4", "D3", "E3", "D5", "E5", "K4", "F4", "E4"};

	@Test
	public void gameTracking() {
		LocalGameHandler handler = new LocalGameHandler();

		handler.playMove(play(getCoords("D4"), BLACK));
		handler.playMove(play(getCoords("E5"), WHITE));

		assertThat(handler.getStones(BLACK), hasItems(getCoords("D4")));
		assertThat(handler.getStones(WHITE), hasItems(getCoords("E5")));
	}

	@Test
	public void koIsIllegal() {
		LocalGameHandler handler = buildTestHandler(koBoard);

		assertThat(handler.isLegalMove(play(getCoords("D4"), WHITE)), is(false));
		assertThat(handler.isLegalMove(play(getCoords("D4"), BLACK)), is(true));
	}

	private LocalGameHandler buildTestHandler(String[] sequence) {
		LocalGameHandler handler = new LocalGameHandler();

		for (int i = 0; i < sequence.length; i++) {
			if ( i % 2 == 0 )
				handler.playMove(play(getCoords(sequence[i]), BLACK));
			else
				handler.playMove(play(getCoords(sequence[i]), WHITE));
		}

		return handler;
	}

	@Test
	public void playingOnOccupiedSpaceIsIllegal() {
		LocalGameHandler handler = new LocalGameHandler();

		handler.playMove(play(getCoords("D4"), BLACK));

		assertThat(handler.isLegalMove(play(getCoords("D4"), BLACK)), is(false));
		assertThat(handler.isLegalMove(play(getCoords("D4"), WHITE)), is(false));

		assertThat(handler.isLegalMove(play(getCoords("D3"), BLACK)), is(true));
		assertThat(handler.isLegalMove(play(getCoords("D3"), WHITE)), is(true));

		handler = new LocalGameHandler();
		handler.playMove(play(getCoords("D4"), WHITE));

		assertThat(handler.isLegalMove(play(getCoords("D4"), BLACK)), is(false));
		assertThat(handler.isLegalMove(play(getCoords("D4"), WHITE)), is(false));

		assertThat(handler.isLegalMove(play(getCoords("D3"), BLACK)), is(true));
		assertThat(handler.isLegalMove(play(getCoords("D3"), WHITE)), is(true));
	}

	@Test
	public void suicideIsIllegal() {
		LocalGameHandler handler = new LocalGameHandler();

		handler.playMove(play(getCoords("D5"), BLACK));
		handler.playMove(play(getCoords("D3"), BLACK));
		handler.playMove(play(getCoords("E4"), BLACK));
		handler.playMove(play(getCoords("C4"), BLACK));

		assertThat(handler.isLegalMove(play(getCoords("D4"), WHITE)), is(false));

		handler = new LocalGameHandler();
		handler.playMove(play(getCoords("D5"), WHITE));
		handler.playMove(play(getCoords("D3"), WHITE));
		handler.playMove(play(getCoords("E4"), WHITE));
		handler.playMove(play(getCoords("C4"), WHITE));

		assertThat(handler.isLegalMove(play(getCoords("D4"), BLACK)), is(false));
	}

	@Test
	public void passing() {
		LocalGameHandler handler = new LocalGameHandler();

		handler.playMove(play(getCoords("D4"), BLACK));
		handler.playMove(play(getCoords("E5"), WHITE));

		assertThat(handler.getTurnPlayer(), is(BLACK));

		handler.pass();

		assertThat(handler.getTurnPlayer(), is(WHITE));

		assertThat(handler.isLegalMove(Move.pass(WHITE)), is(true));
	}

	@Test
	public void undo() {
		LocalGameHandler handler = new LocalGameHandler();

		handler.playMove(play(getCoords("D4"), BLACK));
		handler.playMove(play(getCoords("E5"), WHITE));
		handler.playMove(play(getCoords("C4"), BLACK));
		handler.playMove(play(getCoords("F8"), WHITE));
		handler.playMove(play(getCoords("G9"), BLACK));
		handler.playMove(play(getCoords("M10"), WHITE));

		Board board = handler.getBoard();

		handler.playMove(play(getCoords("A1"), BLACK));
		handler.undo();

		assertThat(handler.getBoard(), is(board));
	}

	@Test
	public void turnPlayer() {
		LocalGameHandler handler = new LocalGameHandler();

		assertThat(handler.getTurnPlayer(), is(BLACK));

		handler = new LocalGameHandler(2);
		assertThat(handler.getTurnPlayer(), is(WHITE));
	}
}
