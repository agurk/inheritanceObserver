package com.agurk.inheritanceObserver;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class InheritanceController {
	
	public static Set<ClassDetails> getChildrenForParent(String parent, InheritanceBush data) {
		ClassDetails returned = data.getMember(parent);
		
		if (returned != null) {
			return returned.getChildren();
		} else {
			return new HashSet<ClassDetails>();
		}
	}
	
	public static List<List <ClassDetails>> findCrossPackageRelationship(InheritanceBush data, String initialPackage) {
		List<List <ClassDetails>> results = new ArrayList<List <ClassDetails>>();
		Set<ClassDetails> processedPackages = new HashSet<ClassDetails>();
		
		for (ClassDetails details : data.getHeads()) {
			checkChildForCrossCrossPackageRelationship(details, initialPackage, results, processedPackages);
		}
		
		return results;
	}
	
	private static void checkChildForCrossCrossPackageRelationship(ClassDetails parent, String initialPackage,
			List<List <ClassDetails>> results, Set<ClassDetails> processedPackages) {
		
//		if (processedPackages.contains(parent))
//			return;
//		
//		processedPackages.add(parent);
		
		for (ClassDetails child : parent.getChildren()) {
			
			if ( child.getCanonicalClassName() ==  null )
				break;
			
			
			String packageHead;
			String childPackageHead;
			
			try {
				packageHead= parent.getPackageName().substring(0, initialPackage.length());
			} catch (StringIndexOutOfBoundsException e) {
				packageHead = parent.getPackageName();
			}
			
			try {
				childPackageHead= child.getPackageName().substring(0, initialPackage.length());
			} catch (StringIndexOutOfBoundsException e) {
				childPackageHead = child.getPackageName();
			}
			
			if (	
					initialPackage.equals(packageHead) &&
					!packageHead.equals(childPackageHead) &&
					!parent.getPackageName().equals(child.getPackageName()))
			{
				
				ArrayList<ClassDetails> result = new ArrayList<ClassDetails>();
				result.add(parent);
				result.add(child);
				
				results.add(result);
			}

			checkChildForCrossCrossPackageRelationship(child, initialPackage, results, processedPackages);
		}
	}
	
	
}
