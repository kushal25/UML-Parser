package umlParser;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.StringTokenizer;
import java.util.regex.Pattern;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ParseException;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.ConstructorDeclaration;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.ModifierSet;
import com.github.javaparser.ast.type.ClassOrInterfaceType;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;

import umlParser.PlantUmlGenerator;

public class JavaUmlParser {

	static FileInputStream instream;
	static CompilationUnit cUnit;
	static String[] partsArray;
	static ArrayList<String> primitive_var = new ArrayList<String>();
	static ArrayList<String> primitive_var_method = new ArrayList<String>();
	static ArrayList<String> ref_var = new ArrayList<String>();
	static HashMap<String, Integer> var_index = new HashMap<String, Integer>();
	static ArrayList<Integer> index_list = new ArrayList<Integer>();
	static ArrayList<String> interface_classes = new ArrayList<String>();
	static ArrayList<String> java_file_name = new ArrayList<String>();
	static ArrayList<String> interface_class_name = new ArrayList<String>();
	static Multimap<String, String> class_obj = ArrayListMultimap.create();
	static ArrayList<String> var = new ArrayList<String>();
	static FileWriter output_file;
	static ArrayList<String> methods_name = new ArrayList<String>();
	static ArrayList<String> constructor_name = new ArrayList<String>();

	public static void main(String[] args) throws IOException, ParseException {

		String input_dir_name = args[0];		
		System.out.println("input Directory Name " + input_dir_name);
		String output_file_name = args[1];
		System.out.println("Output File Name " + output_file_name);
		String dir_name = input_dir_name;
		String outFile = (dir_name + "/" + output_file_name + ".txt");

		PlantUmlGenerator diagramUML = new PlantUmlGenerator();
		File dir = new File(dir_name);		

		try {
			File oFile = new File(outFile);
			output_file = new FileWriter(oFile);

			output_file.write("@startuml");
			output_file.append("\r\n");

		} catch (IOException e) {
			e.printStackTrace();
		}

		String className;
		File[] fList = dir.listFiles();

		for (File file : fList) {
			if (file.isFile()) {
				className = file.getName();
				partsArray = className.split(Pattern.quote("."));
				if (partsArray[1].equals("java")) {
					java_file_name.add(partsArray[0]);
					String filename = dir_name + "/" + file.getName();
					try {
						instream = new FileInputStream(filename);
						cUnit = JavaParser.parse(instream);
					} catch (Exception e) {
						e.printStackTrace();
					}

					finally {
						try {
							instream.close();

						} catch (IOException e) {

							e.printStackTrace();
						}
					}
					new ClassOrInterfaceFirstVisitor().visit(cUnit, null);

				}
			}

		}

		for (File file : fList) {
			if (file.isFile()) {

				className = file.getName();

				partsArray = className.split(Pattern.quote("."));
				if (partsArray[1].equals("java") == false) {

				} else {
					String part1 = partsArray[0];

					String filename = dir_name + "/" + file.getName();

					try {
						instream = new FileInputStream(filename);
						cUnit = JavaParser.parse(instream);
					} catch (Exception e) {
						e.printStackTrace();
					}

					finally {
						try {
							instream.close();

						} catch (IOException e) {
							e.printStackTrace();
						}
					}
					System.out.println("class " + partsArray[0]);
					new FieldVisitor().visit(cUnit, null);
					new ConstructorTypeVisitor().visit(cUnit, null);
					new MethodVisitor().visit(cUnit, null);
					new ClassOrInterfaceVisitor().visit(cUnit, null);

					writeFile();

				}
			}
		}
		writeFileReferences();
		output_file.write("@enduml");
		output_file.close();
		diagramUML.createUML(outFile);

	}

	/*
	 *  Method to retrieve Classes and Interfaces
	 */
	private static class ClassOrInterfaceVisitor extends VoidVisitorAdapter { 
																				

