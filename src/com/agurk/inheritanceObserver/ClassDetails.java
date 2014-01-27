package com.agurk.inheritanceObserver;

import java.util.HashSet;
import java.util.Set;

public class ClassDetails {
	
	private String CanonicalClassName = "";
	private String PackageName = "";
	private String Name = "";

	public String getCanonicalClassName() {
		return CanonicalClassName;
	}

	// unknown to start with, but we assume we will find out if not
	private boolean isInterface = true;
	
	private ClassDetails parent = null;
	private final Set<ClassDetails> children = new HashSet<ClassDetails>();
	private final Set<ClassDetails> interfaces = new HashSet<ClassDetails>();
	

	public ClassDetails(String canonicalName){
		this.CanonicalClassName = canonicalName;
	}
	
	@SuppressWarnings("rawtypes")
	public ClassDetails(Class clazz) {
		super();
		this.updateDetails(clazz);
	}
	
	@SuppressWarnings("rawtypes")
	public void updateDetails(Class clazz) {
		this.CanonicalClassName = clazz.getCanonicalName();
//		this.Name = clazz.getSimpleName();
		Package classPackage = clazz.getPackage();
		if (classPackage != null)
			this.PackageName = classPackage.getName();
		this.isInterface = clazz.isInterface();
	}
	
	public void setParent(ClassDetails parent) {
		this.parent = parent;
		parent.addChild(this);
	}
	
	public void addChild(ClassDetails child) {
		this.children.add(child);
	}
	
	public void setPackageName(String packageName) {
		PackageName = packageName;
	}
	
	public void addInterface(ClassDetails interfaceName) {
		this.interfaces.add(interfaceName);
		interfaceName.addChild(this);
	}
	
	public void setIsInterface (boolean isInterface) {
		this.isInterface = isInterface;
	}
	
	public String getPackageName() {
		return PackageName;
	}

	public boolean isInterface() {
		return isInterface;
	}

	public ClassDetails getParent() {
		return parent;
	}

	public Set<ClassDetails> getInterfaces() {
		return interfaces;
	}
	
	public Set<ClassDetails> getChildren() {
		return children;
	}
	
	public String getName() {
		return Name;
	}

}
