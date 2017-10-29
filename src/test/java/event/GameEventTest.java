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
import logic.board.Board;
import logic.gamehandler.GameHandler;
import logic.gamehandler.LocalGame;
import org.junit.Test;
import util.Move;
import util.StoneColour;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static util.Coords.getCoords;
import static util.Move.play;
import static util.StoneColour.BLACK;
import static util.StoneColour.WHITE;

public class GameEventTest {

	@Test
	public void getState() {
		GameHandler game = new LocalGame(0);
		final Board[] result = {null};
		final StoneColour[] colours = {null};
		Board board = new Board();

		Move move = play(getCoords("K10"), BLACK);
		board.playStone(move);

		EventHandler<GameEvent> dummy = event -> {
			result[0] = event.getBoard();
			colours[0] = event.getTurnPlayer();
		};
		game.subscribe(GameEvent.MOVE, dummy);

		game.playMove(move);

		assertThat(result[0], is(board));
		assertThat(colours[0], is(WHITE));
	}
}
