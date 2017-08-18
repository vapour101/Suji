package util;

import javafx.util.Pair;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static util.CoordProjector.fromBoardCoords;
import static util.Coords.getCoords;

public class CoordProjectorTest {
    @Test
    public void conversion()
    {
        CoordProjector proj = new CoordProjector();
        Pair<Double, Double> projection = fromBoardCoords(getCoords(4,4), 20);

        assertThat(projection, is(new Pair<Double, Double>(2.0, 2.0)));
    }
}
