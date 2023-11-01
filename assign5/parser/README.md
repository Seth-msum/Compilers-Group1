Note that assign4 parser cannot just me moved over here with out modification to extend ASTVisitor and not implement. 

A symbol table within the AST needs to store:
- variable descriptors
    - variable types
    - var names
    - var values...

