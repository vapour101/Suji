package util;

import java.util.HashSet;
import java.util.Set;

import static util.Coords.getCoords;

public class HandicapHelper {
    public static Set<Coords> getHandicapStones(int handicap) {
        Set<Coords> stones = new HashSet<>();

        if (handicap > 1 && handicap <= 9) {
            stones.add(getCoords("D4"));
            stones.add(getCoords("Q16"));

            if (handicap > 2)
                stones.add(getCoords("Q4"));
            if (handicap > 3)
                stones.add(getCoords("D16"));
            if (handicap > 4 && (handicap % 2) == 1)
                stones.add(getCoords("K10"));
            if (handicap > 5) {
                stones.add(getCoords("D10"));
                stones.add(getCoords("Q10"));
            }
            if (handicap > 7) {
                stones.add(getCoords("K4"));
                stones.add(getCoords("K16"));
            }
        }

        return stones;
    }
}
