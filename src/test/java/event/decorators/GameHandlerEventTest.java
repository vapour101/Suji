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
import event.GameEvent;
import logic.gamehandler.GameHandler;
import logic.gamehandler.LocalGameHandler;
import org.junit.Test;
import util.Move;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static util.Coords.getCoords;
import static util.Move.play;
import static util.StoneColour.BLACK;
import static util.StoneColour.WHITE;

public class GameHandlerEventTest {

	@Test
	public void decoratorDelegates() {
		GameHandler game = new LocalGameHandler(0);
		GameHandler decorator = new GameHandlerEventDecorator(game);

		assertThat(decorator.getTurnPlayer(), is(game.getTurnPlayer()));

		decorator.setKomi(15);
		assertThat(game.getScorer().getScore(WHITE), is(15.0));

		Move move = play(getCoords("A1"), BLACK);
		assertThat(decorator.isLegalMove(move), is(game.isLegalMove(move)));

		assertThat(decorator.getStones(BLACK), is(game.getStones(BLACK)));

		String sgf = game.getSGFWriter().getSGFString();
		assertThat(decorator.getSGFWriter().getSGFString(), is(sgf));
	}

	@Test
	public void triggerEventsOnUpdate() {
		GameHandler game = new GameHandlerEventDecorator(new LocalGameHandler(0));
		GameEventConsumer dummy = new GameEventConsumer();

		EventBus.addEventHandler(GameEvent.ANY, dummy::consume);

		game.playMove(play(getCoords("A1"), BLACK));
		assertThat(dummy.hits, is(1));

		game.playMove(play(getCoords("A1"), BLACK));
		assertThat(dummy.hits, is(1));
	}

	@Test
	public void triggerOnPass() {
		GameHandler game = new GameHandlerEventDecorator(new LocalGameHandler(0));
		GameEventConsumer dummy = new GameEventConsumer();

		EventBus.addEventHandler(GameEvent.ANY, dummy::consume);

		game.pass();
		assertThat(dummy.hits, is(1));
	}

	@Test
	public void twoPassesEndsGame() {
		GameHandler game = new GameHandlerEventDecorator(new LocalGameHandler(0));
		GameEventConsumer dummy = new GameEventConsumer();

		EventBus.addEventHandler(GameEvent.GAMEOVER, dummy::consume);

		game.playMove(play(getCoords("A1"), BLACK));
		game.pass();
		game.pass();
		assertThat(dummy.hits, is(1));
	}

	@Test
	public void fireOnEffectiveUndoOnly() {
		GameHandler game = new GameHandlerEventDecorator(new LocalGameHandler(0));
		GameEventConsumer dummy = new GameEventConsumer();

		game.playMove(play(getCoords("A1"), BLACK));

		EventBus.addEventHandler(GameEvent.ANY, dummy::consume);

		game.undo();
		assertThat(dummy.hits, is(1));

		game.undo();
		assertThat(dummy.hits, is(1));
	}

	private class GameEventConsumer {

		GameEvent lastEvent;
		int hits = 0;


		void consume(GameEvent event) {
			lastEvent = event;
			hits++;
		}
	}
}