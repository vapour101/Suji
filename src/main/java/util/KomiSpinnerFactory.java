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

import javafx.scene.control.SpinnerValueFactory;
import javafx.util.StringConverter;

public class KomiSpinnerFactory extends SpinnerValueFactory<Double> {

	public KomiSpinnerFactory() {
		setValue(6.5);
		setConverter(new StringConverter<Double>() {
			@Override
			public String toString(Double object) {
				return object.toString();
			}

			@Override
			public Double fromString(String string) {
				return Double.parseDouble(string);
			}
		});
	}

	@Override
	public void decrement(int steps) {
		double value = komiClamp(getValue());

		if ( value == getValue() ) {
			if ( value == 0.5 )
				value = -0.5;
			else
				value -= 6;
		}
		else if ( value > getValue() ) {
			if ( value == 0.5 )
				value = -0.5;
			else
				value -= 6;
		}

		setValue(value);

		if ( steps > 1 )
			decrement(steps - 1);
	}

	@Override
	public void increment(int steps) {
		double value = komiClamp(getValue());

		if ( value == getValue() ) {
			if ( value == -0.5 )
				value = 0.5;
			else
				value += 6;
		}
		else if ( value < getValue() ) {
			if ( value == -0.5 )
				value = 0.5;
			else
				value += 6;
		}


		setValue(value);

		if ( steps > 1 )
			increment(steps - 1);
	}

	private double komiClamp(double value) {
		boolean negative = value < 0;
		value = Math.abs(value);
		boolean wholeNumber = value - Math.floor(value) < 0.0001;
		value = Math.floor(value);

		if ( value % 6 != 0 ) {
			value -= value % 6;
		}

		if ( !wholeNumber )
			value += 0.5;

		if ( negative )
			value = -value;

		return value;
	}
}
