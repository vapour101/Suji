package ui.javafx;

import com.mxgraph.canvas.mxICanvas;
import com.mxgraph.util.mxPoint;
import com.mxgraph.view.mxCellState;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Paint;
import javafx.scene.transform.Affine;
import javafx.util.Pair;
import ui.drawer.StoneDrawer;
import util.DrawCoords;
import util.StoneColour;

import java.util.Stack;

public class GameTreemxCanvas implements mxICanvas {

	private GraphicsContext context;
	private DrawCoords translation;
	private double scale;
	private StoneDrawer stoneDrawer;

	private Stack<Pair<Affine, Paint>> contextStack;

	public GameTreemxCanvas(GraphicsContext graphicsContext, StoneDrawer drawer) {
		context = graphicsContext;
		translation = new DrawCoords(0, 0);
		scale = 1;
		contextStack = new Stack<>();
		stoneDrawer = drawer.clone();
		stoneDrawer.setCanvas(context.getCanvas());
	}

	@Override
	public void setTranslate(double x, double y) {
		translation.setX(x);
		translation.setY(y);
	}

	@Override
	public mxPoint getTranslate() {
		double x = translation.getX();
		double y = translation.getY();

		return new mxPoint(x, y);
	}

	private void pushIdentity(Paint colour) {
		pushContext(colour);
		context.setTransform(1, 0, 0, 1, 0, 0);
	}

	private void resize(mxCellState state) {
		double x = state.getX() + state.getWidth();
		double y = state.getY() + state.getHeight();

		x += translation.getX();
		y += translation.getY();

		x *= scale;
		y *= scale;

		if ( x > context.getCanvas().getWidth() )
			context.getCanvas().setWidth(x + 10);

		if ( y > context.getCanvas().getHeight() )
			context.getCanvas().setHeight(y + 10);
	}

	private void drawConnector(mxCellState state) {
		pushIdentity(Paint.valueOf("#000000"));

		if ( state.getAbsolutePointCount() < 2 ) {
			popContext();
			return;
		}

		for (int i = 1; i < state.getAbsolutePointCount(); i++) {
			mxPoint start = state.getAbsolutePoint(i - 1);
			mxPoint end = state.getAbsolutePoint(i);

			context.strokeLine(start.getX(), start.getY(), end.getX(), end.getY());
		}

		popContext();
	}

	private void drawEmptyNode(mxCellState state) {
		pushContext(Paint.valueOf("#FF0000"));

		double x = state.getX();
		double y = state.getY();
		double w = state.getWidth();
		double h = state.getHeight();

		context.strokeRect(x, y, w, h);

		popContext();
	}

	private void drawStone(mxCellState state) {
		pushContext(Paint.valueOf("#FFFFFF"));

		DrawCoords coords = new DrawCoords(state.getCenterX(), state.getCenterY());
		StoneColour colour = StoneColour.fromString(state.getStyle().get("color").toString());

		double radius = Math.min(state.getWidth(), state.getHeight());
		radius /= 2;

		if ( colour != null ) {
			stoneDrawer.setRadius(radius);
			stoneDrawer.draw(coords, colour);
		}

		popContext();
	}

	private void pushContext(Paint colour) {
		Affine oldTransform = context.getTransform();
		Paint oldColour = context.getFill();

		context.setTransform(getTransform());
		context.setFill(colour);
		context.setStroke(colour);

		Pair<Affine, Paint> settings = new Pair<>(oldTransform, oldColour);

		contextStack.push(settings);
	}


	private Affine getTransform() {
		return new Affine(scale, 0, translation.getX(), 0, scale, translation.getY());
	}


	private void popContext() {
		if ( contextStack.empty() )
			return;

		Pair<Affine, Paint> settings = contextStack.pop();

		context.setTransform(settings.getKey());
		context.setFill(settings.getValue());
		context.setStroke(settings.getValue());
	}


	@Override
	public void setScale(double s) {
		scale = s;
	}


	@Override
	public double getScale() {
		return scale;
	}


	@Override
	public Object drawCell(mxCellState state) {
		resize(state);

		String shape = state.getStyle().get("shape").toString();

		switch (shape) {
		case "connector":
			drawConnector(state);
			break;
		case "emptyNode":
			drawEmptyNode(state);
			break;
		case "stone":
			drawStone(state);
			break;
		}

		return null;
	}


	@Override
	public Object drawLabel(String text, mxCellState state, boolean html) {
		if ( state.getStyle().get("depth") == null )
			return null;

		String depth = state.getStyle().get("depth").toString();
		Paint paint = Paint.valueOf("#000000");

		if ( state.getStyle().get("color") != null && state.getStyle().get("color").equals("black") )
			paint = Paint.valueOf("#FFFFFF");

		pushContext(paint);

		double x = state.getX();
		double y = state.getCenterY();
		context.fillText(depth, x, y);

		popContext();

		return null;
	}
}
