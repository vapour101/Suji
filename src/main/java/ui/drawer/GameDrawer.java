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

package ui.drawer;

import event.GameEvent;
import event.GamePublisher;
import event.HoverEvent;
import javafx.scene.canvas.Canvas;
import util.*;

import static util.Move.play;

public class GameDrawer extends Drawer {

	private Move hoverStone;

	public GameDrawer(Canvas canvas, GamePublisher publisher) {
		super(canvas);

		hoverStone = null;

		setStoneDrawer(new SimpleStoneDrawer(canvas));
		setBoardDrawer(new SimpleBoardDrawer(canvas));

		canvas.widthProperty().addListener(this::onCanvasResize);
		canvas.heightProperty().addListener(this::onCanvasResize);

		publisher.subscribe(GameEvent.CHANGE, this::onChange);
		publisher.subscribe(HoverEvent.HOVER, this::onHover);
	}

	private void setHoverStone(DrawCoords position, StoneColour colour) {
		CoordProjector projector = getProjector();

		if ( !projector.isWithinBounds(position) ) {
			hoverStone = null;
			draw(lastState);
			return;
		}

		Coords boardPos = projector.nearestCoords(position);
		DrawCoords pos = projector.fromBoardCoords(boardPos);
		Move move = play(boardPos, colour);

		if ( hoverStone == move )
			return;

		if ( !lastState.isOccupied(move.getPosition()) ) {
			draw(lastState);
			getStoneDrawer().drawGhostStone(pos, colour);
			hoverStone = move;
		}
	}

	private void onChange(GameEvent event) {
		draw(event.getBoard());
	}

	private void onHover(HoverEvent event) {
		setHoverStone(event.getPoint(), event.getTurnPlayer());
	}


}
