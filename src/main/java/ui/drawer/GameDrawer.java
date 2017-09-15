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

import static util.Coords.getCoords;
import static util.DimensionHelper.getBoardLength;
import static util.DimensionHelper.getTopLeftCorner;
import static util.HandicapHelper.getHandicapStones;
import static util.Move.play;
import static util.StoneColour.BLACK;

public class GameDrawer {

	private Canvas canvas;
	private GameHandler game;
	private StoneDrawer stoneDrawer;
	private Move hoverStone;

	public GameDrawer(Canvas canvas, GameHandler game) {
		this.canvas = canvas;
		this.game = game;

		hoverStone = null;

		setStoneDrawer(new SimpleStoneDrawer(canvas));

		canvas.widthProperty().addListener(this::onCanvasResize);
		canvas.heightProperty().addListener(this::onCanvasResize);
	}

	StoneDrawer getStoneDrawer() {
		return stoneDrawer;
	}

	public void setStoneDrawer(StoneDrawer stoneDrawer) {
		this.stoneDrawer = stoneDrawer;
		stoneDrawer.setRadiusBuilder(this::getStoneRadius);
		stoneDrawer.setProjectorBuilder(this::getProjector);
	}

	double getStoneRadius() {
		return getBoardLength(canvas) / (19 + 1) / 2;
	}

	private void onCanvasResize(Observable observable) {
		draw();
	}

	public void draw() {
		drawBackground();
		drawBoardTexture();
		drawBoardLines();
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

	private void drawBoardTexture() {
		DrawCoords topLeft = getTopLeftCorner(canvas);
		GraphicsContext context = getGraphicsContext();
		double length = getBoardLength(canvas);

		context.setFill(Color.web("0xB78600"));
		context.fillRect(topLeft.getX(), topLeft.getY(), length, length);
	}

	private void drawBoardLines() {
		CoordProjector projector = getProjector();

		GraphicsContext context = getGraphicsContext();

		for (int i = 1; i < 20; i++) {
			//Horizontal Lines
			DrawCoords start = projector.fromBoardCoords(getCoords(1, i));
			DrawCoords end = projector.fromBoardCoords(getCoords(19, i));

			context.strokeLine(start.getX(), start.getY(), end.getX(), end.getY());

			//Vertical Lines
			start = projector.fromBoardCoords(getCoords(i, 1));
			end = projector.fromBoardCoords(getCoords(i, 19));

			context.strokeLine(start.getX(), start.getY(), end.getX(), end.getY());
		}

		StoneDrawer drawer = new SimpleStoneDrawer(canvas);
		drawer.setProjectorBuilder(() -> projector);
		drawer.setRadiusBuilder(() -> context.getLineWidth() * 4);

		drawer.drawStones(getHandicapStones(9), BLACK);
	}

	private CoordProjector getProjector() {
		return new CoordProjector(getBoardLength(canvas), getTopLeftCorner(canvas));
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
}
