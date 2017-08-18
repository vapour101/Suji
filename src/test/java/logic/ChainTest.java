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

import java.util.Set;

import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static util.Coords.getCoords;

public class ChainTest {

    @Test
    public void contains() {
        Chain chain = new Chain(getCoords(3, 4));

        assertThat(chain.contains(getCoords(3, 4)), is(true));
    }

    @Test
    public void adjacency() {
        Chain chain = new Chain(getCoords(4, 4));
        Chain other = new Chain(getCoords(3, 4));

        assertThat(chain.isAdjacentTo(getCoords(3, 4)), is(true));

        assertThat(chain.isAdjacentTo(getCoords(3, 3)), is(false));

        assertThat(chain.isAdjacentTo(other), is(true));
    }

    @Test
    public void liberties() {
        Chain chain = new Chain(getCoords(4, 4));

        Set<Coords> liberties = chain.getLiberties();

        assertThat(liberties.size(), is(4));
        assertThat(liberties, hasItems(getCoords(3, 4),
                getCoords(4, 3),
                getCoords(5, 4),
                getCoords(4, 5)));

        chain = new Chain(getCoords(1, 1));
        liberties = chain.getLiberties();

        assertThat(liberties.size(), is(2));
    }

    @Test
    public void size() {
        Chain chain = new Chain(getCoords(1, 1));

        assertThat(chain.size(), is(1));
    }

    @Test
    public void merge() {
        Chain main = new Chain(getCoords(4, 4));
        Chain other = new Chain(getCoords(3, 4));

        main.mergeChain(other);

        assertThat(main.size(), is(2));
        assertThat(other.size(), is(0));

        assertThat(main.contains(getCoords(4, 4)), is(true));
        assertThat(main.contains(getCoords(3, 4)), is(true));

        assertThat(main.getLiberties().size(), is(6));
        assertThat(other.getLiberties().size(), is(0));
    }
}
