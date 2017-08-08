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

    public Set<Coords> getBlackStones() {
        Set<Coords> result = (HashSet<Coords>) blackStones.clone();

        return result;
    }

    public Set<Coords> getWhiteStones() {
        Set<Coords> result = (HashSet<Coords>) whiteStones.clone();

        return result;
    }

    protected void playBlackStone(Coords coords) {
        blackStones.add(coords);
    }

    protected void playWhiteStone(Coords coords) {
        whiteStones.add(coords);
    }
}
