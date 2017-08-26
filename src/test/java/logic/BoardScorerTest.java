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

package logic;

import org.junit.Test;
import util.Coords;
import util.StoneColour;

import java.util.Collection;
import java.util.HashSet;

import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static util.Coords.getCoords;
import static util.HandicapHelper.getHandicapStones;

public class BoardScorerTest {

	private static final String[] testBoard1 = {"D1", "E1", "D2", "E2", "D3", "E4", "E3", "F3", "D4", "E5", "F2",
			"G2", "F1", "G1", "D5", "D6", "C5", "C6", "B4", "G4", "B6", "B7", "A7", "A8", "A6", "B8", "T19", "S18",
			"T18", "S17", "T17", "S15", "T16", "R14", "S16", "R16", "T15", "T14", "Q19", "R19", "Q18", "R18", "Q17",
			"R17", "Q16", "Q15", "P15", "P14", "O15", "O14", "N15", "N14", "N17", "M15", "M16", "L16", "L17", "K16",
			"K17", "J16", "J17", "H16", "H17", "G16", "G17", "F16", "F17", "E17", "E18", "D17", "D18", "C18", "C19",
			"B19", "B18", "C17", "A19", "B17", "B19", "A17", "F18", "A18", "D19", "M18", "M17"};

	private static final String[] testBoard2 = {"J1", "K1", "J2", "K2", "J3", "K3", "J4", "K4", "J5", "K5", "J6",
			"K6", "J7", "K7", "J8", "K8", "J9", "K9", "J10", "K10", "J11", "K11", "J12", "K12", "J13", "K13", "J14",
			"K14", "J15", "K15", "J16", "K16", "J17", "K17", "J18", "K18", "J19", "K19"};

	private static final String[] testBoard3 = {"R4", "D16", "F3", "C6", "L3", "Q10", "O17", "Q14", "K17", "M17",
			"M18", "F17", "C11", "C15", "C8", "D2", "G6", "R7", "R17", "Q17", "S16", "S15", "Q18", "R16", "S18",
			"O16", "N17", "N16", "P4", "L18", "L17", "M16", "N18", "J17", "K18", "L5", "J4", "N5", "M4", "O6", "M11",
			"O11", "T15", "T14", "T16", "S14", "N10", "M12", "L12", "M13", "N11", "O12", "O9", "P8", "S6", "R6", "S5",
			"S7", "M7", "M5", "K6", "D10", "C9", "E7", "D6", "C7", "D7", "D5", "E6", "C10", "B10", "B11", "B12", "B9",
			"A11", "B8", "E8", "E12", "F10", "G12", "H11", "H12", "J11", "Q3", "P3", "Q4", "Q5", "R5", "R3", "Q2",
			"P6", "O7", "J16", "H17", "K15", "H15", "D13", "D12", "C12", "D14", "C13", "E13", "B14", "B15", "G19",
			"G18", "J19", "F19", "A15", "A16", "A14", "B17", "E2", "E3", "F2", "F4", "G4", "F5", "G5", "F6", "F7",
			"T6", "S4", "L13", "K12", "E1", "O8", "P7", "P9", "Q9", "P10", "P11", "N12", "N13", "H16", "G16", "J13",
			"H14", "D11", "E11", "E10", "G11", "G10", "J15", "K16", "P17", "P18", "L4", "N3", "L7", "L6", "N7", "N8",
			"M6", "M8", "O4", "P5", "O3", "P2", "O2", "O1", "M2", "M3", "N2", "K2", "L2", "K4", "N4", "K3", "N1",
			"P1", "O5", "R2", "Q6", "Q1", "F1", "E5", "E4", "C3", "D3", "C5", "B5", "C4", "C2", "B6", "B7", "B4",
			"A6", "B2", "A4", "B3", "G2", "G3", "B1", "A2", "A5", "C1", "D1", "H2", "A3", "G1", "A1", "T5", "T7",
			"L15", "M15", "H13", "G13", "H19", "J18", "F11", "J12", "K13", "K5", "J5", "F12", "L14", "M14", "J14",
			"A9", "L1", "M1", "K1", "A10", "D9", "H18", "K19", "C14", "O10", "L16"};

