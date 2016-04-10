# UML-Parser
A Parser which converts Java Source Code into a  UML Diagram.

<b>Tools and Libraries Used</b>

<b><i>1. Programming Languages</i></b>

● Java: Version 1.8.0_74 
● Java(TM) SE Runtime Environment (build 1.8.0_74­b02)  
● Java HotSpot(TM) 64­Bit Server VM (build 25.74­b02, mixed mode) 
 
<b><i>2. Tools</i></b>

● <b>Eclipse IDE:</b>
 Used to write, compile, and test project code. The executable jar file can 
be conveniently tested using Eclipse IDE at all stages of creation, compilation and 
debug. 

● <b>Apache Maven:</b>
 It is a build automation tool. There are two ways in which Maven can be 
used to build software. One aspect is that Maven can be used to describe how software 
is built and the second is that it also describes what the dependencies are. Maven has a 
central repository in which all its components and dependencies are published. These 
can be downloaded easily when needed. 

● <b>Java Parser GitHub Code:</b>  
<ul>
<li>https://github.com/javaparser/javaparser</li> 
<li>Jar File: Javaparser­core­2.3.0.jar</li> 
<li>This code is used to parse Java Source Code. Like Maven Central, it contains 
project binaries. Maven Project has been created, and dependencies of this tool 
have been added to POM.xml file</li>
</ul>

● <b>Plant UML</b> 
<ul>
<li>http://plantuml.com/ </li>
<li>Jar File : plantuml.jar </li>
<li>Java Source Code UML diagram is generated using this tool. Graphviz software 
is required to use this tool and generate diagrams. Graphviz must be installed in 
the default directory of your system. 
c:\Program Files\GraphvizX.XX (Windows) or /usr/bin/do </li>
</ul>

<b>Note : All the above libraries are included in the project. You dont need to download anything.</b>

<b><i>How to run this project via Command Line?</i></b>

1. 1. Install Graphviz in the default directory of your machine. This is c:\Program 
Files\GraphvizX.XX (Windows) or /usr/bin/dot (Ubuntu). Graphviz is used to generate UML 
diagrams

<b>for Ubuntu</b>

2. Naviage to the folder with the file runTestCases.sh inside testcases.

3. ./runTestCases.sh to run the testcases.

4. You can see the output in folders result* for each testcase. 

<b>For Windows</b>

2. Navigate to testcases folder

3. Run the following commands
	<ul>
	<li> java -jar ../UMLParser.jar test1 ../result1/output1</li>
	<li> java -jar ../UMLParser.jar test2 ../result2/output2</li>
	<li> java -jar ../UMLParser.jar test3 ../result3/output3</li>
	<li> java -jar ../UMLParser.jar test4 ../result4/output4</li>
	<li> java -jar ../UMLParser.jar test5 ../result5/output5</li>
	</ul>
