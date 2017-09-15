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
import javafx.scene.paint.Color;
import util.CoordProjector;
import util.DimensionHelper;
import util.DrawCoords;

import static util.Coords.getCoords;
import static util.HandicapHelper.getHandicapStones;
import static util.StoneColour.BLACK;

public class SimpleBoardDrawer extends BoardDrawer {

	SimpleBoardDrawer(Canvas canvas) {
		super(canvas);
	}

	@Override
	public void drawBackground() {
		DrawCoords topLeft = getTopLeftCorner();
		GraphicsContext context = getGraphicsContext();
		double length = getBoardLength();

		context.setFill(Color.web("0xB78600"));
		context.fillRect(topLeft.getX(), topLeft.getY(), length, length);
	}

	@Override
	public void drawLines() {
		CoordProjector projector = getProjector();

		GraphicsContext context = getGraphicsContext();

		for (int i = 1; i < 20; i++) {
			//Horizontal Lines
			DrawCoords start = projector.fromBoardCoords(getCoords(1, i));
			DrawCoords end = projector.fromBoardCoords(getCoords(19, i));

			context.strokeLine(start.getX(), start.getY(), end.getX(), end.getY());

			//Vertical Lines
			start = projector.fromBoardCoords(getCoords(i, 1));
			end = projector.fromBoardCoords(getCoords(i, 19));

			context.strokeLine(start.getX(), start.getY(), end.getX(), end.getY());
		}
		Canvas canvas = context.getCanvas();

		StoneDrawer drawer = new SimpleStoneDrawer(canvas);
		double scale = DimensionHelper.getStoneRadius(canvas);
		scale = (context.getLineWidth() * 4) / scale;

		drawer.drawStones(getHandicapStones(9), BLACK, scale);
	}
}