	private static final String[] testBoard4 = {"E17", "Q10", "O17", "R14", "L16", "K4", "R6", "O3", "S4", "R3", "Q8",
			"R10", "H3", "F3", "K3", "L3", "L2", "M2", "J2", "M4", "J4", "C3", "Q18", "R17", "C9", "D15", "C16",
			"C15", "D16", "C12", "E15", "B11", "E14", "B16", "B17", "B15", "C6", "O16", "N16", "O15", "N15", "N14",
			"M14", "N13", "M13", "N11", "M12", "P9", "S3", "O7", "S9", "S10", "R2", "Q2", "R4", "Q3", "S1", "J5",
			"H5", "J6", "K5", "L4", "H6", "K6", "H7", "K9", "L10", "M8", "K10", "E12", "F12", "E11", "F11", "E10",
			"F10", "E9", "B4", "B3", "C5", "E7", "D8", "E8", "B10", "C10", "B8", "D9", "R18", "Q6", "Q7", "Q5", "S17",
			"S16", "Q17", "R16", "T18", "H9", "J10", "J9", "M1", "N1", "L1", "N2", "G2", "F2", "F1", "E1", "G1", "D2",
			"A3", "A2", "A4", "B1", "M9", "N9", "N10", "O10", "M10", "N8", "N12", "O12", "D14", "C14", "T10", "T11",
			"T9", "S12", "R9", "A17", "A18", "A16", "C18", "D7", "C7", "C8", "B9", "D8", "P7", "P6", "L9", "L8", "J7",
			"K7", "D13", "C13", "D12", "D11", "M11", "G4", "H4", "G5", "O11", "P11", "O13", "P12", "O14", "P14",
			"P13", "Q13", "P15", "P16", "Q14", "Q15", "P14", "D5", "R13", "S13", "Q12", "R12", "Q13", "Q11", "S14",
			"S15", "R15", "T14", "R14", "H10", "H11", "G6", "D6", "E6", "P17", "T13", "T17", "T15", "A10", "A11",
			"E13", "C4", "B6", "G7", "F9", "F8", "J8", "K8", "P8", "O8", "R5", "Q9", "R7", "Q1", "R1", "G10", "G11"};

	@Test
	public void emptyBoardIsZeroPoints() {
		Board board = new Board();
		BoardScorer scorer = new BoardScorer(board);

		assertThat(scorer.getScore(StoneColour.BLACK), is(0.0));
		assertThat(scorer.getScore(StoneColour.WHITE), is(0.0));
		assertThat(scorer.getScore(), is(0.0));
	}

	@Test
	public void normalKomi() {
		Board board = new Board();
		BoardScorer scorer = new BoardScorer(board, 6.5);

		assertThat(scorer.getScore(StoneColour.BLACK), is(0.0));
		assertThat(scorer.getScore(StoneColour.WHITE), is(6.5));
		assertThat(scorer.getScore(), is(-6.5));
	}

	@Test
	public void negativeKomi() {
		Board board = new Board();
		BoardScorer scorer = new BoardScorer(board, -6.5);

		assertThat(scorer.getScore(StoneColour.BLACK), is(6.5));
		assertThat(scorer.getScore(StoneColour.WHITE), is(0.0));
		assertThat(scorer.getScore(), is(6.5));
	}

	@Test
	public void findingContiguousPoints() {
		Board board = new Board();
		BoardScorer scorer = new BoardScorer(board);

		Collection<Coords> emptyPoints = new HashSet<>();

		emptyPoints.add(getCoords("A1"));
		emptyPoints.add(getCoords("A2"));
		emptyPoints.add(getCoords("B1"));
		emptyPoints.add(getCoords("B2"));

		emptyPoints.add(getCoords("D1"));
		emptyPoints.add(getCoords("E1"));
		emptyPoints.add(getCoords("F1"));
		emptyPoints.add(getCoords("G1"));

		assertThat(scorer.getContiguousEmptySection(emptyPoints, getCoords("A1")).size(), is(4));
		assertThat(scorer.getContiguousEmptySection(emptyPoints, getCoords("D1")).size(), is(4));

		assertThat(scorer.getContiguousEmptySection(emptyPoints, getCoords("A1")),
				   hasItems(getCoords("A1"), getCoords("A2"), getCoords("B1"), getCoords("B2")));

		assertThat(scorer.getContiguousEmptySection(emptyPoints, getCoords("F1")),
				   hasItems(getCoords("D1"), getCoords("E1"), getCoords("F1"), getCoords("G1")));

		board = buildTestBoard(testBoard1);
		scorer = new BoardScorer(board);
		emptyPoints = scorer.getEmptyIntersections();

		assertThat(scorer.getContiguousEmptySection(emptyPoints, getCoords("S19")).size(), is(1));
		assertThat(scorer.getContiguousEmptySection(emptyPoints, getCoords("S19")), hasItems(getCoords("S19")));
	}

