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

package logic.board;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;
import static util.Coords.getCoords;

public class ChainSetTest {

	@Test
	public void trackStones() {
		ChainSet chains = new ChainSet();

		chains.add(getCoords("D4"));

		assertThat(chains.contains(getCoords("D4")), is(true));
		assertThat(chains.contains(getCoords("C3")), is(false));
	}

	@Test
	public void autoMergeChains() {
		ChainSet chains = new ChainSet();

		chains.add(getCoords("D4"));

		chains.add(getCoords("C4"));

		assertThat(chains.contains(getCoords("D4")), is(true));
		assertThat(chains.contains(getCoords("C4")), is(true));
		assertThat(chains.getChainCount(), is(1));

		chains.add(getCoords("F4"));

		assertThat(chains.getChainCount(), is(2));

		chains.add(getCoords("E4"));

		assertThat(chains.getChainCount(), is(1));
	}

	@Test
	public void nonContiguousChains() {
		ChainSet chains = new ChainSet();

		chains.add(getCoords("D4"));

		chains.add(getCoords("C3"));

		assertThat(chains.contains(getCoords("D4")), is(true));
		assertThat(chains.contains(getCoords("C3")), is(true));
	}

	@Test
	public void getting() {
		ChainSet chains = new ChainSet();

		chains.add(getCoords("C3"));
		chains.add(getCoords("D4"));

		assertThat(chains.getStones(), hasItems(getCoords("C3"), getCoords("D4")));
		assertThat(chains.getStones().size(), is(2));
	}

	@Test
	public void checkingForCaptures() {
		ChainSet black = new ChainSet();
		ChainSet white = new ChainSet();

		black.add(getCoords("D4"));
		white.add(getCoords("C4"));
		white.add(getCoords("E4"));

		assertThat(black.chainIsCaptured(getCoords("D3"), white), is(false));
		assertThat(black.chainIsCaptured(getCoords("C3"), white), is(false));
		assertThat(black.chainIsCaptured(getCoords("E4"), white), is(false));

		white.add(getCoords("D3"));

		assertThat(black.chainIsCaptured(getCoords("D5"), white), is(true));
		assertThat(black.chainIsCaptured(getCoords("E4"), white), is(false));
	}

	@Test
	public void checkingForSuicide() {
		ChainSet black = new ChainSet();
		ChainSet white = new ChainSet();

		white.add(getCoords("D3"));
		white.add(getCoords("D5"));
		white.add(getCoords("C4"));

		assertThat(black.isSuicide(getCoords("D4"), white), is(false));

		white.add(getCoords("E4"));

		assertThat(black.isSuicide(getCoords("D4"), white), is(true));

		black.add(getCoords("E3"));
		black.add(getCoords("E5"));
		black.add(getCoords("F4"));

		assertThat(black.isSuicide(getCoords("D4"), white), is(false));

		black = new ChainSet();
		white = new ChainSet();

		white.add(getCoords("C3"));
		black.add(getCoords("D5"));
		assertThat(black.isSuicide(getCoords("D4"), white), is(false));
	}

	@Test
	public void simpleCapturing() {
		ChainSet black = new ChainSet();
		ChainSet white = new ChainSet();

		white.add(getCoords("B3"));
		white.add(getCoords("D3"));
		white.add(getCoords("D5"));
		white.add(getCoords("E4"));

		black.add(getCoords("C3"));
		black.add(getCoords("D4"));
		black.add(getCoords("E5"));

		assertThat(black.chainIsCaptured(getCoords("C4"), white), is(true));
		assertThat(black.getChainCount(), is(3));

		assertThat(black.captureStones(getCoords("E4"), white), is(0));
		assertThat(black.getChainCount(), is(3));

		assertThat(black.captureStones(getCoords("C4"), white), is(1));
		assertThat(black.getChainCount(), is(2));
	}

	@Test
	public void capturingMultipleSingleStones() {
		ChainSet black = new ChainSet();
		ChainSet white = new ChainSet();

		white.add(getCoords("C3"));
		white.add(getCoords("C4"));
		white.add(getCoords("E3"));
		white.add(getCoords("E4"));
		white.add(getCoords("D2"));

		black.add(getCoords("D3"));
		black.add(getCoords("D4"));

		assertThat(black.chainIsCaptured(getCoords("D5"), white), is(true));
		assertThat(black.getChainCount(), is(1));
		assertThat(white.getChainCount(), is(3));

		assertThat(black.captureStones(getCoords("A1"), white), is(0));
		assertThat(black.captureStones(getCoords("C4"), white), is(0));

		assertThat(black.captureStones(getCoords("D5"), white), is(2));
		assertThat(black.getChainCount(), is(0));
	}

	@Test
	public void capturingGroupWithSmallEye() {
		ChainSet black = new ChainSet();
		ChainSet white = new ChainSet();

		String[] stones = new String[]{"C3", "C4", "C5", "D3", "D5", "E3", "E4", "E5"};
		for (String stone : stones)
			black.add(getCoords(stone));

		stones = new String[]{"B3", "B4", "B5", "C2", "D2", "E2", "C6", "D6", "E6", "F3", "F4", "F5"};
		for (String stone : stones)
			white.add(getCoords(stone));

		assertThat(black.getChainCount(), is(1));
		assertThat(white.getChainCount(), is(4));

		assertThat(white.isSuicide(getCoords("D4"), black), is(false));
		assertThat(black.chainIsCaptured(getCoords("D4"), white), is(true));
		assertThat(black.captureStones(getCoords("D4"), white), is(8));

		assertThat(black.getChainCount(), is(0));
	}

	@Test
	public void capturingGroupWithBigEye() {
		ChainSet black = new ChainSet();
		ChainSet white = new ChainSet();

		String[] stones = new String[]{"C3", "C4", "C5", "C6", "D3", "D6", "E3", "E4", "E5", "E6"};
		for (String stone : stones)
			black.add(getCoords(stone));

		stones = new String[]{"B3", "B4", "B5", "B6", "C2", "D2", "E2", "C7", "D7", "E7", "F3", "F4", "F5", "F6", "D5"};
		for (String stone : stones)
			white.add(getCoords(stone));

		assertThat(black.getChainCount(), is(1));
		assertThat(white.getChainCount(), is(5));

		assertThat(white.isSuicide(getCoords("D4"), black), is(false));
		assertThat(black.chainIsCaptured(getCoords("D4"), white), is(true));
		assertThat(black.captureStones(getCoords("D4"), white), is(10));

		assertThat(black.getChainCount(), is(0));
	}

	@Test
	public void findingChains() {
		ChainSet chains = new ChainSet();

		chains.add(getCoords("D4"));

		assertThat(chains.getChainFromStone(getCoords("D4")).contains(getCoords("D4")), is(true));
		assertThat(chains.getChainFromStone(getCoords("C3")), nullValue());
	}
}
