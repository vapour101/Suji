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

package util;

import javafx.util.Pair;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertThat;
import static util.Coords.getCoords;

public class CoordsTest {

    @Test
    public void equalTo() {
        Coords c1 = new Coords(4, 3);
        Coords c2 = new Coords(4, 3);
        Pair<Integer, Integer> notCoords = new Pair<>(4, 3);

        assertThat(c1, is(c2));
        assertThat(c2, is(c1));
        assertThat(c1, is(c1));
        assertThat(c1, not(notCoords));
    }

    @Test
    public void coordsGetter() {
        Coords coords = new Coords(4, 3);

        assertThat(getCoords(4, 3), is(coords));
    }

    @Test
    public void convertToString() {
        assertThat(getCoords(3, 4).toString(), is("(3, 4)"));
    }

    @Test
    public void neighbours() {
        Coords center = getCoords(10, 10);
        Coords topLeft = getCoords(1, 1);
        Coords bottomRight = getCoords(19, 19);

        assertThat(center.getNeighbours().size(), is(4));
        assertThat(center.getNeighbours(), hasItems(
                getCoords(10, 9),
                getCoords(9, 10),
                getCoords(10, 11),
                getCoords(11, 10)));

        assertThat(topLeft.getNeighbours().size(), is(2));
        assertThat(topLeft.getNeighbours(), hasItems(
                getCoords(1, 2),
                getCoords(2, 1)));

        assertThat(bottomRight.getNeighbours().size(), is(2));
        assertThat(bottomRight.getNeighbours(), hasItems(
                getCoords(19, 18),
                getCoords(18, 19)));
    }
}
