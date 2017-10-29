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

import java.util.List;

public class ComplexTree implements GameTree {

	private TreeNode root;
	private GameTreeIterator current;

	public ComplexTree() {
		this(new TreeNode());
	}

	private ComplexTree(TreeNode rootNode) {
		root = rootNode;
		current = new ComplexTreeIterator(root);
	}

	@Override
	public boolean isRoot() {
		return current.isRoot();
	}

	@Override
	public GameTreeIterator getRoot() {
		return new ComplexTreeIterator(root);
	}

	@Override
	public int getNumChildren() {
		return current.getNumChildren();
	}

	@Override
	public void stepForward(int child) {
		current.stepForward(child);
	}

	@Override
	public void stepForward(Move move) {
		current.stepForward(move);
	}

	@Override
	public void stepBack() {
		current.stepBack();
	}

	@Override
	public Move getLastMove() {
		return current.getLastMove();
	}

	@Override
	public int getNumMoves() {
		return current.getNumMoves();
	}

	@Override
	public List<Move> getSequence() {
		return getSequenceAt(current);
	}

	private List<Move> getSequenceAt(GameTreeIterator node) {
		return node.getSequence();
	}

	@Override
	public Board getPosition() {
		return current.getPosition();
	}

	@Override
	public Board getLastPosition() {
		return current.getLastPosition();
	}
}
