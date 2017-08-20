/*
 * Copyright (c) 2017 Vincent Varkevisser
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

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertThat;
import static util.DrawCoords.sum;

public class DrawCoordsTest {

	@Test
	public void coordsTest() {
		DrawCoords coords = new DrawCoords(0.5, 0.7);
		DrawCoords offset = new DrawCoords(0.5, 0.3);
		DrawCoords result = new DrawCoords(0, 0);

		assertThat(coords, is(coords));

		result.setX(1);
		result.setY(1);

		assertThat(result, is(new DrawCoords(1.0, 1.0)));
		assertThat(sum(coords, offset), is(result));

		coords.applyOffset(offset);

		assertThat(coords, is(result));

		assertThat(coords, is(not(new Pair<>(1.0, 1.0))));
	}
}
