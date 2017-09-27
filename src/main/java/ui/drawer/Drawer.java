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
import util.CoordProjector;
import util.Coords;
import util.DimensionHelper;
import util.StoneColour;

import java.util.Collection;

public class Drawer {

	Board lastState;
	private Canvas canvas;
	private StoneDrawer stoneDrawer;
	private BoardDrawer boardDrawer;

	Drawer(Drawer clone) {
		this(clone.getCanvas());

		setStoneDrawer(clone.getStoneDrawer());
		setBoardDrawer(clone.getBoardDrawer());
	}

	private BoardDrawer getBoardDrawer() {
		return boardDrawer;
	}

	public void setBoardDrawer(BoardDrawer boardDrawer) {
		this.boardDrawer = boardDrawer;
	}

	Drawer(Canvas canvas) {
		this.canvas = canvas;
		lastState = new Board();

		setStoneDrawer(new SimpleStoneDrawer(canvas));
		setBoardDrawer(new SimpleBoardDrawer(canvas));

		canvas.widthProperty().addListener(this::onCanvasResize);
		canvas.heightProperty().addListener(this::onCanvasResize);
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
}
