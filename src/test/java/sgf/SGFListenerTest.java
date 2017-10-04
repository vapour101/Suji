package sgf;

import org.antlr.v4.runtime.*;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.io.StringWriter;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.*;

public class SGFListenerTest {

	private static final String testSGF = SGFListenerTest.class.getResource("/ff4_ex.sgf").getPath();

	private SGFLexer sgfLexer;

	private SGFParser parser;
	private SGFListener listener;
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

		listener = new SGFListener();
	}

	private void loadFile(String filename) throws IOException {
		ANTLRInputStream inputStream = new ANTLRFileStream(filename);
		sgfLexer.setInputStream(inputStream);
	}

	private void loadString(String string) {
		ANTLRInputStream inputStream = new ANTLRInputStream(string);
		sgfLexer.setInputStream(inputStream);
	}

	@Test
	public void sgfRecognition() throws IOException {
		loadFile(testSGF);

		SGFParser.CollectionContext context = parser.collection();
		listener.enterCollection(context);

		assertThat(errorListener.getSymbol(), is(nullValue()));
	}

	@Test
	public void emptyIsError() {
		SGFParser.CollectionContext context = parser.collection();
		listener.enterCollection(context);

		assertThat(errorListener.getSymbol(), is(not(nullValue())));
		assertThat(errorListener.getSymbol(), is("<EOF>"));
	}

	@Test
	public void baseCase() {
		loadString("(;)");

		SGFParser.CollectionContext context = parser.collection();
		listener.enterCollection(context);

		assertThat(errorListener.getSymbol(), is(nullValue()));
	}

}