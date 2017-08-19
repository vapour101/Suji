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

import javafx.beans.value.ChangeListener;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.util.Pair;
import logic.Board;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.shape.Rectangle;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.stage.Stage;

public class BoardController implements Initializable {
    public Canvas boardCanvas;
    public Pane pane;
    private Board board;
    public primaryStage;

    public BoardController() {
        board = new Board();
    }

    public void canvasClicked(MouseEvent mouseEvent) {
        drawBoard();
    }

    private void resizeCanvas() {
        boardCanvas.setHeight(pane.getHeight());
        boardCanvas.setWidth(pane.getWidth());
    }

    private void drawBoard() {
        drawBackground();
        drawBoardTexture(getTopLeftCorner());
        drawBoardLines();
        drawStones();
    }

    private void drawBoardTexture(Pair<Double, Double> topLeft) {
        GraphicsContext context = boardCanvas.getGraphicsContext2D();
        double length = getBoardLength();

        context.setFill(Color.web("0xB78600"));
        context.fillRect(topLeft.getKey(), topLeft.getValue(), length, length);
    }

    private void drawBackground() {
        GraphicsContext context = boardCanvas.getGraphicsContext2D();

        context.setFill(Color.GREEN);
        context.fillRect(0, 0, boardCanvas.getWidth(), boardCanvas.getHeight());
    }

    private void drawBoardLines() {

        Group root = new Group();
        double canvasWidth = boardCanvas.getWidth();
        double canvasHeight = boardCanvas.getHeight();
        Scene scene = new Scene(root, canvasWidth, canvasHeight, Color.GREEN);

        Rectangle[][] rec = new Rectangle[canvasWidth][canvasHeight];

        for(int i=0; i<canvasWidth; i++){
            for(int j=0; j<canvasHeight; j++){

                rec[i][j] = new Rectangle();
                rec[i][j].setX(i * canvasWidth);
                rec[i][j].setY(j * canvasWidth);
                rec[i][j].setWidth(canvasWidth);
                rec[i][j].setHeight(canvasWidth);
                rec[i][j].setFill(null);

                rec[i][j].setStrokeWidth(3);
                rec[i][j].setStroke(Color.BLACK);
                root.getChildren().add(rec[i][j]);
            }
        }
        primaryStage.setScene(scene);
        primaryStage.show();

    }

    private void drawStones() {

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
}