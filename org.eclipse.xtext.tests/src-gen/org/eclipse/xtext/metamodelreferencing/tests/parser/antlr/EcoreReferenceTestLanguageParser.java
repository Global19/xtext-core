/*
 * generated by Xtext
 */
package org.eclipse.xtext.metamodelreferencing.tests.parser.antlr;

import com.google.inject.Inject;
import org.eclipse.xtext.metamodelreferencing.tests.parser.antlr.internal.InternalEcoreReferenceTestLanguageParser;
import org.eclipse.xtext.metamodelreferencing.tests.services.EcoreReferenceTestLanguageGrammarAccess;
import org.eclipse.xtext.parser.antlr.AbstractAntlrParser;
import org.eclipse.xtext.parser.antlr.XtextTokenStream;

public class EcoreReferenceTestLanguageParser extends AbstractAntlrParser {

	@Inject
	private EcoreReferenceTestLanguageGrammarAccess grammarAccess;

	@Override
	protected void setInitialHiddenTokens(XtextTokenStream tokenStream) {
		tokenStream.setInitialHiddenTokens("RULE_WS", "RULE_ML_COMMENT", "RULE_SL_COMMENT");
	}
	

	@Override
	protected InternalEcoreReferenceTestLanguageParser createParser(XtextTokenStream stream) {
		return new InternalEcoreReferenceTestLanguageParser(stream, getGrammarAccess());
	}

	@Override 
	protected String getDefaultRuleName() {
		return "Model";
	}

	public EcoreReferenceTestLanguageGrammarAccess getGrammarAccess() {
		return this.grammarAccess;
	}

	public void setGrammarAccess(EcoreReferenceTestLanguageGrammarAccess grammarAccess) {
		this.grammarAccess = grammarAccess;
	}
}