		public void visit(ClassOrInterfaceDeclaration m, Object arg) {
			List<ClassOrInterfaceType> classType;
			List<String> classValue = null;

			if (m.getExtends() != null) {

				classType = m.getExtends();

				for (int i = 0; i < classType.size(); i++) {
					String text = classType.get(i).toString().replace("[", "").replace("]", "");

					System.out.println(text + " <|-- " + partsArray[0]);
					String text1 = text + " <|-- " + partsArray[0];
					ref_var.add(text1);
				}

			}

			if (m.getImplements() != null) {

				classType = m.getImplements();

				for (int i = 0; i < classType.size(); i++) {
					String text = classType.get(i).toString().replace("[", "").replace("]", "");
					interface_classes.add(text);

					System.out.println(text + " <|.. " + partsArray[0]);
					String text1 = text + " <|.. " + partsArray[0];
					ref_var.add(text1);

				}
			}
		}

	}

	/*
	 * Fetch all Interfaces
	 */	
	private static class ClassOrInterfaceFirstVisitor
			extends VoidVisitorAdapter { 

		public void visit(ClassOrInterfaceDeclaration m, Object arg) {
			List<ClassOrInterfaceType> classType;
			List<String> classValue = null;

			if (m.getImplements() != null) {

				classType = m.getImplements();

				StringTokenizer st = new StringTokenizer(classType.toString(), "[,] ");

				while (st.hasMoreTokens()) {
					var.add(st.nextToken().toString());
				}

				interface_class_name.addAll(var);
			}
			var.clear();

		}
	}

	/*
	 * Fetch all variables in the Class
	 */
	private static class FieldVisitor extends VoidVisitorAdapter { 
																	

		public void visit(FieldDeclaration m, Object arg) {

			int modifier = 0;
			int number = 0;
			String flagCollection = "N";
			Boolean isObject = false;
			String modifierSign = null;
			String variableClass = null;
			String classObjectExist = "N";
			ModifierSet ms = null;

			if (m.getModifiers() != 0)
				modifier = m.getModifiers();

			String variableType = m.getType().toString();
			StringTokenizer st = new StringTokenizer(variableType, "<> ");

			while (st.hasMoreTokens()) {
				var.add(st.nextToken().toString());
			}
			if (m.getType() != null) {

				for (String s : java_file_name) {
					classObjectExist = "N";
					if (s.equals(var.get(0))) {
						isObject = true;
						variableClass = var.get(0);
						if (class_obj.containsKey(var.get(0))) {
							List<String> classValue = (List<String>) class_obj.get((String) var.get(0));

							for (int i = 0; i <= classValue.size() - 1; i++) {
								if (partsArray[0].equals(classValue.get(i))) {
									classObjectExist = "Y";
									number = number + 1;

								}
							}

						}
						if (classObjectExist == "N") {
							class_obj.put(partsArray[0], var.get(0));
							System.out.println(partsArray[0] + " \"0\"" + "--" + "\"1\" " + var.get(0));

							String text = partsArray[0] + " \"0\"" + "--" + "\"1\" " + var.get(0);
							ref_var.add(text);
							var_index.put(partsArray[0] + var.get(0), ref_var.size() - 1);
						}

					} else if (s.equals(var.get(var.size() - 1))) {
						variableClass = var.get(1);
						isObject = true;
						if (class_obj.containsKey(partsArray[0])) {
							List<String> classValue = (List<String>) class_obj.get((String) partsArray[0]);

							for (int i = 0; i < classValue.size(); i++) {
								if (var.get(1).equals(classValue.get(i))) {
									classObjectExist = "Y";
									flagCollection = "Y";
									number = number + 1;
								}
							}

						}
						if (classObjectExist == "N") {
							class_obj.put(partsArray[0], var.get(1));
							System.out.println(partsArray[0] + " \"0\"" + "--" + "\"*\" " + var.get(1));
							String text = partsArray[0] + " \"0\"" + "--" + "\"*\" " + var.get(1);
							ref_var.add(text);
							var_index.put(partsArray[0] + var.get(1), ref_var.size() - 1);
						}

					}

				}
				if (isObject == false) {
					if (m.getModifiers() != 0) {

						switch (modifier) {
						case 1:
							modifier = 1;
							modifierSign = "+";
							break;
						case 2:
							modifier = 2;
							modifierSign = "-";
							break;
						case 4:
							modifier = 4;
							modifierSign = "#";
							break;

						}
					}

					String text1;
					String text = m.getVariables().toString().replace("[", "").replace("]", "");
					if (ms.isStatic(modifier)) {
						text1 = modifierSign + " " + "{static}" + " " + text + " : " + m.getType().toString();

					} else {
						text1 = modifierSign + " " + text + " : " + m.getType().toString();
					}

					if (modifier == 1 || modifier == 2) {
						primitive_var.add(text1);
					}

				}

			}
			if (number > 0) {
				int index = 0;

				index = var_index.get(variableClass + partsArray[0]);
				index_list.add(index);
				String text = ref_var.get(index);
				StringTokenizer st1 = new StringTokenizer(text, " \"\"");
				int i = 0;
				String tokenText = null;
				while (st1.hasMoreTokens()) {
					i = 1 + i;
					if (i == 4) {
						tokenText = st1.nextToken();
					}
					st1.nextToken();

				}
				String text1 = variableClass + " \"" + "1" + "\"" + " -- " + "\"" + tokenText + "\" " + partsArray[0];

				ref_var.add(text1);
			}

			isObject = false;
			number = 0;
			var.clear();

		}

	}

