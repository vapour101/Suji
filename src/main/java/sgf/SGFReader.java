package sgf;

import logic.gametree.GameTree;
import logic.gametree.GameTreeProvider;
import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;

public class SGFReader implements GameTreeProvider {

	SGFParser parser;
	SGFVisitor visitor;

	public SGFReader(String sgfString) {
		ANTLRInputStream inputStream = new ANTLRInputStream(sgfString);
		SGFLexer lexer = new SGFLexer(inputStream);
		CommonTokenStream tokens = new CommonTokenStream(lexer);
		parser = new SGFParser(tokens);

		visitor = new SGFVisitor();
	}

	@Override
	public GameTree getGameTree() {
		return visitor.visitCollection(parser.collection());
	}
}
