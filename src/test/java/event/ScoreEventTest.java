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

package event;

import javafx.event.EventHandler;
import logic.score.BoardScorer;
import logic.score.BoardScorerTest;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static util.Coords.getCoords;
import static util.StoneColour.BLACK;
import static util.StoneColour.WHITE;

public class ScoreEventTest {

	@Test
	public void scoreEvent() {
		EventBus bus = EventBus.getInstance();
		BoardScorer scorer = new BoardScorer(BoardScorerTest.buildTestBoard(BoardScorerTest.testBoard2), 0);
		ScoreEvent event = new ScoreEvent(scorer, bus);

		ScoreEventConsumer dummy = new ScoreEventConsumer();

		EventHandler<ScoreEvent> handler = dummy::consume;

		EventBus.addEventHandler(ScoreEvent.ANY, handler);
		bus.fireEvent(event);
		assertThat(dummy.hits, is(1));

		EventBus.removeEventHandler(ScoreEvent.ANY, handler);
		bus.fireEvent(event);
		assertThat(dummy.hits, is(1));
	}

	@Test
	public void scoreData() {
		BoardScorer scorer = new BoardScorer(BoardScorerTest.buildTestBoard(BoardScorerTest.testBoard3, 2), 0.5);
		scorer.markGroupDead(getCoords("L7"));
		scorer.markGroupDead(getCoords("L18"));

		EventBus bus = EventBus.getInstance();
		ScoreEvent event = new ScoreEvent(scorer, bus);

		ScoreEventConsumer dummy = new ScoreEventConsumer();
		EventBus.addEventHandler(ScoreEvent.ANY, dummy::consume);

		bus.fireEvent(event);

		assertThat(dummy.scoreEvent.getScore(BLACK), is(scorer.getScore(BLACK)));
		assertThat(dummy.scoreEvent.getDeadStones(WHITE), is(scorer.getDeadStones(WHITE)));
		assertThat(dummy.scoreEvent.getTerritory(BLACK), is(scorer.getTerritory(BLACK)));
	}

	private class ScoreEventConsumer {

		public int hits = 0;
		public ScoreEvent scoreEvent;

		public void consume(ScoreEvent event) {
			hits++;
			scoreEvent = event;
		}
	}
}