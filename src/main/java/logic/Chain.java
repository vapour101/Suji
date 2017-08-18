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

public class Chain {
    private HashSet<Coords> stones;
    private HashSet<Coords> liberties;

    Chain(Coords coords) {
        stones = new HashSet<>();

        stones.add(coords);
        recalculateLiberties();
    }

    private void recalculateLiberties() {
        liberties = new HashSet<>();

        for (Coords stone : stones) {
            Set<Coords> neighbours = stone.getNeighbours();

            for (Coords c : neighbours)
                if (!this.contains(c))
                    liberties.add(c);
        }
    }

    protected boolean contains(Coords stone) {
        return stones.contains(stone);
    }

    protected HashSet<Coords> getLiberties() {
        return liberties;
    }

    protected boolean isAdjacentTo(Coords coords) {
        return liberties.contains(coords);
    }

    protected boolean isAdjacentTo(Chain other) {
        for (Coords lib : liberties)
            if (other.contains(lib))
                return true;

        return false;
    }

    protected int size() {
        return stones.size();
    }

    private void clear() {
        stones.clear();
        liberties.clear();
    }

    protected int countLiberties() {
        return liberties.size();
    }

    protected void mergeChain(Chain other) {
        if (!isAdjacentTo(other))
            throw new IllegalArgumentException("Chains are not adjacent and cannot be merged.");

        stones.addAll(other.stones);

        other.clear();

        recalculateLiberties();
    }
}
