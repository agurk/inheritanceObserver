package com.agurk.inheritanceObserver;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.common.reflect.ClassPath.ClassInfo;

public class InheritanceBush {
	
	Map<String, ClassDetails> results = new HashMap<String, ClassDetails>();
	List<ClassDetails> Heads = new ArrayList<ClassDetails>();
	
	private final static String PRINT_INDENT = " -- ";
	
	public final static ClassDetails INTERFACE_HEAD = new ClassDetails("Interface_Head");
	public final static String INTERFACE_HEAD_PACKAGE = "INTERFACE_HEAD_PACKAGE";
	
	static {
		INTERFACE_HEAD.setIsInterface(true);
		INTERFACE_HEAD.setPackageName(INTERFACE_HEAD_PACKAGE);
	}
	
	public InheritanceBush() {
		super();
		Heads.add(INTERFACE_HEAD);
	}
	
	@SuppressWarnings("rawtypes")
	public void addClass(ClassInfo classIn) {
		try {
			Class clazz = classIn.load();
			
			ClassDetails head = INTERFACE_HEAD;
			
			if (! clazz.isInterface() ) {
				head = getOrCreateHead(clazz.getSuperclass().getCanonicalName());
			}
			
			ClassDetails details = getOrCreateClassDetails(clazz);
			details.setParent(head);
		
			
			for (Class interfaceClazz : clazz.getInterfaces()) {
				ClassDetails interfaceDetails = getOrCreateInterface(interfaceClazz.getCanonicalName());
				details.addInterface(interfaceDetails);
			}
			
			
		} catch (NoClassDefFoundError e) {
			System.err.println("Could not find class: " + e.getLocalizedMessage());
		} catch (IncompatibleClassChangeError e) {
			System.err.println("Error reading name from class: " + e.getLocalizedMessage());
		} catch (VerifyError e) {
			System.err.println("Error with class: " + e.getLocalizedMessage());
		} catch (InternalError e) {
			System.err.println("Error: " + e.getLocalizedMessage());
		} catch (SecurityException e) {
			System.err.println("Security Error: " + e.getLocalizedMessage());
		}
		
	}
	
	private ClassDetails getOrCreateInterface(String name) {
		ClassDetails returnable = results.get(name);
		if (returnable == null) {
			returnable = new ClassDetails(name);
			returnable.setParent(INTERFACE_HEAD);
			results.put(name, returnable);
		}
		return returnable;
	}

	@SuppressWarnings("rawtypes")
	private ClassDetails getOrCreateClassDetails(Class clazz) {
		String name = clazz.getCanonicalName();
		ClassDetails returnable = results.get(name);
		if (returnable == null) {
			returnable = new ClassDetails(clazz);
			results.put(name, returnable);
		} else {
			returnable.updateDetails(clazz);
		}
		Heads.remove(returnable);
		return returnable;
	}
	
	private ClassDetails getOrCreateHead(String name) {
		ClassDetails returnable;
		returnable = results.get(name);
		if (returnable == null) {
			returnable = new ClassDetails(name);
			returnable.setIsInterface(false);
			Heads.add(returnable);
			results.put(name, returnable);
		}
		return returnable;
	}
	
	public String toString() {
		StringBuilder results = new StringBuilder();
		for (ClassDetails details : Heads) {
			results.append(details.getCanonicalClassName());
			results.append("\n");
			toStringChildren(details, details.getCanonicalClassName() + PRINT_INDENT, results);
		}
		return results.toString();
	}
	
	private void toStringChildren(ClassDetails parent, String indent, StringBuilder results) {
		for (ClassDetails child : parent.getChildren() ) {
			if (child == null || child.getCanonicalClassName() == null || child.getCanonicalClassName().equals("null"))
				break;
			results.append(indent);
			results.append(child.getCanonicalClassName());
			results.append("\n");
			toStringChildren(child, indent + child.getCanonicalClassName() + PRINT_INDENT, results);
		}
	}
	
	public void findCrossPackageRelationship(Set<String> filters) {
		for (ClassDetails details : Heads) {
			checkChild(details, filters);
		}
	}
	
	private void checkChild(ClassDetails parent, Set<String> filters) {
		for (ClassDetails child : parent.getChildren()) {
			
			if ( child.getCanonicalClassName() ==  null )
				break;
			
			String parentInitialPackage = getPackageCompare(parent);
			String childInitialPackage = getPackageCompare(child);
			
			if (	!filters.contains(parent.getPackageName()) && 
					!filters.contains(parent.getCanonicalClassName()) &&
//					!parent.getPackageName().equals(child.getPackageName()))
					!parentInitialPackage.equals(childInitialPackage)    )
			{
				System.out.println(parent.getCanonicalClassName() + PRINT_INDENT + child.getCanonicalClassName());
			}

			checkChild(child, filters);
		}
	}
	
	private String getPackageCompare(final ClassDetails in) {
		String[] parts = in.getPackageName().split("\\.");
		if (parts.length > 0) {
			return parts[0];
		}
		return in.getPackageName();
	}
	
	public void findCrossPackageRelationship(String initialPackage) {
		for (ClassDetails details : Heads) {
			checkChild(details, initialPackage);
		}
	}
	
	private void checkChild(ClassDetails parent, String initialPackage) {
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
//					!filters.contains(parent.getPackageName()) && 
//					!filters.contains(parent.getCanonicalClassName()) &&
					initialPackage.equals(packageHead) &&
					!packageHead.equals(childPackageHead) &&
					!parent.getPackageName().equals(child.getPackageName()))
//					!parentInitialPackage.equals(childInitialPackage)    )
			{
				System.out.println(parent.isInterface() + PRINT_INDENT + parent.getCanonicalClassName() + PRINT_INDENT + child.getCanonicalClassName());
			}

			checkChild(child, initialPackage);
		}
	}
	
//	private void checkChildDifferentPackage(ClassDetails parent) {
//		for (ClassDetails child : parent.getChildren()) {
//			
//			if ( child.getCanonicalClassName() ==  null )
//				break;
//			
//			String packageName = child.getPackageName();
//		
//			if (!filters.contains(parent.getPackageName()) && !parent.getPackageName().equals(child.getPackageName())) {
//				System.out.println(parent.getCanonicalClassName() + PRINT_INDENT + child.getCanonicalClassName());
//			}
//
//			checkChildDifferentPackage(child);
//		}		
//	}
}
