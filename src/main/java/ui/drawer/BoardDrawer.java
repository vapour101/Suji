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

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import logic.Board;
import logic.GameHandler;
import util.CoordProjector;
import util.Coords;
import util.DrawCoords;
import util.StoneColour;

import java.util.Collection;

import static util.Coords.getCoords;
import static util.DimensionHelper.getBoardLength;
import static util.DimensionHelper.getTopLeftCorner;
import static util.HandicapHelper.getHandicapStones;

public class BoardDrawer {

	private Canvas canvas;

	public BoardDrawer(Canvas canvas) {
		this.canvas = canvas;
	}

	public void draw(GameHandler board) {
		drawBackground();
		drawBoardTexture();
		drawBoardLines();
		drawStones(board.getBoard());
	}

	public void drawGhostStone(GameHandler board, DrawCoords position, StoneColour colour) {
		double radius = getStoneRadius();
		CoordProjector projector = new CoordProjector(getBoardLength(canvas), getTopLeftCorner(canvas));
		GraphicsContext context = getGraphicsContext();
		Coords boardPos = projector.nearestCoords(position);
		DrawCoords pos = projector.fromBoardCoords(boardPos);

		context.setGlobalAlpha(0.5);

		if ( board.isLegalMove(boardPos, colour) ) {
			drawStoneToCanvas(pos, radius, colour);
		}

		context.setGlobalAlpha(1);
	}

	double getStoneRadius() {
		return getBoardLength(canvas) / (19 + 1) / 2;
	}

	GraphicsContext getGraphicsContext() {
		return canvas.getGraphicsContext2D();
	}

	void drawStoneToCanvas(DrawCoords position, double radius, StoneColour colour) {
		GraphicsContext context = getGraphicsContext();

		if ( colour == StoneColour.BLACK )
			context.setFill(Paint.valueOf("#000000"));
		else
			context.setFill(Paint.valueOf("#FFFFFF"));

		drawCircle(position, radius);
	}

	private void drawCircle(DrawCoords position, double radius) {
		double left = position.getX() - radius;
		double top = position.getY() - radius;
		double diameter = 2 * radius;

		getGraphicsContext().fillOval(left, top, diameter, diameter);
	}

	private void drawStones(Board board) {
		for (StoneColour colour : StoneColour.values())
			drawStones(board, colour);
	}

	void drawStones(Board board, StoneColour colour) {
		double radius = getStoneRadius();

		Collection<Coords> stones = board.getStones(colour);
		drawStonesToCanvas(stones, radius, colour);
	}

	CoordProjector getProjector() {
		return new CoordProjector(getBoardLength(canvas), getTopLeftCorner(canvas));
	}

	void drawStonesToCanvas(Collection<Coords> stones, double radius, StoneColour colour) {
		for (Coords stone : stones) {
			drawStoneToCanvas(getProjector().fromBoardCoords(stone), radius, colour);
		}
	}

	private void drawBackground() {
		GraphicsContext context = getGraphicsContext();

		context.setFill(Color.GREEN);
		context.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
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

		for (Coords c : getHandicapStones(9)) {
			DrawCoords star = projector.fromBoardCoords(c);
			double radius = context.getLineWidth() * 4;

			context.setFill(Paint.valueOf("#000000"));
			drawCircle(star, radius);
		}
	}
}
