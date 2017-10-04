package sgf;

import org.antlr.v4.runtime.BaseErrorListener;
import org.antlr.v4.runtime.CommonToken;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.Recognizer;

import java.io.StringWriter;

public class SGFErrorListener extends BaseErrorListener {

	private String symbol = null;
	private StringWriter output;

	public SGFErrorListener(StringWriter writer) {
		output = writer;
	}

	public String getSymbol() {
		return symbol;
	}

	public String getOutput() {
		return output.toString();
	}

	@Override
	public void syntaxError(Recognizer<?, ?> recognizer,
							Object offendingSymbol,
							int line,
							int charPositionInLine,
							String msg,
							RecognitionException e) {
		output.write(msg);
		output.write("\n");

		if ( offendingSymbol instanceof org.antlr.v4.runtime.CommonToken )
			symbol = ((CommonToken) offendingSymbol).getText();
	}
}
