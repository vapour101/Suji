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

import javafx.scene.canvas.Canvas;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static util.DimensionHelper.getBoardLength;
import static util.DimensionHelper.getTopLeftCorner;

public class DimensionHelperTest {

	@Test
	public void findingTopLeft() {
		Canvas canvas = new Canvas(100, 80);
		DrawCoords topLeft = getTopLeftCorner(canvas);

		assertThat(topLeft, is(new DrawCoords(10, 0)));

		canvas = new Canvas(80, 100);
		topLeft = getTopLeftCorner(canvas);

		assertThat(topLeft, is(new DrawCoords(0, 10)));

		canvas = new Canvas(100, 100);
		topLeft = getTopLeftCorner(canvas);

		assertThat(topLeft, is(new DrawCoords(0, 0)));
	}

	@Test
	public void gettingBoardLength() {
		Canvas canvas = new Canvas(100, 80);
		double length = getBoardLength(canvas);

		assertThat(length, is(80.0));

		canvas = new Canvas(80, 100);
		length = getBoardLength(canvas);

		assertThat(length, is(80.0));
	}
}