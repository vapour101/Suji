package ui.controller.sidebar;

import event.GameEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import logic.gamehandler.GameHandler;
import ui.controller.SelfBuildingController;
import ui.drawer.StoneDrawer;
import ui.drawer.TreeDrawer;

import java.net.URL;
import java.util.ResourceBundle;


public class GameTreePane extends SelfBuildingController implements Initializable {

	private StoneDrawer drawer;
	private TreeDrawer treeDrawer;
	@FXML
	private Canvas canvas;

	public GameTreePane(GameHandler game, StoneDrawer stoneDrawer) {
		game.subscribe(GameEvent.MOVE, this::onGameEvent);
		drawer = stoneDrawer;
	}

	private void onGameEvent(GameEvent event) {
		canvas.getGraphicsContext2D().clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
		treeDrawer.updateModel(event.getHandler().getGameTree());
		treeDrawer.draw(drawer);
	}


	@Override
	protected String getResourcePath() {
		return "/fxml/gameTreePane.fxml";
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		drawer = drawer.clone();
		drawer.setRadius(5.0);
		drawer.setCanvas(canvas);
		treeDrawer = new TreeDrawer(canvas);
	}
}
