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

public class LocalGameHandlerTest {

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
