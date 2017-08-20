/*
 * Copyright (C) 2017 Vincent Varkevisser
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

import logic.Board;
import logic.BoardScorer;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static util.Coords.getCoords;

public class BoardScorerTest {

	private static final String[] testSequence = {"D1", "E1", "D2", "E2", "D3", "E4", "E3", "F3", "D4", "E5", "F2",
			"G2", "F1", "G1", "D5", "D6", "C5", "C6", "B4", "G4", "B6", "B7", "A7", "A8", "A6", "B8", "T19", "S18",
			"T18", "S17", "T17", "S15", "T16", "R14", "S16", "R16", "T15", "T14", "Q19", "R19", "Q18", "R18", "Q17",
			"R17", "Q16", "Q15", "P15", "P14", "O15", "O14", "N15", "N14", "N17", "M15", "M16", "L16", "L17", "K16",
			"K17", "J16", "J17", "H16", "H17", "G16", "G17", "F16", "F17", "E17", "E18", "D17", "D18", "C18", "C19",
			"B19", "B18", "C17", "A19", "B17", "B19", "A17", "F18", "A18", "D19", "M18", "M17"};

	@Test
	public void emptyBoardIsZeroPoints() {
		Board board = new Board();
		BoardScorer scorer = new BoardScorer(board);

		assertThat(scorer.getBlackScore(), is(0.0));
		assertThat(scorer.getWhiteScore(), is(0.0));
		assertThat(scorer.getScore(), is(0.0));
	}

	@Test
	public void normalKomi() {
		Board board = new Board();
		BoardScorer scorer = new BoardScorer(board, 6.5);

		assertThat(scorer.getBlackScore(), is(0.0));
		assertThat(scorer.getWhiteScore(), is(6.5));
		assertThat(scorer.getScore(), is(-6.5));
	}

	@Test
	public void negativeKomi() {
		Board board = new Board();
		BoardScorer scorer = new BoardScorer(board, -6.5);

		assertThat(scorer.getBlackScore(), is(6.5));
		assertThat(scorer.getWhiteScore(), is(0.0));
		assertThat(scorer.getScore(), is(6.5));
	}

	@Test
	public void markingDeadStones() {
		Board board = buildTestBoard();
		BoardScorer scorer = new BoardScorer(board);

		scorer.markGroupDead(getCoords("K10"));
		assertThat(scorer.getDeadWhiteStones().size(), is(0));
		assertThat(scorer.getDeadBlackStones().size(), is(0));

		assertThat(board.getBlackCaptures(), is(3));
		assertThat(board.getWhiteCaptures(), is(0));

		scorer.markGroupDead(getCoords("M18"));
		scorer.markGroupDead(getCoords("T19"));

		assertThat(scorer.getDeadBlackStones(),
				   hasItems(getCoords("S16"),
							getCoords("T15"),
							getCoords("T16"),
							getCoords("T17"),
							getCoords("T18"),
							getCoords("T19")));
		assertThat(scorer.getDeadBlackStones().size(), is(6));

		assertThat(scorer.getDeadWhiteStones(), hasItems(getCoords("M18")));
		assertThat(scorer.getDeadWhiteStones().size(), is(1));
	}

	private Board buildTestBoard() {
		Board testBoard = new Board();

		for (int i = 0; i < testSequence.length; i++) {
			if ( i % 2 == 0 )
				testBoard.playBlackStone(getCoords(testSequence[i]));
			else
				testBoard.playWhiteStone(getCoords(testSequence[i]));
		}

		return testBoard;
	}
}
