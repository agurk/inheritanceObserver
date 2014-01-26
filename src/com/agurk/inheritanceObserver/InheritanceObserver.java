package com.agurk.inheritanceObserver;

import java.io.IOException;
import com.google.common.reflect.ClassPath;
import com.google.common.reflect.ClassPath.ClassInfo;

public class InheritanceObserver {
	
	public static void main(String[] ags) {
		InheritanceBush results = new InheritanceBush();

		ClassPath foo;
		
		try {
			foo = ClassPath.from(InheritanceObserver.class.getClassLoader());
			
			for (ClassInfo bat : foo.getAllClasses()) {
				results.addResult(bat);
			}
			
//			results.printResults();
			results.findCrossPackageRelationship();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
}