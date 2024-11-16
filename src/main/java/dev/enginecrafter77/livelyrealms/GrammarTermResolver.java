package dev.enginecrafter77.livelyrealms;

import java.util.Optional;

public interface GrammarTermResolver {
	public static String EPSILON = "epsilon";

	public Optional<GrammarNonterminal> getNonterminal(String name);
}
