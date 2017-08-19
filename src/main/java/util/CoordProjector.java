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

public class CoordProjector {
    private static final int BOARD_SIZE = 19;

    static public Pair<Double, Double> fromBoardCoords(Coords boardCoords, double boardLength) {
        double offset = (boardLength / (BOARD_SIZE + 1)) / 2;

        double realX = boardCoords.x() * offset;
        double realY = boardCoords.y() * offset;

        return new Pair<>(realX, realY);
    }
}
