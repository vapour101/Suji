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
import util.Coords;
import util.Move;
import util.StoneColour;

import java.util.LinkedList;
import java.util.List;

import static util.Coords.fromSGFString;
import static util.Move.pass;
import static util.Move.play;
import static util.StoneColour.fromString;

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

	public static GameTreeBuilder getBuilder() {
		return new Builder();
	}

	private static class Builder implements GameTreeBuilder {

		private TreeNode root;
		private TreeNode current;

		private Builder() {
			root = new TreeNode();
			current = root;
		}

		@Override
		public GameTree build() {
			GameTree result = new ComplexTree(root);

			while (result.getNumChildren() > 0)
				result.stepForward(0);

			return result;
		}

		@Override
		public void gotoRoot() {
			current = root;
		}

		@Override
		public TreeNode getRoot() {
			return root;
		}

		@Override
		public void addVariation(GameTreeBuilder subtree) {
			current.addChild(subtree.getRoot());
		}

		@Override
		public void appendNode() {
			TreeNode node = new TreeNode();
			current.addChild(node);
			current = node;
		}

		@Override
		public void appendProperty(GameTreeProperty property) {
			String identifier = property.getIdentifier();
			if ( identifier.equals("B") || identifier.equals("W") ) {
				StoneColour colour = fromString(identifier);

				if ( property.getValues().isEmpty() )
					current.setMove(pass(colour));
				else {
					Coords coords = fromSGFString(property.getValues().firstElement());

					current.setMove(play(coords, colour));
				}
			}
		}
	}

	@Override
	public boolean isRoot() {
		return current.isRoot();
	}

	@Override
	public TreeNode getRoot() {
		return root;
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
