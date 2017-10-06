package ui.javafx;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ObjectPropertyBase;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.SplitPane;
import javafx.util.Duration;

import java.io.IOException;

public class GoTimer extends SplitPane {

	@FXML
	private Label label;
	@FXML
	private ProgressIndicator indicator;

	private ObjectProperty<Duration> current = null;
	private ObjectProperty<Duration> initial = null;

	public GoTimer() {
		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/goTimer.fxml"));
		fxmlLoader.setRoot(this);
		fxmlLoader.setController(this);

		try {
			fxmlLoader.load();
		}
		catch (IOException exception) {
			throw new RuntimeException(exception);
		}
	}

	public ObjectProperty<Duration> initialProperty() {
		if ( initial == null ) {
			initial = new ObjectPropertyBase<Duration>(Duration.seconds(1)) {
				@Override
				public void invalidated() {
					refreshIndicator();
				}

				@Override
				public Object getBean() {
					return GoTimer.this;
				}

				@Override
				public String getName() {
					return "initial";
				}
			};
		}

		return initial;
	}

	public double getInitial() {
		return initialProperty().get().toSeconds();
	}

	public void setInitial(double initial) {
		initialProperty().set(Duration.seconds(initial));
	}

	public DoubleProperty durationProperty() {
		return indicator.progressProperty();
	}

	public ObjectProperty<Duration> currentProperty() {
		if ( current == null ) {
			current = new ObjectPropertyBase<Duration>(Duration.millis(1)) {
				@Override
				public void invalidated() {
					refreshIndicator();
					refreshLabel();
				}

				@Override
				public Object getBean() {
					return GoTimer.this;
				}

				@Override
				public String getName() {
					return "current";
				}
			};
		}

		return current;
	}

	public double getCurrent() {
		return currentProperty().get().toSeconds();
	}

	public void setCurrent(double current) {
		currentProperty().set(Duration.seconds(current));
	}

	private void refreshLabel() {
		Duration current = currentProperty().get();

		String text = "";
		if ( floor(current.toHours()) > 0 )
			text += floor(current.toHours()) + ":";

		text += floor(current.toMinutes()) % 60 + ":";
		text += floor(current.toSeconds()) % 60;

		label.setText(text);
	}

	private long floor(double a) {
		return Math.round(Math.floor(a));
	}

	private void refreshIndicator() {
		indicator.setProgress(getCurrent() / getInitial());
	}
}