	private Board buildTestBoard(String[] sequence) {
		Board testBoard = new Board();

		for (int i = 0; i < sequence.length; i++) {
			if ( i % 2 == 0 )
				testBoard.playBlackStone(getCoords(sequence[i]));
			else
				testBoard.playWhiteStone(getCoords(sequence[i]));
		}

		return testBoard;
	}

	@Test
	public void realGameScore() {
		Board board = buildTestBoard(testBoard3, 2);
		BoardScorer scorer = new BoardScorer(board, 0.5);

		scorer.markGroupDead(getCoords("L7"));
		scorer.markGroupDead(getCoords("L18"));

		assertThat(scorer.getScore(StoneColour.WHITE), is(82.5));
		assertThat(scorer.getScore(StoneColour.BLACK), is(85.0));
		assertThat(scorer.getScore(), is(2.5));
	}

	private Board buildTestBoard(String[] sequence, int handicap) {
		if ( handicap < 2 || handicap > 9 )
			return buildTestBoard(sequence);

		Board testBoard = new Board();

		for (Coords stone : getHandicapStones(handicap))
			testBoard.playBlackStone(stone);

		for (int i = 0; i < sequence.length; i++) {
			if ( i % 2 == 1 )
				testBoard.playBlackStone(getCoords(sequence[i]));
			else
				testBoard.playWhiteStone(getCoords(sequence[i]));
		}

		return testBoard;
	}

	@Test
	public void realGameScore2() {
		Board board = buildTestBoard(testBoard4, 3);
		board.playWhiteStone(getCoords("N11"));

		BoardScorer scorer = new BoardScorer(board, -4.5);
		scorer.markGroupDead(getCoords("K5"));

		assertThat(scorer.getScore(StoneColour.WHITE), is(112.0));
		assertThat(scorer.getScore(StoneColour.BLACK), is(57.5));

		assertThat(scorer.getScore(), is(-54.5));
	}

	@Test
	public void markingDeadStones() {
		Board board = buildTestBoard(testBoard1);
		BoardScorer scorer = new BoardScorer(board);

		scorer.markGroupDead(getCoords("K10"));
		assertThat(scorer.getDeadStones(StoneColour.WHITE).size(), is(0));
		assertThat(scorer.getDeadStones(StoneColour.BLACK).size(), is(0));

		assertThat(board.getBlackCaptures(), is(3));
		assertThat(board.getWhiteCaptures(), is(0));

		scorer.markGroupDead(getCoords("M18"));
		scorer.markGroupDead(getCoords("T19"));

		assertThat(scorer.getDeadStones(StoneColour.BLACK),
				   hasItems(getCoords("S16"),
							getCoords("T15"),
							getCoords("T16"),
							getCoords("T17"),
							getCoords("T18"),
							getCoords("T19")));
		assertThat(scorer.getDeadStones(StoneColour.BLACK).size(), is(6));

		assertThat(scorer.getDeadStones(StoneColour.WHITE), hasItems(getCoords("M18")));
		assertThat(scorer.getDeadStones(StoneColour.WHITE).size(), is(1));
	}

	@Test
	public void simpleScore() {
		Board board = buildTestBoard(testBoard2);
		BoardScorer scorer = new BoardScorer(board);

		assertThat(scorer.getScore(StoneColour.BLACK), is(152.0));
		assertThat(scorer.getScore(StoneColour.WHITE), is(171.0));
		assertThat(scorer.getScore(), is(-19.0));
	}

	@Test
	public void complexScore() {
		Board board = buildTestBoard(testBoard1);
		BoardScorer scorer = new BoardScorer(board);

		scorer.markGroupDead(getCoords("M18"));
		scorer.markGroupDead(getCoords("T19"));

		scorer.markGroupDead(getCoords("H16"));
		scorer.markGroupDead(getCoords("O14"));
		scorer.markGroupDead(getCoords("M15"));

		scorer.unmarkGroupDead(getCoords("M15"));
		scorer.unmarkGroupDead(getCoords("H16"));
		scorer.unmarkGroupDead(getCoords("O14"));

		assertThat(scorer.getScore(), is(-208.0));

		scorer.unmarkGroupDead(getCoords("M18"));

		assertThat(scorer.getScore(), is(-234.0));

		scorer.unmarkGroupDead(getCoords("K10"));
		scorer.unmarkGroupDead(getCoords("T19"));

		assertThat(scorer.getDeadStones(StoneColour.BLACK).size(), is(0));
		assertThat(scorer.getDeadStones(StoneColour.WHITE).size(), is(0));

		assertThat(scorer.getScore(StoneColour.BLACK), is(18.0));
		assertThat(scorer.getScore(StoneColour.WHITE), is(239.0));

		assertThat(scorer.getScore(), is(-221.0));
	}
}
