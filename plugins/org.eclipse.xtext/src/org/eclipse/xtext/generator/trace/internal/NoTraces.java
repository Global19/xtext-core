/*******************************************************************************
 * Copyright (c) 2015 itemis AG (http://www.itemis.eu) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package org.eclipse.xtext.generator.trace.internal;

import org.eclipse.xtext.generator.trace.AbsoluteURI;
import org.eclipse.xtext.generator.trace.SourceRelativeURI;
import org.eclipse.xtext.workspace.IProjectConfig;
import org.eclipse.xtext.workspace.IWorkspaceConfig;

/**
 * Abstract null implementation for platform specific enhancements to the trace API.
 * 
 * @author Sebastian Zarnekow - Initial contribution and API
 */
public class NoTraces<PlatformResource, Trace extends IPlatformSpecificTrace<PlatformResource,?,?,Trace>> implements IPlatformSpecificTraceProvider<PlatformResource, Trace> {

	@Override
	public Trace getTraceToSource(PlatformResource derivedResource) {
		return null;
	}

	@Override
	public Trace getTraceToTarget(PlatformResource sourceResource) {
		return null;
	}

	@Override
	public Trace getTraceToSource(AbsoluteURI absoluteDerivedResource, IWorkspaceConfig context) {
		return null;
	}

	@Override
	public Trace getTraceToSource(SourceRelativeURI srcRelativeDerivedResource, IProjectConfig project) {
		return null;
	}

	@Override
	public Trace getTraceToTarget(AbsoluteURI absoluteSourceResource, IWorkspaceConfig context) {
		return null;
	}

	@Override
	public Trace getTraceToTarget(SourceRelativeURI srcRelativeSourceResource, IProjectConfig project) {
		return null;
	}
		
}