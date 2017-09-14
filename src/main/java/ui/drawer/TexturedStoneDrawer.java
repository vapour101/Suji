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
import javafx.scene.image.Image;
import util.DrawCoords;
import util.StoneColour;

import static util.StoneColour.BLACK;

public class TexturedStoneDrawer extends StoneDrawer {

	private Image black;
	private Image white;

	public TexturedStoneDrawer(Canvas canvas, Image blackStone, Image whiteStone) {
		super(canvas);
		black = blackStone;
		white = whiteStone;
	}

	@Override
	public void draw(DrawCoords position, StoneColour colour) {
		Image texture = colour == BLACK ? black : white;

		double x = position.getX();
		double y = position.getY();
		double r = getRadius();
		x -= r;
		y -= r;
		r *= 2;

		getGraphicsContext().drawImage(texture, x, y, r, r);
	}
}
