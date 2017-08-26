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

package logic;

import util.Coords;

import java.util.Collection;
import java.util.HashSet;

public class ChainSet {

	private Collection<Chain> chains;

	ChainSet() {
		chains = new HashSet<>();
	}

	public void add(Coords stone) {
		addChain(new Chain(stone));
	}

	boolean contains(Coords stone) {
		for (Chain chain : chains)
			if ( chain.contains(stone) )
				return true;

		return false;
	}

	Collection<Coords> getStones() {
		Collection<Coords> stones = new HashSet<>();

		for (Chain chain : chains)
			stones.addAll(chain.getStones());

		return stones;
	}

	boolean chainIsCaptured(Coords stone, ChainSet other) {
		for (Chain chain : chains)
			if ( chain.getLiberties().contains(stone) ) {
				Collection<Coords> freeLiberties = chain.getOpenLiberties(other);

				if ( freeLiberties.size() == 1 && freeLiberties.contains(stone) )
					return true;
			}

		return false;
	}

	int captureStones(Coords stone, ChainSet other) {
		Collection<Chain> deadChains = new HashSet<>();

		for (Chain chain : chains) {
			if ( chain.getLiberties().contains(stone) ) {
				Collection<Coords> freeLiberties = chain.getOpenLiberties(other);

				if ( freeLiberties.size() == 1 && freeLiberties.contains(stone) )
					deadChains.add(chain);
			}
		}

		int deadStones = 0;

		for (Chain chain : deadChains)
			deadStones += chain.size();

		chains.removeAll(deadChains);

		return deadStones;
	}

	boolean isSuicide(Coords stone, ChainSet other) {
		//Any play that results in a capture is never suicide
		if ( other.chainIsCaptured(stone, this) )
			return false;

		ChainSet futureChain = copy();

		futureChain.add(stone);
		Chain stoneChain = futureChain.getChainFromStone(stone);

		return (stoneChain.getOpenLiberties(other).size() == 0);
	}

	int getChainCount() {
		return chains.size();
	}

	Chain getChainFromStone(Coords stone) {
		for (Chain chain : chains)
			if ( chain.contains(stone) ) {
				return chain;
			}

		return null;
	}

	private void addChain(Chain chain) {
		for (Chain existing : chains)
			if ( existing.isAdjacentTo(chain) ) {
				chains.remove(existing);
				existing.mergeChain(chain);
				addChain(existing);
				return;
			}

		chains.add(chain);
	}

	private ChainSet copy() {
		ChainSet result = new ChainSet();

		for (Chain chain : chains)
			result.chains.add(chain.copy());

		return result;
	}
}
