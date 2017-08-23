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

package ui;

import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import logic.Board;
import logic.BoardScorer;
import util.CoordProjector;
import util.Coords;
import util.DrawCoords;
import util.HandicapHelper;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.Set;

import static util.Coords.getCoords;
import static util.HandicapHelper.getHandicapStones;

public class BoardController implements Initializable {

	public Button passButton;
	public Label blackScore;
	public Label whiteScore;
	public Pane pane;
	public Pane scorePane;
	public Button blackDone;
	public Button whiteDone;

	private Canvas boardCanvas;
	private Board board;
	private BoardScorer scorer;

	private boolean blackMove;
	private boolean pass;
	private boolean scoring;
	private boolean playing;
	private double komi;

	public BoardController() {
		board = new Board();
		blackMove = true;
		pass = false;
		scoring = false;
		playing = true;
		komi = 0;
	}

	void setHandicap(int handicap) {
		if ( board.getBlackStones().size() > 0 || board.getWhiteStones().size() > 0 )
			board = new Board();

		blackMove = (handicap == 0);

		if ( handicap > 0 )
			for (Coords stone : HandicapHelper.getHandicapStones(handicap))
				board.playBlackStone(stone);
	}

	void setKomi(double komi) {
		this.komi = komi;
	}

	private void constructCanvas() {
		boardCanvas = new Canvas();
		boardCanvas.setOnMouseMoved(this::canvasHover);
		boardCanvas.setOnMouseClicked(this::canvasClicked);
		pane.getChildren().add(boardCanvas);
	}

	private void canvasClicked(MouseEvent mouseEvent) {
		DrawCoords mousePosition = new DrawCoords(mouseEvent.getX(), mouseEvent.getY());
		CoordProjector projector = new CoordProjector(getBoardLength(), getTopLeftCorner());
		Coords boardPos = projector.nearestCoords(mousePosition);

		if ( scoring ) {
			blackDone.setDisable(false);
			whiteDone.setDisable(false);
			if ( mouseEvent.getButton() == MouseButton.PRIMARY )
				scorer.markGroupDead(boardPos);
			else if ( mouseEvent.getButton() == MouseButton.SECONDARY )
				scorer.unmarkGroupDead(boardPos);
		}
		else if ( playing ) {
			if ( blackMove && board.isLegalBlackMove(boardPos) ) {
				board.playBlackStone(boardPos);
				blackMove = !blackMove;
				pass = false;
			}
			else if ( !blackMove && board.isLegalWhiteMove(boardPos) ) {
				board.playWhiteStone(boardPos);
				blackMove = !blackMove;
				pass = false;
			}
		}

		drawBoard();
	}

	private void canvasHover(MouseEvent mouseEvent) {
		if ( scoring || !playing )
			return;

		DrawCoords mousePosition = new DrawCoords(mouseEvent.getX(), mouseEvent.getY());
		drawBoard();

		double radius = getStoneRadius();
		CoordProjector projector = new CoordProjector(getBoardLength(), getTopLeftCorner());
		GraphicsContext context = boardCanvas.getGraphicsContext2D();
		Coords boardPos = projector.nearestCoords(mousePosition);
		DrawCoords pos = projector.fromBoardCoords(boardPos);

		context.setGlobalAlpha(0.5);

		if ( blackMove && board.isLegalBlackMove(boardPos) ) {
			drawBlackStone(context, pos, radius);
		}
		else if ( !blackMove && board.isLegalWhiteMove(boardPos) ) {
			drawWhiteStone(context, pos, radius);
		}

		context.setGlobalAlpha(1);
	}

	private void drawBoard() {
		drawBackground();
		drawBoardTexture();
		drawBoardLines();
		drawStones();

		if ( scoring ) {
			drawTerritory();
			blackScore.setText(Double.toString(scorer.getBlackScore()));
			whiteScore.setText(Double.toString(scorer.getWhiteScore()));
		}
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

		for (Coords c : getHandicapStones(9)) {
			DrawCoords star = projector.fromBoardCoords(c);
			double radius = context.getLineWidth() * 4;

			context.setFill(Paint.valueOf("#000000"));
			drawCircle(context, star, radius);
		}
	}

	private void drawTerritory() {
		double radius = getStoneRadius() / 2;
		DrawCoords offset = new DrawCoords(0, 0);
		CoordProjector projector = new CoordProjector(getBoardLength(), getTopLeftCorner());
		GraphicsContext context = boardCanvas.getGraphicsContext2D();

		for (Coords stone : scorer.getBlackTerritory()) {
			DrawCoords pos = projector.fromBoardCoords(stone);
			pos.applyOffset(offset);
			drawBlackStone(context, pos, radius);
		}
		for (Coords stone : scorer.getWhiteTerritory()) {
			DrawCoords pos = projector.fromBoardCoords(stone);
			pos.applyOffset(offset);
			drawWhiteStone(context, pos, radius);
		}
	}

