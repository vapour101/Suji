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
import javafx.scene.paint.Paint;
import util.DrawCoords;
import util.StoneColour;

import static util.StoneColour.BLACK;

public class SimpleStoneDrawer extends StoneDrawer {

	protected SimpleStoneDrawer(Canvas canvas) {
		super(canvas);
	}

	@Override
	public void draw(DrawCoords position, StoneColour colour) {
		drawStone(position, colour, getRadius());
	}

	private void drawStone(DrawCoords position, StoneColour colour, double radius) {
		GraphicsContext context = getGraphicsContext();

		if ( colour == BLACK )
			context.setFill(javafx.scene.paint.Paint.valueOf("#000000"));
		else
			context.setFill(Paint.valueOf("#FFFFFF"));

		drawCircle(position, radius);
	}

	private void drawCircle(DrawCoords position, double radius) {
		double left = position.getX() - radius;
		double top = position.getY() - radius;
		double diameter = 2 * radius;

		getGraphicsContext().fillOval(left, top, diameter, diameter);
	}

	@Override
	public void draw(DrawCoords position, StoneColour colour, double scale) {
		drawStone(position, colour, getRadius() * scale);
	}
}
