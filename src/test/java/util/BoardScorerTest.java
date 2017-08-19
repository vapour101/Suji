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

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class BoardScorerTest {

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
}
