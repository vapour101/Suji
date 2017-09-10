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

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import event.EventBus;
import event.ScoreEvent;
import util.Coords;
import util.StoneColour;

import java.util.*;

import static util.Coords.getCoords;

public class BoardScorer {

	private Board board;
	private double komi;
	private Multimap<StoneColour, Chain> deadChains;

	public BoardScorer(Board board, double komi) {
		this.board = board;
		this.komi = komi;
		deadChains = HashMultimap.create();

		fireScoreEvent();
	}

	private void fireScoreEvent() {
		EventBus bus = EventBus.getInstance();
		ScoreEvent event = new ScoreEvent(this, bus);
		bus.fireEvent(event);
	}

	public double getScore() {
		return getScore(StoneColour.BLACK) - getScore(StoneColour.WHITE);
	}

	public double getScore(StoneColour colour) {
		double score = countTerritory(colour);
		score += board.getCaptures(colour);

		for (Chain chain : getDeadChains(colour.other()))
			score += chain.size();

		if ( komi < 0 && colour == StoneColour.BLACK )
			score -= komi;
		else if ( komi > 0 && colour == StoneColour.WHITE )
			score += komi;

		return score;
	}

	public Set<Coords> getTerritory(StoneColour colour) {
		Collection<Coords> potentialTerritory = getEmptyIntersections();

		Set<Coords> liberties = new HashSet<>();
		Set<Coords> otherLiberties = new HashSet<>();

		for (Coords c : getLiveStones(colour))
			liberties.addAll(c.getNeighbours());

		for (Coords c : getLiveStones(colour.other()))
			otherLiberties.addAll(c.getNeighbours());

		liberties.retainAll(potentialTerritory);
		otherLiberties.retainAll(potentialTerritory);

		liberties.removeAll(otherLiberties);
		otherLiberties.removeAll(liberties);

		Set<Coords> territory = new HashSet<>();

		for (Coords c : liberties)
			territory.addAll(getContiguousEmptySection(potentialTerritory, c));

		for (Coords c : otherLiberties)
			territory.removeAll(getContiguousEmptySection(potentialTerritory, c));

		return territory;
	}

	public void markGroupDead(Coords coords) {
		Chain deadChain = board.getChainAtCoords(coords);

		if ( deadChain == null )
			return;

		for (StoneColour colour : StoneColour.values())
			if ( board.getStones(colour).contains(coords) ) {
				getDeadChains(colour).add(deadChain);
				fireScoreEvent();
				break;
			}
	}

	private Collection<Chain> getDeadChains(StoneColour colour) {
		return deadChains.get(colour);
	}

	public void unmarkGroupDead(Coords coords) {
		Chain undeadChain = null;

		for (Chain deadChain : getDeadChains(StoneColour.WHITE))
			if ( deadChain.contains(coords) ) {
				undeadChain = deadChain;
				break;
			}

		if ( undeadChain != null ) {
			getDeadChains(StoneColour.WHITE).remove(undeadChain);
			fireScoreEvent();
			return;
		}

		for (Chain deadChain : getDeadChains(StoneColour.BLACK))
			if ( deadChain.contains(coords) ) {
				undeadChain = deadChain;
				break;
			}

		if ( undeadChain != null ) {
			getDeadChains(StoneColour.BLACK).remove(undeadChain);
			fireScoreEvent();
		}
	}

	public Set<Coords> getDeadStones(StoneColour colour) {
		Set<Coords> deadStones = new HashSet<>();

		for (Chain chain : getDeadChains(colour))
			deadStones.addAll(chain.getStones());

		return deadStones;
	}

	Collection<Coords> getEmptyIntersections() {
		Collection<Coords> emptyIntersections = getAllIntersections();

		emptyIntersections.removeAll(getLiveStones());

		return emptyIntersections;
	}

	Collection<Coords> getContiguousEmptySection(Collection<Coords> emptyBoard, Coords startingPoint) {
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

	private int countTerritory(StoneColour colour) {
		return getTerritory(colour).size();
	}

	private Collection<Coords> getLiveStones() {
		Collection<Coords> liveStones = getLiveStones(StoneColour.BLACK);
		liveStones.addAll(getLiveStones(StoneColour.WHITE));

		return liveStones;
	}

	private Collection<Coords> getAllIntersections() {
		Set<Coords> coords = new HashSet<>();

		for (int i = 1; i < 20; i++)
			for (int j = 1; j < 20; j++)
				coords.add(getCoords(i, j));

		return coords;
	}

	private Collection<Coords> getLiveStones(StoneColour colour) {
		Collection<Coords> liveStones;

		liveStones = board.getStones(colour);


		liveStones.removeAll(getDeadStones(colour));

		return liveStones;
	}
}
