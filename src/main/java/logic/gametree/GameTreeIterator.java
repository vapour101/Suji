package logic.gametree;

import logic.board.Board;
import util.Move;

import java.util.List;
import java.util.function.Consumer;

public interface GameTreeIterator {

	/**
	 * @return Whether the iterator is at the root node.
	 */
	boolean isRoot();

	/**
	 * @return The number of children to the current node pointed to by the GameTree's
	 * internal iterator.
	 */
	int getNumChildren();

	void stepForward();

	void addProperty(GameTreeBuilder.GameTreeProperty property);

	void preorder(Consumer<TreeNode> enterNode, Consumer<TreeNode> exitNode);

	/**
	 * Move the iterator to a pre-existing child node.
	 *
	 * @param child The ordinal value of the child to move the iterator to.
	 */
	void stepForward(int child);

	/**
	 * Move the iterator to the child matching the argument, if no such child exists, create it.
	 *
	 * @param move The next move.
	 */
	void stepForward(Move move);

	/**
	 * Move the iterator to the current node's parent. If we're at the root node, do nothing.
	 */
	void stepBack();

	/**
	 * @return The last move played in the tree, if the current node's parent doesn't correspond
	 * to a move, keep searching until we find one, if no moves have been played in the current
	 * sequence, return null.
	 */
	Move getLastMove();

	/**
	 * @return The number of moves played in the current sequence.
	 */
	int getNumMoves();

	List<Move> getSequence();

	/**
	 * @return The board corresponding to the current sequence pointed to by the iterator.
	 */
	Board getPosition();

	/**
	 * @return The last board position, as if we called stepBack() followed by getPosition(), but
	 * without moving the iterator.
	 */
	Board getLastPosition();
}
