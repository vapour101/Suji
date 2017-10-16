package logic.gametree;

import util.Move;

import java.util.LinkedList;
import java.util.List;
import java.util.Vector;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class TreeNode {

	//private proplist
	private Move move;

	private TreeNode parent;
	private TreeNode firstChild;
	private TreeNode nextSibling;
	private TreeNode prevSibling;

	private Integer depth;
	private Integer width;

	protected TreeNode() {
		parent = null;
		firstChild = null;
		nextSibling = null;
		prevSibling = null;
		move = null;

		depth = null;
		width = null;
	}

	public boolean hasMove() {
		return move != null;
	}

	public Move getMove() {
		return move;
	}

	public void setMove(Move move) {
		this.move = move;
	}

	protected TreeNode(TreeNode parent) {
		this();

		parent.addChild(this);
	}

	public void addChild(TreeNode child) {
		child.parent = this;

		if ( hasChild() )
			firstChild.addSibling(child);
		else
			firstChild = child;
	}

	public void addSibling(TreeNode sibling) {
		if ( hasNextSibling() )
			nextSibling.addSibling(sibling);
		else {
			sibling.prevSibling = this;
			nextSibling = sibling;
		}
	}

	public List<TreeNode> getSequence() {
		LinkedList<TreeNode> sequence = new LinkedList<>();

		backtrack(sequence::addFirst);

		return sequence;
	}

	public boolean hasChild() {
		return firstChild != null;
	}

	private boolean hasPrevSibling() {
		return prevSibling != null;
	}

	private boolean hasNextSibling() {
		return nextSibling != null;
	}

	private boolean hasParent() {
		return parent != null;
	}

	public boolean isRoot() {
		return !hasParent();
	}

	public int getWidth() {
		if ( width == null )
			width = calculateWidth();

		return width;
	}

	public int getDepth() {
		if ( depth == null )
			depth = calculateDepth();

		return depth;
	}

	public TreeNode getParent() {
		return parent;
	}

	public Vector<TreeNode> getChildren() {
		Vector<TreeNode> children = new Vector<>();

		if ( hasChild() )
			firstChild.siblingTraverse(children::add);

		return children;
	}

	private int calculateDepth() {
		if ( isRoot() )
			return 0;
		else
			return parent.getDepth() + 1;
	}

	private int calculateWidth() {
		if ( isRoot() )
			return 1;
		else if ( hasPrevSibling() )
			return prevSibling.getWidth() + 1;
		else
			return parent.getWidth();
	}

	public void preorder(Consumer<TreeNode> enterNode, Consumer<TreeNode> exitNode) {
		enterNode.accept(this);

		if ( hasChild() )
			firstChild.preorder(enterNode, exitNode);

		exitNode.accept(this);

		if ( hasNextSibling() )
			nextSibling.preorder(enterNode, exitNode);
	}

	public void backtrack(Consumer<TreeNode> visitor) {
		visitor.accept(this);

		if ( hasParent() )
			parent.backtrack(visitor);
	}

	public void siblingTraverse(Consumer<TreeNode> visitor) {
		visitor.accept(this);

		if ( hasNextSibling() )
			nextSibling.siblingTraverse(visitor);
	}

	public TreeNode getChildMatching(Predicate<TreeNode> searchFunction) {
		if ( !hasChild() )
			return null;

		return firstChild.getSiblingMatching(searchFunction);
	}

	public TreeNode getSiblingMatching(Predicate<TreeNode> searchFunction) {
		if ( searchFunction.test(this) )
			return this;

		if ( !hasNextSibling() )
			return null;

		return nextSibling.getSiblingMatching(searchFunction);
	}
}
