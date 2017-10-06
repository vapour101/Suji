package logic.gametree;

import java.util.Vector;

public interface GameTreeBuilder {

	GameTree build();

	void gotoRoot();

	TreeNode getRoot();

	void addVariation(GameTreeBuilder subtree);

	void appendNode();

	void appendProperty(GameTreeProperty property);

	class GameTreeProperty {

		private String identifier;
		private Vector<String> values;

		public static GameTreeProperty getSGFProperty(String identifier, Vector<String> values) {
			GameTreeProperty result = new GameTreeProperty();
			result.identifier = identifier;
			result.values = values;
			return result;
		}

		public String getIdentifier() {
			return identifier;
		}

		public Vector<String> getValues() {
			return values;
		}
	}
}
