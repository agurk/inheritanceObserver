package com.agurk.inheritanceObserver;

import java.util.HashSet;
import java.util.Set;

public class ClassDetails {
	
	private String className;
	
	// unknown to start with, but we assume we will find out
	private boolean isInterface = true;
	
	private ClassDetails parent = null;
	private final Set<ClassDetails> children = new HashSet<ClassDetails>();
	private final Set<ClassDetails> interfaces = new HashSet<ClassDetails>();
	
	public Set<ClassDetails> getInterfaces() {
		return interfaces;
	}

	public Set<ClassDetails> getChildren() {
		return children;
	}

	public ClassDetails(String name){
		this.className = name;
	}
	
	public void setParent(ClassDetails parent) {
		this.parent = parent;
		parent.addChild(this);
	}
	
	public void addChild(ClassDetails child) {
		this.children.add(child);
	}
	
	public void addInterface(ClassDetails interfaceName) {
		this.interfaces.add(interfaceName);
		interfaceName.addChild(this);
	}

	public boolean isInterface() {
		return isInterface;
	}
	
	public String getName() {
		return className;
	}

	public ClassDetails getParent() {
		return parent;
	}
	
	public void setIsInterface (boolean isInterface) {
		this.isInterface = isInterface;
	}

}
