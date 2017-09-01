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
import util.CoordProjector;
import util.Coords;
import util.DrawCoords;
import util.StoneColour;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import static util.DimensionHelper.getBoardLength;
import static util.DimensionHelper.getTopLeftCorner;

public class WabiSabiBoardDrawer extends BoardDrawer {

	Map<Coords, DrawCoords> coordsMap;

	public WabiSabiBoardDrawer(Canvas canvas) {
		super(canvas);
		coordsMap = new HashMap<>();
	}

	@Override
	void drawStonesToCanvas(Collection<Coords> stones, double radius, StoneColour colour) {
		for (Coords stone : stones) {
			drawStoneToCanvas(getWSProjector().fromBoardCoords(stone), radius, colour);
		}
	}

	private WabiSabiProjector getWSProjector() {
		return new WabiSabiProjector(getBoardLength(getCanvas()), getTopLeftCorner(getCanvas()), coordsMap);
	}

	private class WabiSabiProjector extends CoordProjector {

		Map<Coords, DrawCoords> coordsMap;
		Random generator;

		WabiSabiProjector(double boardLength, DrawCoords topLeft, Map<Coords, DrawCoords> map) {
			super(boardLength, topLeft);
			generator = new Random();
			coordsMap = map;
		}

		private double getRandomOffset() {
			double result = generator.nextGaussian();
			result /= 6;

			if ( result > 0.3 )
				result = 0.3;
			else if ( result < -0.3 )
				result = -0.3;

			return result;
		}

		@Override
		public DrawCoords fromBoardCoords(Coords boardCoords) {

			if ( coordsMap.containsKey(boardCoords) ) {
				DrawCoords regularCoords = super.fromBoardCoords(boardCoords);
				DrawCoords modifier = coordsMap.get(boardCoords);

				double x = regularCoords.getX() + modifier.getX() * getSpacing();
				double y = regularCoords.getY() + modifier.getY() * getSpacing();

				return new DrawCoords(x, y);
			}

			DrawCoords wabiSabiOffset = new DrawCoords(getRandomOffset(), getRandomOffset());

			coordsMap.put(boardCoords, wabiSabiOffset);

			return fromBoardCoords(boardCoords);
		}
	}
}
