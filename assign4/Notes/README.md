# Notes
These are just notes that do not belonn in the main README.
## Judahs edits/notes

- Parser has a look variable that needs to be fixed

- Parser has different visit methods.

- missig visit fucntion in the compilationUnit

- I added BlockStatementNode
    - It refrences an identifier node but Im not sure what that is
- Created identifierNode and it seems similar or to replace literalNode
    - based of lab05 - time 13:36 - LiteralNode is not used.
- I commented out the visit fuctions in the Parser
- It seems that the parser is to have the same functions as the ASTVisitor.
- I added to the instruction to compile it.

 - The parser visit function for block statemts used to assume it only stored an identifier block -> {id;}. Now it assumes block -> {assign}.
 - The assignment was added to recognise: assign = id=id;

### How I add a new nodes to the compiler.
- add a visit class with accept() calls only for the node
- create the node.
- add the new node to the relative visit functions in parser.
- fix the nodes that dont like the new node as their values.
    - ex: changed right IdentifierNode to AdditionNode in the AssignmentNode.
- added the visit function to the Parser for the new node.
