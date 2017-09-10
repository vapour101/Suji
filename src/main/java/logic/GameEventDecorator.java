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

import event.EventBus;
import event.GameEvent;
import util.Coords;
import util.StoneColour;

import java.util.Collection;

public class GameEventDecorator implements GameHandler {

	private GameHandler instance;

	public GameEventDecorator(GameHandler game) {
		instance = game;
	}

	@Override
	public boolean isLegalMove(Coords move, StoneColour colour) {
		return instance.isLegalMove(move, colour);
	}

	@Override
	public void playStone(Coords move, StoneColour colour) {
		Board previousPosition = instance.getBoard();
		instance.playStone(move, colour);

		if ( !previousPosition.equals(instance.getBoard()) )
			fireGameEvent();
	}

	@Override
	public Collection<Coords> getStones(StoneColour colour) {
		return instance.getStones(colour);
	}

	@Override
	public Board getBoard() {
		return instance.getBoard();
	}

	private void fireGameEvent() {
		EventBus bus = EventBus.getInstance();
		GameEvent event = new GameEvent(this, bus);
		bus.fireEvent(event);
	}
}
