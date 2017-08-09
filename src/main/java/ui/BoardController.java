/*
 * Copyright (C) 2017 Vincent Varkevisser
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

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.util.Pair;

public class BoardController {
    public Canvas boardCanvas;
    public Pane pane;

    public void canvasClicked(MouseEvent mouseEvent) {
        boardCanvas.setHeight(pane.getHeight());
        boardCanvas.setWidth(pane.getWidth());
        drawBoard();

    }

    public void drawBoard() {
        Pair<Double, Double> topLeft = getTopLeftCorner();
        double length = getBoardLength();

        GraphicsContext context = boardCanvas.getGraphicsContext2D();

        context.setFill(Color.GREEN);
        context.fillRect(0, 0, boardCanvas.getWidth(), boardCanvas.getHeight());

        context.setFill(Color.web("0xB78600"));
        context.fillRect(topLeft.getKey(), topLeft.getValue(), length, length);
    }

    private double getBoardLength() {
        double canvasWidth = boardCanvas.getWidth();
        double canvasHeight = boardCanvas.getHeight();

        return Math.min(canvasHeight, canvasWidth);
    }

    private Pair<Double, Double> getTopLeftCorner() {
        double length = getBoardLength();
        double canvasWidth = boardCanvas.getWidth();
        double canvasHeight = boardCanvas.getHeight();

        double x = 0;
        double y = 0;

        if (canvasWidth > length)
            x = (canvasWidth - length) / 2;
        else
            y = (canvasHeight - length) / 2;

        return new Pair<>(x, y);
    }
}