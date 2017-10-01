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

import javafx.event.EventTarget;
import javafx.event.EventType;
import logic.board.Board;
import logic.gamehandler.GameHandler;
import util.StoneColour;

public class GameEvent extends SujiEvent {

	public static final EventType<GameEvent> GAME = new EventType<>(ANY, "GAME");
	public static final EventType<GameEvent> START = new EventType<>(GAME, "START");
	public static final EventType<GameEvent> GAMEOVER = new EventType<>(GAME, "GAMEOVER");
	public static final EventType<GameEvent> REVIEW = new EventType<>(GAME, "REVIEW");
	public static final EventType<GameEvent> REVIEWSTART = new EventType<>(REVIEW, "REVIEWSTART");

	public static final EventType<GameEvent> PASS = new EventType<>(GAME, "PASS");

	public static final EventType<GameEvent> CHANGE = new EventType<>(GAME, "CHANGE");
	public static final EventType<GameEvent> MOVE = new EventType<>(CHANGE, "MOVE");
	public static final EventType<GameEvent> UNDO = new EventType<>(CHANGE, "UNDO");

	public GameEvent(GameHandler source, EventTarget target, EventType<? extends GameEvent> eventType) {
		super(source, target, eventType);
	}

	public Board getBoard() {
		return getHandler().getBoard();
	}

	public GameHandler getHandler() {
		return (GameHandler) getSource();
	}

	public StoneColour getTurnPlayer() {
		return getHandler().getTurnPlayer();
	}
}
