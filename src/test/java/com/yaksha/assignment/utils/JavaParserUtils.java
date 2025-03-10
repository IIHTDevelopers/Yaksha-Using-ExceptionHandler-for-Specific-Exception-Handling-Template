package com.yaksha.assignment.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ParseResult;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.expr.MemberValuePair;
import com.github.javaparser.ast.expr.NormalAnnotationExpr;
import com.github.javaparser.ast.expr.SingleMemberAnnotationExpr;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

public class JavaParserUtils {

	private static final Logger logger = Logger.getLogger(JavaParserUtils.class.getName());

	/**
	 * Loads the content of a class file from the given file path and parses it
	 * using JavaParser.
	 *
	 * @param filePath Full path to the class file.
	 * @return The class content as a String.
	 * @throws IOException If an error occurs while reading the file.
	 */
	private static String loadClassContent(String filePath) throws IOException {
		// Create a File object from the provided file path
		File participantFile = new File(filePath);
		if (!participantFile.exists()) {
			System.out.println("Error: Class file not found at path: " + filePath);
			throw new IOException("Class file not found: " + filePath);
		}

		// Read the content of the file
		try (FileInputStream fileInputStream = new FileInputStream(participantFile)) {
			byte[] bytes = fileInputStream.readAllBytes();
			return new String(bytes, StandardCharsets.UTF_8);
		}
	}

	/**
	 * Retrieves the class name from the file path.
	 * 
	 * @param filePath The file path of the class.
	 * @return The class name extracted from the file path.
	 */
	private static String getClassNameFromPath(String filePath) {
		// Extract class name from the file path (e.g., "ApiController.java" ->
		// "ApiController")
		int start = filePath.lastIndexOf("/") + 1;
		int end = filePath.lastIndexOf(".");
		return filePath.substring(start, end);
	}

	// Check if the class has the specified annotation
	public static boolean checkClassAnnotation(String filePath, String classAnnotations) {
		String classContent;
		try {
			classContent = loadClassContent(filePath);
		} catch (IOException e) {
			return false;
		}

		JavaParser javaParser = new JavaParser();
		Optional<CompilationUnit> optionalCompilationUnit = javaParser.parse(classContent).getResult();
		if (optionalCompilationUnit.isEmpty()) {
			System.out.println("Error: Failed to parse the class content at path: " + filePath);
			return false;
		}

		CompilationUnit compilationUnit = optionalCompilationUnit.get();
		boolean hasClassAnnotation = compilationUnit.getClassByName(getClassNameFromPath(filePath)).get()
				.getAnnotations().stream()
				.anyMatch(annotation -> annotation.getNameAsString().equals(classAnnotations));

		if (!hasClassAnnotation) {
			System.out.println("Error: The class " + getClassNameFromPath(filePath) + " is missing the @"
					+ classAnnotations + " annotation.");
			return false;
		}
		return true;
	}

	// Check if the method has the specified annotation
	public static boolean checkMethodAnnotation(String filePath, String methodName, String methodAnnotations) {
		String classContent;
		try {
			classContent = loadClassContent(filePath);
		} catch (IOException e) {
			return false;
		}

		JavaParser javaParser = new JavaParser();
		Optional<CompilationUnit> optionalCompilationUnit = javaParser.parse(classContent).getResult();
		if (optionalCompilationUnit.isEmpty()) {
			System.out.println("Error: Failed to parse the class content at path: " + filePath);
			return false;
		}

		CompilationUnit compilationUnit = optionalCompilationUnit.get();
		MethodDeclaration method = compilationUnit.getClassByName(getClassNameFromPath(filePath)).get()
				.getMethodsByName(methodName).stream().findFirst().orElse(null);

		if (method == null) {
			System.out.println("Error: The method " + methodName + " does not exist in the class "
					+ getClassNameFromPath(filePath) + ".");
			return false;
		}

		boolean hasMethodAnnotation = method.getAnnotationByName(methodAnnotations).isPresent();
		if (!hasMethodAnnotation) {
			System.out.println("Error: The method " + methodName + " is missing the @" + methodAnnotations
					+ " annotation in class " + getClassNameFromPath(filePath) + ".");
			return false;
		}

		return true;
	}

