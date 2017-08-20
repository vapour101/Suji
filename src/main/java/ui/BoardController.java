/*
 * Copyright (c) 2017 Vincent Varkevisser
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

package ui;

import javafx.beans.value.ChangeListener;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import logic.Board;
import util.CoordProjector;
import util.Coords;
import util.DrawCoords;

import java.net.URL;
import java.util.ResourceBundle;

import static util.Coords.getCoords;

public class BoardController implements Initializable {

	public Canvas boardCanvas;
	public Pane pane;
	private Board board;

	public BoardController() {
		board = new Board();
	}

	public void canvasClicked(MouseEvent mouseEvent) {
		drawBoard();
	}

	private void drawBoard() {
		drawBackground();
		drawBoardTexture();
		drawBoardLines();
		drawStones();
	}

	private void drawBackground() {
		GraphicsContext context = boardCanvas.getGraphicsContext2D();

		context.setFill(Color.GREEN);
		context.fillRect(0, 0, boardCanvas.getWidth(), boardCanvas.getHeight());
	}

	private void drawBoardTexture() {
		DrawCoords topLeft = getTopLeftCorner();
		GraphicsContext context = boardCanvas.getGraphicsContext2D();
		double length = getBoardLength();

		context.setFill(Color.web("0xB78600"));
		context.fillRect(topLeft.getX(), topLeft.getY(), length, length);
	}

	private DrawCoords getTopLeftCorner() {
		double length = getBoardLength();
		double canvasWidth = boardCanvas.getWidth();
		double canvasHeight = boardCanvas.getHeight();

		double x = 0;
		double y = 0;

		if ( canvasWidth > length )
			x = (canvasWidth - length) / 2;
		else
			y = (canvasHeight - length) / 2;

		return new DrawCoords(x, y);
	}

	private void drawBoardLines() {
		CoordProjector projector = new CoordProjector(getBoardLength(), getTopLeftCorner());

		GraphicsContext context = boardCanvas.getGraphicsContext2D();

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
	}

	private void drawStones() {
		double diameter = getStoneDiameter();
		CoordProjector projector = new CoordProjector(getBoardLength(), getTopLeftCorner());
		GraphicsContext context = boardCanvas.getGraphicsContext2D();

		for (Coords stone : board.getBlackStones()) {
			context.setFill(Paint.valueOf("#000000"));
			drawStone(context, projector.fromBoardCoords(stone), diameter);
		}

		for (Coords stone : board.getWhiteStones()) {
			context.setFill(Paint.valueOf("#FFFFFF"));
			drawStone(context, projector.fromBoardCoords(stone), diameter);
		}
	}

	private void drawStone(GraphicsContext context, DrawCoords pos, double diameter) {
		context.fillOval(pos.getX() - (diameter / 2), pos.getY() - (diameter / 2), diameter, diameter);
	}

	private double getBoardLength() {
		double canvasWidth = boardCanvas.getWidth();
		double canvasHeight = boardCanvas.getHeight();

		return Math.min(canvasHeight, canvasWidth);
	}

	private double getStoneDiameter() {
		return getBoardLength() / (19 + 1);
	}

	@Override
	public void initialize(URL url, ResourceBundle resourceBundle) {
		ChangeListener<Number> paneChangeListener = (observableValue, number, t1) -> {
			resizeCanvas();
			drawBoard();
		};
		pane.widthProperty().addListener(paneChangeListener);
		pane.heightProperty().addListener(paneChangeListener);

		drawBoard();
	}

	private void resizeCanvas() {
		boardCanvas.setHeight(pane.getHeight());
		boardCanvas.setWidth(pane.getWidth());
	}
}