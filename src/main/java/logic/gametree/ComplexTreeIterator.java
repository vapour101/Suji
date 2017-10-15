package logic.gametree;

import logic.board.Board;
import util.Coords;
import util.Move;
import util.StoneColour;

import java.util.LinkedList;
import java.util.List;
import java.util.Vector;
import java.util.function.Consumer;

public class ComplexTreeIterator implements GameTreeIterator {

	private TreeNode node;

	protected ComplexTreeIterator(TreeNode treeNode) {
		node = treeNode;
	}

	@Override
	public boolean isRoot() {
		return node.isRoot();
	}

	@Override
	public int getNumChildren() {
		return node.getChildren().size();
	}

	@Override
	public void stepForward() {
		TreeNode child = new TreeNode(node);
		node = child;
	}

	@Override
	public void addProperty(GameTreeBuilder.GameTreeProperty property) {
		String identifier = property.getIdentifier();

		if ( identifier.equals("B") || identifier.equals("W") ) {
			System.out.println("Here");
			StoneColour colour = StoneColour.fromString(identifier);
			String value = property.getValues().firstElement();
			Move move = Move.pass(colour);

			if ( !value.equals("") ) {
				Coords coords = Coords.fromSGFString(value);
				move = Move.play(coords, colour);
			}

			node.setMove(move);
		}
	}

	@Override
	public void preorder(Consumer<TreeNode> enterNode, Consumer<TreeNode> exitNode) {
		node.preorder(enterNode, exitNode);
	}

	@Override
	public void stepForward(int child) {
		Vector<TreeNode> children = node.getChildren();

		if ( child >= children.size() )
			node = children.get(child);
	}

	@Override
	public void stepForward(Move move) {
		TreeNode child = node.getChildMatching(treeNode -> treeNode.hasMove() && treeNode.getMove().equals(move));

		if ( child == null ) {
			child = new TreeNode(node);
			child.setMove(move);
		}

		node = child;
	}

	@Override
	public void stepBack() {
		if ( node.isRoot() )
			return;

		node = node.getParent();
	}

	@Override
	public Move getLastMove() {
		TreeNode search = node;

		while (!search.hasMove() && search.getParent() != null) {
			search = search.getParent();
		}

		return search.getMove();
	}

	@Override
	public int getNumMoves() {
		TreeNode search = node;
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
		return getSequenceAt(node);
	}

	private List<Move> getSequenceAt(TreeNode point) {
		LinkedList<Move> result = new LinkedList<>();

		point.backtrack(treeNode -> {
			if ( treeNode.hasMove() )
				result.addFirst(treeNode.getMove());
		});

		return result;
	}

	private Board getPositionFromSequence(List<Move> sequence) {
		Board position = new Board();

		for (Move m : sequence)
			if ( m.getType() == Move.Type.PLAY )
				position.playStone(m);

		return position;
	}


	@Override
	public Board getPosition() {
		return getPositionFromSequence(getSequence());
	}

	@Override
	public Board getLastPosition() {
		if ( isRoot() )
			return new Board();

		return getPositionFromSequence(getSequenceAt(node.getParent()));
	}
}
