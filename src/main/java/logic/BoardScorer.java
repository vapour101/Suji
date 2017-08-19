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

package logic;

import java.util.Set;

public class BoardScorer {
    private Board board;
    private double komi;
    private Set<Chain> deadBlackChains;
    private Set<Chain> deadWhiteChains;

    public BoardScorer(Board board) {
        this.board = board;
        komi = 0;
    }

    public BoardScorer(Board board, double komi) {
        this.board = board;
        this.komi = komi;
    }

    public double getScore() {
        return getBlackScore() - getWhiteScore();
    }

    public double getBlackScore() {
        double blackScore = countBlackTerritory();
        blackScore += board.getBlackCaptures();

        if (komi < 0)
            blackScore -= komi;

        return blackScore;
    }

    public double getWhiteScore() {
        double whiteScore = countWhiteTerritory();
        whiteScore += board.getWhiteCaptures();

        if (komi > 0)
            whiteScore += komi;

        return whiteScore;
    }

    private int countWhiteTerritory() {
        return 0;
    }

    private int countBlackTerritory() {
        return 0;
    }
}