	// Check if the method parameter has the specified annotation
	public static boolean checkMethodParameterAnnotation(String filePath, String methodName, String paramName,
			String paramAnnotation) {
		String classContent;
		try {
			classContent = loadClassContent(filePath);
		} catch (IOException e) {
			return false;
		}

		JavaParser javaParser = new JavaParser();
		Optional<CompilationUnit> optionalCompilationUnit = javaParser.parse(classContent).getResult();
		if (optionalCompilationUnit.isEmpty()) {
			System.out.println("Error: Failed to parse the class content at path: " + filePath);
			return false;
		}

		CompilationUnit compilationUnit = optionalCompilationUnit.get();
		MethodDeclaration method = compilationUnit.getClassByName(getClassNameFromPath(filePath)).get()
				.getMethodsByName(methodName).stream().findFirst().orElse(null);

		if (method == null) {
			System.out.println("Error: The method " + methodName + " does not exist in the class "
					+ getClassNameFromPath(filePath) + ".");
			return false;
		}

		boolean hasParamAnnotation = method.getParameters().stream()
				.anyMatch(param -> param.getNameAsString().equals(paramName)
						&& param.getAnnotationByName(paramAnnotation).isPresent());

		if (!hasParamAnnotation) {
			System.out.println("Error: The parameter " + paramName + " in method " + methodName + " is missing the @"
					+ paramAnnotation + " annotation.");
			return false;
		}

		return true;
	}

	// Check if the method's return type matches the expected return type
	public static boolean checkMethodReturnType(String filePath, String methodName, String expectedReturnType) {
		String classContent;
		try {
			classContent = loadClassContent(filePath);
		} catch (IOException e) {
			return false;
		}

		JavaParser javaParser = new JavaParser();
		Optional<CompilationUnit> optionalCompilationUnit = javaParser.parse(classContent).getResult();
		if (optionalCompilationUnit.isEmpty()) {
			System.out.println("Error: Failed to parse the class content at path: " + filePath);
			return false;
		}

		CompilationUnit compilationUnit = optionalCompilationUnit.get();
		MethodDeclaration method = compilationUnit.getClassByName(getClassNameFromPath(filePath)).get()
				.getMethodsByName(methodName).stream().findFirst().orElse(null);

		if (method == null) {
			System.out.println("Error: The method " + methodName + " does not exist in the class "
					+ getClassNameFromPath(filePath) + ".");
			return false;
		}

		boolean isReturnTypeCorrect = method.getType().asString().equals(expectedReturnType);
		if (!isReturnTypeCorrect) {
			System.out.println("Error: The return type of the method " + methodName + " is not " + expectedReturnType
					+ " in class " + getClassNameFromPath(filePath) + ".");
			return false;
		}

		return true;
	}

	// Check if the method contains specific status-related keywords provided by the
	// user
	public static boolean checkStatusKeywordsInMethod(String filePath, String methodName, List<String> keywords) {
		String classContent;
		try {
			classContent = loadClassContent(filePath);
		} catch (IOException e) {
			return false;
		}

		JavaParser javaParser = new JavaParser();
		Optional<CompilationUnit> optionalCompilationUnit = javaParser.parse(classContent).getResult();
		if (optionalCompilationUnit.isEmpty()) {
			System.out.println("Error: Failed to parse the class content at path: " + filePath);
			return false;
		}

		CompilationUnit compilationUnit = optionalCompilationUnit.get();
		MethodDeclaration method = compilationUnit.getClassByName(getClassNameFromPath(filePath)).get()
				.getMethodsByName(methodName).stream().findFirst().orElse(null);

		if (method == null) {
			System.out.println("Error: The method " + methodName + " does not exist in the class "
					+ getClassNameFromPath(filePath) + ".");
			return false;
		}

		// Convert the method body to string and check if it contains any of the
		// specified keywords
		String methodBody = method.getBody().map(Object::toString).orElse("");
		boolean containsAllKeywords = keywords.stream().allMatch(methodBody::contains);

		if (!containsAllKeywords) {
			// Find missing keywords
			List<String> missingKeywords = keywords.stream().filter(keyword -> !methodBody.contains(keyword))
					.collect(Collectors.toList());

			// Print detailed error message with missing keywords
			System.out.println(
					"Error: The method " + methodName + " is missing the following required status-related keywords: "
							+ String.join(", ", missingKeywords));
			return false;
		}

		return true;
	}