	/*
	 * Fetch all methods in the Class
	 */
	private static class MethodVisitor extends VoidVisitorAdapter {  
																	

		@Override
		public void visit(MethodDeclaration m, Object arg) {
			ModifierSet ms = null;

			Boolean isObject = false;
			String classObjectExist = "N";
			String varType = m.getParameters().toString();
			StringTokenizer str_token = new StringTokenizer(varType, "[] ");

			while (str_token.hasMoreTokens()) {
				var.add(str_token.nextToken().toString());

			}

			if (m.getParameters().isEmpty() == false) {
				isObject = verifyDependency();
				if (isObject == false) {
					String text = m.getType().toString() + " "
							+ m.getParameters().toString().replace("[", "").replace("]", "");
					primitive_var_method.add(text);
					System.out.println(text);
				}
			}

			isObject = false;
			String returnPart[];
			returnPart = m.getChildrenNodes().toString().split(Pattern.quote(","));
			String returnType = returnPart[0].replace("[", "");
			returnType = returnType.replace("]", "");

			String methodName = m.getName();

			int modifier = m.getModifiers();
			String sign = null;
			switch (modifier) {
			case 1:
				modifier = 1;
				sign = "+";
				break;
			case 2:
				modifier = 2;
				sign = "-";
				break;
			case 4:
				modifier = 4;
				sign = "#";
				break;

			}
			String modifierVariable[];
			if (ms.isAbstract(modifier) || ms.isStatic(modifier) || ms.isFinal(modifier) || modifier == 0) {

				modifierVariable = m.getDeclarationAsString().split(Pattern.quote(" "));
				if (modifierVariable[0].equals("public") || modifier == 0)
					sign = "+";
				if (modifierVariable[0].equals("protected"))
					sign = "-";
				if (modifierVariable[0].equals("private"))
					sign = "#";

			}

			String parameters = "";
			int flag = 0;
			String[] splitVariable;
			String tempString;
			String mergeVariable;

			if (m.getParameters().isEmpty() == false) {
				parameters = var.get(1) + " : " + var.get(0);
			}
			if ((methodName.substring(0, 3).equals("get") || methodName.substring(0, 3).equals("set"))
					& modifier == 1) {

				for (int i = 0; i <= primitive_var.size() - 1; i++) {

					splitVariable = primitive_var.get(i).split(Pattern.quote(" "));

					if (methodName.substring(3).equalsIgnoreCase(splitVariable[1])) {

						flag = 1;
						tempString = primitive_var.get(i).substring(1);
						mergeVariable = "+" + tempString;
						primitive_var.set(i, mergeVariable);

					}

				}
				if (flag == 0) {
					String text = sign + " " + m.getName() + "( " + parameters + " )" + " : " + returnType;
					methods_name.add(text);
				}

				flag = 0;

			} else if (ms.isStatic(modifier)) {
				String text = sign + " " + "{static}" + " " + m.getName() + "( " + parameters + " )" + " : "
						+ returnType;
				methods_name.add(text);
			} else if (ms.isPublic(modifier)) {
				String text = sign + " " + m.getName() + "( " + parameters + " )" + " : " + returnType;
				methods_name.add(text);
			}
			var.clear();
			insideDependency(m.getChildrenNodes().toString());
		}

	}

