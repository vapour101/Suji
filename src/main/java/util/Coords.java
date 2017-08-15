/*
 * Copyright (C) 2017 Vincent Varkevisser
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

public class Coords {
    private Pair<Integer, Integer> coordinates;

    Coords(int x, int y) {
        coordinates = new Pair<>(x, y);
    }

    public boolean equals(Object other) {
        if (this == other)
            return true;
        else if (!(other instanceof Coords))
            return false;
        else {
            Coords compare = (Coords) other;
            return this.coordinates.equals(compare.coordinates);
        }
    }

    public static Coords get(int x, int y) {
        return new Coords(x,y);
    }

    public String toString()
    {
        String result = "(";
        result += coordinates.getKey().toString();
        result += ", ";
        result += coordinates.getValue().toString();
        result += ")";
        return result;
    }

    public int hashCode() {
        return this.coordinates.hashCode();
    }
}
