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

import javafx.util.Pair;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Coords {

	private static Map<Pair<Integer, Integer>, Coords> coordsPool = new HashMap<Pair<Integer, Integer>, Coords>();
	private Pair<Integer, Integer> coordinates;

	private Coords(int x, int y) {
		if ( x < 1 || y < 1 || x > 19 || y > 19 ) {
			String errorMessage = "One or more of the Coordinates (" + Integer.toString(x) + ", ";
			errorMessage += Integer.toString(y) + ") are outside of the acceptable range for the Coords class. ";
			errorMessage += "Coordinates must lie" + "in the " + "" + "range: 1 <= x,y <= 19.";
			throw new IllegalArgumentException(errorMessage);
		}

		coordinates = new Pair<>(x, y);
	}

	public static Coords getCoords(String coords) {
		if ( !coords.matches("[a-hj-tA-HJ-T]((1?[1-9])|(10))") )
			throw new IllegalArgumentException("String: '" + coords + "' is not recognizable as Go coordinates.");

		int x = coords.toUpperCase().charAt(0) - 'A';
		if ( x < 9 )
			x++;
		int y = Integer.parseInt(coords.replaceAll("[a-tA-T]", ""));

		return getCoords(x, y);
	}

	public static Coords getCoords(int x, int y) {
		Pair<Integer, Integer> key = new Pair<>(x, y);

		if ( !coordsPool.containsKey(key) )
			coordsPool.put(key, new Coords(x, y));

		return coordsPool.get(key);
	}

	public static Coords fromSGFString(String coords) {
		if ( !coords.matches("[a-s][a-s]") )
			throw new IllegalArgumentException("String: '" + coords + "' is not recognizable as SGF coordinates.");

		int x = coords.charAt(0) - 'a' + 1;
		int y = coords.charAt(1) - 'a' + 1;

		return getCoords(x, y);
	}

	public Set<Coords> getNeighbours() {
		HashSet<Coords> neighbours = new HashSet<>();

		if ( north() != null )
			neighbours.add(north());

		if ( south() != null )
			neighbours.add(south());

		if ( west() != null )
			neighbours.add(west());

		if ( east() != null )
			neighbours.add(east());

		return neighbours;
	}

	private Coords north() {
		if ( getY() == 19 )
			return null;

		return getCoords(getX(), getY() + 1);
	}

	public final int getY() {
		return coordinates.getValue();
	}

	public final int getX() {
		return coordinates.getKey();
	}

	private Coords south() {
		if ( getY() == 1 )
			return null;

		return getCoords(getX(), getY() - 1);
	}

	private Coords west() {
		if ( getX() == 1 )
			return null;

		return getCoords(getX() - 1, getY());
	}

	private Coords east() {
		if ( getX() == 19 )
			return null;

		return getCoords(getX() + 1, getY());
	}

	public int hashCode() {
		return this.coordinates.hashCode();
	}

	public boolean equals(Object other) {
		return this == other;
	}

	public String toString() {
		String result = "(";
		result += coordinates.getKey().toString();
		result += ", ";
		result += coordinates.getValue().toString();
		result += ")";
		return result;
	}

	public String toSGFString() {
		char x = 'a';
		x += getX() - 1;
		char y = 'a';
		y += getY() - 1;

		String result = "";
		result += x;
		result += y;

		return result;
	}
}
