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

public class DimensionHelper {

	public static CoordProjector getProjector(Canvas canvas) {
		return new CoordProjector(getBoardLength(canvas), getTopLeftCorner(canvas));
	}

	public static DrawCoords getTopLeftCorner(Canvas canvas) {
		double length = getBoardLength(canvas);
		double canvasWidth = canvas.getWidth();
		double canvasHeight = canvas.getHeight();

		double x = 0;
		double y = 0;

		if ( canvasWidth > length )
			x = (canvasWidth - length) / 2;
		else
			y = (canvasHeight - length) / 2;

		return new DrawCoords(x, y);
	}

	public static double getBoardLength(Canvas canvas) {
		double canvasWidth = canvas.getWidth();
		double canvasHeight = canvas.getHeight();

		return Math.min(canvasHeight, canvasWidth);
	}

	public static double getStoneRadius(Canvas canvas) {
		return getBoardLength(canvas) / (19 + 1) / 2;
	}
}