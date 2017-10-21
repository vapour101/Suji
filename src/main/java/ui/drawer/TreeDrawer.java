package ui.drawer;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import javafx.scene.canvas.Canvas;
import javafx.scene.paint.Paint;
import javafx.util.Pair;
import logic.gametree.GameTree;
import logic.gametree.TreeNode;
import util.DrawCoords;
import util.StoneColour;

import java.util.HashMap;
import java.util.Map;

public class TreeDrawer {

	private Canvas canvas;
	private Map<Pair<Integer, Integer>, TreeDrawerNode> nodeMap;
	private Multimap<Pair<Integer, Integer>, Pair<Integer, Integer>> edges;
	private TreeDrawerNode last;

	private double size = 10;
	private double spacing = 10;

	public TreeDrawer(Canvas output) {
		canvas = output;
		nodeMap = new HashMap<>();
		edges = HashMultimap.create();
	}

	public void updateModel(GameTree tree) {
		nodeMap.clear();
		edges.clear();
		last = null;

		tree.getRoot().preorder(this::modelEnter, this::modelExit);
	}

	private void modelEnter(TreeNode treeNode) {
		if ( treeNode.isRoot() ) {
			last = new TreeDrawerNode(treeNode);
			return;
		}

		TreeDrawerNode current = new TreeDrawerNode(treeNode, last);

		if ( current.getY() == last.getY() || current.getY() == last.getY() + 1 ) {
			insertEdge(last, current);
		}
		else {
			int x = current.getX() - 1;
			int y = current.getY() - 1;

			TreeDrawerNode invisible = new TreeDrawerNode(x, y);
			insertEdge(invisible, current);
			insertEdge(last, invisible);
		}

		last = current;
	}

	private void insertEdge(TreeDrawerNode left, TreeDrawerNode right) {
		edges.put(left.coords, right.coords);
	}

	private void modelExit(TreeNode treeNode) {
		if ( treeNode.isRoot() ) {
			last = null;
			return;
		}

		last = last.getParent();
	}

	public void draw(StoneDrawer drawer) {
		drawEdges();

		for (TreeDrawerNode node : nodeMap.values())
			node.draw(drawer);
	}

	private void drawEdges() {
		for (Pair<Integer, Integer> start : edges.keySet()) {
			double sX = getDrawX(start);
			double sY = getDrawY(start);

			for (Pair<Integer, Integer> end : edges.get(start)) {
				double eX = getDrawX(end);
				double eY = getDrawY(end);

				canvas.getGraphicsContext2D().save();

				canvas.getGraphicsContext2D().translate(0.5, 0.5);
				canvas.getGraphicsContext2D().setStroke(Paint.valueOf("#000000"));
				canvas.getGraphicsContext2D().strokeLine(sX, sY, eX, eY);

				canvas.getGraphicsContext2D().restore();
			}
		}
	}

	private double getDrawX(Pair<Integer, Integer> coords) {
		return drawTransform(coords.getKey());
	}

	private double drawTransform(int val) {
		return (val * (size + spacing) + spacing);
	}

	private double getDrawY(Pair<Integer, Integer> coords) {
		return drawTransform(coords.getValue());
	}

	private DrawCoords getDrawCoords(Pair<Integer, Integer> coords) {
		double x = getDrawX(coords);
		double y = getDrawY(coords);

		return new DrawCoords(x, y);
	}

	private class TreeDrawerNode {

		private TreeNode node;
		private Pair<Integer, Integer> coords;
		private TreeDrawerNode parent;

		TreeDrawerNode(int x, int y) {
			setup(null, x, y);
		}

		private void setup(TreeNode treeNode, int x, int y) {
			node = treeNode;
			coords = new Pair<>(x, y);
			parent = null;
			addToMap();
		}

		private void addToMap() {
			nodeMap.put(coords, this);
		}

		TreeDrawerNode(TreeNode root) {
			setup(root, 0, 0);
		}

		TreeDrawerNode(TreeNode treeNode, TreeDrawerNode parentDrawer) {
			Pair<Integer, Integer> trialCoords = new Pair<>(treeNode.getDepth(), parentDrawer.getY());

			while (nodeMap.containsKey(trialCoords)) {
				trialCoords = new Pair<>(trialCoords.getKey(), trialCoords.getValue() + 1);
			}

			setup(treeNode, trialCoords.getKey(), trialCoords.getValue());
			parent = parentDrawer;
		}

		int getY() {
			return coords.getValue();
		}

		int getX() {
			return coords.getKey();
		}

		TreeDrawerNode getParent() {
			return parent;
		}

		void draw(StoneDrawer drawer) {
			if ( node == null )
				return;

			if ( node.hasMove() ) {
				StoneColour colour = node.getMove().getPlayer();
				DrawCoords drawCoords = getDrawCoords(coords);

				drawer.draw(drawCoords, colour);
			}
		}
	}
}
