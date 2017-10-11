package ui.controller.sidebar;

import com.mxgraph.view.mxGraph;
import event.GameEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import logic.gamehandler.GameHandler;
import logic.gametree.TreeNode;
import logic.gametree.TreeNodeOld;
import ui.controller.SelfBuildingController;
import ui.drawer.StoneDrawer;
import ui.javafx.GameTreemxCanvas;
import util.StoneColour;

import java.net.URL;
import java.util.ResourceBundle;


public class GameTreePane extends SelfBuildingController implements Initializable {

	private StoneDrawer drawer;
	private GameTreemxCanvas canvasWrapper;
	@FXML
	private Canvas canvas;

	public GameTreePane(GameHandler game, StoneDrawer stoneDrawer) {
		game.subscribe(GameEvent.MOVE, this::onGameEvent);
		drawer = stoneDrawer;
	}

	private void onGameEvent(GameEvent event) {
		canvas.getGraphicsContext2D().clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
		mxGraph graph = constructGraph(event.getHandler().getGameTree().getRoot());
		graph.drawGraph(canvasWrapper);
	}

	private mxGraph constructGraph(TreeNode tree) {
		if ( tree == null )
			return new mxGraph();

		TreeNode node = tree;

		mxGraph graph = new mxGraph();
		Object parent = graph.getDefaultParent();

		String player = getStyle(node);
		double x = 15;
		Object last = graph.insertVertex(parent, null, node.getDepth(), x, 15, 20, 20, player);


		while (!node.getChildren().isEmpty()) {
			x += 30;
			node = node.getChildren().firstElement();

			player = getStyle(node);
			Object cur = graph.insertVertex(parent, null, player, x, 15, 20, 20, player);

			graph.insertEdge(parent, null, "", last, cur);
			last = cur;
		}

		return graph;
	}

	private String getStyle(TreeNode node) {
		if ( !node.hasMove() )
			return "defaultVertex;shape=emptyNode;depth=" + node.getDepth();

		if ( node.getMove().getPlayer() == StoneColour.BLACK )
			return "defaultVertex;shape=stone;color=black;depth=" + node.getDepth();

		return "defaultVertex;shape=stone;color=white;depth=" + node.getDepth();
	}


	@Override
	protected String getResourcePath() {
		return "/fxml/gameTreePane.fxml";
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		canvasWrapper = new GameTreemxCanvas(canvas.getGraphicsContext2D(), drawer);
	}
}
