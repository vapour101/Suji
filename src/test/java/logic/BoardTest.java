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

import org.junit.Assert;
import org.junit.Test;
import util.Coords;

import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

public class BoardTest {

    @Test
    public void playMoves() {
        Board board = new Board();

        Coords coords = Coords.get(4, 4);
        board.playBlackStone(coords);
        assertThat(board.getBlackStones(), hasItems(coords));

        coords = Coords.get(5,5);
        board.playWhiteStone(coords);
        assertThat(board.getWhiteStones(), hasItems(coords));
    }

    @Test
    public void blockOccupiedSameColour()
    {
        Board board = new Board();

        board.playBlackStone(Coords.get(4, 3));

        boolean noThrow = true;

        try {
            board.playBlackStone(Coords.get(4, 3));
        }
        catch (Exception e)
        {
            assertThat(e, instanceOf(IllegalArgumentException.class));
            noThrow = false;
        }

        if (noThrow)
            fail("Board did not throw exception when playing on top of an existing stone with a stone of the same colour.");
    }

    @Test
    public void blockOccupiedDifferentColour()
    {
        Board board = new Board();

        board.playBlackStone(Coords.get(4, 3));

        boolean noThrow  = true;

        try {
            board.playWhiteStone(Coords.get(4, 3));
        }
        catch (Exception e)
        {
            assertThat(e, instanceOf(IllegalArgumentException.class));
            noThrow = false;
        }

        if (noThrow)
            fail("Board did not throw exception when playing on top of an existing stone with a stone of a different colour.");
    }
}
