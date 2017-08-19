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

        Coords coords = getCoords("D4");
        board.playBlackStone(coords);
        assertThat(board.getBlackStones(), hasItems(coords));

        coords = getCoords("E5");
        board.playWhiteStone(coords);
        assertThat(board.getWhiteStones(), hasItems(coords));
    }

    @Test
    public void findingChains() {
        Board board = new Board();

        String[] stones = {"D3", "D4", "D5", "E6"};

        for (String stone : stones)
            board.playBlackStone(getCoords(stone));

        stones = new String[]{"F5", "F6"};
        for (String stone : stones)
            board.playWhiteStone(getCoords(stone));

        assertThat(board.getChainAtCoords(getCoords("D4")).getStones(), hasItems(
                getCoords("D3"),
                getCoords("D4"),
                getCoords("D5")
        ));

        assertThat(board.getChainAtCoords(getCoords("D3")).getStones(), hasItems(
                getCoords("D3"),
                getCoords("D4"),
                getCoords("D5")
        ));

        assertThat(board.getChainAtCoords(getCoords("E6")).getStones(), hasItem(getCoords("E6")));

        assertThat(board.getChainAtCoords(getCoords("D4")).size(), is(3));
        assertThat(board.getChainAtCoords(getCoords("D5")).size(), is(3));
        assertThat(board.getChainAtCoords(getCoords("E6")).size(), is(1));
        assertThat(board.getChainAtCoords(getCoords("F5")).size(), is(2));

        assertThat(board.getChainAtCoords(getCoords("F5")).getStones(), hasItems(
                getCoords("F5"),
                getCoords("F6")
        ));

        assertThat(board.getChainAtCoords(getCoords("A1")), nullValue());

    }

    @Test
    public void blockOccupiedSameColourBlack() {
        Board board = new Board();

        board.playBlackStone(getCoords("C4"));

        try {
            board.playBlackStone(getCoords("C4"));
            fail("Board did not throw exception when playing on top of an existing stone with a stone of the same colour.");
        } catch (Exception e) {
            assertThat(e, instanceOf(IllegalArgumentException.class));
        }
    }

    @Test
    public void blockOccupiedDifferentColourBlack() {
        Board board = new Board();

        board.playBlackStone(getCoords("C4"));

        try {
            board.playWhiteStone(getCoords("C4"));
            fail("Board did not throw exception when playing on top of an existing stone with a stone of a different colour.");
        } catch (Exception e) {
            assertThat(e, instanceOf(IllegalArgumentException.class));
        }
    }

    @Test
    public void blockOccupiedSameColourWhite() {
        Board board = new Board();

        board.playWhiteStone(getCoords("C4"));

        try {
            board.playWhiteStone(getCoords("C4"));
            fail("Board did not throw exception when playing on top of an existing stone with a stone of the same colour.");
        } catch (Exception e) {
            assertThat(e, instanceOf(IllegalArgumentException.class));
        }
    }

    @Test
    public void blockOccupiedDifferentColourWhite() {
        Board board = new Board();

        board.playWhiteStone(getCoords("C4"));

        try {
            board.playBlackStone(getCoords("C4"));
            fail("Board did not throw exception when playing on top of an existing stone with a stone of a different colour.");
        } catch (Exception e) {
            assertThat(e, instanceOf(IllegalArgumentException.class));
        }
    }

    @Test
    public void playingOnOccupiedSpaceIsIllegal() {
        Board board = new Board();

        board.playBlackStone(getCoords("D4"));

        assertThat(board.isLegalBlackMove(getCoords("D4")), is(false));
        assertThat(board.isLegalWhiteMove(getCoords("D4")), is(false));

        assertThat(board.isLegalBlackMove(getCoords("D3")), is(true));
        assertThat(board.isLegalWhiteMove(getCoords("D3")), is(true));

        board = new Board();
        board.playWhiteStone(getCoords("D4"));

        assertThat(board.isLegalBlackMove(getCoords("D4")), is(false));
        assertThat(board.isLegalWhiteMove(getCoords("D4")), is(false));

        assertThat(board.isLegalBlackMove(getCoords("D3")), is(true));
        assertThat(board.isLegalWhiteMove(getCoords("D3")), is(true));
    }

    @Test
    public void suicideIsIllegal() {
        Board board = new Board();

        board.playBlackStone(getCoords("D5"));
        board.playBlackStone(getCoords("D3"));
        board.playBlackStone(getCoords("E4"));
        board.playBlackStone(getCoords("C4"));

        assertThat(board.isLegalWhiteMove(getCoords("D4")), is(false));

        board = new Board();
        board.playWhiteStone(getCoords("D5"));
        board.playWhiteStone(getCoords("D3"));
        board.playWhiteStone(getCoords("E4"));
        board.playWhiteStone(getCoords("C4"));

        assertThat(board.isLegalBlackMove(getCoords("D4")), is(false));
    }

    @Test
    public void simpleCapturing() {
        Board board = new Board();

        assertThat(board.getBlackCaptures(), is(0));
        assertThat(board.getWhiteCaptures(), is(0));

        board.playBlackStone(getCoords("D4"));
        board.playWhiteStone(getCoords("D3"));
        board.playWhiteStone(getCoords("D5"));
        board.playWhiteStone(getCoords("C4"));

        assertThat(board.getBlackStones().size(), is(1));
        assertThat(board.getWhiteStones().size(), is(3));
        assertThat(board.getBlackCaptures(), is(0));
        assertThat(board.getWhiteCaptures(), is(0));

        board.playWhiteStone(getCoords("E4"));

        assertThat(board.getWhiteCaptures(), is(1));
        assertThat(board.getBlackStones().size(), is(0));


        board = new Board();

        assertThat(board.getBlackCaptures(), is(0));
        assertThat(board.getWhiteCaptures(), is(0));

        board.playWhiteStone(getCoords("D4"));
        board.playBlackStone(getCoords("D3"));
        board.playBlackStone(getCoords("D5"));
        board.playBlackStone(getCoords("C4"));

        assertThat(board.getBlackStones().size(), is(3));
        assertThat(board.getWhiteStones().size(), is(1));
        assertThat(board.getBlackCaptures(), is(0));
        assertThat(board.getWhiteCaptures(), is(0));

        board.playBlackStone(getCoords("E4"));

        assertThat(board.getBlackCaptures(), is(1));
        assertThat(board.getWhiteStones().size(), is(0));
    }
}
