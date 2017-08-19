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

public class ChainSet {
    private HashSet<Chain> chains;

    public ChainSet() {
        chains = new HashSet<>();
    }

    public boolean contains(Coords stone) {
        for (Chain chain : chains)
            if (chain.contains(stone))
                return true;

        return false;
    }

    public Set<Coords> getStones() {
        Set<Coords> stones = new HashSet<>();

        for (Chain chain : chains)
            stones.addAll(chain.getStones());

        return stones;
    }

    protected boolean chainIsCaptured(Coords stone, ChainSet other) {
        for (Chain chain : chains)
            if (chain.getLiberties().contains(stone)) {
                Set<Coords> freeLiberties = chain.getOpenLiberties(other);

                if (freeLiberties.size() == 1 && freeLiberties.contains(stone))
                    return true;
            }

        return false;
    }

    protected boolean isSuicide(Coords stone, ChainSet other) {


        return false;
    }

    public void add(Coords stone) {
        addChain(new Chain(stone));
    }

    private void addChain(Chain chain) {
        for (Chain existing : chains)
            if (existing.isAdjacentTo(chain)) {
                existing.mergeChain(chain);
                return;
            }

        chains.add(chain);

    }
}
