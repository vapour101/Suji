package sgf;

import logic.gametree.ComplexTree;
import logic.gametree.GameTree;
import logic.gametree.GameTreeBuilder;
import logic.gametree.GameTreeBuilder.GameTreeProperty;
import logic.gametree.GameTreeIterator;
import sgf.SGFParser.*;

import java.util.Vector;

public class SGFVisitor extends SGFParserBaseVisitor<GameTree> {

	private GameTreeIterator iterator;

	@Override
	public GameTree visitCollection(CollectionContext ctx) {
		GameTree result = new ComplexTree();
		iterator = result.getRoot();

		visit(ctx.getChild(ctx.getChildCount() - 1));
		ctx.removeLastChild();

		return result;
	}

	@Override
	public GameTree visitGametree(GametreeContext ctx) {
		if ( ctx.sequence() != null )
			visit(ctx.sequence());

		for (GametreeContext tree : ctx.gametree())
			visit(tree);

		return null;
	}

	@Override
	public GameTree visitSequence(SequenceContext ctx) {
		if ( iterator == null )
			throw new IllegalStateException("Iterator has not been set.");

		visitChildren(ctx);

		return null;
	}

	@Override
	public GameTree visitNode(NodeContext ctx) {
		if ( iterator == null )
			throw new IllegalStateException("Iterator has not been set.");

		iterator.stepForward();
		visitChildren(ctx);
		iterator.stepBack();

		return null;
	}

	@Override
	public GameTree visitProperty(PropertyContext ctx) {
		if ( iterator == null )
			throw new IllegalStateException("Iterator has not been set.");

		String identifier = ctx.identifier().IDENTIFIER().getSymbol().getText();
		Vector<String> values = new Vector<>();
		StringVisitor valueVisitor = new StringVisitor();

		for (ValueContext value : ctx.value()) {
			values.add(valueVisitor.visitValue(value));
		}

		GameTreeProperty property = GameTreeProperty.getSGFProperty(identifier, values);

		iterator.addProperty(property);

		return null;
	}

	private class StringVisitor extends SGFParserBaseVisitor<String> {

		@Override
		public String visitValue(ValueContext ctx) {
			return visit(ctx.valueornull());
		}

		@Override
		public String visitNovalue(NovalueContext ctx) {
			return "";
		}

		@Override
		public String visitHasvalue(HasvalueContext ctx) {
			return ctx.VALUE().getSymbol().getText();
		}
	}
}
