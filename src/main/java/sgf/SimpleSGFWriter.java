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

package sgf;

import util.Move;

import java.util.List;

import static util.Move.Type.PLAY;
import static util.StoneColour.BLACK;

public class SimpleSGFWriter implements SGFWriter {

	private List<Move> tree;

	public SimpleSGFWriter(List<Move> gameSequence) {
		tree = gameSequence;
	}

	@Override
	public String getSGFString() {
		StringBuilder builder = new StringBuilder();
		beginSGFFile(builder);

		for (Move move : tree) {
			appendMove(builder, move);
		}

		endSGFFile(builder);
		return builder.toString();
	}

	private void beginSGFFile(StringBuilder stringBuilder) {
		stringBuilder.append("(;FF[4]GM[1]SZ[19]");
		stringBuilder.append("\n");
	}

	private void endSGFFile(StringBuilder stringBuilder) {
		stringBuilder.append(")");
		stringBuilder.append("\n");
	}

	private void appendMove(StringBuilder stringBuilder, Move move) {
		stringBuilder.append(";");

		if ( move.getPlayer() == BLACK )
			stringBuilder.append("B");
		else
			stringBuilder.append("W");

		stringBuilder.append("[");

		if ( move.getType() == PLAY )
			stringBuilder.append(move.getPosition().sgfString());

		stringBuilder.append("]");
		stringBuilder.append("\n");
	}
}