	// New method to check if the annotation has a specific value
	public static boolean checkMethodAnnotationValue(String filePath, String methodName, String expectedValue)
			throws IOException {
		CompilationUnit compilationUnit = parseFile(filePath);
		return compilationUnit.findAll(MethodDeclaration.class).stream()
				.anyMatch(method -> method.getNameAsString().equals(methodName)
						&& method.getAnnotations().stream().anyMatch(annotation -> {
							if (annotation.getNameAsString().equals("GetMapping")) {
								// Check if the annotation has a value and matches the expected value
								Optional<String> annotationValue = annotation.getChildNodes().stream()
										.filter(child -> child.toString().contains(expectedValue)).map(Object::toString)
										.findFirst();
								return annotationValue.isPresent();
							}
							return false;
						}));
	}

	// Helper method to parse the file
	private static CompilationUnit parseFile(String filePath) throws IOException {
		FileInputStream fileInputStream = new FileInputStream(new File(filePath));
		JavaParser parser = new JavaParser(); // Create an instance of JavaParser
		return parser.parse(fileInputStream).getResult()
				.orElseThrow(() -> new IOException("Failed to parse the Java file"));
	}

	public static boolean isInterfaceAnnotated(String filePath, String annotationName) throws IOException {
		logger.log(Level.INFO, "Checking if interface is annotated with {0} in file {1}",
				new Object[] { annotationName, filePath });
		FileInputStream in = new FileInputStream(filePath);
		ParseResult<CompilationUnit> parseResult = new JavaParser().parse(in);

		if (parseResult.isSuccessful() && parseResult.getResult().isPresent()) {
			CompilationUnit cu = parseResult.getResult().get();

			InterfaceAnnotationVisitor visitor = new InterfaceAnnotationVisitor(annotationName);
			cu.accept(visitor, null);
			return visitor.isAnnotated();
		} else {
			logger.log(Level.SEVERE, "Failed to parse the file: {0}", filePath);
			return false;
		}
	}

	public static String getMethodAnnotationValue(String filePath, String methodName, String annotationName)
			throws IOException {
		logger.log(Level.INFO, "Checking if method {0} in interface has annotation {1} in file {2}",
				new Object[] { methodName, annotationName, filePath });
		FileInputStream in = new FileInputStream(filePath);
		ParseResult<CompilationUnit> parseResult = new JavaParser().parse(in);

		if (parseResult.isSuccessful() && parseResult.getResult().isPresent()) {
			CompilationUnit cu = parseResult.getResult().get();

			MethodAnnotationVisitor visitor = new MethodAnnotationVisitor(methodName, annotationName);
			cu.accept(visitor, null);
			return visitor.getAnnotationValue();
		} else {
			logger.log(Level.SEVERE, "Failed to parse the file: {0}", filePath);
			return "false";
		}
	}

	private static class InterfaceAnnotationVisitor extends VoidVisitorAdapter<Void> {
		private final String annotationName;
		private boolean annotated;

		public InterfaceAnnotationVisitor(String annotationName) {
			this.annotationName = annotationName;
			this.annotated = false;
		}

