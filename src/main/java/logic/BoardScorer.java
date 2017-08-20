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

package logic;

import util.Coords;

import java.util.ArrayDeque;
import java.util.HashSet;
import java.util.Queue;
import java.util.Set;

import static util.Coords.getCoords;

public class BoardScorer {

	private Board board;
	private double komi;
	private Set<Chain> deadBlackChains;
	private Set<Chain> deadWhiteChains;

	public BoardScorer(Board board) {
		this(board, 0);
	}

	public BoardScorer(Board board, double komi) {
		this.board = board;
		this.komi = komi;
		deadBlackChains = new HashSet<>();
		deadWhiteChains = new HashSet<>();
	}

	public double getScore() {
		return getBlackScore() - getWhiteScore();
	}

	public double getBlackScore() {
		double blackScore = countBlackTerritory();
		blackScore += board.getBlackCaptures();

		for (Chain chain : deadWhiteChains)
			blackScore += chain.size();

		if ( komi < 0 )
			blackScore -= komi;

		return blackScore;
	}

	public double getWhiteScore() {
		double whiteScore = countWhiteTerritory();
		whiteScore += board.getWhiteCaptures();

		for (Chain chain : deadBlackChains)
			whiteScore += chain.size();

		if ( komi > 0 )
			whiteScore += komi;

		return whiteScore;
	}

	private int countBlackTerritory() {
		return 0;
	}

	private int countWhiteTerritory() {
		return 0;
	}

	public void markGroupDead(Coords coords) {
		Chain deadChain = board.getChainAtCoords(coords);

		if ( deadChain == null )
			return;

		if ( board.getBlackStones().contains(coords) )
			deadBlackChains.add(deadChain);
		else
			deadWhiteChains.add(deadChain);
	}

	public void unmarkGroupDead(Coords coords) {
		Chain undeadChain = null;

		for (Chain deadChain : deadWhiteChains)
			if ( deadChain.contains(coords) ) {
				undeadChain = deadChain;
				break;
			}

		if ( undeadChain != null ) {
			deadWhiteChains.remove(undeadChain);
			return;
		}

		for (Chain deadChain : deadBlackChains)
			if ( deadChain.contains(coords) ) {
				undeadChain = deadChain;
				break;
			}

		if ( undeadChain != null )
			deadBlackChains.remove(undeadChain);
	}

	private Set<Coords> getEmptyIntersections() {
		Set<Coords> emptyIntersections = getAllIntersections();

		emptyIntersections.removeAll(getLiveBlackStones());
		emptyIntersections.removeAll(getLiveWhiteStones());

		return emptyIntersections;
	}

	private Set<Coords> getAllIntersections() {
		Set<Coords> coords = new HashSet<>();

		for (int i = 1; i < 20; i++)
			for (int j = 1; j < 20; j++)
				coords.add(getCoords(i, j));

		return coords;
	}

	private Set<Coords> getLiveBlackStones() {
		Set<Coords> liveBlackStones = board.getBlackStones();
		liveBlackStones.removeAll(getDeadBlackStones());

		return liveBlackStones;
	}

	private Set<Coords> getLiveWhiteStones() {
		Set<Coords> liveWhiteStones = board.getWhiteStones();
		liveWhiteStones.removeAll(getDeadWhiteStones());

		return liveWhiteStones;
	}

	public Set<Coords> getDeadBlackStones() {
		Set<Coords> deadBlackStones = new HashSet<>();

		for (Chain chain : deadBlackChains)
			deadBlackStones.addAll(chain.getStones());

		return deadBlackStones;
	}

	public Set<Coords> getDeadWhiteStones() {
		Set<Coords> deadWhiteStones = new HashSet<>();

		for (Chain chain : deadWhiteChains)
			deadWhiteStones.addAll(chain.getStones());

		return deadWhiteStones;
	}

	protected Set<Coords> getContiguousEmptySection(Set<Coords> emptyBoard, Coords startingPoint) {
		Set<Coords> contiguousEmpty = new HashSet<>();
		Queue<Coords> searchQueue = new ArrayDeque<>();

		searchQueue.add(startingPoint);

		while (!searchQueue.isEmpty()) {
			Coords cur = searchQueue.remove();

			if ( contiguousEmpty.contains(cur) || !emptyBoard.contains(cur) )
				continue;

			Set<Coords> unsearchedNeighbours = cur.getNeighbours();
			unsearchedNeighbours.removeAll(contiguousEmpty);

			searchQueue.addAll(unsearchedNeighbours);

			contiguousEmpty.add(cur);
		}

		return contiguousEmpty;
	}
}
