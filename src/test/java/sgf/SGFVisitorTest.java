package sgf;

import org.antlr.v4.runtime.ANTLRFileStream;
import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.io.StringWriter;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

public class SGFVisitorTest {

	private static final String testSGF = SGFVisitorTest.class.getResource("/ff4_ex.sgf").getPath();

	private SGFLexer sgfLexer;

	private SGFParser parser;
	private SGFVisitor visitor;
	private SGFErrorListener errorListener;

	@Before
	public void commonSetup() {
		ANTLRInputStream inputStream = new ANTLRInputStream("");
		sgfLexer = new SGFLexer(inputStream);
		CommonTokenStream tokenStream = new CommonTokenStream(sgfLexer);

		parser = new SGFParser(tokenStream);

		StringWriter errorWriter = new StringWriter();
		errorListener = new SGFErrorListener(errorWriter);
		parser.removeErrorListeners();
		parser.addErrorListener(errorListener);

		visitor = new SGFVisitor();
	}

	@Test
	public void sgfRecognition() throws IOException {
		loadFile(testSGF);

		SGFParser.CollectionContext context = parser.collection();
		visitor.visitCollection(context);

		assertThat(errorListener.getSymbol(), is(nullValue()));
	}

	private void loadFile(String filename) throws IOException {
		ANTLRInputStream inputStream = new ANTLRFileStream(filename);
		sgfLexer.setInputStream(inputStream);
	}

	@Test
	public void emptyIsError() {
		SGFParser.CollectionContext context = parser.collection();
		visitor.visitCollection(context);

		assertThat(errorListener.getSymbol(), is("<EOF>"));
	}

	@Test
	public void baseCase() {
		loadString("(;)");

		SGFParser.CollectionContext context = parser.collection();
		visitor.visitCollection(context);

		assertThat(errorListener.getSymbol(), is(nullValue()));
	}

	private void loadString(String string) {
		ANTLRInputStream inputStream = new ANTLRInputStream(string);
		sgfLexer.setInputStream(inputStream);
	}

	@Test
	public void gametreeNeedsNodes() {
		loadString("()");

		SGFParser.CollectionContext context = parser.collection();
		visitor.visitCollection(context);

		assertThat(errorListener.getSymbol(), is(")"));
	}

	@Test
	public void ignoreInitialJunk() {
		loadString("adgt56srse;))))(;)");

		SGFParser.CollectionContext context = parser.collection();
		visitor.visitCollection(context);

		assertThat(errorListener.getSymbol(), is(nullValue()));
	}
}