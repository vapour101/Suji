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

import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static util.Coords.getCoords;

public class ChainSetTest {

    @Test
    public void trackStones() {
        ChainSet chains = new ChainSet();

        chains.add(getCoords(4, 4));

        assertThat(chains.contains(getCoords(4, 4)), is(true));
        assertThat(chains.contains(getCoords(3, 3)), is(false));
    }

    @Test
    public void autoMergeChains() {
        ChainSet chains = new ChainSet();

        chains.add(getCoords(4, 4));

        chains.add(getCoords(4, 3));

        assertThat(chains.contains(getCoords(4, 4)), is(true));
        assertThat(chains.contains(getCoords(4, 3)), is(true));
    }

    @Test
    public void nonContiguousChains() {
        ChainSet chains = new ChainSet();

        chains.add(getCoords(4, 4));

        chains.add(getCoords(3, 3));

        assertThat(chains.contains(getCoords(4, 4)), is(true));
        assertThat(chains.contains(getCoords(3, 3)), is(true));
    }

    @Test
    public void getting() {
        ChainSet chains = new ChainSet();

        chains.add(getCoords(3, 3));
        chains.add(getCoords(4, 4));

        assertThat(chains.getStones(), hasItems(getCoords(3, 3), getCoords(4, 4)));
        assertThat(chains.getStones().size(), is(2));
    }

    @Test
    public void checkingForCaptures() {
        ChainSet black = new ChainSet();
        ChainSet white = new ChainSet();

        black.add(getCoords(4, 4));
        white.add(getCoords(4, 3));
        white.add(getCoords(4, 5));

        assertThat(black.chainIsCaptured(getCoords(3, 4), white), is(false));
        assertThat(black.chainIsCaptured(getCoords(3, 3), white), is(false));
        assertThat(black.chainIsCaptured(getCoords(4, 5), white), is(false));

        white.add(getCoords(3, 4));

        assertThat(black.chainIsCaptured(getCoords(5, 4), white), is(true));
        assertThat(black.chainIsCaptured(getCoords(4, 5), white), is(false));
    }

    @Test
    public void checkingForSuicide() {
        ChainSet black = new ChainSet();
        ChainSet white = new ChainSet();

        white.add(getCoords(3, 4));
        white.add(getCoords(5, 4));
        white.add(getCoords(4, 3));

        assertThat(black.isSuicide(getCoords(4, 4), white), is(false));

        white.add(getCoords(4, 5));

        assertThat(black.isSuicide(getCoords(4, 4), white), is(true));

        black.add(getCoords(3, 5));
        black.add(getCoords(5, 5));
        black.add(getCoords(4, 6));

        assertThat(black.isSuicide(getCoords(4, 4), white), is(false));
    }
}
