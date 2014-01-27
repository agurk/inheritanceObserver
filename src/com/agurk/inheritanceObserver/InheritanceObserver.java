package com.agurk.inheritanceObserver;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import com.google.common.reflect.ClassPath;
import com.google.common.reflect.ClassPath.ClassInfo;

public class InheritanceObserver {
	
	public static void main(String[] ags) {
		InheritanceBush results = new InheritanceBush();

		ClassPath foo;
		
		try {
			foo = ClassPath.from(InheritanceObserver.class.getClassLoader());
			
			for (ClassInfo bat : foo.getAllClasses()) {
				results.addClass(bat);
			}
			
			Set<String> filters = new HashSet<String>();
			filters.add("java.lang");
			filters.add(InheritanceBush.INTERFACE_HEAD_PACKAGE);
			
			results.findCrossPackageRelationship(filters);
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
}