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

package ui.drawer;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import util.CoordProjector;
import util.DimensionHelper;
import util.DrawCoords;

public abstract class BoardDrawer {

	private Canvas canvas;

	BoardDrawer(Canvas canvas) {
		this.canvas = canvas;
	}

	public void draw() {
		drawBackground();
		drawLines();
	}

	abstract void drawBackground();

	abstract void drawLines();

	GraphicsContext getGraphicsContext() {
		return canvas.getGraphicsContext2D();
	}

	double getBoardLength() {
		return DimensionHelper.getBoardLength(canvas);
	}

	DrawCoords getTopLeftCorner() {
		return DimensionHelper.getTopLeftCorner(canvas);
	}

	CoordProjector getProjector() {
		return DimensionHelper.getProjector(canvas);
	}
}
