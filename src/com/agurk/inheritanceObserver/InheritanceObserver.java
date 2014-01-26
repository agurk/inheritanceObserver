package com.agurk.inheritanceObserver;

import java.io.IOException;
import com.google.common.reflect.ClassPath;
import com.google.common.reflect.ClassPath.ClassInfo;

public class InheritanceObserver {
	
	public static void main(String[] ags) {
		Results results = new Results();

		ClassPath foo;
		
		try {
			foo = ClassPath.from(InheritanceObserver.class.getClassLoader());
			
			for (ClassInfo bat : foo.getAllClasses()) {
				results.addResult(bat);
			}
			
			results.printResults();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
}