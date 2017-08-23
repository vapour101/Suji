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

import java.util.HashSet;
import java.util.Set;

public class Coords {

	private Pair<Integer, Integer> coordinates;

	private Coords(int x, int y) {
		coordinates = new Pair<>(x, y);
	}

	public static Coords getCoords(int x, int y) {
		return new Coords(x, y);
	}

	public static Coords getCoords(String coords) {
		if ( !coords.matches("[a-hj-tA-HJ-T]((1?[1-9])|(10))") )
			throw new IllegalArgumentException("String: '" + coords + "' is not recognizable as Go coordinates.");

		int x = coords.toUpperCase().charAt(0) - 'A';
		if ( x < 9 )
			x++;
		int y = Integer.parseInt(coords.replaceAll("[a-tA-T]", ""));

		return new Coords(x, y);
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

		return new Coords(getX(), getY() + 1);
	}

	private Coords south() {
		if ( getY() == 1 )
			return null;

		return new Coords(getX(), getY() - 1);
	}

	private Coords west() {
		if ( getX() == 1 )
			return null;

		return new Coords(getX() - 1, getY());
	}

	private Coords east() {
		if ( getX() == 19 )
			return null;

		return new Coords(getX() + 1, getY());
	}

	public final int getY() {
		return coordinates.getValue();
	}

	public final int getX() {
		return coordinates.getKey();
	}

	public int hashCode() {
		return this.coordinates.hashCode();
	}

	public boolean equals(Object other) {
		if ( this == other )
			return true;
		else if ( !(other instanceof Coords) )
			return false;
		else {
			Coords compare = (Coords) other;
			return this.coordinates.equals(compare.coordinates);
		}
	}

	public String toString() {
		String result = "(";
		result += coordinates.getKey().toString();
		result += ", ";
		result += coordinates.getValue().toString();
		result += ")";
		return result;
	}
}
