/*
 * Copyright (c) 2017
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package ui.drawer;

import javafx.scene.canvas.Canvas;
import logic.board.Board;
import util.DrawCoords;
import util.StoneColour;

public class DrawerDecorator implements Drawer {

	private Drawer instance;

	public DrawerDecorator(Drawer drawer) {
		instance = drawer;
	}

	@Override
	public BoardDrawer getBoardDrawer() {
		return instance.getBoardDrawer();
	}

	@Override
	public void setBoardDrawer(BoardDrawer boardDrawer) {
		instance.setBoardDrawer(boardDrawer);
	}

	@Override
	public StoneDrawer getStoneDrawer() {
		return instance.getStoneDrawer();
	}

	@Override
	public void setStoneDrawer(StoneDrawer stoneDrawer) {
		instance.setStoneDrawer(stoneDrawer);
	}

	@Override
	public void draw(Board board) {
		instance.draw(board);
	}

	@Override
	public void redraw() {
		instance.redraw();
	}

	@Override
	public void setHoverStone(DrawCoords position, StoneColour colour) {
		instance.setHoverStone(position, colour);
	}

	@Override
	public Canvas getCanvas() {
		return instance.getCanvas();
	}
}
