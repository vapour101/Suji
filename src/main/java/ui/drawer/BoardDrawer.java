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
import util.CoordProjector;
import util.Coords;
import util.DrawCoords;
import util.StoneColour;

import java.util.Collection;

import static util.Coords.getCoords;
import static util.DimensionHelper.getBoardLength;
import static util.DimensionHelper.getTopLeftCorner;
import static util.HandicapHelper.getHandicapStones;
import static util.Move.play;
import static util.StoneColour.BLACK;

public class BoardDrawer {

	private Canvas canvas;
	private GameHandler game;
	private StoneDrawer stoneDrawer;

	public BoardDrawer(Canvas canvas, GameHandler game) {
		this.canvas = canvas;
		this.game = game;

		setStoneDrawer(new SimpleStoneDrawer(canvas));

		canvas.widthProperty().addListener(this::onCanvasResize);
		canvas.heightProperty().addListener(this::onCanvasResize);
	}

	StoneDrawer getStoneDrawer() {
		return stoneDrawer;
	}

	public void setStoneDrawer(StoneDrawer stoneDrawer) {
		this.stoneDrawer = stoneDrawer;
		stoneDrawer.setRadius(getStoneRadius());
		stoneDrawer.setProjector(getProjector());
	}

	double getStoneRadius() {
		return getBoardLength(canvas) / (19 + 1) / 2;
	}

	private CoordProjector getProjector() {
		return new CoordProjector(getBoardLength(canvas), getTopLeftCorner(canvas));
	}

	private void onCanvasResize(Observable observable) {
		draw();
		stoneDrawer.setProjector(getProjector());
		stoneDrawer.setRadius(getStoneRadius());
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
		drawer.setProjector(projector);
		drawer.setRadius(context.getLineWidth() * 4);

		drawer.drawStones(getHandicapStones(9), BLACK);
	}

	public void drawGhostStone(DrawCoords position, StoneColour colour) {
		CoordProjector projector = new CoordProjector(getBoardLength(canvas), getTopLeftCorner(canvas));
		Coords boardPos = projector.nearestCoords(position);
		DrawCoords pos = projector.fromBoardCoords(boardPos);

		if ( game.isLegalMove(play(boardPos, colour)) ) {
			stoneDrawer.drawGhostStone(pos, colour);
		}
	}

	private void drawCircle(DrawCoords position, double radius) {
		double left = position.getX() - radius;
		double top = position.getY() - radius;
		double diameter = 2 * radius;

		getGraphicsContext().fillOval(left, top, diameter, diameter);
	}
}
