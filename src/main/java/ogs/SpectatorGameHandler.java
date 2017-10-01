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

package ogs;

import event.EventHelper;
import event.GameEvent;
import event.SujiEvent;
import javafx.event.EventDispatchChain;
import javafx.event.EventHandler;
import javafx.event.EventType;
import logic.board.Board;
import logic.gamehandler.GameHandler;
import logic.gametree.ComplexGameTree;
import logic.gametree.GameTree;
import logic.score.Scorer;
import sgf.SGFWriter;
import util.*;

import java.util.Collection;
import java.util.List;

public class SpectatorGameHandler implements GameHandler {

	private StoneColour initialPlayer;
	private GameTree gameTree;
	private int gameId;
	private EventHelper publisher;

	public SpectatorGameHandler(int gameId) {
		this.gameId = gameId;
		initialPlayer = StoneColour.BLACK;
		gameTree = new ComplexGameTree();
		Connection.connectToGame(gameId, this::onGameData, this::onMovedata);
		publisher = new EventHelper(this);
	}

	public void disconnect() {
		Connection.disconnectGame(gameId);
	}

	private void onGameData(Gamedata gamedata) {
		if ( gamedata == null ) {
			LogHelper.severe("Gamedata is null");
			return;
		}

		gameTree = new ComplexGameTree();

		initialPlayer = gamedata.getInitialPlayer();
		int handicap = gamedata.getHandicap();

		for (Coords c : HandicapHelper.getHandicapStones(handicap)) {
			gameTree.stepForward(Move.play(c, StoneColour.BLACK));
		}

		List<Movedata> moveList = gamedata.getMoves();

		for (Movedata move : moveList) {
			gameTree.stepForward(move.getMove(getTurnPlayer()));
		}

		fireGameEvent(GameEvent.MOVE);
	}

	private void fireGameEvent(EventType<? extends GameEvent> type) {
		GameEvent event = new GameEvent(this, this, type);
		fireEvent(event);
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

	private void onMovedata(Movedata move) {
		if ( gameTree == null ) {
			LogHelper.severe("gameTree is null");
			return;
		}

		gameTree.stepForward(move.getMove(getTurnPlayer()));
		fireGameEvent(GameEvent.MOVE);
	}

	@Override
	public void pass() {

	}

	@Override
	public void undo() {

	}

	@Override
	public StoneColour getTurnPlayer() {
		if ( gameTree.getNumMoves() == 0 )
			return initialPlayer;

		return gameTree.getLastMove().getPlayer().other();
	}

	@Override
	public void setKomi(double komi) {

	}

	@Override
	public EventDispatchChain buildEventDispatchChain(EventDispatchChain tail) {
		return publisher.buildEventDispatchChain(tail);
	}

	@Override
	public GameTree getGameTree() {
		return gameTree;
	}

	@Override
	public SGFWriter getSGFWriter() {
		return null;
	}

	@Override
	public Scorer getScorer() {
		return null;
	}

	@Override
	public Board getBoard() {
		return gameTree.getPosition();
	}

	@Override
	public boolean isLegalMove(Move move) {
		return false;
	}

	@Override
	public void playMove(Move move) {

	}

	@Override
	public Collection<Coords> getStones(StoneColour colour) {
		return getBoard().getStones(colour);
	}
}
