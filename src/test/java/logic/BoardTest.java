/*
 * Copyright (C) 2017 Vincent Varkevisser
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
import util.Coords;

import static org.hamcrest.CoreMatchers.hasItems;
import static org.junit.Assert.assertThat;

public class BoardTest {

    @Test
    public void playMoves() {
        Board board = new Board();

        Coords coords = new Coords(4, 4);
        board.playBlackStone(coords);
        assertThat(board.getBlackStones(), hasItems(coords));

        coords = new Coords(5, 5);
        board.playWhiteStone(coords);
        assertThat(board.getWhiteStones(), hasItems(coords));
    }
}
