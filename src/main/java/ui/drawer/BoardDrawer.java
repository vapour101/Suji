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
import util.CoordProjector;
import util.Coords;
import util.DrawCoords;
import util.StoneColour;

import java.util.Collection;
import java.util.Set;

import static util.Coords.getCoords;
import static util.HandicapHelper.getHandicapStones;

public class BoardDrawer {

	private Canvas canvas;

	public BoardDrawer(Canvas canvas) {
		this.canvas = canvas;
	}

	public void draw(Board board) {
		drawBackground();
		drawBoardTexture();
		drawBoardLines();
		drawStones(board);
	}

	public void drawGhostStone(Board board, DrawCoords position, StoneColour colour) {
		double radius = getStoneRadius();
		CoordProjector projector = new CoordProjector(getBoardLength(), getTopLeftCorner());
		GraphicsContext context = getGraphicsContext();
		Coords boardPos = projector.nearestCoords(position);
		DrawCoords pos = projector.fromBoardCoords(boardPos);

		context.setGlobalAlpha(0.5);

		boolean blackLegalMove = colour == StoneColour.BLACK && board.isLegalBlackMove(boardPos);
		boolean whiteLegalMove = colour == StoneColour.WHITE && board.isLegalWhiteMove(boardPos);

		if ( blackLegalMove || whiteLegalMove ) {
			drawStone(pos, radius, colour);
		}

		context.setGlobalAlpha(1);
	}

	double getStoneRadius() {
		return getBoardLength() / (19 + 1) / 2;
	}

	private double getBoardLength() {
		double canvasWidth = canvas.getWidth();
		double canvasHeight = canvas.getHeight();

		return Math.min(canvasHeight, canvasWidth);
	}

	private DrawCoords getTopLeftCorner() {
		double length = getBoardLength();
		double canvasWidth = canvas.getWidth();
		double canvasHeight = canvas.getHeight();

		double x = 0;
		double y = 0;

		if ( canvasWidth > length )
			x = (canvasWidth - length) / 2;
		else
			y = (canvasHeight - length) / 2;

		return new DrawCoords(x, y);
	}

	GraphicsContext getGraphicsContext() {
		return canvas.getGraphicsContext2D();
	}

	void drawStone(DrawCoords position, double radius, StoneColour colour) {
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

	void drawStones(Board board) {
		double radius = getStoneRadius();

		Set<Coords> blackStones = board.getBlackStones();
		Set<Coords> whiteStones = board.getWhiteStones();

		drawStones(blackStones, radius, StoneColour.BLACK);
		drawStones(whiteStones, radius, StoneColour.WHITE);
	}

	CoordProjector getProjector() {
		return new CoordProjector(getBoardLength(), getTopLeftCorner());
	}

	void drawStones(Collection<Coords> stones, double radius, StoneColour colour) {
		for (Coords stone : stones) {
			drawStone(getProjector().fromBoardCoords(stone), radius, colour);
		}
	}

	private void drawBackground() {
		GraphicsContext context = getGraphicsContext();

		context.setFill(Color.GREEN);
		context.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
	}

	private void drawBoardTexture() {
		DrawCoords topLeft = getTopLeftCorner();
		GraphicsContext context = getGraphicsContext();
		double length = getBoardLength();

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
