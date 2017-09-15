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

import static util.Coords.getCoords;

public class TexturedBoardDrawer extends SimpleBoardDrawer {

	private boolean simpleLines;
	private Image background;
	private Image lines;

	public TexturedBoardDrawer(Canvas canvas, Image backgroundTexture, boolean useSimpleLines) {
		this(canvas, backgroundTexture, null, useSimpleLines);
	}

	private TexturedBoardDrawer(Canvas canvas, Image backgroundTexture, Image lineTexture, boolean useSimpleLines) {
		super(canvas);

		background = backgroundTexture;
		lines = lineTexture;
		simpleLines = useSimpleLines;
	}

	@Override
	public void drawBackground() {
		if ( background == null )
			super.drawBackground();
		else
			drawBackgroundTexture();
	}

	@Override
	public void drawLines() {
		if ( simpleLines )
			super.drawLines();
		else if ( lines != null )
			drawLineTexture();
	}

	private void drawLineTexture() {
		DrawCoords topLeft = getProjector().fromBoardCoords(getCoords("A1"));
		double x = topLeft.getX();
		double y = topLeft.getY();

		double l = getProjector().fromBoardCoords(getCoords("T19")).getX() - x;

		getGraphicsContext().drawImage(lines, x, y, l, l);
	}

	private void drawBackgroundTexture() {
		double x = getTopLeftCorner().getX();
		double y = getTopLeftCorner().getY();
		double l = getBoardLength();

		getGraphicsContext().drawImage(background, x, y, l, l);
	}
}