	/*
	 * Fetch the constructor of the class and dependency
	 */
	private static class ConstructorTypeVisitor extends VoidVisitorAdapter { 
																				

		public void visit(ConstructorDeclaration cd, Object arg) {

			ArrayList<String> var_constructor = new ArrayList<String>();
			String classObjectExist = "N";
			boolean isObject = false;

			String parameters = "";
			int modifier = cd.getModifiers();
			String sign = null;
			switch (modifier) {
			case 1:
				modifier = 1;
				sign = "+";
				break;
			case 2:
				modifier = 2;
				sign = "-";
				break;
			case 4:
				modifier = 4;
				sign = "#";
				break;

			}
			if (cd.getParameters().isEmpty() == false) {
				String varType = cd.getParameters().toString();
				StringTokenizer strToken = new StringTokenizer(varType, "[] ");
				while (strToken.hasMoreTokens()) {
					var.add(strToken.nextToken().toString());
				}
				parameters = var.get(1) + " : " + var.get(0);
			}

			if (cd.getParameters().isEmpty() == false) {
				verifyDependency();
			}

			String text = sign + " " + cd.getName() + "(" + parameters + ")";
			constructor_name.add(text);

			var_constructor.clear();

		}
	}

	/*
	 * Write all the variables , methods , dependencies in text file which act a input to UML generator
	 */
	private static void writeFile() { 
										 
		try {

			output_file.write("class " + partsArray[0] + " {");
			output_file.append("\r\n");
			for (String str : primitive_var) {
				output_file.write(str);
				output_file.append("\r\n");
			}
			for (String str : constructor_name) {
				output_file.write(str);
				output_file.append("\r\n");
			}
			for (String str : methods_name) {
				output_file.write(str);
				output_file.append("\r\n");
			}
			output_file.write("}");
			output_file.append("\r\n");

		} catch (IOException e) {
			e.printStackTrace();
		}
		primitive_var.clear();
		methods_name.clear();
		constructor_name.clear();
	}
	
	/*
	 * Fetch the dependency inside the methods
	 */

	public static void insideDependency(String statement) { 
																	
		ArrayList<String> inside_statement = new ArrayList<String>();
		ArrayList<String> var_inside = new ArrayList<String>();

		StringTokenizer st = new StringTokenizer(statement, "{;");

		while (st.hasMoreTokens()) {
			inside_statement.add(st.nextToken().toString());
		}

		for (int i = 1; i < inside_statement.size() - 1; i++) {
			String stw = inside_statement.get(i);
			StringTokenizer st1 = new StringTokenizer(stw, " ");
			while (st1.hasMoreTokens()) {
				var_inside.add(st1.nextToken());
			}

			for (int j = 0; j <= var_inside.size() - 1; j++) {
				var.add(var_inside.get(j));

				if (j <= var_inside.size() - 2) {
					var.add(var_inside.get(j + 1));
					verifyDependency();
				}
				var.clear();
			}

			var_inside.clear();

		}

	}
	
