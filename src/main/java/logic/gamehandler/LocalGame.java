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

package logic.gamehandler;

import event.EventPublisher;
import event.GameEvent;
import event.SujiEvent;
import javafx.event.EventDispatchChain;
import javafx.event.EventHandler;
import javafx.event.EventType;
import logic.board.Board;
import logic.gametree.ComplexGameTree;
import logic.gametree.GameTree;
import logic.score.BoardScorer;
import sgf.SGFWriter;
import sgf.SimpleSGFWriter;
import util.Coords;
import util.Move;
import util.StoneColour;

import java.util.Collection;

import static event.GameEvent.*;

public class LocalGame implements GameHandler {

	private GameTree gameTree;
	private int handicap;
	private double komi;
	private EventPublisher publisher;

	LocalGame() {
		this(0);
	}

	public LocalGame(int handicap) {
		gameTree = new ComplexGameTree();
		this.handicap = handicap;
		komi = 0;
		publisher = new EventPublisher(this);
	}

	@Override
	public void pass() {
		boolean gameOver = false;
		if ( gameTree.getNumMoves() > 0 && gameTree.getLastMove().getType() == Move.Type.PASS )
			gameOver = true;

		playMove(Move.pass(getTurnPlayer()));
		fireGameEvent(PASS);

		if ( gameOver )
			fireGameEvent(GAMEOVER);
	}

	@Override
	public void undo() {
		gameTree.stepBack();
		fireGameEvent(UNDO);
	}

	@Override
	public StoneColour getTurnPlayer() {
		if ( gameTree.getNumMoves() == 0 )
			return handicap == 0 ? StoneColour.BLACK : StoneColour.WHITE;

		return gameTree.getLastMove().getPlayer().other();
	}

	@Override
	public void setKomi(double komi) {
		this.komi = komi;
	}

	@Override
	public <T extends SujiEvent> void fireEvent(T event) {
		publisher.fireEvent(event);
	}

	@Override
	public <T extends SujiEvent> void subscribe(EventType<T> eventType, EventHandler<? super T> eventHandler) {
		publisher.subscribe(eventType, eventHandler);
	}

	@Override

	public <T extends SujiEvent> void unsubscribe(EventType<T> eventType, EventHandler<? super T> eventHandler) {
		publisher.unsubscribe(eventType, eventHandler);
	}

	@Override
	public GameTree getGameTree() {
		return gameTree;
	}

	@Override
	public SGFWriter getSGFWriter() {
		return new SimpleSGFWriter(gameTree.getSequence());
	}

	@Override
	public BoardScorer getScorer() {
		return new BoardScorer(getBoard(), komi);
	}

	@Override
	public Board getBoard() {
		return gameTree.getPosition();
	}

	@Override
	public boolean isLegalMove(Move move) {
		if ( move.getType() == Move.Type.PASS )
			return true; //Passing is never illegal

		boolean isLegal;

		isLegal = !gameTree.getPosition().isOccupied(move.getPosition());
		isLegal &= !gameTree.getPosition().isSuicide(move);

		if ( isLegal && gameTree.getNumMoves() > 2 ) {
			Board previous = gameTree.getLastPosition();
			Board future = gameTree.getPosition();
			future.playStone(move);

			isLegal = !previous.equals(future);
		}

		return isLegal;
	}

	@Override
	public void playMove(Move move) {
		if ( !isLegalMove(move) )
			return;

		gameTree.stepForward(move);

		fireGameEvent(MOVE);
	}

	@Override
	public Collection<Coords> getStones(StoneColour colour) {
		return getBoard().getStones(colour);
	}

	private void fireGameEvent(EventType<? extends GameEvent> type) {
		GameEvent event = new GameEvent(this, this, type);
		fireEvent(event);
	}

	@Override
	public EventDispatchChain buildEventDispatchChain(EventDispatchChain tail) {
		return publisher.buildEventDispatchChain(tail);
	}
}
