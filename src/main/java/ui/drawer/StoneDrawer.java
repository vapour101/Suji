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
import util.*;

import java.util.Collection;


public abstract class StoneDrawer {

	private Canvas canvas;
	private Double radius;

	StoneDrawer(StoneDrawer other) {
		this(other.canvas);
	}

	StoneDrawer(Canvas canvas) {
		this.canvas = canvas;
		radius = null;
	}

	public void drawGhostStone(DrawCoords position, StoneColour colour) {
		GraphicsContext context = getGraphicsContext();

		context.setGlobalAlpha(0.5);
		draw(position, colour);
		context.setGlobalAlpha(1);
	}

	GraphicsContext getGraphicsContext() {
		return canvas.getGraphicsContext2D();
	}

	public abstract void draw(DrawCoords position, StoneColour colour);

	public abstract StoneDrawer clone();

	public void setCanvas(Canvas canvas) {
		this.canvas = canvas;
	}

	public void drawGhostStones(Collection<Coords> stones, StoneColour colour) {
		GraphicsContext context = getGraphicsContext();

		context.setGlobalAlpha(0.5);
		drawStones(stones, colour);
		context.setGlobalAlpha(1);
	}

	public void drawStones(Collection<Coords> stones, StoneColour colour) {
		for (Coords stone : stones)
			draw(getProjector().fromBoardCoords(stone), colour);
	}

	private CoordProjector getProjector() {
		return DimensionHelper.getProjector(canvas);
	}

	public void drawStones(Collection<Coords> stones, StoneColour colour, double scale) {
		for (Coords stone : stones)
			draw(getProjector().fromBoardCoords(stone), colour, scale);
	}

	public abstract void draw(DrawCoords position, StoneColour colour, double scale);

	double getRadius() {
		if ( radius != null )
			return radius;

		return DimensionHelper.getStoneRadius(canvas);
	}

	public void setRadius(Double r) {
		radius = r;
	}
}
