package sgf;

import logic.gametree.ComplexGameTree;
import logic.gametree.GameTree;
import logic.gametree.GameTreeBuilder;
import logic.gametree.GameTreeProvider;
import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;

public class SGFReader implements GameTreeProvider {

	private GameTreeBuilder builder;

	public SGFReader(String sgfString) {
		ANTLRInputStream inputStream = new ANTLRInputStream(sgfString);
		SGFLexer lexer = new SGFLexer(inputStream);
		CommonTokenStream tokens = new CommonTokenStream(lexer);
		SGFParser parser = new SGFParser(tokens);

		builder = ComplexGameTree.getBuilder();
		SGFVisitor visitor = new SGFVisitor();
		visitor.visitCollection(parser.collection());
	}

	@Override
	public GameTree getGameTree() {
		return builder.build();
	}
}
