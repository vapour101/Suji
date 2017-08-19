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

import org.junit.Test;

import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static util.Coords.getCoords;

public class HandicapHelperTest {
    public static final Coords TOP_LEFT = getCoords("D16");
    public static final Coords TOP_RIGHT = getCoords("Q16");
    public static final Coords BOTTOM_LEFT = getCoords("D4");
    public static final Coords BOTTOM_RIGHT = getCoords("Q4");

    public static final Coords TOP = getCoords("K16");
    public static final Coords BOTTOM = getCoords("K4");
    public static final Coords LEFT = getCoords("D10");
    public static final Coords RIGHT = getCoords("Q10");

    public static final Coords TENGEN = getCoords("K10");

    @Test
    public void constructor() {
        HandicapHelper helper = new HandicapHelper();
    }

    @Test
    public void oneStoneHandicap() {
        assertThat(HandicapHelper.getHandicapStones(1).isEmpty(), is(true));
    }

    @Test
    public void twoStoneHandicap() {
        assertThat(HandicapHelper.getHandicapStones(2), hasItems(
                TOP_RIGHT,
                BOTTOM_LEFT
        ));
    }

    @Test
    public void threeStoneHandicap() {
        assertThat(HandicapHelper.getHandicapStones(3), hasItems(
                TOP_RIGHT,
                BOTTOM_LEFT,
                BOTTOM_RIGHT
        ));
    }

    @Test
    public void fourStoneHandicap() {
        assertThat(HandicapHelper.getHandicapStones(4), hasItems(
                TOP_RIGHT,
                TOP_LEFT,
                BOTTOM_LEFT,
                BOTTOM_RIGHT
        ));
    }

    @Test
    public void fiveStoneHandicap() {
        assertThat(HandicapHelper.getHandicapStones(5), hasItems(
                TOP_RIGHT,
                TOP_LEFT,
                BOTTOM_LEFT,
                BOTTOM_RIGHT,
                TENGEN
        ));
    }

    @Test
    public void sixStoneHandicap() {
        assertThat(HandicapHelper.getHandicapStones(6), hasItems(
                TOP_RIGHT,
                TOP_LEFT,
                BOTTOM_LEFT,
                BOTTOM_RIGHT,
                LEFT,
                RIGHT
        ));
    }

    @Test
    public void sevenStoneHandicap() {
        assertThat(HandicapHelper.getHandicapStones(7), hasItems(
                TOP_RIGHT,
                TOP_LEFT,
                BOTTOM_LEFT,
                BOTTOM_RIGHT,
                LEFT,
                RIGHT,
                TENGEN
        ));
    }

    @Test
    public void eightStoneHandicap() {
        assertThat(HandicapHelper.getHandicapStones(8), hasItems(
                TOP_RIGHT,
                TOP_LEFT,
                BOTTOM_LEFT,
                BOTTOM_RIGHT,
                LEFT,
                RIGHT,
                TOP,
                BOTTOM
        ));
    }

    @Test
    public void nineStoneHandicap() {
        assertThat(HandicapHelper.getHandicapStones(9), hasItems(
                TOP_RIGHT,
                TOP_LEFT,
                BOTTOM_LEFT,
                BOTTOM_RIGHT,
                LEFT,
                RIGHT,
                TOP,
                BOTTOM,
                TENGEN
        ));
    }

    @Test
    public void invalidHandicap() {
        assertThat(HandicapHelper.getHandicapStones(0).isEmpty(), is(true));
        assertThat(HandicapHelper.getHandicapStones(10).isEmpty(), is(true));
        assertThat(HandicapHelper.getHandicapStones(-5).isEmpty(), is(true));
        assertThat(HandicapHelper.getHandicapStones(Integer.MAX_VALUE).isEmpty(), is(true));
        assertThat(HandicapHelper.getHandicapStones(Integer.MIN_VALUE).isEmpty(), is(true));
    }
}
