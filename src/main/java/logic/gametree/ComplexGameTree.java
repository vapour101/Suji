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

package logic.gametree;

import logic.board.Board;
import util.Move;

import java.util.LinkedList;
import java.util.List;

public class ComplexGameTree implements GameTree {

	private TreeNode root;
	private TreeNode current;

	public ComplexGameTree() {
		root = new TreeNode();
		current = root;
	}


	@Override
	public boolean isRoot() {
		return current == root;
	}

	@Override
	public int getNumChildren() {
		return current.getChildren().size();
	}

	@Override
	public void stepForward(int child) {
		if ( child >= getNumChildren() )
			return;

		current = current.getChildren().get(child);
	}

	@Override
	public void stepForward(Move move) {
		for (TreeNode node : current.getChildren()) {
			if ( node.hasMove() && node.getMove().equals(move) ) {
				current = node;
				return;
			}
		}

		TreeNode node = new TreeNode();
		node.setMove(move);
		current.addChild(node);
		current = node;
	}

	@Override
	public void stepBack() {
		if ( isRoot() )
			return;

		current = current.getParent();
	}

	@Override
	public Move getLastMove() {
		TreeNode search = current;

		while (!search.hasMove() && search.getParent() != null) {
			search = search.getParent();
		}

		return search.getMove();
	}

	@Override
	public int getNumMoves() {
		TreeNode search = current;
		int count = 0;

		while (search != null) {
			if ( search.hasMove() )
				count++;

			search = search.getParent();
		}

		return count;
	}

	@Override
	public List<Move> getSequence() {
		return getSequenceAt(current);
	}

	private List<Move> getSequenceAt(TreeNode node) {
		LinkedList<Move> sequence = new LinkedList<>();

		for (TreeNode search = node; search != null; search = search.getParent()) {
			if ( search.hasMove() )
				sequence.addFirst(search.getMove());
		}

		return sequence;
	}

	private Board getPositionAt(TreeNode node) {
		Board position = new Board();

		for (Move m : getSequenceAt(node))
			if ( m.getType() == Move.Type.PLAY )
				position.playStone(m.getPosition(), m.getPlayer());

		return position;
	}

	@Override
	public Board getPosition() {
		return getPositionAt(current);
	}

	@Override
	public Board getLastPosition() {
		if ( isRoot() )
			return new Board();

		return getPositionAt(current.getParent());
	}
}
