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

package logic;

import util.Coords;

import java.util.HashSet;
import java.util.Set;

public class Board {
    private HashSet<Coords> blackStones;
    private HashSet<Coords> whiteStones;

    public Board() {
        blackStones = new HashSet<>();
        whiteStones = new HashSet<>();
    }

    public final Set<Coords> getBlackStones() {
        return blackStones;
    }

    public final Set<Coords> getWhiteStones() {
        return whiteStones;
    }

    public void playBlackStone(Coords coords) {
        throwIfOccupied(coords);

        blackStones.add(coords);
    }

    public void playWhiteStone(Coords coords) {
        throwIfOccupied(coords);

        whiteStones.add(coords);
    }

    public boolean isLegalWhiteMove(Coords coords) {
        return !isOccupied(coords);
    }

    public boolean isLegalBlackMove(Coords coords) {
        return !isOccupied(coords);
    }


    private void throwIfOccupied(Coords coords) {
        if (isOccupied(coords))
            throw new IllegalArgumentException(coords.toString() + " is already occupied.");
    }

    private boolean isOccupied(Coords coords) {
        return blackStones.contains(coords) || whiteStones.contains(coords);
    }
}
