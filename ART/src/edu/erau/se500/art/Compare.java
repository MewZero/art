package edu.erau.se500.art;

import java.util.ArrayList;
import java.util.List;

public class Compare {

	static List<ExtractedClass> UMLClasses = new ArrayList<ExtractedClass>();
	static List<ExtractedClass> javaClasses = new ArrayList<ExtractedClass>();

	//static List<CompareResult> results = new ArrayList<CompareResult>();
	static List<CompareClassResult> results = new ArrayList<CompareClassResult>();

	static int parameterCount;
	static int parameterMatch;

	public static void compare(boolean doForward) {
		
		if (doForward) compareForwards();
		else compareBackwards();
	}

	private static void compareForwards() {		

		for (ExtractedClass umlClass : UMLClasses) {
			CompareResult thisResult = new CompareResult(umlClass.name);

			for (ExtractedClass javaClass : javaClasses) {

				if (umlClass.name.toLowerCase().equals(javaClass.name.toLowerCase())) {

					thisResult.matchFound = true;

					if (umlClass.parentClass.toLowerCase().equals(javaClass.parentClass.toLowerCase())) {

						thisResult.parentClassMatchFound = true;
					}

					for (String umlInterfaceClass : umlClass.interfaceClasses) {
						thisResult.interfaceClassTotal++;
						for (String javaInterfaceClass : javaClass.interfaceClasses) {

							if (umlInterfaceClass.toLowerCase().equals(javaInterfaceClass.toLowerCase())) {

								thisResult.interfaceClassFound++;

								break;
							}
						}
					}

					for (ExtractedAttribute umlAttribute : umlClass.attributes) {
						thisResult.attributesTotal++;
						for (ExtractedAttribute javaAttribute : javaClass.attributes) {

							if (umlAttribute.name.toLowerCase().equals(javaAttribute.name.toLowerCase()) && 
									umlAttribute.type.toLowerCase().equals(javaAttribute.type.toLowerCase())) {

								thisResult.attributesFound++;

								if (umlAttribute.type.toLowerCase().equals(javaAttribute.type.toLowerCase())) {

									thisResult.attributeTypesFound++;
								}

								if (umlAttribute.accessModifier.equals(javaAttribute.accessModifier)) {

									thisResult.attributeAccessModifiersFound++;
								}

								if ((umlAttribute.isFinal == javaAttribute.isFinal) && 
										(umlAttribute.isStatic == javaAttribute.isStatic)) {

									thisResult.attributeNonAccessModifiersFound++;	
								}
								break;
							}
						}
					}

					for (ExtractedMethod umlMethod : umlClass.methods) {

						parameterCount = 0;
						parameterMatch = 0;					
						for (ExtractedMethod javaMethod : javaClass.methods) {

							if (umlMethod.name.toLowerCase().equals(javaMethod.name.toLowerCase()) && 
									umlMethod.type.toLowerCase().equals(javaMethod.type.toLowerCase())) {

								thisResult.methodsFound++;

								if (umlMethod.type.toLowerCase().equals(javaMethod.type.toLowerCase())) {

									thisResult.methodTypesFound++;
								}

								if (umlMethod.accessModifier.equals(javaMethod.accessModifier)) {

									thisResult.methodAccessModifiersFound++;
								}

								if ((umlMethod.isFinal == javaMethod.isFinal) && 
										(umlMethod.isStatic == javaMethod.isStatic) && 
										(umlMethod.isAbstract == javaMethod.isAbstract)) {

									thisResult.methodNonAccessModifiersFound++;
								}

								for (String umlParameter : umlMethod.parameters) {
									parameterCount++;
									for (String javaParameter : javaMethod.parameters) {

										if (umlParameter.toLowerCase().equals(javaParameter.toLowerCase())) {

											parameterMatch++;

;											break;
										}
									}
								}
								break;
							}
						}
					}
					break;
				}
			}
		}
	}

	private static void compareBackwards() {

	}
}

