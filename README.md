# Compilers-Group1

# To compile:
This command to clean all left up classes:
```
rm -r assign4/*.class assign4/lexer/*.class assign4/parser/*.class assign4/pretty/*.class assign4/visitor/*.class
```
To compile:
```
javac assign4/Main.java
```
To run:
```
java assign.Main
```
Note that if you run and compile from just outside the assign4 directory, you dont have to mess with classpath.
Also have the input just outside the assign4 folder as well if it doesnt return anything after running.


- Parser has a look variable that needs to be fixed

- Parser has different visit methods.

- missig visit fucntion in the compilationUnit

### Judahs edits/notes
- I added BlockStatementNode
    - It refrences an identifier node but Im not sure what that is
- Created identifierNode and it seems similar or to replace literalNode
    - based of lab05 - time 13:36 - LiteralNode is not used.
- I commented out the visit fuctions in the Parser
- It seems that the parser is to have the same functions as the ASTVisitor.
- I added to the instruction to compile it.

 - The parser visit function for block statemts used to assume it only stored an identifier block -> {id;}. Now it assumes block -> {assign}.
 - The assignment was added to recognise: assign = id=id;
 