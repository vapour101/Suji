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

    Chain(Coords coords)
    {
        stones = new HashSet<>();
        stones.add(coords);
    }

    protected boolean contains(Coords stone)
    {
        return stones.contains(stone);
    }

    protected Set<Coords> getLiberties()
    {
        HashSet<Coords> liberties = new HashSet<>();

        for (Coords stone : stones)
        {
            Set<Coords> neighbours = stone.getNeighbours();

            for (Coords c : neighbours)
                if ( ! this.contains(c) )
                    liberties.add(c);
        }

        return liberties;
    }

    protected boolean isAdjacentTo(Coords coords)
    {
        Set<Coords> neighbours = coords.getNeighbours();

        for (Coords c : neighbours)
        {
            if (this.contains(c))
                return true;
        }

        return false;
    }
}
