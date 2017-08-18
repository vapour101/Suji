package logic;

import org.junit.Test;
import util.Coords;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static util.Coords.getCoords;

public class ChainSetTest {

    @Test
    public void trackStones()
    {
        ChainSet chains = new ChainSet();

        chains.addStone(getCoords(4,4));

        assertThat(chains.contains(getCoords(4,4)), is(true));
        assertThat(chains.contains(getCoords(3,3)), is(false));
    }

    @Test
    public void autoMergeChains()
    {
        ChainSet chains = new ChainSet();

        chains.addStone(getCoords(4,4));

        chains.addStone(getCoords(4,3));

        assertThat(chains.contains(getCoords(4,4)), is(true));
        assertThat(chains.contains(getCoords(4,3)), is(true));
    }

    @Test
    public void nonContiguousChains()
    {
        ChainSet chains = new ChainSet();

        chains.addStone(getCoords(4,4));

        chains.addStone(getCoords(3,3));

        assertThat(chains.contains(getCoords(4,4)), is(true));
        assertThat(chains.contains(getCoords(3,3)), is(true));
    }
}
