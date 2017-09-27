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
import javafx.beans.Observable;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import logic.board.Board;
import util.*;

import java.util.Collection;

import static util.Move.play;

public class GameDrawer {

	private Canvas canvas;
	private StoneDrawer stoneDrawer;
	private BoardDrawer boardDrawer;
	private Move hoverStone;
	private Board lastState;


	GameDrawer(GameDrawer clone, GamePublisher publisher) {
		this(clone.canvas, publisher);

		setStoneDrawer(clone.stoneDrawer);
		setBoardDrawer(clone.boardDrawer);
	}

	public GameDrawer(Canvas canvas, GamePublisher game) {
		lastState = new Board();
		this.canvas = canvas;

		hoverStone = null;

		setStoneDrawer(new SimpleStoneDrawer(canvas));
		setBoardDrawer(new SimpleBoardDrawer(canvas));

		game.subscribe(GameEvent.MOVE, this::onGameUpdate);

		canvas.widthProperty().addListener(this::onCanvasResize);
		canvas.heightProperty().addListener(this::onCanvasResize);
	}

	public void setBoardDrawer(BoardDrawer boardDrawer) {
		this.boardDrawer = boardDrawer;
	}

	private void onCanvasResize(Observable observable) {

	}

	private void onGameUpdate(GameEvent event) {
		draw(event.getBoard());
	}

	public void draw(Board board) {
		drawBackground();
		boardDrawer.draw();
		drawStones(board);
		lastState = board;
	}

	private void drawStones(Board board) {
		for (StoneColour colour : StoneColour.values())
			drawStones(board, colour);
	}

	void drawStones(Board board, StoneColour colour) {
		StoneDrawer drawer = getStoneDrawer();
		Collection<Coords> stones = board.getStones(colour);

		drawer.drawStones(stones, colour);
	}

	StoneDrawer getStoneDrawer() {
		return stoneDrawer;
	}

	public void setStoneDrawer(StoneDrawer stoneDrawer) {
		this.stoneDrawer = stoneDrawer;
	}

	private void drawBackground() {
		GraphicsContext context = getGraphicsContext();

		context.setFill(Color.GREEN);
		context.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
	}

	private GraphicsContext getGraphicsContext() {
		return canvas.getGraphicsContext2D();
	}

	void redraw() {
		draw(lastState);
	}

	public void setHoverStone(DrawCoords position, StoneColour colour) {
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
			stoneDrawer.drawGhostStone(pos, colour);
			hoverStone = move;
		}
	}

	private CoordProjector getProjector() {
		return DimensionHelper.getProjector(canvas);
	}
}
