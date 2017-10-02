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

public class ScoreDrawerEventWrapper extends DrawerDecorator {

	private EventHandler<ScoreEvent> scoreChangeHandler = this::onScoreChange;
	private EventHandler<ScoreEvent> scoreStartHandler = this::onScoreStart;
	private EventHandler<ScoreEvent> scoreDoneHandler = this::onDoneScoring;

	public ScoreDrawerEventWrapper(Drawer drawer, EventPublisher publisher) {
		super(drawer);

		publisher.subscribe(ScoreEvent.START, scoreStartHandler);
		publisher.subscribe(ScoreEvent.SCORE, scoreChangeHandler);
		publisher.subscribe(ScoreEvent.DONE, scoreDoneHandler);
	}

	private void onScoreChange(ScoreEvent event) {
		redraw();
	}

	private void onScoreStart(ScoreEvent event) {
		draw(event.getGameHandler().getBoard());
	}

	private void onDoneScoring(ScoreEvent event) {
		event.getPublisher().unsubscribe(ScoreEvent.SCORE, scoreChangeHandler);
		event.getPublisher().unsubscribe(ScoreEvent.DONE, scoreDoneHandler);
		event.getPublisher().unsubscribe(ScoreEvent.START, scoreStartHandler);

		scoreChangeHandler = null;
		scoreDoneHandler = null;
		scoreStartHandler = null;
	}

	@Override
	protected void finalize() throws Throwable {
		super.finalize();
		LogHelper.finest("Finalizing ScoreDrawerEventWrapper");
	}
}
