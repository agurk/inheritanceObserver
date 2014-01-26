package com.agurk.inheritanceObserver;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.google.common.reflect.ClassPath.ClassInfo;

public class InheritanceBush {
	
	Map<String, ClassDetails> results = new HashMap<String, ClassDetails>();
	List<ClassDetails> Heads = new ArrayList<ClassDetails>();
	
	private final static String PRINT_INDENT = " -- ";
	
	public final static ClassDetails INTERFACE_HEAD = new ClassDetails("Interface_Head");
	
	static {
		INTERFACE_HEAD.setIsInterface(true);
	}
	
	public InheritanceBush() {
		super();
		Heads.add(INTERFACE_HEAD);
	}
	
	@SuppressWarnings("rawtypes")
	public void addResult(ClassInfo classIn) {
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
			printChildren(details, details.getCanonicalClassName() + PRINT_INDENT, results);
		}
		return results.toString();
	}
	
	private void printChildren(ClassDetails parent, String indent, StringBuilder results) {
		for (ClassDetails child : parent.getChildren() ) {
			if (child == null || child.getCanonicalClassName() == null || child.getCanonicalClassName().equals("null"))
				break;
			results.append(indent);
			results.append(child.getCanonicalClassName());
			results.append("\n");
			printChildren(child, indent + child.getCanonicalClassName() + PRINT_INDENT, results);
		}
	}
	
	public void findCrossPackageRelationship() {
		for (ClassDetails details : Heads) {
			checkChild(details);
		}
	}
	
	private void checkChild(ClassDetails parent) {
		for (ClassDetails child : parent.getChildren()) {
			if (child == null || child.getPackageName() == null || child.getCanonicalClassName() == null )//child.getPackageName().equals("null"))
				break;
			
			if (parent == null)
				break;
			
			if (!parent.getPackageName().equals(child.getPackageName())) {
				System.out.println(parent.getCanonicalClassName() + PRINT_INDENT + child.getCanonicalClassName());
			}
			
			checkChild(child);
		}
	}
}
