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

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class ChainSet {
    private HashMap<Chain, Integer> chains;

    public ChainSet() {
        chains = new HashMap<>();
    }

    public boolean contains(Coords stone) {
        for (Chain chain : chains.keySet())
            if (chain.contains(stone))
                return true;

        return false;
    }

    public Set<Coords> getStones() {
        Set<Coords> stones = new HashSet<>();

        for (Chain chain : chains.keySet())
            stones.addAll(chain.getStones());

        return stones;
    }

    public void add(Coords stone) {
        addChain(new Chain(stone));
    }

    private void addChain(Chain chain) {
        for (Chain existing : chains.keySet())
            if (existing.isAdjacentTo(chain)) {
                existing.mergeChain(chain);
                return;
            }

        chains.put(chain, chain.countLiberties());

    }
}