		@Override
		public void visit(ClassOrInterfaceDeclaration n, Void arg) {
			if (n.isInterface()) {
				logger.log(Level.INFO, "Checking interface: {0}", n.getName());
				n.getAnnotations().forEach(annotation -> {
					if (annotation.getName().asString().equals(annotationName)) {
						annotated = true;
						logger.log(Level.INFO, "Annotation {0} found on interface {1}",
								new Object[] { annotationName, n.getName() });
					}
				});
			}
			super.visit(n, arg);
		}

		public boolean isAnnotated() {
			return annotated;
		}
	}

	public static boolean isMethodPresent(String filePath, String methodName) throws IOException {
		logger.log(Level.INFO, "Checking if method {0} is present in interface in file {1}",
				new Object[] { methodName, filePath });
		FileInputStream in = new FileInputStream(filePath);
		ParseResult<CompilationUnit> parseResult = new JavaParser().parse(in);

		if (parseResult.isSuccessful() && parseResult.getResult().isPresent()) {
			CompilationUnit cu = parseResult.getResult().get();

			MethodPresenceVisitor visitor = new MethodPresenceVisitor(methodName);
			cu.accept(visitor, null);
			return visitor.isMethodPresent();
		} else {
			logger.log(Level.SEVERE, "Failed to parse the file: {0}", filePath);
			return false;
		}
	}

	private static class MethodAnnotationVisitor extends VoidVisitorAdapter<Void> {
		private final String methodName;
		private final String annotationName;
		private String annotationValue = "false";

		public MethodAnnotationVisitor(String methodName, String annotationName) {
			this.methodName = methodName;
			this.annotationName = annotationName;
		}

		@Override
		public void visit(ClassOrInterfaceDeclaration n, Void arg) {
			if (n.isInterface()) {
				logger.log(Level.INFO, "Checking methods in interface: {0}", n.getName());
				n.getMethods().forEach(method -> {
					if (method.getName().asString().equals(methodName)) {
						logger.log(Level.INFO, "Found method: {0}", methodName);
						method.getAnnotations().forEach(annotation -> {
							if (annotation.getName().asString().equals(annotationName)) {
								logger.log(Level.INFO, "Annotation {0} found on method {1}",
										new Object[] { annotationName, methodName });
								if (annotation instanceof SingleMemberAnnotationExpr) {
									SingleMemberAnnotationExpr singleMember = (SingleMemberAnnotationExpr) annotation;
									annotationValue = singleMember.getMemberValue().toString();
									logger.log(Level.INFO, "Annotation value: {0}", annotationValue);
								} else if (annotation instanceof NormalAnnotationExpr) {
									NormalAnnotationExpr normalAnnotation = (NormalAnnotationExpr) annotation;
									for (MemberValuePair pair : normalAnnotation.getPairs()) {
										annotationValue = pair.getValue().toString();
										logger.log(Level.INFO, "Annotation value: {0}", annotationValue);
									}
								}
							}
						});
					}
				});
			}
			super.visit(n, arg);
		}

		public String getAnnotationValue() {
			return annotationValue;
		}
	}

	private static class MethodPresenceVisitor extends VoidVisitorAdapter<Void> {
		private final String methodName;
		private boolean methodPresent;

		public MethodPresenceVisitor(String methodName) {
			this.methodName = methodName;
			this.methodPresent = false;
		}

		@Override
		public void visit(ClassOrInterfaceDeclaration n, Void arg) {
			if (n.isInterface()) {
				logger.log(Level.INFO, "Checking methods in interface: {0}", n.getName());
				n.getMethods().forEach(method -> {
					if (method.getName().asString().equals(methodName)) {
						logger.log(Level.INFO, "Found method: {0}", methodName);
						methodPresent = true;
					}
				});
			}
			super.visit(n, arg);
		}

		public boolean isMethodPresent() {
			return methodPresent;
		}
	}
}