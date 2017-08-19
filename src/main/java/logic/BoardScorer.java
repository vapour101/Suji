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

import util.Coords;

import java.util.HashSet;
import java.util.Set;

public class BoardScorer {
    private Board board;
    private double komi;
    private Set<Chain> deadBlackChains;
    private Set<Chain> deadWhiteChains;

    public BoardScorer(Board board) {
        this(board, 0);
    }

    public BoardScorer(Board board, double komi) {
        this.board = board;
        this.komi = komi;
        deadBlackChains = new HashSet<>();
        deadWhiteChains = new HashSet<>();
    }

    public double getScore() {
        return getBlackScore() - getWhiteScore();
    }

    public double getBlackScore() {
        double blackScore = countBlackTerritory();
        blackScore += board.getBlackCaptures();

        for (Chain chain : deadWhiteChains)
            blackScore += chain.size();

        if (komi < 0)
            blackScore -= komi;

        return blackScore;
    }

    public double getWhiteScore() {
        double whiteScore = countWhiteTerritory();
        whiteScore += board.getWhiteCaptures();

        for (Chain chain : deadBlackChains)
            whiteScore += chain.size();

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

    public void markGroupDead(Coords coords) {
        Chain deadChain = board.getChainAtCoords(coords);

        if (deadChain == null)
            return;

        if (board.getBlackStones().contains(coords))
            deadBlackChains.add(deadChain);
        else
            deadWhiteChains.add(deadChain);
    }

    public void unmarkGroupDead(Coords coords) {
        Chain undeadChain = null;

        for (Chain deadChain : deadWhiteChains)
            if (deadChain.contains(coords)) {
                undeadChain = deadChain;
                break;
            }

        if (undeadChain != null) {
            deadWhiteChains.remove(undeadChain);
            return;
        }

        for (Chain deadChain : deadBlackChains)
            if (deadChain.contains(coords)) {
                undeadChain = deadChain;
                break;
            }

        if (undeadChain != null)
            deadBlackChains.remove(undeadChain);

    }
}
