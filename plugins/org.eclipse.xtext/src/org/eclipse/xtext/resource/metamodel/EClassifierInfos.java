/*******************************************************************************
 * Copyright (c) 2008 itemis AG (http://www.itemis.eu) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 *******************************************************************************/
package org.eclipse.xtext.resource.metamodel;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.xtext.AbstractMetamodelDeclaration;
import org.eclipse.xtext.EcoreUtil2;
import org.eclipse.xtext.TypeRef;
import org.eclipse.xtext.resource.metamodel.EClassifierInfo.EClassInfo;
import org.eclipse.xtext.util.Pair;
import org.eclipse.xtext.util.Tuples;

/**
 * A possible extension would be to normalize the type hierarchy and remove
 * redundant supertype references. We currently don't think thats necessary as
 * EMF handles multiple inheritance gracefully.
 * 
 * @author Jan K�hnlein - Initial contribution and API
 * @author Sebastian Zarnekow
 */
public class EClassifierInfos {

	private Map<Pair<String, String>, EClassifierInfo> infoMap = new HashMap<Pair<String, String>, EClassifierInfo>();
	
	private EClassifierInfos parent;
	
	public EClassifierInfos getParent() {
		return parent;
	}

	public void setParent(EClassifierInfos parent) {
		this.parent = parent;
	}

	public boolean addInfo(TypeRef typeRef, EClassifierInfo metatypeInfo) {
		if (typeRef.getMetamodel() == null || typeRef.getType() == null)
			throw new NullPointerException();
		return addInfo(typeRef.getMetamodel(), typeRef.getType().getName(), metatypeInfo);
	}

	public boolean addInfo(AbstractMetamodelDeclaration alias, String name, EClassifierInfo metatypeInfo) {
		return infoMap.put(getKey(alias, name), metatypeInfo) == null;
	}

	private Pair<String, String> getKey(AbstractMetamodelDeclaration metamodelDecl, String name) {
		if (metamodelDecl == null || name == null)
			throw new NullPointerException("metamodelDecl: " + metamodelDecl + " / name: " + name);
		return Tuples.create(metamodelDecl.getEPackage().getNsURI(), name);
	}

	public EClassifierInfo getInfo(TypeRef typeRef) {
		if (typeRef.getType() == null)
			return null;
		if (typeRef.getMetamodel() == null)
			throw new NullPointerException();
		return getInfo(typeRef.getMetamodel(), typeRef.getType().getName());
	}
	
	public EClassifierInfo getInfo(AbstractMetamodelDeclaration alias, String name) {
		return getInfo(getKey(alias, name));
	}

	private EClassifierInfo getInfo(Pair<String, String> qualifiedName) {
		return infoMap.get(qualifiedName);
	}

	public EClassifierInfo getInfo(EClassifier eClassifier) {
		for (EClassifierInfo info : infoMap.values())
			if (info.getEClassifier().equals(eClassifier))
				return info;
		throw new NullPointerException("cannot find type info for classifier '" + eClassifier.getName() + "'");
	}

	public EClassifierInfo getInfoOrNull(EClassifier eClassifier) {
		for (EClassifierInfo info : infoMap.values()) {
			if (info.getEClassifier().equals(eClassifier))
				return info;
		}
		return parent == null ? null : parent.getInfoOrNull(eClassifier);
	}
	
	private EClassifierInfo getCompatibleType(EClassifierInfo infoA, EClassifierInfo infoB) {
		if (infoA.equals(infoB))
			return infoA;

		if (infoA.getEClassifier() instanceof EDataType || infoB.getEClassifier() instanceof EDataType)
			throw new IllegalArgumentException(
					"Simple Datatypes (lexer rules or keywords) do not have a common supertype (" + infoA + ", "
							+ infoB + ")");

		EClassifier compatibleType = EcoreUtil2.getCompatibleType((EClass) infoA.getEClassifier(), (EClass) infoB
				.getEClassifier());
		
		return getInfoOrNull(compatibleType);
	}

	public EClassifierInfo getCompatibleTypeOf(Collection<EClassifierInfo> types) {
		Iterator<EClassifierInfo> i = types.iterator();
		if (!i.hasNext())
			throw new IllegalArgumentException("Empty set of types cannot have a compatible type.");

		EClassifierInfo result = i.next();
		while (i.hasNext())
			result = getCompatibleType(result, i.next());

		return result;
	}

	public EClassifier getCompatibleTypeNameOf(Collection<EClassifier> classifiers, boolean useParent) {
		final Collection<EClassifierInfo> types = new HashSet<EClassifierInfo>();
		for (EClassifier classifier : classifiers) {
			final EClassifierInfo info = getInfoOrNull(classifier);
			if (info == null)
				return null;
			types.add(info);
		}

		final EClassifierInfo compatibleType = getCompatibleTypeOf(types);
		if (compatibleType != null)
			return compatibleType.getEClassifier();
		else
			return EcorePackage.Literals.EOBJECT;
	}

	public List<EClassInfo> getAllEClassInfos() {
		List<EClassInfo> result = new ArrayList<EClassInfo>();
		for (EClassifierInfo classifier : this.infoMap.values())
			if (classifier instanceof EClassInfo)
				result.add((EClassInfo) classifier);

		return Collections.unmodifiableList(result);
	}

	public List<EClassInfo> getSuperTypeInfos(EClassInfo subTypeInfo) throws UnexpectedClassInfoException {
		List<EClassInfo> result = new ArrayList<EClassInfo>();
		for (EClass superType : subTypeInfo.getEClass().getESuperTypes()) {
			EClassifierInfo info = getInfo(superType);
			if (info instanceof EClassInfo)
				result.add((EClassInfo) this.getInfo(superType));
			else
				throw new UnexpectedClassInfoException(TransformationErrorCode.InvalidSupertype, subTypeInfo, info, null);
		}
		return result;
	}

}
