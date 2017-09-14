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

package event.decorators;

import event.EventBus;
import event.ScoreEvent;
import logic.gamehandler.GameHandler;
import logic.gamehandler.LocalGameHandler;
import logic.score.Scorer;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static util.Coords.getCoords;
import static util.Move.play;
import static util.StoneColour.BLACK;
import static util.StoneColour.WHITE;

public class ScorerEventTest {

	@Test
	public void triggerOnMarkingStones() {
		GameHandler game = new GameHandlerEventDecorator(new LocalGameHandler(0));
		game.playMove(play(getCoords("A1"), BLACK));
		game.playMove(play(getCoords("B2"), WHITE));
		Scorer scorer = game.getScorer();

		ScoreEventConsumer dummy = new ScoreEventConsumer();
		EventBus.addEventHandler(ScoreEvent.ANY, dummy::consume);

		scorer.markGroupDead(getCoords("A2"));
		assertThat(dummy.hits, is(0));

		scorer.markGroupDead(getCoords("A1"));
		assertThat(dummy.hits, is(1));

		scorer.markGroupDead(getCoords("B2"));
		assertThat(dummy.hits, is(2));
	}

	@Test
	public void triggerOnUnmarkingStones() {
		GameHandler game = new GameHandlerEventDecorator(new LocalGameHandler(0));
		game.playMove(play(getCoords("A1"), BLACK));
		game.playMove(play(getCoords("B2"), WHITE));
		Scorer scorer = game.getScorer();

		scorer.markGroupDead(getCoords("A1"));
		scorer.markGroupDead(getCoords("B2"));

		ScoreEventConsumer dummy = new ScoreEventConsumer();
		EventBus.addEventHandler(ScoreEvent.ANY, dummy::consume);

		scorer.unmarkGroupDead(getCoords("A2"));
		assertThat(dummy.hits, is(0));

		scorer.unmarkGroupDead(getCoords("A1"));
		assertThat(dummy.hits, is(1));

		scorer.unmarkGroupDead(getCoords("B2"));
		assertThat(dummy.hits, is(2));
	}

	private class ScoreEventConsumer {

		ScoreEvent lastEvent;
		int hits = 0;

		void consume(ScoreEvent event) {
			lastEvent = event;
			hits++;
		}
	}
}
