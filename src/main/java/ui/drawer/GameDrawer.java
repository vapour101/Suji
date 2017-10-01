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

import javafx.beans.Observable;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import logic.board.Board;
import util.*;

import java.util.Collection;

import static util.Move.play;

public class GameDrawer implements Drawer {

	private Move hoverStone;
	private Board lastState;
	private Canvas canvas;
	private StoneDrawer stoneDrawer;
	private BoardDrawer boardDrawer;


	GameDrawer(Drawer clone) {
		this(clone.getCanvas());

		setStoneDrawer(clone.getStoneDrawer());
		setBoardDrawer(clone.getBoardDrawer());
	}

	public GameDrawer(Canvas canvas) {
		this.canvas = canvas;
		lastState = new Board();

		setStoneDrawer(new SimpleStoneDrawer(canvas));
		setBoardDrawer(new SimpleBoardDrawer(canvas));

		canvas.widthProperty().addListener(this::onCanvasResize);
		canvas.heightProperty().addListener(this::onCanvasResize);
		hoverStone = null;
	}

	@Override
	public BoardDrawer getBoardDrawer() {
		return boardDrawer;
	}

	@Override
	public void setBoardDrawer(BoardDrawer boardDrawer) {
		this.boardDrawer = boardDrawer;
	}

	@Override
	public StoneDrawer getStoneDrawer() {
		return stoneDrawer;
	}

	@Override
	public void setStoneDrawer(StoneDrawer stoneDrawer) {
		this.stoneDrawer = stoneDrawer;
	}

	@Override
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

	@Override
	public void redraw() {
		draw(lastState);
	}

	@Override
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
			getStoneDrawer().drawGhostStone(pos, colour);
			hoverStone = move;
		}
	}

	private CoordProjector getProjector() {
		return DimensionHelper.getProjector(canvas);
	}

	@Override
	public Canvas getCanvas() {
		return canvas;
	}

	private void onCanvasResize(Observable observable) {
		redraw();
	}
}
