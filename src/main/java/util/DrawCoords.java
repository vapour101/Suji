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

import javafx.util.Pair;

public class DrawCoords {

	private Pair<Double, Double> coordinates;

	public DrawCoords(double x, double y) {
		coordinates = new Pair<>(x, y);
	}

	public static DrawCoords sum(DrawCoords lhs, DrawCoords rhs) {
		return new DrawCoords(lhs.getX() + rhs.getX(), lhs.getY() + rhs.getY());
	}

	public double getX() {
		return coordinates.getKey();
	}

	public double getY() {
		return coordinates.getValue();
	}

	public void setY(double y) {
		coordinates = new Pair<>(coordinates.getKey(), y);
	}

	public void setX(double x) {
		coordinates = new Pair<>(x, coordinates.getValue());
	}

	public void applyOffset(DrawCoords offset) {
		double x = getX() + offset.getX();
		double y = getY() + offset.getY();

		coordinates = new Pair<>(x, y);
	}

	public boolean equals(Object other) {
		if ( this == other )
			return true;
		else if ( !(other instanceof DrawCoords) )
			return false;
		else {
			DrawCoords compare = (DrawCoords) other;
			return this.coordinates.equals(compare.coordinates);
		}
	}
}