	private void drawStones() {
		double radius = getStoneRadius();
		CoordProjector projector = new CoordProjector(getBoardLength(), getTopLeftCorner());
		GraphicsContext context = boardCanvas.getGraphicsContext2D();

		Set<Coords> blackStones = board.getBlackStones();
		Set<Coords> whiteStones = board.getWhiteStones();

		if ( scoring ) {
			blackStones.removeAll(scorer.getDeadBlackStones());
			whiteStones.removeAll(scorer.getDeadWhiteStones());

			context.setGlobalAlpha(0.5);

			drawBlackStones(context, projector, scorer.getDeadBlackStones(), radius);
			drawWhiteStones(context, projector, scorer.getDeadWhiteStones(), radius);

			context.setGlobalAlpha(1);
		}

		drawBlackStones(context, projector, blackStones, radius);
		drawWhiteStones(context, projector, whiteStones, radius);
	}

	private void drawBlackStones(GraphicsContext context, CoordProjector projector, Set<Coords> stones, double
																												radius) {
		for (Coords stone : stones) {
			drawBlackStone(context, projector.fromBoardCoords(stone), radius);
		}
	}

	private void drawWhiteStones(GraphicsContext context, CoordProjector projector, Set<Coords> stones, double
																												radius) {
		for (Coords stone : stones) {
			drawWhiteStone(context, projector.fromBoardCoords(stone), radius);
		}
	}

	private void drawBlackStone(GraphicsContext context, DrawCoords pos, double radius) {
		context.setFill(Paint.valueOf("#000000"));
		drawCircle(context, pos, radius);
	}

	private void drawWhiteStone(GraphicsContext context, DrawCoords pos, double radius) {
		context.setFill(Paint.valueOf("#FFFFFF"));
		drawCircle(context, pos, radius);
	}

	private void drawCircle(GraphicsContext context, DrawCoords pos, double radius) {
		context.fillOval(pos.getX() - radius, pos.getY() - radius, 2 * radius, 2 * radius);
	}

	private double getBoardLength() {
		double canvasWidth = boardCanvas.getWidth();
		double canvasHeight = boardCanvas.getHeight();

		return Math.min(canvasHeight, canvasWidth);
	}

	private double getStoneRadius() {
		return getBoardLength() / (19 + 1) / 2;
	}

	@Override
	public void initialize(URL url, ResourceBundle resourceBundle) {
		pane.widthProperty().addListener(this::resizeCanvas);
		pane.heightProperty().addListener(this::resizeCanvas);
		constructCanvas();
		scorePane.setVisible(false);
		passButton.setOnAction(this::pass);

		blackDone.setOnAction(event -> {
			blackDone.setDisable(true);
			if ( whiteDone.isDisabled() )
				doneScoring();
		});

		whiteDone.setOnAction(event -> {
			whiteDone.setDisable(true);
			if ( blackDone.isDisabled() )
				doneScoring();
		});

		scorePane.widthProperty().addListener(this::resizeScore);

		drawBoard();
	}

	private void resizeScore(ObservableValue<? extends Number> observableValue, Number number, Number t1) {
		VBox blackScoreBox = null;
		Separator separator = null;

		for (Node node : scorePane.getChildren()) {
			if ( node instanceof Separator )
				separator = (Separator) node;
		}

		if ( separator == null )
			return;

		for (Node node : scorePane.getChildren()) {
			if ( node instanceof VBox && node.getLayoutX() < separator.getLayoutX() )
				blackScoreBox = (VBox) node;
		}

		if ( blackScoreBox == null )
			return;

		double width = (scorePane.getWidth() - separator.getWidth()) / 2;

		blackScoreBox.setMinWidth(width);
	}

	private void resizeCanvas(ObservableValue<? extends Number> observableValue, Number number, Number t1) {
		boardCanvas.setHeight(pane.getHeight());
		boardCanvas.setWidth(pane.getWidth());
		drawBoard();
	}

	private void doneScoring() {
		scoring = false;
		playing = false;

		Alert alert = new Alert(Alert.AlertType.INFORMATION);
		alert.setTitle("Game Over");

		double finalScore = scorer.getScore();

		if ( finalScore == 0 ) {
			alert.setContentText("Game ends in a draw.");
			alert.showAndWait();
			return;
		}

		String message;

		if ( finalScore > 0 )
			message = "Black";
		else
			message = "White";

		finalScore = Math.abs(finalScore);

		message += " wins by " + Double.toString(finalScore) + " points.";

		alert.setContentText(message);
		alert.showAndWait();
	}

	private void pass(ActionEvent event) {
		blackMove = !blackMove;

		if ( pass ) {
			playing = false;
			scoring = true;
			scorer = new BoardScorer(board, komi);
			passButton.setVisible(false);
			scorePane.setVisible(true);

			drawBoard();
		}

		pass = true;
	}
}