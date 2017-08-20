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

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static util.CoordProjector.fromBoardCoords;
import static util.Coords.getCoords;

public class CoordProjectorTest {

	@Test
	public void conversion() {
		CoordProjector proj = new CoordProjector();
		DrawCoords projection = fromBoardCoords(getCoords("D4"), 20);

		assertThat(projection, is(new DrawCoords(4.0, 4.0)));
	}
}
