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
import event.HoverEvent;
import javafx.beans.Observable;
import javafx.event.EventHandler;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import logic.board.Board;
import logic.gamehandler.GameHandler;
import util.*;

import java.util.Collection;

import static util.Move.play;

public class GameDrawer {

	protected Move hoverStone;
	protected EventHandler<GameEvent> changeHandler = this::onChange;
	protected EventHandler<HoverEvent> hoverHandler = this::onHover;
	protected EventHandler<GameEvent> gameOverHandler = this::onGameOver;
	Board lastState;
	private Canvas canvas;
	private StoneDrawer stoneDrawer;
	private BoardDrawer boardDrawer;

	GameDrawer(GameDrawer clone) {
		this(clone.getCanvas());

		setStoneDrawer(clone.getStoneDrawer());
		setBoardDrawer(clone.getBoardDrawer());
	}

	public GameDrawer(Canvas canvas, GameHandler handler) {
		this(canvas);

		setStoneDrawer(new SimpleStoneDrawer(canvas));
		setBoardDrawer(new SimpleBoardDrawer(canvas));

		canvas.widthProperty().addListener(this::onCanvasResize);
		canvas.heightProperty().addListener(this::onCanvasResize);


		handler.subscribe(GameEvent.CHANGE, changeHandler);
		handler.subscribe(HoverEvent.HOVER, hoverHandler);
		handler.subscribe(GameEvent.GAMEOVER, gameOverHandler);
	}

	private BoardDrawer getBoardDrawer() {
		return boardDrawer;
	}

	public void setBoardDrawer(BoardDrawer boardDrawer) {
		this.boardDrawer = boardDrawer;
	}

	GameDrawer(Canvas canvas) {
		this.canvas = canvas;
		lastState = new Board();

		setStoneDrawer(new SimpleStoneDrawer(canvas));
		setBoardDrawer(new SimpleBoardDrawer(canvas));

		canvas.widthProperty().addListener(this::onCanvasResize);
		canvas.heightProperty().addListener(this::onCanvasResize);
		hoverStone = null;
	}

	StoneDrawer getStoneDrawer() {
		return stoneDrawer;
	}

	public void setStoneDrawer(StoneDrawer stoneDrawer) {
		this.stoneDrawer = stoneDrawer;
	}

	Canvas getCanvas() {
		return canvas;
	}

	void onCanvasResize(Observable observable) {
		redraw();
	}

	void redraw() {
		draw(lastState);
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

	private void drawBackground() {
		GraphicsContext context = getCanvas().getGraphicsContext2D();

		context.setFill(Color.GREEN);
		context.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
	}

	CoordProjector getProjector() {
		return DimensionHelper.getProjector(canvas);
	}

	private void onChange(GameEvent event) {
		draw(event.getBoard());
	}

	private void onHover(HoverEvent event) {
		setHoverStone(event.getPoint(), event.getTurnPlayer());
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

	private void onGameOver(GameEvent event) {
		event.getHandler().unsubscribe(GameEvent.CHANGE, changeHandler);
		event.getHandler().unsubscribe(HoverEvent.HOVER, hoverHandler);
		event.getHandler().unsubscribe(GameEvent.GAMEOVER, gameOverHandler);
	}
}
