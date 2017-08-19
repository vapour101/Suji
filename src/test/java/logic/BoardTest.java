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

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import static util.Coords.getCoords;

public class BoardTest {

    @Test
    public void playMoves() {
        Board board = new Board();

        Coords coords = getCoords(4, 4);
        board.playBlackStone(coords);
        assertThat(board.getBlackStones(), hasItems(coords));

        coords = getCoords(5, 5);
        board.playWhiteStone(coords);
        assertThat(board.getWhiteStones(), hasItems(coords));
    }

    @Test
    public void blockOccupiedSameColourBlack() {
        Board board = new Board();

        board.playBlackStone(getCoords(4, 3));

        try {
            board.playBlackStone(getCoords(4, 3));
            fail("Board did not throw exception when playing on top of an existing stone with a stone of the same colour.");
        } catch (Exception e) {
            assertThat(e, instanceOf(IllegalArgumentException.class));
        }
    }

    @Test
    public void blockOccupiedDifferentColourBlack() {
        Board board = new Board();

        board.playBlackStone(getCoords(4, 3));

        try {
            board.playWhiteStone(getCoords(4, 3));
            fail("Board did not throw exception when playing on top of an existing stone with a stone of a different colour.");
        } catch (Exception e) {
            assertThat(e, instanceOf(IllegalArgumentException.class));
        }
    }

    @Test
    public void blockOccupiedSameColourWhite() {
        Board board = new Board();

        board.playWhiteStone(getCoords(4, 3));

        try {
            board.playWhiteStone(getCoords(4, 3));
            fail("Board did not throw exception when playing on top of an existing stone with a stone of the same colour.");
        } catch (Exception e) {
            assertThat(e, instanceOf(IllegalArgumentException.class));
        }
    }

    @Test
    public void blockOccupiedDifferentColourWhite() {
        Board board = new Board();

        board.playWhiteStone(getCoords(4, 3));

        try {
            board.playBlackStone(getCoords(4, 3));
            fail("Board did not throw exception when playing on top of an existing stone with a stone of a different colour.");
        } catch (Exception e) {
            assertThat(e, instanceOf(IllegalArgumentException.class));
        }
    }

    @Test
    public void playingOnOccupiedSpaceIsIllegal() {
        Board board = new Board();

        board.playBlackStone(getCoords(4, 4));

        assertThat(board.isLegalBlackMove(getCoords(4, 4)), is(false));
        assertThat(board.isLegalWhiteMove(getCoords(4, 4)), is(false));

        assertThat(board.isLegalBlackMove(getCoords(3, 4)), is(true));
        assertThat(board.isLegalWhiteMove(getCoords(3, 4)), is(true));

        board = new Board();
        board.playWhiteStone(getCoords(4, 4));

        assertThat(board.isLegalBlackMove(getCoords(4, 4)), is(false));
        assertThat(board.isLegalWhiteMove(getCoords(4, 4)), is(false));

        assertThat(board.isLegalBlackMove(getCoords(3, 4)), is(true));
        assertThat(board.isLegalWhiteMove(getCoords(3, 4)), is(true));
    }

    @Test
    public void suicideIsIllegal() {
        Board board = new Board();

        board.playBlackStone(getCoords(5, 4));
        board.playBlackStone(getCoords(3, 4));
        board.playBlackStone(getCoords(4, 5));
        board.playBlackStone(getCoords(4, 3));

        assertThat(board.isLegalWhiteMove(getCoords(4, 4)), is(false));

        board = new Board();
        board.playWhiteStone(getCoords(5, 4));
        board.playWhiteStone(getCoords(3, 4));
        board.playWhiteStone(getCoords(4, 5));
        board.playWhiteStone(getCoords(4, 3));

        assertThat(board.isLegalBlackMove(getCoords(4, 4)), is(false));
    }

    public void simpleCapturing() {
        Board board = new Board();

        assertThat(board.getBlackCaptures(), is(0));
        assertThat(board.getWhiteCaptures(), is(0));

        board.playBlackStone(getCoords(4, 4));
        board.playWhiteStone(getCoords(3, 4));
        board.playWhiteStone(getCoords(5, 4));
        board.playWhiteStone(getCoords(4, 3));

        assertThat(board.getBlackStones().size(), is(1));
        assertThat(board.getWhiteStones().size(), is(3));
        assertThat(board.getBlackCaptures(), is(0));
        assertThat(board.getWhiteCaptures(), is(0));

        board.playWhiteStone(getCoords(4, 5));

        assertThat(board.getWhiteCaptures(), is(1));
        assertThat(board.getBlackStones().size(), is(0));
    }
}
