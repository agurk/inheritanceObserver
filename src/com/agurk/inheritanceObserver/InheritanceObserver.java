package com.agurk.inheritanceObserver;

import java.io.IOException;
import java.util.List;
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

			List<List <ClassDetails>> relationships = InheritanceController.findCrossPackageRelationship(results, "com.calypso");
			for ( List<ClassDetails> result : relationships ) {
				System.out.print( result.get(0).isInterface() );
				System.out.print( ',' );
				System.out.print( result.get(0).getCanonicalClassName() );
				System.out.print( ',' );
				System.out.print( result.get(1).getCanonicalClassName() );
				System.out.println();
			}
			
			printChildren("pohjola.tk.report.PohDefaultReportOutput", results);
			printChildren("com.calypso.tk.report.DefaultReportOutput", results);
			printChildren("pohjola.tk.report.threading.PohThreadableDefaultReportOutput", results);
			
			System.out.println("");
			
//			printChildren("PohThreadableReportStyle", results);
//			printChildren("", results);
			
			printChildren("pohjola.tk.report.PohTradeAuditReportStyle", results);
			printChildren("com.calypso.tk.report.TradeAuditReportStyle", results);
			System.out.println("");
			
			printChildren("pohjola.apps.reporting.PohTradeReportTemplatePanel", results);
			printChildren("com.calypso.apps.reporting.TradeReportTemplatePanel", results);
			System.out.println("Next");
			
			printChildren("pohjola.apps.reporting.PohCashFlowReportTemplatePanel", results);
			printChildren("com.calypso.apps.reporting.CashFlowReportTemplatePanel", results);
			
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	private static void printChildren(String className, InheritanceBush data) {
		
//		ClassDetails parent = InheritanceController.getChildrenForParent(parent, data);
		
		System.out.println(className);
		
		for ( ClassDetails child : InheritanceController.getChildrenForParent(className, data) ) {
			System.out.println(" --" + child.getCanonicalClassName());
		}
	}
}