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

import static util.Coords.getCoords;

public class CoordProjector {

	private static final int BOARD_SIZE = 19;
	private double boardLength;
	private DrawCoords topLeft;

	public CoordProjector(double boardLength) {
		this.boardLength = boardLength;
		this.topLeft = new DrawCoords(0, 0);
	}

	public CoordProjector(double boardLength, DrawCoords topLeft) {
		this.boardLength = boardLength;
		this.topLeft = topLeft;
	}

	public DrawCoords fromBoardCoords(Coords boardCoords) {
		double spacing = (boardLength / (BOARD_SIZE + 1));

		double realX = boardCoords.x() * spacing;
		double realY = boardCoords.y() * spacing;

		DrawCoords result = new DrawCoords(realX, realY);
		result.applyOffset(topLeft);

		return result;
	}

	public Coords nearestCoords(DrawCoords point) {
		point = snapToBounds(point);
		point.removeOffset(topLeft);

		double spacing = (boardLength / (BOARD_SIZE + 1));

		int boardX = (int) (point.getX() / spacing);
		int boardY = (int) (point.getY() / spacing);

		return getCoords(boardX, boardY);
	}

	private DrawCoords snapToBounds(DrawCoords point) {
		double snapX = point.getX();
		double snapY = point.getY();

		if ( snapX < xLowerBound() )
			snapX = xLowerBound();

		if ( snapX > xUpperBound() )
			snapX = xUpperBound();

		if ( snapY < yLowerBound() )
			snapY = yLowerBound();

		if ( snapY > yUpperBound() )
			snapY = yUpperBound();

		return new DrawCoords(snapX, snapY);
	}

	private double xLowerBound() {
		return topLeft.getX();
	}

	private double xUpperBound() {
		return topLeft.getX() + boardLength;
	}

	private double yLowerBound() {
		return topLeft.getY();
	}

	private double yUpperBound() {
		return topLeft.getY() + boardLength;
	}
}
