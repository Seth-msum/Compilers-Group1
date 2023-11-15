package assign4.parser;

import assign4.visitor.* ;

public class CompilationUnit extends Node {

    //Node ast ;
    //public AssignmentNode assign
    public BlockStatementNode block ;

    public CompilationUnit () {

    }

    public CompilationUnit (BlockStatementNode assign) {

        this.block = block ;
    }
    
    public void accept(ASTVisitor v) {

        v.visit(this) ;
    }
}
