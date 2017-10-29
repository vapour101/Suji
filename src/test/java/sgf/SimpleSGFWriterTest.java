package sgf;

import org.junit.Test;
import util.Move;

import java.util.LinkedList;
import java.util.List;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;
import static util.Coords.getCoords;
import static util.Move.pass;
import static util.Move.play;
import static util.StoneColour.BLACK;
import static util.StoneColour.WHITE;

public class SimpleSGFWriterTest {

	@Test
	public void sgfString() {
		List<Move> moveList = new LinkedList<Move>();
		moveList.add(play(getCoords("D4"), BLACK));
		moveList.add(play(getCoords("C3"), WHITE));
		moveList.add(pass(BLACK));

		SimpleSGFWriter writer = new SimpleSGFWriter(moveList);

		assertThat(writer.getSGFString(), containsString("B[dd]"));
		assertThat(writer.getSGFString(), containsString("W[cc]"));

		SGFReader reader = new SGFReader(writer.getSGFString());
		assertThat(reader.getGameTree(), is(notNullValue()));
	}
}