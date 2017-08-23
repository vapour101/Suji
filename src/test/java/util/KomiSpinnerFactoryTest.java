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

import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class KomiSpinnerFactoryTest {

	private KomiSpinnerFactory factory;

	@Before
	public void setup() {
		factory = new KomiSpinnerFactory();
	}

	@Test
	public void defaultValue() {
		assertThat(factory.getValue(), is(6.5));
	}

	@Test
	public void incrementStandard() {
		factory.increment(1);

		assertThat(factory.getValue(), is(12.5));

		factory.increment(2);

		assertThat(factory.getValue(), is(24.5));

		factory.setValue(-12.5);
		factory.increment(1);

		assertThat(factory.getValue(), is(-6.5));

		factory.increment(1);

		assertThat(factory.getValue(), is(-0.5));
	}

	@Test
	public void decrementStandard() {
		factory.setValue(-6.5);
		factory.decrement(1);

		assertThat(factory.getValue(), is(-12.5));

		factory.decrement(2);

		assertThat(factory.getValue(), is(-24.5));

		factory.setValue(12.5);
		factory.decrement(1);

		assertThat(factory.getValue(), is(6.5));

		factory.decrement(1);

		assertThat(factory.getValue(), is(0.5));
	}

	@Test
	public void incrementPastZeroStandard() {
		factory.setValue(-0.5);
		factory.increment(1);

		assertThat(factory.getValue(), is(0.5));
	}

	@Test
	public void decrementPastZeroStandard() {
		factory.setValue(0.5);
		factory.decrement(1);

		assertThat(factory.getValue(), is(-0.5));
	}

	@Test
	public void incrementNonStandard() {
		factory.setValue(5.5);
		factory.increment(1);

		assertThat(factory.getValue(), is(6.5));

		factory.setValue(7.5);
		factory.increment(1);

		assertThat(factory.getValue(), is(12.5));
	}

	@Test
	public void decrementNonStandard() {
		factory.setValue(-5.5);
		factory.decrement(1);

		assertThat(factory.getValue(), is(-6.5));

		factory.setValue(-7.5);
		factory.decrement(1);

		assertThat(factory.getValue(), is(-12.5));
	}

	@Test
	public void incrementPastZeroNonStandard() {
		factory.setValue(-1.5);
		factory.increment(1);

		assertThat(factory.getValue(), is(-0.5));

		factory.setValue(-0.25);
		factory.increment(1);

		assertThat(factory.getValue(), is(0.5));
	}

	@Test
	public void decrementPastZeroNonStandard() {
		factory.setValue(1.5);
		factory.decrement(1);

		assertThat(factory.getValue(), is(0.5));

		factory.setValue(0.25);
		factory.decrement(1);

		assertThat(factory.getValue(), is(-0.5));
	}

	@Test
	public void parsingStrings() {
		double convertedValue = factory.getConverter().fromString("6.4");

		assertThat(convertedValue, is(6.4));

		String convertedString = factory.getConverter().toString(1.2);

		assertThat(convertedString, is("1.2"));
	}

	@Test
	public void wholeNumbers() {
		factory.setValue(6.0);

		factory.increment(1);

		assertThat(factory.getValue(), is(12.0));

		factory.decrement(2);

		assertThat(factory.getValue(), is(0.0));

		factory.decrement(1);

		assertThat(factory.getValue(), is(-6.0));
	}
}
