/*
 * generated by Xtext
 */
package org.eclipse.xtext.testlanguages.nestedRefs;


/**
 * Initialization support for running Xtext languages without Equinox extension registry.
 */
public class NestedRefsTestLanguageStandaloneSetup extends NestedRefsTestLanguageStandaloneSetupGenerated {

	public static void doSetup() {
		new NestedRefsTestLanguageStandaloneSetup().createInjectorAndDoEMFRegistration();
	}
}
