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

import java.util.HashSet;
import java.util.Set;

import static util.Coords.getCoords;

public class HandicapHelper {

	public static Set<Coords> getHandicapStones(int handicap) {
		Set<Coords> stones = new HashSet<>();

		if ( handicap > 1 && handicap <= 9 ) {
			stones.add(getCoords("D4"));
			stones.add(getCoords("Q16"));

			if ( handicap > 2 )
				stones.add(getCoords("Q4"));
			if ( handicap > 3 )
				stones.add(getCoords("D16"));
			if ( handicap > 4 && (handicap % 2) == 1 )
				stones.add(getCoords("K10"));
			if ( handicap > 5 ) {
				stones.add(getCoords("D10"));
				stones.add(getCoords("Q10"));
			}
			if ( handicap > 7 ) {
				stones.add(getCoords("K4"));
				stones.add(getCoords("K16"));
			}
		}

		return stones;
	}
}
