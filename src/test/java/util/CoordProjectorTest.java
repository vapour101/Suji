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

import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static util.Coords.getCoords;

public class CoordProjectorTest {

	@Test
	public void toRealCoords() {
		CoordProjector proj = new CoordProjector(19, new DrawCoords(3.4, 5.6));
		DrawCoords projection = proj.fromBoardCoords(getCoords("D4"));

		assertThat(projection, is(new DrawCoords(6.9, 9.1)));

		proj = new CoordProjector(19);
		projection = proj.fromBoardCoords(getCoords("D4"));

		assertThat(projection, is(new DrawCoords(3.5, 3.5)));
	}

	@Test
	public void toBoardCoords() {
		CoordProjector proj = new CoordProjector(20, new DrawCoords(3.4, 5.6));
		Coords projection = proj.nearestCoords(new DrawCoords(7.3, 9.5));

		assertThat(projection, is(getCoords("D4")));

		projection = proj.nearestCoords(new DrawCoords(2, 2));
		assertThat(projection, is(getCoords("A1")));

		projection = proj.nearestCoords(new DrawCoords(40, 40));
		assertThat(projection, is(getCoords("T19")));
	}

	@Test
	public void boundsChecking() {
		CoordProjector projector = new CoordProjector(20, new DrawCoords(3.4, 5.6));
		DrawCoords check1 = new DrawCoords(10, 10);
		DrawCoords check2 = new DrawCoords(3, 10);
		DrawCoords check3 = new DrawCoords(5, 0);
		DrawCoords check4 = new DrawCoords(27, 10);
		DrawCoords check5 = new DrawCoords(8, 30);

		assertThat(projector.isWithinBounds(check1), is(true));
		assertThat(projector.isWithinBounds(check2), is(false));
		assertThat(projector.isWithinBounds(check3), is(false));
		assertThat(projector.isWithinBounds(check4), is(false));
		assertThat(projector.isWithinBounds(check5), is(false));
	}
}
