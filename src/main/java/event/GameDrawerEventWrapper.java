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
import ui.drawer.Drawer;
import ui.drawer.DrawerDecorator;
import util.LogHelper;

public class GameDrawerEventWrapper extends DrawerDecorator {

	private EventHandler<GameEvent> changeHandler = this::onChange;
	private EventHandler<HoverEvent> hoverHandler = this::onHover;
	private EventHandler<GameEvent> gameOverHandler = this::onGameOver;

	public GameDrawerEventWrapper(Drawer drawer, EventPublisher publisher) {
		super(drawer);

		publisher.subscribe(GameEvent.CHANGE, changeHandler);
		publisher.subscribe(HoverEvent.HOVER, hoverHandler);
		publisher.subscribe(GameEvent.GAMEOVER, gameOverHandler);
	}

	@Override
	protected void finalize() throws Throwable {
		super.finalize();
		LogHelper.finest("Finalizing GameDrawerEventWrapper");
	}

	private void onChange(GameEvent event) {
		draw(event.getBoard());
	}

	private void onHover(HoverEvent event) {
		setHoverStone(event.getPoint(), event.getTurnPlayer());
	}

	private void onGameOver(GameEvent event) {
		event.getPublisher().unsubscribe(GameEvent.CHANGE, changeHandler);
		event.getPublisher().unsubscribe(HoverEvent.HOVER, hoverHandler);
		event.getPublisher().unsubscribe(GameEvent.GAMEOVER, gameOverHandler);

		changeHandler = null;
		hoverHandler = null;
		gameOverHandler = null;
	}
}
