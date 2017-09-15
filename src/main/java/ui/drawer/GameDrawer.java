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
import logic.gamehandler.GameHandler;
import util.*;

import java.util.Collection;

import static util.Move.play;

public class GameDrawer {

	private Canvas canvas;
	private GameHandler game;
	private StoneDrawer stoneDrawer;
	private BoardDrawer boardDrawer;
	private Move hoverStone;

	public GameDrawer(Canvas canvas, GameHandler game) {
		this.canvas = canvas;
		this.game = game;

		hoverStone = null;

		setStoneDrawer(new SimpleStoneDrawer(canvas));
		setBoardDrawer(new SimpleBoardDrawer(canvas));

		canvas.widthProperty().addListener(this::onCanvasResize);
		canvas.heightProperty().addListener(this::onCanvasResize);
	}

	public void setBoardDrawer(BoardDrawer boardDrawer) {
		this.boardDrawer = boardDrawer;
	}

	StoneDrawer getStoneDrawer() {
		return stoneDrawer;
	}

	public void setStoneDrawer(StoneDrawer stoneDrawer) {
		this.stoneDrawer = stoneDrawer;
	}

	private void onCanvasResize(Observable observable) {
		draw();
	}

	public void draw() {
		drawBackground();
		boardDrawer.draw();
		drawStones();
	}

	private void drawStones() {
		for (StoneColour colour : StoneColour.values())
			drawStones(colour);
	}

	void drawStones(StoneColour colour) {
		Collection<Coords> stones = getStones(colour);
		stoneDrawer.drawStones(stones, colour);
	}

	Collection<Coords> getStones(StoneColour colour) {
		return game.getStones(colour);
	}

	private void drawBackground() {
		GraphicsContext context = getGraphicsContext();

		context.setFill(Color.GREEN);
		context.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
	}

	private GraphicsContext getGraphicsContext() {
		return canvas.getGraphicsContext2D();
	}

	public void setHoverStone(DrawCoords position, StoneColour colour) {
		CoordProjector projector = getProjector();

		if ( !projector.isWithinBounds(position) ) {
			hoverStone = null;
			draw();
			return;
		}

		Coords boardPos = projector.nearestCoords(position);
		DrawCoords pos = projector.fromBoardCoords(boardPos);
		Move move = play(boardPos, colour);

		if ( hoverStone == move )
			return;

		if ( game.isLegalMove(move) ) {
			draw();
			stoneDrawer.drawGhostStone(pos, colour);
			hoverStone = move;
		}
	}

	private CoordProjector getProjector() {
		return DimensionHelper.getProjector(canvas);
	}
}
