# UML-Parser
A Parser which converts Java Source Code into a  UML Diagram.

Requirements

1. Programming Languages

● Java: Version 1.8.0_74 
● Java(TM) SE Runtime Environment (build 1.8.0_74­b02)  
● Java HotSpot(TM) 64­Bit Server VM (build 25.74­b02, mixed mode) 
 
2. Tools 

● Eclipse IDE:
 Used to write, compile, and test project code. The executable jar file can 
be conveniently tested using Eclipse IDE at all stages of creation, compilation and 
debug. 

● Apache Maven:
 It is a build automation tool. There are two ways in which Maven can be 
used to build software. One aspect is that Maven can be used to describe how software 
is built and the second is that it also describes what the dependencies are. Maven has a 
central repository in which all its components and dependencies are published. These 
can be downloaded easily when needed. 

● Java Parser GitHub Code:  
○ https://github.com/javaparser/javaparser 
○ Jar File: Javaparser­core­2.3.0.jar 
○ This code is used to parse Java Source Code. Like Maven Central, it contains 
project binaries. Maven Project has been created, and dependencies of this tool 
have been added to POM.xml file 

● Plant UML 
○ http://plantuml.com/ 
○ Jar File : plantuml.jar 
○ Java Source Code UML diagram is generated using this tool. Graphviz software 
is required to use this tool and generate diagrams. Graphviz must be installed in 
the default directory of your system. 
c:\Program Files\GraphvizX.XX (Windows) or /usr/bin/do

Note : All the above libraries are included in the project. You dont need to download anything.

How to run this project via Command Line?

1. 1. Install Graphviz in the default directory of your machine. This is c:\Program 
Files\GraphvizX.XX (Windows) or /usr/bin/dot (Ubuntu). Graphviz is used to generate UML 
diagrams

2. Naviage to the folder with the file runTestCases.sh inside testcases.

3. ./runTestCases.sh to run the testcases.

4. You can see the output in folders result* for each testcase. 
