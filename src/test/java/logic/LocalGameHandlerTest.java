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

import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static util.Coords.getCoords;

public class LocalGameHandlerTest {

	private static final String[] koBoard = {"C4", "D4", "D3", "E3", "D5", "E5", "K4", "F4", "E4"};

	@Test
	public void gameTracking() {
		LocalGameHandler handler = new LocalGameHandler();

		handler.playStone(getCoords("D4"), StoneColour.BLACK);
		handler.playStone(getCoords("E5"), StoneColour.WHITE);

		assertThat(handler.getStones(StoneColour.BLACK), hasItems(getCoords("D4")));
		assertThat(handler.getStones(StoneColour.WHITE), hasItems(getCoords("E5")));
	}

	@Test
	public void koIsIllegal() {
		LocalGameHandler handler = buildTestHandler(koBoard);

		assertThat(handler.isLegalMove(getCoords("D4"), StoneColour.WHITE), is(false));
		assertThat(handler.isLegalMove(getCoords("D4"), StoneColour.BLACK), is(true));
	}

	private LocalGameHandler buildTestHandler(String[] sequence) {
		LocalGameHandler handler = new LocalGameHandler();

		for (int i = 0; i < sequence.length; i++) {
			if ( i % 2 == 0 )
				handler.playStone(getCoords(sequence[i]), StoneColour.BLACK);
			else
				handler.playStone(getCoords(sequence[i]), StoneColour.WHITE);
		}

		return handler;
	}

	@Test
	public void playingOnOccupiedSpaceIsIllegal() {
		LocalGameHandler handler = new LocalGameHandler();

		handler.playStone(getCoords("D4"), StoneColour.BLACK);

		assertThat(handler.isLegalMove(getCoords("D4"), StoneColour.BLACK), is(false));
		assertThat(handler.isLegalMove(getCoords("D4"), StoneColour.WHITE), is(false));

		assertThat(handler.isLegalMove(getCoords("D3"), StoneColour.BLACK), is(true));
		assertThat(handler.isLegalMove(getCoords("D3"), StoneColour.WHITE), is(true));

		handler = new LocalGameHandler();
		handler.playStone(getCoords("D4"), StoneColour.WHITE);

		assertThat(handler.isLegalMove(getCoords("D4"), StoneColour.BLACK), is(false));
		assertThat(handler.isLegalMove(getCoords("D4"), StoneColour.WHITE), is(false));

		assertThat(handler.isLegalMove(getCoords("D3"), StoneColour.BLACK), is(true));
		assertThat(handler.isLegalMove(getCoords("D3"), StoneColour.WHITE), is(true));
	}

	@Test
	public void suicideIsIllegal() {
		LocalGameHandler handler = new LocalGameHandler();

		handler.playStone(getCoords("D5"), StoneColour.BLACK);
		handler.playStone(getCoords("D3"), StoneColour.BLACK);
		handler.playStone(getCoords("E4"), StoneColour.BLACK);
		handler.playStone(getCoords("C4"), StoneColour.BLACK);

		assertThat(handler.isLegalMove(getCoords("D4"), StoneColour.WHITE), is(false));

		handler = new LocalGameHandler();
		handler.playStone(getCoords("D5"), StoneColour.WHITE);
		handler.playStone(getCoords("D3"), StoneColour.WHITE);
		handler.playStone(getCoords("E4"), StoneColour.WHITE);
		handler.playStone(getCoords("C4"), StoneColour.WHITE);

		assertThat(handler.isLegalMove(getCoords("D4"), StoneColour.BLACK), is(false));
	}
}
