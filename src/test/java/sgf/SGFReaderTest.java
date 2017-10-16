package sgf;

import logic.board.Board;
import logic.gametree.GameTree;
import logic.score.BoardScorerTest;
import org.junit.Test;
import util.Move;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static util.Coords.getCoords;
import static util.Move.play;
import static util.StoneColour.BLACK;

public class SGFReaderTest {

	private static String simpleSGF = "(;FF[4]GM[1]SZ[19];B[aa])";
	private static String complexSGF = SGFReaderTest.class.getResource("/tb4.sgf").getPath();

	@Test
	public void readSimpleSGF() {
		SGFReader reader = new SGFReader(simpleSGF);
		Board board = new Board();
		board.playStone(play(getCoords("A1"), BLACK));

		GameTree tree = reader.getGameTree();

		while (tree.getNumChildren() != 0)
			tree.stepForward(0);

		assertThat(tree.getPosition(), is(board));
	}

	@Test
	public void readComplexSGF() {
		SGFReader reader = new SGFReader(loadFile(complexSGF));
		Board board = BoardScorerTest.buildTestBoard(BoardScorerTest.testBoard4, 3);

		GameTree tree = reader.getGameTree();

		while (tree.getNumChildren() != 0)
			tree.stepForward(0);

		assertThat(tree.getPosition(), is(board));
	}

	private String loadFile(String filepath) {
		InputStreamReader is = null;
		BufferedReader reader = null;
		StringBuilder builder = new StringBuilder();

		try {
			is = new FileReader(filepath);
			reader = new BufferedReader(is);

			String currentLine = null;
			while ((currentLine = reader.readLine()) != null) {
				builder.append(currentLine);
			}
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		finally {
			try {
				if ( is != null )
					is.close();

				if ( reader != null )
					reader.close();
			}
			catch (IOException e) {
				e.printStackTrace();
			}
		}

		return builder.toString();
	}
}