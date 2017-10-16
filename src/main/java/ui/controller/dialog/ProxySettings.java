package ui.controller.dialog;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Spinner;
import javafx.scene.control.TextField;
import ui.controller.DockNodeController;
import util.WebHelper;


public class ProxySettings extends DockNodeController {

	@FXML
	private CheckBox enableProxy;
	@FXML
	private TextField server;
	@FXML
	private TextField domain;
	@FXML
	private TextField username;
	@FXML
	private TextField password;
	@FXML
	private TextField protocol;
	@FXML
	private Spinner<Integer> port;

	@Override
	protected String getResourcePath() {
		return "/fxml/proxySettings.fxml";
	}


	@FXML
	private void onApply(ActionEvent event) {
		WebHelper.USE_PROXY = enableProxy.isSelected();
		WebHelper.PROXY_SERVER = server.getText();
		WebHelper.PROXY_DOMAIN = domain.getText();
		WebHelper.PROXY_USER = username.getText();
		WebHelper.PROXY_PASS = password.getText();
		WebHelper.PROXY_PROTOCOL = protocol.getText();
		WebHelper.PROXY_PORT = port.getValue();

		getDockNode().close();
	}

	@FXML
	private void onCancel(ActionEvent event) {
		getDockNode().close();
	}
}
