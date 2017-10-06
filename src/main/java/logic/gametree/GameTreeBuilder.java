package logic.gametree;

import java.util.Collection;

public interface GameTreeBuilder {

	GameTree build();

	void gotoRoot();

	TreeNode getRoot();

	void addVariation(GameTreeBuilder subtree);

	void appendNode();

	void appendProperty(GameTreeProperty property);

	class GameTreeProperty {

		public static GameTreeProperty getSGFProperty(String identifier, Collection<String> values) {
			return new GameTreeProperty();
		}
	}
}
