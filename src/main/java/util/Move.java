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

package util;

public class Move {

	private Type moveType;
	private Coords position;
	private StoneColour player;

	private Move(Type type, StoneColour player) {
		this.moveType = type;
		this.player = player;
	}

	public static Move play(Coords position, StoneColour player) {
		Move result = new Move(Type.PLAY, player);

		result.position = position;

		return result;
	}

	public static Move pass(StoneColour player) {
		return new Move(Type.PASS, player);
	}

	public Coords getPosition() {
		return position;
	}

	public StoneColour getPlayer() {
		return player;
	}

	public Type getType() {
		return moveType;
	}

	public boolean equals(Object other) {
		if ( this == other )
			return true;
		else if ( !(other instanceof Move) )
			return false;
		else {
			Move compare = (Move) other;
			boolean equal = this.moveType == compare.moveType;
			equal &= this.player == compare.player;

			if ( equal && this.moveType == Type.PLAY )
				equal = this.position.equals(compare.position);

			return equal;
		}
	}

	@Override
	public String toString() {
		String out = player.toString();

		if ( moveType == Type.PLAY )
			out += " " + position.toString();
		else
			out += " PASS";

		return out;
	}

	public enum Type {
		PLAY, PASS;
	}
}
