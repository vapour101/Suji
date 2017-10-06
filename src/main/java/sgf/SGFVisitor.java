package sgf;

import logic.gametree.ComplexGameTree;
import logic.gametree.GameTreeBuilder;
import logic.gametree.GameTreeBuilder.GameTreeProperty;
import sgf.SGFParser.*;

import java.util.Collection;
import java.util.LinkedList;

public class SGFVisitor extends SGFParserBaseVisitor<GameTreeBuilder> {

	private GameTreeBuilder builder;

	@Override
	public GameTreeBuilder visitCollection(CollectionContext ctx) {
		GameTreeBuilder result = visit(ctx.getChild(ctx.getChildCount() - 1));
		ctx.removeLastChild();

		return result;
	}

	@Override
	public GameTreeBuilder visitGametree(GametreeContext ctx) {
		GameTreeBuilder result = null;

		if ( ctx.sequence() != null )
			result = visit(ctx.sequence());
		else
			result = ComplexGameTree.getBuilder();

		result.gotoRoot();

		for (GametreeContext tree : ctx.gametree()) {
			result.addVariation(visit(tree));
		}

		return result;
	}

	@Override
	public GameTreeBuilder visitSequence(SequenceContext ctx) {
		if ( builder != null )
			throw new IllegalStateException("Builder is in use.");

		builder = ComplexGameTree.getBuilder();

		visitChildren(ctx);

		GameTreeBuilder result = builder;
		builder = null;

		return result;
	}

	@Override
	public GameTreeBuilder visitNode(NodeContext ctx) {
		if ( builder == null )
			throw new IllegalStateException("Builder has not been set.");

		builder.appendNode();
		visitChildren(ctx);

		return null;
	}

	@Override
	public GameTreeBuilder visitProperty(PropertyContext ctx) {
		if ( builder == null )
			throw new IllegalStateException("Builder has not been set.");

		String identifier = ctx.identifier().IDENTIFIER().getSymbol().getText();
		Collection<String> values = new LinkedList<>();
		StringVisitor valueVisitor = new StringVisitor();

		for (ValueContext value : ctx.value()) {
			values.add(valueVisitor.visitValue(value));
		}

		GameTreeProperty property = GameTreeProperty.getSGFProperty(identifier, values);

		builder.appendProperty(property);

		return null;
	}

	private class StringVisitor extends SGFParserBaseVisitor<String> {

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
