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

package logic.board;

import util.Coords;

import java.util.Collection;
import java.util.HashSet;

public class Chain {

	private Collection<Coords> stones;
	private Collection<Coords> liberties;

	private Chain(Chain other) {
		stones = new HashSet<>();

		stones.addAll(other.stones);

		recalculateLiberties();
	}

	private void recalculateLiberties() {
		liberties = new HashSet<>();

		for (Coords stone : stones) {
			Collection<Coords> neighbours = stone.getNeighbours();

			for (Coords c : neighbours)
				if ( !this.contains(c) )
					liberties.add(c);
		}
	}

	public boolean contains(Coords stone) {
		return stones.contains(stone);
	}

	Chain(Coords coords) {
		stones = new HashSet<>();

		stones.add(coords);
		recalculateLiberties();
	}

	protected Chain copy() {
		return new Chain(this);
	}

	boolean isAdjacentTo(Coords coords) {
		return liberties.contains(coords);
	}

	public int size() {
		return stones.size();
	}

	int countLiberties() {
		return liberties.size();
	}

	void mergeChain(Chain other) {
		if ( !isAdjacentTo(other) )
			throw new IllegalArgumentException("Chains are not adjacent and cannot be merged.");

		stones.addAll(other.stones);

		other.clear();

		recalculateLiberties();
	}

	boolean isAdjacentTo(Chain other) {
		for (Coords lib : liberties)
			if ( other.contains(lib) )
				return true;

		return false;
	}

	private void clear() {
		stones.clear();
		liberties.clear();
	}

	public Collection<Coords> getStones() {
		return stones;
	}

	Collection<Coords> getOpenLiberties(ChainSet others) {
		Collection<Coords> openLiberties = new HashSet<>();

		openLiberties.addAll(getLiberties());
		openLiberties.removeAll(others.getStones());

		return openLiberties;
	}

	Collection<Coords> getLiberties() {
		return liberties;
	}
}
