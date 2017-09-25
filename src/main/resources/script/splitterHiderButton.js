var hidden = false;

function hideInSplitter(splitPane, item, button) {
    hidden = !hidden;
    if (hidden) {
        splitPane.getItems().remove(item);
        button.setText("<");
    }
    else {
        splitPane.getItems().add(item);
        splitPane.setDividerPosition(0, 0.7)
        button.setText(">");
    }
}
