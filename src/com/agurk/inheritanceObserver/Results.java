package com.agurk.inheritanceObserver;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.common.reflect.ClassPath.ClassInfo;

public class Results {
	
	Map<String, ClassDetails> results = new HashMap<String, ClassDetails>();
	List<ClassDetails> Heads = new ArrayList<ClassDetails>();
	
	private final static String PRINT_INDENT = " -- ";
	
	public final static ClassDetails INTERFACE_HEAD = new ClassDetails("Interface_Head");
	
	static {
		INTERFACE_HEAD.setIsInterface(true);
	}
	
	public Results() {
		super();
		Heads.add(INTERFACE_HEAD);
	}
	
	@SuppressWarnings("rawtypes")
	public void addResult(ClassInfo classIn) {
		try {
			Class clazz = classIn.load();
			
			boolean isInterface = clazz.isInterface();
			String name = clazz.getCanonicalName();
			
			ClassDetails head = INTERFACE_HEAD;
			
			if (! isInterface ) {
				head = getOrCreateHead(clazz.getSuperclass().getCanonicalName());
			}
			
			ClassDetails details = getOrCreateClassDetails(name);
			details.setIsInterface(isInterface);
			details.setParent(head);
		
			
			for (Class interfaceClazz : clazz.getInterfaces()) {
				ClassDetails interfaceDetails = getOrCreateInterface(interfaceClazz.getCanonicalName());
				details.addInterface(interfaceDetails);
			}
			
			
		} catch (NoClassDefFoundError e) {
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

	private ClassDetails getOrCreateClassDetails(String name) {
		ClassDetails returnable = results.get(name);
		if (returnable == null) {
			returnable = new ClassDetails(name);
			results.put(name, returnable);
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
	
	public void printResults() {
		for (ClassDetails details : Heads) {
			System.out.println(details.getName());
			printChildren(details, details.getName() + PRINT_INDENT);
		}
		System.out.println("done");
	}
	
	private void printChildren(ClassDetails parent, String indent) {
		Set<ClassDetails> children = parent.getChildren();
		for (ClassDetails child : children) {
			if (child == null || child.getName() == null || child.getName().equals("null"))
				break;
			System.out.println(indent + child.getName());
			printChildren(child, indent + child.getName() + PRINT_INDENT);
		}
	}
}
