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

package util;

import logic.board.Board;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;
import static util.Coords.getCoords;
import static util.Move.pass;
import static util.Move.play;
import static util.StoneColour.BLACK;
import static util.StoneColour.WHITE;

public class MoveTest {

	@Test
	public void equals() {
		Move movePlay = play(getCoords("A1"), BLACK);
		Move movePass = pass(BLACK);

		Move compare1 = play(getCoords("A1"), BLACK);
		Move compare2 = play(getCoords("A1"), WHITE);
		Move compare3 = play(getCoords("A2"), BLACK);
		Move compare4 = play(getCoords("A2"), WHITE);
		Move compare5 = pass(WHITE);
		Move compare6 = pass(BLACK);
		Board board = new Board();

		assertThat(movePlay, is(movePlay));
		assertThat(movePlay, is(compare1));
		assertThat(movePlay, is(not(compare2)));
		assertThat(movePlay, is(not(compare3)));
		assertThat(movePlay, is(not(compare4)));
		assertThat(movePlay, is(not(board)));

		assertThat(movePass, is(not(movePlay)));
		assertThat(movePass, is(not(compare5)));
		assertThat(movePass, is(compare6));
	}
}