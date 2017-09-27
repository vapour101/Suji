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

package logic.gamehandler;

import event.SujiEvent;
import javafx.event.EventHandler;
import javafx.event.EventTarget;
import javafx.event.EventType;
import logic.board.BoardProvider;
import logic.gametree.GameTreeProvider;
import logic.score.ScoreProvider;
import sgf.SGFProvider;
import util.StoneColour;

/**
 * Common interface for any class that tracks a game of Go from
 * start to finish.
 */
public interface GameHandler extends BoardProvider, EventTarget, GameTreeProvider, SGFProvider, ScoreProvider {

	void pass();

	void undo();

	StoneColour getTurnPlayer();

	void setKomi(double komi);

	<T extends SujiEvent> void fireEvent(T event);

	<T extends SujiEvent> void subscribe(EventType<T> eventType, EventHandler<? super T> eventHandler);

	<T extends SujiEvent> void unsubscribe(EventType<T> eventType, EventHandler<? super T> eventHandler);
}