	/*
	 * Verify multiplicity
	 */

	private static void writeFileReferences() { // Write the dependencies in file
												
		if (interface_classes.isEmpty() == false) {
			for (String str : interface_classes) {
				try {
					output_file.write("Interface " + str);
					output_file.append("\r\n");
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

		}
		if (index_list.isEmpty() == false) {

			int j = index_list.get(0).intValue();
			ref_var.remove(j);
			int i = 1;
			do {

				j = index_list.get(i).intValue() - i;
				ref_var.remove(j);
				i = 1 + i;
			} while (i != index_list.size());

		}
		deleteDependency();
		HashSet<String> hashset = new HashSet<String>(ref_var);
		for (String str : hashset) {

			try {
				output_file.write(str);
				output_file.append("\r\n");
			} catch (IOException e) {
				e.printStackTrace();
			}

		}

	}

	private static void deleteDependency() { // Remove the unwanted or duplicate
												// dependencies
		String r1 = "N";
		String r2 = "N";
		ArrayList<String> parse_var = new ArrayList<String>();
		for (int i = 0; i < ref_var.size() - 1; i++) {
			StringTokenizer st = new StringTokenizer(ref_var.get(i), " ");
			while (st.hasMoreTokens()) {
				parse_var.add(st.nextToken().toString());
			}
			for (String s_interference : interface_class_name) {

				if (parse_var.get(1).equals("..>")) {
					if (parse_var.get(0).equals(s_interference)) {
						r1 = "Y";
					}
					if (parse_var.get(2).equals(s_interference)) {
						r2 = "Y";
					}

				}
			}
			if ((r1.equals("Y") & r2.equals("Y"))
					|| ((r1.equals("N") & r2.equals("N"))) & parse_var.get(1).equals("..>")) {
				ref_var.remove(i);
				i = i - 1;

			}
			parse_var.clear();
			r1 = "N";
			r2 = "N";

		}
	}

	public static Boolean verifyDependency() { 
		String objExists = "N";
		boolean is_obj = false;

		for (String s : java_file_name) {
			objExists = "N";

			if (s.equals(var.get(0))) {
				is_obj = true;
				if (class_obj.containsKey(var.get(0))) {
					List<String> class_val = (List<String>) class_obj.get((String) var.get(0));

					for (int i = 0; i <= class_val.size() - 1; i++) {
						if (partsArray[0].equals(class_val.get(i))) {
							objExists = "Y";
						}
					}

				}

				if (objExists.equals("N")) {
					class_obj.put(partsArray[0], var.get(0));
					System.out.println(partsArray[0] + " ..> " + var.get(0) + " : uses");
					String text = partsArray[0] + " ..> " + var.get(0) + " : uses";
					ref_var.add(text);
					var_index.put(partsArray[0] + var.get(0), ref_var.size() - 1);
				}

			} else if (s.equals(var.get(var.size() - 1))) {

				is_obj = true;
				if (class_obj.containsKey(var.get(1))) {
					List<String> classList = (List<String>) class_obj.get((String) var.get(1));

					for (int i = 0; i < classList.size(); i++) {
						if (partsArray[0].equals(classList.get(i))) {
							objExists = "Y";
						}
					}

				}
				if (objExists.equals("N")) {
					class_obj.put(partsArray[0], var.get(1));
					System.out.println(partsArray[0] + " ..> " + var.get(1) + " : uses");
					String doc = partsArray[0] + " ..> " + var.get(1) + " : uses";
					ref_var.add(doc);
					ref_var.add(doc);
					var_index.put(partsArray[0] + var.get(1), ref_var.size() - 1);
				}

			}

		}
		return is_obj;

	}
																																																																													
}
