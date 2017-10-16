function onClickEnable() {
    var enable = enableProxy.isSelected();
    var disable = !enable;
    server.setDisable(disable);
    domain.setDisable(disable);
    username.setDisable(disable);
    password.setDisable(disable);
    protocol.setDisable(disable);
    port.setDisable(disable);
}
